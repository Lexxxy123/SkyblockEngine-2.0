package vn.giakhanhvn.skysim.nms.pingrep;

import vn.giakhanhvn.skysim.util.SLog;
import org.bukkit.plugin.Plugin;
import vn.giakhanhvn.skysim.SkySimEngine;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;
import java.util.ArrayList;
import java.util.List;

public class PingAPI
{
    private static List<PingListener> listeners;
    
    public static void register() {
        try {
            PingAPI.listeners = new ArrayList<PingListener>();
            final Class<?> injector = PingInjector.class;
            Bukkit.getPluginManager().registerEvents((Listener)injector.newInstance(), (Plugin)SkySimEngine.getPlugin());
            SLog.info("Successfully hooked into " + Bukkit.getServer().getName());
        }
        catch (final SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException e) {
            SLog.severe("Non compatible server version!");
            Bukkit.getPluginManager().disablePlugin((Plugin)SkySimEngine.getPlugin());
        }
    }
    
    public static void registerListener(final PingListener listener) {
        PingAPI.listeners.add(listener);
    }
    
    public static List<PingListener> getListeners() {
        return PingAPI.listeners;
    }
}
