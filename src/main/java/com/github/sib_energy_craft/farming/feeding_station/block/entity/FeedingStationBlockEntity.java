package com.github.sib_energy_craft.farming.feeding_station.block.entity;

import com.github.sib_energy_craft.farming.feeding_station.block.FeedingStationBlock;
import com.github.sib_energy_craft.farming.feeding_station.load.Entities;
import com.github.sib_energy_craft.farming.feeding_station.screen.FeedingStationScreenHandler;
import com.github.sib_energy_craft.machines.screen.AbstractEnergyMachineScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

/**
 * @author sibmaks
 * @since 0.0.4
 */
public class FeedingStationBlockEntity extends AbstractFeedingStationBlockEntity<FeedingStationBlock> {
    public FeedingStationBlockEntity(@NotNull BlockPos pos,
                                     @NotNull BlockState state,
                                     @NotNull FeedingStationBlock block) {
        super(Entities.FEEDING_STATION, pos, state, block);
    }

    @Override
    public @NotNull Text getDisplayName() {
        return Text.translatable("container.feeding_station");
    }

    @Override
    protected AbstractEnergyMachineScreenHandler<?> createScreenHandler(int syncId,
                                                                     @NotNull PlayerInventory playerInventory,
                                                                     @NotNull PlayerEntity player) {
        return new FeedingStationScreenHandler(syncId, playerInventory, this, player.getWorld(), pos);
    }

    @Override
    public void writeScreenOpeningData(@NotNull ServerPlayerEntity player,
                                       @NotNull PacketByteBuf buf) {

    }
}

