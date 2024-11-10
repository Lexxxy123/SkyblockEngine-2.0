/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package net.hypixel.skyblock.entity.dungeons.boss.sadan;

import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SEntityType;
import net.hypixel.skyblock.entity.zombie.BaseZombie;
import net.hypixel.skyblock.util.EntityManager;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class SleepingGolem_S
extends BaseZombie {
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
    public void onSpawn(LivingEntity entity, SEntity sEntity) {
        ((CraftZombie)entity).setBaby(false);
        Location l = entity.getLocation().clone();
        l.setPitch(45.0f);
        entity.teleport(l);
        EntityManager.noAI((Entity)entity);
        Sputnik.applyPacketGolem((Entity)entity);
        EntityManager.DEFENSE_PERCENTAGE.put((Entity)entity, 0);
        entity.setMetadata("NNP", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)true));
    }

    @Override
    public void onDeath(SEntity sEntity, Entity killed, Entity damager) {
        new SEntity(killed.getLocation(), SEntityType.WOKE_GOLEM, new Object[0]);
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

