package in.godspunky.skyblock.command;

import in.godspunky.skyblock.ranks.PlayerRank;
import org.bukkit.ChatColor;

public class CommandPermissionException extends RuntimeException {

    public CommandPermissionException(PlayerRank permission) {
        super(ChatColor.RED + "You need " + permission + "  rank to use this command");
    }
}