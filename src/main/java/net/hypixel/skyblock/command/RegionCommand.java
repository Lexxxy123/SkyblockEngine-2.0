/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 */
package net.hypixel.skyblock.command;

import java.util.HashMap;
import java.util.Map;
import net.hypixel.skyblock.command.CommandArgumentException;
import net.hypixel.skyblock.command.CommandFailException;
import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.features.region.Region;
import net.hypixel.skyblock.features.region.RegionGenerator;
import net.hypixel.skyblock.features.region.RegionType;
import net.hypixel.skyblock.module.DatabaseModule;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandParameters(description="Manage world regions.", usage="/<command> [create <name> <type> | update <name> [type] | delete <name>]", aliases="reg", permission=PlayerRank.ADMIN)
public class RegionCommand
extends SCommand {
    public static Map<CommandSender, RegionGenerator> REGION_GENERATION_MAP = new HashMap<CommandSender, RegionGenerator>();

    @Override
    public void run(CommandSource sender, String[] args) {
        Region region;
        String name;
        if (args.length == 3) {
            String lowerCase;
            name = args[1];
            RegionType type = RegionType.valueOf(args[2].toUpperCase());
            switch (lowerCase = args[0].toLowerCase()) {
                case "create": {
                    if (name.length() > 100) {
                        throw new CommandFailException("Name too long!");
                    }
                    if (DatabaseModule.getRegionData().exists(name)) {
                        throw new CommandFailException("There is already a region named that!");
                    }
                    REGION_GENERATION_MAP.put(sender.getSender(), new RegionGenerator("create", name, type));
                    this.send("Created a region named \"" + name + "\"");
                    this.send(ChatColor.DARK_AQUA + "Click the first corner of your region.");
                    return;
                }
            }
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("delete")) {
            name = args[1];
            region = Region.get(name);
            if (region == null) {
                throw new CommandFailException("There is no region named that!");
            }
            region.delete();
            this.send("Deleted region \"" + name + "\" successfully.");
        } else if (args.length == 2 || args.length == 3) {
            name = args[1];
            region = Region.get(name);
            if (region == null) {
                throw new CommandFailException("There is no region named that!");
            }
            RegionType type2 = region.getType();
            if (args.length == 3) {
                type2 = RegionType.valueOf(args[2].toUpperCase());
            }
            if (!args[0].equalsIgnoreCase("update")) {
                throw new CommandArgumentException();
            }
            REGION_GENERATION_MAP.put(sender.getSender(), new RegionGenerator("update", name, type2));
            this.send("Updating \"" + name + "\"");
            this.send(ChatColor.DARK_AQUA + "Click the first corner of your region.");
        } else {
            if (args.length != 0) {
                throw new CommandArgumentException();
            }
            StringBuilder result = new StringBuilder().append("Regions");
            for (Region region2 : Region.getRegions()) {
                result.append("\n").append(" - ").append(region2.getName()).append(" (").append(region2.getType().name().toLowerCase()).append(")");
            }
            this.send(result.toString());
        }
    }
}

