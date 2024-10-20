/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.MongoNamespace;
import com.mongodb.ServerAddress;
import com.mongodb.assertions.Assertions;
import com.mongodb.async.AsyncBatchCursor;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.binding.AsyncConnectionSource;
import com.mongodb.binding.AsyncReadBinding;
import com.mongodb.binding.ConnectionSource;
import com.mongodb.binding.ReadBinding;
import com.mongodb.connection.AsyncConnection;
import com.mongodb.connection.Connection;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.connection.QueryResult;
import com.mongodb.connection.ServerDescription;
import com.mongodb.internal.async.ErrorHandlingResultCallback;
import com.mongodb.operation.AsyncQueryBatchCursor;
import com.mongodb.operation.AsyncReadOperation;
import com.mongodb.operation.BatchCursor;
import com.mongodb.operation.CommandOperationHelper;
import com.mongodb.operation.CommandResultDocumentCodec;
import com.mongodb.operation.OperationHelper;
import com.mongodb.operation.OperationReadConcernHelper;
import com.mongodb.operation.QueryBatchCursor;
import com.mongodb.operation.ReadOperation;
import com.mongodb.session.SessionContext;
import java.util.ArrayList;
import java.util.List;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.codecs.Decoder;

@Deprecated
public class ParallelCollectionScanOperation<T>
implements AsyncReadOperation<List<AsyncBatchCursor<T>>>,
ReadOperation<List<BatchCursor<T>>> {
    private final MongoNamespace namespace;
    private final int numCursors;
    private boolean retryReads;
    private int batchSize = 0;
    private final Decoder<T> decoder;

    public ParallelCollectionScanOperation(MongoNamespace namespace, int numCursors, Decoder<T> decoder) {
        this.namespace = Assertions.notNull("namespace", namespace);
        Assertions.isTrue("numCursors >= 1", numCursors >= 1);
        this.numCursors = numCursors;
        this.decoder = Assertions.notNull("decoder", decoder);
    }

    public int getNumCursors() {
        return this.numCursors;
    }

    public int getBatchSize() {
        return this.batchSize;
    }

    public ParallelCollectionScanOperation<T> batchSize(int batchSize) {
        Assertions.isTrue("batchSize >= 0", batchSize >= 0);
        this.batchSize = batchSize;
        return this;
    }

    public ParallelCollectionScanOperation<T> retryReads(boolean retryReads) {
        this.retryReads = retryReads;
        return this;
    }

    public boolean getRetryReads() {
        return this.retryReads;
    }

    @Override
    public List<BatchCursor<T>> execute(ReadBinding binding) {
        return CommandOperationHelper.executeCommand(binding, this.namespace.getDatabaseName(), this.getCommandCreator(binding.getSessionContext()), CommandResultDocumentCodec.create(this.decoder, "firstBatch"), this.transformer(), this.retryReads);
    }

    @Override
    public void executeAsync(AsyncReadBinding binding, SingleResultCallback<List<AsyncBatchCursor<T>>> callback) {
        CommandOperationHelper.executeCommandAsync(binding, this.namespace.getDatabaseName(), this.getCommandCreator(binding.getSessionContext()), CommandResultDocumentCodec.create(this.decoder, "firstBatch"), this.asyncTransformer(), this.retryReads, ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER));
    }

    private CommandOperationHelper.CommandReadTransformer<BsonDocument, List<BatchCursor<T>>> transformer() {
        return new CommandOperationHelper.CommandReadTransformer<BsonDocument, List<BatchCursor<T>>>(){

            @Override
            public List<BatchCursor<T>> apply(BsonDocument result, ConnectionSource source, Connection connection) {
                ArrayList cursors = new ArrayList();
                for (BsonValue cursorValue : ParallelCollectionScanOperation.this.getCursorDocuments(result)) {
                    cursors.add(new QueryBatchCursor(ParallelCollectionScanOperation.this.createQueryResult(ParallelCollectionScanOperation.this.getCursorDocument(cursorValue.asDocument()), source.getServerDescription().getAddress()), 0, ParallelCollectionScanOperation.this.getBatchSize(), ParallelCollectionScanOperation.this.decoder, source));
                }
                return cursors;
            }
        };
    }

    private CommandOperationHelper.CommandReadTransformerAsync<BsonDocument, List<AsyncBatchCursor<T>>> asyncTransformer() {
        return new CommandOperationHelper.CommandReadTransformerAsync<BsonDocument, List<AsyncBatchCursor<T>>>(){

            @Override
            public List<AsyncBatchCursor<T>> apply(BsonDocument result, AsyncConnectionSource source, AsyncConnection connection) {
                ArrayList cursors = new ArrayList();
                for (BsonValue cursorValue : ParallelCollectionScanOperation.this.getCursorDocuments(result)) {
                    cursors.add(new AsyncQueryBatchCursor(ParallelCollectionScanOperation.this.createQueryResult(ParallelCollectionScanOperation.this.getCursorDocument(cursorValue.asDocument()), source.getServerDescription().getAddress()), 0, ParallelCollectionScanOperation.this.getBatchSize(), 0L, ParallelCollectionScanOperation.this.decoder, source, connection, result));
                }
                return cursors;
            }
        };
    }

    private BsonArray getCursorDocuments(BsonDocument result) {
        return result.getArray("cursors");
    }

    private BsonDocument getCursorDocument(BsonDocument cursorDocument) {
        return cursorDocument.getDocument("cursor");
    }

    private QueryResult<T> createQueryResult(BsonDocument cursorDocument, ServerAddress serverAddress) {
        return OperationHelper.cursorDocumentToQueryResult(cursorDocument, serverAddress);
    }

    private CommandOperationHelper.CommandCreator getCommandCreator(final SessionContext sessionContext) {
        return new CommandOperationHelper.CommandCreator(){

            @Override
            public BsonDocument create(ServerDescription serverDescription, ConnectionDescription connectionDescription) {
                OperationHelper.validateReadConcern(connectionDescription, sessionContext.getReadConcern());
                return ParallelCollectionScanOperation.this.getCommand(sessionContext);
            }
        };
    }

    private BsonDocument getCommand(SessionContext sessionContext) {
        BsonDocument document = new BsonDocument("parallelCollectionScan", new BsonString(this.namespace.getCollectionName())).append("numCursors", new BsonInt32(this.getNumCursors()));
        OperationReadConcernHelper.appendReadConcernToCommand(sessionContext, document);
        return document;
    }
}

