package in.godspunky.skyblock.command;

import in.godspunky.skyblock.ranks.PlayerRank;
import in.godspunky.skyblock.user.PlayerUtils;
import net.swofty.swm.api.exceptions.WorldAlreadyExistsException;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

@CommandParameters(description = "go to or create your island", aliases = "is", permission = PlayerRank.DEFAULT)
public class IslandCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        final Player player = sender.getPlayer();
        try {
            PlayerUtils.sendToIsland(player);
        } catch (WorldAlreadyExistsException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
