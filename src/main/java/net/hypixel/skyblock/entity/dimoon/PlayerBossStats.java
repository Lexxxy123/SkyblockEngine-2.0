package net.hypixel.skyblock.entity.dimoon;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerBossStats {
    private static Map<UUID, PlayerBossStats> CACHE;
    public int catalystsPlaced;
    public int damagePlace;

    public PlayerBossStats(final UUID uuid) {
        PlayerBossStats.CACHE.put(uuid, this);
    }

    public static PlayerBossStats get(final UUID u) {
        if (PlayerBossStats.CACHE.containsKey(u)) {
            return PlayerBossStats.CACHE.get(u);
        }
        return null;
    }

    static {
        PlayerBossStats.CACHE = new HashMap<UUID, PlayerBossStats>();
    }
}
