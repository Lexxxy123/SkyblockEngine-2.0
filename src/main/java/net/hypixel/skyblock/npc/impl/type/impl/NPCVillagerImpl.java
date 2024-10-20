/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.Entity
 *  net.minecraft.server.v1_8_R3.EntityLiving
 *  net.minecraft.server.v1_8_R3.EntityVillager
 *  net.minecraft.server.v1_8_R3.Packet
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntity$PacketPlayOutEntityLook
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation
 *  net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving
 *  net.minecraft.server.v1_8_R3.World
 *  org.bukkit.Location
 *  org.bukkit.craftbukkit.v1_8_R3.CraftWorld
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftVillager
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.npc.impl.type.impl;

import net.hypixel.skyblock.api.reflection.ReflectionsUtils;
import net.hypixel.skyblock.npc.impl.type.NPCBase;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityVillager;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftVillager;
import org.bukkit.entity.Player;

public class NPCVillagerImpl
extends EntityVillager
implements NPCBase {
    private final Location location;

    public NPCVillagerImpl(Location location) {
        super((World)((CraftWorld)location.getWorld()).getHandle());
        this.location = location;
        this.setPosition(location.getX(), location.getY(), location.getZ());
        ((CraftVillager)this.getBukkitEntity()).getHandle().setInvisible(false);
    }

    @Override
    public void hide(Player player) {
        PacketPlayOutEntityDestroy destroyPacket = new PacketPlayOutEntityDestroy(new int[]{this.getId()});
        this.sendPacket(player, (Packet)destroyPacket);
    }

    @Override
    public void show(Player player) {
        PacketPlayOutSpawnEntityLiving spawnPacket = new PacketPlayOutSpawnEntityLiving((EntityLiving)this);
        ReflectionsUtils.setValue(spawnPacket, "b", 120);
        this.sendPacket(player, (Packet)spawnPacket);
    }

    @Override
    public void sendRotation(Player player) {
        Location original = this.location;
        Location location = original.clone().setDirection(player.getLocation().subtract(original.clone()).toVector());
        byte yaw = (byte)(location.getYaw() * 256.0f / 360.0f);
        byte pitch = (byte)(location.getPitch() * 256.0f / 360.0f);
        PacketPlayOutEntityHeadRotation headRotationPacket = new PacketPlayOutEntityHeadRotation((Entity)this, yaw);
        this.sendPacket(player, (Packet)headRotationPacket);
        PacketPlayOutEntity.PacketPlayOutEntityLook lookPacket = new PacketPlayOutEntity.PacketPlayOutEntityLook(this.getId(), yaw, pitch, false);
        this.sendPacket(player, (Packet)lookPacket);
    }

    @Override
    public void setLocation(Location location) {
        this.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    @Override
    public int entityId() {
        return this.bukkitEntity.getEntityId();
    }
}

