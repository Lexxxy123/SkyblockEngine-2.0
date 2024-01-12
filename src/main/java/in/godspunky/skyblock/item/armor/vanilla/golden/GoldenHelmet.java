package in.godspunky.skyblock.item.armor.vanilla.golden;

import in.godspunky.skyblock.item.*;

public class GoldenHelmet implements ToolStatistics, MaterialFunction {
    @Override
    public String getDisplayName() {
        return "Golden Helmet";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ARMOR;
    }

    @Override
    public SpecificItemType getSpecificType() {
        return SpecificItemType.HELMET;
    }

    @Override
    public double getBaseDefense() {
        return 10.0;
    }
}
