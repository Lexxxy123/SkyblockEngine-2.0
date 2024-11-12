/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoInternalException;
import com.mongodb.MongoNamespace;
import com.mongodb.WriteConcern;
import com.mongodb.WriteConcernResult;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.connection.ByteBufferBsonOutput;
import com.mongodb.diagnostics.logging.Logger;
import com.mongodb.event.CommandListener;
import com.mongodb.internal.connection.InternalConnection;
import com.mongodb.internal.connection.LegacyProtocol;
import com.mongodb.internal.connection.MessageSettings;
import com.mongodb.internal.connection.NoOpSessionContext;
import com.mongodb.internal.connection.ProtocolHelper;
import com.mongodb.internal.connection.RequestMessage;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;
import org.bson.io.OutputBuffer;

abstract class WriteProtocol
implements LegacyProtocol<WriteConcernResult> {
    private final MongoNamespace namespace;
    private final boolean ordered;
    private CommandListener commandListener;

    WriteProtocol(MongoNamespace namespace, boolean ordered) {
        this.namespace = namespace;
        this.ordered = ordered;
    }

    @Override
    public void setCommandListener(CommandListener commandListener) {
        this.commandListener = commandListener;
    }

    @Override
    public WriteConcernResult execute(InternalConnection connection) {
        RequestMessage requestMessage = null;
        long startTimeNanos = System.nanoTime();
        boolean sentCommandStartedEvent = false;
        ByteBufferBsonOutput bsonOutput = new ByteBufferBsonOutput(connection);
        try {
            requestMessage = this.createRequestMessage(ProtocolHelper.getMessageSettings(connection.getDescription()));
            requestMessage.encode(bsonOutput, NoOpSessionContext.INSTANCE);
            this.sendStartedEvent(connection, requestMessage, requestMessage.getEncodingMetadata(), bsonOutput);
            sentCommandStartedEvent = true;
            int messageId = requestMessage.getId();
            connection.sendMessage(bsonOutput.getByteBuffers(), messageId);
        } catch (RuntimeException e2) {
            this.sendFailedEvent(connection, requestMessage, sentCommandStartedEvent, e2, startTimeNanos);
            throw e2;
        } finally {
            bsonOutput.close();
        }
        this.sendSucceededEvent(connection, requestMessage, startTimeNanos);
        return WriteConcernResult.unacknowledged();
    }

    @Override
    public void executeAsync(InternalConnection connection, SingleResultCallback<WriteConcernResult> callback) {
        this.executeAsync(this.createRequestMessage(ProtocolHelper.getMessageSettings(connection.getDescription())), connection, callback);
    }

    private void executeAsync(RequestMessage requestMessage, InternalConnection connection, SingleResultCallback<WriteConcernResult> callback) {
        long startTimeNanos = System.nanoTime();
        boolean sentCommandStartedEvent = false;
        try {
            ByteBufferBsonOutput bsonOutput = new ByteBufferBsonOutput(connection);
            RequestMessage.EncodingMetadata encodingMetadata = ProtocolHelper.encodeMessageWithMetadata(requestMessage, bsonOutput);
            this.sendStartedEvent(connection, requestMessage, encodingMetadata, bsonOutput);
            sentCommandStartedEvent = true;
            connection.sendMessageAsync(bsonOutput.getByteBuffers(), requestMessage.getId(), new UnacknowledgedWriteResultCallback(callback, requestMessage, bsonOutput, connection, startTimeNanos));
        } catch (Throwable t2) {
            this.sendFailedEvent(connection, requestMessage, sentCommandStartedEvent, t2, startTimeNanos);
            callback.onResult(null, t2);
        }
    }

    protected abstract BsonDocument getAsWriteCommand(ByteBufferBsonOutput var1, int var2);

    protected BsonDocument getBaseCommandDocument(String commandName) {
        BsonDocument baseCommandDocument = new BsonDocument(commandName, new BsonString(this.getNamespace().getCollectionName())).append("ordered", BsonBoolean.valueOf(this.isOrdered()));
        baseCommandDocument.append("writeConcern", WriteConcern.UNACKNOWLEDGED.asDocument());
        return baseCommandDocument;
    }

    protected String getCommandName(RequestMessage message) {
        switch (message.getOpCode()) {
            case OP_INSERT: {
                return "insert";
            }
            case OP_UPDATE: {
                return "update";
            }
            case OP_DELETE: {
                return "delete";
            }
        }
        throw new MongoInternalException("Unexpected op code for write: " + (Object)((Object)message.getOpCode()));
    }

    private void sendStartedEvent(InternalConnection connection, RequestMessage message, RequestMessage.EncodingMetadata encodingMetadata, ByteBufferBsonOutput bsonOutput) {
        if (this.commandListener != null) {
            ProtocolHelper.sendCommandStartedEvent(message, this.namespace.getDatabaseName(), this.getCommandName(message), this.getAsWriteCommand(bsonOutput, encodingMetadata.getFirstDocumentPosition()), connection.getDescription(), this.commandListener);
        }
    }

    private void sendSucceededEvent(InternalConnection connection, RequestMessage message, long startTimeNanos) {
        if (this.commandListener != null) {
            this.sendSucceededEvent(connection, message, this.getResponseDocument(), startTimeNanos);
        }
    }

    private void sendSucceededEvent(InternalConnection connection, RequestMessage message, BsonDocument responseDocument, long startTimeNanos) {
        if (this.commandListener != null) {
            ProtocolHelper.sendCommandSucceededEvent(message, this.getCommandName(message), responseDocument, connection.getDescription(), System.nanoTime() - startTimeNanos, this.commandListener);
        }
    }

    private void sendFailedEvent(InternalConnection connection, RequestMessage message, boolean sentCommandStartedEvent, Throwable t2, long startTimeNanos) {
        if (this.commandListener != null && sentCommandStartedEvent) {
            ProtocolHelper.sendCommandFailedEvent(message, this.getCommandName(message), connection.getDescription(), System.nanoTime() - startTimeNanos, t2, this.commandListener);
        }
    }

    private BsonDocument getResponseDocument() {
        return new BsonDocument("ok", new BsonInt32(1));
    }

    protected abstract RequestMessage createRequestMessage(MessageSettings var1);

    protected MongoNamespace getNamespace() {
        return this.namespace;
    }

    protected boolean isOrdered() {
        return this.ordered;
    }

    protected abstract Logger getLogger();

    private final class UnacknowledgedWriteResultCallback
    implements SingleResultCallback<Void> {
        private final SingleResultCallback<WriteConcernResult> callback;
        private final RequestMessage message;
        private final OutputBuffer writtenBuffer;
        private final InternalConnection connection;
        private final long startTimeNanos;

        UnacknowledgedWriteResultCallback(SingleResultCallback<WriteConcernResult> callback, RequestMessage message, OutputBuffer writtenBuffer, InternalConnection connection, long startTimeNanos) {
            this.callback = callback;
            this.message = message;
            this.connection = connection;
            this.writtenBuffer = writtenBuffer;
            this.startTimeNanos = startTimeNanos;
        }

        @Override
        public void onResult(Void result, Throwable t2) {
            this.writtenBuffer.close();
            if (t2 != null) {
                WriteProtocol.this.sendFailedEvent(this.connection, this.message, true, t2, this.startTimeNanos);
                this.callback.onResult(null, t2);
            } else {
                WriteProtocol.this.sendSucceededEvent(this.connection, this.message, this.startTimeNanos);
                this.callback.onResult(WriteConcernResult.unacknowledged(), null);
            }
        }
    }
}

