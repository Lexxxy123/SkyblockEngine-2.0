/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Effect
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.command;

import net.hypixel.skyblock.command.CommandArgumentException;
import net.hypixel.skyblock.command.CommandFailException;
import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description="Play a Bukkit enum sound.", usage="/playenumsound <sound>", permission=PlayerRank.ADMIN)
public class PlayEnumEffectCommand
extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        if (args.length < 1 || args.length > 2) {
            throw new CommandArgumentException();
        }
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        Player player = sender.getPlayer();
        Effect effect = Effect.valueOf((String)args[0].toUpperCase());
        int count = 1;
        if (args.length == 2) {
            count = Integer.parseInt(args[1]);
        }
        for (int i2 = 0; i2 < count; ++i2) {
            player.getWorld().playEffect(player.getLocation(), effect, (Object)effect.getData());
        }
        player.sendMessage(ChatColor.GRAY + "Played " + effect.name() + ".");
    }
}

