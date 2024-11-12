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
import net.hypixel.skyblock.features.enchantment.EnchantmentType;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.item.SItem;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandParameters(description="Adds an enchantment from Spec to the specified item.", aliases="sench", permission=PlayerRank.ADMIN)
public class SpecEnchantmentCommand
extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        if (args.length != 2) {
            throw new CommandArgumentException();
        }
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        if (!sender.getPlayer().isOp()) {
            return;
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
        EnchantmentType type = EnchantmentType.getByNamespace(args[0]);
        if (type == null) {
            throw new CommandFailException(ChatColor.RED + "Invalid enchantment type!");
        }
        int i2 = Integer.parseInt(args[1]);
        if (i2 <= 0) {
            this.send(ChatColor.RED + "Are you serious? If you want to remove enchantments, use /re");
            return;
        }
        if (i2 > 10000000) {
            this.send(ChatColor.RED + "Too high enchantment level.");
            return;
        }
        if (i2 > 50 && type == EnchantmentType.CHIMERA) {
            this.send(ChatColor.RED + "Too high enchantment level.");
            return;
        }
        if (i2 > 100 && type == EnchantmentType.VICIOUS) {
            this.send(ChatColor.RED + "Too high enchantment level.");
            return;
        }
        if (i2 > 100 && type == EnchantmentType.LUCKINESS) {
            this.send(ChatColor.RED + "Too high enchantment level.");
            return;
        }
        if (i2 > 10 && type == EnchantmentType.LEGION) {
            this.send(ChatColor.RED + "Too high enchantment level.");
            return;
        }
        sItem.addEnchantment(type, i2);
        this.send(ChatColor.GREEN + "Your " + sItem.getType().getDisplayName(sItem.getVariant()) + " now has " + type.getName() + " " + i2 + " on it.");
    }
}

