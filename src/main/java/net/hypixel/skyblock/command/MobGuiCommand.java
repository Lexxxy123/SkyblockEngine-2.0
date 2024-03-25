package net.hypixel.skyblock.command;


import net.hypixel.skyblock.features.ranks.PlayerRank;
import net.hypixel.skyblock.gui.GUIType;

@CommandParameters(description = "open mob spawn gui.", usage = "/mobgui", aliases = "mobgui" , permission = PlayerRank.ADMIN)
public class MobGuiCommand extends SCommand{
    @Override
    public void run(CommandSource sender, String[] args) {
        GUIType.MOB_GUI.getGUI().open(sender.getPlayer());
    }
}