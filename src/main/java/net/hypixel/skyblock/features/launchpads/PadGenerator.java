/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 */
package net.hypixel.skyblock.features.launchpads;

import org.bukkit.Location;

public class PadGenerator {
    private String start;
    private String end;
    Location startLocation;
    Location endLocation;
    Location teleportLocation;
    private int phase;

    public PadGenerator(String start, String end) {
        this.start = start;
        this.end = end;
        this.phase = 1;
    }

    public String getStart() {
        return this.start;
    }

    public String getEnd() {
        return this.end;
    }

    public Location getStartLocation() {
        return this.startLocation;
    }

    public Location getEndLocation() {
        return this.endLocation;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }

    public void setTeleportLocation(Location teleportLocation) {
        this.teleportLocation = teleportLocation;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public Location getTeleportLocation() {
        return this.teleportLocation;
    }

    public int getPhase() {
        return this.phase;
    }
}

