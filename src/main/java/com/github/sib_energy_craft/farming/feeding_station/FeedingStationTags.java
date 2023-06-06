package com.github.sib_energy_craft.farming.feeding_station;

import com.github.sib_energy_craft.energy_api.utils.Identifiers;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

/**
 * @author sibmaks
 * @since 0.0.4
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FeedingStationTags {

    public static final TagKey<Item> USED_IN_FEEDING_STATION;

    static {
        USED_IN_FEEDING_STATION = TagKey.of(RegistryKeys.ITEM, Identifiers.of("used_in_feeding_station"));
    }

}
