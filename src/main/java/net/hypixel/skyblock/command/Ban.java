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
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.config.Config;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

@CommandParameters(description="ban player", aliases="ban", permission=PlayerRank.MOD)
public class Ban
extends SCommand {
    public static Config config = SkyBlock.getInstance().config;

    @Override
    public void run(CommandSource sender, String[] args) {
        if (args.length >= 2) {
            String reason = "";
            for (int i2 = 1; i2 < args.length; ++i2) {
                reason = String.valueOf(reason) + args[i2] + " ";
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
            } else if (loadConfiguration.contains(uuid)) {
                if (!loadConfiguration.getBoolean(String.valueOf(uuid) + ".ban.isbanned")) {
                    try {
                        String pwd = RandomStringUtils.random((int)8, (String)"ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
                        loadConfiguration.set(String.valueOf(uuid) + ".ban.isbanned", (Object)true);
                        loadConfiguration.set(String.valueOf(uuid) + ".ban.reason", (Object)reason2);
                        loadConfiguration.set(String.valueOf(uuid) + ".ban.length", (Object)-1);
                        loadConfiguration.set(String.valueOf(uuid) + ".ban.id", (Object)pwd);
                        loadConfiguration.save(playerfile);
                        if (target == null) {
                            sender.send("\u00a7aPermanently banned " + args[0] + " for " + reason2);
                        }
                        if (target != null) {
                            sender.send("\u00a7aPermanently banned " + Bukkit.getPlayer((String)args[0]).getName() + " for " + reason2);
                            target.getPlayer().kickPlayer("\u00a7cYou are permanently banned from this server!\n\n\u00a77Reason: \u00a7f" + loadConfiguration.getString(String.valueOf(uuid) + ".ban.reason") + "\n\u00a77Find out more: \u00a7b\u00a7n" + config.getString("discord") + "\n\n\u00a77Ban ID: \u00a7f#" + loadConfiguration.getString(String.valueOf(uuid) + ".ban.id") + "\n\u00a77Sharing your Ban ID may affect the processing of your appeal!");
                        }
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
                sender.send("\u00a7cPlayer is already banned!");
            }
        }
        sender.send("\u00a7cInvalid syntax. Correct: /ban <name> <reason>");
    }
}

