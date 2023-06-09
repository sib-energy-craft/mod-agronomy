package com.github.sib_energy_craft.farming.mixin;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Mixin(SugarCaneBlock.class)
public abstract class SugarCaneBlockMixin extends AbstractBlock {

    public SugarCaneBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(@NotNull BlockState state,
                              @NotNull World world,
                              @NotNull BlockPos pos,
                              @NotNull PlayerEntity player,
                              @NotNull Hand hand,
                              @NotNull BlockHitResult hit) {
        if (!(world instanceof ServerWorld serverWorld)) {
            return ActionResult.PASS;
        }

        int amount = 0;
        var position = pos.up();
        while (serverWorld.getBlockState(position).isOf((Block)(Object)this)) {
            amount++;
            world.setBlockState(position, Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS);
            position = position.up();
        }
        if(amount == 0) {
            return ActionResult.PASS;
        }

        var stack = new ItemStack(Items.SUGAR_CANE, amount);

        if (!player.getInventory().insertStack(stack)) {
            player.dropItem(stack, false);
        }

        return ActionResult.CONSUME;
    }
}
