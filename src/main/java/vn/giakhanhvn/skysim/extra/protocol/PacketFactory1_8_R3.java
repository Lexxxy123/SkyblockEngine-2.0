package vn.giakhanhvn.skysim.extra.protocol;

import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.reflect.accessors.Accessors;
import com.comphenix.protocol.utility.MinecraftReflection;
import org.bukkit.entity.Entity;
import org.bukkit.Location;

public class PacketFactory1_8_R3 {
    public static WrappedBeamPacket createPacketSquidSpawn(Location location) {
        Entity fakeSquid = (Entity)Accessors.getConstructorAccessor((Class)MinecraftReflection.getCraftBukkitClass((String)"entity.CraftSquid"), (Class[])new Class[]{MinecraftReflection.getCraftBukkitClass((String)"CraftServer"), MinecraftReflection.getMinecraftClass((String)"EntitySquid")}).invoke(new Object[]{null, Accessors.getConstructorAccessor((Class)MinecraftReflection.getMinecraftClass((String)"EntitySquid"), (Class[])new Class[]{MinecraftReflection.getNmsWorldClass()}).invoke(new Object[]{null})});
        PacketContainer container = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
        container.getIntegers().write(0, EIDGen.generateEID());
        container.getIntegers().write(1, 94);
        container.getIntegers().write(2, (int) Math.floor(location.getX() * 32.0));
        container.getIntegers().write(3, (int) Math.floor(location.getY() * 32.0));
        container.getIntegers().write(4, (int) Math.floor(location.getZ() * 32.0));
        container.getBytes().write(0, (byte) (location.getYaw() * 256.0f / 360.0f));
        container.getBytes().write(1, (byte) (location.getPitch() * 256.0f / 360.0f));
        WrappedDataWatcher wrapper = WrappedDataWatcher.getEntityWatcher((Entity)fakeSquid);
        wrapper.setObject(0, 32);
        container.getDataWatcherModifier().write(0, wrapper);
        return new WrappedBeamPacket(container);
    }

    public static WrappedBeamPacket createPacketGuardianSpawn(Location location, WrappedBeamPacket squidPacket) {
        Entity fakeGuardian = (Entity)Accessors.getConstructorAccessor((Class)MinecraftReflection.getCraftBukkitClass((String)"entity.CraftGuardian"), (Class[])new Class[]{MinecraftReflection.getCraftBukkitClass((String)"CraftServer"), MinecraftReflection.getMinecraftClass((String)"EntityGuardian")}).invoke(new Object[]{null, Accessors.getConstructorAccessor((Class)MinecraftReflection.getMinecraftClass((String)"EntityGuardian"), (Class[])new Class[]{MinecraftReflection.getNmsWorldClass()}).invoke(new Object[]{null})});
        PacketContainer container = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
        container.getIntegers().write(0, EIDGen.generateEID());
        container.getIntegers().write(1, 68);
        container.getIntegers().write(2, (int) Math.floor(location.getX() * 32.0));
        container.getIntegers().write(3, (int) Math.floor(location.getY() * 32.0));
        container.getIntegers().write(4, (int) Math.floor(location.getZ() * 32.0));
        container.getBytes().write(0, (byte) (location.getYaw() * 256.0f / 360.0f));
        container.getBytes().write(1, (byte) (location.getPitch() * 256.0f / 360.0f));
        WrappedDataWatcher watcher = WrappedDataWatcher.getEntityWatcher((Entity)fakeGuardian);
        watcher.setObject(0, 32);
        watcher.setObject(16, 0);
        watcher.setObject(17, squidPacket.getHandle().getIntegers().read(0));
        container.getDataWatcherModifier().write(0, watcher);
        return new WrappedBeamPacket(container);
    }

    public static WrappedBeamPacket modifyPacketEntitySpawn(WrappedBeamPacket entitySpawnPacket, Location location) {
        PacketContainer container = entitySpawnPacket.getHandle();
        container.getIntegers().write(2, (int) Math.floor(location.getX() * 32.0));
        container.getIntegers().write(3, (int) Math.floor(location.getY() * 32.0));
        container.getIntegers().write(4, (int) Math.floor(location.getZ() * 32.0));
        container.getBytes().write(0, (byte) (location.getYaw() * 256.0f / 360.0f));
        container.getBytes().write(1, (byte) (location.getPitch() * 256.0f / 360.0f));
        return entitySpawnPacket;
    }

    public static WrappedBeamPacket createPacketEntityMove(WrappedBeamPacket entityPacket) {
        PacketContainer container = new PacketContainer(PacketType.Play.Server.ENTITY_TELEPORT);
        container.getIntegers().write(0, ((Integer)entityPacket.getHandle().getIntegers().read(0)));
        return new WrappedBeamPacket(container);
    }

    public static WrappedBeamPacket modifyPacketEntityMove(WrappedBeamPacket entityMovePacket, Location location) {
        PacketContainer container = entityMovePacket.getHandle();
        container.getIntegers().write(1, (int) Math.floor(location.getX() * 32.0));
        container.getIntegers().write(2, (int) Math.floor(location.getY() * 32.0));
        container.getIntegers().write(3, (int) Math.floor(location.getZ() * 32.0));
        container.getBytes().write(0, (byte) (location.getYaw() * 256.0f / 360.0f));
        container.getBytes().write(1, (byte) (location.getPitch() * 256.0f / 360.0f));
        return entityMovePacket;
    }

    public static WrappedBeamPacket createPacketRemoveEntities(WrappedBeamPacket squidPacket, WrappedBeamPacket guardianPacket) {
        PacketContainer container = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
        container.getIntegerArrays().write(0, new int[]{(Integer)squidPacket.getHandle().getIntegers().read(0), (Integer)guardianPacket.getHandle().getIntegers().read(0)});
        return new WrappedBeamPacket(container);
    }
}
