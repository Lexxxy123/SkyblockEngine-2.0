/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package net.hypixel.skyblock.entity.den;

import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.entity.EntityFunction;
import net.hypixel.skyblock.entity.SlimeStatistics;
import net.hypixel.skyblock.util.SUtil;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SpidersDenSlime
implements SlimeStatistics,
EntityFunction {
    @Override
    public String getEntityName() {
        return "Slime";
    }

    @Override
    public double getEntityMaxHealth() {
        return SUtil.random(200.0, 400.0);
    }

    @Override
    public double getDamageDealt() {
        return 140.0;
    }

    @Override
    public int getSize() {
        return 9;
    }

    @Override
    public void onAttack(final EntityDamageByEntityEvent e2) {
        new BukkitRunnable(){

            public void run() {
                e2.getEntity().setVelocity(e2.getEntity().getVelocity().clone().setY(1.5));
            }
        }.runTaskLater((Plugin)SkyBlock.getPlugin(), 1L);
    }

    @Override
    public double getXPDropped() {
        return 4.0;
    }
}

