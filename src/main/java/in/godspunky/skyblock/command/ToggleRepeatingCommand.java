package in.godspunky.skyblock.command;

import in.godspunky.skyblock.Repeater;
import in.godspunky.skyblock.ranks.PlayerRank;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@CommandParameters(description = "Spec test command.", aliases = "db:tsr", permission = PlayerRank.ADMIN)
public class ToggleRepeatingCommand extends SCommand {
    public Repeater repeater;

    @Override
    public void run(final CommandSource sender, final String[] args) {
        final Player player = sender.getPlayer();
        if (player != null) {
            if (this.repeater == null) {
                this.repeater = new Repeater();
                player.sendMessage("SERVER LOOP TURNED ON");
            } else {
                this.repeater.stop();
                player.sendMessage("SERVER LOOP SHUTTED DOWN!");
            }
        } else {
            this.send(ChatColor.RED + "Something occurred while taking services from the API!");
        }
    }
}
