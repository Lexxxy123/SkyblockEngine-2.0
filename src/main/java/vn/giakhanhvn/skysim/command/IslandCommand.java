package vn.giakhanhvn.skysim.command;

import org.bukkit.entity.Player;
import vn.giakhanhvn.skysim.user.PlayerUtils;
import org.bukkit.command.ConsoleCommandSender;

@CommandParameters(description = "go to or create your island", aliases = "is")
public class IslandCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        final Player player = sender.getPlayer();
        PlayerUtils.sendToIsland(player);
    }
}
