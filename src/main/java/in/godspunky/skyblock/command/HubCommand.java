package in.godspunky.skyblock.command;

import in.godspunky.skyblock.ranks.PlayerRank;
import in.godspunky.skyblock.region.RegionGenerator;
import in.godspunky.skyblock.util.Sputnik;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

@CommandParameters(description = "Manage world regions.", usage = "", aliases = "hub", permission = PlayerRank.DEFAULT)
public class HubCommand extends SCommand {
    public static Map<CommandSender, RegionGenerator> REGION_GENERATION_MAP;

    static {
        HubCommand.REGION_GENERATION_MAP = new HashMap<CommandSender, RegionGenerator>();
    }

    @Override
    public void run(final CommandSource sender, final String[] args) {
        this.send(Sputnik.trans("&7Sending you to the Hub..."));
        final Location l = new Location(Bukkit.getWorld("world"), -2.5, 70.0, -68.5, 180.0f, 0.0f);
        if (sender.getPlayer() != null) {
            sender.getPlayer().teleport(l);
        }
    }
}
