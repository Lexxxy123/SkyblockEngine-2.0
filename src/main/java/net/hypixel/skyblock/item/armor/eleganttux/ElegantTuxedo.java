/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.item.armor.eleganttux;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import net.hypixel.skyblock.item.MaterialStatistics;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.armor.TickingSet;
import net.hypixel.skyblock.item.armor.eleganttux.ElegantTuxBoots;
import net.hypixel.skyblock.item.armor.eleganttux.ElegantTuxChestplate;
import net.hypixel.skyblock.item.armor.eleganttux.ElegantTuxLeggings;
import net.hypixel.skyblock.item.armor.eleganttux.nullhelm;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class ElegantTuxedo
implements TickingSet {
    @Override
    public String getName() {
        return "Dashing!";
    }

    @Override
    public String getDescription() {
        return "Max health set to " + ChatColor.RED + "250\u2764 " + ChatColor.GRAY + "Deal " + ChatColor.GREEN + "+150% " + ChatColor.GRAY + "more damage!";
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
    public void tick(Player owner, SItem helmet, SItem chestplate, SItem leggings, SItem boots, List<AtomicInteger> counters) {
    }
}

