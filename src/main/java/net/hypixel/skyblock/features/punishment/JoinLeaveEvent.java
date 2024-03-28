package net.hypixel.skyblock.features.punishment;


import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.listener.PListener;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class JoinLeaveEvent extends PListener {
    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        String uuid = event.getPlayer().getUniqueId().toString();
        File playerfile = new File(( SkyBlock.getPlugin(SkyBlock.class)).getDataFolder() + File.separator, "punishments.yml");
        YamlConfiguration loadConfiguration = YamlConfiguration.loadConfiguration(playerfile);
        if (loadConfiguration.contains(uuid) && loadConfiguration.getBoolean(String.valueOf(uuid) + ".ban.isbanned")) {
            event.setQuitMessage((String) null);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        File playerfile = new File(((SkyBlock) SkyBlock.getPlugin(SkyBlock.class)).getDataFolder() + File.separator, "punishments.yml");
        YamlConfiguration loadConfiguration = YamlConfiguration.loadConfiguration(playerfile);
        String uuid = event.getPlayer().getUniqueId().toString();
        long unixTime = System.currentTimeMillis() / 1000;
        if (loadConfiguration.contains(uuid) && loadConfiguration.getBoolean(String.valueOf(uuid) + ".ban.isbanned")) {
            if (loadConfiguration.getInt(String.valueOf(uuid) + ".ban.length") <= unixTime && loadConfiguration.getInt(String.valueOf(uuid) + ".ban.length") != -1) {
                try {
                    loadConfiguration.set(String.valueOf(uuid) + ".ban.isbanned", false);
                    loadConfiguration.set(String.valueOf(uuid) + ".ban.reason", "");
                    loadConfiguration.set(String.valueOf(uuid) + ".ban.length", 0);
                    loadConfiguration.set(String.valueOf(uuid) + ".ban.id", "");
                    loadConfiguration.save(playerfile);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
            event.setJoinMessage((String) null);
            if (loadConfiguration.getInt(String.valueOf(uuid) + ".ban.length") == -1) {
                event.getPlayer().kickPlayer("§cYou are permanently banned from this server!\n\n§7Reason: §f" + loadConfiguration.getString(String.valueOf(uuid) + ".ban.reason") + "\n§7Find out more: §b§n" + "https://discord.gg/godspunky" + "\n\n§7Ban ID: §f#" + loadConfiguration.getString(String.valueOf(uuid) + ".ban.id") + "\n§7Sharing your Ban ID may affect the processing of your appeal!");
            } else if (loadConfiguration.getInt(String.valueOf(uuid) + ".ban.length") == 0) {
                return;
            } else {
                event.getPlayer().kickPlayer("§cYou are temporarily banned for §f" + calculateTime(loadConfiguration.getInt(String.valueOf(uuid) + ".ban.length") - unixTime) + " §cfrom this server!\n\n§7Reason: §f" + loadConfiguration.getString(String.valueOf(uuid) + ".ban.reason") + "\n§7Find out more: §b§n" + "https://discord.gg/godspunky" + "\n\n§7Ban ID: §f#" + loadConfiguration.getString(String.valueOf(uuid) + ".ban.id") + "\n§7Sharing your Ban ID may affect the processing of your appeal!");
            }
        }
        if (!loadConfiguration.contains(uuid)) {
            try {
                loadConfiguration.createSection(uuid);
                loadConfiguration.set(String.valueOf(uuid) + ".name", event.getPlayer().getName());
                loadConfiguration.createSection(String.valueOf(uuid) + ".ban");
                loadConfiguration.createSection(String.valueOf(uuid) + ".mute");
                loadConfiguration.set(String.valueOf(uuid) + ".ban.isbanned", false);
                loadConfiguration.set(String.valueOf(uuid) + ".ban.reason", "");
                loadConfiguration.set(String.valueOf(uuid) + ".ban.length", 0);
                loadConfiguration.set(String.valueOf(uuid) + ".ban.id", "");
                loadConfiguration.set(String.valueOf(uuid) + ".mute.ismuted", false);
                loadConfiguration.set(String.valueOf(uuid) + ".mute.reason", "");
                loadConfiguration.set(String.valueOf(uuid) + ".mute.length", 0);
                loadConfiguration.set(String.valueOf(uuid) + ".mute.id", "");
                loadConfiguration.save(playerfile);
            } catch (IOException exception2) {
                exception2.printStackTrace();
            }
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
