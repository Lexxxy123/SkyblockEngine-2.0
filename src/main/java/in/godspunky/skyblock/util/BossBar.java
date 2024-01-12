package in.godspunky.skyblock.util;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import in.godspunky.skyblock.Skyblock;

import java.util.HashMap;
import java.util.Map;

public class BossBar extends BukkitRunnable {
    private String title;
    private final HashMap<Player, EntityWither> withers;

    public BossBar(final String title) {
        this.withers = new HashMap<>();
        this.title = title;
        this.runTaskTimer(Skyblock.getPlugin(), 0L, 1L);
    }

    public void addPlayer(final Player p) {
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
        return l.add(l.getDirection().multiply(20));
    }

    public void run() {
        for (final Map.Entry<Player, EntityWither> en : this.withers.entrySet()) {
            final EntityWither wither = en.getValue();
            final Location l = this.getWitherLocation(en.getKey().getLocation());
            wither.setLocation(l.getX(), l.getY(), l.getZ(), 0.0f, 0.0f);
            final PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(wither);
            ((CraftPlayer) en.getKey()).getHandle().playerConnection.sendPacket(packet);
        }
    }
}
