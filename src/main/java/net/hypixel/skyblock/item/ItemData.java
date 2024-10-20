/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.NBTTagCompound
 */
package net.hypixel.skyblock.item;

import java.util.List;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

public interface ItemData {
    public NBTTagCompound getData();

    default public List<String> getDataLore(String key, Object value) {
        return null;
    }
}

