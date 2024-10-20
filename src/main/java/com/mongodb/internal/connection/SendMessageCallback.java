/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.async.SingleResultCallback;
import com.mongodb.event.CommandListener;
import com.mongodb.internal.connection.InternalConnection;
import com.mongodb.internal.connection.ProtocolHelper;
import com.mongodb.internal.connection.RequestMessage;
import com.mongodb.internal.connection.ResponseBuffers;
import org.bson.io.OutputBuffer;

class SendMessageCallback<T>
implements SingleResultCallback<Void> {
    private final OutputBuffer buffer;
    private final InternalConnection connection;
    private final SingleResultCallback<ResponseBuffers> receiveMessageCallback;
    private final int requestId;
    private final RequestMessage message;
    private final CommandListener commandListener;
    private final long startTimeNanos;
    private final SingleResultCallback<T> callback;
    private final String commandName;

    SendMessageCallback(InternalConnection connection, OutputBuffer buffer, RequestMessage message, String commandName, long startTimeNanos, CommandListener commandListener, SingleResultCallback<T> callback, SingleResultCallback<ResponseBuffers> receiveMessageCallback) {
        this(connection, buffer, message, message.getId(), commandName, startTimeNanos, commandListener, callback, receiveMessageCallback);
    }

    SendMessageCallback(InternalConnection connection, OutputBuffer buffer, RequestMessage message, int requestId, String commandName, long startTimeNanos, CommandListener commandListener, SingleResultCallback<T> callback, SingleResultCallback<ResponseBuffers> receiveMessageCallback) {
        this.buffer = buffer;
        this.connection = connection;
        this.message = message;
        this.commandName = commandName;
        this.commandListener = commandListener;
        this.startTimeNanos = startTimeNanos;
        this.callback = callback;
        this.receiveMessageCallback = receiveMessageCallback;
        this.requestId = requestId;
    }

    @Override
    public void onResult(Void result, Throwable t2) {
        this.buffer.close();
        if (t2 != null) {
            if (this.commandListener != null) {
                ProtocolHelper.sendCommandFailedEvent(this.message, this.commandName, this.connection.getDescription(), System.nanoTime() - this.startTimeNanos, t2, this.commandListener);
            }
            this.callback.onResult(null, t2);
        } else {
            this.connection.receiveMessageAsync(this.requestId, this.receiveMessageCallback);
        }
    }
}

