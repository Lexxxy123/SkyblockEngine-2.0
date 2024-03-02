package net.hypixel.skyblock.item.armor.vanilla.diamond;

import net.hypixel.skyblock.item.*;
import net.hypixel.skyblock.item.*;

public class DiamondBoots implements ToolStatistics, MaterialFunction {
    @Override
    public String getDisplayName() {
        return "Diamond Boots";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.UNCOMMON;
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
        return 15.0;
    }
}
