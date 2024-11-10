/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.server.ServerListPingEvent
 */
package net.hypixel.skyblock.listener;

import net.hypixel.skyblock.listener.PListener;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerPingListener
extends PListener {
    @EventHandler
    public void onServerPing(ServerListPingEvent e) {
        e.setMotd(ChatColor.GOLD + "SkyBlock Version 0.7.0 BETA, Starting up...");
    }
}

