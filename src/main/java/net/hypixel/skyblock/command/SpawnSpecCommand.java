/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.command;

import net.hypixel.skyblock.command.CommandArgumentException;
import net.hypixel.skyblock.command.CommandFailException;
import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SEntityType;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.util.Sputnik;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@CommandParameters(description="Spawn a mob from Spec.", aliases="scm", permission=PlayerRank.ADMIN)
public class SpawnSpecCommand
extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        if (args.length == 0) {
            throw new CommandArgumentException();
        }
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        Player player = sender.getPlayer();
        if (SEntityType.getEntityType(args[0]) == null) {
            player.sendMessage(Sputnik.trans("&cMob not found or it get banned!"));
            return;
        }
        SEntityType type = SEntityType.getEntityType(args[0]);
        if (type.toString().toLowerCase().contains("banned") || type.toString().toLowerCase().contains("test")) {
            player.sendMessage(Sputnik.trans("&cMob not found or it get banned!"));
            return;
        }
        SEntity entity = null;
        switch (type) {
            case REVENANT_HORROR: 
            case SVEN_PACKMASTER: 
            case TARANTULA_BROODFATHER: 
            case VOIDGLOOM_SERAPH: 
            case ATONED_HORROR: 
            case CRIMSON_SATHANAS: {
                if (args.length != 2) {
                    throw new CommandArgumentException();
                }
                int tier = Integer.parseInt(args[1]);
                entity = new SEntity((Entity)player, type, tier, player.getUniqueId());
                break;
            }
            default: {
                entity = new SEntity((Entity)player, type, new Object[0]);
            }
        }
        this.send(ChatColor.GREEN + "Success! You have spawned a(n) " + ChatColor.GOLD + entity.getStatistics().getEntityName());
    }
}

