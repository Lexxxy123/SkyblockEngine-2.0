/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.entity.den;

import java.util.Collections;
import java.util.List;
import net.hypixel.skyblock.entity.EntityDrop;
import net.hypixel.skyblock.entity.EntityDropType;
import net.hypixel.skyblock.entity.den.BaseSpider;
import net.hypixel.skyblock.item.SMaterial;

public class TarantulaVermin
extends BaseSpider {
    @Override
    public String getEntityName() {
        return "Tarantula Vermin";
    }

    @Override
    public double getEntityMaxHealth() {
        return 54000.0;
    }

    @Override
    public double getDamageDealt() {
        return 900.0;
    }

    @Override
    public double getXPDropped() {
        return 150.0;
    }

    @Override
    public List<EntityDrop> drops() {
        return Collections.singletonList(new EntityDrop(SMaterial.TARANTULA_WEB, EntityDropType.GUARANTEED, 1.0));
    }
}

