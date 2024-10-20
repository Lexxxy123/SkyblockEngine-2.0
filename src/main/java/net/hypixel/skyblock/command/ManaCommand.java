/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.command;

import java.util.UUID;
import net.hypixel.skyblock.Repeater;
import net.hypixel.skyblock.command.CommandArgumentException;
import net.hypixel.skyblock.command.CommandFailException;
import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description="Modify your mana amount.", permission=PlayerRank.ADMIN)
public class ManaCommand
extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        if (args.length != 1) {
            throw new CommandArgumentException();
        }
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        Player player = sender.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!Repeater.MANA_MAP.containsKey(uuid)) {
            throw new CommandFailException("Something went wrong!");
        }
        int set = Integer.parseInt(args[0]);
        Repeater.MANA_MAP.remove(uuid);
        Repeater.MANA_MAP.put(uuid, set);
        this.send(ChatColor.GREEN + "Your mana is now " + ChatColor.AQUA + set + ".");
    }
}

