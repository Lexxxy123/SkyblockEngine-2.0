/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection.tlschannel;

import com.mongodb.internal.connection.tlschannel.BufferAllocator;
import com.mongodb.internal.connection.tlschannel.TlsChannel;
import com.mongodb.internal.connection.tlschannel.TlsChannelBuilder;
import com.mongodb.internal.connection.tlschannel.TrackingAllocator;
import com.mongodb.internal.connection.tlschannel.impl.BufferHolder;
import com.mongodb.internal.connection.tlschannel.impl.ByteBufferSet;
import com.mongodb.internal.connection.tlschannel.impl.TlsChannelImpl;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;

public final class ClientTlsChannel
implements TlsChannel {
    private final ByteChannel underlying;
    private final TlsChannelImpl impl;

    private static SSLEngine defaultSSLEngineFactory(SSLContext sslContext) {
        SSLEngine engine = sslContext.createSSLEngine();
        engine.setUseClientMode(true);
        return engine;
    }

    public static Builder newBuilder(ByteChannel underlying, SSLEngine sslEngine) {
        return new Builder(underlying, sslEngine);
    }

    public static Builder newBuilder(ByteChannel underlying, SSLContext sslContext) {
        return new Builder(underlying, sslContext);
    }

    private ClientTlsChannel(ByteChannel underlying, SSLEngine engine, Consumer<SSLSession> sessionInitCallback, boolean runTasks, BufferAllocator plainBufAllocator, BufferAllocator encryptedBufAllocator, boolean releaseBuffers, boolean waitForCloseNotifyOnClose) {
        if (!engine.getUseClientMode()) {
            throw new IllegalArgumentException("SSLEngine must be in client mode");
        }
        this.underlying = underlying;
        TrackingAllocator trackingPlainBufAllocator = new TrackingAllocator(plainBufAllocator);
        TrackingAllocator trackingEncryptedAllocator = new TrackingAllocator(encryptedBufAllocator);
        this.impl = new TlsChannelImpl(underlying, underlying, engine, Optional.<BufferHolder>empty(), sessionInitCallback, runTasks, trackingPlainBufAllocator, trackingEncryptedAllocator, releaseBuffers, waitForCloseNotifyOnClose);
    }

    @Override
    public ByteChannel getUnderlying() {
        return this.underlying;
    }

    @Override
    public SSLEngine getSslEngine() {
        return this.impl.engine();
    }

    @Override
    public Consumer<SSLSession> getSessionInitCallback() {
        return this.impl.getSessionInitCallback();
    }

    @Override
    public TrackingAllocator getPlainBufferAllocator() {
        return this.impl.getPlainBufferAllocator();
    }

    @Override
    public TrackingAllocator getEncryptedBufferAllocator() {
        return this.impl.getEncryptedBufferAllocator();
    }

    @Override
    public boolean getRunTasks() {
        return this.impl.getRunTasks();
    }

    @Override
    public long read(ByteBuffer[] dstBuffers, int offset, int length) throws IOException {
        ByteBufferSet dest = new ByteBufferSet(dstBuffers, offset, length);
        TlsChannelImpl.checkReadBuffer(dest);
        return this.impl.read(dest);
    }

    @Override
    public long read(ByteBuffer[] dstBuffers) throws IOException {
        return this.read(dstBuffers, 0, dstBuffers.length);
    }

    @Override
    public int read(ByteBuffer dstBuffer) throws IOException {
        return (int)this.read(new ByteBuffer[]{dstBuffer});
    }

    @Override
    public long write(ByteBuffer[] srcBuffers, int offset, int length) throws IOException {
        ByteBufferSet source = new ByteBufferSet(srcBuffers, offset, length);
        return this.impl.write(source);
    }

    @Override
    public long write(ByteBuffer[] outs) throws IOException {
        return this.write(outs, 0, outs.length);
    }

    @Override
    public int write(ByteBuffer srcBuffer) throws IOException {
        return (int)this.write(new ByteBuffer[]{srcBuffer});
    }

    @Override
    public void renegotiate() throws IOException {
        this.impl.renegotiate();
    }

    @Override
    public void handshake() throws IOException {
        this.impl.handshake();
    }

    @Override
    public void close() throws IOException {
        this.impl.close();
    }

    @Override
    public boolean isOpen() {
        return this.impl.isOpen();
    }

    @Override
    public boolean shutdown() throws IOException {
        return this.impl.shutdown();
    }

    @Override
    public boolean shutdownReceived() {
        return this.impl.shutdownReceived();
    }

    @Override
    public boolean shutdownSent() {
        return this.impl.shutdownSent();
    }

    public static final class Builder
    extends TlsChannelBuilder<Builder> {
        private Supplier<SSLEngine> sslEngineFactory;

        private Builder(ByteChannel underlying, final SSLEngine sslEngine) {
            super(underlying);
            this.sslEngineFactory = new Supplier<SSLEngine>(){

                @Override
                public SSLEngine get() {
                    return sslEngine;
                }
            };
        }

        private Builder(ByteChannel underlying, final SSLContext sslContext) {
            super(underlying);
            this.sslEngineFactory = new Supplier<SSLEngine>(){

                @Override
                public SSLEngine get() {
                    return ClientTlsChannel.defaultSSLEngineFactory(sslContext);
                }
            };
        }

        @Override
        Builder getThis() {
            return this;
        }

        public ClientTlsChannel build() {
            return new ClientTlsChannel(this.underlying, this.sslEngineFactory.get(), this.sessionInitCallback, this.runTasks, this.plainBufferAllocator, this.encryptedBufferAllocator, this.releaseBuffers, this.waitForCloseConfirmation);
        }
    }
}

