package net.hypixel.skyblock.command;


import java.io.File;
import java.io.IOException;

import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

@CommandParameters(description = "ban player", aliases = "ban", permission = PlayerRank.MOD)
public class Ban extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
            if (args.length >= 2) {
                String reason = "";
                for (int i = 1; i < args.length; i++) {
                    reason = String.valueOf(reason) + args[i] + " ";
                }
                String reason2 = reason.substring(0, reason.length() - 1);
                Player target = Bukkit.getPlayerExact(args[0]);
                File playerfile = new File((SkyBlock.getPlugin(SkyBlock.class)).getDataFolder() + File.separator, "punishments.yml");
                YamlConfiguration loadConfiguration = YamlConfiguration.loadConfiguration(playerfile);
                String uuid = null;
                if (target != null) {
                    uuid = target.getPlayer().getUniqueId().toString();
                }
                if (uuid == null) {
                    for (String key : loadConfiguration.getKeys(false)) {
                        if (loadConfiguration.getString(String.valueOf(key) + ".name").equalsIgnoreCase(args[0])) {
                            uuid = key;
                        }
                    }
                }
                if (uuid == null) {
                    sender.send("§cPlayer does not exist.");
                } else if (loadConfiguration.contains(uuid)) {
                    if (!loadConfiguration.getBoolean(String.valueOf(uuid) + ".ban.isbanned")) {
                        try {
                            String pwd = RandomStringUtils.random(8, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
                            loadConfiguration.set(String.valueOf(uuid) + ".ban.isbanned", true);
                            loadConfiguration.set(String.valueOf(uuid) + ".ban.reason", reason2);
                            loadConfiguration.set(String.valueOf(uuid) + ".ban.length", -1);
                            loadConfiguration.set(String.valueOf(uuid) + ".ban.id", pwd);
                            loadConfiguration.save(playerfile);
                            if (target == null) {
                                sender.send("§aPermanently banned " + args[0] + " for " + reason2);
                            }
                            if (target != null) {
                                sender.send("§aPermanently banned " + Bukkit.getPlayer(args[0]).getName() + " for " + reason2);
                                target.getPlayer().kickPlayer("§cYou are permanently banned from this server!\n\n§7Reason: §f" + loadConfiguration.getString(String.valueOf(uuid) + ".ban.reason") + "\n§7Find out more: §b§n" + "https://discord.gg/godspunky" + "\n\n§7Ban ID: §f#" + loadConfiguration.getString(String.valueOf(uuid) + ".ban.id") + "\n§7Sharing your Ban ID may affect the processing of your appeal!");
                            }
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    }
                    sender.send("§cPlayer is already banned!");
                }
            }
            sender.send("§cInvalid syntax. Correct: /ban <name> <reason>");
    }
}
