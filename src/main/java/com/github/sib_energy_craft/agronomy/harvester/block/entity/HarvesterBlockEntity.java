package com.github.sib_energy_craft.agronomy.harvester.block.entity;

import com.github.sib_energy_craft.agronomy.harvester.block.HarvesterBlock;
import com.github.sib_energy_craft.agronomy.harvester.load.Entities;
import com.github.sib_energy_craft.agronomy.harvester.screen.HarvesterScreenHandler;
import com.github.sib_energy_craft.machines.screen.AbstractEnergyMachineScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @since 0.0.1
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
    public @Nullable Recipe<Inventory> getRecipe(@NotNull World world, int process) {
        throw new UnsupportedOperationException("Machine is not craft items");
    }

    @Override
    public int getCookTimeTotal(@NotNull World world) {
        return 0;
    }

    @Override
    protected boolean canAcceptRecipeOutput(int process, @NotNull World world, @NotNull Recipe<Inventory> recipe, int count) {
        throw new UnsupportedOperationException("Machine is not craft items");
    }

    @Override
    public boolean craftRecipe(int process, @NotNull World world, @NotNull Recipe<Inventory> recipe, int decrement, int maxCount) {
        throw new UnsupportedOperationException("Machine is not craft items");
    }

    @Override
    protected AbstractEnergyMachineScreenHandler createScreenHandler(int syncId,
                                                                     @NotNull PlayerInventory playerInventory,
                                                                     @NotNull PlayerEntity player) {
        return new HarvesterScreenHandler(syncId, playerInventory, this);
    }

    @Override
    public void writeScreenOpeningData(@NotNull ServerPlayerEntity player,
                                       @NotNull PacketByteBuf buf) {

    }
}

