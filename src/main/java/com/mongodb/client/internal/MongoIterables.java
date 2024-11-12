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
import com.mongodb.client.internal.FallbackMongoIterableFactory;
import com.mongodb.client.internal.Java8MongoIterableFactory;
import com.mongodb.client.internal.MongoIterableFactory;
import com.mongodb.client.internal.OperationExecutor;
import com.mongodb.client.model.AggregationLevel;
import com.mongodb.client.model.changestream.ChangeStreamLevel;
import com.mongodb.lang.Nullable;
import java.util.List;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

public final class MongoIterables {
    private static MongoIterableFactory factory;

    public static <TDocument, TResult> FindIterable<TResult> findOf(@Nullable ClientSession clientSession, MongoNamespace namespace, Class<TDocument> documentClass, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, OperationExecutor executor, Bson filter, boolean retryReads) {
        return factory.findOf(clientSession, namespace, documentClass, resultClass, codecRegistry, readPreference, readConcern, executor, filter, retryReads);
    }

    public static <TDocument, TResult> AggregateIterable<TResult> aggregateOf(@Nullable ClientSession clientSession, MongoNamespace namespace, Class<TDocument> documentClass, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, WriteConcern writeConcern, OperationExecutor executor, List<? extends Bson> pipeline, AggregationLevel aggregationLevel, boolean retryReads) {
        return factory.aggregateOf(clientSession, namespace, documentClass, resultClass, codecRegistry, readPreference, readConcern, writeConcern, executor, pipeline, aggregationLevel, retryReads);
    }

    public static <TDocument, TResult> AggregateIterable<TResult> aggregateOf(@Nullable ClientSession clientSession, String databaseName, Class<TDocument> documentClass, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, WriteConcern writeConcern, OperationExecutor executor, List<? extends Bson> pipeline, AggregationLevel aggregationLevel, boolean retryReads) {
        return factory.aggregateOf(clientSession, databaseName, documentClass, resultClass, codecRegistry, readPreference, readConcern, writeConcern, executor, pipeline, aggregationLevel, retryReads);
    }

    public static <TResult> ChangeStreamIterable<TResult> changeStreamOf(@Nullable ClientSession clientSession, String databaseName, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, OperationExecutor executor, List<? extends Bson> pipeline, Class<TResult> resultClass, ChangeStreamLevel changeStreamLevel, boolean retryReads) {
        return factory.changeStreamOf(clientSession, databaseName, codecRegistry, readPreference, readConcern, executor, pipeline, resultClass, changeStreamLevel, retryReads);
    }

    public static <TResult> ChangeStreamIterable<TResult> changeStreamOf(@Nullable ClientSession clientSession, MongoNamespace namespace, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, OperationExecutor executor, List<? extends Bson> pipeline, Class<TResult> resultClass, ChangeStreamLevel changeStreamLevel, boolean retryReads) {
        return factory.changeStreamOf(clientSession, namespace, codecRegistry, readPreference, readConcern, executor, pipeline, resultClass, changeStreamLevel, retryReads);
    }

    public static <TDocument, TResult> DistinctIterable<TResult> distinctOf(@Nullable ClientSession clientSession, MongoNamespace namespace, Class<TDocument> documentClass, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, OperationExecutor executor, String fieldName, Bson filter, boolean retryReads) {
        return factory.distinctOf(clientSession, namespace, documentClass, resultClass, codecRegistry, readPreference, readConcern, executor, fieldName, filter, retryReads);
    }

    public static <TResult> ListDatabasesIterable<TResult> listDatabasesOf(@Nullable ClientSession clientSession, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, OperationExecutor executor, boolean retryReads) {
        return factory.listDatabasesOf(clientSession, resultClass, codecRegistry, readPreference, executor, retryReads);
    }

    public static <TResult> ListCollectionsIterable<TResult> listCollectionsOf(@Nullable ClientSession clientSession, String databaseName, boolean collectionNamesOnly, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, OperationExecutor executor, boolean retryReads) {
        return factory.listCollectionsOf(clientSession, databaseName, collectionNamesOnly, resultClass, codecRegistry, readPreference, executor, retryReads);
    }

    public static <TResult> ListIndexesIterable<TResult> listIndexesOf(@Nullable ClientSession clientSession, MongoNamespace namespace, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, OperationExecutor executor, boolean retryReads) {
        return factory.listIndexesOf(clientSession, namespace, resultClass, codecRegistry, readPreference, executor, retryReads);
    }

    public static <TDocument, TResult> MapReduceIterable<TResult> mapReduceOf(@Nullable ClientSession clientSession, MongoNamespace namespace, Class<TDocument> documentClass, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, WriteConcern writeConcern, OperationExecutor executor, String mapFunction, String reduceFunction) {
        return factory.mapReduceOf(clientSession, namespace, documentClass, resultClass, codecRegistry, readPreference, readConcern, writeConcern, executor, mapFunction, reduceFunction);
    }

    private MongoIterables() {
    }

    static {
        try {
            Class.forName("java.util.function.Consumer");
            factory = new Java8MongoIterableFactory();
        } catch (ClassNotFoundException e2) {
            factory = new FallbackMongoIterableFactory();
        }
    }
}

