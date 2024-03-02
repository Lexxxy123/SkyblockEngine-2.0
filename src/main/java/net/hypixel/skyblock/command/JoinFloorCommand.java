package net.hypixel.skyblock.command;

import net.hypixel.skyblock.gui.GUIType;

@CommandParameters(description = "Spec test command.", aliases = "joinfloor6")
public class JoinFloorCommand extends SCommand {
    @Override
    public void run(CommandSource sender, String[] args) {
        GUIType.CATACOMBS_BOSS.getGUI().open(sender.getPlayer());
    }
}
