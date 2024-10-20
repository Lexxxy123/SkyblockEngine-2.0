/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  io.netty.bootstrap.Bootstrap
 *  io.netty.buffer.ByteBuf
 *  io.netty.buffer.ByteBufAllocator
 *  io.netty.buffer.CompositeByteBuf
 *  io.netty.buffer.PooledByteBufAllocator
 *  io.netty.channel.Channel
 *  io.netty.channel.ChannelFuture
 *  io.netty.channel.ChannelFutureListener
 *  io.netty.channel.ChannelHandler
 *  io.netty.channel.ChannelHandlerContext
 *  io.netty.channel.ChannelInboundHandlerAdapter
 *  io.netty.channel.ChannelInitializer
 *  io.netty.channel.ChannelOption
 *  io.netty.channel.ChannelPipeline
 *  io.netty.channel.EventLoopGroup
 *  io.netty.channel.SimpleChannelInboundHandler
 *  io.netty.channel.socket.SocketChannel
 *  io.netty.handler.ssl.SslHandler
 *  io.netty.handler.timeout.ReadTimeoutException
 *  io.netty.util.concurrent.GenericFutureListener
 */
package com.mongodb.connection.netty;

import com.mongodb.MongoClientException;
import com.mongodb.MongoException;
import com.mongodb.MongoInternalException;
import com.mongodb.MongoInterruptedException;
import com.mongodb.MongoSocketException;
import com.mongodb.MongoSocketOpenException;
import com.mongodb.MongoSocketReadTimeoutException;
import com.mongodb.ServerAddress;
import com.mongodb.annotations.ThreadSafe;
import com.mongodb.connection.AsyncCompletionHandler;
import com.mongodb.connection.SocketSettings;
import com.mongodb.connection.SslSettings;
import com.mongodb.connection.Stream;
import com.mongodb.connection.netty.NettyByteBuf;
import com.mongodb.internal.connection.SslHelper;
import com.mongodb.lang.Nullable;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.util.concurrent.GenericFutureListener;
import java.io.IOException;
import java.net.SocketAddress;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import org.bson.ByteBuf;

final class NettyStream
implements Stream {
    private static final byte NO_SCHEDULE_TIME = 0;
    private final ServerAddress address;
    private final SocketSettings settings;
    private final SslSettings sslSettings;
    private final EventLoopGroup workerGroup;
    private final Class<? extends SocketChannel> socketChannelClass;
    private final ByteBufAllocator allocator;
    private volatile boolean isClosed;
    private volatile Channel channel;
    private final LinkedList<io.netty.buffer.ByteBuf> pendingInboundBuffers = new LinkedList();
    private PendingReader pendingReader;
    private Throwable pendingException;
    @Nullable
    private ReadTimeoutTask readTimeoutTask;
    private long readTimeoutMillis = 0L;

    NettyStream(ServerAddress address, SocketSettings settings, SslSettings sslSettings, EventLoopGroup workerGroup, Class<? extends SocketChannel> socketChannelClass, ByteBufAllocator allocator) {
        this.address = address;
        this.settings = settings;
        this.sslSettings = sslSettings;
        this.workerGroup = workerGroup;
        this.socketChannelClass = socketChannelClass;
        this.allocator = allocator;
    }

    @Override
    public ByteBuf getBuffer(int size) {
        return new NettyByteBuf(this.allocator.buffer(size, size));
    }

    @Override
    public void open() throws IOException {
        FutureAsyncCompletionHandler<Void> handler = new FutureAsyncCompletionHandler<Void>();
        this.openAsync(handler);
        handler.get();
    }

    @Override
    public void openAsync(AsyncCompletionHandler<Void> handler) {
        this.initializeChannel(handler, new LinkedList<SocketAddress>(this.address.getSocketAddresses()));
    }

    private void initializeChannel(AsyncCompletionHandler<Void> handler, Queue<SocketAddress> socketAddressQueue) {
        if (socketAddressQueue.isEmpty()) {
            handler.failed(new MongoSocketException("Exception opening socket", this.getAddress()));
        } else {
            SocketAddress nextAddress = socketAddressQueue.poll();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(this.workerGroup);
            bootstrap.channel(this.socketChannelClass);
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, (Object)this.settings.getConnectTimeout(TimeUnit.MILLISECONDS));
            bootstrap.option(ChannelOption.TCP_NODELAY, (Object)true);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, (Object)this.settings.isKeepAlive());
            if (this.settings.getReceiveBufferSize() > 0) {
                bootstrap.option(ChannelOption.SO_RCVBUF, (Object)this.settings.getReceiveBufferSize());
            }
            if (this.settings.getSendBufferSize() > 0) {
                bootstrap.option(ChannelOption.SO_SNDBUF, (Object)this.settings.getSendBufferSize());
            }
            bootstrap.option(ChannelOption.ALLOCATOR, (Object)this.allocator);
            bootstrap.handler((ChannelHandler)new ChannelInitializer<SocketChannel>(){

                public void initChannel(SocketChannel ch) {
                    int readTimeout;
                    ChannelPipeline pipeline = ch.pipeline();
                    if (NettyStream.this.sslSettings.isEnabled()) {
                        SSLEngine engine = NettyStream.this.getSslContext().createSSLEngine(NettyStream.this.address.getHost(), NettyStream.this.address.getPort());
                        engine.setUseClientMode(true);
                        SSLParameters sslParameters = engine.getSSLParameters();
                        SslHelper.enableSni(NettyStream.this.address.getHost(), sslParameters);
                        if (!NettyStream.this.sslSettings.isInvalidHostNameAllowed()) {
                            SslHelper.enableHostNameVerification(sslParameters);
                        }
                        engine.setSSLParameters(sslParameters);
                        pipeline.addFirst("ssl", (ChannelHandler)new SslHandler(engine, false));
                    }
                    if ((readTimeout = NettyStream.this.settings.getReadTimeout(TimeUnit.MILLISECONDS)) > 0) {
                        NettyStream.this.readTimeoutMillis = readTimeout;
                        pipeline.addLast(new ChannelHandler[]{new ChannelInboundHandlerAdapter()});
                        NettyStream.this.readTimeoutTask = new ReadTimeoutTask(pipeline.lastContext());
                    }
                    pipeline.addLast(new ChannelHandler[]{new InboundBufferHandler()});
                }
            });
            ChannelFuture channelFuture = bootstrap.connect(nextAddress);
            channelFuture.addListener((GenericFutureListener)new OpenChannelFutureListener(socketAddressQueue, channelFuture, handler));
        }
    }

    @Override
    public void write(List<ByteBuf> buffers) throws IOException {
        FutureAsyncCompletionHandler<Void> future = new FutureAsyncCompletionHandler<Void>();
        this.writeAsync(buffers, future);
        future.get();
    }

    @Override
    public ByteBuf read(int numBytes) throws IOException {
        FutureAsyncCompletionHandler<ByteBuf> future = new FutureAsyncCompletionHandler<ByteBuf>();
        this.readAsync(numBytes, future);
        return future.get();
    }

    @Override
    public void writeAsync(List<ByteBuf> buffers, final AsyncCompletionHandler<Void> handler) {
        CompositeByteBuf composite = PooledByteBufAllocator.DEFAULT.compositeBuffer();
        for (ByteBuf cur : buffers) {
            composite.addComponent(true, ((NettyByteBuf)cur).asByteBuf());
        }
        this.channel.writeAndFlush((Object)composite).addListener((GenericFutureListener)new ChannelFutureListener(){

            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    handler.failed(future.cause());
                } else {
                    handler.completed(null);
                }
            }
        });
    }

    @Override
    public void readAsync(int numBytes, AsyncCompletionHandler<ByteBuf> handler) {
        this.readAsync(numBytes, handler, this.readTimeoutMillis);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void readAsync(int numBytes, AsyncCompletionHandler<ByteBuf> handler, long readTimeoutMillis) {
        ByteBuf buffer = null;
        Throwable exceptionResult = null;
        NettyStream nettyStream = this;
        synchronized (nettyStream) {
            exceptionResult = this.pendingException;
            if (exceptionResult == null) {
                if (!this.hasBytesAvailable(numBytes)) {
                    if (this.pendingReader == null) {
                        this.pendingReader = new PendingReader(numBytes, handler, NettyStream.scheduleReadTimeout(this.readTimeoutTask, readTimeoutMillis));
                    }
                } else {
                    CompositeByteBuf composite = this.allocator.compositeBuffer(this.pendingInboundBuffers.size());
                    int bytesNeeded = numBytes;
                    Iterator iter = this.pendingInboundBuffers.iterator();
                    while (iter.hasNext()) {
                        io.netty.buffer.ByteBuf next = (io.netty.buffer.ByteBuf)iter.next();
                        int bytesNeededFromCurrentBuffer = Math.min(next.readableBytes(), bytesNeeded);
                        if (bytesNeededFromCurrentBuffer == next.readableBytes()) {
                            composite.addComponent(next);
                            iter.remove();
                        } else {
                            next.retain();
                            composite.addComponent(next.readSlice(bytesNeededFromCurrentBuffer));
                        }
                        composite.writerIndex(composite.writerIndex() + bytesNeededFromCurrentBuffer);
                        if ((bytesNeeded -= bytesNeededFromCurrentBuffer) != 0) continue;
                        break;
                    }
                    buffer = new NettyByteBuf((io.netty.buffer.ByteBuf)composite).flip();
                }
            }
            if ((exceptionResult != null || buffer != null) && this.pendingReader != null) {
                NettyStream.cancel(this.pendingReader.timeout);
                this.pendingReader = null;
            }
        }
        if (exceptionResult != null) {
            handler.failed(exceptionResult);
        }
        if (buffer != null) {
            handler.completed(buffer);
        }
    }

    private boolean hasBytesAvailable(int numBytes) {
        int bytesAvailable = 0;
        for (io.netty.buffer.ByteBuf cur : this.pendingInboundBuffers) {
            if ((bytesAvailable += cur.readableBytes()) < numBytes) continue;
            return true;
        }
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void handleReadResponse(io.netty.buffer.ByteBuf buffer, Throwable t2) {
        PendingReader localPendingReader = null;
        NettyStream nettyStream = this;
        synchronized (nettyStream) {
            if (buffer != null) {
                this.pendingInboundBuffers.add(buffer.retain());
            } else {
                this.pendingException = t2;
            }
            localPendingReader = this.pendingReader;
        }
        if (localPendingReader != null) {
            this.readAsync(localPendingReader.numBytes, localPendingReader.handler, 0L);
        }
    }

    @Override
    public ServerAddress getAddress() {
        return this.address;
    }

    @Override
    public synchronized void close() {
        this.isClosed = true;
        if (this.channel != null) {
            this.channel.close();
            this.channel = null;
        }
        Iterator iterator = this.pendingInboundBuffers.iterator();
        while (iterator.hasNext()) {
            io.netty.buffer.ByteBuf nextByteBuf = (io.netty.buffer.ByteBuf)iterator.next();
            iterator.remove();
            nextByteBuf.release();
        }
    }

    @Override
    public boolean isClosed() {
        return this.isClosed;
    }

    public SocketSettings getSettings() {
        return this.settings;
    }

    public SslSettings getSslSettings() {
        return this.sslSettings;
    }

    public EventLoopGroup getWorkerGroup() {
        return this.workerGroup;
    }

    public Class<? extends SocketChannel> getSocketChannelClass() {
        return this.socketChannelClass;
    }

    public ByteBufAllocator getAllocator() {
        return this.allocator;
    }

    private SSLContext getSslContext() {
        try {
            return this.sslSettings.getContext() == null ? SSLContext.getDefault() : this.sslSettings.getContext();
        } catch (NoSuchAlgorithmException e2) {
            throw new MongoClientException("Unable to create default SSLContext", e2);
        }
    }

    private static void cancel(@Nullable Future<?> f2) {
        if (f2 != null) {
            f2.cancel(false);
        }
    }

    private static long combinedTimeout(long timeout, int additionalTimeout) {
        if (timeout == 0L) {
            return 0L;
        }
        return Math.addExact(timeout, (long)additionalTimeout);
    }

    private static ScheduledFuture<?> scheduleReadTimeout(@Nullable ReadTimeoutTask readTimeoutTask, long timeoutMillis) {
        if (timeoutMillis == 0L) {
            return null;
        }
        return readTimeoutTask.schedule(timeoutMillis);
    }

    @ThreadSafe
    private static final class ReadTimeoutTask
    implements Runnable {
        private final ChannelHandlerContext ctx;

        private ReadTimeoutTask(ChannelHandlerContext timeoutChannelHandlerContext) {
            this.ctx = timeoutChannelHandlerContext;
        }

        @Override
        public void run() {
            try {
                if (this.ctx.channel().isOpen()) {
                    this.ctx.fireExceptionCaught((Throwable)ReadTimeoutException.INSTANCE);
                    this.ctx.close();
                }
            } catch (Throwable t2) {
                this.ctx.fireExceptionCaught(t2);
            }
        }

        private ScheduledFuture<?> schedule(long timeoutMillis) {
            return this.ctx.executor().schedule((Runnable)this, timeoutMillis, TimeUnit.MILLISECONDS);
        }
    }

    private class OpenChannelFutureListener
    implements ChannelFutureListener {
        private final Queue<SocketAddress> socketAddressQueue;
        private final ChannelFuture channelFuture;
        private final AsyncCompletionHandler<Void> handler;

        OpenChannelFutureListener(Queue<SocketAddress> socketAddressQueue, ChannelFuture channelFuture, AsyncCompletionHandler<Void> handler) {
            this.socketAddressQueue = socketAddressQueue;
            this.channelFuture = channelFuture;
            this.handler = handler;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public void operationComplete(ChannelFuture future) {
            NettyStream nettyStream = NettyStream.this;
            synchronized (nettyStream) {
                if (future.isSuccess()) {
                    if (NettyStream.this.isClosed) {
                        this.channelFuture.channel().close();
                    } else {
                        NettyStream.this.channel = this.channelFuture.channel();
                        NettyStream.this.channel.closeFuture().addListener((GenericFutureListener)new ChannelFutureListener(){

                            public void operationComplete(ChannelFuture future) {
                                NettyStream.this.handleReadResponse(null, new IOException("The connection to the server was closed"));
                            }
                        });
                    }
                    this.handler.completed(null);
                } else if (NettyStream.this.isClosed) {
                    this.handler.completed(null);
                } else if (this.socketAddressQueue.isEmpty()) {
                    this.handler.failed(new MongoSocketOpenException("Exception opening socket", NettyStream.this.getAddress(), future.cause()));
                } else {
                    NettyStream.this.initializeChannel(this.handler, this.socketAddressQueue);
                }
            }
        }
    }

    private static final class FutureAsyncCompletionHandler<T>
    implements AsyncCompletionHandler<T> {
        private final CountDownLatch latch = new CountDownLatch(1);
        private volatile T t;
        private volatile Throwable throwable;

        FutureAsyncCompletionHandler() {
        }

        @Override
        public void completed(T t2) {
            this.t = t2;
            this.latch.countDown();
        }

        @Override
        public void failed(Throwable t2) {
            this.throwable = t2;
            this.latch.countDown();
        }

        public T get() throws IOException {
            try {
                this.latch.await();
                if (this.throwable != null) {
                    if (this.throwable instanceof IOException) {
                        throw (IOException)this.throwable;
                    }
                    if (this.throwable instanceof MongoException) {
                        throw (MongoException)this.throwable;
                    }
                    throw new MongoInternalException("Exception thrown from Netty Stream", this.throwable);
                }
                return this.t;
            } catch (InterruptedException e2) {
                throw new MongoInterruptedException("Interrupted", e2);
            }
        }
    }

    private static final class PendingReader {
        private final int numBytes;
        private final AsyncCompletionHandler<ByteBuf> handler;
        @Nullable
        private final ScheduledFuture<?> timeout;

        private PendingReader(int numBytes, AsyncCompletionHandler<ByteBuf> handler, @Nullable ScheduledFuture<?> timeout) {
            this.numBytes = numBytes;
            this.handler = handler;
            this.timeout = timeout;
        }
    }

    private class InboundBufferHandler
    extends SimpleChannelInboundHandler<io.netty.buffer.ByteBuf> {
        private InboundBufferHandler() {
        }

        protected void channelRead0(ChannelHandlerContext ctx, io.netty.buffer.ByteBuf buffer) throws Exception {
            NettyStream.this.handleReadResponse(buffer, null);
        }

        public void exceptionCaught(ChannelHandlerContext ctx, Throwable t2) {
            if (t2 instanceof ReadTimeoutException) {
                NettyStream.this.handleReadResponse(null, new MongoSocketReadTimeoutException("Timeout while receiving message", NettyStream.this.address, t2));
            } else {
                NettyStream.this.handleReadResponse(null, t2);
            }
            ctx.close();
        }
    }
}

