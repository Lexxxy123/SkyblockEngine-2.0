package in.godspunky.skyblock.command;

import in.godspunky.skyblock.island.SkyblockIsland;
import in.godspunky.skyblock.ranks.PlayerRank;
import in.godspunky.skyblock.user.User;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "go to or create your island", aliases = "is", permission = PlayerRank.DEFAULT)
public class IslandCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        final Player player = sender.getPlayer();
        User user = User.getUser(player.getUniqueId());
        SkyblockIsland.getIsland(player.getUniqueId()).send(user.selectedProfile.getProfileId());
    }
}
