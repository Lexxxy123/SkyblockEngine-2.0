package in.godspunky.skyblock.command;

import in.godspunky.skyblock.gui.GUIType;
import in.godspunky.skyblock.ranks.PlayerRank;
import org.bukkit.entity.Player;

@CommandParameters(description = "Gets the NBT of your current item.", aliases = "sbmenu", permission = PlayerRank.DEFAULT)
public class SkySimMenuCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        final Player player = sender.getPlayer();
        GUIType.SKYBLOCK_MENU.getGUI().open(player);
    }
}
