package net.hypixel.skyblock.util;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import net.hypixel.skyblock.SkyBlock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BossBar extends BukkitRunnable {
    private String title;
    private final HashMap<UUID, EntityWither> withers;

    public BossBar(final String title) {
        this.withers = new HashMap<>();
        this.title = title;
        this.runTaskTimer(SkyBlock.getPlugin(), 0L, 1L);
    }

    public void addPlayer(final UUID playerId) {
        Player player = Bukkit.getPlayer(playerId);
        if (player != null && player.isOnline()) {
            EntityWither wither = new EntityWither(((CraftWorld) player.getWorld()).getHandle());
            Location location = getWitherLocation(player.getLocation());
            wither.setCustomName(this.title);
            wither.setInvisible(true);
            wither.r(877);
            wither.setLocation(location.getX(), location.getY(), location.getZ(), 0.0f, 0.0f);
            PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(wither);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            this.withers.put(playerId, wither);
        }
    }

    public void removePlayer(final UUID playerId) {
        EntityWither wither = this.withers.remove(playerId);
        if (wither != null) {
            PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(wither.getId());
            Player player = Bukkit.getPlayer(playerId);
            if (player != null && player.isOnline()) {
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }
        }
    }

    public void setTitle(final String title) {
        this.title = title;
        for (Map.Entry<UUID, EntityWither> entry : this.withers.entrySet()) {
            EntityWither wither = entry.getValue();
            wither.setCustomName(title);
            PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(wither.getId(), wither.getDataWatcher(), true);
            Player player = Bukkit.getPlayer(entry.getKey());
            if (player != null && player.isOnline()) {
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }
        }
    }

    public void setProgress(final double progress) {
        for (Map.Entry<UUID, EntityWither> entry : this.withers.entrySet()) {
            EntityWither wither = entry.getValue();
            wither.setHealth((float) (progress * wither.getMaxHealth()));
            PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(wither.getId(), wither.getDataWatcher(), true);
            Player player = Bukkit.getPlayer(entry.getKey());
            if (player != null && player.isOnline()) {
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }
        }
    }

    public Location getWitherLocation(final Location location) {
        return location.add(location.getDirection().multiply(20));
    }

    @Override
    public void run() {
        for (Map.Entry<UUID, EntityWither> entry : this.withers.entrySet()) {
            EntityWither wither = entry.getValue();
            Player player = Bukkit.getPlayer(entry.getKey());
            if (player != null && player.isOnline()) {
                Location location = getWitherLocation(player.getLocation());
                wither.setLocation(location.getX(), location.getY(), location.getZ(), 0.0f, 0.0f);
                PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(wither);
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }
        }
    }
}