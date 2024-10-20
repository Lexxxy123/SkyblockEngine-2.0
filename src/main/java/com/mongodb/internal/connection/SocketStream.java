/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoSocketException;
import com.mongodb.MongoSocketOpenException;
import com.mongodb.MongoSocketReadException;
import com.mongodb.ServerAddress;
import com.mongodb.assertions.Assertions;
import com.mongodb.connection.AsyncCompletionHandler;
import com.mongodb.connection.BufferProvider;
import com.mongodb.connection.SocketSettings;
import com.mongodb.connection.SslSettings;
import com.mongodb.connection.Stream;
import com.mongodb.internal.connection.SocketStreamHelper;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Iterator;
import java.util.List;
import javax.net.SocketFactory;
import org.bson.ByteBuf;

public class SocketStream
implements Stream {
    private final ServerAddress address;
    private final SocketSettings settings;
    private final SslSettings sslSettings;
    private final SocketFactory socketFactory;
    private final BufferProvider bufferProvider;
    private volatile Socket socket;
    private volatile OutputStream outputStream;
    private volatile InputStream inputStream;
    private volatile boolean isClosed;

    public SocketStream(ServerAddress address, SocketSettings settings, SslSettings sslSettings, SocketFactory socketFactory, BufferProvider bufferProvider) {
        this.address = Assertions.notNull("address", address);
        this.settings = Assertions.notNull("settings", settings);
        this.sslSettings = Assertions.notNull("sslSettings", sslSettings);
        this.socketFactory = Assertions.notNull("socketFactory", socketFactory);
        this.bufferProvider = Assertions.notNull("bufferProvider", bufferProvider);
    }

    @Override
    public void open() {
        try {
            this.socket = this.initializeSocket();
            this.outputStream = this.socket.getOutputStream();
            this.inputStream = this.socket.getInputStream();
        } catch (IOException e2) {
            this.close();
            throw new MongoSocketOpenException("Exception opening socket", this.getAddress(), (Throwable)e2);
        }
    }

    protected Socket initializeSocket() throws IOException {
        Iterator<InetSocketAddress> inetSocketAddresses = this.address.getSocketAddresses().iterator();
        while (inetSocketAddresses.hasNext()) {
            Socket socket = this.socketFactory.createSocket();
            try {
                SocketStreamHelper.initialize(socket, inetSocketAddresses.next(), this.settings, this.sslSettings);
                return socket;
            } catch (SocketTimeoutException e2) {
                if (inetSocketAddresses.hasNext()) continue;
                throw e2;
            }
        }
        throw new MongoSocketException("Exception opening socket", this.getAddress());
    }

    @Override
    public ByteBuf getBuffer(int size) {
        return this.bufferProvider.getBuffer(size);
    }

    @Override
    public void write(List<ByteBuf> buffers) throws IOException {
        for (ByteBuf cur : buffers) {
            this.outputStream.write(cur.array(), 0, cur.limit());
        }
    }

    @Override
    public ByteBuf read(int numBytes) throws IOException {
        int bytesRead;
        ByteBuf buffer = this.bufferProvider.getBuffer(numBytes);
        byte[] bytes = buffer.array();
        for (int totalBytesRead = 0; totalBytesRead < buffer.limit(); totalBytesRead += bytesRead) {
            bytesRead = this.inputStream.read(bytes, totalBytesRead, buffer.limit() - totalBytesRead);
            if (bytesRead != -1) continue;
            buffer.release();
            throw new MongoSocketReadException("Prematurely reached end of stream", this.getAddress());
        }
        return buffer;
    }

    @Override
    public void openAsync(AsyncCompletionHandler<Void> handler) {
        throw new UnsupportedOperationException(this.getClass() + " does not support asynchronous operations.");
    }

    @Override
    public void writeAsync(List<ByteBuf> buffers, AsyncCompletionHandler<Void> handler) {
        throw new UnsupportedOperationException(this.getClass() + " does not support asynchronous operations.");
    }

    @Override
    public void readAsync(int numBytes, AsyncCompletionHandler<ByteBuf> handler) {
        throw new UnsupportedOperationException(this.getClass() + " does not support asynchronous operations.");
    }

    @Override
    public ServerAddress getAddress() {
        return this.address;
    }

    SocketSettings getSettings() {
        return this.settings;
    }

    @Override
    public void close() {
        try {
            this.isClosed = true;
            if (this.socket != null) {
                this.socket.close();
            }
        } catch (IOException iOException) {
            // empty catch block
        }
    }

    @Override
    public boolean isClosed() {
        return this.isClosed;
    }
}

