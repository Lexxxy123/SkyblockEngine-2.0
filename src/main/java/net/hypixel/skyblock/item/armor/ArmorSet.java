package net.hypixel.skyblock.item.armor;

import org.bukkit.entity.Player;
import net.hypixel.skyblock.item.MaterialStatistics;
import net.hypixel.skyblock.item.PlayerBoostStatistics;

public interface ArmorSet {
    String getName();

    String getDescription();

    Class<? extends MaterialStatistics> getHelmet();

    Class<? extends MaterialStatistics> getChestplate();

    Class<? extends MaterialStatistics> getLeggings();

    Class<? extends MaterialStatistics> getBoots();

    default PlayerBoostStatistics whileHasFullSet(final Player player) {
        return null;
    }
}
