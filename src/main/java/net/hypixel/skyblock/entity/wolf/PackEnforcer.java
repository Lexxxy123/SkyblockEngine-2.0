/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.entity.wolf;

import java.util.Collections;
import java.util.List;
import net.hypixel.skyblock.entity.EntityDrop;
import net.hypixel.skyblock.entity.EntityDropType;
import net.hypixel.skyblock.entity.wolf.BaseWolf;
import net.hypixel.skyblock.item.SMaterial;

public class PackEnforcer
extends BaseWolf {
    @Override
    public String getEntityName() {
        return "Pack Enforcer";
    }

    @Override
    public double getEntityMaxHealth() {
        return 45000.0;
    }

    @Override
    public double getDamageDealt() {
        return 900.0;
    }

    @Override
    public int mobLevel() {
        return 100;
    }

    @Override
    public List<EntityDrop> drops() {
        return Collections.singletonList(new EntityDrop(SMaterial.WOLF_TOOTH, EntityDropType.GUARANTEED, 1.0));
    }

    @Override
    public double getXPDropped() {
        return 150.0;
    }

    @Override
    public boolean isAngry() {
        return true;
    }
}

