package com.github.sib_energy_craft.farming.feeding_station.block;

import com.github.sib_energy_craft.farming.feeding_station.block.entity.FeedingStationBlockEntity;
import com.github.sib_energy_craft.farming.feeding_station.load.Entities;
import com.github.sib_energy_craft.farming.feeding_station.load.Stats;
import com.github.sib_energy_craft.energy_api.Energy;
import com.github.sib_energy_craft.energy_api.EnergyLevel;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author sibmaks
 * @since 0.0.4
 */
public class FeedingStationBlock extends AbstractFeedingStationBlock {
    public FeedingStationBlock(@NotNull Settings settings) {
        super(settings, EnergyLevel.L1, new Energy(800), new Energy(4), 5);
    }

    @NotNull
    @Override
    public BlockEntity createBlockEntity(@NotNull BlockPos pos,
                                         @NotNull BlockState state) {
        return new FeedingStationBlockEntity(pos, state, this);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull World world,
                                                                  @NotNull BlockState state,
                                                                  @NotNull BlockEntityType<T> type) {
        return FeedingStationBlock.checkType(world, type, Entities.FEEDING_STATION);
    }

    @Override
    protected void openScreen(@NotNull World world,
                              @NotNull BlockPos pos,
                              @NotNull PlayerEntity player) {
        var blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof FeedingStationBlockEntity machineBlockEntity) {
            player.openHandledScreen(machineBlockEntity);
            player.incrementStat(Stats.INTERACT_WITH_FEEDING_STATION);
        }
    }

    @Override
    public void randomDisplayTick(@NotNull BlockState state,
                                  @NotNull World world,
                                  @NotNull BlockPos pos,
                                  @NotNull Random random) {
        if (!state.get(WORKING)) {
            return;
        }
        // TODO: Nice to have here compressor sound
    }
}
