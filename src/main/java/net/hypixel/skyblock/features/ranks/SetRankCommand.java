/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.features.ranks;

import java.util.Objects;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetRankCommand
implements CommandExecutor {
    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean onCommand(CommandSender sender, Command command, String s2, String[] args) {
        Player player = (Player)sender;
        User user = User.getUser(player);
        if (!sender.isOp() && Objects.requireNonNull(user).rank != PlayerRank.ADMIN && Objects.requireNonNull(user).rank != PlayerRank.ADMIN) {
            sender.sendMessage(Sputnik.trans("&cYou need ADMIN rank to use this command."));
            return false;
        }
        if (args.length < 2) {
            sender.sendMessage(Sputnik.trans("&cUsage: /setrank <player> <rank>"));
            return false;
        }
        try {
            Player target = Bukkit.getPlayer((String)args[0]);
            PlayerRank newRank = PlayerRank.valueOf(args[1].toUpperCase().replace("+", "P"));
            User.getUser(target).setRank(newRank);
            String prefix = newRank == PlayerRank.DEFAULT ? "&7Default" : newRank.getPrefix().replace("[", "").replace("]", "");
            sender.sendMessage(Sputnik.trans("&aSet " + args[0] + "'s Rank To " + prefix + "&a!"));
            return true;
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
    }
}

