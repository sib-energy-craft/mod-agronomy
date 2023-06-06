package com.github.sib_energy_craft.farming.feeding_station.load.client;

import com.github.sib_energy_craft.farming.feeding_station.load.ScreenHandlers;
import com.github.sib_energy_craft.farming.feeding_station.screen.FeedingStationScreen;
import com.github.sib_energy_craft.sec_utils.load.DefaultClientModInitializer;

import static com.github.sib_energy_craft.sec_utils.utils.ScreenUtils.registerScreen;

/**
 * @author sibmaks
 * @since 0.0.4
 */
public final class Screens implements DefaultClientModInitializer {

    static {
        registerScreen(ScreenHandlers.FEEDING_STATION, FeedingStationScreen::new);
    }

}
