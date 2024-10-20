/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 */
package net.hypixel.skyblock.item;

import com.google.common.util.concurrent.AtomicDouble;
import net.hypixel.skyblock.item.SItem;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public interface MaterialFunction {
    default public void onInteraction(PlayerInteractEvent e2) {
    }

    default public void onInventoryClick(SItem instance, InventoryClickEvent e2) {
    }

    default public void onDamage(Entity damaged, Player damager, AtomicDouble damage, SItem item) {
    }

    default public void onKill(Entity damaged, Player damager, SItem item) {
    }

    default public void whileHolding(Player holding) {
    }

    default public void onInstanceUpdate(SItem instance) {
    }
}

