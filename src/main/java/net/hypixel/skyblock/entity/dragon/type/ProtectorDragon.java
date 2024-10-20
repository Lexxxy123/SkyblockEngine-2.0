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
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

public class ProtectorDragon
extends Dragon {
    public ProtectorDragon(World world) {
        super(world, 1.4, DEFAULT_DAMAGE_DEGREE_RANGE, 300L);
    }

    public ProtectorDragon() {
        this((World)((CraftWorld)Bukkit.getWorlds().get(0)).getHandle());
    }

    @Override
    public String getEntityName() {
        return "Protector Dragon";
    }

    @Override
    public double getEntityMaxHealth() {
        return 9000000.0;
    }

    @Override
    public double getDamageDealt() {
        return 1300.0;
    }
}

