package in.godspunky.skyblock.dimoon.commands;

import in.godspunky.skyblock.Skyblock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DimoonAbility implements CommandExecutor {
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        final Skyblock plugin = Skyblock.getPlugin();
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (plugin.dimoon != null) {
                plugin.dimoon.getAbilities().get(Integer.parseInt(args[0])).activate(player, plugin.dimoon);
            }
        }
        return true;
    }
}
