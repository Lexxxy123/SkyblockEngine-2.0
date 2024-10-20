/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.operation;

import com.mongodb.MongoNamespace;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.async.AsyncBatchCursor;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.AggregationLevel;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.CountOptions;
import com.mongodb.client.model.CreateIndexOptions;
import com.mongodb.client.model.DeleteOptions;
import com.mongodb.client.model.DropIndexOptions;
import com.mongodb.client.model.FindOneAndDeleteOptions;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.FindOptions;
import com.mongodb.client.model.IndexModel;
import com.mongodb.client.model.InsertManyOptions;
import com.mongodb.client.model.InsertOneOptions;
import com.mongodb.client.model.MapReduceAction;
import com.mongodb.client.model.RenameCollectionOptions;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.WriteModel;
import com.mongodb.internal.client.model.CountStrategy;
import com.mongodb.internal.operation.Operations;
import com.mongodb.operation.AsyncReadOperation;
import com.mongodb.operation.AsyncWriteOperation;
import com.mongodb.operation.MapReduceAsyncBatchCursor;
import com.mongodb.operation.MapReduceStatistics;
import java.util.List;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

public final class AsyncOperations<TDocument> {
    private Operations<TDocument> operations;

    public AsyncOperations(Class<TDocument> documentClass, ReadPreference readPreference, CodecRegistry codecRegistry) {
        this(null, documentClass, readPreference, codecRegistry, ReadConcern.DEFAULT, WriteConcern.ACKNOWLEDGED, false, true);
    }

    public AsyncOperations(Class<TDocument> documentClass, ReadPreference readPreference, CodecRegistry codecRegistry, boolean retryReads) {
        this(null, documentClass, readPreference, codecRegistry, ReadConcern.DEFAULT, WriteConcern.ACKNOWLEDGED, false, retryReads);
    }

    public AsyncOperations(MongoNamespace namespace, Class<TDocument> documentClass, ReadPreference readPreference, CodecRegistry codecRegistry) {
        this(namespace, documentClass, readPreference, codecRegistry, ReadConcern.DEFAULT, WriteConcern.ACKNOWLEDGED, false, true);
    }

    public AsyncOperations(MongoNamespace namespace, Class<TDocument> documentClass, ReadPreference readPreference, CodecRegistry codecRegistry, boolean retryReads) {
        this(namespace, documentClass, readPreference, codecRegistry, ReadConcern.DEFAULT, WriteConcern.ACKNOWLEDGED, false, retryReads);
    }

    public AsyncOperations(MongoNamespace namespace, Class<TDocument> documentClass, ReadPreference readPreference, CodecRegistry codecRegistry, WriteConcern writeConcern) {
        this(namespace, documentClass, readPreference, codecRegistry, ReadConcern.DEFAULT, writeConcern, false, true);
    }

    public AsyncOperations(MongoNamespace namespace, Class<TDocument> documentClass, ReadPreference readPreference, CodecRegistry codecRegistry, ReadConcern readConcern, WriteConcern writeConcern) {
        this(namespace, documentClass, readPreference, codecRegistry, readConcern, writeConcern, false, true);
    }

    public AsyncOperations(MongoNamespace namespace, Class<TDocument> documentClass, ReadPreference readPreference, CodecRegistry codecRegistry, ReadConcern readConcern, WriteConcern writeConcern, boolean retryWrites, boolean retryReads) {
        this.operations = new Operations<TDocument>(namespace, documentClass, readPreference, codecRegistry, readConcern, writeConcern, retryWrites, retryReads);
    }

    public AsyncReadOperation<Long> count(Bson filter, CountOptions options, CountStrategy countStrategy) {
        return this.operations.count(filter, options, countStrategy);
    }

    public <TResult> AsyncReadOperation<AsyncBatchCursor<TResult>> findFirst(Bson filter, Class<TResult> resultClass, FindOptions options) {
        return this.operations.findFirst(filter, resultClass, options);
    }

    public <TResult> AsyncReadOperation<AsyncBatchCursor<TResult>> find(Bson filter, Class<TResult> resultClass, FindOptions options) {
        return this.operations.find(filter, resultClass, options);
    }

    public <TResult> AsyncReadOperation<AsyncBatchCursor<TResult>> find(MongoNamespace findNamespace, Bson filter, Class<TResult> resultClass, FindOptions options) {
        return this.operations.find(findNamespace, filter, resultClass, options);
    }

    public <TResult> AsyncReadOperation<AsyncBatchCursor<TResult>> distinct(String fieldName, Bson filter, Class<TResult> resultClass, long maxTimeMS, Collation collation) {
        return this.operations.distinct(fieldName, filter, resultClass, maxTimeMS, collation);
    }

    public <TResult> AsyncReadOperation<AsyncBatchCursor<TResult>> aggregate(List<? extends Bson> pipeline, Class<TResult> resultClass, long maxTimeMS, long maxAwaitTimeMS, Integer batchSize, Collation collation, Bson hint, String comment, Boolean allowDiskUse, Boolean useCursor, AggregationLevel aggregationLevel) {
        return this.operations.aggregate(pipeline, resultClass, maxTimeMS, maxAwaitTimeMS, batchSize, collation, hint, comment, allowDiskUse, useCursor, aggregationLevel);
    }

    public AsyncWriteOperation<Void> aggregateToCollection(List<? extends Bson> pipeline, long maxTimeMS, Boolean allowDiskUse, Boolean bypassDocumentValidation, Collation collation, Bson hint, String comment, AggregationLevel aggregationLevel) {
        return this.operations.aggregateToCollection(pipeline, maxTimeMS, allowDiskUse, bypassDocumentValidation, collation, hint, comment, aggregationLevel);
    }

    public AsyncWriteOperation<MapReduceStatistics> mapReduceToCollection(String databaseName, String collectionName, String mapFunction, String reduceFunction, String finalizeFunction, Bson filter, int limit, long maxTimeMS, boolean jsMode, Bson scope, Bson sort, boolean verbose, MapReduceAction action, boolean nonAtomic, boolean sharded, Boolean bypassDocumentValidation, Collation collation) {
        return this.operations.mapReduceToCollection(databaseName, collectionName, mapFunction, reduceFunction, finalizeFunction, filter, limit, maxTimeMS, jsMode, scope, sort, verbose, action, nonAtomic, sharded, bypassDocumentValidation, collation);
    }

    public <TResult> AsyncReadOperation<MapReduceAsyncBatchCursor<TResult>> mapReduce(String mapFunction, String reduceFunction, String finalizeFunction, Class<TResult> resultClass, Bson filter, int limit, long maxTimeMS, boolean jsMode, Bson scope, Bson sort, boolean verbose, Collation collation) {
        return this.operations.mapReduce(mapFunction, reduceFunction, finalizeFunction, resultClass, filter, limit, maxTimeMS, jsMode, scope, sort, verbose, collation);
    }

    public AsyncWriteOperation<TDocument> findOneAndDelete(Bson filter, FindOneAndDeleteOptions options) {
        return this.operations.findOneAndDelete(filter, options);
    }

    public AsyncWriteOperation<TDocument> findOneAndReplace(Bson filter, TDocument replacement, FindOneAndReplaceOptions options) {
        return this.operations.findOneAndReplace(filter, replacement, options);
    }

    public AsyncWriteOperation<TDocument> findOneAndUpdate(Bson filter, Bson update, FindOneAndUpdateOptions options) {
        return this.operations.findOneAndUpdate(filter, update, options);
    }

    public AsyncWriteOperation<TDocument> findOneAndUpdate(Bson filter, List<? extends Bson> update, FindOneAndUpdateOptions options) {
        return this.operations.findOneAndUpdate(filter, update, options);
    }

    public AsyncWriteOperation<BulkWriteResult> insertOne(TDocument document, InsertOneOptions options) {
        return this.operations.insertOne(document, options);
    }

    public AsyncWriteOperation<BulkWriteResult> replaceOne(Bson filter, TDocument replacement, ReplaceOptions options) {
        return this.operations.replaceOne(filter, replacement, options);
    }

    public AsyncWriteOperation<BulkWriteResult> deleteOne(Bson filter, DeleteOptions options) {
        return this.operations.deleteOne(filter, options);
    }

    public AsyncWriteOperation<BulkWriteResult> deleteMany(Bson filter, DeleteOptions options) {
        return this.operations.deleteMany(filter, options);
    }

    public AsyncWriteOperation<BulkWriteResult> updateOne(Bson filter, Bson update, UpdateOptions updateOptions) {
        return this.operations.updateOne(filter, update, updateOptions);
    }

    public AsyncWriteOperation<BulkWriteResult> updateOne(Bson filter, List<? extends Bson> update, UpdateOptions updateOptions) {
        return this.operations.updateOne(filter, update, updateOptions);
    }

    public AsyncWriteOperation<BulkWriteResult> updateMany(Bson filter, Bson update, UpdateOptions updateOptions) {
        return this.operations.updateMany(filter, update, updateOptions);
    }

    public AsyncWriteOperation<BulkWriteResult> updateMany(Bson filter, List<? extends Bson> update, UpdateOptions updateOptions) {
        return this.operations.updateMany(filter, update, updateOptions);
    }

    public AsyncWriteOperation<BulkWriteResult> insertMany(List<? extends TDocument> documents, InsertManyOptions options) {
        return this.operations.insertMany(documents, options);
    }

    public AsyncWriteOperation<BulkWriteResult> bulkWrite(List<? extends WriteModel<? extends TDocument>> requests, BulkWriteOptions options) {
        return this.operations.bulkWrite(requests, options);
    }

    public AsyncWriteOperation<Void> dropCollection() {
        return this.operations.dropCollection();
    }

    public AsyncWriteOperation<Void> renameCollection(MongoNamespace newCollectionNamespace, RenameCollectionOptions options) {
        return this.operations.renameCollection(newCollectionNamespace, options);
    }

    public AsyncWriteOperation<Void> createIndexes(List<IndexModel> indexes, CreateIndexOptions options) {
        return this.operations.createIndexes(indexes, options);
    }

    public AsyncWriteOperation<Void> dropIndex(String indexName, DropIndexOptions options) {
        return this.operations.dropIndex(indexName, options);
    }

    public AsyncWriteOperation<Void> dropIndex(Bson keys, DropIndexOptions options) {
        return this.operations.dropIndex(keys, options);
    }

    public <TResult> AsyncReadOperation<AsyncBatchCursor<TResult>> listCollections(String databaseName, Class<TResult> resultClass, Bson filter, boolean collectionNamesOnly, Integer batchSize, long maxTimeMS) {
        return this.operations.listCollections(databaseName, resultClass, filter, collectionNamesOnly, batchSize, maxTimeMS);
    }

    public <TResult> AsyncReadOperation<AsyncBatchCursor<TResult>> listDatabases(Class<TResult> resultClass, Bson filter, Boolean nameOnly, long maxTimeMS) {
        return this.operations.listDatabases(resultClass, filter, nameOnly, maxTimeMS);
    }

    public <TResult> AsyncReadOperation<AsyncBatchCursor<TResult>> listIndexes(Class<TResult> resultClass, Integer batchSize, long maxTimeMS) {
        return this.operations.listIndexes(resultClass, batchSize, maxTimeMS);
    }
}

