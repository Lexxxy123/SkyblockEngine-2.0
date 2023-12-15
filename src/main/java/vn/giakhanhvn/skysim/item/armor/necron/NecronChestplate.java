package vn.giakhanhvn.skysim.item.armor.necron;

import vn.giakhanhvn.skysim.item.SpecificItemType;
import vn.giakhanhvn.skysim.item.GenericItemType;
import vn.giakhanhvn.skysim.item.Rarity;
import vn.giakhanhvn.skysim.item.armor.LeatherArmorStatistics;
import vn.giakhanhvn.skysim.item.MaterialFunction;

public class NecronChestplate implements MaterialFunction, LeatherArmorStatistics {
    @Override
    public double getBaseStrength() {
        return 40.0;
    }

    @Override
    public double getBaseCritDamage() {
        return 0.3;
    }

    @Override
    public double getBaseIntelligence() {
        return 10.0;
    }

    @Override
    public double getBaseHealth() {
        return 260.0;
    }

    @Override
    public double getBaseDefense() {
        return 140.0;
    }

    @Override
    public int getColor() {
        return 15155516;
    }

    @Override
    public String getDisplayName() {
        return "Necron's Chestplate";
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
        return SpecificItemType.CHESTPLATE;
    }

    @Override
    public String getLore() {
        return null;
    }
}
