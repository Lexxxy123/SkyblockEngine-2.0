/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package net.hypixel.skyblock.command;

import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandParameters(description="", aliases="smd", permission=PlayerRank.ADMIN)
public class StackMyDimoon
extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        boolean stg = false;
        Player player = sender.getPlayer();
        ItemStack[] iss = player.getInventory().getContents();
        for (int i2 = 0; i2 < player.getInventory().getContents().length; ++i2) {
            ItemStack itemStack = iss[i2];
        }
    }
}

