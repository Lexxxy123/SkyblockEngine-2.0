package in.godspunky.skyblock.command;


import in.godspunky.skyblock.gui.menu.Items.HexGUI;
import in.godspunky.skyblock.ranks.PlayerRank;
import org.bukkit.entity.Player;

@CommandParameters(permission = PlayerRank.DEFAULT, aliases = "hex")
public class HexCommand extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        Player player = sender.getPlayer();
        new HexGUI(player).open(player);
    }
}
