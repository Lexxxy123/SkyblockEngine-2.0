/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package net.hypixel.skyblock.command;

import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

@CommandParameters(description="", aliases="bbc", permission=PlayerRank.ADMIN)
public class BuyCookieCommand
extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        Player player = sender.getPlayer();
        if (player.isOp()) {
            int amount = Integer.parseInt(args[0]);
            PlayerInventory inv = player.getInventory();
            for (int i2 = 0; i2 < amount; ++i2) {
                ItemStack stack = SItem.of(SMaterial.HIDDEN_BOOSTER_COOKIE).getStack();
                Sputnik.smartGiveItem(stack, player);
            }
        } else {
            this.send(ChatColor.RED + "Unknown Command.");
        }
    }
}

