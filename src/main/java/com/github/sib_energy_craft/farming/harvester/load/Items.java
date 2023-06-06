package com.github.sib_energy_craft.farming.harvester.load;

import com.github.sib_energy_craft.machines.item.EnergyMachineBlockItem;
import com.github.sib_energy_craft.sec_utils.load.DefaultModInitializer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;

import static com.github.sib_energy_craft.sec_utils.utils.ItemUtils.register;

/**
 * @since 0.0.2
 * @author sibmaks
 */
public final class Items implements DefaultModInitializer {
    public static final BlockItem HARVESTER;

    static {
        var harvesterSettings = new Item.Settings();

        var harvester = new EnergyMachineBlockItem<>(Blocks.HARVESTER.entity(), harvesterSettings);
        HARVESTER = register(ItemGroups.FUNCTIONAL, Blocks.HARVESTER.identifier(), harvester);
    }
}
