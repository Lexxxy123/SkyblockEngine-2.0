package vn.giakhanhvn.skysim.util;

import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class SerialNBTTagCompound extends NBTTagCompound implements ConfigurationSerializable {
    public SerialNBTTagCompound() {
    }

    public SerialNBTTagCompound(final NBTTagCompound compound) {
        for (final String k : compound.c()) {
            this.set(k, compound.get(k));
        }
    }

    public Map<String, Object> serialize() {
        final Map<String, Object> map = new HashMap<String, Object>();
        for (final String k : this.c()) {
            final NBTBase b = this.get(k);
            if (b instanceof NBTTagCompound) {
                final SerialNBTTagCompound serial = new SerialNBTTagCompound((NBTTagCompound) b);
                for (final Map.Entry<String, Object> entry : serial.serialize().entrySet()) {
                    map.put(k + "." + entry.getKey(), entry.getValue());
                }
            } else {
                map.put(k, SUtil.getObjectFromCompound(this, k));
            }
        }
        return map;
    }

    public static SerialNBTTagCompound deserialize(final Map<String, Object> map) {
        final SerialNBTTagCompound compound = new SerialNBTTagCompound();
        for (final Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            final String[] dir = entry.getKey().split("\\.");
            if (dir.length >= 2) {
                key = dir[dir.length - 1];
                NBTTagCompound track = compound;
                for (int i = 0; i < dir.length - 1; ++i) {
                    if (!track.hasKey(dir[i])) {
                        track.set(dir[i], new NBTTagCompound());
                    }
                    track = track.getCompound(dir[i]);
                }
                track.set(key, SUtil.getBaseFromObject(entry.getValue()));
            } else {
                compound.set(key, SUtil.getBaseFromObject(entry.getValue()));
            }
        }
        return compound;
    }
}
