/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.GameMode
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 */
package net.hypixel.skyblock.listener;

import net.hypixel.skyblock.command.RegionCommand;
import net.hypixel.skyblock.features.region.Region;
import net.hypixel.skyblock.features.region.RegionGenerator;
import net.hypixel.skyblock.listener.PListener;
import net.hypixel.skyblock.user.User;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockListener
extends PListener {
    @EventHandler
    public void onBlockInteract(PlayerInteractEvent e2) {
        Block block = e2.getClickedBlock();
        if (block == null) {
            return;
        }
        Player player = e2.getPlayer();
        if (!RegionCommand.REGION_GENERATION_MAP.containsKey(player)) {
            return;
        }
        e2.setCancelled(true);
        RegionGenerator generator = RegionCommand.REGION_GENERATION_MAP.get(player);
        switch (generator.getPhase()) {
            case 1: {
                generator.setFirstLocation(block.getLocation());
                generator.setPhase(2);
                player.sendMessage(ChatColor.GRAY + "Set your clicked block as the first location of the region!");
                player.sendMessage(ChatColor.DARK_AQUA + "Click the second corner of your region.");
                break;
            }
            case 2: {
                generator.setSecondLocation(block.getLocation());
                if (generator.getModificationType().equals("create")) {
                    Region.create(generator.getName(), generator.getFirstLocation(), generator.getSecondLocation(), generator.getType());
                } else {
                    Region region = Region.get(generator.getName());
                    region.setFirstLocation(generator.getFirstLocation());
                    region.setSecondLocation(generator.getSecondLocation());
                    region.setType(generator.getType());
                    region.save();
                }
                player.sendMessage(ChatColor.GRAY + "Region \"" + generator.getName() + "\" has been fully set up and " + generator.getModificationType() + "d!");
                RegionCommand.REGION_GENERATION_MAP.remove(player);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        User user = User.getUser(player);
        if (user.isOnUserIsland() && player.getGameMode() != GameMode.CREATIVE && player.getWorld().getName().equalsIgnoreCase("world")) {
            event.setCancelled(true);
        }
    }
}

