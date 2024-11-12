/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.features.dungeons;

import java.util.ArrayList;
import java.util.UUID;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Dungeon {
    private final UUID uuid;
    private final World world;
    private ArrayList<Player> dungeonsmate = new ArrayList();
    private int deaths;
    private int totalsecretfound;
    private int score;
    private int percentagecomplete;
    private boolean isBloodCleared;
    private boolean bloodkey;
    private int witherkeys;

    public Dungeon(UUID uuid, World world, ArrayList<Player> dungeonmembers) {
        this.world = world;
        this.deaths = 0;
        this.uuid = uuid;
        this.score = 0;
        this.totalsecretfound = 0;
        this.percentagecomplete = 0;
        this.dungeonsmate = dungeonmembers;
        this.bloodkey = false;
        this.witherkeys = 0;
    }

    public int getScore() {
        return this.score;
    }

    public World getOperatingWorld() {
        return this.world;
    }

    public void setScore(int sc) {
        this.score = sc;
    }

    public UUID getRunUUID() {
        return this.uuid;
    }

    public boolean getBloodKey() {
        return this.bloodkey;
    }

    public void setBloodKey(boolean bk2) {
        this.bloodkey = bk2;
    }

    public int getDungeonCompletePercent() {
        return this.percentagecomplete;
    }

    public void setBloodKey(int percent) {
        this.percentagecomplete = percent;
    }

    public int getAllSecrets() {
        return this.totalsecretfound;
    }

    public void setSecretAmount(int tsf) {
        this.totalsecretfound = tsf;
    }

    public int getWitherKeys() {
        return this.witherkeys;
    }

    public void setWithersKey(int wk) {
        this.witherkeys = wk;
    }

    public int getDeaths() {
        return this.deaths;
    }

    public void setDeaths(int d2) {
        this.deaths = d2;
    }

    public ArrayList<Player> getAllDungeonsMembers() {
        return this.dungeonsmate;
    }

    public void addPlayer(Player p2) {
        this.dungeonsmate.add(p2);
    }

    public boolean removePlayer(Player p2) {
        boolean success = false;
        if (this.dungeonsmate.contains(p2)) {
            this.dungeonsmate.remove(p2);
            success = true;
        }
        return success;
    }

    public boolean isBloodCleared() {
        return this.isBloodCleared;
    }
}

