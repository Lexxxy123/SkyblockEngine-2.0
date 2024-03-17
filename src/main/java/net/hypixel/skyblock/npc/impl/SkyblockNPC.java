package net.hypixel.skyblock.npc.impl;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.hypixel.skyblock.npc.impl.enums.NPCType;
import net.hypixel.skyblock.npc.impl.type.NPCBase;
import net.hypixel.skyblock.npc.impl.type.impl.NPCPlayerImpl;
import net.hypixel.skyblock.npc.impl.type.impl.NPCVillagerImpl;
import net.hypixel.skyblock.user.User;
import net.hypixel.skyblock.util.SUtil;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import net.hypixel.skyblock.SkyBlock;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Getter
public class SkyblockNPC {
    private final Set<UUID> viewers = new HashSet<>();
    private final static Set<UUID> ALREADY_TALKING = new HashSet<>();

    private final List<EntityArmorStand> holograms = new ArrayList<>();
    protected double cosFOV = Math.cos(Math.toRadians(60));

    private final String[] messages;
    private final UUID uuid;

    private final String name;
    private final NPCType type;

    private final World world;
    private final Location location;

    private final NPCSkin skin;
    private final NPCBase npcBase;

    private final GameProfile gameProfile;
    private final NPCParameters parameters;


    public SkyblockNPC(NPCParameters npcParameters){
        this.uuid = UUID.randomUUID();
        this.name = npcParameters.name();
        this.type = npcParameters.type();
        this.world = Bukkit.getWorld(npcParameters.world());
        this.messages = npcParameters.messages();
        if (world == null) {
            throw new NullPointerException("World cannot be null for npc : " + name);
        }
        this.location = new Location(world, npcParameters.x() , npcParameters.y() , npcParameters.z() , npcParameters.yaw() , npcParameters.pitch());
        this.skin = npcParameters.skin();
        this.gameProfile = new GameProfile(uuid , name);

        if (type == NPCType.VILLAGER){
            this.npcBase = new NPCVillagerImpl(
                    location
            );
        } else {
            if (skin != null) {
                if (skin.getTexture() != null && skin.getSignature() != null) {
                    gameProfile.getProperties().put(
                            "textures",
                            new Property("textures",
                                    skin.getTexture(),
                                    skin.getSignature()
                            )
                    );
                }
            }
            this.npcBase = new NPCPlayerImpl(
                    location,
                    gameProfile
            );
        }

        npcBase.setLocation(location);
        this.parameters = npcParameters;
        SkyblockNPCManager.registerNPC(this);

    }
    public void showTo(Player player){
        if (viewers.contains(player.getUniqueId())) return;
        npcBase.show(player);
        npcBase.hideNameTag(player);

        sendHologram(player , getParameters().holograms());
        this.viewers.add(player.getUniqueId());

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline() || !viewers.contains(player.getUniqueId())) {
                    cancel();
                    return;
                }
                if (isPlayerNearby(player)) {
                    sendHeadRotationPacket(player);
                }
            }
        }.runTaskTimerAsynchronously(SkyBlock.getPlugin(), 0, 2);

    }

    public void hideFrom(Player player) {
        if (!viewers.contains(player.getUniqueId())) return;
        viewers.remove(player.getUniqueId());
        npcBase.hide(player);
        removeHolograms(player);
    }

    public void sendHeadRotationPacket(Player player) {
        npcBase.sendRotation(player);

    }

    public int getEntityID(){
        return npcBase.entityId();
    }

    public boolean isPlayerNearby(Player player) {
        Location npcLocation = getLocation();
        Location playerLocation = player.getLocation();
        double distanceSquared = npcLocation.distanceSquared(playerLocation);
        return distanceSquared <= 35.0;
    }
    public boolean isShown(Player player){
        return viewers.contains(player.getUniqueId());
    }
    // #inRangeOf method is used from npc-lib because mine method is not that good and I am not good at math
    public boolean inRangeOf(Player player) {
        if (player == null) return false;
        if (!player.getWorld().equals(location.getWorld())) {
            // No need to continue our checks, they are in different worlds.
            return false;
        }

        // If Bukkit doesn't track the NPC entity anymore, bypass the hiding distance variable.
        // This will cause issues otherwise (e.g. custom skin disappearing).
        double hideDistance = 20;
        double distanceSquared = player.getLocation().distanceSquared(location);
        double bukkitRange = Bukkit.getViewDistance() << 4;

        return distanceSquared <= SUtil.square(hideDistance) && distanceSquared <= SUtil.square(bukkitRange);
    }

    public static void sendPacket(Player player, Packet<?> packet) {
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }

    public boolean canSpeak(User user){
        if (messages == null) return false;
        if (messages.length == 0) return false;
        if (ALREADY_TALKING.contains(user.getUuid())) return false;
        return !user.getTalkedNPCs().contains(getName());
    }

    public void sendHologram(Player player, String[] lines) {
        double yOffset = 0.0;
        double DELTA = 0.3;

        for (String text : lines) {
            EntityArmorStand armorStand = new EntityArmorStand(((CraftPlayer) player).getHandle().getWorld());
            Location hologramLocation = getLocation().clone().add(0, yOffset, 0);
            armorStand.setLocation(hologramLocation.getX(), hologramLocation.getY(), hologramLocation.getZ(), 0, 0);
            armorStand.setCustomName(ChatColor.translateAlternateColorCodes('&' , text));
            armorStand.setCustomNameVisible(true);
            armorStand.setInvisible(true);

            PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(armorStand);
            sendPacket(player , packet);

            holograms.add(armorStand);
            yOffset -= DELTA;
        }
    }


    public void removeHolograms(Player player) {
        for (EntityArmorStand armorStand : holograms) {
            PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(armorStand.getId());
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
        holograms.clear();
    }

    public CompletableFuture<Void> speak(Player player) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        if (ALREADY_TALKING.contains(player.getUniqueId())) {
            future.complete(null);
            return future;
        }

        ALREADY_TALKING.add(player.getUniqueId());
        int i = 0;
        for (String message : messages) {
            SUtil.delay(() -> sendMessage(player, message), i * 20L);
            i++;
        }

        SUtil.delay(() -> {
            ALREADY_TALKING.remove(player.getUniqueId());
            User.getUser(player.getUniqueId()).addTalkedNPC(name);
            future.complete(null);
        }, messages.length * 20L);

        return future;
    }


    public void sendMessage(Player player , String message){
        player.sendMessage(ChatColor.YELLOW + "[NPC] " + name + ChatColor.WHITE + ": " + message);
    }

}