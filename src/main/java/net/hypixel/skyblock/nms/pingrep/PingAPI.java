/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 */
package net.hypixel.skyblock.nms.pingrep;

import java.util.ArrayList;
import java.util.List;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.nms.pingrep.PingInjector;
import net.hypixel.skyblock.nms.pingrep.PingListener;
import net.hypixel.skyblock.util.SLog;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class PingAPI {
    private static List<PingListener> listeners;

    public static void register() {
        try {
            listeners = new ArrayList<PingListener>();
            Class<PingInjector> injector = PingInjector.class;
            Bukkit.getPluginManager().registerEvents((Listener)injector.newInstance(), (Plugin)SkyBlock.getPlugin());
            SLog.info("Successfully hooked into " + Bukkit.getServer().getName());
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | SecurityException e2) {
            SLog.severe("Non compatible server version!");
            Bukkit.getPluginManager().disablePlugin((Plugin)SkyBlock.getPlugin());
        }
    }

    public static void registerListener(PingListener listener) {
        listeners.add(listener);
    }

    public static List<PingListener> getListeners() {
        return listeners;
    }
}

