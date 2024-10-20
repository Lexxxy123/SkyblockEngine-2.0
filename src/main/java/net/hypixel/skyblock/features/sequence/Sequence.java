/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 */
package net.hypixel.skyblock.features.sequence;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public interface Sequence {
    public void play(Location var1);

    public void play(Entity var1);
}

