package net.hypixel.skyblock.command;

import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.gui.GUIType;
import net.hypixel.skyblock.gui.menu.Items.HexGUI;

@CommandParameters(permission = PlayerRank.DEFAULT, aliases = "ie")
public class ItemEditorCommand extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        //sender.getPlayer().sendMessage("Coming soon");
        GUIType.ITEM_EDITOR.getGUI().open(sender.getPlayer());
    }
}
