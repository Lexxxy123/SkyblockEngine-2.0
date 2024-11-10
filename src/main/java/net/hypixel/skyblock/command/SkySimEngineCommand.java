/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 */
package net.hypixel.skyblock.command;

import net.hypixel.skyblock.command.CommandParameters;
import net.hypixel.skyblock.command.CommandSource;
import net.hypixel.skyblock.command.SCommand;
import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.ChatColor;

@CommandParameters(description="The main command for SkyBlockEngine.", aliases="ssei", permission=PlayerRank.ADMIN)
public class SkySimEngineCommand
extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        this.send(ChatColor.BLUE + "Fun" + ChatColor.GREEN + "Pixel" + ChatColor.GOLD + " Skyblock" + plugin.getDescription().getVersion());
        this.send(ChatColor.GREEN + "Created by" + ChatColor.GOLD + " Lexxxy " + ChatColor.GREEN + "for Funpixel Skyblock Usage and the \ncompatibility with highly modified version of Spigot!");
        this.send(Sputnik.trans("&aSome of the data structures and logic have been taken from &6super&a's (superischroma) project, thank you super, very cool!"));
    }
}

