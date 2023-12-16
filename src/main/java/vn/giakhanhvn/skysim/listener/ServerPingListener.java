package vn.giakhanhvn.skysim.listener;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerPingListener extends PListener {
    @EventHandler
    public void onServerPing(final ServerListPingEvent e) {
        e.setMotd(ChatColor.GOLD + "SkySim Version 0.7.0 BETA, Starting up...");
    }
}
