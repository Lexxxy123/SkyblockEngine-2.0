package net.hypixel.skyblock.features.punishment;


import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.listener.PListener;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;


public class PlayerChat extends PListener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        File playerfile = new File(( SkyBlock.getPlugin(SkyBlock.class)).getDataFolder() + File.separator, "punishments.yml");
        YamlConfiguration loadConfiguration = YamlConfiguration.loadConfiguration(playerfile);
        String uuid = event.getPlayer().getUniqueId().toString();
        long unixTime = System.currentTimeMillis() / 1000;
        if (loadConfiguration.contains(uuid) && loadConfiguration.getBoolean(String.valueOf(uuid) + ".mute.ismuted")) {
            if (loadConfiguration.getInt(String.valueOf(uuid) + ".mute.length") <= unixTime) {
                try {
                    loadConfiguration.set(String.valueOf(uuid) + ".mute.ismuted", false);
                    loadConfiguration.set(String.valueOf(uuid) + ".mute.reason", "");
                    loadConfiguration.set(String.valueOf(uuid) + ".mute.length", 0);
                    loadConfiguration.set(String.valueOf(uuid) + ".mute.id", "");
                    loadConfiguration.save(playerfile);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
            if (loadConfiguration.getInt(String.valueOf(uuid) + ".mute.length") <= 0) {
                return;
            }
            player.sendMessage("§c§l§m---------------------------------------------");
            player.sendMessage("§cYou are currently muted for " + loadConfiguration.getString(String.valueOf(uuid) + ".mute.reason") + ".");
            player.sendMessage("§7Your mute will expire in §c" + calculateTime(loadConfiguration.getInt(String.valueOf(uuid) + ".mute.length") - unixTime));
            player.sendMessage("");
            player.sendMessage("§7Find out more here: §e" + "https://discord.gg/godspunky");
            player.sendMessage("§7Mute ID: §f#" + loadConfiguration.getString(String.valueOf(uuid) + ".mute.id"));
            player.sendMessage("§c§l§m---------------------------------------------");
            event.setCancelled(true);
        }
    }

    public static String calculateTime(long seconds) {
        int days = (int) TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (days * 24);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
        long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) * 60);
        String time = (" " + days + "d " + hours + "h " + minute + "m " + second + "s").toString().replace(" 0d", "").replace(" 0h", "").replace(" 0m", "").replace(" 0s", "").replaceFirst(" ", "");
        return time;
    }
}
