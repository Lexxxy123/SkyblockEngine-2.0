package net.hypixel.skyblock.item.armor.vanilla.diamond;

import net.hypixel.skyblock.item.*;
import net.hypixel.skyblock.item.*;

public class DiamondLeggings implements ToolStatistics, MaterialFunction {
    @Override
    public String getDisplayName() {
        return "Diamond Leggings";
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
        return SpecificItemType.LEGGINGS;
    }

    @Override
    public double getBaseDefense() {
        return 30.0;
    }
}
