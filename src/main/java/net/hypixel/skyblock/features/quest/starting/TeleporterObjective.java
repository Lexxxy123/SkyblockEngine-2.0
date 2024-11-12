/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.PlayerMoveEvent
 */
package net.hypixel.skyblock.features.quest.starting;

import net.hypixel.skyblock.features.quest.Objective;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class TeleporterObjective
extends Objective {
    public TeleporterObjective() {
        super("teleporter", "Use the teleporter");
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e2) {
        Location from = e2.getFrom();
        Location to = e2.getTo();
        if (to == null || from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()) {
            if (!this.isThisObjective(e2.getPlayer())) {
                return;
            }
            if (e2.getTo().getBlock().getType().equals((Object)Material.PORTAL)) {
                this.complete(e2.getPlayer());
            }
        }
    }
}

