package net.hypixel.skyblock.entity.nms;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import net.hypixel.skyblock.entity.EntityFunction;
import net.hypixel.skyblock.entity.EntityStatistics;

public class VelocityArmorStand extends EntityArmorStand implements EntityStatistics, EntityFunction, SNMSEntity {
    public VelocityArmorStand(final World world) {
        super(world);
        this.setGravity(true);
        this.noclip = true;
    }

    public VelocityArmorStand() {
        this(((CraftWorld) Bukkit.getWorlds().get(0)).getHandle());
    }

    public void g(final float f, final float f1) {
        if (!this.hasGravity()) {
            super.g(f, f1);
        } else {
            this.move(this.motX, this.motY, this.motZ);
        }
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
