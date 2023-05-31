package com.github.sib_energy_craft.agronomy.harvester.block.entity;

import com.github.sib_energy_craft.agronomy.harvester.block.AbstractHarvesterBlock;
import com.github.sib_energy_craft.energy_api.Energy;
import com.github.sib_energy_craft.energy_api.items.ChargeableItem;
import com.github.sib_energy_craft.machines.block.entity.AbstractEnergyMachineBlockEntity;
import com.github.sib_energy_craft.machines.block.entity.EnergyMachineEvent;
import com.github.sib_energy_craft.machines.block.entity.EnergyMachineInventoryType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.util.Objects;

/**
 * @since 0.0.2
 * @author sibmaks
 */
public abstract class AbstractHarvesterBlockEntity<T extends AbstractHarvesterBlock>
        extends AbstractEnergyMachineBlockEntity<T> {
    public static final int TOOL_SLOT = 0;

    protected Vector2i harvestPos;
    protected int delta;


    protected AbstractHarvesterBlockEntity(@NotNull BlockEntityType<?> entityType,
                                           @NotNull BlockPos pos,
                                           @NotNull BlockState state,
                                           @NotNull T block) {
        super(entityType, pos, state, block, 1, 9, 1);
        int radius = block.getRadius();
        this.harvestPos = new Vector2i(-radius, -radius);
        this.delta = 1;
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

    public static <E extends AbstractHarvesterBlockEntity<?>> void harvestTick(World world,
                                                                               BlockPos pos,
                                                                               BlockState state,
                                                                               E entity) {
        if (world.isClient || !(world instanceof ServerWorld serverWorld)) {
            return;
        }
        var requiredEnergy = entity.getEnergyUsagePerTick();
        var hasEnergy = entity.energyContainer.hasAtLeast(requiredEnergy);
        var changed = false;
        var working = entity.working;
        entity.working = false;

        if (entity.charge()) {
            changed = true;
        }

        if (!entity.energyContainer.hasAtLeast(requiredEnergy)) {
            entity.updateState(working, state, world, pos, changed);
            entity.dispatch(EnergyMachineEvent.ENERGY_NOT_ENOUGH);
            return;
        }
        boolean harvest = false;
        var toolStack = entity.inventory.getStack(EnergyMachineInventoryType.SOURCE, TOOL_SLOT);

        var neighborPos = pos
                .up()
                .offset(Direction.Axis.X, entity.harvestPos.x)
                .offset(Direction.Axis.Z, entity.harvestPos.y);
        entity.harvestPos.x += entity.delta;
        int radius = entity.block.getRadius();
        if (entity.harvestPos.x > radius || entity.harvestPos.x < -radius) {
            entity.harvestPos.x -= entity.delta;
            entity.harvestPos.y++;
            entity.delta *= -1;
            if (entity.harvestPos.y > radius) {
                entity.harvestPos.x = -radius;
                entity.harvestPos.y = -radius;
            }
        }

        var neighborState = world.getBlockState(neighborPos);
        var neighborBlock = neighborState.getBlock();
        if (neighborBlock instanceof CropBlock cropBlock) {
            var ageProperty = cropBlock.getAgeProperty();
            var age = neighborState.get(ageProperty);
            var maxAge = ageProperty.getValues().stream()
                    .max(Integer::compareTo)
                    .orElse(0);
            if (Objects.equals(age, maxAge)) {
                var droppedStacks = neighborState.getDroppedStacks(new LootContext.Builder(serverWorld)
                        .parameter(LootContextParameters.ORIGIN, neighborPos.toCenterPos()).random(world.getRandom())
                        .parameter(LootContextParameters.TOOL, toolStack)
                );
                boolean canInsert = true;
                var neighborItem = neighborBlock.asItem();
                for (var stack : droppedStacks) {
                    if(stack.isEmpty()) {
                        continue;
                    }
                    if(stack.getItem() == neighborItem) {
                        stack.decrement(1);
                    }
                    if (!entity.inventory.canInsert(EnergyMachineInventoryType.OUTPUT, stack)) {
                        canInsert = false;
                    }
                }
                if (canInsert) {
                    if (entity.energyContainer.subtract(requiredEnergy)) {
                        entity.working = true;
                        changed = true;
                        entity.dispatch(EnergyMachineEvent.ENERGY_USED);

                        serverWorld.setBlockState(neighborPos, cropBlock.withAge(1), Block.NOTIFY_ALL);
                        for (var stack : droppedStacks) {
                            entity.inventory.addStack(EnergyMachineInventoryType.OUTPUT, stack);
                        }
                        serverWorld.syncWorldEvent(WorldEvents.BONE_MEAL_USED, neighborPos, 0);
                        harvest = true;
                    } else {
                        entity.dispatch(EnergyMachineEvent.ENERGY_NOT_ENOUGH);
                    }
                }
            }

            if (harvest) {
                entity.dispatch(EnergyMachineEvent.COOKED);
            }
        }

        boolean energyChanged = hasEnergy != entity.energyContainer.hasAtLeast(requiredEnergy);
        entity.updateState(working, state, world, pos, energyChanged || changed);
    }

    @Override
    public @NotNull Energy getEnergyUsagePerTick() {
        return block.getEnergyUsagePerTick();
    }
}

