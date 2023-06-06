package com.github.sib_energy_craft.farming.harvester.block.entity;

import com.github.sib_energy_craft.farming.harvester.block.HarvesterBlock;
import com.github.sib_energy_craft.farming.harvester.load.Entities;
import com.github.sib_energy_craft.farming.harvester.screen.HarvesterScreenHandler;
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
 * @since 0.0.2
 * @author sibmaks
 */
public class HarvesterBlockEntity extends AbstractHarvesterBlockEntity<HarvesterBlock> {
    public HarvesterBlockEntity(@NotNull BlockPos pos,
                                @NotNull BlockState state,
                                @NotNull HarvesterBlock block) {
        super(Entities.HARVESTER, pos, state, block);
    }

    @Override
    public @NotNull Text getDisplayName() {
        return Text.translatable("container.harvester");
    }

    @Override
    protected AbstractEnergyMachineScreenHandler<?> createScreenHandler(int syncId,
                                                                     @NotNull PlayerInventory playerInventory,
                                                                     @NotNull PlayerEntity player) {
        return new HarvesterScreenHandler(syncId, playerInventory, this);
    }

    @Override
    public void writeScreenOpeningData(@NotNull ServerPlayerEntity player,
                                       @NotNull PacketByteBuf buf) {

    }
}

