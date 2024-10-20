/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.item.armor.necron;

import net.hypixel.skyblock.item.MaterialStatistics;
import net.hypixel.skyblock.item.PlayerBoostStatistics;
import net.hypixel.skyblock.item.armor.ArmorSet;
import net.hypixel.skyblock.item.armor.necron.NecronBoots;
import net.hypixel.skyblock.item.armor.necron.NecronChestplate;
import net.hypixel.skyblock.item.armor.necron.NecronHelmet;
import net.hypixel.skyblock.item.armor.necron.NecronLeggings;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class NecronFullSet
implements ArmorSet {
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
        return NecronHelmet.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getChestplate() {
        return NecronChestplate.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getLeggings() {
        return NecronLeggings.class;
    }

    @Override
    public Class<? extends MaterialStatistics> getBoots() {
        return NecronBoots.class;
    }

    @Override
    public PlayerBoostStatistics whileHasFullSet(Player player) {
        return null;
    }
}

