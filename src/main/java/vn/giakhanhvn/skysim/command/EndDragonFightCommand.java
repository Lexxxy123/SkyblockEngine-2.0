package vn.giakhanhvn.skysim.command;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import vn.giakhanhvn.skysim.entity.StaticDragonManager;
import vn.giakhanhvn.skysim.util.SUtil;

@CommandParameters(description = "bruhbu", aliases = "edf")
public class EndDragonFightCommand extends SCommand {
    @Override
    public void run(final CommandSource sender, final String[] args) {
        final Player player = sender.getPlayer();
        final World world = player.getWorld();
        if (world.getName().equalsIgnoreCase("dragon")) {
            if (StaticDragonManager.ACTIVE) {
                this.send(ChatColor.GREEN + "Processing...");
                SUtil.delay(() -> StaticDragonManager.endFight(), 10L);
                this.endDragonFight(world);
                SUtil.delay(() -> SUtil.broadcast(ChatColor.RED + "[SYSTEM] " + ChatColor.YELLOW + player.getName() + " have ended all Dragon fight in this world!", player), 10L);
            } else {
                this.send(ChatColor.RED + "There are no active dragon fight!");
            }
        } else {
            this.send(ChatColor.RED + "This command is not available on this world!");
        }
    }

    public void endDragonFight(final World world) {
        for (final Entity e : world.getEntities()) {
            if (e.getType() == EntityType.ENDER_DRAGON || e.getType() == EntityType.ENDER_CRYSTAL) {
                e.remove();
            }
        }
    }
}
