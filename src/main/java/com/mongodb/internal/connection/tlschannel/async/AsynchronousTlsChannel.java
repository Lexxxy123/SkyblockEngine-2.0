/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection.tlschannel.async;

import com.mongodb.internal.connection.ExtendedAsynchronousByteChannel;
import com.mongodb.internal.connection.tlschannel.TlsChannel;
import com.mongodb.internal.connection.tlschannel.async.AsynchronousTlsChannelGroup;
import com.mongodb.internal.connection.tlschannel.impl.ByteBufferSet;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.CompletionHandler;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

public class AsynchronousTlsChannel
implements ExtendedAsynchronousByteChannel {
    private final AsynchronousTlsChannelGroup group;
    private final TlsChannel tlsChannel;
    private final AsynchronousTlsChannelGroup.RegisteredSocket registeredSocket;

    public AsynchronousTlsChannel(AsynchronousTlsChannelGroup channelGroup, TlsChannel tlsChannel, SocketChannel socketChannel) throws ClosedChannelException, IllegalArgumentException {
        if (!socketChannel.isOpen()) {
            throw new ClosedChannelException();
        }
        if (!tlsChannel.isOpen()) {
            throw new ClosedChannelException();
        }
        if (socketChannel.isBlocking()) {
            throw new IllegalArgumentException("socket channel must be in non-blocking mode");
        }
        this.group = channelGroup;
        this.tlsChannel = tlsChannel;
        this.registeredSocket = channelGroup.registerSocket(tlsChannel, socketChannel);
    }

    @Override
    public <A> void read(ByteBuffer dst, final A attach, final CompletionHandler<Integer, ? super A> handler) {
        this.checkReadOnly(dst);
        if (!dst.hasRemaining()) {
            this.completeWithZeroInt(attach, handler);
            return;
        }
        this.group.startRead(this.registeredSocket, new ByteBufferSet(dst), 0L, TimeUnit.MILLISECONDS, new LongConsumer(){

            @Override
            public void accept(final long c2) {
                ((AsynchronousTlsChannel)AsynchronousTlsChannel.this).group.executor.submit(new Runnable(){

                    @Override
                    public void run() {
                        handler.completed((int)c2, attach);
                    }
                });
            }
        }, new Consumer<Throwable>(){

            @Override
            public void accept(final Throwable e2) {
                ((AsynchronousTlsChannel)AsynchronousTlsChannel.this).group.executor.submit(new Runnable(){

                    @Override
                    public void run() {
                        handler.failed(e2, attach);
                    }
                });
            }
        });
    }

    @Override
    public <A> void read(ByteBuffer dst, long timeout, TimeUnit unit, final A attach, final CompletionHandler<Integer, ? super A> handler) {
        this.checkReadOnly(dst);
        if (!dst.hasRemaining()) {
            this.completeWithZeroInt(attach, handler);
            return;
        }
        this.group.startRead(this.registeredSocket, new ByteBufferSet(dst), timeout, unit, new LongConsumer(){

            @Override
            public void accept(final long c2) {
                ((AsynchronousTlsChannel)AsynchronousTlsChannel.this).group.executor.submit(new Runnable(){

                    @Override
                    public void run() {
                        handler.completed((int)c2, attach);
                    }
                });
            }
        }, new Consumer<Throwable>(){

            @Override
            public void accept(final Throwable e2) {
                ((AsynchronousTlsChannel)AsynchronousTlsChannel.this).group.executor.submit(new Runnable(){

                    @Override
                    public void run() {
                        handler.failed(e2, attach);
                    }
                });
            }
        });
    }

    @Override
    public <A> void read(ByteBuffer[] dsts, int offset, int length, long timeout, TimeUnit unit, final A attach, final CompletionHandler<Long, ? super A> handler) {
        ByteBufferSet bufferSet = new ByteBufferSet(dsts, offset, length);
        if (bufferSet.isReadOnly()) {
            throw new IllegalArgumentException("buffer is read-only");
        }
        if (!bufferSet.hasRemaining()) {
            this.completeWithZeroLong(attach, handler);
            return;
        }
        this.group.startRead(this.registeredSocket, bufferSet, timeout, unit, new LongConsumer(){

            @Override
            public void accept(final long c2) {
                ((AsynchronousTlsChannel)AsynchronousTlsChannel.this).group.executor.submit(new Runnable(){

                    @Override
                    public void run() {
                        handler.completed(c2, attach);
                    }
                });
            }
        }, new Consumer<Throwable>(){

            @Override
            public void accept(final Throwable e2) {
                ((AsynchronousTlsChannel)AsynchronousTlsChannel.this).group.executor.submit(new Runnable(){

                    @Override
                    public void run() {
                        handler.failed(e2, attach);
                    }
                });
            }
        });
    }

    @Override
    public Future<Integer> read(ByteBuffer dst) {
        this.checkReadOnly(dst);
        if (!dst.hasRemaining()) {
            return CompletableFuture.completedFuture(0);
        }
        final FutureReadResult future = new FutureReadResult();
        future.op = this.group.startRead(this.registeredSocket, new ByteBufferSet(dst), 0L, TimeUnit.MILLISECONDS, new LongConsumer(){

            @Override
            public void accept(long c2) {
                future.complete((int)c2);
            }
        }, new Consumer<Throwable>(){

            @Override
            public void accept(Throwable ex) {
                future.completeExceptionally(ex);
            }
        });
        return future;
    }

    private void checkReadOnly(ByteBuffer dst) {
        if (dst.isReadOnly()) {
            throw new IllegalArgumentException("buffer is read-only");
        }
    }

    @Override
    public <A> void write(ByteBuffer src, final A attach, final CompletionHandler<Integer, ? super A> handler) {
        if (!src.hasRemaining()) {
            this.completeWithZeroInt(attach, handler);
            return;
        }
        this.group.startWrite(this.registeredSocket, new ByteBufferSet(src), 0L, TimeUnit.MILLISECONDS, new LongConsumer(){

            @Override
            public void accept(final long c2) {
                ((AsynchronousTlsChannel)AsynchronousTlsChannel.this).group.executor.submit(new Runnable(){

                    @Override
                    public void run() {
                        handler.completed((int)c2, attach);
                    }
                });
            }
        }, new Consumer<Throwable>(){

            @Override
            public void accept(final Throwable e2) {
                ((AsynchronousTlsChannel)AsynchronousTlsChannel.this).group.executor.submit(new Runnable(){

                    @Override
                    public void run() {
                        handler.failed(e2, attach);
                    }
                });
            }
        });
    }

    @Override
    public <A> void write(ByteBuffer src, long timeout, TimeUnit unit, final A attach, final CompletionHandler<Integer, ? super A> handler) {
        if (!src.hasRemaining()) {
            this.completeWithZeroInt(attach, handler);
            return;
        }
        this.group.startWrite(this.registeredSocket, new ByteBufferSet(src), timeout, unit, new LongConsumer(){

            @Override
            public void accept(final long c2) {
                ((AsynchronousTlsChannel)AsynchronousTlsChannel.this).group.executor.submit(new Runnable(){

                    @Override
                    public void run() {
                        handler.completed((int)c2, attach);
                    }
                });
            }
        }, new Consumer<Throwable>(){

            @Override
            public void accept(final Throwable e2) {
                ((AsynchronousTlsChannel)AsynchronousTlsChannel.this).group.executor.submit(new Runnable(){

                    @Override
                    public void run() {
                        handler.failed(e2, attach);
                    }
                });
            }
        });
    }

    @Override
    public <A> void write(ByteBuffer[] srcs, int offset, int length, long timeout, TimeUnit unit, final A attach, final CompletionHandler<Long, ? super A> handler) {
        ByteBufferSet bufferSet = new ByteBufferSet(srcs, offset, length);
        if (!bufferSet.hasRemaining()) {
            this.completeWithZeroLong(attach, handler);
            return;
        }
        this.group.startWrite(this.registeredSocket, bufferSet, timeout, unit, new LongConsumer(){

            @Override
            public void accept(final long c2) {
                ((AsynchronousTlsChannel)AsynchronousTlsChannel.this).group.executor.submit(new Runnable(){

                    @Override
                    public void run() {
                        handler.completed(c2, attach);
                    }
                });
            }
        }, new Consumer<Throwable>(){

            @Override
            public void accept(final Throwable e2) {
                ((AsynchronousTlsChannel)AsynchronousTlsChannel.this).group.executor.submit(new Runnable(){

                    @Override
                    public void run() {
                        handler.failed(e2, attach);
                    }
                });
            }
        });
    }

    @Override
    public Future<Integer> write(ByteBuffer src) {
        if (!src.hasRemaining()) {
            return CompletableFuture.completedFuture(0);
        }
        final FutureWriteResult future = new FutureWriteResult();
        future.op = this.group.startWrite(this.registeredSocket, new ByteBufferSet(src), 0L, TimeUnit.MILLISECONDS, new LongConsumer(){

            @Override
            public void accept(long c2) {
                future.complete((int)c2);
            }
        }, new Consumer<Throwable>(){

            @Override
            public void accept(Throwable ex) {
                future.completeExceptionally(ex);
            }
        });
        return future;
    }

    private <A> void completeWithZeroInt(final A attach, final CompletionHandler<Integer, ? super A> handler) {
        this.group.executor.submit(new Runnable(){

            @Override
            public void run() {
                handler.completed(0, attach);
            }
        });
    }

    private <A> void completeWithZeroLong(final A attach, final CompletionHandler<Long, ? super A> handler) {
        this.group.executor.submit(new Runnable(){

            @Override
            public void run() {
                handler.completed(0L, attach);
            }
        });
    }

    @Override
    public boolean isOpen() {
        return this.tlsChannel.isOpen();
    }

    @Override
    public void close() throws IOException {
        this.tlsChannel.close();
        this.registeredSocket.close();
    }

    private class FutureWriteResult
    extends CompletableFuture<Integer> {
        AsynchronousTlsChannelGroup.WriteOperation op;

        private FutureWriteResult() {
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            super.cancel(mayInterruptIfRunning);
            return AsynchronousTlsChannel.this.group.doCancelWrite(AsynchronousTlsChannel.this.registeredSocket, this.op);
        }
    }

    private class FutureReadResult
    extends CompletableFuture<Integer> {
        AsynchronousTlsChannelGroup.ReadOperation op;

        private FutureReadResult() {
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            super.cancel(mayInterruptIfRunning);
            return AsynchronousTlsChannel.this.group.doCancelRead(AsynchronousTlsChannel.this.registeredSocket, this.op);
        }
    }
}

