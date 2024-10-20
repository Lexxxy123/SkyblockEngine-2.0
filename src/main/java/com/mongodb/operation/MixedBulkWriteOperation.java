/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.MongoException;
import com.mongodb.MongoNamespace;
import com.mongodb.WriteConcern;
import com.mongodb.WriteConcernResult;
import com.mongodb.assertions.Assertions;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.binding.AsyncConnectionSource;
import com.mongodb.binding.AsyncWriteBinding;
import com.mongodb.binding.ConnectionSource;
import com.mongodb.binding.WriteBinding;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.bulk.DeleteRequest;
import com.mongodb.bulk.InsertRequest;
import com.mongodb.bulk.UpdateRequest;
import com.mongodb.bulk.WriteRequest;
import com.mongodb.connection.AsyncConnection;
import com.mongodb.connection.Connection;
import com.mongodb.internal.async.ErrorHandlingResultCallback;
import com.mongodb.internal.connection.MongoWriteConcernWithResponseException;
import com.mongodb.internal.connection.ProtocolHelper;
import com.mongodb.internal.operation.ServerVersionHelper;
import com.mongodb.internal.validator.NoOpFieldNameValidator;
import com.mongodb.operation.AsyncWriteOperation;
import com.mongodb.operation.BulkWriteBatch;
import com.mongodb.operation.CommandOperationHelper;
import com.mongodb.operation.OperationHelper;
import com.mongodb.operation.WriteOperation;
import com.mongodb.session.SessionContext;
import java.util.List;
import org.bson.BsonDocument;
import org.bson.FieldNameValidator;

@Deprecated
public class MixedBulkWriteOperation
implements AsyncWriteOperation<BulkWriteResult>,
WriteOperation<BulkWriteResult> {
    private static final FieldNameValidator NO_OP_FIELD_NAME_VALIDATOR = new NoOpFieldNameValidator();
    private final MongoNamespace namespace;
    private final List<? extends WriteRequest> writeRequests;
    private final boolean ordered;
    private final boolean retryWrites;
    private final WriteConcern writeConcern;
    private Boolean bypassDocumentValidation;

    @Deprecated
    public MixedBulkWriteOperation(MongoNamespace namespace, List<? extends WriteRequest> writeRequests, boolean ordered, WriteConcern writeConcern) {
        this(namespace, writeRequests, ordered, writeConcern, false);
    }

    public MixedBulkWriteOperation(MongoNamespace namespace, List<? extends WriteRequest> writeRequests, boolean ordered, WriteConcern writeConcern, boolean retryWrites) {
        this.ordered = ordered;
        this.namespace = Assertions.notNull("namespace", namespace);
        this.writeRequests = Assertions.notNull("writes", writeRequests);
        this.writeConcern = Assertions.notNull("writeConcern", writeConcern);
        this.retryWrites = retryWrites;
        Assertions.isTrueArgument("writes is not an empty list", !writeRequests.isEmpty());
    }

    public MongoNamespace getNamespace() {
        return this.namespace;
    }

    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }

    public boolean isOrdered() {
        return this.ordered;
    }

    public List<? extends WriteRequest> getWriteRequests() {
        return this.writeRequests;
    }

    public Boolean getBypassDocumentValidation() {
        return this.bypassDocumentValidation;
    }

    public MixedBulkWriteOperation bypassDocumentValidation(Boolean bypassDocumentValidation) {
        this.bypassDocumentValidation = bypassDocumentValidation;
        return this;
    }

    public Boolean getRetryWrites() {
        return this.retryWrites;
    }

    @Override
    public BulkWriteResult execute(final WriteBinding binding) {
        return OperationHelper.withReleasableConnection(binding, new OperationHelper.CallableWithConnectionAndSource<BulkWriteResult>(){

            @Override
            public BulkWriteResult call(ConnectionSource connectionSource, Connection connection) {
                MixedBulkWriteOperation.this.validateWriteRequestsAndReleaseConnectionIfError(binding, connection);
                if (MixedBulkWriteOperation.this.getWriteConcern().isAcknowledged() || ServerVersionHelper.serverIsAtLeastVersionThreeDotSix(connection.getDescription())) {
                    BulkWriteBatch bulkWriteBatch = BulkWriteBatch.createBulkWriteBatch(MixedBulkWriteOperation.this.namespace, connectionSource.getServerDescription(), connection.getDescription(), MixedBulkWriteOperation.this.ordered, MixedBulkWriteOperation.this.getAppliedWriteConcern(binding), MixedBulkWriteOperation.this.bypassDocumentValidation, MixedBulkWriteOperation.this.retryWrites, MixedBulkWriteOperation.this.writeRequests, binding.getSessionContext());
                    return MixedBulkWriteOperation.this.executeBulkWriteBatch(binding, connection, bulkWriteBatch);
                }
                return MixedBulkWriteOperation.this.executeLegacyBatches(connection);
            }
        });
    }

    @Override
    public void executeAsync(final AsyncWriteBinding binding, SingleResultCallback<BulkWriteResult> callback) {
        final SingleResultCallback<BulkWriteResult> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER);
        OperationHelper.withAsyncConnection(binding, new OperationHelper.AsyncCallableWithConnectionAndSource(){

            @Override
            public void call(final AsyncConnectionSource source, AsyncConnection connection, Throwable t2) {
                if (t2 != null) {
                    errHandlingCallback.onResult(null, t2);
                } else {
                    OperationHelper.validateWriteRequests(connection, MixedBulkWriteOperation.this.bypassDocumentValidation, MixedBulkWriteOperation.this.writeRequests, MixedBulkWriteOperation.this.getAppliedWriteConcern(binding), new OperationHelper.AsyncCallableWithConnection(){

                        @Override
                        public void call(AsyncConnection connection, Throwable t1) {
                            OperationHelper.ConnectionReleasingWrappedCallback<Object> releasingCallback = new OperationHelper.ConnectionReleasingWrappedCallback<Object>(errHandlingCallback, source, connection);
                            if (t1 != null) {
                                releasingCallback.onResult(null, t1);
                            } else if (MixedBulkWriteOperation.this.getAppliedWriteConcern(binding).isAcknowledged() || ServerVersionHelper.serverIsAtLeastVersionThreeDotSix(connection.getDescription())) {
                                try {
                                    BulkWriteBatch batch = BulkWriteBatch.createBulkWriteBatch(MixedBulkWriteOperation.this.namespace, source.getServerDescription(), connection.getDescription(), MixedBulkWriteOperation.this.ordered, MixedBulkWriteOperation.this.getAppliedWriteConcern(binding), MixedBulkWriteOperation.this.bypassDocumentValidation, MixedBulkWriteOperation.this.retryWrites, MixedBulkWriteOperation.this.writeRequests, binding.getSessionContext());
                                    MixedBulkWriteOperation.this.executeBatchesAsync(binding, connection, batch, MixedBulkWriteOperation.this.retryWrites, releasingCallback);
                                } catch (Throwable t2) {
                                    releasingCallback.onResult(null, t2);
                                }
                            } else {
                                MixedBulkWriteOperation.this.executeLegacyBatchesAsync(connection, MixedBulkWriteOperation.this.getWriteRequests(), 1, releasingCallback);
                            }
                        }
                    });
                }
            }
        });
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private BulkWriteResult executeBulkWriteBatch(WriteBinding binding, Connection connection, BulkWriteBatch originalBatch) {
        BulkWriteBatch currentBatch = originalBatch;
        MongoException exception = null;
        try {
            while (currentBatch.shouldProcessBatch()) {
                MongoException writeConcernBasedError;
                BsonDocument result = this.executeCommand(connection, currentBatch, binding);
                if (this.retryWrites && !binding.getSessionContext().hasActiveTransaction() && (writeConcernBasedError = ProtocolHelper.createSpecialException(result, connection.getDescription().getServerAddress(), "errMsg")) != null && CommandOperationHelper.shouldAttemptToRetryWrite(true, (Throwable)writeConcernBasedError)) {
                    throw new MongoWriteConcernWithResponseException(writeConcernBasedError, result);
                }
                currentBatch.addResult(result);
                currentBatch = currentBatch.getNextBatch();
            }
        } catch (MongoException e2) {
            exception = e2;
        } finally {
            connection.release();
        }
        if (exception == null) {
            try {
                return currentBatch.getResult();
            } catch (MongoException e3) {
                if (originalBatch.getRetryWrites()) {
                    CommandOperationHelper.logUnableToRetry(originalBatch.getPayload().getPayloadType().toString(), e3);
                }
                throw e3;
            }
        }
        if (!(exception instanceof MongoWriteConcernWithResponseException) && !CommandOperationHelper.shouldAttemptToRetryWrite(originalBatch.getRetryWrites(), (Throwable)exception)) {
            if (originalBatch.getRetryWrites()) {
                CommandOperationHelper.logUnableToRetry(originalBatch.getPayload().getPayloadType().toString(), exception);
            }
            throw CommandOperationHelper.transformWriteException(exception);
        }
        return this.retryExecuteBatches(binding, currentBatch, exception);
    }

    private BulkWriteResult retryExecuteBatches(final WriteBinding binding, final BulkWriteBatch retryBatch, final MongoException originalError) {
        CommandOperationHelper.logRetryExecute(retryBatch.getPayload().getPayloadType().toString(), originalError);
        return OperationHelper.withReleasableConnection(binding, originalError, new OperationHelper.CallableWithConnectionAndSource<BulkWriteResult>(){

            @Override
            public BulkWriteResult call(ConnectionSource source, Connection connection) {
                if (!OperationHelper.isRetryableWrite(MixedBulkWriteOperation.this.retryWrites, MixedBulkWriteOperation.this.getAppliedWriteConcern(binding), source.getServerDescription(), connection.getDescription(), binding.getSessionContext())) {
                    return this.checkMongoWriteConcernWithResponseException(connection);
                }
                try {
                    retryBatch.addResult(MixedBulkWriteOperation.this.executeCommand(connection, retryBatch, binding));
                } catch (Throwable t2) {
                    return this.checkMongoWriteConcernWithResponseException(connection);
                }
                return MixedBulkWriteOperation.this.executeBulkWriteBatch(binding, connection, retryBatch.getNextBatch());
            }

            private BulkWriteResult checkMongoWriteConcernWithResponseException(Connection connection) {
                if (originalError instanceof MongoWriteConcernWithResponseException) {
                    retryBatch.addResult((BsonDocument)((MongoWriteConcernWithResponseException)originalError).getResponse());
                    return MixedBulkWriteOperation.this.executeBulkWriteBatch(binding, connection, retryBatch.getNextBatch());
                }
                connection.release();
                throw originalError;
            }
        });
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private BulkWriteResult executeLegacyBatches(Connection connection) {
        try {
            for (WriteRequest writeRequest : this.getWriteRequests()) {
                if (writeRequest.getType() == WriteRequest.Type.INSERT) {
                    connection.insert(this.getNamespace(), this.isOrdered(), (InsertRequest)writeRequest);
                    continue;
                }
                if (writeRequest.getType() == WriteRequest.Type.UPDATE || writeRequest.getType() == WriteRequest.Type.REPLACE) {
                    connection.update(this.getNamespace(), this.isOrdered(), (UpdateRequest)writeRequest);
                    continue;
                }
                connection.delete(this.getNamespace(), this.isOrdered(), (DeleteRequest)writeRequest);
            }
            BulkWriteResult bulkWriteResult = BulkWriteResult.unacknowledged();
            return bulkWriteResult;
        } finally {
            connection.release();
        }
    }

    private void executeBatchesAsync(AsyncWriteBinding binding, AsyncConnection connection, BulkWriteBatch batch, boolean retryWrites, OperationHelper.ConnectionReleasingWrappedCallback<BulkWriteResult> callback) {
        this.executeCommandAsync(binding, connection, batch, callback, this.getCommandCallback(binding, connection, batch, retryWrites, false, callback));
    }

    private void retryExecuteBatchesAsync(final AsyncWriteBinding binding, final BulkWriteBatch retryBatch, final Throwable originalError, final SingleResultCallback<BulkWriteResult> callback) {
        CommandOperationHelper.logRetryExecute(retryBatch.getPayload().getPayloadType().toString(), originalError);
        OperationHelper.withAsyncConnection(binding, new OperationHelper.AsyncCallableWithConnectionAndSource(){

            @Override
            public void call(AsyncConnectionSource source, final AsyncConnection connection, Throwable t2) {
                if (t2 != null) {
                    callback.onResult(null, originalError);
                } else {
                    final OperationHelper.ConnectionReleasingWrappedCallback<BulkWriteResult> releasingCallback = new OperationHelper.ConnectionReleasingWrappedCallback<BulkWriteResult>(callback, source, connection);
                    if (!OperationHelper.isRetryableWrite(MixedBulkWriteOperation.this.retryWrites, MixedBulkWriteOperation.this.getAppliedWriteConcern(binding), source.getServerDescription(), connection.getDescription(), binding.getSessionContext())) {
                        this.checkMongoWriteConcernWithResponseException(connection, releasingCallback);
                    } else {
                        MixedBulkWriteOperation.this.executeCommandAsync(binding, connection, retryBatch, releasingCallback, new SingleResultCallback<BsonDocument>(){

                            @Override
                            public void onResult(BsonDocument result, Throwable t2) {
                                if (t2 != null) {
                                    this.checkMongoWriteConcernWithResponseException(connection, releasingCallback);
                                } else {
                                    MixedBulkWriteOperation.this.getCommandCallback(binding, connection, retryBatch, true, true, releasingCallback).onResult(result, null);
                                }
                            }
                        });
                    }
                }
            }

            private void checkMongoWriteConcernWithResponseException(AsyncConnection connection, OperationHelper.ConnectionReleasingWrappedCallback<BulkWriteResult> releasingCallback) {
                if (originalError instanceof MongoWriteConcernWithResponseException) {
                    retryBatch.addResult((BsonDocument)((MongoWriteConcernWithResponseException)originalError).getResponse());
                    BulkWriteBatch nextBatch = retryBatch.getNextBatch();
                    MixedBulkWriteOperation.this.executeCommandAsync(binding, connection, nextBatch, releasingCallback, MixedBulkWriteOperation.this.getCommandCallback(binding, connection, nextBatch, true, true, releasingCallback));
                } else {
                    releasingCallback.onResult(null, originalError);
                }
            }
        });
    }

    private void executeLegacyBatchesAsync(final AsyncConnection connection, List<? extends WriteRequest> writeRequests, final int batchNum, final SingleResultCallback<BulkWriteResult> callback) {
        try {
            if (!writeRequests.isEmpty()) {
                WriteRequest writeRequest = writeRequests.get(0);
                final List<? extends WriteRequest> remaining = writeRequests.subList(1, writeRequests.size());
                SingleResultCallback<WriteConcernResult> writeCallback = new SingleResultCallback<WriteConcernResult>(){

                    @Override
                    public void onResult(WriteConcernResult result, Throwable t2) {
                        if (t2 != null) {
                            callback.onResult(null, t2);
                        } else {
                            MixedBulkWriteOperation.this.executeLegacyBatchesAsync(connection, remaining, batchNum + 1, callback);
                        }
                    }
                };
                if (writeRequest.getType() == WriteRequest.Type.INSERT) {
                    connection.insertAsync(this.getNamespace(), this.isOrdered(), (InsertRequest)writeRequest, writeCallback);
                } else if (writeRequest.getType() == WriteRequest.Type.UPDATE || writeRequest.getType() == WriteRequest.Type.REPLACE) {
                    connection.updateAsync(this.getNamespace(), this.isOrdered(), (UpdateRequest)writeRequest, writeCallback);
                } else {
                    connection.deleteAsync(this.getNamespace(), this.isOrdered(), (DeleteRequest)writeRequest, writeCallback);
                }
            } else {
                callback.onResult(BulkWriteResult.unacknowledged(), null);
            }
        } catch (Throwable t2) {
            callback.onResult(null, t2);
        }
    }

    private BsonDocument executeCommand(Connection connection, BulkWriteBatch batch, WriteBinding binding) {
        return connection.command(this.namespace.getDatabaseName(), batch.getCommand(), NO_OP_FIELD_NAME_VALIDATOR, null, batch.getDecoder(), binding.getSessionContext(), this.shouldAcknowledge(batch, binding.getSessionContext()), batch.getPayload(), batch.getFieldNameValidator());
    }

    private void executeCommandAsync(AsyncWriteBinding binding, AsyncConnection connection, BulkWriteBatch batch, OperationHelper.ConnectionReleasingWrappedCallback<BulkWriteResult> callback, SingleResultCallback<BsonDocument> commandCallback) {
        try {
            connection.commandAsync(this.namespace.getDatabaseName(), batch.getCommand(), NO_OP_FIELD_NAME_VALIDATOR, null, batch.getDecoder(), binding.getSessionContext(), this.shouldAcknowledge(batch, binding.getSessionContext()), batch.getPayload(), batch.getFieldNameValidator(), commandCallback);
        } catch (Throwable t2) {
            callback.onResult(null, t2);
        }
    }

    private WriteConcern getAppliedWriteConcern(WriteBinding binding) {
        return this.getAppliedWriteConcern(binding.getSessionContext());
    }

    private WriteConcern getAppliedWriteConcern(AsyncWriteBinding binding) {
        return this.getAppliedWriteConcern(binding.getSessionContext());
    }

    private WriteConcern getAppliedWriteConcern(SessionContext sessionContext) {
        if (sessionContext.hasActiveTransaction()) {
            return WriteConcern.ACKNOWLEDGED;
        }
        return this.writeConcern;
    }

    private boolean shouldAcknowledge(BulkWriteBatch batch, SessionContext sessionContext) {
        return this.ordered ? batch.hasAnotherBatch() || this.getAppliedWriteConcern(sessionContext).isAcknowledged() : this.getAppliedWriteConcern(sessionContext).isAcknowledged();
    }

    private SingleResultCallback<BsonDocument> getCommandCallback(final AsyncWriteBinding binding, final AsyncConnection connection, final BulkWriteBatch batch, final boolean retryWrites, final boolean isSecondAttempt, final OperationHelper.ConnectionReleasingWrappedCallback<BulkWriteResult> callback) {
        return new SingleResultCallback<BsonDocument>(){

            @Override
            public void onResult(BsonDocument result, Throwable t2) {
                if (t2 != null) {
                    if (isSecondAttempt || !CommandOperationHelper.shouldAttemptToRetryWrite(retryWrites, t2)) {
                        if (retryWrites && !isSecondAttempt) {
                            CommandOperationHelper.logUnableToRetry(batch.getPayload().getPayloadType().toString(), t2);
                        }
                        if (t2 instanceof MongoWriteConcernWithResponseException) {
                            MixedBulkWriteOperation.this.addBatchResult((BsonDocument)((MongoWriteConcernWithResponseException)t2).getResponse(), binding, connection, batch, retryWrites, callback);
                        } else {
                            callback.onResult(null, t2 instanceof MongoException ? CommandOperationHelper.transformWriteException((MongoException)t2) : t2);
                        }
                    } else {
                        MixedBulkWriteOperation.this.retryExecuteBatchesAsync(binding, batch, t2, callback.releaseConnectionAndGetWrapped());
                    }
                } else {
                    MongoException writeConcernBasedError;
                    if (retryWrites && !isSecondAttempt && (writeConcernBasedError = ProtocolHelper.createSpecialException(result, connection.getDescription().getServerAddress(), "errMsg")) != null && CommandOperationHelper.shouldAttemptToRetryWrite(true, (Throwable)writeConcernBasedError)) {
                        MixedBulkWriteOperation.this.retryExecuteBatchesAsync(binding, batch, new MongoWriteConcernWithResponseException(writeConcernBasedError, result), callback.releaseConnectionAndGetWrapped());
                        return;
                    }
                    MixedBulkWriteOperation.this.addBatchResult(result, binding, connection, batch, retryWrites, callback);
                }
            }
        };
    }

    private void addBatchResult(BsonDocument result, AsyncWriteBinding binding, AsyncConnection connection, BulkWriteBatch batch, boolean retryWrites, OperationHelper.ConnectionReleasingWrappedCallback<BulkWriteResult> callback) {
        batch.addResult(result);
        BulkWriteBatch nextBatch = batch.getNextBatch();
        if (nextBatch.shouldProcessBatch()) {
            this.executeBatchesAsync(binding, connection, nextBatch, retryWrites, callback);
        } else if (batch.hasErrors()) {
            if (retryWrites) {
                CommandOperationHelper.logUnableToRetry(batch.getPayload().getPayloadType().toString(), batch.getError());
            }
            callback.onResult(null, CommandOperationHelper.transformWriteException(batch.getError()));
        } else {
            callback.onResult(batch.getResult(), null);
        }
    }

    private void validateWriteRequestsAndReleaseConnectionIfError(WriteBinding binding, Connection connection) {
        try {
            OperationHelper.validateWriteRequests(connection.getDescription(), this.bypassDocumentValidation, this.writeRequests, this.getAppliedWriteConcern(binding));
        } catch (IllegalArgumentException e2) {
            connection.release();
            throw e2;
        } catch (MongoException e3) {
            connection.release();
            throw e3;
        } catch (Throwable t2) {
            connection.release();
            throw MongoException.fromThrowableNonNull(t2);
        }
    }
}

