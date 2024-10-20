/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.entity;

import net.hypixel.skyblock.entity.EntityStatistics;

public interface SkeletonStatistics
extends EntityStatistics {
    default public boolean isWither() {
        return false;
    }
}

