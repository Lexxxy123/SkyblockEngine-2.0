/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.MongoNamespace;
import com.mongodb.assertions.Assertions;
import com.mongodb.async.AsyncAggregateResponseBatchCursor;
import com.mongodb.async.AsyncBatchCursor;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.binding.AsyncConnectionSource;
import com.mongodb.binding.AsyncReadBinding;
import com.mongodb.binding.ConnectionSource;
import com.mongodb.binding.ReadBinding;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.changestream.ChangeStreamLevel;
import com.mongodb.client.model.changestream.FullDocument;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.operation.AggregateOperationImpl;
import com.mongodb.operation.AggregateResponseBatchCursor;
import com.mongodb.operation.AsyncChangeStreamBatchCursor;
import com.mongodb.operation.AsyncReadOperation;
import com.mongodb.operation.BatchCursor;
import com.mongodb.operation.ChangeStreamBatchCursor;
import com.mongodb.operation.OperationHelper;
import com.mongodb.operation.ReadOperation;
import com.mongodb.session.SessionContext;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.bson.BsonArray;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;
import org.bson.BsonTimestamp;
import org.bson.BsonValue;
import org.bson.RawBsonDocument;
import org.bson.codecs.Decoder;
import org.bson.codecs.RawBsonDocumentCodec;

@Deprecated
public class ChangeStreamOperation<T>
implements AsyncReadOperation<AsyncBatchCursor<T>>,
ReadOperation<BatchCursor<T>> {
    private static final RawBsonDocumentCodec RAW_BSON_DOCUMENT_CODEC = new RawBsonDocumentCodec();
    private final AggregateOperationImpl<RawBsonDocument> wrapped;
    private final FullDocument fullDocument;
    private final Decoder<T> decoder;
    private final ChangeStreamLevel changeStreamLevel;
    private BsonDocument resumeAfter;
    private BsonDocument startAfter;
    private BsonTimestamp startAtOperationTime;

    public ChangeStreamOperation(MongoNamespace namespace, FullDocument fullDocument, List<BsonDocument> pipeline, Decoder<T> decoder) {
        this(namespace, fullDocument, pipeline, decoder, ChangeStreamLevel.COLLECTION);
    }

    public ChangeStreamOperation(MongoNamespace namespace, FullDocument fullDocument, List<BsonDocument> pipeline, Decoder<T> decoder, ChangeStreamLevel changeStreamLevel) {
        this.wrapped = new AggregateOperationImpl<RawBsonDocument>(namespace, pipeline, RAW_BSON_DOCUMENT_CODEC, this.getAggregateTarget(), this.getPipelineCreator());
        this.fullDocument = Assertions.notNull("fullDocument", fullDocument);
        this.decoder = Assertions.notNull("decoder", decoder);
        this.changeStreamLevel = Assertions.notNull("changeStreamLevel", changeStreamLevel);
    }

    public MongoNamespace getNamespace() {
        return this.wrapped.getNamespace();
    }

    public Decoder<T> getDecoder() {
        return this.decoder;
    }

    public FullDocument getFullDocument() {
        return this.fullDocument;
    }

    @Deprecated
    public BsonDocument getResumeToken() {
        return this.resumeAfter;
    }

    public BsonDocument getResumeAfter() {
        return this.resumeAfter;
    }

    public ChangeStreamOperation<T> resumeAfter(BsonDocument resumeAfter) {
        this.resumeAfter = resumeAfter;
        return this;
    }

    public BsonDocument getStartAfter() {
        return this.startAfter;
    }

    public ChangeStreamOperation<T> startAfter(BsonDocument startAfter) {
        this.startAfter = startAfter;
        return this;
    }

    public List<BsonDocument> getPipeline() {
        return this.wrapped.getPipeline();
    }

    public Integer getBatchSize() {
        return this.wrapped.getBatchSize();
    }

    public ChangeStreamOperation<T> batchSize(Integer batchSize) {
        this.wrapped.batchSize(batchSize);
        return this;
    }

    public long getMaxAwaitTime(TimeUnit timeUnit) {
        return this.wrapped.getMaxAwaitTime(timeUnit);
    }

    public ChangeStreamOperation<T> maxAwaitTime(long maxAwaitTime, TimeUnit timeUnit) {
        this.wrapped.maxAwaitTime(maxAwaitTime, timeUnit);
        return this;
    }

    public Collation getCollation() {
        return this.wrapped.getCollation();
    }

    public ChangeStreamOperation<T> collation(Collation collation) {
        this.wrapped.collation(collation);
        return this;
    }

    public ChangeStreamOperation<T> startAtOperationTime(BsonTimestamp startAtOperationTime) {
        this.startAtOperationTime = startAtOperationTime;
        return this;
    }

    public BsonTimestamp getStartAtOperationTime() {
        return this.startAtOperationTime;
    }

    public ChangeStreamOperation<T> retryReads(boolean retryReads) {
        this.wrapped.retryReads(retryReads);
        return this;
    }

    public boolean getRetryReads() {
        return this.wrapped.getRetryReads();
    }

    @Override
    public BatchCursor<T> execute(final ReadBinding binding) {
        return (BatchCursor)OperationHelper.withReadConnectionSource(binding, new OperationHelper.CallableWithSource<BatchCursor<T>>(){

            @Override
            public BatchCursor<T> call(ConnectionSource source) {
                AggregateResponseBatchCursor cursor = (AggregateResponseBatchCursor)ChangeStreamOperation.this.wrapped.execute(binding);
                return new ChangeStreamBatchCursor(ChangeStreamOperation.this, cursor, binding, ChangeStreamOperation.this.setChangeStreamOptions(cursor.getPostBatchResumeToken(), cursor.getOperationTime(), source.getServerDescription().getMaxWireVersion(), cursor.isFirstBatchEmpty()));
            }
        });
    }

    @Override
    public void executeAsync(final AsyncReadBinding binding, final SingleResultCallback<AsyncBatchCursor<T>> callback) {
        this.wrapped.executeAsync(binding, new SingleResultCallback<AsyncBatchCursor<RawBsonDocument>>(){

            @Override
            public void onResult(AsyncBatchCursor<RawBsonDocument> result, Throwable t2) {
                if (t2 != null) {
                    callback.onResult(null, t2);
                } else {
                    final AsyncAggregateResponseBatchCursor cursor = (AsyncAggregateResponseBatchCursor)result;
                    OperationHelper.withAsyncReadConnection(binding, new OperationHelper.AsyncCallableWithSource(){

                        @Override
                        public void call(AsyncConnectionSource source, Throwable t2) {
                            if (t2 != null) {
                                callback.onResult(null, t2);
                            } else {
                                callback.onResult(new AsyncChangeStreamBatchCursor(ChangeStreamOperation.this, cursor, binding, ChangeStreamOperation.this.setChangeStreamOptions(cursor.getPostBatchResumeToken(), cursor.getOperationTime(), source.getServerDescription().getMaxWireVersion(), cursor.isFirstBatchEmpty())), null);
                            }
                            source.release();
                        }
                    });
                }
            }
        });
    }

    private BsonDocument setChangeStreamOptions(BsonDocument postBatchResumeToken, BsonTimestamp operationTime, int maxWireVersion, boolean firstBatchEmpty) {
        BsonDocument resumeToken = null;
        if (this.startAfter != null) {
            resumeToken = this.startAfter;
        } else if (this.resumeAfter != null) {
            resumeToken = this.resumeAfter;
        } else if (this.startAtOperationTime == null && postBatchResumeToken == null && firstBatchEmpty && maxWireVersion >= 7) {
            this.startAtOperationTime = operationTime;
        }
        return resumeToken;
    }

    public void setChangeStreamOptionsForResume(BsonDocument resumeToken, int maxWireVersion) {
        this.startAfter = null;
        if (resumeToken != null) {
            this.startAtOperationTime = null;
            this.resumeAfter = resumeToken;
        } else if (this.startAtOperationTime != null && maxWireVersion >= 7) {
            this.resumeAfter = null;
        } else {
            this.resumeAfter = null;
            this.startAtOperationTime = null;
        }
    }

    private AggregateOperationImpl.AggregateTarget getAggregateTarget() {
        return new AggregateOperationImpl.AggregateTarget(){

            @Override
            public BsonValue create() {
                return ChangeStreamOperation.this.changeStreamLevel == ChangeStreamLevel.COLLECTION ? new BsonString(ChangeStreamOperation.this.getNamespace().getCollectionName()) : new BsonInt32(1);
            }
        };
    }

    private AggregateOperationImpl.PipelineCreator getPipelineCreator() {
        return new AggregateOperationImpl.PipelineCreator(){

            @Override
            public BsonArray create(ConnectionDescription description, SessionContext sessionContext) {
                ArrayList<BsonDocument> changeStreamPipeline = new ArrayList<BsonDocument>();
                BsonDocument changeStream = new BsonDocument("fullDocument", new BsonString(ChangeStreamOperation.this.fullDocument.getValue()));
                if (ChangeStreamOperation.this.changeStreamLevel == ChangeStreamLevel.CLIENT) {
                    changeStream.append("allChangesForCluster", BsonBoolean.TRUE);
                }
                if (ChangeStreamOperation.this.resumeAfter != null) {
                    changeStream.append("resumeAfter", ChangeStreamOperation.this.resumeAfter);
                }
                if (ChangeStreamOperation.this.startAfter != null) {
                    changeStream.append("startAfter", ChangeStreamOperation.this.startAfter);
                }
                if (ChangeStreamOperation.this.startAtOperationTime != null) {
                    changeStream.append("startAtOperationTime", ChangeStreamOperation.this.startAtOperationTime);
                }
                changeStreamPipeline.add(new BsonDocument("$changeStream", changeStream));
                changeStreamPipeline.addAll(ChangeStreamOperation.this.getPipeline());
                return new BsonArray(changeStreamPipeline);
            }
        };
    }
}

