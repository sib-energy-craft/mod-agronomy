package com.github.sib_energy_craft.agronomy.harvester.load.client;

import com.github.sib_energy_craft.agronomy.harvester.load.ScreenHandlers;
import com.github.sib_energy_craft.agronomy.harvester.screen.HarvesterScreen;
import com.github.sib_energy_craft.sec_utils.load.DefaultClientModInitializer;

import static com.github.sib_energy_craft.sec_utils.utils.ScreenUtils.registerScreen;

/**
 * @since 0.0.2
 * @author sibmaks
 */
public final class Screens implements DefaultClientModInitializer {

    static {
        registerScreen(ScreenHandlers.HARVESTER, HarvesterScreen::new);
    }

}
