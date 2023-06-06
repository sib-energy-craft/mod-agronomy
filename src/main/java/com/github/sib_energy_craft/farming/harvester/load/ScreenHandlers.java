package com.github.sib_energy_craft.farming.harvester.load;

import com.github.sib_energy_craft.farming.harvester.screen.HarvesterScreenHandler;
import com.github.sib_energy_craft.energy_api.utils.Identifiers;
import com.github.sib_energy_craft.sec_utils.load.DefaultModInitializer;
import net.minecraft.screen.ScreenHandlerType;

import static com.github.sib_energy_craft.sec_utils.utils.ScreenUtils.registerHandler;

/**
 * @since 0.0.2
 * @author sibmaks
 */
public final class ScreenHandlers implements DefaultModInitializer {
    public static final ScreenHandlerType<HarvesterScreenHandler> HARVESTER;

    static {
        HARVESTER = registerHandler(Identifiers.of("harvester"), HarvesterScreenHandler::new);
    }

}
