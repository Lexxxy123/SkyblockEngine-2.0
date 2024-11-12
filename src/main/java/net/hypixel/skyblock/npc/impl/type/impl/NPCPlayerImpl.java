/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.server.v1_8_R3.DataWatcher
 *  net.minecraft.server.v1_8_R3.Entity
 *  net.minecraft.server.v1_8_R3.EntityHuman
 *  net.minecraft.server.v1_8_R3.EntityPlayer
 *  net.minecraft.server.v1_8_R3.Packet
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntity$PacketPlayOutEntityLook
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata
 *  net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn
 *  net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo
 *  net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo$EnumPlayerInfoAction
 *  net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam
 *  net.minecraft.server.v1_8_R3.PlayerInteractManager
 *  net.minecraft.server.v1_8_R3.Scoreboard
 *  net.minecraft.server.v1_8_R3.ScoreboardTeam
 *  net.minecraft.server.v1_8_R3.ScoreboardTeamBase$EnumNameTagVisibility
 *  net.minecraft.server.v1_8_R3.World
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.craftbukkit.v1_8_R3.CraftServer
 *  org.bukkit.craftbukkit.v1_8_R3.CraftWorld
 *  org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard
 *  org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboardManager
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.npc.impl.type.impl;

import com.mojang.authlib.GameProfile;
import java.util.Collections;
import net.hypixel.skyblock.npc.impl.type.NPCBase;
import net.hypixel.skyblock.util.SUtil;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.Scoreboard;
import net.minecraft.server.v1_8_R3.ScoreboardTeam;
import net.minecraft.server.v1_8_R3.ScoreboardTeamBase;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboardManager;
import org.bukkit.entity.Player;

public class NPCPlayerImpl
extends EntityPlayer
implements NPCBase {
    private final Location location;
    private final String name;

    public NPCPlayerImpl(Location location, GameProfile gameProfile) {
        super(((CraftServer)Bukkit.getServer()).getServer(), ((CraftWorld)location.getWorld()).getHandle(), gameProfile, new PlayerInteractManager((World)((CraftWorld)location.getWorld()).getHandle()));
        this.name = gameProfile.getName();
        this.location = location;
    }

    @Override
    public void show(Player player) {
        PacketPlayOutPlayerInfo infoAddPacket = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, new EntityPlayer[]{this});
        PacketPlayOutNamedEntitySpawn spawnPacket = new PacketPlayOutNamedEntitySpawn((EntityHuman)this);
        PacketPlayOutPlayerInfo infoRemovePacket = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, new EntityPlayer[]{this});
        this.sendPacket(player, (Packet)infoAddPacket);
        this.sendPacket(player, (Packet)spawnPacket);
        this.sendPacket(player, (Packet)infoRemovePacket);
    }

    @Override
    public void hide(Player player) {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[]{this.getId()});
        this.sendPacket(player, (Packet)packet);
    }

    @Override
    public void hideNameTag(Player player) {
        CraftScoreboardManager scoreboardManager = ((CraftServer)Bukkit.getServer()).getScoreboardManager();
        CraftScoreboard craftScoreboard = scoreboardManager.getMainScoreboard();
        Scoreboard scoreboard = craftScoreboard.getHandle();
        ScoreboardTeam scoreboardTeam = scoreboard.getTeam(this.name);
        if (scoreboardTeam == null) {
            scoreboardTeam = new ScoreboardTeam(scoreboard, this.name);
        }
        scoreboardTeam.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);
        scoreboardTeam.setPrefix("[NPC] ");
        this.sendPacket(player, (Packet)new PacketPlayOutScoreboardTeam(scoreboardTeam, 1));
        this.sendPacket(player, (Packet)new PacketPlayOutScoreboardTeam(scoreboardTeam, 0));
        this.sendPacket(player, (Packet)new PacketPlayOutScoreboardTeam(scoreboardTeam, Collections.singletonList(this.name), 3));
        SUtil.delay(() -> this.fixSkinHelmetLayerForPlayer(player), 8L);
    }

    public void fixSkinHelmetLayerForPlayer(Player player) {
        DataWatcher watcher = this.getDataWatcher();
        watcher.watch(10, (Object)127);
        PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(this.getId(), watcher, true);
        this.sendPacket(player, (Packet)metadata);
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
        return this.getBukkitEntity().getEntityId();
    }
}

