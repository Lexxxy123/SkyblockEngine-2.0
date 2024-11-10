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

public class OldWolf
extends BaseWolf {
    @Override
    public String getEntityName() {
        return "Old Wolf";
    }

    @Override
    public double getEntityMaxHealth() {
        return 15000.0;
    }

    @Override
    public double getDamageDealt() {
        return 720.0;
    }

    @Override
    public int mobLevel() {
        return 50;
    }

    @Override
    public List<EntityDrop> drops() {
        return Collections.singletonList(new EntityDrop(SMaterial.BONE, EntityDropType.GUARANTEED, 1.0));
    }

    @Override
    public double getXPDropped() {
        return 40.0;
    }

    @Override
    public boolean isAngry() {
        return true;
    }
}

