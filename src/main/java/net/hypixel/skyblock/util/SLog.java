/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 */
package net.hypixel.skyblock.util;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.hypixel.skyblock.util.CC;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class SLog {
    private static final Logger LOGGER;
    private static final String PREFIX;

    private static void log(Object o2, Level l2) {
        LOGGER.log(l2, SLog.getPrefix() + o2);
    }

    public static String getPrefix() {
        return ChatColor.translateAlternateColorCodes((char)'&', (String)"&7[&aHypixel&bSkyblock&dCore&7] &f");
    }

    public static void sendMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(SLog.getPrefix() + CC.translate(message));
    }

    public static void info(Object o2) {
        SLog.sendMessage((String)o2);
    }

    public static void warn(Object o2) {
        SLog.log(o2, Level.WARNING);
    }

    public static void severe(Object o2) {
        SLog.log(o2, Level.SEVERE);
    }

    static {
        PREFIX = Sputnik.trans("&7[&aHypixel&bSkyblock&dCore&7] &f");
        LOGGER = Logger.getLogger("Minecraft");
    }
}

