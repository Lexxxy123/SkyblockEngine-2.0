package net.hypixel.skyblock.command;

import org.bukkit.entity.Player;
import net.hypixel.skyblock.gui.GUIType;

@CommandParameters(description = "Gets the NBT of your current item.", aliases = "sbmenu", permission = "sse.cc")
public class SkySimMenuCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        final Player player = sender.getPlayer();
        GUIType.SKYBLOCK_MENU.getGUI().open(player);
    }
}
