/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoSocketException;
import com.mongodb.MongoSocketOpenException;
import com.mongodb.ServerAddress;
import com.mongodb.assertions.Assertions;
import com.mongodb.connection.AsyncCompletionHandler;
import com.mongodb.connection.BufferProvider;
import com.mongodb.connection.SocketSettings;
import com.mongodb.connection.Stream;
import com.mongodb.internal.connection.AsynchronousChannelStream;
import com.mongodb.internal.connection.ExtendedAsynchronousByteChannel;
import java.io.IOException;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public final class AsynchronousSocketChannelStream
extends AsynchronousChannelStream
implements Stream {
    private final ServerAddress serverAddress;
    private final SocketSettings settings;
    private final AsynchronousChannelGroup group;

    public AsynchronousSocketChannelStream(ServerAddress serverAddress, SocketSettings settings, BufferProvider bufferProvider, AsynchronousChannelGroup group) {
        super(serverAddress, settings, bufferProvider);
        this.serverAddress = serverAddress;
        this.settings = settings;
        this.group = group;
    }

    @Override
    public void openAsync(AsyncCompletionHandler<Void> handler) {
        Assertions.isTrue("unopened", this.getChannel() == null);
        this.initializeSocketChannel(handler, new LinkedList<SocketAddress>(this.serverAddress.getSocketAddresses()));
    }

    private void initializeSocketChannel(AsyncCompletionHandler<Void> handler, Queue<SocketAddress> socketAddressQueue) {
        if (socketAddressQueue.isEmpty()) {
            handler.failed(new MongoSocketException("Exception opening socket", this.serverAddress));
        } else {
            SocketAddress socketAddress = socketAddressQueue.poll();
            try {
                AsynchronousSocketChannel attemptConnectionChannel = AsynchronousSocketChannel.open(this.group);
                attemptConnectionChannel.setOption((SocketOption)StandardSocketOptions.TCP_NODELAY, (Object)true);
                attemptConnectionChannel.setOption((SocketOption)StandardSocketOptions.SO_KEEPALIVE, (Object)this.settings.isKeepAlive());
                if (this.settings.getReceiveBufferSize() > 0) {
                    attemptConnectionChannel.setOption((SocketOption)StandardSocketOptions.SO_RCVBUF, (Object)this.settings.getReceiveBufferSize());
                }
                if (this.settings.getSendBufferSize() > 0) {
                    attemptConnectionChannel.setOption((SocketOption)StandardSocketOptions.SO_SNDBUF, (Object)this.settings.getSendBufferSize());
                }
                attemptConnectionChannel.connect(socketAddress, null, new OpenCompletionHandler(handler, socketAddressQueue, attemptConnectionChannel));
            } catch (IOException e2) {
                handler.failed(new MongoSocketOpenException("Exception opening socket", this.serverAddress, (Throwable)e2));
            } catch (Throwable t2) {
                handler.failed(t2);
            }
        }
    }

    public AsynchronousChannelGroup getGroup() {
        return this.group;
    }

    private static final class AsynchronousSocketChannelAdapter
    implements ExtendedAsynchronousByteChannel {
        private final AsynchronousSocketChannel channel;

        private AsynchronousSocketChannelAdapter(AsynchronousSocketChannel channel) {
            this.channel = channel;
        }

        @Override
        public <A> void read(ByteBuffer dst, long timeout, TimeUnit unit, A attach, CompletionHandler<Integer, ? super A> handler) {
            this.channel.read(dst, timeout, unit, attach, handler);
        }

        @Override
        public <A> void read(ByteBuffer[] dsts, int offset, int length, long timeout, TimeUnit unit, A attach, CompletionHandler<Long, ? super A> handler) {
            this.channel.read(dsts, offset, length, timeout, unit, attach, handler);
        }

        @Override
        public <A> void write(ByteBuffer src, long timeout, TimeUnit unit, A attach, CompletionHandler<Integer, ? super A> handler) {
            this.channel.write(src, timeout, unit, attach, handler);
        }

        @Override
        public <A> void write(ByteBuffer[] srcs, int offset, int length, long timeout, TimeUnit unit, A attach, CompletionHandler<Long, ? super A> handler) {
            this.channel.write(srcs, offset, length, timeout, unit, attach, handler);
        }

        @Override
        public <A> void read(ByteBuffer dst, A attachment, CompletionHandler<Integer, ? super A> handler) {
            this.channel.read(dst, attachment, handler);
        }

        @Override
        public Future<Integer> read(ByteBuffer dst) {
            return this.channel.read(dst);
        }

        @Override
        public <A> void write(ByteBuffer src, A attachment, CompletionHandler<Integer, ? super A> handler) {
            this.channel.write(src, attachment, handler);
        }

        @Override
        public Future<Integer> write(ByteBuffer src) {
            return this.channel.write(src);
        }

        @Override
        public boolean isOpen() {
            return this.channel.isOpen();
        }

        @Override
        public void close() throws IOException {
            this.channel.close();
        }
    }

    private class OpenCompletionHandler
    implements CompletionHandler<Void, Object> {
        private AtomicReference<AsyncCompletionHandler<Void>> handlerReference;
        private final Queue<SocketAddress> socketAddressQueue;
        private final AsynchronousSocketChannel attemptConnectionChannel;

        OpenCompletionHandler(AsyncCompletionHandler<Void> handler, Queue<SocketAddress> socketAddressQueue, AsynchronousSocketChannel attemptConnectionChannel) {
            this.handlerReference = new AtomicReference<AsyncCompletionHandler<Void>>(handler);
            this.socketAddressQueue = socketAddressQueue;
            this.attemptConnectionChannel = attemptConnectionChannel;
        }

        @Override
        public void completed(Void result, Object attachment) {
            AsynchronousSocketChannelStream.this.setChannel(new AsynchronousSocketChannelAdapter(this.attemptConnectionChannel));
            ((AsyncCompletionHandler)this.handlerReference.getAndSet(null)).completed(null);
        }

        @Override
        public void failed(Throwable exc, Object attachment) {
            AsyncCompletionHandler localHandler = this.handlerReference.getAndSet(null);
            if (this.socketAddressQueue.isEmpty()) {
                if (exc instanceof IOException) {
                    localHandler.failed(new MongoSocketOpenException("Exception opening socket", AsynchronousSocketChannelStream.this.getAddress(), exc));
                } else {
                    localHandler.failed(exc);
                }
            } else {
                AsynchronousSocketChannelStream.this.initializeSocketChannel(localHandler, this.socketAddressQueue);
            }
        }
    }
}

