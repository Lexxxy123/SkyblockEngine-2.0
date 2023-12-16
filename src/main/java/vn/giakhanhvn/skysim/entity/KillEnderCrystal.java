package vn.giakhanhvn.skysim.entity;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public final class KillEnderCrystal {
    public static void killEC(final World world) {
        for (final Entity entity : world.getEntities()) {
            if (entity.getType() == EntityType.ENDER_CRYSTAL) {
                entity.remove();
            }
        }
    }
}
