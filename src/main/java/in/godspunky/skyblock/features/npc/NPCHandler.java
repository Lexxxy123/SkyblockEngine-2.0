package in.godspunky.skyblock.features.npc;


import in.godspunky.skyblock.Skyblock;
import org.bukkit.Bukkit;

import java.util.HashMap;

public class NPCHandler {

    private final HashMap<String, NPC> npcs;

    public NPCHandler() {
        this.npcs = new HashMap<>();
    }

    public void spawnAll() {
        for (NPC npc : npcs.values()) {
            npc.spawn();
        }
    }

    public void registerNPC(String id, NPC npc) {
        Bukkit.getPluginManager().registerEvents(npc, Skyblock.getPlugin());

        this.npcs.put(id, npc);
    }

    public HashMap<String, NPC> getNPCs() {
        return this.npcs;
    }

}
