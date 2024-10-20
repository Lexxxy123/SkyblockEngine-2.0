/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.connection;

import com.mongodb.MongoClientException;
import com.mongodb.ServerAddress;
import com.mongodb.UnixServerAddress;
import com.mongodb.assertions.Assertions;
import com.mongodb.connection.BufferProvider;
import com.mongodb.connection.SocketSettings;
import com.mongodb.connection.SslSettings;
import com.mongodb.connection.Stream;
import com.mongodb.connection.StreamFactory;
import com.mongodb.internal.connection.PowerOfTwoBufferPool;
import com.mongodb.internal.connection.SocketStream;
import com.mongodb.internal.connection.UnixSocketChannelStream;
import java.security.NoSuchAlgorithmException;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;

public class SocketStreamFactory
implements StreamFactory {
    private final SocketSettings settings;
    private final SslSettings sslSettings;
    private final SocketFactory socketFactory;
    private final BufferProvider bufferProvider = new PowerOfTwoBufferPool();

    public SocketStreamFactory(SocketSettings settings, SslSettings sslSettings) {
        this(settings, sslSettings, null);
    }

    public SocketStreamFactory(SocketSettings settings, SslSettings sslSettings, SocketFactory socketFactory) {
        this.settings = Assertions.notNull("settings", settings);
        this.sslSettings = Assertions.notNull("sslSettings", sslSettings);
        this.socketFactory = socketFactory;
    }

    @Override
    public Stream create(ServerAddress serverAddress) {
        SocketStream stream;
        if (serverAddress instanceof UnixServerAddress) {
            if (this.sslSettings.isEnabled()) {
                throw new MongoClientException("Socket based connections do not support ssl");
            }
            stream = new UnixSocketChannelStream((UnixServerAddress)serverAddress, this.settings, this.sslSettings, this.bufferProvider);
        } else {
            stream = this.socketFactory != null ? new SocketStream(serverAddress, this.settings, this.sslSettings, this.socketFactory, this.bufferProvider) : (this.sslSettings.isEnabled() ? new SocketStream(serverAddress, this.settings, this.sslSettings, this.getSslContext().getSocketFactory(), this.bufferProvider) : new SocketStream(serverAddress, this.settings, this.sslSettings, SocketFactory.getDefault(), this.bufferProvider));
        }
        return stream;
    }

    private SSLContext getSslContext() {
        try {
            return this.sslSettings.getContext() == null ? SSLContext.getDefault() : this.sslSettings.getContext();
        } catch (NoSuchAlgorithmException e2) {
            throw new MongoClientException("Unable to create default SSLContext", e2);
        }
    }
}

