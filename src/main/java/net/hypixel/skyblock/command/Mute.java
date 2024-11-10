/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.RandomStringUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.command;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

@CommandParameters(description="mute player", aliases="mute", permission=PlayerRank.HELPER)
public class Mute
extends SCommand {
    private static final Pattern periodPattern = Pattern.compile("([0-9]+)([hdwmy])");

    @Override
    public void run(CommandSource sender, String[] args) {
        if (args.length >= 3) {
            String reason = "";
            for (int i = 2; i < args.length; ++i) {
                reason = String.valueOf(reason) + args[i] + " ";
            }
            String reason2 = reason.substring(0, reason.length() - 1);
            Player target = Bukkit.getPlayerExact((String)args[0]);
            File playerfile = new File(((SkyBlock)SkyBlock.getPlugin(SkyBlock.class)).getDataFolder() + File.separator, "punishments.yml");
            YamlConfiguration loadConfiguration = YamlConfiguration.loadConfiguration((File)playerfile);
            String uuid = null;
            if (target != null) {
                uuid = target.getPlayer().getUniqueId().toString();
            }
            if (uuid == null) {
                for (String key : loadConfiguration.getKeys(false)) {
                    if (!loadConfiguration.getString(String.valueOf(key) + ".name").equalsIgnoreCase(args[0])) continue;
                    uuid = key;
                }
            }
            if (uuid == null) {
                sender.send("\u00a7cPlayer does not exist.");
            }
            long unixTime = System.currentTimeMillis() / 1000L;
            long muteTime = Mute.parsePeriod(args[1]) / 1000L - 1L;
            if (muteTime < 59L) {
                sender.send("\u00a7cYou can not mute someone for less than 1 minute.");
            } else if (loadConfiguration.contains(uuid)) {
                if (!loadConfiguration.getBoolean(String.valueOf(uuid) + ".mute.ismuted")) {
                    try {
                        loadConfiguration.set(String.valueOf(uuid) + ".mute.ismuted", (Object)true);
                        loadConfiguration.set(String.valueOf(uuid) + ".mute.reason", (Object)reason2);
                        loadConfiguration.set(String.valueOf(uuid) + ".mute.length", (Object)(unixTime + muteTime));
                        String pwd = RandomStringUtils.random((int)8, (String)"ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
                        loadConfiguration.set(String.valueOf(uuid) + ".mute.id", (Object)pwd);
                        loadConfiguration.save(playerfile);
                        if (target != null) {
                            sender.send("\u00a7aMuted " + Bukkit.getPlayer((String)args[0]).getName() + " for " + args[1] + " for " + reason2);
                            target.sendMessage("\u00a7c\u00a7l\u00a7m---------------------------------------------");
                            target.sendMessage("\u00a7cYou are currently muted for " + reason2 + ".");
                            target.sendMessage("\u00a77Your mute will expire in \u00a7c" + Mute.calculateTime((long)loadConfiguration.getInt(String.valueOf(uuid) + ".mute.length") - unixTime));
                            target.sendMessage("");
                            target.sendMessage("\u00a77Find out more here: \u00a7ehttps://discord.gg/funpixel");
                            target.sendMessage("\u00a77Mute ID: \u00a7f#" + loadConfiguration.getString(String.valueOf(uuid) + ".mute.id"));
                            target.sendMessage("\u00a7c\u00a7l\u00a7m---------------------------------------------");
                        } else {
                            sender.send("\u00a7aMuted " + args[0] + " for " + args[1] + " for " + reason2);
                        }
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
                sender.send("\u00a7cPlayer is already muted!");
            }
        }
        sender.send("\u00a7cInvalid syntax. Correct: /mute <name> <length> <reason>");
    }

    public static String calculateTime(long seconds) {
        int days = (int)TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (long)(days * 24);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - TimeUnit.SECONDS.toHours(seconds) * 60L;
        long second = TimeUnit.SECONDS.toSeconds(seconds) - TimeUnit.SECONDS.toMinutes(seconds) * 60L;
        String time = (" " + days + "d " + hours + "h " + minute + "m " + second + "s").toString().replace(" 0d", "").replace(" 0h", "").replace(" 0m", "").replace(" 0s", "").replaceFirst(" ", "");
        return time;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static Long parsePeriod(String period) {
        if (period == null) {
            return null;
        }
        Matcher matcher = periodPattern.matcher(period.toLowerCase(Locale.ENGLISH));
        Instant instant = Instant.EPOCH;
        block10: while (matcher.find()) {
            int num = Integer.parseInt(matcher.group(1));
            switch (matcher.group(2)) {
                case "d": {
                    instant = instant.plus(Duration.ofDays(num));
                    break;
                }
                case "h": {
                    instant = instant.plus(Duration.ofHours(num));
                    break;
                }
                case "m": {
                    instant = instant.plus(Duration.ofMinutes(num));
                    continue block10;
                }
            }
        }
        return instant.toEpochMilli();
    }
}

