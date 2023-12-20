package in.godspunky.skyblock.item.dragon.strong;

import in.godspunky.skyblock.item.GenericItemType;
import in.godspunky.skyblock.item.MaterialFunction;
import in.godspunky.skyblock.item.Rarity;
import in.godspunky.skyblock.item.SpecificItemType;
import in.godspunky.skyblock.item.armor.LeatherArmorStatistics;

public class StrongDragonChestplate implements MaterialFunction, LeatherArmorStatistics {
    @Override
    public double getBaseStrength() {
        return 25.0;
    }

    @Override
    public double getBaseHealth() {
        return 120.0;
    }

    @Override
    public double getBaseDefense() {
        return 160.0;
    }

    @Override
    public int getColor() {
        return 14229057;
    }

    @Override
    public String getDisplayName() {
        return "Strong Dragon Chestplate";
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
