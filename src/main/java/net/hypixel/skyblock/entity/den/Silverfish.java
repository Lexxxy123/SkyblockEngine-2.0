/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.entity.den;

import net.hypixel.skyblock.entity.EntityFunction;
import net.hypixel.skyblock.entity.EntityStatistics;

public class Silverfish
implements EntityStatistics,
EntityFunction {
    @Override
    public String getEntityName() {
        return "Silverfish";
    }

    @Override
    public double getEntityMaxHealth() {
        return 50.0;
    }

    @Override
    public double getDamageDealt() {
        return 20.0;
    }

    @Override
    public int mobLevel() {
        return 2;
    }

    @Override
    public double getXPDropped() {
        return 5.4;
    }
}

