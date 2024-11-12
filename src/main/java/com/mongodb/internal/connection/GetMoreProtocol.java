/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoCursorNotFoundException;
import com.mongodb.MongoNamespace;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.connection.ByteBufferBsonOutput;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.connection.QueryResult;
import com.mongodb.diagnostics.logging.Logger;
import com.mongodb.diagnostics.logging.Loggers;
import com.mongodb.event.CommandListener;
import com.mongodb.internal.connection.ByteBufBsonDocument;
import com.mongodb.internal.connection.GetMoreMessage;
import com.mongodb.internal.connection.InternalConnection;
import com.mongodb.internal.connection.LegacyProtocol;
import com.mongodb.internal.connection.NoOpSessionContext;
import com.mongodb.internal.connection.ProtocolHelper;
import com.mongodb.internal.connection.ReplyMessage;
import com.mongodb.internal.connection.ResponseBuffers;
import com.mongodb.internal.connection.ResponseCallback;
import com.mongodb.internal.connection.SendMessageCallback;
import java.util.Collections;
import java.util.List;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonString;
import org.bson.codecs.BsonDocumentCodec;
import org.bson.codecs.Decoder;

class GetMoreProtocol<T>
implements LegacyProtocol<QueryResult<T>> {
    public static final Logger LOGGER = Loggers.getLogger("protocol.getmore");
    private static final String COMMAND_NAME = "getMore";
    private final Decoder<T> resultDecoder;
    private final MongoNamespace namespace;
    private final long cursorId;
    private final int numberToReturn;
    private CommandListener commandListener;

    GetMoreProtocol(MongoNamespace namespace, long cursorId, int numberToReturn, Decoder<T> resultDecoder) {
        this.namespace = namespace;
        this.cursorId = cursorId;
        this.numberToReturn = numberToReturn;
        this.resultDecoder = resultDecoder;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public QueryResult<T> execute(InternalConnection connection) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Getting more documents from namespace %s with cursor %d on connection [%s] to server %s", this.namespace, this.cursorId, connection.getDescription().getConnectionId(), connection.getDescription().getServerAddress()));
        }
        long startTimeNanos = System.nanoTime();
        GetMoreMessage message = new GetMoreMessage(this.namespace.getFullName(), this.cursorId, this.numberToReturn);
        QueryResult<T> result = null;
        try {
            this.sendMessage(message, connection);
            ResponseBuffers responseBuffers = connection.receiveMessage(message.getId());
            try {
                if (responseBuffers.getReplyHeader().isCursorNotFound()) {
                    throw new MongoCursorNotFoundException(message.getCursorId(), connection.getDescription().getServerAddress());
                }
                if (responseBuffers.getReplyHeader().isQueryFailure()) {
                    BsonDocument errorDocument = new ReplyMessage<BsonDocument>(responseBuffers, new BsonDocumentCodec(), message.getId()).getDocuments().get(0);
                    throw ProtocolHelper.getQueryFailureException(errorDocument, connection.getDescription().getServerAddress());
                }
                ReplyMessage<T> replyMessage = new ReplyMessage<T>(responseBuffers, this.resultDecoder, message.getId());
                result = new QueryResult<T>(this.namespace, replyMessage.getDocuments(), replyMessage.getReplyHeader().getCursorId(), connection.getDescription().getServerAddress());
                if (this.commandListener != null) {
                    ProtocolHelper.sendCommandSucceededEvent(message, COMMAND_NAME, this.asGetMoreCommandResponseDocument(result, responseBuffers), connection.getDescription(), System.nanoTime() - startTimeNanos, this.commandListener);
                }
            } finally {
                responseBuffers.close();
            }
            LOGGER.debug("Get-more completed");
            return result;
        } catch (RuntimeException e2) {
            if (this.commandListener != null) {
                ProtocolHelper.sendCommandFailedEvent(message, COMMAND_NAME, connection.getDescription(), System.nanoTime() - startTimeNanos, e2, this.commandListener);
            }
            throw e2;
        }
    }

    @Override
    public void executeAsync(InternalConnection connection, SingleResultCallback<QueryResult<T>> callback) {
        long startTimeNanos = System.nanoTime();
        GetMoreMessage message = new GetMoreMessage(this.namespace.getFullName(), this.cursorId, this.numberToReturn);
        boolean sentStartedEvent = false;
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format("Asynchronously getting more documents from namespace %s with cursor %d on connection [%s] to server %s", this.namespace, this.cursorId, connection.getDescription().getConnectionId(), connection.getDescription().getServerAddress()));
            }
            ByteBufferBsonOutput bsonOutput = new ByteBufferBsonOutput(connection);
            if (this.commandListener != null) {
                ProtocolHelper.sendCommandStartedEvent(message, this.namespace.getDatabaseName(), COMMAND_NAME, this.asGetMoreCommandDocument(), connection.getDescription(), this.commandListener);
                sentStartedEvent = true;
            }
            ProtocolHelper.encodeMessage(message, bsonOutput);
            GetMoreResultCallback receiveCallback = new GetMoreResultCallback(callback, this.cursorId, message, connection.getDescription(), this.commandListener, startTimeNanos);
            connection.sendMessageAsync(bsonOutput.getByteBuffers(), message.getId(), new SendMessageCallback<QueryResult<T>>(connection, bsonOutput, message, COMMAND_NAME, startTimeNanos, this.commandListener, callback, receiveCallback));
        } catch (Throwable t2) {
            if (sentStartedEvent) {
                ProtocolHelper.sendCommandFailedEvent(message, COMMAND_NAME, connection.getDescription(), System.nanoTime() - startTimeNanos, t2, this.commandListener);
            }
            callback.onResult(null, t2);
        }
    }

    @Override
    public void setCommandListener(CommandListener commandListener) {
        this.commandListener = commandListener;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void sendMessage(GetMoreMessage message, InternalConnection connection) {
        ByteBufferBsonOutput bsonOutput = new ByteBufferBsonOutput(connection);
        try {
            if (this.commandListener != null) {
                ProtocolHelper.sendCommandStartedEvent(message, this.namespace.getDatabaseName(), COMMAND_NAME, this.asGetMoreCommandDocument(), connection.getDescription(), this.commandListener);
            }
            message.encode(bsonOutput, NoOpSessionContext.INSTANCE);
            connection.sendMessage(bsonOutput.getByteBuffers(), message.getId());
        } finally {
            bsonOutput.close();
        }
    }

    private BsonDocument asGetMoreCommandDocument() {
        return new BsonDocument(COMMAND_NAME, new BsonInt64(this.cursorId)).append("collection", new BsonString(this.namespace.getCollectionName())).append("batchSize", new BsonInt32(this.numberToReturn));
    }

    private BsonDocument asGetMoreCommandResponseDocument(QueryResult<T> queryResult, ResponseBuffers responseBuffers) {
        List<Object> rawResultDocuments = Collections.emptyList();
        if (responseBuffers.getReplyHeader().getNumberReturned() != 0) {
            responseBuffers.reset();
            rawResultDocuments = ByteBufBsonDocument.createList(responseBuffers);
        }
        BsonDocument cursorDocument = new BsonDocument("id", queryResult.getCursor() == null ? new BsonInt64(0L) : new BsonInt64(queryResult.getCursor().getId())).append("ns", new BsonString(this.namespace.getFullName())).append("nextBatch", new BsonArray(rawResultDocuments));
        return new BsonDocument("cursor", cursorDocument).append("ok", new BsonDouble(1.0));
    }

    class GetMoreResultCallback
    extends ResponseCallback {
        private final SingleResultCallback<QueryResult<T>> callback;
        private final long cursorId;
        private final GetMoreMessage message;
        private final ConnectionDescription connectionDescription;
        private final CommandListener commandListener;
        private final long startTimeNanos;

        GetMoreResultCallback(SingleResultCallback<QueryResult<T>> callback, long cursorId, GetMoreMessage message, ConnectionDescription connectionDescription, CommandListener commandListener, long startTimeNanos) {
            super(message.getId(), connectionDescription.getServerAddress());
            this.callback = callback;
            this.cursorId = cursorId;
            this.message = message;
            this.connectionDescription = connectionDescription;
            this.commandListener = commandListener;
            this.startTimeNanos = startTimeNanos;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        protected void callCallback(ResponseBuffers responseBuffers, Throwable throwableFromCallback) {
            try {
                if (throwableFromCallback != null) {
                    throw throwableFromCallback;
                }
                if (responseBuffers.getReplyHeader().isCursorNotFound()) {
                    throw new MongoCursorNotFoundException(this.cursorId, this.getServerAddress());
                }
                if (responseBuffers.getReplyHeader().isQueryFailure()) {
                    BsonDocument errorDocument = new ReplyMessage<BsonDocument>(responseBuffers, new BsonDocumentCodec(), this.message.getId()).getDocuments().get(0);
                    throw ProtocolHelper.getQueryFailureException(errorDocument, this.connectionDescription.getServerAddress());
                }
                ReplyMessage replyMessage = new ReplyMessage(responseBuffers, GetMoreProtocol.this.resultDecoder, this.getRequestId());
                QueryResult result = new QueryResult(GetMoreProtocol.this.namespace, replyMessage.getDocuments(), replyMessage.getReplyHeader().getCursorId(), this.getServerAddress());
                if (this.commandListener != null) {
                    ProtocolHelper.sendCommandSucceededEvent(this.message, GetMoreProtocol.COMMAND_NAME, GetMoreProtocol.this.asGetMoreCommandResponseDocument(result, responseBuffers), this.connectionDescription, System.nanoTime() - this.startTimeNanos, this.commandListener);
                }
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(String.format("GetMore results received %s documents with cursor %s", result.getResults().size(), result.getCursor()));
                }
                this.callback.onResult(result, null);
            } catch (Throwable t2) {
                if (this.commandListener != null) {
                    ProtocolHelper.sendCommandFailedEvent(this.message, GetMoreProtocol.COMMAND_NAME, this.connectionDescription, System.nanoTime() - this.startTimeNanos, t2, this.commandListener);
                }
                this.callback.onResult(null, t2);
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
    }
}

