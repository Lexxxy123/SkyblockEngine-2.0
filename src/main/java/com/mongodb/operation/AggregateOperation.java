/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.ExplainVerbosity;
import com.mongodb.MongoNamespace;
import com.mongodb.async.AsyncBatchCursor;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.binding.AsyncReadBinding;
import com.mongodb.binding.ReadBinding;
import com.mongodb.client.model.AggregationLevel;
import com.mongodb.client.model.Collation;
import com.mongodb.operation.AggregateExplainOperation;
import com.mongodb.operation.AggregateOperationImpl;
import com.mongodb.operation.AsyncReadOperation;
import com.mongodb.operation.BatchCursor;
import com.mongodb.operation.ReadOperation;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.bson.codecs.Decoder;

@Deprecated
public class AggregateOperation<T>
implements AsyncReadOperation<AsyncBatchCursor<T>>,
ReadOperation<BatchCursor<T>> {
    private final AggregateOperationImpl<T> wrapped;

    public AggregateOperation(MongoNamespace namespace, List<BsonDocument> pipeline, Decoder<T> decoder) {
        this(namespace, pipeline, decoder, AggregationLevel.COLLECTION);
    }

    public AggregateOperation(MongoNamespace namespace, List<BsonDocument> pipeline, Decoder<T> decoder, AggregationLevel aggregationLevel) {
        this.wrapped = new AggregateOperationImpl<T>(namespace, pipeline, decoder, aggregationLevel);
    }

    public List<BsonDocument> getPipeline() {
        return this.wrapped.getPipeline();
    }

    public Boolean getAllowDiskUse() {
        return this.wrapped.getAllowDiskUse();
    }

    public AggregateOperation<T> allowDiskUse(Boolean allowDiskUse) {
        this.wrapped.allowDiskUse(allowDiskUse);
        return this;
    }

    public Integer getBatchSize() {
        return this.wrapped.getBatchSize();
    }

    public AggregateOperation<T> batchSize(Integer batchSize) {
        this.wrapped.batchSize(batchSize);
        return this;
    }

    public long getMaxAwaitTime(TimeUnit timeUnit) {
        return this.wrapped.getMaxAwaitTime(timeUnit);
    }

    public AggregateOperation<T> maxAwaitTime(long maxAwaitTime, TimeUnit timeUnit) {
        this.wrapped.maxAwaitTime(maxAwaitTime, timeUnit);
        return this;
    }

    public long getMaxTime(TimeUnit timeUnit) {
        return this.wrapped.getMaxTime(timeUnit);
    }

    public AggregateOperation<T> maxTime(long maxTime, TimeUnit timeUnit) {
        this.wrapped.maxTime(maxTime, timeUnit);
        return this;
    }

    @Deprecated
    public Boolean getUseCursor() {
        return this.wrapped.getUseCursor();
    }

    @Deprecated
    public AggregateOperation<T> useCursor(Boolean useCursor) {
        this.wrapped.useCursor(useCursor);
        return this;
    }

    public Collation getCollation() {
        return this.wrapped.getCollation();
    }

    public AggregateOperation<T> collation(Collation collation) {
        this.wrapped.collation(collation);
        return this;
    }

    public String getComment() {
        return this.wrapped.getComment();
    }

    public AggregateOperation<T> comment(String comment) {
        this.wrapped.comment(comment);
        return this;
    }

    public AggregateOperation<T> retryReads(boolean retryReads) {
        this.wrapped.retryReads(retryReads);
        return this;
    }

    public boolean getRetryReads() {
        return this.wrapped.getRetryReads();
    }

    public BsonDocument getHint() {
        BsonValue hint = this.wrapped.getHint();
        if (hint == null) {
            return null;
        }
        if (!hint.isDocument()) {
            throw new IllegalArgumentException("Hint is not a BsonDocument please use the #getHintBsonValue() method. ");
        }
        return hint.asDocument();
    }

    public BsonValue getHintBsonValue() {
        return this.wrapped.getHint();
    }

    public AggregateOperation<T> hint(BsonValue hint) {
        this.wrapped.hint(hint);
        return this;
    }

    @Override
    public BatchCursor<T> execute(ReadBinding binding) {
        return this.wrapped.execute(binding);
    }

    @Override
    public void executeAsync(AsyncReadBinding binding, SingleResultCallback<AsyncBatchCursor<T>> callback) {
        this.wrapped.executeAsync(binding, callback);
    }

    public ReadOperation<BsonDocument> asExplainableOperation(ExplainVerbosity explainVerbosity) {
        return new AggregateExplainOperation(this.getNamespace(), this.getPipeline()).allowDiskUse(this.getAllowDiskUse()).maxTime(this.getMaxAwaitTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).hint(this.wrapped.getHint()).retryReads(this.getRetryReads());
    }

    public AsyncReadOperation<BsonDocument> asExplainableOperationAsync(ExplainVerbosity explainVerbosity) {
        return new AggregateExplainOperation(this.getNamespace(), this.getPipeline()).retryReads(this.getRetryReads()).allowDiskUse(this.getAllowDiskUse()).maxTime(this.getMaxAwaitTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).hint(this.wrapped.getHint());
    }

    MongoNamespace getNamespace() {
        return this.wrapped.getNamespace();
    }

    Decoder<T> getDecoder() {
        return this.wrapped.getDecoder();
    }

    public String toString() {
        return "AggregateOperation{namespace=" + this.getNamespace() + ", pipeline=" + this.getPipeline() + ", decoder=" + this.getDecoder() + ", allowDiskUse=" + this.getAllowDiskUse() + ", batchSize=" + this.getBatchSize() + ", collation=" + this.getCollation() + ", comment=" + this.getComment() + ", hint=" + this.getHint() + ", maxAwaitTimeMS=" + this.getMaxAwaitTime(TimeUnit.MILLISECONDS) + ", maxTimeMS=" + this.getMaxTime(TimeUnit.MILLISECONDS) + ", useCursor=" + this.wrapped.getUseCursor() + "}";
    }
}

