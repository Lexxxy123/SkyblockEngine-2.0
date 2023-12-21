package in.godspunky.skyblock.entity.dungeons.boss.sadan;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import in.godspunky.skyblock.SkySimEngine;
import in.godspunky.skyblock.entity.SEntity;
import in.godspunky.skyblock.entity.SEntityType;
import in.godspunky.skyblock.entity.zombie.BaseZombie;
import in.godspunky.skyblock.util.EntityManager;
import in.godspunky.skyblock.util.Sputnik;

public class SleepingGolem_S extends BaseZombie {
    @Override
    public String getEntityName() {
        return Sputnik.trans("&5&lSleeping Golem");
    }

    @Override
    public double getEntityMaxHealth() {
        return 1.0;
    }

    @Override
    public double getDamageDealt() {
        return 0.0;
    }

    @Override
    public void onSpawn(final LivingEntity entity, final SEntity sEntity) {
        ((CraftZombie) entity).setBaby(false);
        final Location l = entity.getLocation().clone();
        l.setPitch(45.0f);
        entity.teleport(l);
        EntityManager.noAI(entity);
        Sputnik.applyPacketGolem(entity);
        EntityManager.DEFENSE_PERCENTAGE.put(entity, 0);
        entity.setMetadata("NNP", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
    }

    @Override
    public void onDeath(final SEntity sEntity, final Entity killed, final Entity damager) {
        new SEntity(killed.getLocation(), SEntityType.WOKE_GOLEM);
        killed.remove();
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    public boolean hasNameTag() {
        return false;
    }

    @Override
    public boolean isVillager() {
        return false;
    }

    @Override
    public double getXPDropped() {
        return 0.0;
    }

    @Override
    public double getMovementSpeed() {
        return 0.0;
    }
}
