package in.godspunky.skyblock.item.armor.eleganttux;

import in.godspunky.skyblock.item.*;

public class nullhelm implements ToolStatistics, MaterialFunction {
    @Override
    public String getDisplayName() {
        return "null";
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
        return 25.0;
    }
}
