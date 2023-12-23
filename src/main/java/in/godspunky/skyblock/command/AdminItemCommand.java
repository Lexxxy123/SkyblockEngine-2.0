package in.godspunky.skyblock.command;

import in.godspunky.skyblock.gui.GUIType;
import in.godspunky.skyblock.ranks.PlayerRank;


@CommandParameters(description = "Open Admin Items Gui", usage = "/sib", aliases = "sib", permission = PlayerRank.ADMIN)
public class AdminItemCommand extends SCommand{
    @Override
    public void run(CommandSource sender, String[] args) {
        if (!sender.getPlayer().isOp()) return;
        GUIType.Admin_Items.getGUI().open(sender.getPlayer());
    }
}