/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.NBTTagCompound
 */
package net.hypixel.skyblock.item;

import net.hypixel.skyblock.item.ItemData;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

public interface Ownable
extends ItemData {
    @Override
    default public NBTTagCompound getData() {
        return new NBTTagCompound();
    }
}

