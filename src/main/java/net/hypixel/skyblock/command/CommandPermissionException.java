package net.hypixel.skyblock.command;


import net.hypixel.skyblock.features.ranks.PlayerRank;
import org.bukkit.ChatColor;

public class CommandPermissionException extends RuntimeException {

    public CommandPermissionException(PlayerRank permission) {
        super(ChatColor.RED + "You need " + permission + "  rank to use this command");
    }
}