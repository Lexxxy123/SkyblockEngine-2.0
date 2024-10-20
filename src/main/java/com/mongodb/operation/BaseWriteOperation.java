/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.DuplicateKeyException;
import com.mongodb.ErrorCategory;
import com.mongodb.MongoBulkWriteException;
import com.mongodb.MongoException;
import com.mongodb.MongoNamespace;
import com.mongodb.WriteConcern;
import com.mongodb.WriteConcernException;
import com.mongodb.WriteConcernResult;
import com.mongodb.assertions.Assertions;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.binding.AsyncWriteBinding;
import com.mongodb.binding.WriteBinding;
import com.mongodb.bulk.BulkWriteError;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.bulk.WriteRequest;
import com.mongodb.operation.AsyncWriteOperation;
import com.mongodb.operation.MixedBulkWriteOperation;
import com.mongodb.operation.WriteOperation;
import java.util.List;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;

@Deprecated
public abstract class BaseWriteOperation
implements AsyncWriteOperation<WriteConcernResult>,
WriteOperation<WriteConcernResult> {
    private final WriteConcern writeConcern;
    private final MongoNamespace namespace;
    private final boolean ordered;
    private final boolean retryWrites;
    private Boolean bypassDocumentValidation;

    @Deprecated
    public BaseWriteOperation(MongoNamespace namespace, boolean ordered, WriteConcern writeConcern) {
        this(namespace, ordered, writeConcern, false);
    }

    public BaseWriteOperation(MongoNamespace namespace, boolean ordered, WriteConcern writeConcern, boolean retryWrites) {
        this.ordered = ordered;
        this.namespace = Assertions.notNull("namespace", namespace);
        this.writeConcern = Assertions.notNull("writeConcern", writeConcern);
        this.retryWrites = retryWrites;
    }

    protected abstract List<? extends WriteRequest> getWriteRequests();

    protected abstract WriteRequest.Type getType();

    public MongoNamespace getNamespace() {
        return this.namespace;
    }

    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }

    public boolean isOrdered() {
        return this.ordered;
    }

    public Boolean getBypassDocumentValidation() {
        return this.bypassDocumentValidation;
    }

    public BaseWriteOperation bypassDocumentValidation(Boolean bypassDocumentValidation) {
        this.bypassDocumentValidation = bypassDocumentValidation;
        return this;
    }

    @Override
    public WriteConcernResult execute(WriteBinding binding) {
        try {
            BulkWriteResult result = this.getMixedBulkOperation().execute(binding);
            if (result.wasAcknowledged()) {
                return this.translateBulkWriteResult(result);
            }
            return WriteConcernResult.unacknowledged();
        } catch (MongoBulkWriteException e2) {
            throw this.convertBulkWriteException(e2);
        }
    }

    @Override
    public void executeAsync(AsyncWriteBinding binding, final SingleResultCallback<WriteConcernResult> callback) {
        this.getMixedBulkOperation().executeAsync(binding, new SingleResultCallback<BulkWriteResult>(){

            @Override
            public void onResult(BulkWriteResult result, Throwable t2) {
                if (t2 != null) {
                    if (t2 instanceof MongoBulkWriteException) {
                        callback.onResult(null, BaseWriteOperation.this.convertBulkWriteException((MongoBulkWriteException)t2));
                    } else {
                        callback.onResult(null, t2);
                    }
                } else if (result.wasAcknowledged()) {
                    callback.onResult(BaseWriteOperation.this.translateBulkWriteResult(result), null);
                } else {
                    callback.onResult(WriteConcernResult.unacknowledged(), null);
                }
            }
        });
    }

    private MixedBulkWriteOperation getMixedBulkOperation() {
        return new MixedBulkWriteOperation(this.namespace, this.getWriteRequests(), this.ordered, this.writeConcern, this.retryWrites).bypassDocumentValidation(this.bypassDocumentValidation);
    }

    private MongoException convertBulkWriteException(MongoBulkWriteException e2) {
        BulkWriteError lastError = this.getLastError(e2);
        if (lastError != null) {
            if (ErrorCategory.fromErrorCode(lastError.getCode()) == ErrorCategory.DUPLICATE_KEY) {
                return new DuplicateKeyException(this.manufactureGetLastErrorResponse(e2), e2.getServerAddress(), this.translateBulkWriteResult(e2.getWriteResult()));
            }
            return new WriteConcernException(this.manufactureGetLastErrorResponse(e2), e2.getServerAddress(), this.translateBulkWriteResult(e2.getWriteResult()));
        }
        return new WriteConcernException(this.manufactureGetLastErrorResponse(e2), e2.getServerAddress(), this.translateBulkWriteResult(e2.getWriteResult()));
    }

    private BsonDocument manufactureGetLastErrorResponse(MongoBulkWriteException e2) {
        BsonDocument response = new BsonDocument();
        this.addBulkWriteResultToResponse(e2.getWriteResult(), response);
        if (e2.getWriteConcernError() != null) {
            response.putAll(e2.getWriteConcernError().getDetails());
        }
        if (this.getLastError(e2) != null) {
            response.put("err", new BsonString(this.getLastError(e2).getMessage()));
            response.put("code", new BsonInt32(this.getLastError(e2).getCode()));
            response.putAll(this.getLastError(e2).getDetails());
        } else if (e2.getWriteConcernError() != null) {
            response.put("err", new BsonString(e2.getWriteConcernError().getMessage()));
            response.put("code", new BsonInt32(e2.getWriteConcernError().getCode()));
        }
        return response;
    }

    private void addBulkWriteResultToResponse(BulkWriteResult bulkWriteResult, BsonDocument response) {
        response.put("ok", new BsonInt32(1));
        if (this.getType() == WriteRequest.Type.INSERT) {
            response.put("n", new BsonInt32(0));
        } else if (this.getType() == WriteRequest.Type.DELETE) {
            response.put("n", new BsonInt32(bulkWriteResult.getDeletedCount()));
        } else if (this.getType() == WriteRequest.Type.UPDATE || this.getType() == WriteRequest.Type.REPLACE) {
            response.put("n", new BsonInt32(bulkWriteResult.getMatchedCount() + bulkWriteResult.getUpserts().size()));
            if (bulkWriteResult.getUpserts().isEmpty()) {
                response.put("updatedExisting", BsonBoolean.TRUE);
            } else {
                response.put("updatedExisting", BsonBoolean.FALSE);
                response.put("upserted", bulkWriteResult.getUpserts().get(0).getId());
            }
        }
    }

    private WriteConcernResult translateBulkWriteResult(BulkWriteResult bulkWriteResult) {
        return WriteConcernResult.acknowledged(this.getCount(bulkWriteResult), this.getUpdatedExisting(bulkWriteResult), bulkWriteResult.getUpserts().isEmpty() ? null : bulkWriteResult.getUpserts().get(0).getId());
    }

    private int getCount(BulkWriteResult bulkWriteResult) {
        int count = 0;
        if (this.getType() == WriteRequest.Type.UPDATE || this.getType() == WriteRequest.Type.REPLACE) {
            count = bulkWriteResult.getMatchedCount() + bulkWriteResult.getUpserts().size();
        } else if (this.getType() == WriteRequest.Type.DELETE) {
            count = bulkWriteResult.getDeletedCount();
        }
        return count;
    }

    private boolean getUpdatedExisting(BulkWriteResult bulkWriteResult) {
        if (this.getType() == WriteRequest.Type.UPDATE) {
            return bulkWriteResult.getMatchedCount() > 0;
        }
        return false;
    }

    private BulkWriteError getLastError(MongoBulkWriteException e2) {
        return e2.getWriteErrors().isEmpty() ? null : e2.getWriteErrors().get(e2.getWriteErrors().size() - 1);
    }
}

