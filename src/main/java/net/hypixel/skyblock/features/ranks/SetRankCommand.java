/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.features.ranks;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class SetRankCommand
implements CommandExecutor {
    private File rankFile = new File("plugins/SkyblockEngine/rank.yml");
    private FileConfiguration rankConfig;

    public SetRankCommand() {
        if (!this.rankFile.exists()) {
            try {
                this.rankFile.getParentFile().mkdirs();
                this.rankFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.rankConfig = YamlConfiguration.loadConfiguration((File)this.rankFile);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Sputnik.trans("&cOnly players can use this command."));
            return false;
        }
        Player player = (Player)sender;
        User user = User.getUser(player);
        if (!sender.isOp() && Objects.requireNonNull(user).rank != PlayerRank.ADMIN) {
            sender.sendMessage(Sputnik.trans("&cYou need ADMIN rank to use this command."));
            return false;
        }
        if (args.length < 2) {
            sender.sendMessage(Sputnik.trans("&cUsage: /setrank <player> <rank>"));
            return false;
        }
        try {
            Player target = Bukkit.getPlayer((String)args[0]);
            if (target == null) {
                sender.sendMessage(Sputnik.trans("&cPlayer not found!"));
                return false;
            }
            PlayerRank newRank = PlayerRank.valueOf(args[1].toUpperCase().replace("+", "P"));
            User.getUser(target).setRank(newRank);
            this.savePlayerRank(target.getUniqueId().toString(), newRank);
            String prefix = newRank == PlayerRank.DEFAULT ? "&7Default" : newRank.getPrefix().replace("[", "").replace("]", "");
            sender.sendMessage(Sputnik.trans("&aSet " + args[0] + "'s Rank To " + prefix + "&a!"));
            return true;
        } catch (IllegalArgumentException e) {
            sender.sendMessage(Sputnik.trans("&cInvalid rank specified."));
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            sender.sendMessage(Sputnik.trans("&cAn error occurred while setting the rank."));
            return false;
        }
    }

    private void savePlayerRank(String playerId, PlayerRank rank) {
        this.rankConfig.set(playerId + ".rank", (Object)rank.name());
        try {
            this.rankConfig.save(this.rankFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

