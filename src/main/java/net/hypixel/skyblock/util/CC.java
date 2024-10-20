/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 */
package net.hypixel.skyblock.util;

import org.bukkit.ChatColor;

public class CC {
    public static String translate(String message) {
        return ChatColor.translateAlternateColorCodes((char)'&', (String)message);
    }

    public static String getTimeDifferenceAndColor(long start, long end) {
        return CC.getColorBasedOnSize(end - start, 20, 5000, 10000) + "" + (end - start) + "ms";
    }

    public static ChatColor getColorBasedOnSize(long num, int low, int med, int high) {
        if (num <= (long)low) {
            return ChatColor.GREEN;
        }
        if (num <= (long)med) {
            return ChatColor.YELLOW;
        }
        if (num <= (long)high) {
            return ChatColor.RED;
        }
        return ChatColor.DARK_RED;
    }
}

