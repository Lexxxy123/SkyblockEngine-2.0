/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.AsyncPlayerChatEvent
 */
package net.hypixel.skyblock.features.punishment;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.config.Config;
import net.hypixel.skyblock.listener.PListener;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChat
extends PListener {
    public static Config config = SkyBlock.getInstance().config;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        File playerfile = new File(((SkyBlock)SkyBlock.getPlugin(SkyBlock.class)).getDataFolder() + File.separator, "punishments.yml");
        YamlConfiguration loadConfiguration = YamlConfiguration.loadConfiguration((File)playerfile);
        String uuid = event.getPlayer().getUniqueId().toString();
        long unixTime = System.currentTimeMillis() / 1000L;
        if (loadConfiguration.contains(uuid) && loadConfiguration.getBoolean(String.valueOf(uuid) + ".mute.ismuted")) {
            if ((long)loadConfiguration.getInt(String.valueOf(uuid) + ".mute.length") <= unixTime) {
                try {
                    loadConfiguration.set(String.valueOf(uuid) + ".mute.ismuted", (Object)false);
                    loadConfiguration.set(String.valueOf(uuid) + ".mute.reason", (Object)"");
                    loadConfiguration.set(String.valueOf(uuid) + ".mute.length", (Object)0);
                    loadConfiguration.set(String.valueOf(uuid) + ".mute.id", (Object)"");
                    loadConfiguration.save(playerfile);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
            if (loadConfiguration.getInt(String.valueOf(uuid) + ".mute.length") <= 0) {
                return;
            }
            player.sendMessage("\u00a7c\u00a7l\u00a7m---------------------------------------------");
            player.sendMessage("\u00a7cYou are currently muted for " + loadConfiguration.getString(String.valueOf(uuid) + ".mute.reason") + ".");
            player.sendMessage("\u00a77Your mute will expire in \u00a7c" + PlayerChat.calculateTime((long)loadConfiguration.getInt(String.valueOf(uuid) + ".mute.length") - unixTime));
            player.sendMessage("");
            player.sendMessage("\u00a77Find out more here: \u00a7e" + config.getString("discord"));
            player.sendMessage("\u00a77Mute ID: \u00a7f#" + loadConfiguration.getString(String.valueOf(uuid) + ".mute.id"));
            player.sendMessage("\u00a7c\u00a7l\u00a7m---------------------------------------------");
            event.setCancelled(true);
        }
    }

    public static String calculateTime(long seconds) {
        int days = (int)TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (long)(days * 24);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - TimeUnit.SECONDS.toHours(seconds) * 60L;
        long second = TimeUnit.SECONDS.toSeconds(seconds) - TimeUnit.SECONDS.toMinutes(seconds) * 60L;
        String time = (" " + days + "d " + hours + "h " + minute + "m " + second + "s").toString().replace(" 0d", "").replace(" 0h", "").replace(" 0m", "").replace(" 0s", "").replaceFirst(" ", "");
        return time;
    }
}

