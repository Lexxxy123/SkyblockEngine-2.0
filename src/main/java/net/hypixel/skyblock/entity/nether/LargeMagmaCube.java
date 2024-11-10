/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftMagmaCube
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Fireball
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.entity.EntityTargetLivingEntityEvent
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package net.hypixel.skyblock.entity.nether;

import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.entity.EntityFunction;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SlimeStatistics;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftMagmaCube;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class LargeMagmaCube
implements SlimeStatistics,
EntityFunction {
    @Override
    public String getEntityName() {
        return "Magma Cube";
    }

    @Override
    public double getEntityMaxHealth() {
        return 300.0;
    }

    @Override
    public double getDamageDealt() {
        return 150.0;
    }

    @Override
    public int mobLevel() {
        return 9;
    }

    @Override
    public double getXPDropped() {
        return 4.0;
    }

    @Override
    public void onTarget(SEntity sEntity, EntityTargetLivingEntityEvent e) {
        final LivingEntity entity = (LivingEntity)e.getEntity();
        LivingEntity found = e.getTarget();
        new BukkitRunnable((Entity)found, sEntity){
            final /* synthetic */ Entity val$found;
            final /* synthetic */ SEntity val$sEntity;
            {
                this.val$found = entity2;
                this.val$sEntity = sEntity;
            }

            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                CraftEntity target = ((CraftMagmaCube)entity).getHandle().getGoalTarget().getBukkitEntity();
                if (!this.val$found.equals(target)) {
                    this.cancel();
                    return;
                }
                for (int i = 0; i < 3; ++i) {
                    new BukkitRunnable((Entity)target){
                        final /* synthetic */ Entity val$target;
                        {
                            this.val$target = entity;
                        }

                        public void run() {
                            if (entity.isDead()) {
                                this.cancel();
                                return;
                            }
                            Fireball fireball = (Fireball)entity.getWorld().spawn(entity.getEyeLocation().add(entity.getEyeLocation().getDirection().multiply(3.0)), Fireball.class);
                            fireball.setMetadata("magma", (MetadataValue)new FixedMetadataValue((Plugin)SkyBlock.getPlugin(), (Object)val$sEntity));
                            fireball.setDirection(this.val$target.getLocation().getDirection().multiply(-1.0).normalize());
                        }
                    }.runTaskLater((Plugin)SkyBlock.getPlugin(), (long)((i + 1) * 10));
                }
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 60L, 100L);
    }

    @Override
    public int getSize() {
        return 6;
    }

    @Override
    public boolean split() {
        return false;
    }
}

