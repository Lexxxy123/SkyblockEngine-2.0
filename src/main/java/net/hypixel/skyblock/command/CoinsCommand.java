/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.ConsoleCommandSender
 */
package net.hypixel.skyblock.command;

import net.hypixel.skyblock.command.CommandArgumentException;
import net.hypixel.skyblock.command.CommandFailException;
import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

@CommandParameters(description="Modify your coin amount.", permission=PlayerRank.ADMIN)
public class CoinsCommand
extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        String lowerCase;
        if (args.length != 0 && args.length != 2) {
            throw new CommandArgumentException();
        }
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        User user = sender.getUser();
        if (args.length == 0) {
            user.setPermanentCoins(!user.isPermanentCoins());
            this.send(ChatColor.GREEN + "Your coins are no" + (user.isPermanentCoins() ? "w" : " longer") + " permanent.");
            return;
        }
        long coins = Long.parseLong(args[1]);
        switch (lowerCase = args[0].toLowerCase()) {
            case "add": {
                user.addCoins(coins);
                this.send(ChatColor.GREEN + "You (or someone) have added " + ChatColor.GOLD + SUtil.commaify(coins) + ChatColor.GREEN + " to your purse.");
                return;
            }
            case "subtract": 
            case "sub": 
            case "take": {
                user.subCoins(coins);
                this.send(ChatColor.GREEN + "You (or someone) have subtracted " + ChatColor.GOLD + SUtil.commaify(coins) + ChatColor.GREEN + " from your Purse.");
                return;
            }
            case "set": {
                user.setCoins(coins);
                this.send(ChatColor.GREEN + "You (or someone) have set your Purse coins to " + ChatColor.GOLD + SUtil.commaify(coins) + ".");
                return;
            }
        }
        throw new CommandArgumentException();
    }
}

