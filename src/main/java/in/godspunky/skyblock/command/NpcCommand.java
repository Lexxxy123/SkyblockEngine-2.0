package in.godspunky.skyblock.command;

import in.godspunky.skyblock.gui.GUIType;
import in.godspunky.skyblock.ranks.PlayerRank;
import in.godspunky.skyblock.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@CommandParameters(description = "npc cmds", aliases = "sbnpc", permission = PlayerRank.DEFAULT)
public class NpcCommand extends SCommand{
    @Override
    public void run(CommandSource sender, String[] args) {
        Player player = sender.getPlayer();

        if (args.length == 0) {
            player.sendMessage("You Need Admin Rank!");
            return;
        }
        final String lowerCase = args[0].toLowerCase();
        switch (lowerCase) {
            case "reforge":
                GUIType.REFORGE_ANVIL.getGUI().open(player);
                return;
            case "Batphone":
                GUIType.SLAYER.getGUI().open(player);
                return;
            default:
                throw new CommandArgumentException();
        }
    }
}
