package vn.giakhanhvn.skysim;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class DungeonHashMap {
    public static final Map<Player, Boolean> IsPlayingDungeon;
    public static final Map<World, Integer> DungeonScore;
    public static final Map<Player, Double> ExperienceEarned;
    public static final Map<Player, Integer> DeathCount;
    public static final Map<Player, Boolean> IsAGhost;
    public static final Map<World, Integer> SecretFound;
    public static final Map<World, Integer> MaximumSecrets;
    public static final Map<World, Integer> WorldRespawnSecond;
    public static final Map<Player, Integer> RespawnSecondLeft;
    public static final Map<Player, Boolean> PlayerReady;
    public static final Map<Player, Boolean> PlayerBeingRevived;
    public static final Map<Player, Boolean> AutoRevived;
    public static final Map<Player, ItemStack[]> PlayerInventory;
    public static final Map<Player, ItemStack[]> PlayerArmorInventory;

    public static boolean isDead(final Player player) {
        return DungeonHashMap.IsAGhost.containsKey(player) && DungeonHashMap.IsAGhost.get(player);
    }

    public static String GetDungeonScore(final World world) {
        if (world == null) {
            return null;
        }
        if (!world.getName().equals("dungeon")) {
            return null;
        }
        if (!DungeonHashMap.DungeonScore.containsKey(world)) {
            DungeonHashMap.DungeonScore.put(world, 0);
        }
        final int dgs = DungeonHashMap.DungeonScore.get(world);
        if (dgs < 0) {
            DungeonHashMap.DungeonScore.put(world, 0);
        } else {
            if (dgs < 100 && dgs > 0) {
                return "D";
            }
            if (dgs > 100 && dgs <= 159) {
                return "C";
            }
            if (dgs > 159 && dgs <= 229) {
                return "B";
            }
            if (dgs > 229 && dgs <= 269) {
                return "A";
            }
            if (dgs > 269 && dgs <= 299) {
                return "S";
            }
            if (dgs >= 300) {
                return "S+";
            }
        }
        return null;
    }

    static {
        IsPlayingDungeon = new HashMap<Player, Boolean>();
        DungeonScore = new HashMap<World, Integer>();
        ExperienceEarned = new HashMap<Player, Double>();
        DeathCount = new HashMap<Player, Integer>();
        IsAGhost = new HashMap<Player, Boolean>();
        SecretFound = new HashMap<World, Integer>();
        MaximumSecrets = new HashMap<World, Integer>();
        WorldRespawnSecond = new HashMap<World, Integer>();
        RespawnSecondLeft = new HashMap<Player, Integer>();
        PlayerReady = new HashMap<Player, Boolean>();
        PlayerBeingRevived = new HashMap<Player, Boolean>();
        AutoRevived = new HashMap<Player, Boolean>();
        PlayerInventory = new HashMap<Player, ItemStack[]>();
        PlayerArmorInventory = new HashMap<Player, ItemStack[]>();
    }
}
