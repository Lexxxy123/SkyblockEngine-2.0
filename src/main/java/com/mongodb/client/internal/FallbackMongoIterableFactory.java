/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.internal;

import com.mongodb.MongoNamespace;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.ClientSession;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.ListCollectionsIterable;
import com.mongodb.client.ListDatabasesIterable;
import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.MapReduceIterable;
import com.mongodb.client.internal.AggregateIterableImpl;
import com.mongodb.client.internal.ChangeStreamIterableImpl;
import com.mongodb.client.internal.DistinctIterableImpl;
import com.mongodb.client.internal.FindIterableImpl;
import com.mongodb.client.internal.ListCollectionsIterableImpl;
import com.mongodb.client.internal.ListDatabasesIterableImpl;
import com.mongodb.client.internal.ListIndexesIterableImpl;
import com.mongodb.client.internal.MapReduceIterableImpl;
import com.mongodb.client.internal.MongoIterableFactory;
import com.mongodb.client.internal.OperationExecutor;
import com.mongodb.client.model.AggregationLevel;
import com.mongodb.client.model.changestream.ChangeStreamLevel;
import com.mongodb.lang.Nullable;
import java.util.List;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

class FallbackMongoIterableFactory
implements MongoIterableFactory {
    FallbackMongoIterableFactory() {
    }

    @Override
    public <TDocument, TResult> FindIterable<TResult> findOf(@Nullable ClientSession clientSession, MongoNamespace namespace, Class<TDocument> documentClass, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, OperationExecutor executor, Bson filter, boolean retryReads) {
        return new FindIterableImpl<TDocument, TResult>(clientSession, namespace, documentClass, resultClass, codecRegistry, readPreference, readConcern, executor, filter, retryReads);
    }

    @Override
    public <TDocument, TResult> AggregateIterable<TResult> aggregateOf(@Nullable ClientSession clientSession, MongoNamespace namespace, Class<TDocument> documentClass, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, WriteConcern writeConcern, OperationExecutor executor, List<? extends Bson> pipeline, AggregationLevel aggregationLevel, boolean retryReads) {
        return new AggregateIterableImpl<TDocument, TResult>(clientSession, namespace, documentClass, resultClass, codecRegistry, readPreference, readConcern, writeConcern, executor, pipeline, aggregationLevel, retryReads);
    }

    @Override
    public <TDocument, TResult> AggregateIterable<TResult> aggregateOf(@Nullable ClientSession clientSession, String databaseName, Class<TDocument> documentClass, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, WriteConcern writeConcern, OperationExecutor executor, List<? extends Bson> pipeline, AggregationLevel aggregationLevel, boolean retryReads) {
        return new AggregateIterableImpl<TDocument, TResult>(clientSession, databaseName, documentClass, resultClass, codecRegistry, readPreference, readConcern, writeConcern, executor, pipeline, aggregationLevel, retryReads);
    }

    @Override
    public <TResult> ChangeStreamIterable<TResult> changeStreamOf(@Nullable ClientSession clientSession, String databaseName, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, OperationExecutor executor, List<? extends Bson> pipeline, Class<TResult> resultClass, ChangeStreamLevel changeStreamLevel, boolean retryReads) {
        return new ChangeStreamIterableImpl<TResult>(clientSession, databaseName, codecRegistry, readPreference, readConcern, executor, pipeline, resultClass, changeStreamLevel, retryReads);
    }

    @Override
    public <TResult> ChangeStreamIterable<TResult> changeStreamOf(@Nullable ClientSession clientSession, MongoNamespace namespace, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, OperationExecutor executor, List<? extends Bson> pipeline, Class<TResult> resultClass, ChangeStreamLevel changeStreamLevel, boolean retryReads) {
        return new ChangeStreamIterableImpl<TResult>(clientSession, namespace, codecRegistry, readPreference, readConcern, executor, pipeline, resultClass, changeStreamLevel, retryReads);
    }

    @Override
    public <TDocument, TResult> DistinctIterable<TResult> distinctOf(@Nullable ClientSession clientSession, MongoNamespace namespace, Class<TDocument> documentClass, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, OperationExecutor executor, String fieldName, Bson filter, boolean retryReads) {
        return new DistinctIterableImpl<TDocument, TResult>(clientSession, namespace, documentClass, resultClass, codecRegistry, readPreference, readConcern, executor, fieldName, filter, retryReads);
    }

    @Override
    public <TResult> ListDatabasesIterable<TResult> listDatabasesOf(@Nullable ClientSession clientSession, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, OperationExecutor executor, boolean retryReads) {
        return new ListDatabasesIterableImpl<TResult>(clientSession, resultClass, codecRegistry, readPreference, executor, retryReads);
    }

    @Override
    public <TResult> ListCollectionsIterable<TResult> listCollectionsOf(@Nullable ClientSession clientSession, String databaseName, boolean collectionNamesOnly, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, OperationExecutor executor, boolean retryReads) {
        return new ListCollectionsIterableImpl<TResult>(clientSession, databaseName, collectionNamesOnly, resultClass, codecRegistry, readPreference, executor, retryReads);
    }

    @Override
    public <TResult> ListIndexesIterable<TResult> listIndexesOf(@Nullable ClientSession clientSession, MongoNamespace namespace, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, OperationExecutor executor, boolean retryReads) {
        return new ListIndexesIterableImpl<TResult>(clientSession, namespace, resultClass, codecRegistry, readPreference, executor, retryReads);
    }

    @Override
    public <TDocument, TResult> MapReduceIterable<TResult> mapReduceOf(@Nullable ClientSession clientSession, MongoNamespace namespace, Class<TDocument> documentClass, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, WriteConcern writeConcern, OperationExecutor executor, String mapFunction, String reduceFunction) {
        return new MapReduceIterableImpl<TDocument, TResult>(clientSession, namespace, documentClass, resultClass, codecRegistry, readPreference, readConcern, writeConcern, executor, mapFunction, reduceFunction);
    }
}

