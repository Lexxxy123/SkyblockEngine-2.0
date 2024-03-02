package net.hypixel.skyblock.entity.dungeons.watcher;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import net.hypixel.skyblock.SkyBlock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalBossBar extends BukkitRunnable {
    private String title;
    private final HashMap<Player, EntityWither> withers;
    public List<Player> players;
    private final World w;

    public GlobalBossBar(final String title, final World w) {
        this.withers = new HashMap<Player, EntityWither>();
        this.players = new ArrayList<Player>();
        this.title = title;
        this.w = w;
        this.runTaskTimer(SkyBlock.getPlugin(), 0L, 1L);
    }

    public void addPlayer(final Player p) {
        this.players.add(p);
        final EntityWither wither = new EntityWither(((CraftWorld) p.getWorld()).getHandle());
        final Location l = this.getWitherLocation(p.getLocation());
        wither.setCustomName(this.title);
        wither.setInvisible(true);
        wither.r(877);
        wither.setLocation(l.getX(), l.getY(), l.getZ(), 0.0f, 0.0f);
        final PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(wither);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        this.withers.put(p, wither);
    }

    public void removePlayer(final Player p) {
        this.players.remove(p);
        final EntityWither wither = this.withers.remove(p);
        final PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(wither.getId());
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }

    public void setTitle(final String title) {
        this.title = title;
        for (final Map.Entry<Player, EntityWither> entry : this.withers.entrySet()) {
            final EntityWither wither = entry.getValue();
            wither.setCustomName(title);
            final PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(wither.getId(), wither.getDataWatcher(), true);
            ((CraftPlayer) entry.getKey()).getHandle().playerConnection.sendPacket(packet);
        }
    }

    public void setProgress(final double progress) {
        for (final Map.Entry<Player, EntityWither> entry : this.withers.entrySet()) {
            final EntityWither wither = entry.getValue();
            wither.setHealth((float) (progress * wither.getMaxHealth()));
            final PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(wither.getId(), wither.getDataWatcher(), true);
            ((CraftPlayer) entry.getKey()).getHandle().playerConnection.sendPacket(packet);
        }
    }

    public Location getWitherLocation(final Location l) {
        return l.add(l.getDirection().multiply(20)).add(0.0, 1.5, 0.0);
    }

    public void run() {
        for (final Map.Entry<Player, EntityWither> en : this.withers.entrySet()) {
            final EntityWither wither = en.getValue();
            final Location l = this.getWitherLocation(en.getKey().getLocation());
            wither.setLocation(l.getX(), l.getY(), l.getZ(), 0.0f, 0.0f);
            final PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(wither);
            ((CraftPlayer) en.getKey()).getHandle().playerConnection.sendPacket(packet);
        }
        for (final Player p : this.w.getPlayers()) {
            if (!this.players.contains(p)) {
                this.addPlayer(p);
            }
            if (p.getWorld() != this.w || !p.isOnline()) {
                this.removePlayer(p);
            }
        }
    }
}
