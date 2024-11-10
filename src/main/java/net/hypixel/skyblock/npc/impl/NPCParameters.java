/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.npc.impl;

import net.hypixel.skyblock.npc.impl.NPCSkin;
import net.hypixel.skyblock.npc.impl.SkyblockNPC;
import net.hypixel.skyblock.npc.impl.enums.NPCType;
import org.bukkit.entity.Player;

public interface NPCParameters {
    public static final String CLICK = "&e&lCLICK";

    public String name();

    default public NPCType type() {
        return NPCType.PLAYER;
    }

    default public String[] messages() {
        return null;
    }

    public String[] holograms();

    default public NPCSkin skin() {
        return null;
    }

    public String world();

    public double x();

    public double y();

    public double z();

    default public float yaw() {
        return 0.0f;
    }

    default public float pitch() {
        return 0.0f;
    }

    default public boolean looking() {
        return true;
    }

    public void onInteract(Player var1, SkyblockNPC var2);
}

