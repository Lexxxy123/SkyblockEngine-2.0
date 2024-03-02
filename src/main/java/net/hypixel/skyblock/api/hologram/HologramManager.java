package net.hypixel.skyblock.api.hologram;

import java.util.ArrayList;
import java.util.List;

public class HologramManager {
    private static final List<Hologram> HOLOGRAMS = new ArrayList<>();

    public static void register(Hologram hologram){
        if (!HOLOGRAMS.contains(hologram))
            HOLOGRAMS.add(hologram);
    }

    public static void remove(Hologram hologram){
        HOLOGRAMS.remove(hologram);
    }

    public static List<Hologram> getHolograms() {
        return HOLOGRAMS;
    }
}
