package vn.giakhanhvn.skysim.item.pickaxe.vanilla;

import vn.giakhanhvn.skysim.item.SpecificItemType;
import vn.giakhanhvn.skysim.item.GenericItemType;
import vn.giakhanhvn.skysim.item.Rarity;
import vn.giakhanhvn.skysim.item.MaterialFunction;
import vn.giakhanhvn.skysim.item.ToolStatistics;

public class IronPickaxe implements ToolStatistics, MaterialFunction {
    @Override
    public String getDisplayName() {
        return "Iron Pickaxe";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
    }

    @Override
    public int getBaseDamage() {
        return 25;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.TOOL;
    }

    @Override
    public SpecificItemType getSpecificType() {
        return SpecificItemType.PICKAXE;
    }
}
