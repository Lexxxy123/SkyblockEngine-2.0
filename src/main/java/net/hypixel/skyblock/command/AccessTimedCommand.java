/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Sound
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.command;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.hypixel.skyblock.command.CommandFailException;
import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description="Hidden command.", aliases="attc", permission=PlayerRank.DEFAULT)
public class AccessTimedCommand
extends SCommand {
    public static final List<UUID> KEYS = new ArrayList<UUID>();

    @Override
    public void run(CommandSource sender, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        Player p = sender.getPlayer();
        if (2 != args.length) {
            this.send(ChatColor.RED + "System Command! You don't have access to it.");
            p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, -4.0f);
            return;
        }
        if (!KEYS.contains(UUID.fromString(args[0]))) {
            this.send(ChatColor.RED + "The requested action is no longer available!");
            p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, -4.0f);
            return;
        }
        p.chat("/trade " + args[1]);
        KEYS.remove(UUID.fromString(args[0]));
    }
}

