package com.github.sib_energy_craft.farming.feeding_station.screen;

import com.github.sib_energy_craft.farming.feeding_station.FeedingStationMode;
import com.github.sib_energy_craft.farming.feeding_station.FeedingStationTypedProperties;
import com.github.sib_energy_craft.machines.screen.EnergyMachineState;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author sibmaks
 * @since 0.0.4
 */
@AllArgsConstructor
public class FeedingStationState extends EnergyMachineState {
    private static final FeedingStationMode[] MODES = FeedingStationMode.values();

    @Getter
    private FeedingStationMode feedingStationMode;

    @Override
    public <V> void changeProperty(int index, V value) {
        super.changeProperty(index, value);
        if(index == FeedingStationTypedProperties.MODE.getIndex()) {
            int modeIndex = (int) value;
            if(modeIndex >= 0 && modeIndex < MODES.length) {
                feedingStationMode = MODES[modeIndex];
            }
        }
    }

    /**
     * Calculate and get next machine mode
     *
     * @return next machine mode
     */
    public FeedingStationMode getNextMode() {
        feedingStationMode = MODES[(feedingStationMode.ordinal() + 1) % MODES.length];
        return feedingStationMode;
    }
}
