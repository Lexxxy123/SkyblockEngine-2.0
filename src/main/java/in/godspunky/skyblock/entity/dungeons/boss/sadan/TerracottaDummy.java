package in.godspunky.skyblock.entity.dungeons.boss.sadan;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import in.godspunky.skyblock.SkySimEngine;
import in.godspunky.skyblock.entity.SEntity;
import in.godspunky.skyblock.entity.SEntityEquipment;
import in.godspunky.skyblock.entity.zombie.BaseZombie;
import in.godspunky.skyblock.util.EntityManager;
import in.godspunky.skyblock.util.SUtil;
import in.godspunky.skyblock.util.Sputnik;

public class TerracottaDummy extends BaseZombie {
    @Override
    public String getEntityName() {
        return Sputnik.trans("");
    }

    @Override
    public double getEntityMaxHealth() {
        return 1.4E7;
    }

    @Override
    public double getDamageDealt() {
        return 40000.0;
    }

    @Override
    public void onSpawn(final LivingEntity entity, final SEntity sEntity) {
        if (entity.getWorld().getPlayers().size() == 0) {
            return;
        }
        ((CraftZombie) entity).setBaby(false);
        Sputnik.applyPacketNPC(entity, "Ethelian", null, false);
        EntityManager.noAI(entity);
        EntityManager.noHit(entity);
        entity.setMetadata("GiantSword", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
        entity.setMetadata("NoAffect", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
        entity.setMetadata("t_sadan_p1_1", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
        EntityManager.shutTheFuckUp(entity);
        EntityManager.DEFENSE_PERCENTAGE.put(entity, 100);
        entity.setMetadata("SlayerBoss", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
        entity.setMetadata("LD", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
        entity.setMetadata("notDisplay", new FixedMetadataValue(SkySimEngine.getPlugin(), true));
        SUtil.delay(() -> Sputnik.applyPacketNPC(entity, "Ethelian", null, false), 50L);
        new BukkitRunnable() {
            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                Sputnik.applyPacketNPC(entity, "Ethelian", null, false);
            }
        }.runTaskTimer(SkySimEngine.getPlugin(), 0L, 200L);
    }

    @Override
    public SEntityEquipment getEntityEquipment() {
        return new SEntityEquipment(null, null, null, null, null);
    }

    @Override
    public double getXPDropped() {
        return 0.0;
    }
}
