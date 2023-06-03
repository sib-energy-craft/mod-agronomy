package com.github.sib_energy_craft.agronomy.harvester.block.entity;

import com.github.sib_energy_craft.agronomy.harvester.block.AbstractHarvesterBlock;
import com.github.sib_energy_craft.energy_api.Energy;
import com.github.sib_energy_craft.energy_api.items.ChargeableItem;
import com.github.sib_energy_craft.machines.block.entity.AbstractEnergyMachineBlockEntity;
import com.github.sib_energy_craft.machines.block.entity.EnergyMachineInventoryType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.util.List;
import java.util.Map;
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

    @Override
    protected boolean canProcess(int process,
                                 @NotNull World world,
                                 @NotNull BlockPos pos,
                                 @NotNull BlockState state,
                                 @NotNull Map<String, Object> processContext) {
        if (world.isClient || !(world instanceof ServerWorld serverWorld)) {
            return false;
        }

        harvestPos.x += delta;
        int radius = block.getRadius();
        if (harvestPos.x > radius || harvestPos.x < -radius) {
            harvestPos.x -= delta;
            harvestPos.y++;
            delta *= -1;
            if (harvestPos.y > radius) {
                harvestPos.x = -radius;
                harvestPos.y = -radius;
            }
        }

        var neighborPos = pos
                .up()
                .offset(Direction.Axis.X, harvestPos.x)
                .offset(Direction.Axis.Z, harvestPos.y);

        var neighborState = world.getBlockState(neighborPos);
        var neighborBlock = neighborState.getBlock();
        if (!(neighborBlock instanceof CropBlock cropBlock)) {
            return false;
        }
        var age = cropBlock.getAge(neighborState);
        var maxAge = cropBlock.getMaxAge();
        if (Objects.equals(age, maxAge)) {
            var toolStack = inventory.getStack(EnergyMachineInventoryType.SOURCE, TOOL_SLOT);
            var builder = new LootContextParameterSet.Builder(serverWorld)
                    .add(LootContextParameters.ORIGIN, neighborPos.toCenterPos())
                    .add(LootContextParameters.TOOL, toolStack);
            var droppedStacks = neighborState.getDroppedStacks(builder);
            boolean canInsert = true;
            var neighborItem = neighborBlock.asItem();
            for (var stack : droppedStacks) {
                if(stack.isEmpty()) {
                    continue;
                }
                if(stack.getItem() == neighborItem) {
                    stack.decrement(1);
                }
                if (!inventory.canInsert(EnergyMachineInventoryType.OUTPUT, stack)) {
                    canInsert = false;
                }
            }
            if(canInsert) {
                processContext.put("ServerWorld", serverWorld);
                processContext.put("NeighborPos", neighborPos);
                processContext.put("NeighborBlock", neighborBlock);
                processContext.put("DroppedStacks", droppedStacks);
            }
            return canInsert;
        }
        return false;
    }

    @Override
    protected boolean tickProcess(int process,
                                  @NotNull World world,
                                  @NotNull BlockPos pos,
                                  @NotNull BlockState state,
                                  @NotNull Map<String, Object> processContext) {
        working = true;
        var serverWorld = (ServerWorld) processContext.get("ServerWorld");
        var neighborPos = (BlockPos) processContext.get("NeighborPos");
        var neighborBlock = (CropBlock) processContext.get("NeighborBlock");
        var droppedStacks = (List<ItemStack>) processContext.get("DroppedStacks");
        serverWorld.setBlockState(neighborPos, neighborBlock.withAge(1), Block.NOTIFY_ALL);
        for (var stack : droppedStacks) {
            inventory.addStack(EnergyMachineInventoryType.OUTPUT, stack);
        }
        serverWorld.syncWorldEvent(WorldEvents.BONE_MEAL_USED, neighborPos, 0);
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

