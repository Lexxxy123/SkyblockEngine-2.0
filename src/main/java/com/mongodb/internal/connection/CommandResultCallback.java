/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.ServerAddress;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.diagnostics.logging.Logger;
import com.mongodb.diagnostics.logging.Loggers;
import com.mongodb.internal.connection.CommandResultBaseCallback;
import com.mongodb.internal.connection.ProtocolHelper;
import org.bson.BsonDocument;
import org.bson.BsonDocumentReader;
import org.bson.codecs.BsonDocumentCodec;
import org.bson.codecs.Decoder;
import org.bson.codecs.DecoderContext;

class CommandResultCallback<T>
extends CommandResultBaseCallback<BsonDocument> {
    public static final Logger LOGGER = Loggers.getLogger("protocol.command");
    private final SingleResultCallback<T> callback;
    private final Decoder<T> decoder;

    CommandResultCallback(SingleResultCallback<T> callback, Decoder<T> decoder, long requestId, ServerAddress serverAddress) {
        super(new BsonDocumentCodec(), requestId, serverAddress);
        this.callback = callback;
        this.decoder = decoder;
    }

    @Override
    protected void callCallback(BsonDocument response, Throwable t2) {
        if (t2 != null) {
            this.callback.onResult(null, t2);
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Command execution completed with status " + ProtocolHelper.isCommandOk(response));
            }
            if (!ProtocolHelper.isCommandOk(response)) {
                this.callback.onResult(null, ProtocolHelper.getCommandFailureException(response, this.getServerAddress()));
            } else {
                try {
                    this.callback.onResult(this.decoder.decode(new BsonDocumentReader(response), DecoderContext.builder().build()), null);
                } catch (Throwable t1) {
                    this.callback.onResult(null, t1);
                }
            }
        }
    }
}

