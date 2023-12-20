package in.godspunky.skyblock.item.mining;

import in.godspunky.skyblock.item.GenericItemType;
import in.godspunky.skyblock.item.MaterialFunction;
import in.godspunky.skyblock.item.MaterialStatistics;
import in.godspunky.skyblock.item.Rarity;
import in.godspunky.skyblock.util.Sputnik;

public class Mitril implements MaterialStatistics, MaterialFunction {
    @Override
    public String getDisplayName() {
        return "Mithril";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.COMMON;
    }

    @Override
    public String getLore() {
        return Sputnik.trans("&7&o\"The man called it \"true-silver\" while the Dwarves, who loved it above all other things, had their own, secret name for it.\"");
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ITEM;
    }

    @Override
    public boolean isEnchanted() {
        return false;
    }

    @Override
    public boolean isStackable() {
        return true;
    }
}
