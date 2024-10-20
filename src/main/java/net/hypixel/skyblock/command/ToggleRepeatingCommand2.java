/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.command;

import net.hypixel.skyblock.Repeater;
import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@CommandParameters(description="Spec test command.", aliases="db:ssp", permission=PlayerRank.ADMIN)
public class ToggleRepeatingCommand2
extends SCommand {
    public Repeater repeater;

    @Override
    public void run(CommandSource sender, String[] args) {
        Player player = sender.getPlayer();
        if (player == null) {
            this.send(ChatColor.RED + "Something occurred while taking services from the API!");
        }
    }
}

