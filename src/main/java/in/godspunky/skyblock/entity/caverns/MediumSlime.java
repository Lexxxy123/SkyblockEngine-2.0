package in.godspunky.skyblock.entity.caverns;

import in.godspunky.skyblock.Skyblock;
import in.godspunky.skyblock.entity.EntityFunction;
import in.godspunky.skyblock.entity.SlimeStatistics;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class MediumSlime implements SlimeStatistics, EntityFunction {
    @Override
    public String getEntityName() {
        return "Slime";
    }

    @Override
    public double getEntityMaxHealth() {
        return 150.0;
    }

    @Override
    public double getDamageDealt() {
        return 100.0;
    }

    @Override
    public int getSize() {
        return 7;
    }

    @Override
    public void onAttack(final EntityDamageByEntityEvent e) {
        new BukkitRunnable() {
            public void run() {
                e.getEntity().setVelocity(e.getEntity().getVelocity().clone().setY(1.5));
            }
        }.runTaskLater(Skyblock.getPlugin(), 1L);
    }

    @Override
    public double getXPDropped() {
        return 15.0;
    }
}
