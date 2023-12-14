package vn.giakhanhvn.skysim.listener;

import java.util.HashMap;
import vn.giakhanhvn.skysim.util.SLog;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Entity;
import java.util.Map;

public class NPCUtils
{
    public static final Map<Entity, Integer> HP_CURRENT_NPCMOB;
    public static final Map<Entity, Integer> HP_MAX_NPCMOB;
    public static final Map<Entity, Integer> DAMAGE_NPCMOB;
    
    public static void setNPCHealth(final NPC npc, final int health) {
        if (npc.getEntity() == null) {
            return;
        }
        if (NPCUtils.HP_CURRENT_NPCMOB.containsKey(npc.getEntity())) {
            if (NPCUtils.HP_MAX_NPCMOB.containsKey(npc.getEntity())) {
                if (NPCUtils.HP_MAX_NPCMOB.get(npc.getEntity()) >= NPCUtils.HP_CURRENT_NPCMOB.get(npc.getEntity())) {
                    NPCUtils.HP_CURRENT_NPCMOB.put(npc.getEntity(), health);
                }
                else {
                    SLog.severe("NPC STATS ERROR! You can't set Health > Max Health! Cannot set Health >>> " + npc.toString());
                }
            }
            else {
                SLog.severe("NPC STATS ERROR! Max health is null! Cannot set Health >>> " + npc.toString());
            }
        }
        else {
            SLog.severe("NPC STATS ERROR! Health is null! >>> " + npc.toString());
        }
    }
    
    public static void setNPCMaxHealth(final NPC npc, final int health) {
        if (npc.getEntity() == null) {
            return;
        }
        NPCUtils.HP_MAX_NPCMOB.put(npc.getEntity(), health);
        NPCUtils.HP_CURRENT_NPCMOB.put(npc.getEntity(), health);
    }
    
    public static int getNPCHealth(final NPC npc) {
        if (npc.getEntity() == null) {
            return 0;
        }
        int returnstatement = 0;
        if (NPCUtils.HP_CURRENT_NPCMOB.containsKey(npc.getEntity()) && NPCUtils.HP_MAX_NPCMOB.containsKey(npc.getEntity())) {
            returnstatement = NPCUtils.HP_CURRENT_NPCMOB.get(npc.getEntity());
        }
        return returnstatement;
    }
    
    public static int getNPCMaxHealth(final NPC npc) {
        if (npc.getEntity() == null) {
            return 0;
        }
        int returnstatement = 0;
        if (NPCUtils.HP_CURRENT_NPCMOB.containsKey(npc.getEntity()) && NPCUtils.HP_MAX_NPCMOB.containsKey(npc.getEntity())) {
            returnstatement = NPCUtils.HP_MAX_NPCMOB.get(npc.getEntity());
        }
        return returnstatement;
    }
    
    public static boolean updateNPCHealth(final NPC npc) {
        if (npc.getEntity() == null) {
            return false;
        }
        boolean isSuccess = false;
        if (NPCUtils.HP_CURRENT_NPCMOB.containsKey(npc.getEntity()) && !NPCUtils.HP_MAX_NPCMOB.containsKey(npc.getEntity())) {
            NPCUtils.HP_CURRENT_NPCMOB.remove(npc.getEntity());
            isSuccess = true;
        }
        return isSuccess;
    }
    
    public static boolean isNPCVaild(final NPC npc) {
        boolean isVaild = false;
        if (npc.getEntity() != null) {
            isVaild = true;
        }
        return isVaild;
    }
    
    static {
        HP_CURRENT_NPCMOB = new HashMap<Entity, Integer>();
        HP_MAX_NPCMOB = new HashMap<Entity, Integer>();
        DAMAGE_NPCMOB = new HashMap<Entity, Integer>();
    }
}
