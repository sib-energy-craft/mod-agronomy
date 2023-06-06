package com.github.sib_energy_craft.farming.feeding_station.load;

import com.github.sib_energy_craft.farming.feeding_station.block.entity.FeedingStationBlockEntity;
import com.github.sib_energy_craft.sec_utils.load.DefaultModInitializer;
import net.minecraft.block.entity.BlockEntityType;

import static com.github.sib_energy_craft.sec_utils.utils.EntityUtils.register;

/**
 * @author sibmaks
 * @since 0.0.4
 */
public final class Entities implements DefaultModInitializer {
    public static final BlockEntityType<FeedingStationBlockEntity> FEEDING_STATION;

    static {
        FEEDING_STATION = register(Blocks.FEEDING_STATION, FeedingStationBlockEntity::new);
    }
}
