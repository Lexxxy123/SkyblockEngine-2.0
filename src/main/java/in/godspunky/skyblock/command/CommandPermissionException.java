package in.godspunky.skyblock.command;

import org.bukkit.ChatColor;

public class CommandPermissionException extends RuntimeException {
    public CommandPermissionException(final String permission) {
        super(ChatColor.RED + "No permission. You need \"" + permission + "\"");
    }
}
