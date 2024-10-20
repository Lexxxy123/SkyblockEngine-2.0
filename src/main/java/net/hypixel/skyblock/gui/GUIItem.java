/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 */
package net.hypixel.skyblock.gui;

import net.hypixel.skyblock.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface GUIItem {
    public int getSlot();

    default public ItemStack getItem() {
        return new ItemStack(Material.AIR);
    }

    default public boolean canPickup() {
        return false;
    }

    default public GUIItem createLoadingItem(final Material type, final String name, final int slot) {
        return new GUIItem(){

            @Override
            public int getSlot() {
                return slot;
            }

            @Override
            public ItemStack getItem() {
                return SUtil.getSingleLoreStack(name, type, (short)0, 1, ChatColor.DARK_GRAY + "Loading...");
            }
        };
    }
}

