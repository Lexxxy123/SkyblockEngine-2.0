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



    default void onSpawnNameTag(final LivingEntity entity, final SEntity sEntity, final SEntityType specType, final Object... params) {

    }


    default void onSpawn(final LivingEntity entity, final SEntity sEntity) {
    }

    default void onAttack(final EntityDamageByEntityEvent e) {
    }

    default void onTarget(final SEntity sEntity, final EntityTargetLivingEntityEvent e) {
    }

    default void onSpawnNameTag(final LivingEntity entity, final SEntityType specType, final Object... params) {
        this.onSpawnNameTag(entity, SEntity.findSEntity(entity), specType, params);
    }

    default void onSpawn(final LivingEntity entity) {
        this.onSpawn(entity, SEntity.findSEntity(entity));
    }
}
