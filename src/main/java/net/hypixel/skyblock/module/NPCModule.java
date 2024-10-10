package net.hypixel.skyblock.module;

import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.module.impl.ModuleImpl;
import net.hypixel.skyblock.npc.impl.SkyblockNPC;
import net.hypixel.skyblock.npc.impl.SkyblockNPCManager;
import net.hypixel.skyblock.util.ReflectionUtil;

public class NPCModule implements ModuleImpl {
    @Override
    public String name() {
        return "NPC";
    }

    @Override
    public void onStart() {
        ReflectionUtil.loopThroughPackage("net.hypixel.skyblock.npc" , SkyblockNPC.class).forEach(skyblockNPC -> {});
        SkyBlock.getPlugin().sendMessage("&aSuccessfully loaded &e" + SkyblockNPCManager.getNPCS().size() + "&a NPCs");
    }

    @Override
    public void onStop() {

    }

    @Override
    public boolean shouldStart() {
        return true;
    }
}
