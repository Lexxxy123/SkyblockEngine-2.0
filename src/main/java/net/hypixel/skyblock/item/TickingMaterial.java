/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.item;

import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.SItem;
import org.bukkit.entity.Player;

public interface TickingMaterial
extends MaterialFunction {
    default public void tick(SItem item, Player owner) {
    }

    default public long getInterval() {
        return 2L;
    }
}

