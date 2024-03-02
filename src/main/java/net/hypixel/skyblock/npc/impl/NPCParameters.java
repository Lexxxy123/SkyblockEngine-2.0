package net.hypixel.skyblock.npc.impl;

import net.hypixel.skyblock.npc.impl.enums.NPCType;
import org.bukkit.entity.Player;

public interface NPCParameters {
    String name();
    default NPCType type(){
        return NPCType.PLAYER;
    }
   default String[] messages(){
        return new String[]{};
   }
    String[] holograms();
    default NPCSkin skin(){
        return null;
    }
    String world();
    double x();
    double y();
    double z();
   default float yaw(){
       return 0;
   }
   default float pitch(){
       return 0;
   }

   default boolean looking(){
       return true;
   }
    void onInteract(Player player , SkyblockNPC npc);
}