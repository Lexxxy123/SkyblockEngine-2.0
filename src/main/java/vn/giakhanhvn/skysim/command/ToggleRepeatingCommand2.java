package vn.giakhanhvn.skysim.command;

import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import vn.giakhanhvn.skysim.Repeater;

@CommandParameters(description = "Spec test command.", aliases = "db:ssp")
public class ToggleRepeatingCommand2 extends SCommand
{
    public Repeater repeater;
    
    @Override
    public void run(final CommandSource sender, final String[] args) {
        final Player player = sender.getPlayer();
        if (player == null) {
            this.send(ChatColor.RED + "Something occurred while taking services from the API!");
        }
    }
}
