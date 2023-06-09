package com.github.sib_energy_craft.farming.harvester.block.entity;

import com.github.sib_energy_craft.energy_api.Energy;
import com.github.sib_energy_craft.energy_api.items.ChargeableItem;
import com.github.sib_energy_craft.farming.harvester.block.AbstractHarvesterBlock;
import com.github.sib_energy_craft.machines.block.entity.AbstractEnergyMachineBlockEntity;
import com.github.sib_energy_craft.machines.block.entity.EnergyMachineInventoryType;
import net.minecraft.block.*;
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

        for(int i = -radius; i <= radius; i++) {
            var neighborPos = pos
                    .offset(Direction.Axis.X, harvestPos.x)
                    .offset(Direction.Axis.Y, i)
                    .offset(Direction.Axis.Z, harvestPos.y);

            var neighborState = world.getBlockState(neighborPos);
            var neighborBlock = neighborState.getBlock();
            if (neighborBlock instanceof CropBlock cropBlock) {
                if(harvsetCropBlock(cropBlock, neighborState, neighborPos, serverWorld, processContext)) {
                    return true;
                }
            } else if(neighborBlock instanceof GourdBlock gourdBlock) {
                if(harvsetBlock(gourdBlock, neighborState, Blocks.AIR.getDefaultState(),
                        neighborPos, serverWorld, processContext, false)) {
                    return true;
                }
            } else if(neighborBlock instanceof CocoaBlock cocoaBlock) {
                if(harvsetCocoaBlock(cocoaBlock, neighborState, neighborPos, serverWorld, processContext)) {
                    return true;
                }
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
        working = true;
        var serverWorld = (ServerWorld) processContext.get("ServerWorld");
        var neighborPos = (BlockPos) processContext.get("NeighborPos");
        var nextNeighborState = (BlockState) processContext.get("NextNeighborState");
        var droppedStacks = (List<ItemStack>) processContext.get("DroppedStacks");
        serverWorld.setBlockState(neighborPos, nextNeighborState, Block.NOTIFY_ALL);
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

    private boolean harvsetCropBlock(CropBlock cropBlock,
                                     BlockState neighborState,
                                     BlockPos neighborPos,
                                     ServerWorld serverWorld,
                                     Map<String, Object> processContext) {
        var age = cropBlock.getAge(neighborState);
        var maxAge = cropBlock.getMaxAge();
        if (!Objects.equals(age, maxAge)) {
            return false;
        }
        return harvsetBlock(cropBlock, neighborState, cropBlock.withAge(1), neighborPos, serverWorld,
                processContext, true);
    }

    private boolean harvsetCocoaBlock(CocoaBlock cocoaBlock,
                                     BlockState neighborState,
                                     BlockPos neighborPos,
                                     ServerWorld serverWorld,
                                     Map<String, Object> processContext) {
        var age = neighborState.get(CocoaBlock.AGE);
        var maxAge =  CocoaBlock.MAX_AGE;
        if (!Objects.equals(age, maxAge)) {
            return false;
        }
        return harvsetBlock(cocoaBlock, neighborState, neighborState.with(CocoaBlock.AGE, 0), neighborPos,
                serverWorld, processContext, true);
    }

    private boolean harvsetBlock(Block gourdBlock,
                                 BlockState neighborState,
                                 BlockState nextNeighborState,
                                 BlockPos neighborPos,
                                 ServerWorld serverWorld,
                                 Map<String, Object> processContext,
                                 boolean decrementSeeds) {
        var toolStack = inventory.getStack(EnergyMachineInventoryType.SOURCE, TOOL_SLOT);
        var builder = new LootContextParameterSet.Builder(serverWorld)
                .add(LootContextParameters.ORIGIN, neighborPos.toCenterPos())
                .add(LootContextParameters.TOOL, toolStack);
        var droppedStacks = neighborState.getDroppedStacks(builder);
        boolean canInsert = true;
        var neighborItem = gourdBlock.asItem();
        for (var stack : droppedStacks) {
            if (stack.isEmpty()) {
                continue;
            }
            if (decrementSeeds && stack.getItem() == neighborItem) {
                stack.decrement(1);
            }
            if (!inventory.canInsert(EnergyMachineInventoryType.OUTPUT, stack)) {
                canInsert = false;
            }
        }
        if (canInsert) {
            processContext.put("ServerWorld", serverWorld);
            processContext.put("NeighborPos", neighborPos);
            processContext.put("NextNeighborState", nextNeighborState);
            processContext.put("DroppedStacks", droppedStacks);
        }
        return canInsert;
    }
}

