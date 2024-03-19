package net.hypixel.skyblock.api.hologram;

import java.util.HashSet;
import java.util.Set;

public class HologramManager {
    private static final Set<Hologram> HOLOGRAMS = new HashSet<>();

    public static void register(Hologram hologram){
        HOLOGRAMS.add(hologram);
    }

    public static void remove(Hologram hologram){
        HOLOGRAMS.remove(hologram);
    }

    public static Set<Hologram> getHolograms() {
        return HOLOGRAMS;
    }
}
