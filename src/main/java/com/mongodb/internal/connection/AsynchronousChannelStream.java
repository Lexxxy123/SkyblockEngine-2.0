/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoException;
import com.mongodb.MongoInternalException;
import com.mongodb.MongoInterruptedException;
import com.mongodb.MongoSocketReadException;
import com.mongodb.MongoSocketReadTimeoutException;
import com.mongodb.ServerAddress;
import com.mongodb.assertions.Assertions;
import com.mongodb.connection.AsyncCompletionHandler;
import com.mongodb.connection.BufferProvider;
import com.mongodb.connection.SocketSettings;
import com.mongodb.connection.Stream;
import com.mongodb.internal.connection.ExtendedAsynchronousByteChannel;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.nio.channels.InterruptedByTimeoutException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.bson.ByteBuf;

public abstract class AsynchronousChannelStream
implements Stream {
    private final ServerAddress serverAddress;
    private final SocketSettings settings;
    private final BufferProvider bufferProvider;
    private volatile ExtendedAsynchronousByteChannel channel;
    private volatile boolean isClosed;

    public AsynchronousChannelStream(ServerAddress serverAddress, SocketSettings settings, BufferProvider bufferProvider) {
        this.serverAddress = serverAddress;
        this.settings = settings;
        this.bufferProvider = bufferProvider;
    }

    public ServerAddress getServerAddress() {
        return this.serverAddress;
    }

    public SocketSettings getSettings() {
        return this.settings;
    }

    public BufferProvider getBufferProvider() {
        return this.bufferProvider;
    }

    public synchronized ExtendedAsynchronousByteChannel getChannel() {
        return this.channel;
    }

    protected synchronized void setChannel(ExtendedAsynchronousByteChannel channel) {
        Assertions.isTrue("current channel is null", this.channel == null);
        if (this.isClosed) {
            this.closeChannel(channel);
        } else {
            this.channel = channel;
        }
    }

    @Override
    public void writeAsync(List<ByteBuf> buffers, final AsyncCompletionHandler<Void> handler) {
        final AsyncWritableByteChannelAdapter byteChannel = new AsyncWritableByteChannelAdapter();
        final Iterator<ByteBuf> iter = buffers.iterator();
        this.pipeOneBuffer(byteChannel, iter.next(), new AsyncCompletionHandler<Void>(){

            @Override
            public void completed(Void t2) {
                if (iter.hasNext()) {
                    AsynchronousChannelStream.this.pipeOneBuffer(byteChannel, (ByteBuf)iter.next(), this);
                } else {
                    handler.completed(null);
                }
            }

            @Override
            public void failed(Throwable t2) {
                handler.failed(t2);
            }
        });
    }

    @Override
    public void readAsync(int numBytes, AsyncCompletionHandler<ByteBuf> handler) {
        ByteBuf buffer = this.bufferProvider.getBuffer(numBytes);
        this.channel.read(buffer.asNIO(), this.settings.getReadTimeout(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS, null, new BasicCompletionHandler(buffer, handler));
    }

    @Override
    public void open() throws IOException {
        FutureAsyncCompletionHandler<Void> handler = new FutureAsyncCompletionHandler<Void>();
        this.openAsync(handler);
        handler.getOpen();
    }

    @Override
    public void write(List<ByteBuf> buffers) throws IOException {
        FutureAsyncCompletionHandler<Void> handler = new FutureAsyncCompletionHandler<Void>();
        this.writeAsync(buffers, handler);
        handler.getWrite();
    }

    @Override
    public ByteBuf read(int numBytes) throws IOException {
        FutureAsyncCompletionHandler<ByteBuf> handler = new FutureAsyncCompletionHandler<ByteBuf>();
        this.readAsync(numBytes, handler);
        return handler.getRead();
    }

    @Override
    public ServerAddress getAddress() {
        return this.serverAddress;
    }

    @Override
    public synchronized void close() {
        this.isClosed = true;
        try {
            this.closeChannel(this.channel);
        } finally {
            this.channel = null;
        }
    }

    private void closeChannel(ExtendedAsynchronousByteChannel channel) {
        try {
            if (channel != null) {
                channel.close();
            }
        } catch (IOException iOException) {
            // empty catch block
        }
    }

    @Override
    public boolean isClosed() {
        return this.isClosed;
    }

    @Override
    public ByteBuf getBuffer(int size) {
        return this.bufferProvider.getBuffer(size);
    }

    private void pipeOneBuffer(final AsyncWritableByteChannelAdapter byteChannel, final ByteBuf byteBuffer, final AsyncCompletionHandler<Void> outerHandler) {
        byteChannel.write(byteBuffer.asNIO(), new AsyncCompletionHandler<Void>(){

            @Override
            public void completed(Void t2) {
                if (byteBuffer.hasRemaining()) {
                    byteChannel.write(byteBuffer.asNIO(), this);
                } else {
                    outerHandler.completed(null);
                }
            }

            @Override
            public void failed(Throwable t2) {
                outerHandler.failed(t2);
            }
        });
    }

    static class FutureAsyncCompletionHandler<T>
    implements AsyncCompletionHandler<T> {
        private final CountDownLatch latch = new CountDownLatch(1);
        private volatile T result;
        private volatile Throwable error;

        FutureAsyncCompletionHandler() {
        }

        @Override
        public void completed(T result) {
            this.result = result;
            this.latch.countDown();
        }

        @Override
        public void failed(Throwable t2) {
            this.error = t2;
            this.latch.countDown();
        }

        void getOpen() throws IOException {
            this.get("Opening");
        }

        void getWrite() throws IOException {
            this.get("Writing to");
        }

        T getRead() throws IOException {
            return this.get("Reading from");
        }

        private T get(String prefix) throws IOException {
            try {
                this.latch.await();
            } catch (InterruptedException e2) {
                throw new MongoInterruptedException(prefix + " the AsynchronousSocketChannelStream failed", e2);
            }
            if (this.error != null) {
                if (this.error instanceof IOException) {
                    throw (IOException)this.error;
                }
                if (this.error instanceof MongoException) {
                    throw (MongoException)this.error;
                }
                throw new MongoInternalException(prefix + " the TlsChannelStream failed", this.error);
            }
            return this.result;
        }
    }

    private static abstract class BaseCompletionHandler<T, V, A>
    implements CompletionHandler<V, A> {
        private final AtomicReference<AsyncCompletionHandler<T>> handlerReference;

        BaseCompletionHandler(AsyncCompletionHandler<T> handler) {
            this.handlerReference = new AtomicReference<AsyncCompletionHandler<T>>(handler);
        }

        AsyncCompletionHandler<T> getHandlerAndClear() {
            return this.handlerReference.getAndSet(null);
        }
    }

    private final class BasicCompletionHandler
    extends BaseCompletionHandler<ByteBuf, Integer, Void> {
        private final AtomicReference<ByteBuf> byteBufReference;

        private BasicCompletionHandler(ByteBuf dst, AsyncCompletionHandler<ByteBuf> handler) {
            super(handler);
            this.byteBufReference = new AtomicReference<ByteBuf>(dst);
        }

        @Override
        public void completed(Integer result, Void attachment) {
            AsyncCompletionHandler<ByteBuf> localHandler = this.getHandlerAndClear();
            ByteBuf localByteBuf = this.byteBufReference.getAndSet(null);
            if (result == -1) {
                localByteBuf.release();
                localHandler.failed(new MongoSocketReadException("Prematurely reached end of stream", AsynchronousChannelStream.this.serverAddress));
            } else if (!localByteBuf.hasRemaining()) {
                localByteBuf.flip();
                localHandler.completed(localByteBuf);
            } else {
                AsynchronousChannelStream.this.channel.read(localByteBuf.asNIO(), AsynchronousChannelStream.this.settings.getReadTimeout(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS, null, new BasicCompletionHandler(localByteBuf, localHandler));
            }
        }

        @Override
        public void failed(Throwable t2, Void attachment) {
            AsyncCompletionHandler localHandler = this.getHandlerAndClear();
            ByteBuf localByteBuf = this.byteBufReference.getAndSet(null);
            localByteBuf.release();
            if (t2 instanceof InterruptedByTimeoutException) {
                localHandler.failed(new MongoSocketReadTimeoutException("Timeout while receiving message", AsynchronousChannelStream.this.serverAddress, t2));
            } else {
                localHandler.failed(t2);
            }
        }
    }

    private class AsyncWritableByteChannelAdapter {
        private AsyncWritableByteChannelAdapter() {
        }

        void write(ByteBuffer src, AsyncCompletionHandler<Void> handler) {
            AsynchronousChannelStream.this.channel.write(src, null, new WriteCompletionHandler(handler));
        }

        private class WriteCompletionHandler
        extends BaseCompletionHandler<Void, Integer, Object> {
            WriteCompletionHandler(AsyncCompletionHandler<Void> handler) {
                super(handler);
            }

            @Override
            public void completed(Integer result, Object attachment) {
                AsyncCompletionHandler<Object> localHandler = this.getHandlerAndClear();
                localHandler.completed(null);
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                AsyncCompletionHandler localHandler = this.getHandlerAndClear();
                localHandler.failed(exc);
            }
        }
    }
}

