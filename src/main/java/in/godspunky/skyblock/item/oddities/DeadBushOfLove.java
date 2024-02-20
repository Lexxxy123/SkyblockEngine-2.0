package in.godspunky.skyblock.item.oddities;

import net.md_5.bungee.api.ChatColor;
import in.godspunky.skyblock.item.GenericItemType;
import in.godspunky.skyblock.item.MaterialFunction;
import in.godspunky.skyblock.item.MaterialStatistics;
import in.godspunky.skyblock.item.Rarity;

public class DeadBushOfLove implements MaterialStatistics, MaterialFunction {
    @Override
    public String getDisplayName() {
        return "Dead Bush of Love";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.SPECIAL;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ITEM;
    }

    @Override
    public boolean isEnchanted() {
        return true;
    }

    @Override
    public String getLore() {
        return "This item was given to the kind souls who helped so much testing SkySim on Beta! Much love " + ChatColor.RED + "❤";
    }

    @Override
    public boolean isStackable() {
        return false;
    }
}
