/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.internal;

import com.mongodb.ServerAddress;
import com.mongodb.ServerCursor;
import com.mongodb.client.MongoChangeStreamCursor;
import com.mongodb.lang.Nullable;
import com.mongodb.operation.AggregateResponseBatchCursor;
import com.mongodb.operation.BatchCursor;
import java.util.List;
import java.util.NoSuchElementException;
import org.bson.BsonDocument;
import org.bson.RawBsonDocument;
import org.bson.codecs.Decoder;

public class MongoChangeStreamCursorImpl<T>
implements MongoChangeStreamCursor<T> {
    private final AggregateResponseBatchCursor<RawBsonDocument> batchCursor;
    private final Decoder<T> decoder;
    private List<RawBsonDocument> curBatch;
    private int curPos;
    private BsonDocument resumeToken;

    public MongoChangeStreamCursorImpl(BatchCursor<RawBsonDocument> batchCursor, Decoder<T> decoder, @Nullable BsonDocument initialResumeToken) {
        this.batchCursor = (AggregateResponseBatchCursor)batchCursor;
        this.decoder = decoder;
        this.resumeToken = initialResumeToken;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Cursors do not support removal");
    }

    @Override
    public void close() {
        this.batchCursor.close();
    }

    @Override
    public boolean hasNext() {
        return this.curBatch != null || this.batchCursor.hasNext();
    }

    @Override
    public T next() {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        if (this.curBatch == null) {
            this.curBatch = this.batchCursor.next();
        }
        return this.getNextInBatch();
    }

    @Override
    @Nullable
    public T tryNext() {
        if (this.curBatch == null) {
            this.curBatch = this.batchCursor.tryNext();
        }
        if (this.curBatch == null && this.batchCursor.getPostBatchResumeToken() != null) {
            this.resumeToken = this.batchCursor.getPostBatchResumeToken();
        }
        return this.curBatch == null ? null : (T)this.getNextInBatch();
    }

    @Override
    @Nullable
    public ServerCursor getServerCursor() {
        return this.batchCursor.getServerCursor();
    }

    @Override
    public ServerAddress getServerAddress() {
        return this.batchCursor.getServerAddress();
    }

    private T getNextInBatch() {
        RawBsonDocument nextInBatch = this.curBatch.get(this.curPos);
        this.resumeToken = nextInBatch.getDocument("_id");
        if (this.curPos < this.curBatch.size() - 1) {
            ++this.curPos;
        } else {
            this.curBatch = null;
            this.curPos = 0;
            if (this.batchCursor.getPostBatchResumeToken() != null) {
                this.resumeToken = this.batchCursor.getPostBatchResumeToken();
            }
        }
        return nextInBatch.decode(this.decoder);
    }

    @Override
    @Nullable
    public BsonDocument getResumeToken() {
        return this.resumeToken;
    }
}

