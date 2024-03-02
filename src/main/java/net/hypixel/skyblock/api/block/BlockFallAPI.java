package net.hypixel.skyblock.api.block;

import net.hypixel.skyblock.util.SUtil;
import net.minecraft.server.v1_8_R3.EntityFallingBlock;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityVelocity;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class BlockFallAPI {
    public static void sendVelocityBlock(final Location loc, final Material mat, final byte data, final World players, final Integer delay, final Vector vec) {
        final net.minecraft.server.v1_8_R3.World world = ((CraftWorld) loc.getWorld()).getHandle();
        final EntityFallingBlock entityfallingblock = new EntityFallingBlock(world);
        entityfallingblock.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0.0f, 0.0f);
        final PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity(entityfallingblock, 70, mat.getId() + (data << 12));
        final double x = vec.getX();
        final double y = vec.getY();
        final double z = vec.getZ();
        for (final Player player : players.getPlayers()) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityVelocity(entityfallingblock.getId(), x, y, z));
        }
        SUtil.delay(() -> removeBlock(entityfallingblock, players), delay);
    }

    public static void sendBlock(final Location loc, final Material mat, final byte data, final World players, final Integer delay) {
        final net.minecraft.server.v1_8_R3.World world = ((CraftWorld) loc.getWorld()).getHandle();
        final EntityFallingBlock entityfallingblock = new EntityFallingBlock(world);
        entityfallingblock.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0.0f, 0.0f);
        final PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity(entityfallingblock, 70, mat.getId() + (data << 12));
        for (final Player player : players.getPlayers()) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
        SUtil.delay(() -> removeBlock(entityfallingblock, players), delay);
    }

    public static void removeBlock(final EntityFallingBlock entityfallingblock, final World players) {
        if (entityfallingblock == null) {
            return;
        }
        final PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(entityfallingblock.getId());
        for (final Player player : players.getPlayers()) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(destroy);
        }
    }

    public static EntityFallingBlock sendBlockDestroyWithSignal(final Location loc, final Material mat, final byte data, final World players) {
        final net.minecraft.server.v1_8_R3.World world = ((CraftWorld) loc.getWorld()).getHandle();
        final EntityFallingBlock entityfallingblock = new EntityFallingBlock(world);
        entityfallingblock.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0.0f, 0.0f);
        final PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity(entityfallingblock, 70, mat.getId() + (data << 12));
        for (final Player player : players.getPlayers()) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
        SUtil.delay(() -> removeBlock(entityfallingblock, players), 300L);
        return entityfallingblock;
    }
}
