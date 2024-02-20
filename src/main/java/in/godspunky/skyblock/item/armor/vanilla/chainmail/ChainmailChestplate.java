package in.godspunky.skyblock.item.armor.vanilla.chainmail;

import in.godspunky.skyblock.item.*;
import in.godspunky.skyblock.item.*;

public class ChainmailChestplate implements ToolStatistics, MaterialFunction {
    @Override
    public String getDisplayName() {
        return "Chainmail Chestplate";
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
