package in.godspunky.skyblock.item.oddities;

import in.godspunky.skyblock.item.GenericItemType;
import in.godspunky.skyblock.item.MaterialFunction;
import in.godspunky.skyblock.item.Rarity;
import in.godspunky.skyblock.item.SkullStatistics;
import org.bukkit.ChatColor;

public class RemnantOfTheEye implements SkullStatistics, MaterialFunction {
    @Override
    public String getDisplayName() {
        return "Remnant of the Eye";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.EPIC;
    }

    @Override
    public String getLore() {
        return "Keeps you alive when you are on death's door, granting ashort period of invincibility. Consumed on use. " + ChatColor.RED + "NOTE: ONLY WORKS ON THE END ISLAND";
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
        return "7d389c55ecf7db572d6961ce3d57b572e761397b67a2d6d94c72fc91dddd74";
    }
}
