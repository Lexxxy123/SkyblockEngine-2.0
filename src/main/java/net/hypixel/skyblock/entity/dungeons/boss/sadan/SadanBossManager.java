/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.entity.dungeons.boss.sadan;

import java.util.ArrayList;
import java.util.UUID;
import net.hypixel.skyblock.api.worldmanager.SkyBlockWorldManager;
import net.hypixel.skyblock.entity.SEntity;
import net.hypixel.skyblock.entity.SEntityType;
import net.hypixel.skyblock.entity.dungeons.boss.sadan.SadanFunction;
import net.hypixel.skyblock.util.SLog;
import net.hypixel.skyblock.util.SUtil;
import net.hypixel.skyblock.util.Sputnik;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class SadanBossManager {
    public static void startFloor(ArrayList<UUID> plist) {
        String worldname = "f6_" + SadanFunction.generateRandom();
        World source = Bukkit.getWorld((String)"f6");
        SkyBlockWorldManager skyBlockWorldManager = new SkyBlockWorldManager(source);
        skyBlockWorldManager.cloneWorld(worldname);
        World world = Bukkit.getWorld((String)worldname);
        for (UUID tm : plist) {
            Bukkit.getPlayer((UUID)tm).teleport(new Location(world, 213.0, 71.0, 221.0, 0.0f, 0.0f));
        }
        Sputnik.RunThisSession.put(Bukkit.getServer(), Sputnik.rf_() + 1);
        SUtil.delay(() -> SadanBossManager.r(plist, world), 1L);
        SUtil.delay(() -> new SEntity(new Location(world, 183.0, 100.0, 251.0), SEntityType.SADAN, new Object[0]), 1L);
    }

    public static void r(ArrayList<UUID> plist, World world) {
        for (UUID tm : plist) {
            Bukkit.getPlayer((UUID)tm).teleport(new Location(world, 191.5, 69.0, 199.5, 0.0f, 0.0f));
        }
    }

    public static void endFloor(World w2) {
        if (w2.getName().toLowerCase().startsWith("f6") && !w2.getName().equalsIgnoreCase("f6")) {
            for (Entity e2 : w2.getEntities()) {
                if (e2 instanceof Player) continue;
                e2.remove();
            }
            new SkyBlockWorldManager(w2).delete();
            SLog.severe("[DUNGEON BOSSROOM] Deleted " + w2.getName() + " and cleaned the memory !");
        }
    }
}

