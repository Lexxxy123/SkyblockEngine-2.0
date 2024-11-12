/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.EntityHuman
 *  org.bukkit.ChatColor
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.command;

import net.hypixel.skyblock.command.CommandArgumentException;
import net.hypixel.skyblock.command.CommandFailException;
import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.util.SputnikPlayer;
import net.minecraft.server.v1_8_R3.EntityHuman;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.entity.Player;

@CommandParameters(description="Modify your absorption amount.", permission=PlayerRank.ADMIN)
public class AbsorptionCommand
extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        if (1 != args.length) {
            throw new CommandArgumentException();
        }
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        Player player = sender.getPlayer();
        EntityHuman human = ((CraftHumanEntity)player).getHandle();
        float f2 = Float.parseFloat(args[0]);
        SputnikPlayer.setCustomAbsorptionHP(player, f2);
        this.send(ChatColor.GREEN + "You now have " + ChatColor.GOLD + f2 + ChatColor.GREEN + " absorption hearts.");
    }
}

