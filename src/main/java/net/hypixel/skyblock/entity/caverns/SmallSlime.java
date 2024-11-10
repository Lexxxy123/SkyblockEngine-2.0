/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package net.hypixel.skyblock.entity.caverns;

import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.entity.EntityFunction;
import net.hypixel.skyblock.entity.SlimeStatistics;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SmallSlime
implements SlimeStatistics,
EntityFunction {
    @Override
    public String getEntityName() {
        return "Slime";
    }

    @Override
    public double getEntityMaxHealth() {
        return 80.0;
    }

    @Override
    public double getDamageDealt() {
        return 70.0;
    }

    @Override
    public int mobLevel() {
        return 5;
    }

    @Override
    public int getSize() {
        return 5;
    }

    @Override
    public void onAttack(final EntityDamageByEntityEvent e) {
        new BukkitRunnable(){

            public void run() {
                e.getEntity().setVelocity(e.getEntity().getVelocity().clone().setY(1.5));
            }
        }.runTaskLater((Plugin)SkyBlock.getPlugin(), 1L);
    }

    @Override
    public double getXPDropped() {
        return 12.0;
    }
}

