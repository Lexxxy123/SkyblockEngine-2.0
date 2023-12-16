package vn.giakhanhvn.skysim.item.accessory;

import vn.giakhanhvn.skysim.item.*;

public interface AccessoryStatistics extends PlayerBoostStatistics, SkullStatistics, Reforgable {
    default GenericItemType getType() {
        return GenericItemType.ACCESSORY;
    }

    default SpecificItemType getSpecificType() {
        return SpecificItemType.ACCESSORY;
    }
}
