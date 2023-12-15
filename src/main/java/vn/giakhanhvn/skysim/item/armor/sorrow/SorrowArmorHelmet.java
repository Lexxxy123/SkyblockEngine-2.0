package vn.giakhanhvn.skysim.item.armor.sorrow;

import vn.giakhanhvn.skysim.item.SpecificItemType;
import vn.giakhanhvn.skysim.item.GenericItemType;
import vn.giakhanhvn.skysim.item.Rarity;
import vn.giakhanhvn.skysim.item.MaterialFunction;
import vn.giakhanhvn.skysim.item.ToolStatistics;

public class SorrowArmorHelmet implements ToolStatistics, MaterialFunction {
    @Override
    public String getDisplayName() {
        return "Sorrow Helmet";
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
        return SpecificItemType.HELMET;
    }

    @Override
    public double getBaseMagicFind() {
        return 0.05;
    }

    @Override
    public double getBaseDefense() {
        return 100.0;
    }
}
