/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  jnr.unixsocket.UnixSocketAddress
 *  jnr.unixsocket.UnixSocketChannel
 */
package com.mongodb.internal.connection;

import com.mongodb.UnixServerAddress;
import com.mongodb.connection.BufferProvider;
import com.mongodb.connection.SocketSettings;
import com.mongodb.connection.SslSettings;
import com.mongodb.internal.connection.SocketStream;
import java.io.IOException;
import java.net.Socket;
import javax.net.SocketFactory;
import jnr.unixsocket.UnixSocketAddress;
import jnr.unixsocket.UnixSocketChannel;

public class UnixSocketChannelStream
extends SocketStream {
    private final UnixServerAddress address;

    public UnixSocketChannelStream(UnixServerAddress address, SocketSettings settings, SslSettings sslSettings, BufferProvider bufferProvider) {
        super(address, settings, sslSettings, SocketFactory.getDefault(), bufferProvider);
        this.address = address;
    }

    @Override
    protected Socket initializeSocket() throws IOException {
        return UnixSocketChannel.open((UnixSocketAddress)((UnixSocketAddress)this.address.getUnixSocketAddress())).socket();
    }
}

