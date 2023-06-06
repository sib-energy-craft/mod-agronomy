package com.github.sib_energy_craft.farming.harvester.load;

import com.github.sib_energy_craft.energy_api.utils.Identifiers;
import com.github.sib_energy_craft.sec_utils.load.DefaultModInitializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.stat.StatFormatter;
import net.minecraft.util.Identifier;

import static net.minecraft.stat.Stats.CUSTOM;

/**
 * @since 0.0.2
 * @author sibmaks
 */
public final class Stats implements DefaultModInitializer {
    public static final Identifier INTERACT_WITH_HARVESTER;

    static {
        INTERACT_WITH_HARVESTER = register(Identifiers.asString("interact_with_harvester"), StatFormatter.DEFAULT);
    }

    private static Identifier register(String id, StatFormatter formatter) {
        var identifier = new Identifier(id);
        Registry.register(Registries.CUSTOM_STAT, id, identifier);
        CUSTOM.getOrCreateStat(identifier, formatter);
        return identifier;
    }
}
