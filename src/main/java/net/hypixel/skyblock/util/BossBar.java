/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.Entity
 *  net.minecraft.server.v1_8_R3.EntityLiving
 *  net.minecraft.server.v1_8_R3.EntityWither
 *  net.minecraft.server.v1_8_R3.Packet
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport
 *  net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving
 *  net.minecraft.server.v1_8_R3.World
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.craftbukkit.v1_8_R3.CraftWorld
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package net.hypixel.skyblock.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.hypixel.skyblock.SkyBlock;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityWither;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class BossBar
extends BukkitRunnable {
    private String title;
    private final HashMap<UUID, EntityWither> withers = new HashMap();

    public BossBar(String title) {
        this.title = title;
        this.runTaskTimer((Plugin)SkyBlock.getPlugin(), 0L, 1L);
    }

    public void addPlayer(UUID playerId) {
        Player player = Bukkit.getPlayer((UUID)playerId);
        if (player != null && player.isOnline()) {
            EntityWither wither = new EntityWither((World)((CraftWorld)player.getWorld()).getHandle());
            Location location = this.getWitherLocation(player.getLocation());
            wither.setCustomName(this.title);
            wither.setInvisible(true);
            wither.r(877);
            wither.setLocation(location.getX(), location.getY(), location.getZ(), 0.0f, 0.0f);
            PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving((EntityLiving)wither);
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)packet);
            this.withers.put(playerId, wither);
        }
    }

    public void removePlayer(UUID playerId) {
        EntityWither wither = this.withers.remove(playerId);
        if (wither != null) {
            PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[]{wither.getId()});
            Player player = Bukkit.getPlayer((UUID)playerId);
            if (player != null && player.isOnline()) {
                ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)packet);
            }
        }
    }

    public void setTitle(String title) {
        this.title = title;
        for (Map.Entry<UUID, EntityWither> entry : this.withers.entrySet()) {
            EntityWither wither = entry.getValue();
            wither.setCustomName(title);
            PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(wither.getId(), wither.getDataWatcher(), true);
            Player player = Bukkit.getPlayer((UUID)entry.getKey());
            if (player == null || !player.isOnline()) continue;
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)packet);
        }
    }

    public void setProgress(double progress) {
        for (Map.Entry<UUID, EntityWither> entry : this.withers.entrySet()) {
            EntityWither wither = entry.getValue();
            wither.setHealth((float)(progress * (double)wither.getMaxHealth()));
            PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(wither.getId(), wither.getDataWatcher(), true);
            Player player = Bukkit.getPlayer((UUID)entry.getKey());
            if (player == null || !player.isOnline()) continue;
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)packet);
        }
    }

    public Location getWitherLocation(Location location) {
        return location.add(location.getDirection().multiply(20));
    }

    public void run() {
        for (Map.Entry<UUID, EntityWither> entry : this.withers.entrySet()) {
            EntityWither wither = entry.getValue();
            Player player = Bukkit.getPlayer((UUID)entry.getKey());
            if (player == null || !player.isOnline()) continue;
            Location location = this.getWitherLocation(player.getLocation());
            wither.setLocation(location.getX(), location.getY(), location.getZ(), 0.0f, 0.0f);
            PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport((Entity)wither);
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)packet);
        }
    }
}

