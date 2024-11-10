/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 */
package net.hypixel.skyblock.entity.den;

import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SEntityType;
import net.hypixel.skyblock.entity.den.BaseSpider;
import org.bukkit.entity.Entity;

public class SplitterSpider
extends BaseSpider {
    @Override
    public String getEntityName() {
        return "Splitter Spider";
    }

    @Override
    public double getEntityMaxHealth() {
        return 180.0;
    }

    @Override
    public double getDamageDealt() {
        return 30.0;
    }

    @Override
    public int mobLevel() {
        return 3;
    }

    @Override
    public double getXPDropped() {
        return 9.7;
    }

    @Override
    public void onDeath(SEntity sEntity, Entity killed, Entity damager) {
        super.onDeath(sEntity, killed, damager);
        for (int i = 0; i < 2; ++i) {
            new SEntity((Entity)sEntity.getEntity(), SEntityType.SILVERFISH, new Object[0]);
        }
    }
}

