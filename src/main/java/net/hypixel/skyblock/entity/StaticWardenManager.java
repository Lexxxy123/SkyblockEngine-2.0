package net.hypixel.skyblock.entity;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import net.hypixel.skyblock.SkyBlock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class StaticWardenManager {
    public static boolean ACTIVE;
    public static Map<UUID, List<Location>> EYES;
    public static SEntity WARDEN;

    public static void endFight() {
        if (StaticWardenManager.WARDEN == null) {
            return;
        }
        StaticWardenManager.ACTIVE = false;
        for (final List<Location> locations : StaticWardenManager.EYES.values()) {
            for (final Location location : locations) {
                final Block b = location.getBlock();
                final BlockState s = b.getState();
                s.setRawData((byte) 0);
                s.update();
                b.removeMetadata("placer", SkyBlock.getPlugin());
            }
        }
        StaticWardenManager.EYES.clear();
        StaticWardenManager.WARDEN = null;
    }

    static {
        StaticWardenManager.ACTIVE = false;
        StaticWardenManager.EYES = new HashMap<UUID, List<Location>>();
        StaticWardenManager.WARDEN = null;
    }
}
