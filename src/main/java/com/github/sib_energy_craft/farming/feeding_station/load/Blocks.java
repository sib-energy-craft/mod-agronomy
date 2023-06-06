package com.github.sib_energy_craft.farming.feeding_station.load;

import com.github.sib_energy_craft.farming.feeding_station.block.FeedingStationBlock;
import com.github.sib_energy_craft.energy_api.utils.Identifiers;
import com.github.sib_energy_craft.sec_utils.common.Identified;
import com.github.sib_energy_craft.sec_utils.load.DefaultModInitializer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.MapColor;
import net.minecraft.sound.BlockSoundGroup;

import static com.github.sib_energy_craft.sec_utils.utils.BlockUtils.register;

/**
 * @author sibmaks
 * @since 0.0.4
 */
public final class Blocks implements DefaultModInitializer {
    public static final Identified<FeedingStationBlock> FEEDING_STATION;

    static {
        var feedingStationSettings = AbstractBlock.Settings.create()
                .mapColor(MapColor.IRON_GRAY)
                .sounds(BlockSoundGroup.METAL)
                .strength(5, 6)
                .requiresTool();

        var feedingStationBlock = new FeedingStationBlock(feedingStationSettings);
        FEEDING_STATION = register(Identifiers.of("feeding_station"), feedingStationBlock);
    }
}