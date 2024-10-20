/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.command;

import net.hypixel.skyblock.command.CommandFailException;
import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.user.PlayerUtils;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description="bruhbu", aliases="resetcb", permission=PlayerRank.ADMIN)
public class ResetCookieCommand
extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        Player player = sender.getPlayer();
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        User user = sender.getUser();
        if (!player.isOp()) {
            this.send(ChatColor.RED + "No permission to execute this command!");
            return;
        }
        if (0 == args.length) {
            this.send(ChatColor.RED + "Invaild Syntax! You need to provide a player");
            return;
        }
        Player target = Bukkit.getPlayer((String)args[0]);
        if (null != target) {
            PlayerUtils.setCookieDurationTicks(target, 0L);
            this.send(Sputnik.trans("&aReseted " + target.getName() + "'s &dCookie Buff&a."));
            target.sendMessage(Sputnik.trans("&e[WARNING] ") + ChatColor.RED + player.getName() + " have reseted your Cookie Buff. If you believe this is an error, contact Admins.");
            return;
        }
        this.send(ChatColor.RED + "Invaild Syntax! You need to provide a vaild player");
    }
}

