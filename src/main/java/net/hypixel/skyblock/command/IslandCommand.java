package net.hypixel.skyblock.command;

import net.hypixel.skyblock.features.island.SkyblockIsland;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@CommandParameters(description = "go to or create your island", aliases = "is")
public class IslandCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            throw new CommandFailException("Console senders cannot use this command!");
        }
        final Player player = sender.getPlayer();
        SkyblockIsland.getIsland(player.getUniqueId()).send();
    }
}
