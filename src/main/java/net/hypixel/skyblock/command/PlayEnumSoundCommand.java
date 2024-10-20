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

import net.hypixel.skyblock.command.CommandArgumentException;
import net.hypixel.skyblock.command.CommandFailException;
import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description="Play a Bukkit enum sound.", usage="/playenumsound <sound>", permission=PlayerRank.ADMIN)
public class PlayEnumSoundCommand
extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        if (args.length < 1 || args.length > 4) {
            throw new CommandArgumentException();
        }
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        Player player = sender.getPlayer();
        Sound sound = Sound.valueOf((String)args[0].toUpperCase());
        float volume = 1.0f;
        float pitch = 1.0f;
        int times = 1;
        if (args.length >= 2) {
            volume = Float.parseFloat(args[1]);
        }
        if (args.length >= 3) {
            pitch = Float.parseFloat(args[2]);
        }
        if (args.length == 4) {
            times = Integer.parseInt(args[3]);
        }
        for (int i2 = 0; i2 < times; ++i2) {
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
        player.sendMessage(ChatColor.GRAY + "Played " + sound.name() + ".");
    }
}

