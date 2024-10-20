/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  io.netty.channel.ChannelHandlerContext
 *  net.minecraft.server.v1_8_R3.ChatComponentText
 *  net.minecraft.server.v1_8_R3.IChatBaseComponent
 *  net.minecraft.server.v1_8_R3.IChatBaseComponent$ChatSerializer
 *  net.minecraft.server.v1_8_R3.PacketStatusOutServerInfo
 *  net.minecraft.server.v1_8_R3.ServerPing
 *  net.minecraft.server.v1_8_R3.ServerPing$ServerData
 *  net.minecraft.server.v1_8_R3.ServerPing$ServerPingPlayerSample
 *  org.bukkit.craftbukkit.v1_8_R3.util.CraftIconCache
 */
package net.hypixel.skyblock.nms.pingrep;

import com.mojang.authlib.GameProfile;
import io.netty.channel.ChannelHandlerContext;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.hypixel.skyblock.nms.pingrep.PingReply;
import net.hypixel.skyblock.nms.pingrep.ServerInfoPacket;
import net.hypixel.skyblock.nms.pingrep.reflect.ReflectUtils;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketStatusOutServerInfo;
import net.minecraft.server.v1_8_R3.ServerPing;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftIconCache;

public class ServerInfoPacketHandler
extends ServerInfoPacket {
    private static final Field SERVER_PING_FIELD = ReflectUtils.getFirstFieldByType(PacketStatusOutServerInfo.class, ServerPing.class);

    public ServerInfoPacketHandler(PingReply reply) {
        super(reply);
    }

    @Override
    public void send() {
        try {
            Field field = this.getReply().getClass().getDeclaredField("ctx");
            field.setAccessible(true);
            Object ctx = field.get(this.getReply());
            Method writeAndFlush = ctx.getClass().getMethod("writeAndFlush", Object.class);
            writeAndFlush.setAccessible(true);
            writeAndFlush.invoke(ctx, ServerInfoPacketHandler.constructPacket(this.getReply()));
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | NoSuchMethodException | SecurityException | InvocationTargetException e2) {
            e2.printStackTrace();
        }
    }

    public static PacketStatusOutServerInfo constructPacket(PingReply reply) {
        GameProfile[] sample = new GameProfile[reply.getPlayerSample().size()];
        List<String> list = reply.getPlayerSample();
        for (int i2 = 0; i2 < list.size(); ++i2) {
            sample[i2] = new GameProfile(UUID.randomUUID(), list.get(i2));
        }
        ServerPing.ServerPingPlayerSample playerSample = new ServerPing.ServerPingPlayerSample(reply.getMaxPlayers(), reply.getOnlinePlayers());
        playerSample.a(sample);
        ServerPing ping = new ServerPing();
        ping.setMOTD((IChatBaseComponent)new ChatComponentText(reply.getMOTD()));
        ping.setPlayerSample(playerSample);
        ping.setServerInfo(new ServerPing.ServerData(reply.getProtocolName(), reply.getProtocolVersion()));
        ping.setFavicon(((CraftIconCache)reply.getIcon()).value);
        return new PacketStatusOutServerInfo(ping);
    }

    public static PingReply constructReply(PacketStatusOutServerInfo packet, ChannelHandlerContext ctx) {
        try {
            ServerPing ping = (ServerPing)SERVER_PING_FIELD.get(packet);
            String motd = IChatBaseComponent.ChatSerializer.a((IChatBaseComponent)ping.a());
            int max = ping.b().a();
            int online = ping.b().b();
            int protocolVersion = ping.c().b();
            String protocolName = ping.c().a();
            GameProfile[] profiles = ping.b().c();
            ArrayList<String> list = new ArrayList<String>();
            for (int i2 = 0; i2 < profiles.length; ++i2) {
                list.add(profiles[i2].getName());
            }
            PingReply reply = new PingReply(ctx, motd, online, max, protocolVersion, protocolName, list);
            return reply;
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
            return null;
        }
    }
}

