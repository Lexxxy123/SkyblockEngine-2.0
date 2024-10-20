/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection.tlschannel;

import com.mongodb.internal.connection.tlschannel.TrackingAllocator;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.util.function.Consumer;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;

public interface TlsChannel
extends ByteChannel,
GatheringByteChannel,
ScatteringByteChannel {
    public ByteChannel getUnderlying();

    public SSLEngine getSslEngine();

    public Consumer<SSLSession> getSessionInitCallback();

    public TrackingAllocator getPlainBufferAllocator();

    public TrackingAllocator getEncryptedBufferAllocator();

    public boolean getRunTasks();

    @Override
    public int read(ByteBuffer var1) throws IOException;

    @Override
    public int write(ByteBuffer var1) throws IOException;

    public void renegotiate() throws IOException;

    public void handshake() throws IOException;

    @Override
    public long write(ByteBuffer[] var1, int var2, int var3) throws IOException;

    @Override
    public long write(ByteBuffer[] var1) throws IOException;

    @Override
    public long read(ByteBuffer[] var1, int var2, int var3) throws IOException;

    @Override
    public long read(ByteBuffer[] var1) throws IOException;

    @Override
    public void close() throws IOException;

    public boolean shutdown() throws IOException;

    public boolean shutdownReceived();

    public boolean shutdownSent();
}

