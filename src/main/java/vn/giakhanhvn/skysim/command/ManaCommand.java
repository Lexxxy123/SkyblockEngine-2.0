package vn.giakhanhvn.skysim.command;

import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import vn.giakhanhvn.skysim.Repeater;
import org.bukkit.command.ConsoleCommandSender;

@CommandParameters(description = "Modify your mana amount.", permission = "spt.item")
public class ManaCommand extends SCommand
{
    @Override
    public void run(final CommandSource sender, final String[] args) {
        if (args.length != 1) {
            throw new CommandArgumentException();
        }
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        final Player player = sender.getPlayer();
        final UUID uuid = player.getUniqueId();
        if (!Repeater.MANA_MAP.containsKey(uuid)) {
            throw new CommandFailException("Something went wrong!");
        }
        final int set = Integer.parseInt(args[0]);
        Repeater.MANA_MAP.remove(uuid);
        Repeater.MANA_MAP.put(uuid, set);
        this.send(ChatColor.GREEN + "Your mana is now " + ChatColor.AQUA + set + ".");
    }
}
