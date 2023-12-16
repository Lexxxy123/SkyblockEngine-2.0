package vn.giakhanhvn.skysim.dimoon.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import vn.giakhanhvn.skysim.SkySimEngine;

public class DimoonAbility implements CommandExecutor {
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        final SkySimEngine plugin = SkySimEngine.getPlugin();
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (plugin.dimoon != null) {
                plugin.dimoon.getAbilities().get(Integer.parseInt(args[0])).activate(player, plugin.dimoon);
            }
        }
        return true;
    }
}
