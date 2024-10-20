/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  io.netty.channel.Channel
 *  io.netty.channel.ChannelDuplexHandler
 *  io.netty.channel.ChannelHandler
 *  io.netty.channel.ChannelHandlerContext
 *  io.netty.channel.ChannelPromise
 *  org.bukkit.entity.Player
 */
package net.hypixel.skyblock.nms.nmsutil.packetlistener.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import java.lang.reflect.Field;
import java.net.SocketAddress;
import java.util.ArrayList;
import net.hypixel.skyblock.nms.nmsutil.packetlistener.Cancellable;
import net.hypixel.skyblock.nms.nmsutil.packetlistener.IPacketListener;
import net.hypixel.skyblock.nms.nmsutil.packetlistener.channel.ChannelAbstract;
import net.hypixel.skyblock.nms.nmsutil.packetlistener.channel.ChannelWrapper;
import net.hypixel.skyblock.nms.nmsutil.reflection.minecraft.Minecraft;
import org.bukkit.entity.Player;

public class INCChannel
extends ChannelAbstract {
    private static final Field channelField = networkManagerFieldResolver.resolveByFirstTypeSilent(Channel.class);

    public INCChannel(IPacketListener iPacketListener) {
        super(iPacketListener);
    }

    @Override
    public void addChannel(final Player player) {
        try {
            final Channel channel = this.getChannel(player);
            this.addChannelExecutor.execute(new Runnable(){

                @Override
                public void run() {
                    try {
                        channel.pipeline().addBefore("packet_handler", "packet_listener_player", (io.netty.channel.ChannelHandler)new ChannelHandler(player));
                    } catch (Exception e2) {
                        throw new RuntimeException(e2);
                    }
                }
            });
        } catch (ReflectiveOperationException e2) {
            throw new RuntimeException("Failed to add channel for " + player, e2);
        }
    }

    @Override
    public void removeChannel(Player player) {
        try {
            final Channel channel = this.getChannel(player);
            this.removeChannelExecutor.execute(new Runnable(){

                @Override
                public void run() {
                    try {
                        if (channel.pipeline().get("packet_listener_player") != null) {
                            channel.pipeline().remove("packet_listener_player");
                        }
                    } catch (Exception e2) {
                        throw new RuntimeException(e2);
                    }
                }
            });
        } catch (ReflectiveOperationException e2) {
            throw new RuntimeException("Failed to remove channel for " + player, e2);
        }
    }

    Channel getChannel(Player player) throws ReflectiveOperationException {
        Object handle = Minecraft.getHandle(player);
        Object connection = playerConnection.get(handle);
        return (Channel)channelField.get(networkManager.get(connection));
    }

    @Override
    public ChannelAbstract.IListenerList newListenerList() {
        return new ListenerList();
    }

    class INCChannelWrapper
    extends ChannelWrapper<Channel>
    implements ChannelAbstract.IChannelWrapper {
        public INCChannelWrapper(Channel channel) {
            super(channel);
        }

        @Override
        public SocketAddress getRemoteAddress() {
            return ((Channel)this.channel()).remoteAddress();
        }

        @Override
        public SocketAddress getLocalAddress() {
            return ((Channel)this.channel()).localAddress();
        }
    }

    class ChannelHandler
    extends ChannelDuplexHandler
    implements ChannelAbstract.IChannelHandler {
        private final Object owner;

        public ChannelHandler(Player player) {
            this.owner = player;
        }

        public ChannelHandler(ChannelWrapper channelWrapper) {
            this.owner = channelWrapper;
        }

        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            Cancellable cancellable = new Cancellable();
            Object pckt = msg;
            if (ChannelAbstract.Packet.isAssignableFrom(msg.getClass())) {
                pckt = INCChannel.this.onPacketSend(this.owner, msg, cancellable);
            }
            if (cancellable.isCancelled()) {
                return;
            }
            super.write(ctx, pckt, promise);
        }

        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            Cancellable cancellable = new Cancellable();
            Object pckt = msg;
            if (ChannelAbstract.Packet.isAssignableFrom(msg.getClass())) {
                pckt = INCChannel.this.onPacketReceive(this.owner, msg, cancellable);
            }
            if (cancellable.isCancelled()) {
                return;
            }
            super.channelRead(ctx, pckt);
        }

        public String toString() {
            return "INCChannel$ChannelHandler@" + this.hashCode() + " (" + this.owner + ")";
        }
    }

    class ListenerList<E>
    extends ArrayList<E>
    implements ChannelAbstract.IListenerList<E> {
        ListenerList() {
        }

        @Override
        public boolean add(E paramE) {
            try {
                final E a2 = paramE;
                INCChannel.this.addChannelExecutor.execute(new Runnable(){

                    @Override
                    public void run() {
                        try {
                            Channel channel = null;
                            while (channel == null) {
                                channel = (Channel)channelField.get(a2);
                            }
                            if (channel.pipeline().get("packet_listener_server") == null) {
                                channel.pipeline().addBefore("packet_handler", "packet_listener_server", (io.netty.channel.ChannelHandler)new ChannelHandler(new INCChannelWrapper(channel)));
                            }
                        } catch (Exception exception) {
                            // empty catch block
                        }
                    }
                });
            } catch (Exception exception) {
                // empty catch block
            }
            return super.add(paramE);
        }

        @Override
        public boolean remove(Object arg0) {
            try {
                final Object a2 = arg0;
                INCChannel.this.removeChannelExecutor.execute(new Runnable(){

                    @Override
                    public void run() {
                        try {
                            Channel channel = null;
                            while (channel == null) {
                                channel = (Channel)channelField.get(a2);
                            }
                            channel.pipeline().remove("packet_listener_server");
                        } catch (Exception exception) {
                            // empty catch block
                        }
                    }
                });
            } catch (Exception exception) {
                // empty catch block
            }
            return super.remove(arg0);
        }
    }
}

