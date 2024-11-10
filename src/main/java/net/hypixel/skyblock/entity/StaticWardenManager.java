/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockState
 *  org.bukkit.plugin.Plugin
 */
package net.hypixel.skyblock.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.entity.SEntity;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.plugin.Plugin;

public final class StaticWardenManager {
    public static boolean ACTIVE = false;
    public static Map<UUID, List<Location>> EYES = new HashMap<UUID, List<Location>>();
    public static SEntity WARDEN = null;

    public static void endFight() {
        if (WARDEN == null) {
            return;
        }
        ACTIVE = false;
        for (List<Location> locations : EYES.values()) {
            for (Location location : locations) {
                Block b = location.getBlock();
                BlockState s = b.getState();
                s.setRawData((byte)0);
                s.update();
                b.removeMetadata("placer", (Plugin)SkyBlock.getPlugin());
            }
        }
        EYES.clear();
        WARDEN = null;
    }
}

