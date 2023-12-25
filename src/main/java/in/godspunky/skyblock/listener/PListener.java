package in.godspunky.skyblock.listener;

import org.bukkit.event.Listener;
import in.godspunky.skyblock.Skyblock;

public class PListener implements Listener {
    private static int amount;
    protected Skyblock plugin;

    protected PListener() {
        this.plugin = Skyblock.getPlugin();
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
