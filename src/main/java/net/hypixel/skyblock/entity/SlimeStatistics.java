/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.entity;

import net.hypixel.skyblock.entity.EntityStatistics;

public interface SlimeStatistics
extends EntityStatistics {
    default public int getSize() {
        return 1;
    }

    default public boolean split() {
        return false;
    }
}

