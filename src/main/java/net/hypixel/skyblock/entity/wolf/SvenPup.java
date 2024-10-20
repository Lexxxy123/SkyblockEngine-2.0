/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Wolf
 */
package net.hypixel.skyblock.entity.wolf;

import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.nms.SvenPackmaster;
import net.hypixel.skyblock.entity.wolf.BaseWolf;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Wolf;

public class SvenPup
extends BaseWolf {
    private final double health;
    private final double damage;
    private final CraftPlayer target;
    private final SvenPackmaster parent;

    public SvenPup(Double health, Double damage, CraftPlayer target, SvenPackmaster parent) {
        this.health = health;
        this.damage = damage;
        this.target = target;
        this.parent = parent;
    }

    @Override
    public String getEntityName() {
        return "Sven Pup";
    }

    @Override
    public double getEntityMaxHealth() {
        return this.health;
    }

    @Override
    public double getDamageDealt() {
        return this.damage;
    }

    @Override
    public double getXPDropped() {
        return 0.0;
    }

    @Override
    public boolean isAngry() {
        return true;
    }

    @Override
    public boolean isBaby() {
        return true;
    }

    @Override
    public void onSpawn(LivingEntity entity, SEntity sEntity) {
        ((Wolf)entity).setTarget((LivingEntity)this.target);
    }

    @Override
    public void onDeath(SEntity sEntity, Entity killed, Entity damager) {
        this.parent.getPups().remove(sEntity);
    }

    public SvenPackmaster getParent() {
        return this.parent;
    }
}

