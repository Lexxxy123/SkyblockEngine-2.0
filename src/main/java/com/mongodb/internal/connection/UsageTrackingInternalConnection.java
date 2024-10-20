/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.async.SingleResultCallback;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.diagnostics.logging.Logger;
import com.mongodb.diagnostics.logging.Loggers;
import com.mongodb.internal.async.ErrorHandlingResultCallback;
import com.mongodb.internal.connection.CommandMessage;
import com.mongodb.internal.connection.InternalConnection;
import com.mongodb.internal.connection.ResponseBuffers;
import com.mongodb.session.SessionContext;
import java.util.List;
import org.bson.ByteBuf;
import org.bson.codecs.Decoder;

class UsageTrackingInternalConnection
implements InternalConnection {
    private static final Logger LOGGER = Loggers.getLogger("connection");
    private volatile long openedAt;
    private volatile long lastUsedAt;
    private final int generation;
    private final InternalConnection wrapped;

    UsageTrackingInternalConnection(InternalConnection wrapped, int generation) {
        this.wrapped = wrapped;
        this.generation = generation;
        this.lastUsedAt = this.openedAt = Long.MAX_VALUE;
    }

    @Override
    public void open() {
        this.wrapped.open();
        this.lastUsedAt = this.openedAt = System.currentTimeMillis();
    }

    @Override
    public void openAsync(final SingleResultCallback<Void> callback) {
        this.wrapped.openAsync(new SingleResultCallback<Void>(){

            @Override
            public void onResult(Void result, Throwable t2) {
                if (t2 != null) {
                    callback.onResult(null, t2);
                } else {
                    UsageTrackingInternalConnection.this.openedAt = System.currentTimeMillis();
                    UsageTrackingInternalConnection.this.lastUsedAt = UsageTrackingInternalConnection.this.openedAt;
                    callback.onResult(null, null);
                }
            }
        });
    }

    @Override
    public void close() {
        this.wrapped.close();
    }

    @Override
    public boolean opened() {
        return this.wrapped.opened();
    }

    @Override
    public boolean isClosed() {
        return this.wrapped.isClosed();
    }

    @Override
    public ByteBuf getBuffer(int size) {
        return this.wrapped.getBuffer(size);
    }

    @Override
    public void sendMessage(List<ByteBuf> byteBuffers, int lastRequestId) {
        this.wrapped.sendMessage(byteBuffers, lastRequestId);
        this.lastUsedAt = System.currentTimeMillis();
    }

    @Override
    public <T> T sendAndReceive(CommandMessage message, Decoder<T> decoder, SessionContext sessionContext) {
        T result = this.wrapped.sendAndReceive(message, decoder, sessionContext);
        this.lastUsedAt = System.currentTimeMillis();
        return result;
    }

    @Override
    public <T> void sendAndReceiveAsync(CommandMessage message, Decoder<T> decoder, SessionContext sessionContext, final SingleResultCallback<T> callback) {
        SingleResultCallback errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(new SingleResultCallback<T>(){

            @Override
            public void onResult(T result, Throwable t2) {
                UsageTrackingInternalConnection.this.lastUsedAt = System.currentTimeMillis();
                callback.onResult(result, t2);
            }
        }, LOGGER);
        this.wrapped.sendAndReceiveAsync(message, decoder, sessionContext, errHandlingCallback);
    }

    @Override
    public ResponseBuffers receiveMessage(int responseTo) {
        ResponseBuffers responseBuffers = this.wrapped.receiveMessage(responseTo);
        this.lastUsedAt = System.currentTimeMillis();
        return responseBuffers;
    }

    @Override
    public void sendMessageAsync(List<ByteBuf> byteBuffers, int lastRequestId, final SingleResultCallback<Void> callback) {
        SingleResultCallback<Void> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(new SingleResultCallback<Void>(){

            @Override
            public void onResult(Void result, Throwable t2) {
                UsageTrackingInternalConnection.this.lastUsedAt = System.currentTimeMillis();
                callback.onResult(result, t2);
            }
        }, LOGGER);
        this.wrapped.sendMessageAsync(byteBuffers, lastRequestId, errHandlingCallback);
    }

    @Override
    public void receiveMessageAsync(int responseTo, final SingleResultCallback<ResponseBuffers> callback) {
        SingleResultCallback<ResponseBuffers> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(new SingleResultCallback<ResponseBuffers>(){

            @Override
            public void onResult(ResponseBuffers result, Throwable t2) {
                UsageTrackingInternalConnection.this.lastUsedAt = System.currentTimeMillis();
                callback.onResult(result, t2);
            }
        }, LOGGER);
        this.wrapped.receiveMessageAsync(responseTo, errHandlingCallback);
    }

    @Override
    public ConnectionDescription getDescription() {
        return this.wrapped.getDescription();
    }

    int getGeneration() {
        return this.generation;
    }

    long getOpenedAt() {
        return this.openedAt;
    }

    long getLastUsedAt() {
        return this.lastUsedAt;
    }
}

