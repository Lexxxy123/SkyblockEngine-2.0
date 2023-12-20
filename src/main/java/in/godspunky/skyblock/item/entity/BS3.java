package in.godspunky.skyblock.item.entity;

import in.godspunky.skyblock.item.GenericItemType;
import in.godspunky.skyblock.item.MaterialFunction;
import in.godspunky.skyblock.item.Rarity;
import in.godspunky.skyblock.item.SkullStatistics;

public class BS3 implements SkullStatistics, MaterialFunction {
    @Override
    public String getURL() {
        return "cb1ae7a471729651b5667b81694e492808c5090c2b168f0a9190fd002ee50a26";
    }

    @Override
    public String getDisplayName() {
        return "BZB3";
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
