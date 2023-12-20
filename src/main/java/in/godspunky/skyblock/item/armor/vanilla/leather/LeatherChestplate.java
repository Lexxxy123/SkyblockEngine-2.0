package in.godspunky.skyblock.item.armor.vanilla.leather;

import in.godspunky.skyblock.item.*;
import in.godspunky.skyblock.item.*;

public class LeatherChestplate implements ToolStatistics, MaterialFunction {
    @Override
    public String getDisplayName() {
        return "Leather Chestplate";
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
        return SpecificItemType.CHESTPLATE;
    }

    @Override
    public double getBaseDefense() {
        return 15.0;
    }
}
