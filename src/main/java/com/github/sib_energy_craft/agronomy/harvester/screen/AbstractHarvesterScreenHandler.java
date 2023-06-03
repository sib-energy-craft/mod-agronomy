package com.github.sib_energy_craft.agronomy.harvester.screen;

import com.github.sib_energy_craft.machines.screen.AbstractEnergyMachineScreenHandler;
import com.github.sib_energy_craft.machines.screen.EnergyMachineState;
import com.github.sib_energy_craft.machines.screen.layout.SlotLayoutManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.NotNull;

/**
 * @since 0.0.2
 * @author sibmaks
 */
public abstract class AbstractHarvesterScreenHandler extends AbstractEnergyMachineScreenHandler<EnergyMachineState> {

    protected AbstractHarvesterScreenHandler(@NotNull ScreenHandlerType<?> type,
                                             int syncId,
                                             @NotNull PlayerInventory playerInventory,
                                             int sourceSlots,
                                             int outputSlots,
                                             @NotNull SlotLayoutManager slotLayoutManager) {
        super(type, syncId, playerInventory, sourceSlots, outputSlots, new EnergyMachineState(), slotLayoutManager);
    }

    protected AbstractHarvesterScreenHandler(@NotNull ScreenHandlerType<?> type,
                                             int syncId,
                                             @NotNull PlayerInventory playerInventory,
                                             @NotNull Inventory inventory,
                                             int sourceSlots,
                                             int outputSlots,
                                             @NotNull SlotLayoutManager slotLayoutManager) {
        super(type, syncId, playerInventory, inventory, sourceSlots, outputSlots, new EnergyMachineState(), slotLayoutManager);
    }

    @Override
    protected boolean isUsedInMachine(@NotNull ItemStack itemStack) {
        return true;//CompressorTags.isUsedInCompressor(itemStack);
    }
}

