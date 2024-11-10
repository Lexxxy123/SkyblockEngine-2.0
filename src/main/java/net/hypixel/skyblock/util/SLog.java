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
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class SLog {
    private static final Logger LOGGER = Logger.getLogger("Minecraft");
    private static final String PREFIX = ChatColor.translateAlternateColorCodes((char)'&', (String)"&7[&aSkyblock&dCore&7] &f");

    private static void log(Object message, Level level) {
        LOGGER.log(level, PREFIX + message);
    }

    public static String getPrefix() {
        return PREFIX;
    }

    public static void sendMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.translateAlternateColorCodes((char)'&', (String)message));
    }

    public static void info(Object message) {
        int emoji = 128483;
        SLog.sendMessage(ChatColor.GREEN + message.toString());
    }

    public static void warn(Object message) {
        SLog.log(ChatColor.YELLOW + "U+1F40A " + message.toString(), Level.WARNING);
    }

    public static void severe(Object message) {
        SLog.log(ChatColor.RED + "U+1F99C " + message.toString(), Level.SEVERE);
    }
}

