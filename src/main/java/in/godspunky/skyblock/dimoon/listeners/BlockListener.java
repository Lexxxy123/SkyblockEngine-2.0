package in.godspunky.skyblock.dimoon.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

public class BlockListener implements Listener {
    @EventHandler
    public void onBlockPhysics(final BlockPhysicsEvent event) {
        if (event.getBlock().getWorld().getName().equalsIgnoreCase("arena")) {
            event.setCancelled(true);
        }
    }
}
