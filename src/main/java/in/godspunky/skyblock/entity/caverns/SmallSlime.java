package in.godspunky.skyblock.entity.caverns;

import in.godspunky.skyblock.SkyBlock;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import in.godspunky.skyblock.entity.EntityFunction;
import in.godspunky.skyblock.entity.SlimeStatistics;

public class SmallSlime implements SlimeStatistics, EntityFunction {
    @Override
    public String getEntityName() {
        return "Slime";
    }

    @Override
    public double getEntityMaxHealth() {
        return 80.0;
    }

    @Override
    public double getDamageDealt() {
        return 70.0;
    }

    @Override
    public int getSize() {
        return 5;
    }

    @Override
    public void onAttack(final EntityDamageByEntityEvent e) {
        new BukkitRunnable() {
            public void run() {
                e.getEntity().setVelocity(e.getEntity().getVelocity().clone().setY(1.5));
            }
        }.runTaskLater(SkyBlock.getPlugin(), 1L);
    }

    @Override
    public double getXPDropped() {
        return 12.0;
    }
}
