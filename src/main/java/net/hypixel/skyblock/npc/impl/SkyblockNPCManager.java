/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.npc.impl;

import java.util.HashSet;
import java.util.Set;
import net.hypixel.skyblock.npc.impl.SkyBlockNPC;

public class SkyblockNPCManager {
    private static final Set<SkyBlockNPC> SKYBLOCK_NPCS = new HashSet<SkyBlockNPC>();

    public static void registerNPC(SkyBlockNPC skyblockNPC) {
        SKYBLOCK_NPCS.add(skyblockNPC);
    }

    public static Set<SkyBlockNPC> getNPCS() {
        return SKYBLOCK_NPCS;
    }
}

