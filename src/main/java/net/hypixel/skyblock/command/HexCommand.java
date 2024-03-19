package net.hypixel.skyblock.command;

import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.gui.menu.Items.HexGUI;
import org.bukkit.entity.Player;

@CommandParameters(permission = PlayerRank.DEFAULT, aliases = "hex")
public class HexCommand extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        Player player = sender.getPlayer();
        new HexGUI(player).open(player);//push? u tell pushhhhhh
    }
}
