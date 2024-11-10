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
import net.hypixel.skyblock.util.SUtil;
import org.bukkit.entity.Entity;

public class BroodMother
extends BaseSpider {
    @Override
    public String getEntityName() {
        return "Brood Mother";
    }

    @Override
    public double getEntityMaxHealth() {
        return 6000.0;
    }

    @Override
    public double getDamageDealt() {
        return 100.0;
    }

    @Override
    public int mobLevel() {
        return 12;
    }

    @Override
    public void onDeath(SEntity sEntity, Entity killed, Entity damager) {
        int am = SUtil.random(4, 5);
        for (int i = 0; i < am; ++i) {
            new SEntity((Entity)sEntity.getEntity(), SEntityType.CAVE_SPIDER, new Object[0]);
        }
    }

    @Override
    public double getXPDropped() {
        return 17.0;
    }
}

