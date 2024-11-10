/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.command;

import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.entity.StaticDragonManager;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

@CommandParameters(description="bruhbu", aliases="edf", permission=PlayerRank.ADMIN)
public class EndDragonFightCommand
extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        Player player = sender.getPlayer();
        World world = player.getWorld();
        if (world.getName().equalsIgnoreCase("world")) {
            if (StaticDragonManager.ACTIVE) {
                this.send(ChatColor.GREEN + "Processing...");
                SUtil.delay(() -> StaticDragonManager.endFight(), 10L);
                this.endDragonFight(world);
                SUtil.delay(() -> SUtil.broadcast(ChatColor.RED + "[SYSTEM] " + ChatColor.YELLOW + player.getName() + " have ended all Dragon fight in this world!", player), 10L);
            } else {
                this.send(ChatColor.RED + "There are no active dragon fight!");
            }
        } else {
            this.send(ChatColor.RED + "This command is not available on this world!");
        }
    }

    public void endDragonFight(World world) {
        for (Entity e : world.getEntities()) {
            if (e.getType() != EntityType.ENDER_DRAGON && e.getType() != EntityType.ENDER_CRYSTAL) continue;
            e.remove();
        }
    }
}

