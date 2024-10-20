/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.World
 *  org.bukkit.Bukkit
 *  org.bukkit.craftbukkit.v1_8_R3.CraftWorld
 */
package net.hypixel.skyblock.entity.dragon.type;

import net.hypixel.skyblock.entity.dragon.Dragon;
import net.minecraft.server.v1_8_R3.World;
import org.apache.commons.lang3.Range;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

public class UnstableDragon
extends Dragon {
    public UnstableDragon(World world) {
        super(world, 1.4, Range.between(0.6, 0.9), 300L);
    }

    public UnstableDragon() {
        this((World)((CraftWorld)Bukkit.getWorlds().get(0)).getHandle());
    }

    @Override
    public String getEntityName() {
        return "Unstable Dragon";
    }

    @Override
    public double getEntityMaxHealth() {
        return 9000000.0;
    }

    @Override
    public double getDamageDealt() {
        return 1400.0;
    }
}

