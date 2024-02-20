package in.godspunky.skyblock.item.armor.eleganttux;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import in.godspunky.skyblock.item.MaterialStatistics;
import in.godspunky.skyblock.item.SItem;
import in.godspunky.skyblock.item.armor.TickingSet;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
