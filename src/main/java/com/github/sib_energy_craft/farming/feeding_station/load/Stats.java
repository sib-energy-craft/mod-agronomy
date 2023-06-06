package com.github.sib_energy_craft.farming.feeding_station.load;

import com.github.sib_energy_craft.energy_api.utils.Identifiers;
import com.github.sib_energy_craft.sec_utils.load.DefaultModInitializer;
import net.minecraft.util.Identifier;

import static com.github.sib_energy_craft.sec_utils.utils.StatUtils.register;

/**
 * @author sibmaks
 * @since 0.0.4
 */
public final class Stats implements DefaultModInitializer {
    public static final Identifier INTERACT_WITH_FEEDING_STATION;

    static {
        INTERACT_WITH_FEEDING_STATION = register(Identifiers.asString("interact_with_feeding_station"));
    }
}
