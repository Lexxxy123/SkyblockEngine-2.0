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
import net.hypixel.skyblock.item.SItem;
import net.hypixel.skyblock.item.SMaterial;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandParameters(description="", aliases="smd", permission=PlayerRank.ADMIN)
public class StackMyDimoon
extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        int stg = 0;
        Player player = sender.getPlayer();
        ItemStack[] iss = player.getInventory().getContents();
        for (int i = 0; i < player.getInventory().getContents().length; ++i) {
            ItemStack is = iss[i];
            if (null == SItem.find(is) || SMaterial.HIDDEN_DIMOON_FRAG != SItem.find(is).getType()) continue;
            stg += is.getAmount();
            player.getInventory().setItem(i, null);
        }
        if (0 < stg) {
            ItemStack is2 = SItem.of(SMaterial.HIDDEN_DIMOON_FRAG).getStack();
            is2.setAmount(stg);
            Sputnik.smartGiveItem(is2, player);
            player.sendMessage(Sputnik.trans("&aStacked all your fragments which have been broken before! Have fun!"));
        }
    }
}

