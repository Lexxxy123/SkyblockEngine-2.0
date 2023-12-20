package in.godspunky.skyblock.item.oddities;

import in.godspunky.skyblock.item.GenericItemType;
import in.godspunky.skyblock.item.MaterialFunction;
import in.godspunky.skyblock.item.MaterialStatistics;
import in.godspunky.skyblock.item.Rarity;

public class CreativeMind implements MaterialStatistics, MaterialFunction {
    @Override
    public String getDisplayName() {
        return "Creative Mind";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.SPECIAL;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ITEM;
    }

    @Override
    public boolean isEnchanted() {
        return true;
    }

    @Override
    public String getLore() {
        return "Original, visionary, inventive and innovative! I would even add ingenious, clever! A masterpiece!";
    }

    @Override
    public boolean isStackable() {
        return false;
    }
}
