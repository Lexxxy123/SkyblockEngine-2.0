package in.godspunky.skyblock.util;

import lombok.Getter;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import in.godspunky.skyblock.Skyblock;
import in.godspunky.skyblock.user.User;

import java.util.ArrayList;
import java.util.List;

public class PacketEntity {
    Entity entity;
    @Getter
    List<Player> players;
    Packet spawn;
    List<Packet> spawnPackets;
    BukkitTask tickTask;
    String permission;
    static final int showRadius = 50;
    public static List<PacketEntity> managers;
    double x;
    double y;
    double z;

    public PacketEntity(final Entity e) {
        this.entity = e;
        this.players = new ArrayList<Player>();
        this.updateSpawnPacket();
        PacketEntity.managers.add(this);
        this.tickTask = Bukkit.getScheduler().runTaskTimer(Skyblock.getPlugin(), this::tick, 1L, 5L);
    }

    public void setShowPermission(final String permission) {
        this.permission = permission;
    }

    public void addSpawnPacket(final Packet packet) {
        if (this.spawnPackets == null) {
            this.spawnPackets = new ArrayList<>();
        }
        this.spawnPackets.add(packet);
        this.sendCustomPacket(packet);
    }

    public void removeSpawnPacket(final Packet packet) {
        this.spawnPackets.remove(packet);
    }

    public boolean updateLocation() {
        if (this.players.isEmpty()) {
            return false;
        }
        if (this.x == this.entity.locX && this.y == this.entity.locY && this.z == this.entity.locZ) {
            return true;
        }
        this.x = this.entity.locX;
        this.y = this.entity.locY;
        this.z = this.entity.locZ;
        final PacketPlayOutEntityTeleport teleport = new PacketPlayOutEntityTeleport(this.entity);
        for (int i = 0; i < this.players.size(); ++i) {
            final Player next = this.players.get(i);
            ((CraftPlayer) next).getHandle().playerConnection.sendPacket(teleport);
        }
        return true;
    }

    public boolean playAnimation(final int anim) {
        if (this.players.isEmpty()) {
            return false;
        }
        final PacketPlayOutAnimation teleport = new PacketPlayOutAnimation(this.entity, anim);
        for (final Player next : this.players) {
            ((CraftPlayer) next).getHandle().playerConnection.sendPacket(teleport);
        }
        return true;
    }

    public void tick() {
        final List<Player> newPlayers = new ArrayList<Player>();
        this.updateSpawnPacket();
        for (final Player p : Bukkit.getOnlinePlayers()) {
            if (p.getWorld().equals(this.entity.getBukkitEntity().getWorld()) && p.getLocation().distance(this.entity.getBukkitEntity().getLocation()) < 50.0) {
                if (this.players.contains(p)) {
                    if (this.permission != null && !User.getUser(p.getUniqueId()).hasPermission(this.permission)) {
                        continue;
                    }
                    this.players.remove(p);
                    newPlayers.add(p);
                } else {
                    if (!this.sendSpawnPacket(p)) {
                        continue;
                    }
                    newPlayers.add(p);
                }
            }
        }
        for (final Player p : this.players) {
            this.sendDestroyPacket(p);
        }
        this.players = newPlayers;
    }

    public void destroy() {
        this.hide();
        this.tickTask.cancel();
        PacketEntity.managers.remove(this);
    }

    public void hide() {
        for (final Player next : this.players) {
            this.sendDestroyPacket(next);
        }
    }

    protected void updateSpawnPacket() {
        this.spawn = new PacketPlayOutSpawnEntityLiving((EntityLiving) this.entity);
    }

    public void spawn() {
        for (final Player next : this.players) {
            this.sendSpawnPacket(next);
        }
    }

    public void sendCustomPacket(final Packet packet) {
        for (final Player p : this.players) {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        }
    }

    protected boolean sendSpawnPacket(final Player player) {
        if (this.permission != null && !User.getUser(player.getUniqueId()).hasPermission(this.permission)) {
            this.players.remove(player);
            return false;
        }
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(this.spawn);
        if (this.spawnPackets != null) {
            for (final Packet packet : this.spawnPackets) {
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }
        }
        return true;
    }

    protected void sendDestroyPacket(final Player player) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(new int[]{this.entity.getId()}));
    }

    public static void removePlayer(final Player player) {
        for (final PacketEntity manager : PacketEntity.managers) {
            manager.players.remove(player);
        }
    }

    static {
        PacketEntity.managers = new ArrayList<PacketEntity>();
    }
}
