package com.github.sib_energy_craft.farming.feeding_station.screen;

import com.github.sib_energy_craft.farming.feeding_station.load.ScreenHandlers;
import com.github.sib_energy_craft.machines.screen.layout.MultiSlotMachineLayoutManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

/**
 * @author sibmaks
 * @since 0.0.4
 */
public class FeedingStationScreenHandler extends AbstractFeedingStationScreenHandler {
    private static final MultiSlotMachineLayoutManager LAYOUT_MANAGER = new MultiSlotMachineLayoutManager(
            8, 142,
            8, 84,
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
            },
            41, 53,
            new Vector2i[0]
    );

    public FeedingStationScreenHandler(int syncId,
                                       @NotNull PlayerInventory playerInventory,
                                       @NotNull Inventory inventory,
                                       @NotNull World world,
                                       @NotNull BlockPos pos) {
        super(ScreenHandlers.FEEDING_STATION, syncId, playerInventory, inventory, 9, 0, LAYOUT_MANAGER,
                ScreenHandlerContext.create(world, pos));
    }

    public FeedingStationScreenHandler(int syncId,
                                       @NotNull PlayerInventory playerInventory,
                                       @NotNull PacketByteBuf packetByteBuf) {
        super(ScreenHandlers.FEEDING_STATION, syncId, playerInventory, 9, 0, LAYOUT_MANAGER);

    }
}
