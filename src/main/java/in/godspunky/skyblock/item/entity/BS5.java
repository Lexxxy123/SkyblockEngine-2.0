package in.godspunky.skyblock.item.entity;

import in.godspunky.skyblock.item.GenericItemType;
import in.godspunky.skyblock.item.MaterialFunction;
import in.godspunky.skyblock.item.Rarity;
import in.godspunky.skyblock.item.SkullStatistics;

public class BS5 implements SkullStatistics, MaterialFunction {
    @Override
    public String getURL() {
        return "f868e6a5c4a445d60a3050b5bec1d37af1b25943745d2d479800c8436488065a";
    }

    @Override
    public String getDisplayName() {
        return "BZB5";
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
