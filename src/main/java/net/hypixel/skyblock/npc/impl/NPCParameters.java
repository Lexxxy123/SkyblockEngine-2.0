/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.npc.impl;

import net.hypixel.skyblock.npc.impl.NPCSkin;
import net.hypixel.skyblock.npc.impl.SkyBlockNPC;
import net.hypixel.skyblock.npc.impl.enums.NPCType;
import org.bukkit.entity.Player;

public interface NPCParameters {
    public static final String CLICK = "&e&lCLICK";

    public String id();

    default public String name() {
        return null;
    }

    default public NPCType type() {
        return NPCType.PLAYER;
    }

    default public String[] messages() {
        return null;
    }

    default public String[] holograms() {
        String[] stringArray;
        if (this.name() != null) {
            String[] stringArray2 = new String[2];
            stringArray2[0] = this.name();
            stringArray = stringArray2;
            stringArray2[1] = CLICK;
        } else {
            stringArray = null;
        }
        return stringArray;
    }

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

    public void onInteract(Player var1, SkyBlockNPC var2);
}

