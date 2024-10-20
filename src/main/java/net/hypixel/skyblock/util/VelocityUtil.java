/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.util.Vector
 */
package net.hypixel.skyblock.util;

import org.bukkit.util.Vector;

public class VelocityUtil {
    public static Vector calculateVelocity(Vector from, Vector to, int heightGain) {
        double gravity = 0.115;
        int endGain = to.getBlockY() - from.getBlockY();
        double horizDist = Math.sqrt(VelocityUtil.distanceSquared(from, to));
        int gain = heightGain;
        double maxGain = gain > endGain + gain ? (double)gain : (double)(endGain + gain);
        double a2 = -horizDist * horizDist / (4.0 * maxGain);
        double b2 = horizDist;
        double c2 = -endGain;
        double slope = -b2 / (2.0 * a2) - Math.sqrt(b2 * b2 - 4.0 * a2 * c2) / (2.0 * a2);
        double vy = Math.sqrt(maxGain * 0.115);
        double vh = vy / slope;
        int dx = to.getBlockX() - from.getBlockX();
        int dz = to.getBlockZ() - from.getBlockZ();
        double mag = Math.sqrt(dx * dx + dz * dz);
        double dirx = (double)dx / mag;
        double dirz = (double)dz / mag;
        double vx = vh * dirx;
        double vz = vh * dirz;
        return new Vector(vx, vy, vz);
    }

    private static double distanceSquared(Vector from, Vector to) {
        double dx = to.getBlockX() - from.getBlockX();
        double dz = to.getBlockZ() - from.getBlockZ();
        return dx * dx + dz * dz;
    }
}

