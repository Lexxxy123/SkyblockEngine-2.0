/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.entity.end;

import java.util.Collections;
import java.util.List;
import net.hypixel.skyblock.entity.EntityDrop;
import net.hypixel.skyblock.entity.EntityDropType;
import net.hypixel.skyblock.entity.end.BaseEnderman;
import net.hypixel.skyblock.item.SMaterial;

public class VoidlingDevotee
extends BaseEnderman {
    @Override
    public String getEntityName() {
        return "Voidling Devotee";
    }

    @Override
    public double getEntityMaxHealth() {
        return 2.5E7;
    }

    @Override
    public double getDamageDealt() {
        return 5000.0;
    }

    @Override
    public int mobLevel() {
        return 1730;
    }

    @Override
    public double getXPDropped() {
        return 1500.0;
    }

    @Override
    public List<EntityDrop> drops() {
        return Collections.singletonList(new EntityDrop(SMaterial.NULL_SPHERE, EntityDropType.GUARANTEED, 1.0));
    }
}

