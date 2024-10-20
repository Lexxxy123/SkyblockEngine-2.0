/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoInternalException;
import com.mongodb.ServerAddress;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.internal.connection.ResponseBuffers;

abstract class ResponseCallback
implements SingleResultCallback<ResponseBuffers> {
    private volatile boolean closed;
    private final ServerAddress serverAddress;
    private final long requestId;

    ResponseCallback(long requestId, ServerAddress serverAddress) {
        this.serverAddress = serverAddress;
        this.requestId = requestId;
    }

    protected ServerAddress getServerAddress() {
        return this.serverAddress;
    }

    protected long getRequestId() {
        return this.requestId;
    }

    @Override
    public void onResult(ResponseBuffers responseBuffers, Throwable t2) {
        if (this.closed) {
            throw new MongoInternalException("Callback should not be invoked more than once");
        }
        this.closed = true;
        if (responseBuffers != null) {
            this.callCallback(responseBuffers, t2);
        } else {
            this.callCallback(null, t2);
        }
    }

    protected abstract void callCallback(ResponseBuffers var1, Throwable var2);
}

