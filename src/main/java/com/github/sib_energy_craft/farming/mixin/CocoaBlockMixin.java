package com.github.sib_energy_craft.farming.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CocoaBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Mixin(CocoaBlock.class)
public abstract class CocoaBlockMixin extends AbstractBlock {

    @Shadow @Final public static IntProperty AGE;

    public CocoaBlockMixin(Settings settings) {
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
        var age = state.get(CocoaBlock.AGE);
        var maxAge = CocoaBlock.MAX_AGE;
        if(Objects.equals(age, maxAge)) {
            var parameter = new LootContext.Builder(serverWorld)
                    .parameter(LootContextParameters.ORIGIN, pos.toCenterPos())
                    .parameter(LootContextParameters.TOOL, player.getStackInHand(hand))
                    .luck(player.getLuck());
            var droppedStacks = state.getDroppedStacks(parameter);
            var cropItem = asItem();
            for (var stack : droppedStacks) {
                if(stack.isEmpty()) {
                    continue;
                }
                if(stack.getItem() == cropItem) {
                    stack.decrement(1);
                }
                if (!player.getInventory().insertStack(stack)) {
                    player.dropItem(stack, false);
                }
            }
            world.setBlockState(pos, state.with(AGE, 0), Block.NOTIFY_LISTENERS);
            serverWorld.syncWorldEvent(WorldEvents.BONE_MEAL_USED, pos, 0);
            return ActionResult.CONSUME;
        }

        return ActionResult.PASS;
    }
}
