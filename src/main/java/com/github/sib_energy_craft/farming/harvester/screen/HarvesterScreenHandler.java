package com.github.sib_energy_craft.farming.harvester.screen;

import com.github.sib_energy_craft.farming.harvester.load.ScreenHandlers;
import com.github.sib_energy_craft.machines.screen.layout.MultiSlotMachineLayoutManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

/**
 * @since 0.0.2
 * @author sibmaks
 */
public class HarvesterScreenHandler extends AbstractHarvesterScreenHandler {
    private static final MultiSlotMachineLayoutManager LAYOUT_MANAGER = new MultiSlotMachineLayoutManager(
            8, 142,
            8, 84,
            new Vector2i[]{new Vector2i(41, 17)},
            41, 53,
            new Vector2i[]{
                    new Vector2i(77, 17),
                    new Vector2i(95, 17),
                    new Vector2i(113, 17),

                    new Vector2i(77, 35),
                    new Vector2i(95, 35),
                    new Vector2i(113, 35),

                    new Vector2i(77, 53),
                    new Vector2i(95, 53),
                    new Vector2i(113, 53)
            }
    );

    public HarvesterScreenHandler(int syncId,
                                  @NotNull PlayerInventory playerInventory,
                                  @NotNull Inventory inventory) {
        super(ScreenHandlers.HARVESTER, syncId, playerInventory, inventory, 1, 9, LAYOUT_MANAGER);
    }

    public HarvesterScreenHandler(int syncId,
                                  @NotNull PlayerInventory playerInventory,
                                  @NotNull PacketByteBuf packetByteBuf) {
        super(ScreenHandlers.HARVESTER, syncId, playerInventory, 1, 9, LAYOUT_MANAGER);

    }
}
