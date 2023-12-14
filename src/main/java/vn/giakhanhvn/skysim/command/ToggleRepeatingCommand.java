package vn.giakhanhvn.skysim.command;

import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import vn.giakhanhvn.skysim.Repeater;

@CommandParameters(description = "Spec test command.", aliases = "db:tsr")
public class ToggleRepeatingCommand extends SCommand
{
    public Repeater repeater;
    
    @Override
    public void run(final CommandSource sender, final String[] args) {
        final Player player = sender.getPlayer();
        if (player != null) {
            if (this.repeater == null) {
                this.repeater = new Repeater();
                player.sendMessage("SERVER LOOP TURNED ON");
            }
            else {
                this.repeater.stop();
                player.sendMessage("SERVER LOOP SHUTTED DOWN!");
            }
        }
        else {
            this.send(ChatColor.RED + "Something occurred while taking services from the API!");
        }
    }
}
