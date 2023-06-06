package com.github.sib_energy_craft.farming.feeding_station.screen;

import com.github.sib_energy_craft.farming.feeding_station.block.entity.AbstractFeedingStationBlockEntity;
import com.github.sib_energy_craft.machines.screen.AbstractEnergyMachineScreenHandler;
import com.github.sib_energy_craft.machines.screen.layout.SlotLayoutManager;
import com.github.sib_energy_craft.sec_utils.utils.TagUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.NotNull;

import static com.github.sib_energy_craft.farming.feeding_station.FeedingStationTags.USED_IN_FEEDING_STATION;

/**
 * @author sibmaks
 * @since 0.0.4
 */
public abstract class AbstractFeedingStationScreenHandler extends AbstractEnergyMachineScreenHandler<FeedingStationState> {
    private final ScreenHandlerContext context;

    protected AbstractFeedingStationScreenHandler(@NotNull ScreenHandlerType<?> type,
                                                  int syncId,
                                                  @NotNull PlayerInventory playerInventory,
                                                  int sourceSlots,
                                                  int outputSlots,
                                                  @NotNull SlotLayoutManager slotLayoutManager) {
        super(type, syncId, playerInventory, sourceSlots, outputSlots, new FeedingStationState(), slotLayoutManager);
        this.context = ScreenHandlerContext.EMPTY;
    }

    protected AbstractFeedingStationScreenHandler(@NotNull ScreenHandlerType<?> type,
                                                  int syncId,
                                                  @NotNull PlayerInventory playerInventory,
                                                  @NotNull Inventory inventory,
                                                  int sourceSlots,
                                                  int outputSlots,
                                                  @NotNull SlotLayoutManager slotLayoutManager,
                                                  @NotNull ScreenHandlerContext context) {
        super(type, syncId, playerInventory, inventory, sourceSlots, outputSlots, new FeedingStationState(), slotLayoutManager);
        this.context = context;
    }

    @Override
    protected boolean isUsedInMachine(@NotNull ItemStack itemStack) {
        return TagUtils.hasTag(USED_IN_FEEDING_STATION, itemStack);
    }

    @Override
    public boolean onButtonClick(@NotNull PlayerEntity player, int id) {
        // server side
        var button = FeedingStationButtons.values()[id];
        return onButtonClick(button);
    }

    public boolean onButtonClick(@NotNull FeedingStationButtons button) {
        if(button == FeedingStationButtons.CHANGE_MODE) {
            var nextMode = energyMachineState.getNextMode();
            this.context.run((world, pos) -> {
                var blockEntity = world.getBlockEntity(pos);
                if(blockEntity instanceof AbstractFeedingStationBlockEntity<?> feedingStationBlockEntity) {
                    feedingStationBlockEntity.setFeedingStationMode(nextMode);
                }
            });
            return true;
        }
        return false;
    }
}

