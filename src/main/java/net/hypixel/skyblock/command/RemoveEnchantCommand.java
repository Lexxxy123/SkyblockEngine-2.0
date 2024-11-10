/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.World
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.command;

import net.hypixel.skyblock.command.CommandFailException;
import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.features.enchantment.Enchantment;
import net.hypixel.skyblock.features.enchantment.EnchantmentType;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description="Adds an enchantment from Spec to the specified item.", aliases="rench", permission=PlayerRank.DEFAULT)
public class RemoveEnchantCommand
extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        String lowerCase;
        Player player = sender.getPlayer();
        World world = player.getWorld();
        SItem sitem = SItem.find(player.getItemInHand());
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        User user = sender.getUser();
        if (args.length == 0) {
            this.send(ChatColor.RED + "Invaild Syntax! The command is /rench <specific/all> <type> (Only needed for Specific mode)");
            return;
        }
        if (sitem == null) {
            this.send(ChatColor.RED + "You can't execute this command while not holding anything or an invalid item.");
            return;
        }
        if (sitem.getType().getStatistics().getType() != GenericItemType.ARMOR && sitem.getType().getStatistics().getType() != GenericItemType.WEAPON && sitem.getType().getStatistics().getType() != GenericItemType.RANGED_WEAPON && sitem.getType().getStatistics().getType() != GenericItemType.TOOL && sitem.getType() != SMaterial.ENCHANTED_BOOK) {
            this.send(ChatColor.RED + "You can't execute this command while holding this Item!");
            return;
        }
        switch (lowerCase = args[0].toLowerCase()) {
            case "specific": {
                if (args.length < 2) {
                    this.send(ChatColor.RED + "Missing enchantment type for specific removal mode!");
                    return;
                }
                EnchantmentType type = EnchantmentType.getByNamespace(args[1]);
                if (type == null) {
                    throw new CommandFailException(ChatColor.RED + "Invalid enchantment type!");
                }
                if (sitem.getEnchantment(type) == null) {
                    throw new CommandFailException(ChatColor.RED + "You can't remove an Enchantment which does not exist on your item!");
                }
                SItem book = SItem.of(SMaterial.ENCHANTED_BOOK);
                book.addEnchantment(type, sitem.getEnchantment(type).getLevel());
                sitem.removeEnchantment(type);
                player.setItemInHand(sitem.getStack());
                Sputnik.smartGiveItem(book.getStack(), player);
                this.send(Sputnik.trans("&e" + type.getName() + " &ahas been removed from your " + sitem.getFullName()) + " &eand you got your " + book.getFullName() + " &eback!");
                return;
            }
            case "all": {
                if (sitem.getEnchantments().size() <= 0) {
                    this.send(Sputnik.trans("&cThis item have no enchantments on it!"));
                    return;
                }
                if (sitem.getType() == SMaterial.ENCHANTED_BOOK) {
                    this.send(Sputnik.trans("&cThis action cannot be done with this item!"));
                    return;
                }
                SItem book2 = SItem.of(SMaterial.ENCHANTED_BOOK);
                for (Enchantment e : sitem.getEnchantments()) {
                    book2.addEnchantment(e.getType(), sitem.getEnchantment(e.getType()).getLevel());
                    sitem.removeEnchantment(e.getType());
                }
                player.setItemInHand(sitem.getStack());
                Sputnik.smartGiveItem(book2.getStack(), player);
                this.send(Sputnik.trans("&eAll enchantments has been removed from your " + sitem.getFullName()) + " &eand you got your " + book2.getFullName() + " &eback!");
                break;
            }
        }
    }
}

