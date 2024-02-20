package in.godspunky.skyblock.entity.dungeons.regularentity;

import in.godspunky.skyblock.SkyBlock;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEnderman;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import in.godspunky.skyblock.entity.SEntity;
import in.godspunky.skyblock.entity.end.BaseEnderman;
import in.godspunky.skyblock.util.EntityManager;
import in.godspunky.skyblock.util.SUtil;

import java.util.ArrayList;
import java.util.Collection;

public class Fels extends BaseEnderman {
    private boolean spawned;

    public Fels() {
        this.spawned = false;
    }

    @Override
    public String getEntityName() {
        return "Fels";
    }

    @Override
    public double getEntityMaxHealth() {
        return 1.0E8;
    }

    @Override
    public double getDamageDealt() {
        return 1800000.0;
    }

    @Override
    public void onSpawn(final LivingEntity entity, final SEntity sEntity) {
        final ArmorStand drop = (ArmorStand) entity.getWorld().spawn(entity.getLocation().clone().add(0.0, -1.4, 0.0), (Class) ArmorStand.class);
        drop.setVisible(false);
        drop.setGravity(false);
        drop.setCustomNameVisible(false);
        drop.getEquipment().setHelmet(SUtil.getSkullURLStack("asadas", "96c0b36d53fff69a49c7d6f3932f2b0fe948e032226d5e8045ec58408a36e951", 0, ""));
        entity.setMetadata("upsidedown", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        entity.setMetadata("SlayerBoss", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        entity.setMetadata("DungeonMobs", new FixedMetadataValue(SkyBlock.getPlugin(), true));
        EntityManager.DEFENSE_PERCENTAGE.put(entity, 65);
        entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 10000, 100));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10000, 100));
        new BukkitRunnable() {
            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                if (!Fels.this.spawned) {
                    entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 10000, 100));
                    entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10000, 100));
                    ((CraftEnderman) entity).setTarget(null);
                    final Collection<Entity> ce = entity.getWorld().getNearbyEntities(entity.getLocation(), 0.5, 0.5, 0.5);
                    ce.removeIf(entity -> !(entity instanceof Player));
                    if (ce.size() > 0) {
                        drop.remove();
                        entity.removePotionEffect(PotionEffectType.INVISIBILITY);
                        entity.removePotionEffect(PotionEffectType.SLOW);
                        final ArrayList<LivingEntity> ep = new ArrayList<LivingEntity>();
                        ce.addAll(ep);
                        Fels.this.spawned = true;
                    }
                }
            }
        }.runTaskTimer(SkyBlock.getPlugin(), 20L, 1L);
    }

    @Override
    public double getXPDropped() {
        return 320.0;
    }

    @Override
    public int mobLevel() {
        return 0;
    }
}
