package in.godspunky.skyblock.entity.dungeons.boss.sadan;

import in.godspunky.skyblock.SkyBlock;
import in.godspunky.skyblock.util.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import in.godspunky.skyblock.entity.SEntity;
import in.godspunky.skyblock.entity.SEntityType;

import java.util.ArrayList;

public class SadanBossManager {
    public static void startFloor(ArrayList<Player> plist) {
        String worldname = "f6_" + SadanFunction.generateRandom();
        final World source = Bukkit.getWorld("f6");
        final SkyBlockWorldManager skyBlockWorldManager = new SkyBlockWorldManager(source);
        skyBlockWorldManager.cloneWorld(worldname);
        World world = Bukkit.getWorld(worldname);
        for (Player tm : plist) {
            tm.teleport(new Location(world, 213.0, 71.0, 221.0, 0.0f, 0.0f));
        }
        Sputnik.RunThisSession.put(Bukkit.getServer(), Sputnik.rf_() + 1);
        SUtil.delay(() -> r(plist, world), 1L);
        SUtil.delay(() -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rg flag __global__ -w " + world.getName() + " build deny"), 1L);
        SUtil.delay(() -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rg flag __global__ -w " + world.getName() + " pvp deny"), 2L);
        SUtil.delay(() -> new SEntity(new Location(world, 183.0, 100.0, 251.0), SEntityType.SADAN), 1L);
    }

    public static void r(ArrayList<Player> plist, World world) {
        for (Player tm : plist) {
            tm.teleport(new Location(world, 191.5, 69.0, 199.5, 0.0f, 0.0f));
        }
    }

    public static void endFloor(World w) {
        if (w.getName().toLowerCase().startsWith("f6") && !w.getName().equalsIgnoreCase("f6")) {
            for (Entity e : w.getEntities()) {
                if (e instanceof Player) {
                    continue;
                }
                e.remove();
            }
            new SkyBlockWorldManager(w).delete();
            SLog.severe("[DUNGEON BOSSROOM] Deleted " + w.getName() + " and cleaned the memory !");
        }
    }
}
