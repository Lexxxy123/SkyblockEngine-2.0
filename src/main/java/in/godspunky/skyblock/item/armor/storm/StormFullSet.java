package in.godspunky.skyblock.item.armor.storm;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import in.godspunky.skyblock.item.MaterialStatistics;
import in.godspunky.skyblock.item.PlayerBoostStatistics;
import in.godspunky.skyblock.item.armor.ArmorSet;

public class StormFullSet implements ArmorSet {
    @Override
    public String getName() {
        return "Witherborn";
    }

    @Override
    public String getDescription() {
        return "Spawns a wither minion every " + ChatColor.YELLOW + "30 " + ChatColor.GRAY + "seconds up to a maximum of " + ChatColor.GREEN + "1 " + ChatColor.GRAY + "wither. Your withers will travel to and explode on nearby enemies. Reduces the damage you take from withers by " + ChatColor.GREEN + "10% " + ChatColor.GRAY + "per piece.";
    }

    @Override
    public Class<? extends MaterialStatistics> getHelmet() {
        return StormHelmet.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getChestplate() {
        return StormChestplate.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getLeggings() {
        return StormLeggings.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getBoots() {
        return StormBoots.class;
    }

    @Override
    public PlayerBoostStatistics whileHasFullSet(final Player player) {
        return null;
    }
}
