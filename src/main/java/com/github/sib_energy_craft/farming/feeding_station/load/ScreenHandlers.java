package com.github.sib_energy_craft.farming.feeding_station.load;

import com.github.sib_energy_craft.farming.feeding_station.screen.FeedingStationScreenHandler;
import com.github.sib_energy_craft.energy_api.utils.Identifiers;
import com.github.sib_energy_craft.sec_utils.load.DefaultModInitializer;
import net.minecraft.screen.ScreenHandlerType;

import static com.github.sib_energy_craft.sec_utils.utils.ScreenUtils.registerHandler;

/**
 * @author sibmaks
 * @since 0.0.4
 */
public final class ScreenHandlers implements DefaultModInitializer {
    public static final ScreenHandlerType<FeedingStationScreenHandler> FEEDING_STATION;

    static {
        FEEDING_STATION = registerHandler(Identifiers.of("feeding_station"), FeedingStationScreenHandler::new);
    }

}
