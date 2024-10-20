/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.Packet
 *  org.bukkit.Location
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.npc.impl.type;

import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public interface NPCBase {
    public void hide(Player var1);

    default public void hideNameTag(Player player) {
    }

    public void show(Player var1);

    public void sendRotation(Player var1);

    default public void sendPacket(Player player, Packet<?> packet) {
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }

    public void setLocation(Location var1);

    public int entityId();
}

