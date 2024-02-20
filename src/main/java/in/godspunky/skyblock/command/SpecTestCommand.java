package in.godspunky.skyblock.command;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import in.godspunky.skyblock.user.User;

@CommandParameters(description = "Spec test command.", aliases = "setf6c")
public class SpecTestCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        if (args.length != 1) {
            throw new CommandArgumentException();
        }
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        final Player player = sender.getPlayer();
        if (player.isOp()) {
            final int set = Integer.parseInt(args[0]);
            final User user = User.getUser(player.getUniqueId());
            User.getUser(player.getUniqueId()).setBCollection(set);
        }
    }
}
