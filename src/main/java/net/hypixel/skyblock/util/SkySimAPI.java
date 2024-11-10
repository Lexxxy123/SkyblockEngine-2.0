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
        Player p = player.getPlayer();
        PlayerInventory inv = p.getInventory();
        StringBuilder sb = new StringBuilder();
        for (int i = 39; i >= 0; --i) {
            ItemStack stack = inv.getItem(i);
            if (stack == null) continue;
            sb.append(NBTExplorer.NBTSaver(stack));
        }
        SLog.info(sb.toString());
    }
}

