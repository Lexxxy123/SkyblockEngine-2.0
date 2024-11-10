/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.command;

import java.util.HashMap;
import java.util.Map;
import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.features.launchpads.PadGenerator;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@CommandParameters(aliases="setpad", permission=PlayerRank.ADMIN)
public class SetLaunchPad
extends SCommand {
    public static Map<Player, PadGenerator> PAD_GENERATION_MAP = new HashMap<Player, PadGenerator>();

    @Override
    public void run(CommandSource sender, String[] args) {
        Player player = sender.getPlayer();
        PAD_GENERATION_MAP.put(player, new PadGenerator(args[0], args[1]));
        this.send(ChatColor.DARK_AQUA + "Click at the first location of pad");
    }
}

