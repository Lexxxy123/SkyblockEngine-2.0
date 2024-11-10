/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 */
package net.hypixel.skyblock.item.oddities;

import net.hypixel.skyblock.gui.GUIType;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.MaterialStatistics;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.Untradeable;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SkyBlockMenu
implements MaterialStatistics,
MaterialFunction,
Untradeable {
    @Override
    public String getDisplayName() {
        return ChatColor.GREEN + "SkyBlock Menu " + ChatColor.GRAY + "(Right Click)";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.EXCLUSIVE;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.ITEM;
    }

    @Override
    public String getLore() {
        return "View all of your progress, including your Skills, Collections, Recipes, and more!";
    }

    @Override
    public boolean displayRarity() {
        return false;
    }

    @Override
    public void onInteraction(PlayerInteractEvent e) {
        GUIType.SKYBLOCK_MENU.getGUI().open(e.getPlayer());
    }

    @Override
    public void onInventoryClick(SItem instance, InventoryClickEvent e) {
        e.setCancelled(true);
        GUIType.SKYBLOCK_MENU.getGUI().open((Player)e.getWhoClicked());
    }
}

