package in.godspunky.skyblock.item.accessory;

import in.godspunky.skyblock.item.*;
import in.godspunky.skyblock.item.*;

public interface AccessoryStatistics extends PlayerBoostStatistics, SkullStatistics, Reforgable {
    default GenericItemType getType() {
        return GenericItemType.ACCESSORY;
    }

    default SpecificItemType getSpecificType() {
        return SpecificItemType.ACCESSORY;
    }
}
