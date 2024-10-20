/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.MongoCommandException;
import com.mongodb.MongoException;
import com.mongodb.MongoNamespace;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.ServerCursor;
import com.mongodb.assertions.Assertions;
import com.mongodb.binding.ConnectionSource;
import com.mongodb.connection.Connection;
import com.mongodb.connection.QueryResult;
import com.mongodb.internal.operation.ServerVersionHelper;
import com.mongodb.internal.validator.NoOpFieldNameValidator;
import com.mongodb.operation.AggregateResponseBatchCursor;
import com.mongodb.operation.CommandResultDocumentCodec;
import com.mongodb.operation.CursorHelper;
import com.mongodb.operation.OperationHelper;
import com.mongodb.operation.QueryHelper;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonString;
import org.bson.BsonTimestamp;
import org.bson.FieldNameValidator;
import org.bson.codecs.BsonDocumentCodec;
import org.bson.codecs.Decoder;

class QueryBatchCursor<T>
implements AggregateResponseBatchCursor<T> {
    private static final FieldNameValidator NO_OP_FIELD_NAME_VALIDATOR = new NoOpFieldNameValidator();
    private static final String CURSOR = "cursor";
    private static final String POST_BATCH_RESUME_TOKEN = "postBatchResumeToken";
    private static final String OPERATION_TIME = "operationTime";
    private final MongoNamespace namespace;
    private final ServerAddress serverAddress;
    private final int limit;
    private final Decoder<T> decoder;
    private final long maxTimeMS;
    private int batchSize;
    private ConnectionSource connectionSource;
    private ServerCursor serverCursor;
    private List<T> nextBatch;
    private int count;
    private volatile boolean closed;
    private BsonDocument postBatchResumeToken;
    private BsonTimestamp operationTime;
    private boolean firstBatchEmpty;

    QueryBatchCursor(QueryResult<T> firstQueryResult, int limit, int batchSize, Decoder<T> decoder) {
        this(firstQueryResult, limit, batchSize, decoder, null);
    }

    QueryBatchCursor(QueryResult<T> firstQueryResult, int limit, int batchSize, Decoder<T> decoder, ConnectionSource connectionSource) {
        this(firstQueryResult, limit, batchSize, 0L, decoder, connectionSource, null, null);
    }

    QueryBatchCursor(QueryResult<T> firstQueryResult, int limit, int batchSize, Decoder<T> decoder, ConnectionSource connectionSource, Connection connection) {
        this(firstQueryResult, limit, batchSize, 0L, decoder, connectionSource, connection, null);
    }

    QueryBatchCursor(QueryResult<T> firstQueryResult, int limit, int batchSize, long maxTimeMS, Decoder<T> decoder, ConnectionSource connectionSource, Connection connection) {
        this(firstQueryResult, limit, batchSize, maxTimeMS, decoder, connectionSource, connection, null);
    }

    QueryBatchCursor(QueryResult<T> firstQueryResult, int limit, int batchSize, long maxTimeMS, Decoder<T> decoder, ConnectionSource connectionSource, Connection connection, BsonDocument result) {
        Assertions.isTrueArgument("maxTimeMS >= 0", maxTimeMS >= 0L);
        this.maxTimeMS = maxTimeMS;
        this.namespace = firstQueryResult.getNamespace();
        this.serverAddress = firstQueryResult.getAddress();
        this.limit = limit;
        this.batchSize = batchSize;
        this.decoder = Assertions.notNull("decoder", decoder);
        if (result != null) {
            this.operationTime = result.getTimestamp(OPERATION_TIME, null);
            this.postBatchResumeToken = this.getPostBatchResumeTokenFromResponse(result);
        }
        if (firstQueryResult.getCursor() != null) {
            Assertions.notNull("connectionSource", connectionSource);
        }
        this.connectionSource = connectionSource != null ? connectionSource.retain() : null;
        this.initFromQueryResult(firstQueryResult);
        this.firstBatchEmpty = firstQueryResult.getResults().isEmpty();
        if (this.limitReached()) {
            this.killCursor(connection);
        }
        if (this.serverCursor == null && this.connectionSource != null) {
            this.connectionSource.release();
            this.connectionSource = null;
        }
    }

    @Override
    public boolean hasNext() {
        if (this.closed) {
            throw new IllegalStateException("Cursor has been closed");
        }
        if (this.nextBatch != null) {
            return true;
        }
        if (this.limitReached()) {
            return false;
        }
        while (this.serverCursor != null) {
            this.getMore();
            if (this.closed) {
                throw new IllegalStateException("Cursor has been closed");
            }
            if (this.nextBatch == null) continue;
            return true;
        }
        return false;
    }

    @Override
    public List<T> next() {
        if (this.closed) {
            throw new IllegalStateException("Iterator has been closed");
        }
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        List<T> retVal = this.nextBatch;
        this.nextBatch = null;
        return retVal;
    }

    @Override
    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    @Override
    public int getBatchSize() {
        return this.batchSize;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public void close() {
        if (!this.closed) {
            this.closed = true;
            try {
                this.killCursor();
            } finally {
                if (this.connectionSource != null) {
                    this.connectionSource.release();
                }
            }
        }
    }

    @Override
    public List<T> tryNext() {
        if (this.closed) {
            throw new IllegalStateException("Cursor has been closed");
        }
        if (!this.tryHasNext()) {
            return null;
        }
        return this.next();
    }

    boolean tryHasNext() {
        if (this.nextBatch != null) {
            return true;
        }
        if (this.limitReached()) {
            return false;
        }
        if (this.serverCursor != null) {
            this.getMore();
        }
        return this.nextBatch != null;
    }

    @Override
    public ServerCursor getServerCursor() {
        if (this.closed) {
            throw new IllegalStateException("Iterator has been closed");
        }
        return this.serverCursor;
    }

    @Override
    public ServerAddress getServerAddress() {
        if (this.closed) {
            throw new IllegalStateException("Iterator has been closed");
        }
        return this.serverAddress;
    }

    @Override
    public BsonDocument getPostBatchResumeToken() {
        return this.postBatchResumeToken;
    }

    @Override
    public BsonTimestamp getOperationTime() {
        return this.operationTime;
    }

    @Override
    public boolean isFirstBatchEmpty() {
        return this.firstBatchEmpty;
    }

    private void getMore() {
        Connection connection = this.connectionSource.getConnection();
        try {
            if (ServerVersionHelper.serverIsAtLeastVersionThreeDotTwo(connection.getDescription())) {
                try {
                    this.initFromCommandResult(connection.command(this.namespace.getDatabaseName(), this.asGetMoreCommandDocument(), NO_OP_FIELD_NAME_VALIDATOR, ReadPreference.primary(), CommandResultDocumentCodec.create(this.decoder, "nextBatch"), this.connectionSource.getSessionContext()));
                } catch (MongoCommandException e2) {
                    throw QueryHelper.translateCommandException(e2, this.serverCursor);
                }
            } else {
                QueryResult<T> getMore = connection.getMore(this.namespace, this.serverCursor.getId(), CursorHelper.getNumberToReturn(this.limit, this.batchSize, this.count), this.decoder);
                this.initFromQueryResult(getMore);
            }
            if (this.limitReached()) {
                this.killCursor(connection);
            }
            if (this.serverCursor == null) {
                this.connectionSource.release();
                this.connectionSource = null;
            }
        } finally {
            connection.release();
        }
    }

    private BsonDocument asGetMoreCommandDocument() {
        BsonDocument document = new BsonDocument("getMore", new BsonInt64(this.serverCursor.getId())).append("collection", new BsonString(this.namespace.getCollectionName()));
        int batchSizeForGetMoreCommand = Math.abs(CursorHelper.getNumberToReturn(this.limit, this.batchSize, this.count));
        if (batchSizeForGetMoreCommand != 0) {
            document.append("batchSize", new BsonInt32(batchSizeForGetMoreCommand));
        }
        if (this.maxTimeMS != 0L) {
            document.append("maxTimeMS", new BsonInt64(this.maxTimeMS));
        }
        return document;
    }

    private void initFromQueryResult(QueryResult<T> queryResult) {
        this.serverCursor = queryResult.getCursor();
        this.nextBatch = queryResult.getResults().isEmpty() ? null : queryResult.getResults();
        this.count += queryResult.getResults().size();
    }

    private void initFromCommandResult(BsonDocument getMoreCommandResultDocument) {
        QueryResult queryResult = OperationHelper.getMoreCursorDocumentToQueryResult(getMoreCommandResultDocument.getDocument(CURSOR), this.connectionSource.getServerDescription().getAddress());
        this.postBatchResumeToken = this.getPostBatchResumeTokenFromResponse(getMoreCommandResultDocument);
        this.operationTime = getMoreCommandResultDocument.getTimestamp(OPERATION_TIME, null);
        this.initFromQueryResult(queryResult);
    }

    private boolean limitReached() {
        return Math.abs(this.limit) != 0 && this.count >= Math.abs(this.limit);
    }

    private void killCursor() {
        if (this.serverCursor != null) {
            try {
                Connection connection = this.connectionSource.getConnection();
                try {
                    this.killCursor(connection);
                } finally {
                    connection.release();
                }
            } catch (MongoException mongoException) {
                // empty catch block
            }
        }
    }

    private void killCursor(Connection connection) {
        if (this.serverCursor != null) {
            Assertions.notNull("connection", connection);
            if (ServerVersionHelper.serverIsAtLeastVersionThreeDotTwo(connection.getDescription())) {
                connection.command(this.namespace.getDatabaseName(), this.asKillCursorsCommandDocument(), NO_OP_FIELD_NAME_VALIDATOR, ReadPreference.primary(), new BsonDocumentCodec(), this.connectionSource.getSessionContext());
            } else {
                connection.killCursor(this.namespace, Collections.singletonList(this.serverCursor.getId()));
            }
            this.serverCursor = null;
        }
    }

    private BsonDocument asKillCursorsCommandDocument() {
        return new BsonDocument("killCursors", new BsonString(this.namespace.getCollectionName())).append("cursors", new BsonArray(Collections.singletonList(new BsonInt64(this.serverCursor.getId()))));
    }

    private BsonDocument getPostBatchResumeTokenFromResponse(BsonDocument result) {
        BsonDocument cursor = result.getDocument(CURSOR, null);
        if (cursor != null) {
            return cursor.getDocument(POST_BATCH_RESUME_TOKEN, null);
        }
        return null;
    }
}

