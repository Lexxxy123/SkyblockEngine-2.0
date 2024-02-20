package in.godspunky.skyblock.item.armor.sorrow;

import in.godspunky.skyblock.item.*;
import in.godspunky.skyblock.item.*;

public class SorrowArmorLeggings implements ToolStatistics, MaterialFunction {
    @Override
    public String getDisplayName() {
        return "Sorrow Leggings";
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
        return SpecificItemType.LEGGINGS;
    }

    @Override
    public double getBaseMagicFind() {
        return 0.05;
    }

    @Override
    public double getBaseDefense() {
        return 150.0;
    }
}
