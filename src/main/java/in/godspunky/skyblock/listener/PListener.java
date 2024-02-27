package in.godspunky.skyblock.listener;

import in.godspunky.skyblock.SkyBlock;
import org.bukkit.event.Listener;

public class PListener implements Listener {
    private static int amount;
    protected SkyBlock plugin;

    protected PListener() {
        this.plugin = SkyBlock.getPlugin();
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
        ++amount;
    }

    public static int getAmount() {
        return amount;
    }

    static {
        amount = 0;
    }
}
