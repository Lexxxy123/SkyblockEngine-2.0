package net.hypixel.skyblock.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SLog {

    private static final Logger LOGGER = Logger.getLogger("Minecraft");
    private static final String PREFIX = ChatColor.translateAlternateColorCodes('&', "&7[&aSkyblock&dCore&7] &f");

    private static void log(final Object message, final Level level) {
        LOGGER.log(level, PREFIX + message);
    }

    public static String getPrefix() {
        return PREFIX;
    }

    public static void sendMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void info(final Object message) {
        sendMessage(ChatColor.GREEN + "ℹ️ " + message.toString());
    }

    public static void warn(final Object message) {
        log(ChatColor.YELLOW + "⚠️ " + message.toString(), Level.WARNING);
    }

    public static void severe(final Object message) {
        log(ChatColor.RED + "❗ " + message.toString(), Level.SEVERE);
    }
}