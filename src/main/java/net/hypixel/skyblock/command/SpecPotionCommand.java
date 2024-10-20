/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package net.hypixel.skyblock.command;

import net.hypixel.skyblock.command.CommandArgumentException;
import net.hypixel.skyblock.command.CommandFailException;
import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.features.potion.PotionEffect;
import net.hypixel.skyblock.features.potion.PotionEffectType;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.item.SItem;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandParameters(description="Adds an potion from Spec to the specified item.", aliases="spot", permission=PlayerRank.ADMIN)
public class SpecPotionCommand
extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        if (args.length != 3) {
            throw new CommandArgumentException();
        }
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        Player player = sender.getPlayer();
        ItemStack stack = player.getInventory().getItemInHand();
        if (stack == null) {
            throw new CommandFailException(ChatColor.RED + "You don't have anything in your hand!");
        }
        SItem sItem = SItem.find(stack);
        if (sItem == null) {
            throw new CommandFailException(ChatColor.RED + "That item is not executable!");
        }
        PotionEffectType type = PotionEffectType.getByNamespace(args[0]);
        if (type == null) {
            throw new CommandFailException(ChatColor.RED + "Invalid potion type, try again, now actually put a vaild potion type, nerd!");
        }
        long duration = Long.parseLong(args[1]);
        int level = Integer.parseInt(args[2]);
        if (level <= 0) {
            this.send(ChatColor.RED + "What's the point?");
            return;
        }
        if (level > 2000) {
            this.send(ChatColor.RED + "Cap reached! Limit is LVL 2000!");
            return;
        }
        if (type == PotionEffectType.FEROCITY && level > 15) {
            this.send(ChatColor.RED + "Cap reached! Limit is LVL 15!");
            return;
        }
        sItem.addPotionEffect(new PotionEffect(type, level, duration));
        this.send(ChatColor.GREEN + "Your " + sItem.getType().getDisplayName(sItem.getVariant()) + " now has " + type.getName() + " " + level + ChatColor.GREEN + " on it.");
    }
}

