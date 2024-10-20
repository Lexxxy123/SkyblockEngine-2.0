/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.api.hologram;

import java.util.HashSet;
import java.util.Set;
import net.hypixel.skyblock.api.hologram.Hologram;

public class HologramManager {
    private static final Set<Hologram> HOLOGRAMS = new HashSet<Hologram>();

    public static void register(Hologram hologram) {
        HOLOGRAMS.add(hologram);
    }

    public static void remove(Hologram hologram) {
        HOLOGRAMS.remove((Object)hologram);
    }

    public static Set<Hologram> getHolograms() {
        return HOLOGRAMS;
    }
}

