package com.github.sib_energy_craft.farming.feeding_station.block.entity;

import com.github.sib_energy_craft.energy_api.Energy;
import com.github.sib_energy_craft.energy_api.items.ChargeableItem;
import com.github.sib_energy_craft.farming.feeding_station.FeedingStationMode;
import com.github.sib_energy_craft.farming.feeding_station.FeedingStationTypedProperties;
import com.github.sib_energy_craft.farming.feeding_station.block.AbstractFeedingStationBlock;
import com.github.sib_energy_craft.machines.block.entity.AbstractEnergyMachineBlockEntity;
import com.github.sib_energy_craft.machines.block.entity.EnergyMachineInventoryType;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * @author sibmaks
 * @since 0.0.4
 */
public abstract class AbstractFeedingStationBlockEntity<T extends AbstractFeedingStationBlock>
        extends AbstractEnergyMachineBlockEntity<T> {

    private final Box feedingArea;
    @Setter
    private FeedingStationMode feedingStationMode;

    protected AbstractFeedingStationBlockEntity(@NotNull BlockEntityType<?> entityType,
                                                @NotNull BlockPos pos,
                                                @NotNull BlockState state,
                                                @NotNull T block) {
        super(entityType, pos, state, block, 9, 0, 1);
        int radius = block.getRadius();
        this.feedingArea = new Box(pos.add(-radius, -radius, -radius), pos.add(radius, radius, radius));
        this.feedingStationMode = FeedingStationMode.ADULT_ONLY;
        this.energyMachinePropertyMap.add(FeedingStationTypedProperties.MODE, () -> feedingStationMode.ordinal());
    }

    @Override
    public void readNbt(@NotNull NbtCompound nbt) {
        super.readNbt(nbt);
        var stationModeCode = nbt.getString("FeedingStationMode");
        this.feedingStationMode = FeedingStationMode.valueOf(stationModeCode);
    }

    @Override
    protected void writeNbt(@NotNull NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putString("FeedingStationMode", feedingStationMode.name());
    }

    @Override
    public boolean isValid(int slot, @NotNull ItemStack stack) {
        var slotType = inventory.getType(slot);
        if (slotType == EnergyMachineInventoryType.OUTPUT) {
            return false;
        }
        if(slotType == EnergyMachineInventoryType.CHARGE) {
            var item = stack.getItem();
            if(item instanceof ChargeableItem chargeableItem) {
                return chargeableItem.hasEnergy(stack);
            }
            return false;
        }
        return true;
    }

    @Override
    protected boolean canProcess(int process,
                                 @NotNull World world,
                                 @NotNull BlockPos pos,
                                 @NotNull BlockState state,
                                 @NotNull Map<String, Object> processContext) {
        if (world.isClient || !(world instanceof ServerWorld serverWorld)) {
            return false;
        }

        var sourceInventory = inventory.getInventory(EnergyMachineInventoryType.SOURCE);
        if(sourceInventory == null) {
            return false;
        }
        var filled = state.get(AbstractFeedingStationBlock.FILLED);
        var nowFilled = !sourceInventory.isEmpty();
        if(filled != nowFilled) {
            state = state.with(AbstractFeedingStationBlock.FILLED, nowFilled);
            world.setBlockState(pos, state, Block.NOTIFY_ALL);
        }
        if(sourceInventory.isEmpty()) {
            return false;
        }

        var animals = serverWorld.getEntitiesByClass(
                AnimalEntity.class,
                feedingArea,
                this::filterAnimal
        );

        if (animals.isEmpty()) {
            return false;
        }
        var random = world.getRandom();
        int index = random.nextInt(animals.size());
        var animal = animals.get(index);

        for (int i = 0; i < sourceInventory.size(); i++) {
            var inventoryStack = sourceInventory.getStack(i);
            if(inventoryStack.isEmpty()) {
                continue;
            }
            if(animal.isBreedingItem(inventoryStack)) {
                processContext.put("Animal", animal);
                processContext.put("BreedingItemStack", inventoryStack);
                return true;
            }
        }
        return false;
    }

    private boolean filterAnimal(AnimalEntity animal) {
        switch (feedingStationMode) {
            case ALL -> {
                return animal.canEat() && animal.getBreedingAge() <= 0;
            }
            case ADULT_ONLY -> {
                return animal.canEat() && animal.getBreedingAge() == 0;
            }
            case BABY_ONLY -> {
                return animal.canEat() && animal.isBaby();
            }
        }
        return false;
    }

    @Override
    protected boolean tickProcess(int process,
                                  @NotNull World world,
                                  @NotNull BlockPos pos,
                                  @NotNull BlockState state,
                                  @NotNull Map<String, Object> processContext) {
        var animal = (AnimalEntity) processContext.get("Animal");
        var breedingItemStack = (ItemStack) processContext.get("BreedingItemStack");

        breedingItemStack.decrement(1);
        if(animal.isBaby()) {
            int breadingAge = animal.getBreedingAge();
            animal.growUp(AnimalEntity.toGrowUpAge(-breadingAge), true);
        } else {
            animal.lovePlayer(null);
        }

        return true;
    }

    @Override
    protected void onProcessFinished(int process,
                                     @NotNull World world,
                                     @NotNull BlockPos pos,
                                     @NotNull BlockState state,
                                     @NotNull Map<String, Object> processContext) {
    }

    @Override
    public @NotNull Energy getEnergyUsagePerTick() {
        return block.getEnergyUsagePerTick();
    }
}

