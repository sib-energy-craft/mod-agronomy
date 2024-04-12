package com.github.sib_energy_craft.farming.feeding_station.block;

import com.github.sib_energy_craft.farming.feeding_station.block.entity.AbstractFeedingStationBlockEntity;
import com.github.sib_energy_craft.energy_api.Energy;
import com.github.sib_energy_craft.energy_api.EnergyLevel;
import com.github.sib_energy_craft.machines.block.AbstractEnergyMachineBlock;
import lombok.Getter;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author sibmaks
 * @since 0.0.4
 */
public abstract class AbstractFeedingStationBlock extends AbstractEnergyMachineBlock {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty FILLED = BooleanProperty.of("filled");

    @Getter
    protected final Energy energyUsagePerTick;
    @Getter
    protected final int radius;

    protected AbstractFeedingStationBlock(@NotNull AbstractBlock.Settings settings,
                                          @NotNull EnergyLevel energyLevel,
                                          @NotNull Energy maxCharge,
                                          @NotNull Energy energyUsagePerTick,
                                          int radius) {
        super(settings, energyLevel, maxCharge);
        this.energyUsagePerTick = energyUsagePerTick;
        this.radius = radius;
        this.setDefaultState(getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(FILLED, false));
    }

    @NotNull
    @Override
    public BlockState getPlacementState(@NotNull ItemPlacementContext ctx) {
        var horizontalPlayerFacing = ctx.getHorizontalPlayerFacing();
        return this.getDefaultState().with(FACING, horizontalPlayerFacing.getOpposite());
    }

    @NotNull
    @Override
    public BlockState rotate(@NotNull BlockState state,
                             @NotNull BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @NotNull
    @Override
    public BlockState mirror(@NotNull BlockState state,
                             @NotNull BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(@NotNull StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING, FILLED);
    }

    @Nullable
    protected static <T extends BlockEntity, E extends AbstractFeedingStationBlockEntity<?>> BlockEntityTicker<T> checkType(
            @NotNull World world,
            @NotNull BlockEntityType<T> givenType,
            @NotNull BlockEntityType<E> expectedType) {
        return world.isClient ? null : BlockWithEntity.validateTicker(
                givenType,
                expectedType,
                AbstractFeedingStationBlockEntity::simpleProcessingTick
        );
    }
}
