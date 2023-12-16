package vn.giakhanhvn.skysim.item.armor;

import org.bukkit.entity.Player;
import vn.giakhanhvn.skysim.item.MaterialStatistics;
import vn.giakhanhvn.skysim.item.PlayerBoostStatistics;

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
