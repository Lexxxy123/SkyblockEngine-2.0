/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.internal;

import com.mongodb.ServerAddress;
import com.mongodb.internal.connection.SslHelper;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;

class KeyManagementService {
    private final SSLContext sslContext;
    private final int defaultPort;
    private final int timeoutMillis;

    KeyManagementService(SSLContext sslContext, int defaultPort, int timeoutMillis) {
        this.sslContext = sslContext;
        this.defaultPort = defaultPort;
        this.timeoutMillis = timeoutMillis;
    }

    public InputStream stream(String host, ByteBuffer message) throws IOException {
        ServerAddress serverAddress = host.contains(":") ? new ServerAddress(host) : new ServerAddress(host, this.defaultPort);
        SSLSocket socket = (SSLSocket)this.sslContext.getSocketFactory().createSocket();
        try {
            this.enableHostNameVerification(socket);
            socket.setSoTimeout(this.timeoutMillis);
            socket.connect(serverAddress.getSocketAddress(), this.timeoutMillis);
        } catch (IOException e2) {
            this.closeSocket(socket);
            throw e2;
        }
        try {
            OutputStream outputStream = socket.getOutputStream();
            byte[] bytes = new byte[message.remaining()];
            message.get(bytes);
            outputStream.write(bytes);
        } catch (IOException e3) {
            this.closeSocket(socket);
            throw e3;
        }
        try {
            return socket.getInputStream();
        } catch (IOException e4) {
            this.closeSocket(socket);
            throw e4;
        }
    }

    private void enableHostNameVerification(SSLSocket socket) {
        SSLParameters sslParameters = socket.getSSLParameters();
        if (sslParameters == null) {
            sslParameters = new SSLParameters();
        }
        SslHelper.enableHostNameVerification(sslParameters);
        socket.setSSLParameters(sslParameters);
    }

    public int getDefaultPort() {
        return this.defaultPort;
    }

    private void closeSocket(Socket socket) {
        try {
            socket.close();
        } catch (IOException iOException) {
            // empty catch block
        }
    }
}

