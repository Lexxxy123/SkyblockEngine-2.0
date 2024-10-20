/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.PlayerInventory
 */
package net.hypixel.skyblock.command;

import net.hypixel.skyblock.command.CommandArgumentException;
import net.hypixel.skyblock.command.CommandFailException;
import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.item.SItem;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

@CommandParameters(description="Sets data for a Spec item.", permission=PlayerRank.ADMIN)
public class DataCommand
extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        String lowerCase;
        String key;
        if (3 > args.length) {
            throw new CommandArgumentException();
        }
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        Player player = sender.getPlayer();
        PlayerInventory inv = player.getInventory();
        if (null == inv.getItemInHand()) {
            throw new CommandFailException(ChatColor.RED + "Error! Hold an item in your hand!");
        }
        SItem sItem = SItem.find(inv.getItemInHand());
        if (!sItem.hasDataFor(key = args[0])) {
            throw new CommandFailException(ChatColor.RED + "Error! This item does not have data for '" + key + "'");
        }
        String joined = StringUtils.join((Object[])args, " ", 1, args.length - 1);
        switch (lowerCase = args[args.length - 1].toLowerCase()) {
            case "string": {
                sItem.setDataString(key, joined);
                break;
            }
            case "integer": 
            case "int": {
                sItem.setDataInt(key, Integer.parseInt(joined));
                break;
            }
            case "long": {
                sItem.setDataLong(key, Long.parseLong(joined));
                break;
            }
            case "boolean": 
            case "bool": {
                sItem.setDataBoolean(key, Boolean.parseBoolean(joined));
                break;
            }
            case "double": 
            case "d": {
                sItem.setDataDouble(key, Double.parseDouble(joined));
                break;
            }
            case "float": 
            case "f": {
                sItem.setDataFloat(key, Float.parseFloat(joined));
            }
        }
        sItem.update();
        this.send(ChatColor.GREEN + "'" + key + "' for this item has been set to '" + joined + "' as type '" + args[args.length - 1].toLowerCase() + "'");
    }
}

