/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  io.netty.channel.ChannelDuplexHandler
 *  io.netty.channel.ChannelHandlerContext
 *  io.netty.channel.ChannelPromise
 *  net.minecraft.server.v1_8_R3.PacketStatusOutPong
 *  net.minecraft.server.v1_8_R3.PacketStatusOutServerInfo
 */
package net.hypixel.skyblock.nms.pingrep;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.hypixel.skyblock.nms.pingrep.PingAPI;
import net.hypixel.skyblock.nms.pingrep.PingEvent;
import net.hypixel.skyblock.nms.pingrep.PingListener;
import net.hypixel.skyblock.nms.pingrep.PingReply;
import net.hypixel.skyblock.nms.pingrep.ServerInfoPacketHandler;
import net.minecraft.server.v1_8_R3.PacketStatusOutPong;
import net.minecraft.server.v1_8_R3.PacketStatusOutServerInfo;

public class DuplexHandler
extends ChannelDuplexHandler {
    private PingEvent event;

    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof PacketStatusOutServerInfo) {
            PacketStatusOutServerInfo packet = (PacketStatusOutServerInfo)msg;
            PingReply reply = ServerInfoPacketHandler.constructReply(packet, ctx);
            this.event = new PingEvent(reply);
            for (PingListener listener : PingAPI.getListeners()) {
                listener.onPing(this.event);
            }
            if (!this.event.isCancelled()) {
                super.write(ctx, (Object)ServerInfoPacketHandler.constructPacket(reply), promise);
            }
            return;
        }
        if (msg instanceof PacketStatusOutPong && this.event != null && this.event.isPongCancelled()) {
            return;
        }
        super.write(ctx, msg, promise);
    }

    public void close(ChannelHandlerContext ctx, ChannelPromise future) throws Exception {
        if (this.event == null || !this.event.isPongCancelled()) {
            super.close(ctx, future);
        }
    }
}

