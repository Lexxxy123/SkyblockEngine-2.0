package vn.giakhanhvn.skysim.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboardManager;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import vn.giakhanhvn.skysim.SkySimEngine;
import vn.giakhanhvn.skysim.user.User;
import vn.giakhanhvn.skysim.util.SUtil;

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
    private final World world;
    private final Location location;
    private final String texture;
    private final String signature;
    private final EntityPlayer entityPlayer;
    private final GameProfile gameProfile;
    private final NPCParameters parameters;


    public SkyblockNPC(NPCParameters npc){
        this.uuid = UUID.randomUUID();
        this.name = npc.name();
        this.world = Bukkit.getWorld(npc.world());
        this.messages = npc.messages();
        if (world == null) {
            throw new NullPointerException("World cannot be null for npc : " + name);
        }
        this.location = new Location(world, npc.x() , npc.y() , npc.z() , npc.yaw() , npc.pitch());
        this.texture = npc.texture();
        this.signature = npc.signature();
        MinecraftServer minecraftServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
        this.gameProfile = new GameProfile(uuid , name);
        // skin
        if (texture != null && signature != null) {
            gameProfile.getProperties().put(
                    "textures",
                    new Property("textures",
                            texture,
                            signature
                    )
            );
        }
        PlayerInteractManager interactManager = new PlayerInteractManager(worldServer);
        this.entityPlayer = new EntityPlayer(
                minecraftServer,
                worldServer,
                gameProfile,
                interactManager
        );
        entityPlayer.setLocation(
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
        );
        this.parameters = npc;
        SkyblockNPCManager.registerNPC(this);

    }
    public void showTo(Player player){
        if (viewers.contains(player.getUniqueId())) return;
        PacketPlayOutPlayerInfo packetPlayOutPlayerInfo = new PacketPlayOutPlayerInfo(
                PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER,
                entityPlayer
        );
        PacketPlayOutNamedEntitySpawn packetPlayOutNamedEntitySpawn = new PacketPlayOutNamedEntitySpawn(
                entityPlayer
        );
        sendPacket(player , packetPlayOutPlayerInfo);
        sendPacket(player , packetPlayOutNamedEntitySpawn);

        CraftScoreboardManager scoreboardManager = ((CraftServer) Bukkit.getServer()).getScoreboardManager();
        CraftScoreboard craftScoreboard = scoreboardManager.getMainScoreboard();
        Scoreboard scoreboard = craftScoreboard.getHandle();

        ScoreboardTeam scoreboardTeam = scoreboard.getTeam(name);
        if (scoreboardTeam == null){
            scoreboardTeam = new ScoreboardTeam(scoreboard , name);
        }
        scoreboardTeam.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.NEVER);
        scoreboardTeam.setPrefix("[NPC] ");

        sendPacket(player, new PacketPlayOutScoreboardTeam(scoreboardTeam, 1));
        sendPacket(player, new PacketPlayOutScoreboardTeam(scoreboardTeam, 0));
        sendPacket(player, new PacketPlayOutScoreboardTeam(scoreboardTeam, Collections.singletonList(name), 3));
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
        }.runTaskTimerAsynchronously(SkySimEngine.getPlugin(), 0, 2);

    }

    public void hideFrom(Player player) {
        if (!viewers.contains(player.getUniqueId())) return;
        viewers.remove(player.getUniqueId());
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entityPlayer.getId());
        sendPacket(player, packet);
        removeHolograms(player);
    }

    public void sendHeadRotationPacket(Player player) {
        Location original = getLocation();
        Location location = original.clone().setDirection(player.getLocation().subtract(original.clone()).toVector());

        byte yaw = (byte) (location.getYaw() * 256 / 360);
        byte pitch = (byte) (location.getPitch() * 256 / 360);

        PacketPlayOutEntityHeadRotation headRotationPacket = new PacketPlayOutEntityHeadRotation(
                this.entityPlayer,
                yaw
        );
        sendPacket(player, headRotationPacket);

        PacketPlayOutEntity.PacketPlayOutEntityLook lookPacket = new PacketPlayOutEntity.PacketPlayOutEntityLook(
                getId(),
                yaw,
                pitch,
                false
        );
        sendPacket(player, lookPacket);
    }

    private int getId(){
        return entityPlayer.getId();
    }
    public int getEntityID(){
        return entityPlayer.getBukkitEntity().getEntityId();
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
    // #inRangeOf method is used from npc-lib because mine method is not that good, and I am not good at math
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
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);

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