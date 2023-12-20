package in.godspunky.skyblock.item.dragon.old;

import in.godspunky.skyblock.item.GenericItemType;
import in.godspunky.skyblock.item.MaterialFunction;
import in.godspunky.skyblock.item.Rarity;
import in.godspunky.skyblock.item.SpecificItemType;
import in.godspunky.skyblock.item.armor.LeatherArmorStatistics;

public class OldDragonBoots implements MaterialFunction, LeatherArmorStatistics {
    @Override
    public double getBaseHealth() {
        return 80.0;
    }

    @Override
    public double getBaseDefense() {
        return 90.0;
    }

    @Override
    public int getColor() {
        return 15787690;
    }

    @Override
    public String getDisplayName() {
        return "Old Dragon Boots";
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
        return SpecificItemType.BOOTS;
    }

    @Override
    public String getLore() {
        return null;
    }
}
