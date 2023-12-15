package vn.giakhanhvn.skysim.item.dragon.old;

import vn.giakhanhvn.skysim.item.SpecificItemType;
import vn.giakhanhvn.skysim.item.GenericItemType;
import vn.giakhanhvn.skysim.item.Rarity;
import vn.giakhanhvn.skysim.item.armor.LeatherArmorStatistics;
import vn.giakhanhvn.skysim.item.MaterialFunction;

public class OldDragonLeggings implements MaterialFunction, LeatherArmorStatistics {
    @Override
    public double getBaseHealth() {
        return 130.0;
    }

    @Override
    public double getBaseDefense() {
        return 140.0;
    }

    @Override
    public int getColor() {
        return 15787690;
    }

    @Override
    public String getDisplayName() {
        return "Old Dragon Leggings";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
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
    public String getLore() {
        return null;
    }
}
