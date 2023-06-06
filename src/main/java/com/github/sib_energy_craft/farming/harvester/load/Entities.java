package com.github.sib_energy_craft.farming.harvester.load;

import com.github.sib_energy_craft.farming.harvester.block.entity.HarvesterBlockEntity;
import com.github.sib_energy_craft.sec_utils.load.DefaultModInitializer;
import net.minecraft.block.entity.BlockEntityType;

import static com.github.sib_energy_craft.sec_utils.utils.EntityUtils.register;

/**
 * @since 0.0.2
 * @author sibmaks
 */
public final class Entities implements DefaultModInitializer {
    public static final BlockEntityType<HarvesterBlockEntity> HARVESTER;

    static {
        HARVESTER = register(Blocks.HARVESTER, HarvesterBlockEntity::new);
    }
}
