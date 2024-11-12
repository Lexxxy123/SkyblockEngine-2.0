/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.internal;

import com.mongodb.MongoNamespace;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.assertions.Assertions;
import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoChangeStreamCursor;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.internal.MongoChangeStreamCursorImpl;
import com.mongodb.client.internal.MongoIterableImpl;
import com.mongodb.client.internal.OperationExecutor;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.ChangeStreamLevel;
import com.mongodb.client.model.changestream.FullDocument;
import com.mongodb.lang.Nullable;
import com.mongodb.operation.BatchCursor;
import com.mongodb.operation.ChangeStreamOperation;
import com.mongodb.operation.ReadOperation;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.bson.BsonDocument;
import org.bson.BsonTimestamp;
import org.bson.RawBsonDocument;
import org.bson.codecs.Codec;
import org.bson.codecs.RawBsonDocumentCodec;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

class ChangeStreamIterableImpl<TResult>
extends MongoIterableImpl<ChangeStreamDocument<TResult>>
implements ChangeStreamIterable<TResult> {
    private final MongoNamespace namespace;
    private final CodecRegistry codecRegistry;
    private final List<? extends Bson> pipeline;
    private final Codec<ChangeStreamDocument<TResult>> codec;
    private final ChangeStreamLevel changeStreamLevel;
    private FullDocument fullDocument = FullDocument.DEFAULT;
    private BsonDocument resumeToken;
    private BsonDocument startAfter;
    private long maxAwaitTimeMS;
    private Collation collation;
    private BsonTimestamp startAtOperationTime;

    ChangeStreamIterableImpl(@Nullable ClientSession clientSession, String databaseName, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, OperationExecutor executor, List<? extends Bson> pipeline, Class<TResult> resultClass, ChangeStreamLevel changeStreamLevel, boolean retryReads) {
        this(clientSession, new MongoNamespace(databaseName, "ignored"), codecRegistry, readPreference, readConcern, executor, pipeline, resultClass, changeStreamLevel, retryReads);
    }

    ChangeStreamIterableImpl(@Nullable ClientSession clientSession, MongoNamespace namespace, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, OperationExecutor executor, List<? extends Bson> pipeline, Class<TResult> resultClass, ChangeStreamLevel changeStreamLevel, boolean retryReads) {
        super(clientSession, executor, readConcern, readPreference, retryReads);
        this.namespace = Assertions.notNull("namespace", namespace);
        this.codecRegistry = Assertions.notNull("codecRegistry", codecRegistry);
        this.pipeline = Assertions.notNull("pipeline", pipeline);
        this.codec = ChangeStreamDocument.createCodec(Assertions.notNull("resultClass", resultClass), codecRegistry);
        this.changeStreamLevel = Assertions.notNull("changeStreamLevel", changeStreamLevel);
    }

    @Override
    public ChangeStreamIterable<TResult> fullDocument(FullDocument fullDocument) {
        this.fullDocument = Assertions.notNull("fullDocument", fullDocument);
        return this;
    }

    @Override
    public ChangeStreamIterable<TResult> resumeAfter(BsonDocument resumeAfter) {
        this.resumeToken = Assertions.notNull("resumeAfter", resumeAfter);
        return this;
    }

    @Override
    public ChangeStreamIterable<TResult> batchSize(int batchSize) {
        super.batchSize(batchSize);
        return this;
    }

    @Override
    public ChangeStreamIterable<TResult> maxAwaitTime(long maxAwaitTime, TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        this.maxAwaitTimeMS = TimeUnit.MILLISECONDS.convert(maxAwaitTime, timeUnit);
        return this;
    }

    @Override
    public ChangeStreamIterable<TResult> collation(@Nullable Collation collation) {
        this.collation = Assertions.notNull("collation", collation);
        return this;
    }

    @Override
    public <TDocument> MongoIterable<TDocument> withDocumentClass(final Class<TDocument> clazz) {
        return new MongoIterableImpl<TDocument>(this.getClientSession(), this.getExecutor(), this.getReadConcern(), this.getReadPreference(), this.getRetryReads()){

            @Override
            public MongoCursor<TDocument> iterator() {
                return this.cursor();
            }

            @Override
            public MongoChangeStreamCursor<TDocument> cursor() {
                return new MongoChangeStreamCursorImpl(ChangeStreamIterableImpl.this.execute(), ChangeStreamIterableImpl.this.codecRegistry.get(clazz), ChangeStreamIterableImpl.this.initialResumeToken());
            }

            @Override
            public ReadOperation<BatchCursor<TDocument>> asReadOperation() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public ChangeStreamIterable<TResult> startAtOperationTime(BsonTimestamp startAtOperationTime) {
        this.startAtOperationTime = Assertions.notNull("startAtOperationTime", startAtOperationTime);
        return this;
    }

    @Override
    public ChangeStreamIterableImpl<TResult> startAfter(BsonDocument startAfter) {
        this.startAfter = Assertions.notNull("startAfter", startAfter);
        return this;
    }

    @Override
    public MongoCursor<ChangeStreamDocument<TResult>> iterator() {
        return this.cursor();
    }

    @Override
    public MongoChangeStreamCursor<ChangeStreamDocument<TResult>> cursor() {
        return new MongoChangeStreamCursorImpl<ChangeStreamDocument<TResult>>(this.execute(), this.codec, this.initialResumeToken());
    }

    @Override
    @Nullable
    public ChangeStreamDocument<TResult> first() {
        MongoCursor cursor = this.cursor();
        try {
            if (!cursor.hasNext()) {
                ChangeStreamDocument<TResult> changeStreamDocument = null;
                return changeStreamDocument;
            }
            ChangeStreamDocument changeStreamDocument = (ChangeStreamDocument)cursor.next();
            return changeStreamDocument;
        } finally {
            cursor.close();
        }
    }

    @Override
    public ReadOperation<BatchCursor<ChangeStreamDocument<TResult>>> asReadOperation() {
        throw new UnsupportedOperationException();
    }

    private ReadOperation<BatchCursor<RawBsonDocument>> createChangeStreamOperation() {
        return new ChangeStreamOperation<RawBsonDocument>(this.namespace, this.fullDocument, this.createBsonDocumentList(this.pipeline), new RawBsonDocumentCodec(), this.changeStreamLevel).batchSize(this.getBatchSize()).collation(this.collation).maxAwaitTime(this.maxAwaitTimeMS, TimeUnit.MILLISECONDS).resumeAfter(this.resumeToken).startAtOperationTime(this.startAtOperationTime).startAfter(this.startAfter).retryReads(this.getRetryReads());
    }

    private List<BsonDocument> createBsonDocumentList(List<? extends Bson> pipeline) {
        ArrayList<BsonDocument> aggregateList = new ArrayList<BsonDocument>(pipeline.size());
        for (Bson bson : pipeline) {
            if (bson == null) {
                throw new IllegalArgumentException("pipeline cannot contain a null value");
            }
            aggregateList.add(bson.toBsonDocument(BsonDocument.class, this.codecRegistry));
        }
        return aggregateList;
    }

    private BatchCursor<RawBsonDocument> execute() {
        return this.getExecutor().execute(this.createChangeStreamOperation(), this.getReadPreference(), this.getReadConcern(), this.getClientSession());
    }

    private BsonDocument initialResumeToken() {
        return this.startAfter != null ? this.startAfter : this.resumeToken;
    }
}

