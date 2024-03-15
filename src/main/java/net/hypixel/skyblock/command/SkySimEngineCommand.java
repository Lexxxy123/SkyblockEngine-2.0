package net.hypixel.skyblock.command;

import net.hypixel.skyblock.features.ranks.PlayerRank;
import org.bukkit.ChatColor;
import net.hypixel.skyblock.util.Sputnik;

@CommandParameters(description = "The main command for SkyBlockEngine.", aliases = "ssei", permission = PlayerRank.ADMIN)
public class SkySimEngineCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        this.send(ChatColor.BLUE + "God" + ChatColor.GREEN + "Spunky" + ChatColor.GOLD + " Skyblock" + plugin.getDescription().getVersion());
        this.send(ChatColor.GREEN + "Created by" + ChatColor.GOLD + " Epicportal and Hamza " + ChatColor.GREEN + "for Godspunky Skyblock Usage and the \ncompatibility with highly modified version of Spigot!");
        this.send(Sputnik.trans("&aSome of the data structures and logic have been taken from &6super&a's (superischroma) project, thank you super, very cool!"));
    }
}
