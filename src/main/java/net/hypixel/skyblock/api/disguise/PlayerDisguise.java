/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.properties.Property
 *  net.minecraft.server.v1_8_R3.AttributeInstance
 *  net.minecraft.server.v1_8_R3.Entity
 *  net.minecraft.server.v1_8_R3.EntityHuman
 *  net.minecraft.server.v1_8_R3.EntityPlayer
 *  net.minecraft.server.v1_8_R3.GenericAttributes
 *  net.minecraft.server.v1_8_R3.IAttribute
 *  net.minecraft.server.v1_8_R3.MathHelper
 *  net.minecraft.server.v1_8_R3.Packet
 *  net.minecraft.server.v1_8_R3.PacketPlayInEntityAction
 *  net.minecraft.server.v1_8_R3.PacketPlayInUseEntity
 *  net.minecraft.server.v1_8_R3.PacketPlayOutAnimation
 *  net.minecraft.server.v1_8_R3.PacketPlayOutAttachEntity
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntity$PacketPlayOutEntityLook
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntity$PacketPlayOutRelEntityMove
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntity$PacketPlayOutRelEntityMoveLook
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntityEffect
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntityStatus
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport
 *  net.minecraft.server.v1_8_R3.PacketPlayOutEntityVelocity
 *  net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn
 *  net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo
 *  net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo$EnumPlayerInfoAction
 *  net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam
 *  net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntity
 *  net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving
 *  net.minecraft.server.v1_8_R3.PlayerInteractManager
 *  net.minecraft.server.v1_8_R3.Scoreboard
 *  net.minecraft.server.v1_8_R3.ScoreboardTeam
 *  net.minecraft.server.v1_8_R3.ScoreboardTeamBase$EnumNameTagVisibility
 *  net.minecraft.server.v1_8_R3.World
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.craftbukkit.v1_8_R3.CraftServer
 *  org.bukkit.craftbukkit.v1_8_R3.CraftWorld
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity
 *  org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard
 *  org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboardManager
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 */
package net.hypixel.skyblock.api.disguise;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import net.hypixel.skyblock.SkyBlock;
import net.hypixel.skyblock.api.disguise.utils.ReflectionUtils;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;
import net.minecraft.server.v1_8_R3.AttributeInstance;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.IAttribute;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInEntityAction;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEffect;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityStatus;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityVelocity;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.Scoreboard;
import net.minecraft.server.v1_8_R3.ScoreboardTeam;
import net.minecraft.server.v1_8_R3.ScoreboardTeamBase;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboardManager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class PlayerDisguise {
    public static final HashMap<Integer, PlayerDisguise> nonFake = new HashMap();
    public static final HashMap<Integer, PlayerDisguise> fake = new HashMap();
    private final HashSet<User> shown = new HashSet();
    private final LivingEntity entity;
    private final EntityPlayer fakePlayer;
    private Location l;
    private final BukkitRunnable runnable;
    private static final HashMap<Class<? extends Packet<?>>, String> idFieldsOut = new HashMap();
    private static final HashMap<Class<? extends Packet<?>>, String> idFieldsIn = new HashMap();

    public PlayerDisguise(LivingEntity entity, String texture, String value) {
        this(entity, new Property("textures", texture, value));
    }

    public PlayerDisguise(final LivingEntity entity, Property skin) {
        this.l = entity.getLocation();
        this.entity = entity;
        GameProfile profile = new GameProfile(UUID.randomUUID(), "\u00a7\u00a7\u00a7\u00a7\u00a79\u00a79\u00a79\u00a79");
        new BukkitRunnable(){
            double maxH = 0.0;

            public void run() {
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                if (this.maxH != PlayerDisguise.this.getIAttribute(entity, GenericAttributes.maxHealth).getValue()) {
                    PlayerDisguise.this.getIAttribute((LivingEntity)PlayerDisguise.this.fakePlayer.getBukkitEntity(), GenericAttributes.maxHealth).setValue(PlayerDisguise.this.getIAttribute(entity, GenericAttributes.maxHealth).getValue());
                }
                this.maxH = PlayerDisguise.this.getIAttribute(entity, GenericAttributes.maxHealth).getValue();
                PlayerDisguise.this.fakePlayer.setHealth((float)entity.getHealth());
            }
        }.runTaskTimer((Plugin)SkyBlock.getPlugin(), 1L, 1L);
        if (skin != null) {
            profile.getProperties().put((Object)"textures", (Object)skin);
        }
        this.fakePlayer = new EntityPlayer(((CraftServer)Bukkit.getServer()).getServer(), ((CraftWorld)entity.getWorld()).getHandle(), profile, new PlayerInteractManager((World)((CraftWorld)entity.getWorld()).getHandle()));
        this.fakePlayer.setPosition(entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ());
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.check(User.getUser(player));
        }
        nonFake.put(entity.getEntityId(), this);
        fake.put(this.fakePlayer.getId(), this);
        this.runnable = new BukkitRunnable(){
            int i = 0;

            public void run() {
                ++this.i;
                if (PlayerDisguise.this.l.equals((Object)entity.getLocation())) {
                    return;
                }
                Location location = entity.getLocation();
                PacketPlayOutEntityTeleport packet = null;
                if (entity.getLocation().distance(PlayerDisguise.this.l) > 5.0 || this.i >= 100) {
                    System.out.println("sending entity teleport packet!");
                    packet = new PacketPlayOutEntityTeleport(PlayerDisguise.this.fakePlayer.getId(), MathHelper.floor((double)(location.getX() * 32.0)), MathHelper.floor((double)(location.getY() * 32.0)), MathHelper.floor((double)(location.getZ() * 32.0)), (byte)(location.getYaw() * 256.0f / 360.0f), (byte)(location.getPitch() * 256.0f / 360.0f), entity.isOnGround());
                }
                if (packet != null) {
                    PlayerDisguise.this.sendPacket(packet);
                }
                PlayerDisguise.this.l = entity.getLocation();
            }
        };
        this.runnable.runTaskTimer((Plugin)SkyBlock.getPlugin(), 1L, 1L);
    }

    public boolean onPacketOut(Packet<?> packet) {
        if (idFieldsOut.containsKey(packet.getClass())) {
            ReflectionUtils.setField(idFieldsOut.get(packet.getClass()), packet, (Object)this.fakePlayer.getId());
        }
        if (packet instanceof PacketPlayOutEntity.PacketPlayOutEntityLook || packet instanceof PacketPlayOutEntity.PacketPlayOutRelEntityMove || packet instanceof PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook) {
            return false;
        }
        if (packet instanceof PacketPlayOutEntityStatus || packet instanceof PacketPlayOutEntityMetadata) {
            this.getIAttribute((LivingEntity)this.fakePlayer.getBukkitEntity(), GenericAttributes.maxHealth).setValue(this.getIAttribute(this.entity, GenericAttributes.maxHealth).getValue());
            this.fakePlayer.setHealth((float)this.entity.getHealth());
        }
        return true;
    }

    public void onPacketIn(Packet<?> packet) {
        if (idFieldsIn.containsKey(packet.getClass())) {
            ReflectionUtils.setField(idFieldsIn.get(packet.getClass()), packet, (Object)this.entity.getEntityId());
        }
    }

    public void check(User player) {
        if (player == null) {
            return;
        }
        if (!this.shown.contains(player)) {
            if (this.inRangeOf(player.toBukkitPlayer(), this.entity.getLocation())) {
                this.shown.add(player);
                player.sendPacket((Packet<?>)new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, new EntityPlayer[]{this.fakePlayer}));
                player.sendPacket((Packet<?>)new PacketPlayOutNamedEntitySpawn((EntityHuman)this.fakePlayer));
                Location location = this.entity.getLocation();
                PacketPlayOutEntityTeleport entityTeleportPacket = new PacketPlayOutEntityTeleport(this.fakePlayer.getId(), MathHelper.floor((double)(location.getX() * 32.0)), MathHelper.floor((double)(location.getY() * 32.0)), MathHelper.floor((double)(location.getZ() * 32.0)), (byte)(location.getYaw() * 256.0f / 360.0f), (byte)(location.getPitch() * 256.0f / 360.0f), this.entity.isOnGround());
                player.sendPacket((Packet<?>)entityTeleportPacket);
                player.sendPacket((Packet<?>)new PacketPlayOutEntityDestroy(new int[]{this.entity.getEntityId()}));
                String s2 = "99n" + this.fakePlayer.getUniqueID().toString().substring(1, 5);
                CraftScoreboardManager scoreboardManager = ((CraftServer)Bukkit.getServer()).getScoreboardManager();
                CraftScoreboard craftScoreboard = scoreboardManager.getMainScoreboard();
                Scoreboard scoreboard = craftScoreboard.getHandle();
                ScoreboardTeam scoreboardTeam = scoreboard.getTeam(s2);
                if (scoreboardTeam == null) {
                    scoreboardTeam = new ScoreboardTeam(scoreboard, s2);
                }
                scoreboardTeam.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);
                scoreboardTeam.setPrefix("[NPC] ");
                player.sendPacket((Packet<?>)new PacketPlayOutScoreboardTeam(scoreboardTeam, 1));
                player.sendPacket((Packet<?>)new PacketPlayOutScoreboardTeam(scoreboardTeam, 0));
                player.sendPacket((Packet<?>)new PacketPlayOutScoreboardTeam(scoreboardTeam, Collections.singletonList(s2), 3));
            }
        } else if (!this.inRangeOf(player.toBukkitPlayer(), this.entity.getLocation())) {
            this.shown.remove(player);
        }
    }

    private void sendPacket(Packet<?> packet) {
        if (this.shown.isEmpty()) {
            return;
        }
        for (User player : this.shown) {
            if (player == null) continue;
            player.sendPacket(packet);
        }
    }

    public static void packetInManager(Packet<?> packet) {
        if (!idFieldsIn.containsKey(packet.getClass())) {
            return;
        }
        int id = (Integer)ReflectionUtils.getField(ReflectionUtils.findField(packet.getClass(), idFieldsIn.get(packet.getClass())), packet);
        if (fake.containsKey(id)) {
            fake.get(id).onPacketIn(packet);
        }
    }

    public boolean inRangeOf(Player player, Location location) {
        if (player == null) {
            return false;
        }
        if (!player.getWorld().equals(location.getWorld())) {
            return false;
        }
        double hideDistance = 40.0;
        double distanceSquared = player.getLocation().distanceSquared(location);
        double bukkitRange = Bukkit.getViewDistance() << 4;
        return distanceSquared <= SUtil.square(hideDistance) && distanceSquared <= SUtil.square(bukkitRange);
    }

    public static boolean packetOutManager(Packet<?> packet) {
        if ((packet instanceof PacketPlayOutSpawnEntityLiving || packet instanceof PacketPlayOutSpawnEntity || packet instanceof PacketPlayOutNamedEntitySpawn) && nonFake.containsKey((int)((Integer)ReflectionUtils.getField(ReflectionUtils.findField(packet.getClass(), idFieldsOut.get(packet.getClass())), packet)))) {
            return false;
        }
        if (!idFieldsOut.containsKey(packet.getClass())) {
            return true;
        }
        int id = (Integer)ReflectionUtils.getField(ReflectionUtils.findField(packet.getClass(), idFieldsOut.get(packet.getClass())), packet);
        if (nonFake.containsKey(id)) {
            return nonFake.get(id).onPacketOut(packet);
        }
        return true;
    }

    public void kill(User player) {
        if (player != null) {
            Vector vec = this.entity.getLocation().toVector().subtract(player.toBukkitPlayer().getLocation().toVector()).normalize().setY(0.3);
            PacketPlayOutEntityVelocity p2 = new PacketPlayOutEntityVelocity(this.fakePlayer.getId(), vec.getX(), vec.getY(), vec.getZ());
            this.sendPacket((Packet<?>)p2);
        }
        Bukkit.getScheduler().runTaskLater((Plugin)SkyBlock.getPlugin(), () -> {
            this.entity.setHealth(0.0);
            this.fakePlayer.setHealth(0.0f);
            this.sendPacket((Packet<?>)new PacketPlayOutEntityStatus((Entity)this.fakePlayer, 60));
        }, 1L);
        this.runnable.cancel();
        Bukkit.getScheduler().runTaskLater((Plugin)SkyBlock.getPlugin(), () -> {
            this.sendPacket((Packet<?>)new PacketPlayOutEntityDestroy(new int[]{this.fakePlayer.getId()}));
            nonFake.remove(this.entity.getEntityId());
            fake.remove(this.fakePlayer.getId());
            this.entity.remove();
        }, 20L);
    }

    public AttributeInstance getIAttribute(LivingEntity entity, IAttribute iAttribute) {
        return ((CraftLivingEntity)entity).getHandle().getAttributeInstance(iAttribute);
    }

    public void status(byte b2) {
        this.sendPacket((Packet<?>)new PacketPlayOutEntityStatus((Entity)this.fakePlayer, b2));
    }

    public EntityPlayer getFakePlayer() {
        return this.fakePlayer;
    }

    static {
        idFieldsOut.put(PacketPlayOutEntity.PacketPlayOutEntityLook.class, "a");
        idFieldsOut.put(PacketPlayOutEntity.PacketPlayOutRelEntityMove.class, "a");
        idFieldsOut.put(PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook.class, "a");
        idFieldsOut.put(PacketPlayOutEntityStatus.class, "a");
        idFieldsOut.put(PacketPlayOutEntityHeadRotation.class, "a");
        idFieldsOut.put(PacketPlayOutAttachEntity.class, "a");
        idFieldsOut.put(PacketPlayOutEntityVelocity.class, "a");
        idFieldsOut.put(PacketPlayOutEntityEffect.class, "a");
        idFieldsOut.put(PacketPlayOutEntityEquipment.class, "a");
        idFieldsOut.put(PacketPlayOutAnimation.class, "a");
        idFieldsOut.put(PacketPlayOutEntityMetadata.class, "a");
        idFieldsOut.put(PacketPlayOutNamedEntitySpawn.class, "a");
        idFieldsOut.put(PacketPlayOutSpawnEntityLiving.class, "a");
        idFieldsOut.put(PacketPlayOutSpawnEntity.class, "a");
        idFieldsIn.put(PacketPlayInEntityAction.class, "a");
        idFieldsIn.put(PacketPlayInUseEntity.class, "a");
    }
}

