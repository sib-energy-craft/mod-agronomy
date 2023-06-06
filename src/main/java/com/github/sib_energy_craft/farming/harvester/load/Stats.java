package com.github.sib_energy_craft.farming.harvester.load;

import com.github.sib_energy_craft.energy_api.utils.Identifiers;
import com.github.sib_energy_craft.sec_utils.load.DefaultModInitializer;
import net.minecraft.util.Identifier;

import static com.github.sib_energy_craft.sec_utils.utils.StatUtils.register;

/**
 * @since 0.0.2
 * @author sibmaks
 */
public final class Stats implements DefaultModInitializer {
    public static final Identifier INTERACT_WITH_HARVESTER;

    static {
        INTERACT_WITH_HARVESTER = register(Identifiers.asString("interact_with_harvester"));
    }
}
