/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.command.CommandSender
 */
package net.hypixel.skyblock.command;

import java.util.HashMap;
import java.util.Map;
import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.features.region.RegionGenerator;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

@CommandParameters(description="Manage world regions.", usage="", aliases="hub", permission=PlayerRank.DEFAULT)
public class HubCommand
extends SCommand {
    public static Map<CommandSender, RegionGenerator> REGION_GENERATION_MAP = new HashMap<CommandSender, RegionGenerator>();

    @Override
    public void run(CommandSource sender, String[] args) {
        this.send(Sputnik.trans("&7Sending you to the Hub..."));
        Location l = new Location(Bukkit.getWorld((String)"world"), -2.5, 70.0, -68.5, 180.0f, 0.0f);
        if (null != sender.getPlayer()) {
            sender.getPlayer().teleport(l);
        }
    }
}

