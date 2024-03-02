package net.hypixel.skyblock.entity.dungeons.boss.sadan;

import net.hypixel.skyblock.SkyBlock;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SEntityEquipment;
import net.hypixel.skyblock.entity.zombie.BaseZombie;
import net.hypixel.skyblock.util.EntityManager;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;

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
        entity.setMetadata("GiantSword", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        entity.setMetadata("NoAffect", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        entity.setMetadata("t_sadan_p1_1", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        EntityManager.shutTheFuckUp(entity);
        EntityManager.DEFENSE_PERCENTAGE.put(entity, 100);
        entity.setMetadata("SlayerBoss", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        entity.setMetadata("LD", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        entity.setMetadata("notDisplay", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        SUtil.delay(() -> Sputnik.applyPacketNPC(entity, "Ethelian", null, false), 50L);
        new BukkitRunnable() {
            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                Sputnik.applyPacketNPC(entity, "Ethelian", null, false);
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 0L, 200L);
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
