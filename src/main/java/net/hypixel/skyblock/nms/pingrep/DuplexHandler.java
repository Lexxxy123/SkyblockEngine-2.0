package net.hypixel.skyblock.nms.pingrep;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_8_R3.PacketStatusOutPong;
import net.minecraft.server.v1_8_R3.PacketStatusOutServerInfo;

public class DuplexHandler extends ChannelDuplexHandler {
    private PingEvent event;

    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
        if (msg instanceof PacketStatusOutServerInfo) {
            final PacketStatusOutServerInfo packet = (PacketStatusOutServerInfo) msg;
            final PingReply reply = ServerInfoPacketHandler.constructReply(packet, ctx);
            this.event = new PingEvent(reply);
            for (final PingListener listener : PingAPI.getListeners()) {
                listener.onPing(this.event);
            }
            if (!this.event.isCancelled()) {
                super.write(ctx, ServerInfoPacketHandler.constructPacket(reply), promise);
            }
            return;
        }
        if (msg instanceof PacketStatusOutPong && this.event != null && this.event.isPongCancelled()) {
            return;
        }
        super.write(ctx, msg, promise);
    }

    public void close(final ChannelHandlerContext ctx, final ChannelPromise future) throws Exception {
        if (this.event == null || !this.event.isPongCancelled()) {
            super.close(ctx, future);
        }
    }
}
