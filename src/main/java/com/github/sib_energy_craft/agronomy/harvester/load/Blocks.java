package com.github.sib_energy_craft.agronomy.harvester.load;

import com.github.sib_energy_craft.agronomy.harvester.block.HarvesterBlock;
import com.github.sib_energy_craft.energy_api.utils.Identifiers;
import com.github.sib_energy_craft.sec_utils.common.Identified;
import com.github.sib_energy_craft.sec_utils.load.DefaultModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

import static com.github.sib_energy_craft.sec_utils.utils.BlockUtils.register;

/**
 * @since 0.0.2
 * @author sibmaks
 */
public final class Blocks implements DefaultModInitializer {
    public static final Identified<HarvesterBlock> HARVESTER;

    static {
        var harvesterSettings = FabricBlockSettings.of(Material.METAL)
                .sounds(BlockSoundGroup.METAL)
                .strength(5, 6)
                .requiresTool();

        var harvesterBlock = new HarvesterBlock(harvesterSettings);
        HARVESTER = register(Identifiers.of("harvester"), harvesterBlock);
    }
}