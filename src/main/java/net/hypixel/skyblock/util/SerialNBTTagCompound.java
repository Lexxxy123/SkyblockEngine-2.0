/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.NBTBase
 *  net.minecraft.server.v1_8_R3.NBTTagCompound
 *  org.bukkit.configuration.serialization.ConfigurationSerializable
 */
package net.hypixel.skyblock.util;

import java.util.HashMap;
import java.util.Map;
import net.hypixel.skyblock.util.SUtil;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class SerialNBTTagCompound
extends NBTTagCompound
implements ConfigurationSerializable {
    public SerialNBTTagCompound() {
    }

    public SerialNBTTagCompound(NBTTagCompound compound) {
        for (String k : compound.c()) {
            this.set(k, compound.get(k));
        }
    }

    public Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        for (String k : this.c()) {
            NBTBase b = this.get(k);
            if (b instanceof NBTTagCompound) {
                SerialNBTTagCompound serial = new SerialNBTTagCompound((NBTTagCompound)b);
                for (Map.Entry<String, Object> entry : serial.serialize().entrySet()) {
                    map.put(k + "." + entry.getKey(), entry.getValue());
                }
                continue;
            }
            map.put(k, SUtil.getObjectFromCompound(this, k));
        }
        return map;
    }

    public static SerialNBTTagCompound deserialize(Map<String, Object> map) {
        SerialNBTTagCompound compound = new SerialNBTTagCompound();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            String[] dir = entry.getKey().split("\\.");
            if (dir.length >= 2) {
                key = dir[dir.length - 1];
                SerialNBTTagCompound track = compound;
                for (int i = 0; i < dir.length - 1; ++i) {
                    if (!track.hasKey(dir[i])) {
                        track.set(dir[i], (NBTBase)new NBTTagCompound());
                    }
                    track = track.getCompound(dir[i]);
                }
                track.set(key, SUtil.getBaseFromObject(entry.getValue()));
                continue;
            }
            compound.set(key, SUtil.getBaseFromObject(entry.getValue()));
        }
        return compound;
    }
}

