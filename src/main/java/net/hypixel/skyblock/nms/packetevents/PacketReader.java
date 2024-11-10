/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  io.netty.channel.ChannelDuplexHandler
 *  io.netty.channel.ChannelHandler
 *  io.netty.channel.ChannelHandlerContext
 *  io.netty.channel.ChannelPipeline
 *  net.minecraft.server.v1_8_R3.Packet
 *  net.minecraft.server.v1_8_R3.PacketPlayInUseEntity
 *  org.bukkit.Bukkit
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 */
package net.hypixel.skyblock.nms.packetevents;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import java.lang.reflect.Field;
import net.hypixel.skyblock.event.SkyblockPlayerNPCClickEvent;
import net.hypixel.skyblock.npc.impl.SkyblockNPC;
import net.hypixel.skyblock.npc.impl.SkyblockNPCManager;
import net.hypixel.skyblock.user.User;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class PacketReader {
    private Player player;

    public void injectPlayer(final Player player) {
        this.player = player;
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler(){

            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) {
                if (packet instanceof PacketPlayInUseEntity) {
                    try {
                        PacketPlayInUseEntity packetPlayInUseEntity = (PacketPlayInUseEntity)packet;
                        PacketReader.this.readPacket((Packet<?>)packetPlayInUseEntity, player);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    super.channelRead(channelHandlerContext, packet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ChannelPipeline pipeline = ((CraftPlayer)player).getHandle().playerConnection.networkManager.channel.pipeline();
        pipeline.addBefore("packet_handler", player.getName(), (ChannelHandler)channelDuplexHandler);
    }

    public void readPacket(Packet<?> packet, Player p) {
        if (packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInUseEntity")) {
            int id = (Integer)this.getValue(packet, "a");
            if (this.getValue(packet, "action").toString().equalsIgnoreCase("interact")) {
                for (SkyblockNPC npc : SkyblockNPCManager.getNPCS()) {
                    if (npc.getEntityID() != id) continue;
                    SkyblockPlayerNPCClickEvent npcClickEvent = new SkyblockPlayerNPCClickEvent(this.player, npc);
                    Bukkit.getPluginManager().callEvent((Event)npcClickEvent);
                    if (npc.canSpeak(User.getUser(this.player))) {
                        npc.speak(this.player);
                        continue;
                    }
                    npc.getParameters().onInteract(this.player, npc);
                }
            }
        }
    }

    private Object getValue(Object instance, String name) {
        Object result = null;
        try {
            Field field = instance.getClass().getDeclaredField(name);
            field.setAccessible(true);
            result = field.get(instance);
            field.setAccessible(false);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result;
    }
}

