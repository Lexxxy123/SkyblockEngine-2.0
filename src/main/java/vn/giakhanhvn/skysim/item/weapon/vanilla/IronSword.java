package vn.giakhanhvn.skysim.item.weapon.vanilla;

import vn.giakhanhvn.skysim.item.*;

public class IronSword implements ToolStatistics, MaterialFunction {
    @Override
    public String getDisplayName() {
        return "Iron Sword";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
    }

    @Override
    public int getBaseDamage() {
        return 30;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.WEAPON;
    }

    @Override
    public SpecificItemType getSpecificType() {
        return SpecificItemType.SWORD;
    }
}
