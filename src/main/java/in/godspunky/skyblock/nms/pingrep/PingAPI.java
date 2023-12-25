package in.godspunky.skyblock.nms.pingrep;

import in.godspunky.skyblock.Skyblock;
import in.godspunky.skyblock.util.SLog;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class PingAPI {
    private static List<PingListener> listeners;

    public static void register() {
        try {
            PingAPI.listeners = new ArrayList<PingListener>();
            final Class<?> injector = PingInjector.class;
            Bukkit.getPluginManager().registerEvents((Listener) injector.newInstance(), Skyblock.getPlugin());
            SLog.info("Successfully hooked into " + Bukkit.getServer().getName());
        } catch (final SecurityException | InstantiationException | IllegalAccessException |
                       IllegalArgumentException e) {
            SLog.severe("Non compatible server version!");
            Bukkit.getPluginManager().disablePlugin(Skyblock.getPlugin());
        }
    }

    public static void registerListener(final PingListener listener) {
        PingAPI.listeners.add(listener);
    }

    public static List<PingListener> getListeners() {
        return PingAPI.listeners;
    }
}
