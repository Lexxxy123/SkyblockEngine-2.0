/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.EntityArmorStand
 *  net.minecraft.server.v1_8_R3.EntityLiving
 *  net.minecraft.server.v1_8_R3.MathHelper
 *  net.minecraft.server.v1_8_R3.Packet
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport
 *  net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving
 *  net.minecraft.server.v1_8_R3.World
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.craftbukkit.v1_8_R3.CraftWorld
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.api.hologram;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.hypixel.skyblock.api.hologram.HologramManager;
import net.hypixel.skyblock.api.reflection.ReflectionsUtils;
import net.hypixel.skyblock.util.SUtil;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Hologram
extends EntityArmorStand {
    private final Set<UUID> viewers;
    public Location location;

    public Hologram(Location location) {
        super((World)((CraftWorld)location.getWorld()).getHandle());
        this.setPosition(location.getX(), location.getY(), location.getZ());
        this.setInvisible(true);
        this.setCustomNameVisible(true);
        this.setGravity(false);
        ((ArmorStand)this.getBukkitEntity()).setMarker(true);
        this.setSmall(false);
        this.setBasePlate(false);
        this.location = location;
        this.viewers = new HashSet<UUID>();
        HologramManager.register(this);
    }

    public void setText(String text) {
        if (this.getCustomName().equals(text)) {
            return;
        }
        this.setCustomName(text);
        this.setCustomNameVisible(true);
        this.update();
    }

    public void mount(Entity attachEntity) {
        this.mount(((CraftEntity)attachEntity).getHandle());
        this.update();
    }

    public void update() {
        PacketPlayOutEntityMetadata updatePacket = new PacketPlayOutEntityMetadata(this.getId(), this.getDataWatcher(), true);
        this.sendPacketToViewers((Packet<?>)updatePacket);
    }

    public void show(Player player) {
        if (this.viewers.contains(player.getUniqueId())) {
            return;
        }
        PacketPlayOutSpawnEntityLiving spawnPacket = new PacketPlayOutSpawnEntityLiving((EntityLiving)this);
        ReflectionsUtils.setValue(spawnPacket, "b", 30);
        this.sendPacket(player, (Packet<?>)spawnPacket);
        this.viewers.add(player.getUniqueId());
    }

    public boolean isShown(Player player) {
        return this.viewers.contains(player.getUniqueId());
    }

    public void hide(Player player) {
        if (!this.viewers.contains(player.getUniqueId())) {
            return;
        }
        PacketPlayOutEntityDestroy entityDestroyPacket = new PacketPlayOutEntityDestroy(new int[]{this.getId()});
        this.sendPacket(player, (Packet<?>)entityDestroyPacket);
        this.viewers.remove(player.getUniqueId());
    }

    public void teleport(Location location) {
        this.location = location;
        if (location.getX() == this.locX && location.getY() == this.locY && location.getZ() == this.locZ) {
            return;
        }
        PacketPlayOutEntityTeleport entityTeleportPacket = new PacketPlayOutEntityTeleport(this.getId(), MathHelper.floor((double)(location.getX() * 32.0)), MathHelper.floor((double)(location.getY() * 32.0)), MathHelper.floor((double)(location.getZ() * 32.0)), (byte)(location.getYaw() * 256.0f / 360.0f), (byte)(location.getPitch() * 256.0f / 360.0f), this.onGround);
        this.sendPacketToViewers((Packet<?>)entityTeleportPacket);
    }

    public boolean inRangeOf(Player player) {
        if (player == null) {
            return false;
        }
        if (!player.getWorld().equals(this.location.getWorld())) {
            return false;
        }
        double hideDistance = 20.0;
        double distanceSquared = player.getLocation().distanceSquared(this.location);
        double bukkitRange = Bukkit.getViewDistance() << 4;
        return distanceSquared <= SUtil.square(hideDistance) && distanceSquared <= SUtil.square(bukkitRange);
    }

    public void remove() {
        PacketPlayOutEntityDestroy entityDestroyPacket = new PacketPlayOutEntityDestroy(new int[]{this.getId()});
        this.sendPacketToViewers((Packet<?>)entityDestroyPacket);
        HologramManager.remove(this);
    }

    public void sendPacketToViewers(Packet<?> packet) {
        if (this.viewers.isEmpty()) {
            return;
        }
        this.viewers.forEach(uuid -> {
            Player player = Bukkit.getPlayer((UUID)uuid);
            if (player != null && player.isOnline()) {
                this.sendPacket(player, packet);
            }
        });
    }

    public void sendPacket(Player player, Packet<?> packet) {
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }
}

