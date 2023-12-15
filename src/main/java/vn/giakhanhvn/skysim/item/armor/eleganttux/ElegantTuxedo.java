package vn.giakhanhvn.skysim.item.armor.eleganttux;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;

import vn.giakhanhvn.skysim.item.SItem;
import org.bukkit.entity.Player;
import vn.giakhanhvn.skysim.item.MaterialStatistics;
import net.md_5.bungee.api.ChatColor;
import vn.giakhanhvn.skysim.item.armor.TickingSet;

public class ElegantTuxedo implements TickingSet {
    @Override
    public String getName() {
        return "Dashing!";
    }

    @Override
    public String getDescription() {
        return "Max health set to " + ChatColor.RED + "250❤ " + ChatColor.GRAY + "Deal " + ChatColor.GREEN + "+150% " + ChatColor.GRAY + "more damage!";
    }

    @Override
    public Class<? extends MaterialStatistics> getHelmet() {
        return nullhelm.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getChestplate() {
        return ElegantTuxChestplate.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getLeggings() {
        return ElegantTuxLeggings.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getBoots() {
        return ElegantTuxBoots.class;
    }

    @Override
    public void tick(final Player owner, final SItem helmet, final SItem chestplate, final SItem leggings, final SItem boots, final List<AtomicInteger> counters) {
    }
}
