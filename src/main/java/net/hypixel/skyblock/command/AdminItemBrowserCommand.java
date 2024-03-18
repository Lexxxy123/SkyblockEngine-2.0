package net.hypixel.skyblock.command;

import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.gui.GUIType;

@CommandParameters(description = "Admin Item Browser", aliases = "iba", permission = PlayerRank.ADMIN)
public class AdminItemBrowserCommand extends SCommand{
    @Override
    public void run(CommandSource sender, String[] args) {
        GUIType.ADMIN_ITEM_BROWSER.getGUI().open(sender.getPlayer());
    }
}
