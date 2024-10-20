/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.ServerAddress;
import com.mongodb.diagnostics.logging.Logger;
import com.mongodb.diagnostics.logging.Loggers;
import com.mongodb.internal.connection.ReplyMessage;
import com.mongodb.internal.connection.ResponseBuffers;
import com.mongodb.internal.connection.ResponseCallback;
import org.bson.codecs.Decoder;

abstract class CommandResultBaseCallback<T>
extends ResponseCallback {
    public static final Logger LOGGER = Loggers.getLogger("protocol.command");
    private final Decoder<T> decoder;

    CommandResultBaseCallback(Decoder<T> decoder, long requestId, ServerAddress serverAddress) {
        super(requestId, serverAddress);
        this.decoder = decoder;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    protected void callCallback(ResponseBuffers responseBuffers, Throwable t2) {
        try {
            if (t2 != null || responseBuffers == null) {
                this.callCallback((T)null, t2);
            } else {
                ReplyMessage<T> replyMessage = new ReplyMessage<T>(responseBuffers, this.decoder, this.getRequestId());
                this.callCallback(replyMessage.getDocuments().get(0), null);
            }
        } finally {
            try {
                if (responseBuffers != null) {
                    responseBuffers.close();
                }
            } catch (Throwable t1) {
                LOGGER.debug("GetMore ResponseBuffer close exception", t1);
            }
        }
    }

    protected abstract void callCallback(T var1, Throwable var2);
}

