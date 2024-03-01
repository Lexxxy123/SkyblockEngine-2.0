package in.godspunky.skyblock.npc.impl.type;

import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public interface NPCBase {
    void hide(Player player);
   default void hideNameTag(Player player){

   }
    void show(Player player);
    void sendRotation(Player player);

    default void sendPacket(Player player, Packet<?> packet) {
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }
    void setLocation(Location location);

    int entityId();
}
