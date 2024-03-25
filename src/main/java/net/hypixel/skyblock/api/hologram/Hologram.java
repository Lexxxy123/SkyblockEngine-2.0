package net.hypixel.skyblock.api.hologram;

import net.hypixel.skyblock.api.reflection.ReflectionsUtils;
import net.hypixel.skyblock.util.SUtil;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Hologram extends EntityArmorStand {

    private final Set<UUID> viewers;

    public Location location;
    public Hologram(Location location) {
        super(((CraftWorld)location.getWorld()).getHandle());
        this.setPosition(
                location.getX(),
                location.getY(),
                location.getZ()
        );
        this.setInvisible(true);
        this.setCustomNameVisible(true);
        this.setGravity(false);
        ((ArmorStand) getBukkitEntity()).setMarker(true);
        this.setSmall(false);
        this.setBasePlate(false);
        this.location = location;
        this.viewers = new HashSet<>();
        HologramManager.register(this);

    }

    public void setText(String text){
      if (getCustomName().equals(text)) return;
      this.setCustomName(text);
      this.setCustomNameVisible(true);
      this.update();
    }


    public void mount(Entity attachEntity){
        mount(((CraftEntity)attachEntity).getHandle());
        update();
    }

    public void update(){
        PacketPlayOutEntityMetadata updatePacket = new PacketPlayOutEntityMetadata(getId() , this.getDataWatcher() , true);
        sendPacketToViewers(updatePacket);
    }

    public void show(Player player){
        if (viewers.contains(player.getUniqueId())) return;
        PacketPlayOutSpawnEntityLiving spawnPacket = new PacketPlayOutSpawnEntityLiving(this);
        ReflectionsUtils.setValue(spawnPacket , "b" , 30);
        sendPacket(player , spawnPacket);
        viewers.add(player.getUniqueId());
    }

    public boolean isShown(Player player){
        return viewers.contains(player.getUniqueId());
    }

    public void hide(Player player){
        if (!viewers.contains(player.getUniqueId())) return;
        PacketPlayOutEntityDestroy entityDestroyPacket = new PacketPlayOutEntityDestroy(getId());
        sendPacket(player , entityDestroyPacket);
        viewers.remove(player.getUniqueId());
    }

    public void teleport(Location location){
        this.location = location;
        if (location.getX() == this.locX && location.getY() == this.locY && location.getZ() == locZ) return;
        PacketPlayOutEntityTeleport entityTeleportPacket = new PacketPlayOutEntityTeleport(
                getId() ,
                MathHelper.floor(location.getX() * 32.0) ,
                MathHelper.floor(location.getY() * 32.0),
                MathHelper.floor(location.getZ() * 32.0),
                (byte)((int)(location.getYaw() * 256.0F / 360.0F)),
                (byte)((int)(location.getPitch() * 256.0F / 360.0F)),
                this.onGround
                );
        sendPacketToViewers(entityTeleportPacket);
    }

    public boolean inRangeOf(Player player) {
        if (player == null) return false;
        if (!player.getWorld().equals(location.getWorld())) {
            // No need to continue our checks, they are in different worlds.
            return false;
        }

        // If Bukkit doesn't track the entity anymore, bypass the hiding distance variable.
        double hideDistance = 20;
        double distanceSquared = player.getLocation().distanceSquared(location);
        double bukkitRange = Bukkit.getViewDistance() << 4;

        return distanceSquared <= SUtil.square(hideDistance) && distanceSquared <= SUtil.square(bukkitRange);
    }

    public void remove(){
        PacketPlayOutEntityDestroy entityDestroyPacket = new PacketPlayOutEntityDestroy(getId());
        sendPacketToViewers(entityDestroyPacket);
        HologramManager.remove(this);
    }

    public void sendPacketToViewers(Packet<?> packet){
        if (viewers.isEmpty()) return;
        viewers.forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()){
                sendPacket(player , packet);
            }
        });
    }


    public void sendPacket(Player player, Packet<?> packet) {
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }
}
