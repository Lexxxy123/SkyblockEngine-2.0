/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.ItemStack
 *  net.minecraft.server.v1_8_R3.NBTTagCompound
 *  org.bukkit.ChatColor
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package net.hypixel.skyblock.command;

import net.hypixel.skyblock.command.CommandArgumentException;
import net.hypixel.skyblock.command.CommandFailException;
import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

@CommandParameters(description="Gets the NBT of your current item.", permission=PlayerRank.ADMIN)
public class NBTCommand
extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        if (0 != args.length) {
            throw new CommandArgumentException();
        }
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        Player player = sender.getPlayer();
        PlayerInventory inv = player.getInventory();
        net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy((ItemStack)inv.getItemInHand());
        if (null == inv.getItemInHand()) {
            throw new CommandFailException(ChatColor.RED + "Air do not have NBT kiddo, get something!");
        }
        NBTTagCompound compound = stack.getTag();
        if (null == compound) {
            throw new CommandFailException(ChatColor.RED + "This item does not have any NBT data!");
        }
        this.send(ChatColor.GREEN + "NBT Explorer >");
        for (String key : compound.c()) {
            this.send(ChatColor.YELLOW + key + ChatColor.GREEN + ": " + ChatColor.RESET + compound.get(key).toString().replaceAll("\u00a7", "&"));
        }
    }
}

