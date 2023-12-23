package in.godspunky.skyblock.command;

import in.godspunky.skyblock.Repeater;
import in.godspunky.skyblock.ranks.PlayerRank;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@CommandParameters(description = "Spec test command.", aliases = "db:ssp", permission = PlayerRank.ADMIN)
public class ToggleRepeatingCommand2 extends SCommand {
    public Repeater repeater;

    @Override
    public void run(final CommandSource sender, final String[] args) {
        final Player player = sender.getPlayer();
        if (player == null) {
            this.send(ChatColor.RED + "Something occurred while taking services from the API!");
        }
    }
}
