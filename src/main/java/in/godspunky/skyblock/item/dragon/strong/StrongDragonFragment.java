package in.godspunky.skyblock.item.dragon.strong;

import in.godspunky.skyblock.item.GenericItemType;
import in.godspunky.skyblock.item.MaterialFunction;
import in.godspunky.skyblock.item.Rarity;
import in.godspunky.skyblock.item.SkullStatistics;

public class StrongDragonFragment implements SkullStatistics, MaterialFunction {
    @Override
    public String getURL() {
        return "6ee32fbd4c7b03b869078aa1f493a390e6e13b461d613707eafb326dbcd2b4b5";
    }

    @Override
    public String getDisplayName() {
        return "Strong Dragon Fragment";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.EPIC;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ITEM;
    }
}
