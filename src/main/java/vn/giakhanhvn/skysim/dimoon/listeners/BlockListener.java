package vn.giakhanhvn.skysim.dimoon.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.Listener;

public class BlockListener implements Listener
{
    @EventHandler
    public void onBlockPhysics(final BlockPhysicsEvent event) {
        if (event.getBlock().getWorld().getName().equalsIgnoreCase("arena")) {
            event.setCancelled(true);
        }
    }
}
