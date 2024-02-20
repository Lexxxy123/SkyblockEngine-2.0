package in.godspunky.skyblock.command;

import in.godspunky.skyblock.island.SkyblockIsland;
import in.godspunky.skyblock.user.User;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import in.godspunky.skyblock.user.PlayerUtils;

@CommandParameters(description = "go to or create your island", aliases = "is")
public class IslandCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        final Player player = sender.getPlayer();
        SkyblockIsland.getIsland(player.getUniqueId()).send();
    }
}
