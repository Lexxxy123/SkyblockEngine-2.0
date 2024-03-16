package net.hypixel.skyblock.listener;

import lombok.Getter;
import net.hypixel.skyblock.SkyBlock;
import org.bukkit.event.Listener;

public class PListener implements Listener {
    @Getter
    private static int amount;
    protected SkyBlock plugin;

    protected PListener() {
        this.plugin = SkyBlock.getPlugin();
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
        ++amount;
    }

    static {
        amount = 0;
    }
}
