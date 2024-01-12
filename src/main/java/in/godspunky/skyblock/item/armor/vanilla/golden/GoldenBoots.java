package in.godspunky.skyblock.item.armor.vanilla.golden;

import in.godspunky.skyblock.item.*;

public class GoldenBoots implements ToolStatistics, MaterialFunction {
    @Override
    public String getDisplayName() {
        return "Golden Boots";
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
        return SpecificItemType.BOOTS;
    }

    @Override
    public double getBaseDefense() {
        return 5.0;
    }
}
