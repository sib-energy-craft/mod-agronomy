package com.github.sib_energy_craft.farming.feeding_station;

import com.github.sib_energy_craft.machines.block.entity.property.EnergyMachineTypedProperties;
import com.github.sib_energy_craft.machines.block.entity.property.EnergyMachineTypedProperty;
import com.github.sib_energy_craft.screen.property.ScreenPropertyType;
import com.github.sib_energy_craft.screen.property.ScreenPropertyTypes;

/**
 * @author sibmaks
 * @since 0.0.4
 */
public enum FeedingStationTypedProperties implements EnergyMachineTypedProperty<Integer> {
    MODE;

    private static final int OFFSET = EnergyMachineTypedProperties.PROPERTIES_SIZE;
    public static final int PROPERTIES_SIZE = OFFSET + values().length;

    @Override
    public int getIndex() {
        return OFFSET + ordinal();
    }

    @Override
    public ScreenPropertyType<Integer> getPropertyType() {
        return ScreenPropertyTypes.INT;
    }
}
