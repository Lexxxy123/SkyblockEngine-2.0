package in.godspunky.skyblock.item.armor.lapis;

import in.godspunky.skyblock.item.*;
import in.godspunky.skyblock.item.*;

public class LapisArmorHelmet implements ToolStatistics, MaterialFunction {
    @Override
    public String getDisplayName() {
        return "Lapis Armor Helmet";
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
        return SpecificItemType.HELMET;
    }

    @Override
    public double getBaseDefense() {
        return 25.0;
    }
}
