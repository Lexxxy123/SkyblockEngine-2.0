/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.NBTTagCompound
 */
package net.hypixel.skyblock.item.oddities;

import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.ItemData;
import net.hypixel.skyblock.item.MaterialFunction;
import net.hypixel.skyblock.item.MaterialStatistics;
import net.hypixel.skyblock.item.Rarity;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class TeleporterLauncher
implements MaterialStatistics,
MaterialFunction,
ItemData {
    @Override
    public String getDisplayName() {
        return "Teleporter Launcher";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.EXCLUSIVE;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.BLOCK;
    }

    @Override
    public boolean isEnchanted() {
        return true;
    }

    @Override
    public NBTTagCompound getData() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setFloat("velX", 1.0f);
        compound.setFloat("velY", 1.0f);
        compound.setFloat("velZ", 1.0f);
        compound.setDouble("x", 0.0);
        compound.setDouble("y", 0.0);
        compound.setDouble("z", 0.0);
        compound.setString("world", "world");
        compound.setFloat("yaw", 0.0f);
        compound.setFloat("pitch", 0.0f);
        compound.setLong("delay", 60L);
        return compound;
    }
}

