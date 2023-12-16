package vn.giakhanhvn.skysim.entity.nms;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import vn.giakhanhvn.skysim.entity.EntityFunction;
import vn.giakhanhvn.skysim.entity.EntityStatistics;

public class UncollidableArmorStand extends EntityArmorStand implements EntityStatistics, EntityFunction, SNMSEntity {
    public UncollidableArmorStand(final World world) {
        super(world);
        this.n(true);
    }

    public UncollidableArmorStand() {
        this(((CraftWorld) Bukkit.getWorlds().get(0)).getHandle());
    }

    public String getEntityName() {
        return null;
    }

    public double getEntityMaxHealth() {
        return 1.0;
    }

    public double getDamageDealt() {
        return 0.0;
    }

    public boolean hasNameTag() {
        return false;
    }

    public double getXPDropped() {
        return 0.0;
    }

    public LivingEntity spawn(final Location location) {
        this.world = ((CraftWorld) location.getWorld()).getHandle();
        this.setPosition(location.getX(), location.getY(), location.getZ());
        this.world.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (LivingEntity) this.getBukkitEntity();
    }
}
