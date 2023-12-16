package vn.giakhanhvn.skysim.item.armor.vanilla.iron;

import vn.giakhanhvn.skysim.item.*;

public class IronLeggings implements ToolStatistics, MaterialFunction {
    @Override
    public String getDisplayName() {
        return "Iron Chestplate";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ARMOR;
    }

    @Override
    public SpecificItemType getSpecificType() {
        return SpecificItemType.LEGGINGS;
    }

    @Override
    public double getBaseDefense() {
        return 25.0;
    }
}
