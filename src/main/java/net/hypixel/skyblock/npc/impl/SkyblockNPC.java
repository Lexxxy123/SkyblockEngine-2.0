/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.properties.Property
 *  net.minecraft.server.v1_8_R3.EntityArmorStand
 *  net.minecraft.server.v1_8_R3.EntityLiving
 *  net.minecraft.server.v1_8_R3.Packet
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy
 *  net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package net.hypixel.skyblock.npc.impl;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.npc.impl.NPCParameters;
import net.hypixel.skyblock.npc.impl.NPCSkin;
import net.hypixel.skyblock.npc.impl.SkyblockNPCManager;
import net.hypixel.skyblock.npc.impl.enums.NPCType;
import net.hypixel.skyblock.npc.impl.type.NPCBase;
import net.hypixel.skyblock.npc.impl.type.impl.NPCPlayerImpl;
import net.hypixel.skyblock.npc.impl.type.impl.NPCVillagerImpl;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SkyblockNPC {
    private final Set<UUID> viewers = new HashSet<UUID>();
    private static final Set<UUID> ALREADY_TALKING = new HashSet<UUID>();
    private final List<EntityArmorStand> holograms = new ArrayList<EntityArmorStand>();
    protected double cosFOV = Math.cos(Math.toRadians(60.0));
    private final String[] messages;
    private final UUID uuid = UUID.randomUUID();
    private final String name;
    private final NPCType type;
    private final World world;
    private final Location location;
    private final NPCSkin skin;
    private final NPCBase npcBase;
    private final GameProfile gameProfile;
    private final NPCParameters parameters;

    public SkyblockNPC(NPCParameters npcParameters) {
        this.name = npcParameters.name();
        this.type = npcParameters.type();
        this.world = Bukkit.getWorld((String)npcParameters.world());
        this.messages = npcParameters.messages();
        if (this.world == null) {
            throw new NullPointerException("World cannot be null for npc : " + this.name);
        }
        this.location = new Location(this.world, npcParameters.x(), npcParameters.y(), npcParameters.z(), npcParameters.yaw(), npcParameters.pitch());
        this.skin = npcParameters.skin();
        this.gameProfile = new GameProfile(this.uuid, this.name);
        if (this.type == NPCType.VILLAGER) {
            this.npcBase = new NPCVillagerImpl(this.location);
        } else {
            if (this.skin != null && this.skin.getTexture() != null && this.skin.getSignature() != null) {
                this.gameProfile.getProperties().put((Object)"textures", (Object)new Property("textures", this.skin.getTexture(), this.skin.getSignature()));
            }
            this.npcBase = new NPCPlayerImpl(this.location, this.gameProfile);
        }
        this.npcBase.setLocation(this.location);
        this.parameters = npcParameters;
        SkyblockNPCManager.registerNPC(this);
    }

    public void showTo(final Player player) {
        if (this.viewers.contains(player.getUniqueId())) {
            return;
        }
        this.npcBase.show(player);
        this.npcBase.hideNameTag(player);
        this.sendHologram(player, this.getParameters().holograms());
        this.viewers.add(player.getUniqueId());
        new BukkitRunnable(){

            public void run() {
                if (!player.isOnline() || !SkyblockNPC.this.viewers.contains(player.getUniqueId())) {
                    this.cancel();
                    return;
                }
                if (SkyblockNPC.this.isPlayerNearby(player)) {
                    SkyblockNPC.this.sendHeadRotationPacket(player);
                }
            }
        }.runTaskTimerAsynchronously((Plugin)SkyBlock.getPlugin(), 0L, 2L);
    }

    public void hideFrom(Player player) {
        if (!this.viewers.contains(player.getUniqueId())) {
            return;
        }
        this.viewers.remove(player.getUniqueId());
        this.npcBase.hide(player);
        this.removeHolograms(player);
    }

    public void sendHeadRotationPacket(Player player) {
        this.npcBase.sendRotation(player);
    }

    public int getEntityID() {
        return this.npcBase.entityId();
    }

    public boolean isPlayerNearby(Player player) {
        Location playerLocation;
        Location npcLocation = this.getLocation();
        double distanceSquared = npcLocation.distanceSquared(playerLocation = player.getLocation());
        return distanceSquared <= 35.0;
    }

    public boolean isShown(Player player) {
        return this.viewers.contains(player.getUniqueId());
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

    public static void sendPacket(Player player, Packet<?> packet) {
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }

    public boolean canSpeak(User user) {
        if (this.messages == null) {
            return false;
        }
        if (this.messages.length == 0) {
            return false;
        }
        if (ALREADY_TALKING.contains(user.getUuid())) {
            return false;
        }
        return !user.getTalkedNPCs().contains(this.getName());
    }

    public void sendHologram(Player player, String[] lines) {
        double yOffset = 0.0;
        double DELTA = 0.3;
        for (String text : lines) {
            EntityArmorStand armorStand = new EntityArmorStand(((CraftPlayer)player).getHandle().getWorld());
            Location hologramLocation = this.getLocation().clone().add(0.0, yOffset, 0.0);
            armorStand.setLocation(hologramLocation.getX(), hologramLocation.getY(), hologramLocation.getZ(), 0.0f, 0.0f);
            armorStand.setCustomName(ChatColor.translateAlternateColorCodes((char)'&', (String)text));
            armorStand.setCustomNameVisible(true);
            armorStand.setInvisible(true);
            PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving((EntityLiving)armorStand);
            SkyblockNPC.sendPacket(player, packet);
            this.holograms.add(armorStand);
            yOffset -= DELTA;
        }
    }

    public void removeHolograms(Player player) {
        for (EntityArmorStand armorStand : this.holograms) {
            PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[]{armorStand.getId()});
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)packet);
        }
        this.holograms.clear();
    }

    public CompletableFuture<Void> speak(Player player) {
        CompletableFuture<Void> future = new CompletableFuture<Void>();
        if (ALREADY_TALKING.contains(player.getUniqueId())) {
            future.complete(null);
            return future;
        }
        ALREADY_TALKING.add(player.getUniqueId());
        int i = 0;
        for (String message : this.messages) {
            SUtil.delay(() -> this.sendMessage(player, message), (long)i * 20L);
            ++i;
        }
        SUtil.delay(() -> {
            ALREADY_TALKING.remove(player.getUniqueId());
            if (Objects.equals(this.name, "Jerry")) {
                return;
            }
            User.getUser(player.getUniqueId()).addTalkedNPC(this.name);
            future.complete(null);
        }, (long)this.messages.length * 20L);
        return future;
    }

    public void sendMessage(Player player, String message) {
        player.sendMessage(ChatColor.YELLOW + "[NPC] " + this.name + ChatColor.WHITE + ": " + message);
    }

    public Set<UUID> getViewers() {
        return this.viewers;
    }

    public List<EntityArmorStand> getHolograms() {
        return this.holograms;
    }

    public double getCosFOV() {
        return this.cosFOV;
    }

    public String[] getMessages() {
        return this.messages;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public NPCType getType() {
        return this.type;
    }

    public World getWorld() {
        return this.world;
    }

    public Location getLocation() {
        return this.location;
    }

    public NPCSkin getSkin() {
        return this.skin;
    }

    public NPCBase getNpcBase() {
        return this.npcBase;
    }

    public GameProfile getGameProfile() {
        return this.gameProfile;
    }

    public NPCParameters getParameters() {
        return this.parameters;
    }
}

