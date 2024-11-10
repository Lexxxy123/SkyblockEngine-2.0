/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.npc.impl;

import java.util.HashSet;
import java.util.Set;
import net.hypixel.skyblock.npc.impl.SkyblockNPC;

public class SkyblockNPCManager {
    private static final Set<SkyblockNPC> SKYBLOCK_NPCS = new HashSet<SkyblockNPC>();

    public static void registerNPC(SkyblockNPC skyblockNPC) {
        SKYBLOCK_NPCS.add(skyblockNPC);
    }

    public static Set<SkyblockNPC> getNPCS() {
        return SKYBLOCK_NPCS;
    }
}

