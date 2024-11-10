/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.command;

import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandParameters(description="kick player", aliases="kick", permission=PlayerRank.HELPER)
public class Kick
extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        if (args.length >= 2) {
            String reason = "";
            for (int i = 1; i < args.length; ++i) {
                reason = String.valueOf(reason) + args[i] + " ";
            }
            Player target = Bukkit.getPlayerExact((String)args[0]);
            if (target == null) {
                sender.send("\u00a7cPlayer does not exist or offline.");
            }
            sender.send("\u00a7aKicked player " + Bukkit.getPlayer((String)args[0]).getName() + " for " + reason);
            target.kickPlayer("\u00a7cYou have been kicked!\n\n\u00a77Reason: \u00a7f" + reason + "\n\u00a77Find out more: \u00a7b\u00a7nhttps://discord.gg/funpixel");
        }
        sender.send("\u00a7cInvalid syntax. Correct: /kick <name> <reason>");
    }
}

