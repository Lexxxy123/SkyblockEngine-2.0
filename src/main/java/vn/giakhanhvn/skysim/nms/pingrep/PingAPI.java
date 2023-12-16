package vn.giakhanhvn.skysim.nms.pingrep;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import vn.giakhanhvn.skysim.SkySimEngine;
import vn.giakhanhvn.skysim.util.SLog;

import java.util.ArrayList;
import java.util.List;

public class PingAPI {
    private static List<PingListener> listeners;

    public static void register() {
        try {
            PingAPI.listeners = new ArrayList<PingListener>();
            final Class<?> injector = PingInjector.class;
            Bukkit.getPluginManager().registerEvents((Listener) injector.newInstance(), SkySimEngine.getPlugin());
            SLog.info("Successfully hooked into " + Bukkit.getServer().getName());
        } catch (final SecurityException | InstantiationException | IllegalAccessException |
                       IllegalArgumentException e) {
            SLog.severe("Non compatible server version!");
            Bukkit.getPluginManager().disablePlugin(SkySimEngine.getPlugin());
        }
    }

    public static void registerListener(final PingListener listener) {
        PingAPI.listeners.add(listener);
    }

    public static List<PingListener> getListeners() {
        return PingAPI.listeners;
    }
}
