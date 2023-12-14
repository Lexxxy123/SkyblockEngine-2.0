package vn.giakhanhvn.skysim.command;

import org.bukkit.ChatColor;

@CommandParameters(description = "The main command for Spectaculation.", aliases = "sse")
public class SpectaculationCommand extends SCommand
{
    @Override
    public void run(final CommandSource sender, final String[] args) {
        this.send(ChatColor.RED + "Sky" + ChatColor.GREEN + "Sim" + ChatColor.GOLD + " Engine v" + SpectaculationCommand.plugin.getDescription().getVersion());
        this.send(ChatColor.GREEN + "Created by" + ChatColor.GOLD + " GiaKhanhVN " + ChatColor.GREEN + "for SkySim Usage and the \ncompatibility with highly modified version of Spigot!");
    }
}
