package net.hypixel.skyblock.item.accessory;

import net.hypixel.skyblock.item.*;
import net.hypixel.skyblock.item.*;

public interface AccessoryStatistics extends PlayerBoostStatistics, SkullStatistics, Reforgable {
    default GenericItemType getType() {
        return GenericItemType.ACCESSORY;
    }

    default SpecificItemType getSpecificType() {
        return SpecificItemType.ACCESSORY;
    }
}
