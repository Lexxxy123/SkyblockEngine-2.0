/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoInternalException;
import com.mongodb.connection.SocketSettings;
import com.mongodb.connection.SslSettings;
import com.mongodb.internal.connection.SslHelper;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;

final class SocketStreamHelper {
    static void initialize(Socket socket, InetSocketAddress inetSocketAddress, SocketSettings settings, SslSettings sslSettings) throws IOException {
        socket.setTcpNoDelay(true);
        socket.setSoTimeout(settings.getReadTimeout(TimeUnit.MILLISECONDS));
        socket.setKeepAlive(settings.isKeepAlive());
        if (settings.getReceiveBufferSize() > 0) {
            socket.setReceiveBufferSize(settings.getReceiveBufferSize());
        }
        if (settings.getSendBufferSize() > 0) {
            socket.setSendBufferSize(settings.getSendBufferSize());
        }
        if (sslSettings.isEnabled() || socket instanceof SSLSocket) {
            if (!(socket instanceof SSLSocket)) {
                throw new MongoInternalException("SSL is enabled but the socket is not an instance of javax.net.ssl.SSLSocket");
            }
            SSLSocket sslSocket = (SSLSocket)socket;
            SSLParameters sslParameters = sslSocket.getSSLParameters();
            if (sslParameters == null) {
                sslParameters = new SSLParameters();
            }
            SslHelper.enableSni(inetSocketAddress.getHostName(), sslParameters);
            if (!sslSettings.isInvalidHostNameAllowed()) {
                SslHelper.enableHostNameVerification(sslParameters);
            }
            sslSocket.setSSLParameters(sslParameters);
        }
        socket.connect(inetSocketAddress, settings.getConnectTimeout(TimeUnit.MILLISECONDS));
    }

    private SocketStreamHelper() {
    }
}

