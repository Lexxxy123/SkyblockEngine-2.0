/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.MongoChangeStreamException;
import com.mongodb.MongoException;
import com.mongodb.async.AsyncAggregateResponseBatchCursor;
import com.mongodb.async.AsyncBatchCursor;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.binding.AsyncConnectionSource;
import com.mongodb.binding.AsyncReadBinding;
import com.mongodb.internal.async.ErrorHandlingResultCallback;
import com.mongodb.operation.ChangeStreamBatchCursorHelper;
import com.mongodb.operation.ChangeStreamOperation;
import com.mongodb.operation.OperationHelper;
import java.util.ArrayList;
import java.util.List;
import org.bson.BsonDocument;
import org.bson.BsonTimestamp;
import org.bson.RawBsonDocument;

final class AsyncChangeStreamBatchCursor<T>
implements AsyncAggregateResponseBatchCursor<T> {
    private final AsyncReadBinding binding;
    private final ChangeStreamOperation<T> changeStreamOperation;
    private volatile BsonDocument resumeToken;
    private volatile AsyncAggregateResponseBatchCursor<RawBsonDocument> wrapped;
    private boolean isClosed = false;
    private boolean isOperationInProgress = false;
    private boolean isClosePending = false;

    AsyncChangeStreamBatchCursor(ChangeStreamOperation<T> changeStreamOperation, AsyncAggregateResponseBatchCursor<RawBsonDocument> wrapped, AsyncReadBinding binding, BsonDocument resumeToken) {
        this.changeStreamOperation = changeStreamOperation;
        this.wrapped = wrapped;
        this.binding = binding;
        binding.retain();
        this.resumeToken = resumeToken;
    }

    AsyncAggregateResponseBatchCursor<RawBsonDocument> getWrapped() {
        return this.wrapped;
    }

    @Override
    public void next(SingleResultCallback<List<T>> callback) {
        this.resumeableOperation(new AsyncBlock(){

            @Override
            public void apply(AsyncAggregateResponseBatchCursor<RawBsonDocument> cursor, SingleResultCallback<List<RawBsonDocument>> callback) {
                cursor.next(callback);
                AsyncChangeStreamBatchCursor.this.cachePostBatchResumeToken(cursor);
            }
        }, this.convertResultsCallback(callback), false);
    }

    @Override
    public void tryNext(SingleResultCallback<List<T>> callback) {
        this.resumeableOperation(new AsyncBlock(){

            @Override
            public void apply(AsyncAggregateResponseBatchCursor<RawBsonDocument> cursor, SingleResultCallback<List<RawBsonDocument>> callback) {
                cursor.tryNext(callback);
                AsyncChangeStreamBatchCursor.this.cachePostBatchResumeToken(cursor);
            }
        }, this.convertResultsCallback(callback), true);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void close() {
        boolean closeCursor = false;
        AsyncChangeStreamBatchCursor asyncChangeStreamBatchCursor = this;
        synchronized (asyncChangeStreamBatchCursor) {
            if (this.isOperationInProgress) {
                this.isClosePending = true;
            } else {
                closeCursor = !this.isClosed;
                this.isClosed = true;
                this.isClosePending = false;
            }
        }
        if (closeCursor) {
            this.wrapped.close();
            this.binding.release();
        }
    }

    @Override
    public void setBatchSize(int batchSize) {
        this.wrapped.setBatchSize(batchSize);
    }

    @Override
    public int getBatchSize() {
        return this.wrapped.getBatchSize();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean isClosed() {
        AsyncChangeStreamBatchCursor asyncChangeStreamBatchCursor = this;
        synchronized (asyncChangeStreamBatchCursor) {
            return this.isClosed;
        }
    }

    @Override
    public BsonDocument getPostBatchResumeToken() {
        return this.wrapped.getPostBatchResumeToken();
    }

    @Override
    public BsonTimestamp getOperationTime() {
        return this.changeStreamOperation.getStartAtOperationTime();
    }

    @Override
    public boolean isFirstBatchEmpty() {
        return this.wrapped.isFirstBatchEmpty();
    }

    private void cachePostBatchResumeToken(AsyncAggregateResponseBatchCursor<RawBsonDocument> queryBatchCursor) {
        if (queryBatchCursor.getPostBatchResumeToken() != null) {
            this.resumeToken = queryBatchCursor.getPostBatchResumeToken();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void endOperationInProgress() {
        boolean closePending = false;
        AsyncChangeStreamBatchCursor asyncChangeStreamBatchCursor = this;
        synchronized (asyncChangeStreamBatchCursor) {
            this.isOperationInProgress = false;
            closePending = this.isClosePending;
        }
        if (closePending) {
            this.close();
        }
    }

    private SingleResultCallback<List<RawBsonDocument>> convertResultsCallback(final SingleResultCallback<List<T>> callback) {
        return ErrorHandlingResultCallback.errorHandlingCallback(new SingleResultCallback<List<RawBsonDocument>>(){

            @Override
            public void onResult(List<RawBsonDocument> rawDocuments, Throwable t2) {
                if (t2 != null) {
                    callback.onResult(null, t2);
                } else if (rawDocuments != null) {
                    ArrayList results = new ArrayList();
                    for (RawBsonDocument rawDocument : rawDocuments) {
                        if (!rawDocument.containsKey("_id")) {
                            callback.onResult(null, new MongoChangeStreamException("Cannot provide resume functionality when the resume token is missing."));
                            return;
                        }
                        try {
                            results.add(rawDocument.decode(AsyncChangeStreamBatchCursor.this.changeStreamOperation.getDecoder()));
                        } catch (Exception e2) {
                            callback.onResult(null, e2);
                            return;
                        }
                    }
                    AsyncChangeStreamBatchCursor.this.resumeToken = rawDocuments.get(rawDocuments.size() - 1).getDocument("_id");
                    callback.onResult(results, null);
                } else {
                    callback.onResult(null, null);
                }
            }
        }, OperationHelper.LOGGER);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void resumeableOperation(final AsyncBlock asyncBlock, final SingleResultCallback<List<RawBsonDocument>> callback, final boolean tryNext) {
        AsyncChangeStreamBatchCursor asyncChangeStreamBatchCursor = this;
        synchronized (asyncChangeStreamBatchCursor) {
            if (this.isClosed) {
                callback.onResult(null, new MongoException(String.format("%s called after the cursor was closed.", tryNext ? "tryNext()" : "next()")));
                return;
            }
            this.isOperationInProgress = true;
        }
        asyncBlock.apply(this.wrapped, new SingleResultCallback<List<RawBsonDocument>>(){

            @Override
            public void onResult(List<RawBsonDocument> result, Throwable t2) {
                if (t2 == null) {
                    AsyncChangeStreamBatchCursor.this.endOperationInProgress();
                    callback.onResult(result, null);
                } else if (ChangeStreamBatchCursorHelper.isRetryableError(t2)) {
                    AsyncChangeStreamBatchCursor.this.wrapped.close();
                    AsyncChangeStreamBatchCursor.this.retryOperation(asyncBlock, callback, tryNext);
                } else {
                    AsyncChangeStreamBatchCursor.this.endOperationInProgress();
                    callback.onResult(null, t2);
                }
            }
        });
    }

    private void retryOperation(final AsyncBlock asyncBlock, final SingleResultCallback<List<RawBsonDocument>> callback, final boolean tryNext) {
        OperationHelper.withAsyncReadConnection(this.binding, new OperationHelper.AsyncCallableWithSource(){

            @Override
            public void call(AsyncConnectionSource source, Throwable t2) {
                if (t2 != null) {
                    callback.onResult(null, t2);
                } else {
                    AsyncChangeStreamBatchCursor.this.changeStreamOperation.setChangeStreamOptionsForResume(AsyncChangeStreamBatchCursor.this.resumeToken, source.getServerDescription().getMaxWireVersion());
                    source.release();
                    AsyncChangeStreamBatchCursor.this.changeStreamOperation.executeAsync(AsyncChangeStreamBatchCursor.this.binding, new SingleResultCallback<AsyncBatchCursor<T>>(){

                        @Override
                        public void onResult(AsyncBatchCursor<T> result, Throwable t2) {
                            if (t2 != null) {
                                callback.onResult(null, t2);
                            } else {
                                AsyncChangeStreamBatchCursor.this.wrapped = ((AsyncChangeStreamBatchCursor)result).getWrapped();
                                AsyncChangeStreamBatchCursor.this.binding.release();
                                AsyncChangeStreamBatchCursor.this.resumeableOperation(asyncBlock, callback, tryNext);
                            }
                        }
                    });
                }
            }
        });
    }

    private static interface AsyncBlock {
        public void apply(AsyncAggregateResponseBatchCursor<RawBsonDocument> var1, SingleResultCallback<List<RawBsonDocument>> var2);
    }
}

