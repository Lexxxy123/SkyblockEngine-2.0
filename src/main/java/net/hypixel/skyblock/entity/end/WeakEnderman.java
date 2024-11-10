/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.entity.end;

import net.hypixel.skyblock.entity.end.BaseEnderman;

public class WeakEnderman
extends BaseEnderman {
    @Override
    public String getEntityName() {
        return "Enderman";
    }

    @Override
    public double getEntityMaxHealth() {
        return 4500.0;
    }

    @Override
    public double getDamageDealt() {
        return 500.0;
    }

    @Override
    public int mobLevel() {
        return 42;
    }

    @Override
    public double getXPDropped() {
        return 28.0;
    }
}

