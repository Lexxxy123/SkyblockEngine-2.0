/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.material.MaterialData
 */
package net.hypixel.skyblock.entity.end;

import net.hypixel.skyblock.entity.EntityStatistics;
import org.bukkit.material.MaterialData;

public interface EndermanStatistics
extends EntityStatistics {
    default public MaterialData getCarriedMaterial() {
        return null;
    }
}

