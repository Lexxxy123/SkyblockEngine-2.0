/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 */
package net.hypixel.skyblock.entity;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public final class KillEnderCrystal {
    public static void killEC(World world) {
        for (Entity entity : world.getEntities()) {
            if (entity.getType() != EntityType.ENDER_CRYSTAL) continue;
            entity.remove();
        }
    }
}

