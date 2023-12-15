package vn.giakhanhvn.skysim.listener;

import org.bukkit.plugin.Plugin;
import vn.giakhanhvn.skysim.SkySimEngine;
import org.bukkit.event.Listener;

public class PListener implements Listener {
    private static int amount;
    protected SkySimEngine plugin;

    protected PListener() {
        this.plugin = SkySimEngine.getPlugin();
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
        ++PListener.amount;
    }

    public static int getAmount() {
        return PListener.amount;
    }

    static {
        PListener.amount = 0;
    }
}
