/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.entity.wolf;

import net.hypixel.skyblock.entity.Ageable;
import net.hypixel.skyblock.entity.EntityStatistics;

public interface WolfStatistics
extends EntityStatistics,
Ageable {
    default public boolean isAngry() {
        return false;
    }
}

