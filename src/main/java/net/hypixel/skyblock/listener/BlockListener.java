/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.PlayerInteractEvent
 */
package net.hypixel.skyblock.listener;

import net.hypixel.skyblock.command.RegionCommand;
import net.hypixel.skyblock.command.SetLaunchPad;
import net.hypixel.skyblock.features.launchpads.LaunchPadHandler;
import net.hypixel.skyblock.features.launchpads.PadGenerator;
import net.hypixel.skyblock.features.region.Region;
import net.hypixel.skyblock.features.region.RegionGenerator;
import net.hypixel.skyblock.listener.PListener;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockListener
extends PListener {
    @EventHandler
    public void onBlockInteract(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        if (block == null) {
            return;
        }
        Player player = e.getPlayer();
        if (!RegionCommand.REGION_GENERATION_MAP.containsKey(player)) {
            return;
        }
        e.setCancelled(true);
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
    public void PadCreation(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        if (block == null) {
            return;
        }
        Player player = e.getPlayer();
        if (!SetLaunchPad.PAD_GENERATION_MAP.containsKey(player)) {
            return;
        }
        e.setCancelled(true);
        PadGenerator generator = SetLaunchPad.PAD_GENERATION_MAP.get(player);
        switch (generator.getPhase()) {
            case 1: {
                generator.setStartLocation(block.getLocation());
                generator.setPhase(2);
                player.sendMessage(ChatColor.GRAY + "added your clicked block as the start location!");
                player.sendMessage(ChatColor.DARK_AQUA + "Click on the second location block!");
                break;
            }
            case 2: {
                generator.setEndLocation(block.getLocation());
                generator.setPhase(3);
                player.sendMessage(ChatColor.GRAY + "added your clicked block as the end location!");
                player.sendMessage(ChatColor.DARK_AQUA + "Click on the 3rd block for teleport location!");
                break;
            }
            case 3: {
                generator.setTeleportLocation(block.getLocation().add(0.0, 1.0, 0.0));
                LaunchPadHandler handler = new LaunchPadHandler();
                handler.savePad(generator.getStart(), generator.getEnd(), generator.getStartLocation(), generator.getEndLocation(), generator.getTeleportLocation());
                player.sendMessage(ChatColor.GREEN + "Created LaunchPad!");
                SetLaunchPad.PAD_GENERATION_MAP.remove(player);
                break;
            }
        }
    }
}

