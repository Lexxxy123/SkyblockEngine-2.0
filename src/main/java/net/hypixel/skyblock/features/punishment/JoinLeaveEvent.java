/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 */
package net.hypixel.skyblock.features.punishment;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.config.Config;
import net.hypixel.skyblock.listener.PListener;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveEvent
extends PListener {
    public static Config config = SkyBlock.getPlugin().config;

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        String uuid = event.getPlayer().getUniqueId().toString();
        File playerfile = new File(((SkyBlock)SkyBlock.getPlugin(SkyBlock.class)).getDataFolder() + File.separator, "punishments.yml");
        YamlConfiguration loadConfiguration = YamlConfiguration.loadConfiguration((File)playerfile);
        if (loadConfiguration.contains(uuid) && loadConfiguration.getBoolean(String.valueOf(uuid) + ".ban.isbanned")) {
            event.setQuitMessage((String)null);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        File playerfile = new File(((SkyBlock)SkyBlock.getPlugin(SkyBlock.class)).getDataFolder() + File.separator, "punishments.yml");
        YamlConfiguration loadConfiguration = YamlConfiguration.loadConfiguration((File)playerfile);
        String uuid = event.getPlayer().getUniqueId().toString();
        long unixTime = System.currentTimeMillis() / 1000L;
        if (loadConfiguration.contains(uuid) && loadConfiguration.getBoolean(String.valueOf(uuid) + ".ban.isbanned")) {
            if ((long)loadConfiguration.getInt(String.valueOf(uuid) + ".ban.length") <= unixTime && loadConfiguration.getInt(String.valueOf(uuid) + ".ban.length") != -1) {
                try {
                    loadConfiguration.set(String.valueOf(uuid) + ".ban.isbanned", (Object)false);
                    loadConfiguration.set(String.valueOf(uuid) + ".ban.reason", (Object)"");
                    loadConfiguration.set(String.valueOf(uuid) + ".ban.length", (Object)0);
                    loadConfiguration.set(String.valueOf(uuid) + ".ban.id", (Object)"");
                    loadConfiguration.save(playerfile);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
            event.setJoinMessage((String)null);
            if (loadConfiguration.getInt(String.valueOf(uuid) + ".ban.length") == -1) {
                event.getPlayer().kickPlayer("\u00a7cYou are permanently banned from this server!\n\n\u00a77Reason: \u00a7f" + loadConfiguration.getString(String.valueOf(uuid) + ".ban.reason") + "\n\u00a77Find out more: \u00a7b\u00a7n" + config.getString("discord") + "\n\n\u00a77Ban ID: \u00a7f#" + loadConfiguration.getString(String.valueOf(uuid) + ".ban.id") + "\n\u00a77Sharing your Ban ID may affect the processing of your appeal!");
            } else {
                if (loadConfiguration.getInt(String.valueOf(uuid) + ".ban.length") == 0) {
                    return;
                }
                event.getPlayer().kickPlayer("\u00a7cYou are temporarily banned for \u00a7f" + JoinLeaveEvent.calculateTime((long)loadConfiguration.getInt(String.valueOf(uuid) + ".ban.length") - unixTime) + " \u00a7cfrom this server!\n\n\u00a77Reason: \u00a7f" + loadConfiguration.getString(String.valueOf(uuid) + ".ban.reason") + "\n\u00a77Find out more: \u00a7b\u00a7n" + config.getString("discord") + "\n\n\u00a77Ban ID: \u00a7f#" + loadConfiguration.getString(String.valueOf(uuid) + ".ban.id") + "\n\u00a77Sharing your Ban ID may affect the processing of your appeal!");
            }
        }
        if (!loadConfiguration.contains(uuid)) {
            try {
                loadConfiguration.createSection(uuid);
                loadConfiguration.set(String.valueOf(uuid) + ".name", (Object)event.getPlayer().getName());
                loadConfiguration.createSection(String.valueOf(uuid) + ".ban");
                loadConfiguration.createSection(String.valueOf(uuid) + ".mute");
                loadConfiguration.set(String.valueOf(uuid) + ".ban.isbanned", (Object)false);
                loadConfiguration.set(String.valueOf(uuid) + ".ban.reason", (Object)"");
                loadConfiguration.set(String.valueOf(uuid) + ".ban.length", (Object)0);
                loadConfiguration.set(String.valueOf(uuid) + ".ban.id", (Object)"");
                loadConfiguration.set(String.valueOf(uuid) + ".mute.ismuted", (Object)false);
                loadConfiguration.set(String.valueOf(uuid) + ".mute.reason", (Object)"");
                loadConfiguration.set(String.valueOf(uuid) + ".mute.length", (Object)0);
                loadConfiguration.set(String.valueOf(uuid) + ".mute.id", (Object)"");
                loadConfiguration.save(playerfile);
            } catch (IOException exception2) {
                exception2.printStackTrace();
            }
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

