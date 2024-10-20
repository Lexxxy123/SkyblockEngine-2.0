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
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandParameters(description="Adds an enchantment from Spec to the specified item.", aliases="enc", permission=PlayerRank.DEFAULT)
public class MembersEnchantCommand
extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        if (args.length != 2) {
            throw new CommandArgumentException();
        }
        if (args.length == 0) {
            this.send(ChatColor.RED + "Wrong usage of the command! Usage /enc <enchant type> <level>!");
            return;
        }
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        Player player = sender.getPlayer();
        if (Sputnik.isFullInv(player)) {
            this.send(ChatColor.RED + "Your inventory ran out of spaces! Clean it up!");
            return;
        }
        EnchantmentType type = EnchantmentType.getByNamespace(args[0]);
        if (type == null) {
            this.send(ChatColor.RED + "Invalid enchantment type! It might not get added yet.");
            return;
        }
        int i2 = Integer.parseInt(args[1]);
        if (i2 <= 0) {
            this.send(ChatColor.RED + "Are you serious? If you want to remove enchantments, use /rench");
            return;
        }
        if (type == EnchantmentType.LUCKINESS) {
            this.send(ChatColor.RED + "This enchantment is not free! Purchase it in the Community Shop.");
            return;
        }
        if (type == EnchantmentType.VICIOUS) {
            this.send(ChatColor.RED + "This enchantment is not free! You need to slay Revenant Horror V for it!");
            return;
        }
        if (type == EnchantmentType.CHIMERA) {
            this.send(ChatColor.RED + "This enchantment is not free! You need to slay Sven Packmaster IV for it!");
            return;
        }
        if (type == EnchantmentType.LEGION || type == EnchantmentType.FATAL_TEMPO) {
            this.send(ChatColor.RED + "This enchantment is not free! You need to slay Voidgloom Seraph IV for it!");
            return;
        }
        if (i2 > 5 && type == EnchantmentType.FIRST_STRIKE) {
            this.send(ChatColor.RED + "Too high enchantment level! This enchantment is capped at level 5 in case of /meb command! You need to slay Tarantula Broodfather IV to get a LVL VII version of this enchanment!");
            return;
        }
        if (type == EnchantmentType.EFFICIENCY) {
            this.send(ChatColor.RED + "This enchantment is banned due to rewamping in mining system!");
            return;
        }
        if (i2 > 2 && type == EnchantmentType.KNOCKBACK) {
            this.send(ChatColor.RED + "Too high enchantment level! This enchantment is capped at level 2!");
            return;
        }
        if (i2 > 1500) {
            this.send(ChatColor.RED + "Too high enchantment level! This enchantment is capped at 1,500.");
            return;
        }
        if (i2 > 320 && type == EnchantmentType.POWER) {
            this.send(ChatColor.RED + "Too high enchantment level! This enchantment is capped at level 320.");
            return;
        }
        if (i2 > 400 && type == EnchantmentType.SHARPNESS) {
            this.send(ChatColor.RED + "Too high enchantment level! This enchantment is capped at level 400.");
            return;
        }
        if (i2 > 1 && (type == EnchantmentType.ONE_FOR_ALL || type == EnchantmentType.TELEKINESIS || type == EnchantmentType.AQUA_INFINITY)) {
            this.send(ChatColor.RED + "Too high enchantment level! This enchantment is capped at level 1.");
            return;
        }
        if (i2 > 30 && (type == EnchantmentType.CRITICAL || type == EnchantmentType.EXECUTE || type == EnchantmentType.AIMING)) {
            this.send(ChatColor.RED + "Too high enchantment level! This enchantment is capped at level 30.");
            return;
        }
        if (i2 > 10 && (type == EnchantmentType.ENDER_SLAYER || type == EnchantmentType.SMITE || type == EnchantmentType.BANE_OF_ARTHROPODS || type == EnchantmentType.DRAGON_HUNTER)) {
            this.send(ChatColor.RED + "Too high enchantment level! This enchantment is capped at level 10.");
            return;
        }
        if (i2 > 10 && type == EnchantmentType.ULTIMATE_WISE) {
            this.send(ChatColor.RED + "Too high enchantment level! This enchantment is capped at level 8.");
            return;
        }
        if (i2 > 10 && (type == EnchantmentType.SOUL_EATER || type == EnchantmentType.LIFE_STEAL || type == EnchantmentType.AIMING || type == EnchantmentType.VAMPIRISM)) {
            this.send(ChatColor.RED + "Too high enchantment level! This enchantment is capped at level 5.");
            return;
        }
        SItem eBook = SItem.of(SMaterial.ENCHANTED_BOOK);
        eBook.addEnchantment(type, i2);
        player.getInventory().addItem(new ItemStack[]{eBook.getStack()});
        this.send(Sputnik.trans("&aYou have been given the &dEnchanted Book &awith &9" + type.getName() + " &9" + SUtil.toRomanNumeral(i2) + " &aon it. Use an Anvil to apply it to your items."));
    }

    public void stop() {
        this.send("");
    }
}

