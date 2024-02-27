package in.godspunky.skyblock.entity.dimoon.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import in.godspunky.skyblock.SkyBlock;

public class DimoonAbility implements CommandExecutor {
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        final SkyBlock plugin = SkyBlock.getPlugin();
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (plugin.dimoon != null) {
                plugin.dimoon.getAbilities().get(Integer.parseInt(args[0])).activate(player, plugin.dimoon);
            }
        }
        return true;
    }
}
