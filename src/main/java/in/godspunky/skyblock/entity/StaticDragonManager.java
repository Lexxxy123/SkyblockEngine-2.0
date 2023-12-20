package in.godspunky.skyblock.entity;

import in.godspunky.skyblock.SkySimEngine;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class StaticDragonManager {
    public static boolean ACTIVE;
    public static Map<UUID, List<Location>> EYES;
    public static SEntity DRAGON;

    public static void endFight() {
        if (StaticDragonManager.DRAGON == null) {
            return;
        }
        StaticDragonManager.ACTIVE = false;
        for (final List<Location> locations : StaticDragonManager.EYES.values()) {
            for (final Location location : locations) {
                final Block b = location.getBlock();
                final BlockState s = b.getState();
                s.setRawData((byte) 0);
                s.update();
                b.removeMetadata("placer", SkySimEngine.getPlugin());
            }
        }
        StaticDragonManager.EYES.clear();
        StaticDragonManager.DRAGON = null;
    }

    static {
        StaticDragonManager.ACTIVE = false;
        StaticDragonManager.EYES = new HashMap<UUID, List<Location>>();
        StaticDragonManager.DRAGON = null;
    }
}
