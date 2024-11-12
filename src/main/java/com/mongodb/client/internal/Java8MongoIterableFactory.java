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
import com.mongodb.client.internal.Java8AggregateIterableImpl;
import com.mongodb.client.internal.Java8ChangeStreamIterableImpl;
import com.mongodb.client.internal.Java8DistinctIterableImpl;
import com.mongodb.client.internal.Java8FindIterableImpl;
import com.mongodb.client.internal.Java8ListCollectionsIterableImpl;
import com.mongodb.client.internal.Java8ListDatabasesIterableImpl;
import com.mongodb.client.internal.Java8ListIndexesIterableImpl;
import com.mongodb.client.internal.Java8MapReduceIterableImpl;
import com.mongodb.client.internal.MongoIterableFactory;
import com.mongodb.client.internal.OperationExecutor;
import com.mongodb.client.model.AggregationLevel;
import com.mongodb.client.model.changestream.ChangeStreamLevel;
import com.mongodb.lang.Nullable;
import java.util.List;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

class Java8MongoIterableFactory
implements MongoIterableFactory {
    Java8MongoIterableFactory() {
    }

    @Override
    public <TDocument, TResult> FindIterable<TResult> findOf(@Nullable ClientSession clientSession, MongoNamespace namespace, Class<TDocument> documentClass, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, OperationExecutor executor, Bson filter, boolean retryReads) {
        return new Java8FindIterableImpl<TDocument, TResult>(clientSession, namespace, documentClass, resultClass, codecRegistry, readPreference, readConcern, executor, filter, retryReads);
    }

    @Override
    public <TDocument, TResult> AggregateIterable<TResult> aggregateOf(@Nullable ClientSession clientSession, MongoNamespace namespace, Class<TDocument> documentClass, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, WriteConcern writeConcern, OperationExecutor executor, List<? extends Bson> pipeline, AggregationLevel aggregationLevel, boolean retryReads) {
        return new Java8AggregateIterableImpl<TDocument, TResult>(clientSession, namespace, documentClass, resultClass, codecRegistry, readPreference, readConcern, writeConcern, executor, pipeline, aggregationLevel, retryReads);
    }

    @Override
    public <TDocument, TResult> AggregateIterable<TResult> aggregateOf(@Nullable ClientSession clientSession, String databaseName, Class<TDocument> documentClass, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, WriteConcern writeConcern, OperationExecutor executor, List<? extends Bson> pipeline, AggregationLevel aggregationLevel, boolean retryReads) {
        return new Java8AggregateIterableImpl<TDocument, TResult>(clientSession, databaseName, documentClass, resultClass, codecRegistry, readPreference, readConcern, writeConcern, executor, pipeline, aggregationLevel, retryReads);
    }

    @Override
    public <TResult> ChangeStreamIterable<TResult> changeStreamOf(@Nullable ClientSession clientSession, String databaseName, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, OperationExecutor executor, List<? extends Bson> pipeline, Class<TResult> resultClass, ChangeStreamLevel changeStreamLevel, boolean retryReads) {
        return new Java8ChangeStreamIterableImpl<TResult>(clientSession, databaseName, codecRegistry, readPreference, readConcern, executor, pipeline, resultClass, changeStreamLevel, retryReads);
    }

    @Override
    public <TResult> ChangeStreamIterable<TResult> changeStreamOf(@Nullable ClientSession clientSession, MongoNamespace namespace, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, OperationExecutor executor, List<? extends Bson> pipeline, Class<TResult> resultClass, ChangeStreamLevel changeStreamLevel, boolean retryReads) {
        return new Java8ChangeStreamIterableImpl<TResult>(clientSession, namespace, codecRegistry, readPreference, readConcern, executor, pipeline, resultClass, changeStreamLevel, retryReads);
    }

    @Override
    public <TDocument, TResult> DistinctIterable<TResult> distinctOf(@Nullable ClientSession clientSession, MongoNamespace namespace, Class<TDocument> documentClass, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, OperationExecutor executor, String fieldName, Bson filter, boolean retryReads) {
        return new Java8DistinctIterableImpl<TDocument, TResult>(clientSession, namespace, documentClass, resultClass, codecRegistry, readPreference, readConcern, executor, fieldName, filter, retryReads);
    }

    @Override
    public <TResult> ListDatabasesIterable<TResult> listDatabasesOf(@Nullable ClientSession clientSession, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, OperationExecutor executor, boolean retryReads) {
        return new Java8ListDatabasesIterableImpl<TResult>(clientSession, resultClass, codecRegistry, readPreference, executor, retryReads);
    }

    @Override
    public <TResult> ListCollectionsIterable<TResult> listCollectionsOf(@Nullable ClientSession clientSession, String databaseName, boolean collectionNamesOnly, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, OperationExecutor executor, boolean retryReads) {
        return new Java8ListCollectionsIterableImpl<TResult>(clientSession, databaseName, collectionNamesOnly, resultClass, codecRegistry, readPreference, executor, retryReads);
    }

    @Override
    public <TResult> ListIndexesIterable<TResult> listIndexesOf(@Nullable ClientSession clientSession, MongoNamespace namespace, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, OperationExecutor executor, boolean retryReads) {
        return new Java8ListIndexesIterableImpl<TResult>(clientSession, namespace, resultClass, codecRegistry, readPreference, executor, retryReads);
    }

    @Override
    public <TDocument, TResult> MapReduceIterable<TResult> mapReduceOf(@Nullable ClientSession clientSession, MongoNamespace namespace, Class<TDocument> documentClass, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, WriteConcern writeConcern, OperationExecutor executor, String mapFunction, String reduceFunction) {
        return new Java8MapReduceIterableImpl<TDocument, TResult>(clientSession, namespace, documentClass, resultClass, codecRegistry, readPreference, readConcern, writeConcern, executor, mapFunction, reduceFunction);
    }
}

