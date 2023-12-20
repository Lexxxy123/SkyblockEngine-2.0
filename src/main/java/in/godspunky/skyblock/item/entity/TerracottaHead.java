package in.godspunky.skyblock.item.entity;

import in.godspunky.skyblock.item.GenericItemType;
import in.godspunky.skyblock.item.MaterialFunction;
import in.godspunky.skyblock.item.Rarity;
import in.godspunky.skyblock.item.SkullStatistics;

public class TerracottaHead implements SkullStatistics, MaterialFunction {
    @Override
    public String getURL() {
        return "6570a2d2a362adeeb05e6f25edcd54cac335f22f0e4669e9c1ebd9a02573bcab";
    }

    @Override
    public String getDisplayName() {
        return "E";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.EXCLUSIVE;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ARMOR;
    }
}
