/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package net.hypixel.skyblock.util;

import net.hypixel.skyblock.api.nbt.NBTExplorer;
import net.hypixel.skyblock.util.SLog;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class SkySimAPI {
    public static void requestPlayerAPI(OfflinePlayer player) {
        Player p2 = player.getPlayer();
        PlayerInventory inv = p2.getInventory();
        StringBuilder sb = new StringBuilder();
        for (int i2 = 39; i2 >= 0; --i2) {
            ItemStack stack = inv.getItem(i2);
            if (stack == null) continue;
            sb.append(NBTExplorer.NBTSaver(stack));
        }
        SLog.info(sb.toString());
    }
}

