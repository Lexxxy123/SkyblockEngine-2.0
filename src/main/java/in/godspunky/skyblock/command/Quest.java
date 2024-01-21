package in.godspunky.skyblock.command;

import in.godspunky.skyblock.island.SkyblockIsland;
import in.godspunky.skyblock.ranks.PlayerRank;
import in.godspunky.skyblock.user.User;
import in.godspunky.skyblock.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "go to or create your island", aliases = "quest", permission = PlayerRank.DEFAULT)
public class Quest extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        final Player player = sender.getPlayer();
        User user = User.getUser(player.getUniqueId());
        player.sendMessage(user.getCompletedObjectives().toString());

        final String lowerCase = args[0].toLowerCase();
        switch (lowerCase) {
            case "q":
                player.sendMessage(user.getCompletedQuests().toString());
                return;
            case "o":
                player.sendMessage(user.getCompletedObjectives().toString());
            default:
                throw new CommandArgumentException();
        }
    }
}
