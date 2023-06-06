package com.github.sib_energy_craft.farming.feeding_station.load;

import com.github.sib_energy_craft.machines.item.EnergyMachineBlockItem;
import com.github.sib_energy_craft.sec_utils.load.DefaultModInitializer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;

import static com.github.sib_energy_craft.sec_utils.utils.ItemUtils.register;

/**
 * @author sibmaks
 * @since 0.0.4
 */
public final class Items implements DefaultModInitializer {
    public static final BlockItem FEEDING_STATION;

    static {
        var feedingStationSettings = new Item.Settings();

        var feedingStation = new EnergyMachineBlockItem<>(Blocks.FEEDING_STATION.entity(), feedingStationSettings);
        FEEDING_STATION = register(ItemGroups.FUNCTIONAL, Blocks.FEEDING_STATION.identifier(), feedingStation);
    }
}
