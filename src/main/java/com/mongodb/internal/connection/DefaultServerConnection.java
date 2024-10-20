/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoNamespace;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcernResult;
import com.mongodb.assertions.Assertions;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.bulk.DeleteRequest;
import com.mongodb.bulk.InsertRequest;
import com.mongodb.bulk.UpdateRequest;
import com.mongodb.connection.AsyncConnection;
import com.mongodb.connection.ClusterConnectionMode;
import com.mongodb.connection.Connection;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.connection.QueryResult;
import com.mongodb.connection.ServerType;
import com.mongodb.connection.SplittablePayload;
import com.mongodb.diagnostics.logging.Logger;
import com.mongodb.diagnostics.logging.Loggers;
import com.mongodb.internal.async.ErrorHandlingResultCallback;
import com.mongodb.internal.connection.AbstractReferenceCounted;
import com.mongodb.internal.connection.CommandProtocol;
import com.mongodb.internal.connection.CommandProtocolImpl;
import com.mongodb.internal.connection.DeleteProtocol;
import com.mongodb.internal.connection.GetMoreProtocol;
import com.mongodb.internal.connection.InsertProtocol;
import com.mongodb.internal.connection.InternalConnection;
import com.mongodb.internal.connection.KillCursorProtocol;
import com.mongodb.internal.connection.LegacyProtocol;
import com.mongodb.internal.connection.NoOpSessionContext;
import com.mongodb.internal.connection.ProtocolExecutor;
import com.mongodb.internal.connection.QueryProtocol;
import com.mongodb.internal.connection.UpdateProtocol;
import com.mongodb.session.SessionContext;
import java.util.List;
import org.bson.BsonDocument;
import org.bson.FieldNameValidator;
import org.bson.codecs.Decoder;

public class DefaultServerConnection
extends AbstractReferenceCounted
implements Connection,
AsyncConnection {
    private static final Logger LOGGER = Loggers.getLogger("connection");
    private final InternalConnection wrapped;
    private final ProtocolExecutor protocolExecutor;
    private final ClusterConnectionMode clusterConnectionMode;

    public DefaultServerConnection(InternalConnection wrapped, ProtocolExecutor protocolExecutor, ClusterConnectionMode clusterConnectionMode) {
        this.wrapped = wrapped;
        this.protocolExecutor = protocolExecutor;
        this.clusterConnectionMode = clusterConnectionMode;
    }

    @Override
    public DefaultServerConnection retain() {
        super.retain();
        return this;
    }

    @Override
    public void release() {
        super.release();
        if (this.getCount() == 0) {
            this.wrapped.close();
        }
    }

    @Override
    public ConnectionDescription getDescription() {
        Assertions.isTrue("open", this.getCount() > 0);
        return this.wrapped.getDescription();
    }

    @Override
    public WriteConcernResult insert(MongoNamespace namespace, boolean ordered, InsertRequest insertRequest) {
        return this.executeProtocol(new InsertProtocol(namespace, ordered, insertRequest));
    }

    @Override
    public void insertAsync(MongoNamespace namespace, boolean ordered, InsertRequest insertRequest, SingleResultCallback<WriteConcernResult> callback) {
        this.executeProtocolAsync(new InsertProtocol(namespace, ordered, insertRequest), callback);
    }

    @Override
    public WriteConcernResult update(MongoNamespace namespace, boolean ordered, UpdateRequest updateRequest) {
        return this.executeProtocol(new UpdateProtocol(namespace, ordered, updateRequest));
    }

    @Override
    public void updateAsync(MongoNamespace namespace, boolean ordered, UpdateRequest updateRequest, SingleResultCallback<WriteConcernResult> callback) {
        this.executeProtocolAsync(new UpdateProtocol(namespace, ordered, updateRequest), callback);
    }

    @Override
    public WriteConcernResult delete(MongoNamespace namespace, boolean ordered, DeleteRequest deleteRequest) {
        return this.executeProtocol(new DeleteProtocol(namespace, ordered, deleteRequest));
    }

    @Override
    public void deleteAsync(MongoNamespace namespace, boolean ordered, DeleteRequest deleteRequest, SingleResultCallback<WriteConcernResult> callback) {
        this.executeProtocolAsync(new DeleteProtocol(namespace, ordered, deleteRequest), callback);
    }

    @Override
    public <T> T command(String database, BsonDocument command, boolean slaveOk, FieldNameValidator fieldNameValidator, Decoder<T> commandResultDecoder) {
        return this.command(database, command, fieldNameValidator, this.getReadPreferenceFromSlaveOk(slaveOk), commandResultDecoder, NoOpSessionContext.INSTANCE);
    }

    @Override
    public <T> T command(String database, BsonDocument command, FieldNameValidator fieldNameValidator, ReadPreference readPreference, Decoder<T> commandResultDecoder, SessionContext sessionContext) {
        return this.command(database, command, fieldNameValidator, readPreference, commandResultDecoder, sessionContext, true, null, null);
    }

    @Override
    public <T> T command(String database, BsonDocument command, FieldNameValidator commandFieldNameValidator, ReadPreference readPreference, Decoder<T> commandResultDecoder, SessionContext sessionContext, boolean responseExpected, SplittablePayload payload, FieldNameValidator payloadFieldNameValidator) {
        return this.executeProtocol(new CommandProtocolImpl<T>(database, command, commandFieldNameValidator, readPreference, commandResultDecoder, responseExpected, payload, payloadFieldNameValidator, this.clusterConnectionMode), sessionContext);
    }

    @Override
    public <T> void commandAsync(String database, BsonDocument command, boolean slaveOk, FieldNameValidator fieldNameValidator, Decoder<T> commandResultDecoder, SingleResultCallback<T> callback) {
        this.commandAsync(database, command, fieldNameValidator, this.getReadPreferenceFromSlaveOk(slaveOk), commandResultDecoder, NoOpSessionContext.INSTANCE, callback);
    }

    @Override
    public <T> void commandAsync(String database, BsonDocument command, FieldNameValidator fieldNameValidator, ReadPreference readPreference, Decoder<T> commandResultDecoder, SessionContext sessionContext, SingleResultCallback<T> callback) {
        this.commandAsync(database, command, fieldNameValidator, readPreference, commandResultDecoder, sessionContext, true, null, null, callback);
    }

    @Override
    public <T> void commandAsync(String database, BsonDocument command, FieldNameValidator commandFieldNameValidator, ReadPreference readPreference, Decoder<T> commandResultDecoder, SessionContext sessionContext, boolean responseExpected, SplittablePayload payload, FieldNameValidator payloadFieldNameValidator, SingleResultCallback<T> callback) {
        this.executeProtocolAsync(new CommandProtocolImpl<T>(database, command, commandFieldNameValidator, readPreference, commandResultDecoder, responseExpected, payload, payloadFieldNameValidator, this.clusterConnectionMode), sessionContext, callback);
    }

    @Override
    public <T> QueryResult<T> query(MongoNamespace namespace, BsonDocument queryDocument, BsonDocument fields, int numberToReturn, int skip, boolean slaveOk, boolean tailableCursor, boolean awaitData, boolean noCursorTimeout, boolean partial, boolean oplogReplay, Decoder<T> resultDecoder) {
        return (QueryResult)this.executeProtocol(new QueryProtocol<T>(namespace, skip, numberToReturn, queryDocument, fields, resultDecoder).tailableCursor(tailableCursor).slaveOk(this.getSlaveOk(slaveOk)).oplogReplay(oplogReplay).noCursorTimeout(noCursorTimeout).awaitData(awaitData).partial(partial));
    }

    @Override
    public <T> QueryResult<T> query(MongoNamespace namespace, BsonDocument queryDocument, BsonDocument fields, int skip, int limit, int batchSize, boolean slaveOk, boolean tailableCursor, boolean awaitData, boolean noCursorTimeout, boolean partial, boolean oplogReplay, Decoder<T> resultDecoder) {
        return (QueryResult)this.executeProtocol(new QueryProtocol<T>(namespace, skip, limit, batchSize, queryDocument, fields, resultDecoder).tailableCursor(tailableCursor).slaveOk(this.getSlaveOk(slaveOk)).oplogReplay(oplogReplay).noCursorTimeout(noCursorTimeout).awaitData(awaitData).partial(partial));
    }

    @Override
    public <T> void queryAsync(MongoNamespace namespace, BsonDocument queryDocument, BsonDocument fields, int numberToReturn, int skip, boolean slaveOk, boolean tailableCursor, boolean awaitData, boolean noCursorTimeout, boolean partial, boolean oplogReplay, Decoder<T> resultDecoder, SingleResultCallback<QueryResult<T>> callback) {
        this.executeProtocolAsync(new QueryProtocol<T>(namespace, skip, numberToReturn, queryDocument, fields, resultDecoder).tailableCursor(tailableCursor).slaveOk(this.getSlaveOk(slaveOk)).oplogReplay(oplogReplay).noCursorTimeout(noCursorTimeout).awaitData(awaitData).partial(partial), callback);
    }

    @Override
    public <T> void queryAsync(MongoNamespace namespace, BsonDocument queryDocument, BsonDocument fields, int skip, int limit, int batchSize, boolean slaveOk, boolean tailableCursor, boolean awaitData, boolean noCursorTimeout, boolean partial, boolean oplogReplay, Decoder<T> resultDecoder, SingleResultCallback<QueryResult<T>> callback) {
        this.executeProtocolAsync(new QueryProtocol<T>(namespace, skip, limit, batchSize, queryDocument, fields, resultDecoder).tailableCursor(tailableCursor).slaveOk(this.getSlaveOk(slaveOk)).oplogReplay(oplogReplay).noCursorTimeout(noCursorTimeout).awaitData(awaitData).partial(partial), callback);
    }

    @Override
    public <T> QueryResult<T> getMore(MongoNamespace namespace, long cursorId, int numberToReturn, Decoder<T> resultDecoder) {
        return (QueryResult)this.executeProtocol(new GetMoreProtocol<T>(namespace, cursorId, numberToReturn, resultDecoder));
    }

    @Override
    public <T> void getMoreAsync(MongoNamespace namespace, long cursorId, int numberToReturn, Decoder<T> resultDecoder, SingleResultCallback<QueryResult<T>> callback) {
        this.executeProtocolAsync(new GetMoreProtocol<T>(namespace, cursorId, numberToReturn, resultDecoder), callback);
    }

    @Override
    public void killCursor(List<Long> cursors) {
        this.killCursor(null, cursors);
    }

    @Override
    public void killCursor(MongoNamespace namespace, List<Long> cursors) {
        this.executeProtocol(new KillCursorProtocol(namespace, cursors));
    }

    @Override
    public void killCursorAsync(List<Long> cursors, SingleResultCallback<Void> callback) {
        this.killCursorAsync(null, cursors, callback);
    }

    @Override
    public void killCursorAsync(MongoNamespace namespace, List<Long> cursors, SingleResultCallback<Void> callback) {
        this.executeProtocolAsync(new KillCursorProtocol(namespace, cursors), callback);
    }

    private ReadPreference getReadPreferenceFromSlaveOk(boolean slaveOk) {
        return this.getSlaveOk(slaveOk) ? ReadPreference.secondaryPreferred() : ReadPreference.primary();
    }

    private boolean getSlaveOk(boolean slaveOk) {
        return slaveOk || this.clusterConnectionMode == ClusterConnectionMode.SINGLE && this.wrapped.getDescription().getServerType() != ServerType.SHARD_ROUTER;
    }

    private <T> T executeProtocol(LegacyProtocol<T> protocol) {
        return this.protocolExecutor.execute(protocol, this.wrapped);
    }

    private <T> T executeProtocol(CommandProtocol<T> protocol, SessionContext sessionContext) {
        return this.protocolExecutor.execute(protocol, this.wrapped, sessionContext);
    }

    private <T> void executeProtocolAsync(LegacyProtocol<T> protocol, SingleResultCallback<T> callback) {
        SingleResultCallback<Object> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, LOGGER);
        try {
            this.protocolExecutor.executeAsync(protocol, this.wrapped, errHandlingCallback);
        } catch (Throwable t2) {
            errHandlingCallback.onResult(null, t2);
        }
    }

    private <T> void executeProtocolAsync(CommandProtocol<T> protocol, SessionContext sessionContext, SingleResultCallback<T> callback) {
        SingleResultCallback<Object> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, LOGGER);
        try {
            this.protocolExecutor.executeAsync(protocol, this.wrapped, sessionContext, errHandlingCallback);
        } catch (Throwable t2) {
            errHandlingCallback.onResult(null, t2);
        }
    }
}

