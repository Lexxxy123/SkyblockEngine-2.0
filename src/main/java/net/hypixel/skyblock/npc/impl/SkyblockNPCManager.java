package net.hypixel.skyblock.npc.impl;

import net.hypixel.skyblock.npc.impl.SkyblockNPC;

import java.util.HashSet;
import java.util.Set;

public class SkyblockNPCManager {
    private static final Set<SkyblockNPC> SKYBLOCK_NPCS = new HashSet<>();

    public static void registerNPC(SkyblockNPC skyblockNPC){
        SKYBLOCK_NPCS.add(skyblockNPC);
    }

    public static Set<SkyblockNPC> getNPCS(){
        return SKYBLOCK_NPCS;
    }

}
