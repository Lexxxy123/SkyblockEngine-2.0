package in.godspunky.skyblock.npc.impl.type.impl;

import in.godspunky.skyblock.npc.impl.type.NPCBase;
import in.godspunky.skyblock.util.ReflectionsUtils;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftVillager;
import org.bukkit.entity.Player;

public class NPCVillagerImpl extends EntityVillager implements NPCBase {
    private final Location location;
    public NPCVillagerImpl(Location location) {
        super(((CraftWorld) location.getWorld()).getHandle());
        this.location = location;
        this.setPosition(location.getX(), location.getY(), location.getZ());
        ((CraftVillager) this.getBukkitEntity()).getHandle().setInvisible(false);
    }

    @Override
    public void hide(Player player) {
        PacketPlayOutEntityDestroy destroyPacket = new PacketPlayOutEntityDestroy(getId());
        sendPacket(player , destroyPacket);
    }

    @Override
    public void show(Player player) {
        PacketPlayOutSpawnEntityLiving spawnPacket = new PacketPlayOutSpawnEntityLiving(this);
        ReflectionsUtils.setValue(spawnPacket , "b" , 120); // 120 = Villager entity type
        sendPacket(player , spawnPacket);
    }

    @Override
    public void sendRotation(Player player) {
        Location original = location;
        Location location = original.clone().setDirection(player.getLocation().subtract(original.clone()).toVector());

        byte yaw = (byte) (location.getYaw() * 256 / 360);
        byte pitch = (byte) (location.getPitch() * 256 / 360);

        PacketPlayOutEntityHeadRotation headRotationPacket = new PacketPlayOutEntityHeadRotation(
                this,
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

    @Override
    public void setLocation(Location location) {
        this.setLocation(
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
        );
    }

    @Override
    public int entityId() {
        return bukkitEntity.getEntityId();
    }


}
