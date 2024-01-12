package in.godspunky.skyblock.item.oddities;

import in.godspunky.skyblock.item.GenericItemType;
import in.godspunky.skyblock.item.MaterialFunction;
import in.godspunky.skyblock.item.Rarity;
import in.godspunky.skyblock.item.SkullStatistics;
import org.bukkit.ChatColor;

public class InfiniteEye implements SkullStatistics, MaterialFunction {
    @Override
    public String getDisplayName() {
        return "Infinty Summoning Eye";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.MYTHIC;
    }

    @Override
    public String getLore() {
        return "Use this at the " + ChatColor.DARK_PURPLE + "Ender Altar" + ChatColor.GRAY + " in the " + ChatColor.DARK_PURPLE + "Dragon's Nest" + ChatColor.GRAY + " to summon Ender Dragons! You can use it " + ChatColor.LIGHT_PURPLE + "Infinite";
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ITEM;
    }

    @Override
    public boolean isStackable() {
        return false;
    }

    @Override
    public String getURL() {
        return "12c318157f5c9daf8a7677a3669f9798940aff014a674ee0af2a574cbb21b8c3";
    }
}
