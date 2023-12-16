package vn.giakhanhvn.skysim.command;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import vn.giakhanhvn.skysim.sequence.SoundSequenceType;

@CommandParameters(description = "Play a sound sequence.", usage = "/<command> <sequence>")
public class SoundSequenceCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        if (args.length != 1) {
            throw new CommandArgumentException();
        }
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        final Player player = sender.getPlayer();
        final SoundSequenceType type = SoundSequenceType.getByNamespace(args[0]);
        if (type == null) {
            throw new CommandFailException("That is not a sound sequence!");
        }
        player.sendMessage(ChatColor.AQUA + "Playing " + type.getNamespace() + "...");
        type.play(player);
    }
}
