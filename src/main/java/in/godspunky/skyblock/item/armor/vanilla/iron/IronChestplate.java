package in.godspunky.skyblock.item.armor.vanilla.iron;

import in.godspunky.skyblock.item.*;
import in.godspunky.skyblock.item.*;

public class IronChestplate implements ToolStatistics, MaterialFunction {
    @Override
    public String getDisplayName() {
        return "Iron Chestplate";
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
        return 30.0;
    }
}
