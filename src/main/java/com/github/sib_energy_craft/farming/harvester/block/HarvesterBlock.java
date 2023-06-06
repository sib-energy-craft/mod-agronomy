package com.github.sib_energy_craft.farming.harvester.block;

import com.github.sib_energy_craft.farming.harvester.block.entity.HarvesterBlockEntity;
import com.github.sib_energy_craft.farming.harvester.load.Entities;
import com.github.sib_energy_craft.farming.harvester.load.Stats;
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
 * @since 0.0.2
 * @author sibmaks
 */
public class HarvesterBlock extends AbstractHarvesterBlock {
    public HarvesterBlock(@NotNull Settings settings) {
        super(settings, EnergyLevel.L1, 800, Energy.of(4), 4);
    }

    @NotNull
    @Override
    public BlockEntity createBlockEntity(@NotNull BlockPos pos,
                                         @NotNull BlockState state) {
        return new HarvesterBlockEntity(pos, state, this);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull World world,
                                                                  @NotNull BlockState state,
                                                                  @NotNull BlockEntityType<T> type) {
        return HarvesterBlock.checkType(world, type, Entities.HARVESTER);
    }

    @Override
    protected void openScreen(@NotNull World world,
                              @NotNull BlockPos pos,
                              @NotNull PlayerEntity player) {
        var blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof HarvesterBlockEntity machineBlockEntity) {
            player.openHandledScreen(machineBlockEntity);
            player.incrementStat(Stats.INTERACT_WITH_HARVESTER);
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
