package in.godspunky.skyblock.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import in.godspunky.skyblock.user.User;

public class CommandSource {
    private final CommandSender sender;
    private final Player player;
    private final User user;

    public CommandSource(CommandSender sender) {
        this.sender = sender;
        this.player = ((sender instanceof Player) ? (Player) sender : null);
        this.user = ((null != this.player) ? User.getUser(this.player.getUniqueId()) : null);
    }

    public void send(String message) {
        this.sender.sendMessage(message);
    }

    public CommandSender getSender() {
        return this.sender;
    }

    public Player getPlayer() {
        return this.player;
    }

    public User getUser() {
        return this.user;
    }
}
