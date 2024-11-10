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

@CommandParameters(description="tempban player", aliases="tempban", permission=PlayerRank.MOD)
public class TempBan
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
            long banTime = TempBan.parsePeriod(args[1]) / 1000L - 1L;
            if (banTime < 59L) {
                sender.send("\u00a7cYou can not ban someone for less than 1 minute.");
            } else if (loadConfiguration.contains(uuid)) {
                if (!loadConfiguration.getBoolean(String.valueOf(uuid) + ".ban.isbanned")) {
                    try {
                        String pwd = RandomStringUtils.random((int)8, (String)"ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
                        loadConfiguration.set(String.valueOf(uuid) + ".ban.isbanned", (Object)true);
                        loadConfiguration.set(String.valueOf(uuid) + ".ban.reason", (Object)reason2);
                        loadConfiguration.set(String.valueOf(uuid) + ".ban.length", (Object)(unixTime + banTime));
                        loadConfiguration.set(String.valueOf(uuid) + ".ban.id", (Object)pwd);
                        loadConfiguration.save(playerfile);
                        if (target != null) {
                            sender.send("\u00a7aTempbanned " + Bukkit.getPlayer((String)args[0]).getName() + " for " + args[1] + " for " + reason2);
                            target.kickPlayer("\u00a7cYou are temporarily banned for \u00a7f" + TempBan.calculateTime((long)loadConfiguration.getInt(String.valueOf(uuid) + ".ban.length") - unixTime) + " \u00a7cfrom this server!\n\n\u00a77Reason: \u00a7f" + loadConfiguration.getString(String.valueOf(uuid) + ".ban.reason") + "\n\u00a77Find out more: \u00a7b\u00a7nhttps://discord.gg/funpixel\n\n\u00a77Ban ID: \u00a7f#" + loadConfiguration.getString(String.valueOf(uuid) + ".ban.id") + "\n\u00a77Sharing your Ban ID may affect the processing of your appeal!");
                        } else {
                            sender.send("\u00a7aTempbanned " + args[0] + " for " + args[1] + " for " + reason2);
                        }
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
                sender.send("\u00a7cPlayer is already banned!");
            }
        }
        sender.send("\u00a7cInvalid syntax. Correct: /tempban <name> <length> <reason>");
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

