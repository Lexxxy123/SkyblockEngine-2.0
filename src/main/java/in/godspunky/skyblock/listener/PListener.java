package in.godspunky.skyblock.listener;

import in.godspunky.skyblock.Skyblock;
import org.bukkit.event.Listener;

public class PListener implements Listener {
    private static int amount;

    static {
        PListener.amount = 0;
    }

    protected Skyblock plugin;

    protected PListener() {
        this.plugin = Skyblock.getPlugin();
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
        ++PListener.amount;
    }

    public static int getAmount() {
        return PListener.amount;
    }
}
