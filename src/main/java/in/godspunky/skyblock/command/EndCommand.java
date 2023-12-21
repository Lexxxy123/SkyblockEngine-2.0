package in.godspunky.skyblock.command;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import in.godspunky.skyblock.entity.dungeons.boss.sadan.SadanBossManager;
import in.godspunky.skyblock.util.Sputnik;

@CommandParameters(description = "Spec test command.", aliases = "fed")
public class EndCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        final Player player = sender.getPlayer();
        if (player.isOp()) {
            final World world = player.getWorld();
            Bukkit.broadcastMessage(Sputnik.trans("&c[SYSTEM] &e" + player.getName() + " forcing the bossroom &c" + world.getName() + " &eto end."));
            SadanBossManager.endFloor(world);
        }
    }
}
