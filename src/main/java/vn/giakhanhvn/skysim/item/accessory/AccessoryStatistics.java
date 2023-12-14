package vn.giakhanhvn.skysim.item.accessory;

import vn.giakhanhvn.skysim.item.SpecificItemType;
import vn.giakhanhvn.skysim.item.GenericItemType;
import vn.giakhanhvn.skysim.item.Reforgable;
import vn.giakhanhvn.skysim.item.SkullStatistics;
import vn.giakhanhvn.skysim.item.PlayerBoostStatistics;

public interface AccessoryStatistics extends PlayerBoostStatistics, SkullStatistics, Reforgable
{
    default GenericItemType getType() {
        return GenericItemType.ACCESSORY;
    }
    
    default SpecificItemType getSpecificType() {
        return SpecificItemType.ACCESSORY;
    }
}
