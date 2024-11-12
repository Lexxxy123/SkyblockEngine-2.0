/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.command;

import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.features.enchantment.EnchantmentType;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@CommandParameters(description="", aliases="purc", permission=PlayerRank.ADMIN)
public class BuyBookCommand
extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        Player player = sender.getPlayer();
        if (player.isOp()) {
            EnchantmentType type = EnchantmentType.getByNamespace(args[0]);
            if (type == null) {
                this.send(ChatColor.RED + "Something wrong, contact admins!");
                return;
            }
            int i2 = Integer.parseInt(args[1]);
            SItem eBook = SItem.of(SMaterial.ENCHANTED_BOOK);
            eBook.addEnchantment(type, i2);
            Sputnik.smartGiveItem(eBook.getStack(), player);
        } else {
            this.send(ChatColor.RED + "Unknown Command.");
        }
    }
}

