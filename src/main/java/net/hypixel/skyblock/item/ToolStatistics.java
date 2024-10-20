/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.NBTTagCompound
 */
package net.hypixel.skyblock.item;

import net.hypixel.skyblock.item.Enchantable;
import net.hypixel.skyblock.item.GenericItemType;
import net.hypixel.skyblock.item.PlayerBoostStatistics;
import net.hypixel.skyblock.item.Rarity;
import net.hypixel.skyblock.item.Reforgable;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

public interface ToolStatistics
extends PlayerBoostStatistics,
Enchantable,
Reforgable {
    @Override
    public String getDisplayName();

    @Override
    public Rarity getRarity();

    @Override
    public GenericItemType getType();

    @Override
    default public boolean isStackable() {
        return false;
    }

    @Override
    default public NBTTagCompound getData() {
        return new NBTTagCompound();
    }
}

