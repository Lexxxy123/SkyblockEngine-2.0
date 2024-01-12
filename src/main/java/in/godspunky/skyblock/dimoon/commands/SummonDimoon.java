package in.godspunky.skyblock.dimoon.commands;

import in.godspunky.skyblock.dimoon.Dimoon;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SummonDimoon implements CommandExecutor {
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        Dimoon.spawnDimoon();
        return true;
    }
}
