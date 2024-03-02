package net.hypixel.skyblock.entity.dimoon.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import net.hypixel.skyblock.entity.dimoon.Dimoon;

public class SummonDimoon implements CommandExecutor {
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        Dimoon.spawnDimoon();
        return true;
    }
}
