package vn.giakhanhvn.skysim.dimoon.commands;

import vn.giakhanhvn.skysim.dimoon.Dimoon;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class SummonDimoon implements CommandExecutor {
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        Dimoon.spawnDimoon();
        return true;
    }
}
