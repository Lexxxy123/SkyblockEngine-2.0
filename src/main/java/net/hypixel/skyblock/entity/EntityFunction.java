package net.hypixel.skyblock.entity;

import com.google.common.util.concurrent.AtomicDouble;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.api.hologram.Hologram;
import net.hypixel.skyblock.util.PacketEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftSkeleton;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import net.hypixel.skyblock.util.Sputnik;

import java.util.*;

public interface EntityFunction {
    Map<Entity, ArrayList<UUID>> FIRST_STRIKE_MAP = new HashMap<Entity, ArrayList<UUID>>();

    default void onDeath(final SEntity sEntity, final Entity killed, final Entity damager) {
    }

    default void onDamage(final SEntity sEntity, final Entity damager, final EntityDamageByEntityEvent e, final AtomicDouble damage) {
    }

    default List<EntityDrop> drops() {
        return new ArrayList<EntityDrop>();
    }

    default boolean tick(final LivingEntity entity) {
        return false;
    }



     default void nameTag(final LivingEntity entity, final SEntity sEntity, final SEntityType specType, final Object... params){
         Hologram hologram1 = new Hologram(entity.getLocation());
         hologram1.setSmall(true);
         hologram1.setBasePlate(true);
         hologram1.setCustomNameVisible(false);

         hologram1.mount(((CraftEntity)entity).getHandle());
         if (!entity.hasMetadata("LD")) {
             hologram1.remove();
         }
        FIRST_STRIKE_MAP.put(entity, new ArrayList<UUID>());
         if (entity.hasMetadata("notDisplay")) {
             return;
         }
         if (entity.getType() == EntityType.ARMOR_STAND) {
             return;
         }
         if (entity.getType() == EntityType.ENDER_DRAGON) {
             return;
         }
         if (entity.getType() == EntityType.ENDER_CRYSTAL) {
             return;
         }

         final net.minecraft.server.v1_8_R3.Entity e = ((CraftEntity) entity).getHandle();
         double height_ = e.getBoundingBox().e - e.getBoundingBox().b;
         final Object instance = specType.instance(params);
         final EntityStatistics statistics = (EntityStatistics) instance;
         if (entity.getType() == EntityType.SKELETON || entity.hasMetadata("SKEL")) {
             height_ += 0.2;
         }
         if (entity.getType() == EntityType.SKELETON && ((CraftSkeleton) entity).getSkeletonType() == Skeleton.SkeletonType.WITHER) {
             height_ -= 0.2;
         }
         if (entity.hasMetadata("LD")) {
             height_ -= 0.144;
         }
         if (entity.hasMetadata("highername")) {
             height_ += 10.5;
         }
         if (entity.hasMetadata("NNP")) {
             height_ += 0.8;
         }
         if (entity.hasMetadata("NNPS")) {
             height_ += 0.8;
         }
         final double height = height_;

         Hologram hologram2 = new Hologram(entity.getLocation().add(0.0, height, 0.0));

         boolean hideLVL = entity.hasMetadata("WATCHER_E");

         new BukkitRunnable(){
             @Override
             public void run() {
                 hologram2.setCustomNameVisible(!entity.hasPotionEffect(PotionEffectType.INVISIBILITY));

                 if (entity.isDead()) {
                     if (entity.hasMetadata("NNP")) {
                         hologram2.remove();
                     }
                     if (entity.hasMetadata("h_sadan")) {
                         hologram2.remove();
                     }
                     FIRST_STRIKE_MAP.remove(entity);
                     hologram2.setText(Sputnik.trans(Sputnik.entityNameTag(entity, Sputnik.buildcustomString(statistics.getEntityName(), statistics.mobLevel(), hideLVL))));
                     new BukkitRunnable() {
                         public void run() {
                             hologram2.remove();
                             hologram1.remove();
                         }
                     }.runTaskLater(SkyBlock.getPlugin(), 20L);
                 }
                 if (!hologram2.isAlive()) {
                     this.cancel();
                     return;
                 }
                 if (entity.hasMetadata("RMHN")) {
                     hologram1.remove();
                 } else {
                     entity.setPassenger(hologram1.getBukkitEntity());
                 }
                 hologram2.teleport(entity.getLocation().clone().add(0.0, height, 0.0));
                 hologram2.teleport(entity.getLocation().clone().add(0.0, height, 0.0));
                 if (!entity.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                     if (entity.hasMetadata("Boss")) {
                         hologram2.setText(Sputnik.trans("&e﴾ " + Sputnik.entityNameTag(entity, Sputnik.buildcustomString(statistics.getEntityName(), statistics.mobLevel(), true)) + " &e﴿"));
                     } else if (entity.hasMetadata("NNP")) {
                         hologram2.setText(statistics.getEntityName());
                     } else {
                         hologram2.setText(Sputnik.trans(Sputnik.entityNameTag(entity, Sputnik.buildcustomString(statistics.getEntityName(), statistics.mobLevel(), hideLVL))));
                     }
                 }
             }
         }.runTaskTimer(SkyBlock.getPlugin(), 0L, 0L);
    }

    default void onSpawn(final LivingEntity entity, final SEntity sEntity) {
    }

    default void onAttack(final EntityDamageByEntityEvent e) {
    }

    default void onTarget(final SEntity sEntity, final EntityTargetLivingEntityEvent e) {
    }

    default void onSpawnNameTag(final LivingEntity entity, final SEntityType specType, final Object... params) {
        this.nameTag(entity, SEntity.findSEntity(entity), specType, params);
    }

    default void onSpawn(final LivingEntity entity) {
        this.onSpawn(entity, SEntity.findSEntity(entity));
    }
}
