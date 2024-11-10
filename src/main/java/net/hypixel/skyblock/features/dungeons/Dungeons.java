/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.features.dungeons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SEntityType;
import net.hypixel.skyblock.util.SLog;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Dungeons {
    private static Map<World, Dungeons> DUNGEONS_CACHE = new HashMap<World, Dungeons>();
    public List<Player> players = new ArrayList<Player>();
    public int witherKeys = 0;
    public boolean bloodKey = false;
    public boolean isBloodCleared = false;
    public List<String> openedRooms;
    public int deaths = 0;
    public int cryptsBlown = 0;
    public int score = 0;
    private final World world;
    private final UUID runUUID;

    public Dungeons(World world, List<Player> listPlayers) {
        this.world = world;
        this.players = listPlayers;
        this.runUUID = UUID.randomUUID();
        DUNGEONS_CACHE.put(world, this);
    }

    public static Dungeons getDungeonsInstance(World w) {
        if (DUNGEONS_CACHE.containsKey(w)) {
            return DUNGEONS_CACHE.get(w);
        }
        SLog.severe("Cannot find dungeons instance for this world.");
        return null;
    }

    public void spawnMobAt(Location loc, SEntityType type, boolean starred, UUID roomUUID) {
        SEntity sentity = new SEntity(loc, type, new Object[0]);
        sentity.setStarred(starred);
    }

    public void spawnMobWithCustomStats(Location loc, SEntityType type, boolean starred, double HP, double damage) {
    }

    public void spawnKeyAt() {
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public void setWitherKeys(int witherKeys) {
        this.witherKeys = witherKeys;
    }

    public int getWitherKeys() {
        return this.witherKeys;
    }

    public void setBloodKey(boolean bloodKey) {
        this.bloodKey = bloodKey;
    }

    public boolean isBloodKey() {
        return this.bloodKey;
    }

    public void setBloodCleared(boolean isBloodCleared) {
        this.isBloodCleared = isBloodCleared;
    }

    public boolean isBloodCleared() {
        return this.isBloodCleared;
    }

    public void setOpenedRooms(List<String> openedRooms) {
        this.openedRooms = openedRooms;
    }

    public List<String> getOpenedRooms() {
        return this.openedRooms;
    }

    public int getDeaths() {
        return this.deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getCryptsBlown() {
        return this.cryptsBlown;
    }

    public void setCryptsBlown(int cryptsBlown) {
        this.cryptsBlown = cryptsBlown;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

