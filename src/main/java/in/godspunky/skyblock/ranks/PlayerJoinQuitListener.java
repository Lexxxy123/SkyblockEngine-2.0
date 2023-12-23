package in.godspunky.skyblock.ranks;

import in.godspunky.skyblock.SkySimEngine;
import in.godspunky.skyblock.listener.PListener;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitListener extends PListener {
    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent e) {
        Bukkit.getScheduler().runTaskAsynchronously(SkySimEngine.getPlugin(), () -> new GodspunkyPlayer(e.getPlayer()));
    }
    @EventHandler
    public void playerQuitEvent(PlayerQuitEvent e) {
        Bukkit.getScheduler().runTaskAsynchronously(SkySimEngine.getPlugin(), () -> GodspunkyPlayer.unloadPlayer(e.getPlayer()));
    }
}
