/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Monster
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.command;

import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

@CommandParameters(description="Gets the NBT of your current item.", aliases="kamh", permission=PlayerRank.ADMIN)
public class KillAllHostileMobs
extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        Player player = sender.getPlayer();
        if (null == player) {
            this.send(ChatColor.RED + "You can't use this command here!");
        }
        if (player.isOp()) {
            World world = player.getWorld();
            for (Entity entity : world.getEntities()) {
                if (!(entity instanceof Monster) || entity.hasMetadata("pets") || entity.hasMetadata("Ire")) continue;
                entity.remove();
            }
            this.send(ChatColor.WHITE + "You removed all" + ChatColor.RED + " HOSTILE" + ChatColor.RESET + " mobs in this world.");
        } else {
            this.send(Sputnik.trans("&cYou cant use this, lol."));
        }
    }
}

