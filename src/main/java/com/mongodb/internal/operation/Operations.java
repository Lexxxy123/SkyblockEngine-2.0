/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.operation;

import com.mongodb.MongoNamespace;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.assertions.Assertions;
import com.mongodb.bulk.DeleteRequest;
import com.mongodb.bulk.IndexRequest;
import com.mongodb.bulk.InsertRequest;
import com.mongodb.bulk.UpdateRequest;
import com.mongodb.bulk.WriteRequest;
import com.mongodb.client.model.AggregationLevel;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.CountOptions;
import com.mongodb.client.model.CreateIndexOptions;
import com.mongodb.client.model.DeleteManyModel;
import com.mongodb.client.model.DeleteOneModel;
import com.mongodb.client.model.DeleteOptions;
import com.mongodb.client.model.DropIndexOptions;
import com.mongodb.client.model.FindOneAndDeleteOptions;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.FindOptions;
import com.mongodb.client.model.IndexModel;
import com.mongodb.client.model.InsertManyOptions;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.model.InsertOneOptions;
import com.mongodb.client.model.MapReduceAction;
import com.mongodb.client.model.RenameCollectionOptions;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.UpdateManyModel;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.WriteModel;
import com.mongodb.internal.client.model.CountStrategy;
import com.mongodb.operation.AggregateOperation;
import com.mongodb.operation.AggregateToCollectionOperation;
import com.mongodb.operation.CountOperation;
import com.mongodb.operation.CreateIndexesOperation;
import com.mongodb.operation.DistinctOperation;
import com.mongodb.operation.DropCollectionOperation;
import com.mongodb.operation.DropIndexOperation;
import com.mongodb.operation.FindAndDeleteOperation;
import com.mongodb.operation.FindAndReplaceOperation;
import com.mongodb.operation.FindAndUpdateOperation;
import com.mongodb.operation.FindOperation;
import com.mongodb.operation.ListCollectionsOperation;
import com.mongodb.operation.ListDatabasesOperation;
import com.mongodb.operation.ListIndexesOperation;
import com.mongodb.operation.MapReduceToCollectionOperation;
import com.mongodb.operation.MapReduceWithInlineResultsOperation;
import com.mongodb.operation.MixedBulkWriteOperation;
import com.mongodb.operation.RenameCollectionOperation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonDocumentWrapper;
import org.bson.BsonJavaScript;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

final class Operations<TDocument> {
    private final MongoNamespace namespace;
    private final Class<TDocument> documentClass;
    private final ReadPreference readPreference;
    private final CodecRegistry codecRegistry;
    private final ReadConcern readConcern;
    private final WriteConcern writeConcern;
    private final boolean retryWrites;
    private boolean retryReads;

    Operations(MongoNamespace namespace, Class<TDocument> documentClass, ReadPreference readPreference, CodecRegistry codecRegistry, ReadConcern readConcern, WriteConcern writeConcern, boolean retryWrites, boolean retryReads) {
        this.namespace = namespace;
        this.documentClass = documentClass;
        this.readPreference = readPreference;
        this.codecRegistry = codecRegistry;
        this.readConcern = readConcern;
        this.writeConcern = writeConcern;
        this.retryWrites = retryWrites;
        this.retryReads = retryReads;
    }

    CountOperation count(Bson filter, CountOptions options, CountStrategy countStrategy) {
        CountOperation operation = new CountOperation(this.namespace, countStrategy).retryReads(this.retryReads).filter(this.toBsonDocument(filter)).skip(options.getSkip()).limit(options.getLimit()).maxTime(options.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).collation(options.getCollation());
        if (options.getHint() != null) {
            operation.hint(this.toBsonDocument(options.getHint()));
        } else if (options.getHintString() != null) {
            operation.hint(new BsonString(options.getHintString()));
        }
        return operation;
    }

    <TResult> FindOperation<TResult> findFirst(Bson filter, Class<TResult> resultClass, FindOptions options) {
        return this.createFindOperation(this.namespace, filter, resultClass, options).batchSize(0).limit(-1);
    }

    <TResult> FindOperation<TResult> find(Bson filter, Class<TResult> resultClass, FindOptions options) {
        return this.createFindOperation(this.namespace, filter, resultClass, options);
    }

    <TResult> FindOperation<TResult> find(MongoNamespace findNamespace, Bson filter, Class<TResult> resultClass, FindOptions options) {
        return this.createFindOperation(findNamespace, filter, resultClass, options);
    }

    private <TResult> FindOperation<TResult> createFindOperation(MongoNamespace findNamespace, Bson filter, Class<TResult> resultClass, FindOptions options) {
        FindOperation<TResult> operation = new FindOperation<TResult>(findNamespace, this.codecRegistry.get(resultClass)).retryReads(this.retryReads).filter(filter == null ? new BsonDocument() : filter.toBsonDocument(this.documentClass, this.codecRegistry)).batchSize(options.getBatchSize()).skip(options.getSkip()).limit(options.getLimit()).maxTime(options.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).maxAwaitTime(options.getMaxAwaitTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).modifiers(this.toBsonDocumentOrNull(options.getModifiers())).projection(this.toBsonDocumentOrNull(options.getProjection())).sort(this.toBsonDocumentOrNull(options.getSort())).cursorType(options.getCursorType()).noCursorTimeout(options.isNoCursorTimeout()).oplogReplay(options.isOplogReplay()).partial(options.isPartial()).slaveOk(this.readPreference.isSlaveOk()).collation(options.getCollation()).comment(options.getComment()).min(this.toBsonDocumentOrNull(options.getMin())).max(this.toBsonDocumentOrNull(options.getMax())).maxScan(options.getMaxScan()).returnKey(options.isReturnKey()).showRecordId(options.isShowRecordId()).snapshot(options.isSnapshot());
        if (options.getHint() != null) {
            operation.hint(this.toBsonDocument(options.getHint()));
        } else if (options.getHintString() != null) {
            operation.hint(new BsonString(options.getHintString()));
        }
        return operation;
    }

    <TResult> DistinctOperation<TResult> distinct(String fieldName, Bson filter, Class<TResult> resultClass, long maxTimeMS, Collation collation) {
        return new DistinctOperation<TResult>(this.namespace, fieldName, this.codecRegistry.get(resultClass)).retryReads(this.retryReads).filter(filter == null ? null : filter.toBsonDocument(this.documentClass, this.codecRegistry)).maxTime(maxTimeMS, TimeUnit.MILLISECONDS).collation(collation);
    }

    <TResult> AggregateOperation<TResult> aggregate(List<? extends Bson> pipeline, Class<TResult> resultClass, long maxTimeMS, long maxAwaitTimeMS, Integer batchSize, Collation collation, Bson hint, String comment, Boolean allowDiskUse, Boolean useCursor, AggregationLevel aggregationLevel) {
        return new AggregateOperation<TResult>(this.namespace, this.toBsonDocumentList(pipeline), this.codecRegistry.get(resultClass), aggregationLevel).retryReads(this.retryReads).maxTime(maxTimeMS, TimeUnit.MILLISECONDS).maxAwaitTime(maxAwaitTimeMS, TimeUnit.MILLISECONDS).allowDiskUse(allowDiskUse).batchSize(batchSize).useCursor(useCursor).collation(collation).hint(hint == null ? null : hint.toBsonDocument(this.documentClass, this.codecRegistry)).comment(comment);
    }

    AggregateToCollectionOperation aggregateToCollection(List<? extends Bson> pipeline, long maxTimeMS, Boolean allowDiskUse, Boolean bypassDocumentValidation, Collation collation, Bson hint, String comment, AggregationLevel aggregationLevel) {
        return new AggregateToCollectionOperation(this.namespace, this.toBsonDocumentList(pipeline), this.readConcern, this.writeConcern, aggregationLevel).maxTime(maxTimeMS, TimeUnit.MILLISECONDS).allowDiskUse(allowDiskUse).bypassDocumentValidation(bypassDocumentValidation).collation(collation).hint(hint == null ? null : hint.toBsonDocument(this.documentClass, this.codecRegistry)).comment(comment);
    }

    MapReduceToCollectionOperation mapReduceToCollection(String databaseName, String collectionName, String mapFunction, String reduceFunction, String finalizeFunction, Bson filter, int limit, long maxTimeMS, boolean jsMode, Bson scope, Bson sort, boolean verbose, MapReduceAction action, boolean nonAtomic, boolean sharded, Boolean bypassDocumentValidation, Collation collation) {
        MapReduceToCollectionOperation operation = new MapReduceToCollectionOperation(this.namespace, new BsonJavaScript(mapFunction), new BsonJavaScript(reduceFunction), collectionName, this.writeConcern).filter(this.toBsonDocumentOrNull(filter)).limit(limit).maxTime(maxTimeMS, TimeUnit.MILLISECONDS).jsMode(jsMode).scope(this.toBsonDocumentOrNull(scope)).sort(this.toBsonDocumentOrNull(sort)).verbose(verbose).action(action.getValue()).nonAtomic(nonAtomic).sharded(sharded).databaseName(databaseName).bypassDocumentValidation(bypassDocumentValidation).collation(collation);
        if (finalizeFunction != null) {
            operation.finalizeFunction(new BsonJavaScript(finalizeFunction));
        }
        return operation;
    }

    <TResult> MapReduceWithInlineResultsOperation<TResult> mapReduce(String mapFunction, String reduceFunction, String finalizeFunction, Class<TResult> resultClass, Bson filter, int limit, long maxTimeMS, boolean jsMode, Bson scope, Bson sort, boolean verbose, Collation collation) {
        MapReduceWithInlineResultsOperation<TResult> operation = new MapReduceWithInlineResultsOperation<TResult>(this.namespace, new BsonJavaScript(mapFunction), new BsonJavaScript(reduceFunction), this.codecRegistry.get(resultClass)).filter(this.toBsonDocumentOrNull(filter)).limit(limit).maxTime(maxTimeMS, TimeUnit.MILLISECONDS).jsMode(jsMode).scope(this.toBsonDocumentOrNull(scope)).sort(this.toBsonDocumentOrNull(sort)).verbose(verbose).collation(collation);
        if (finalizeFunction != null) {
            operation.finalizeFunction(new BsonJavaScript(finalizeFunction));
        }
        return operation;
    }

    FindAndDeleteOperation<TDocument> findOneAndDelete(Bson filter, FindOneAndDeleteOptions options) {
        return new FindAndDeleteOperation<TDocument>(this.namespace, this.writeConcern, this.retryWrites, this.getCodec()).filter(this.toBsonDocument(filter)).projection(this.toBsonDocument(options.getProjection())).sort(this.toBsonDocument(options.getSort())).maxTime(options.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).collation(options.getCollation());
    }

    FindAndReplaceOperation<TDocument> findOneAndReplace(Bson filter, TDocument replacement, FindOneAndReplaceOptions options) {
        return new FindAndReplaceOperation<TDocument>(this.namespace, this.writeConcern, this.retryWrites, this.getCodec(), this.documentToBsonDocument(replacement)).filter(this.toBsonDocument(filter)).projection(this.toBsonDocument(options.getProjection())).sort(this.toBsonDocument(options.getSort())).returnOriginal(options.getReturnDocument() == ReturnDocument.BEFORE).upsert(options.isUpsert()).maxTime(options.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).bypassDocumentValidation(options.getBypassDocumentValidation()).collation(options.getCollation());
    }

    FindAndUpdateOperation<TDocument> findOneAndUpdate(Bson filter, Bson update, FindOneAndUpdateOptions options) {
        return new FindAndUpdateOperation<TDocument>(this.namespace, this.writeConcern, this.retryWrites, this.getCodec(), this.toBsonDocument(update)).filter(this.toBsonDocument(filter)).projection(this.toBsonDocument(options.getProjection())).sort(this.toBsonDocument(options.getSort())).returnOriginal(options.getReturnDocument() == ReturnDocument.BEFORE).upsert(options.isUpsert()).maxTime(options.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).bypassDocumentValidation(options.getBypassDocumentValidation()).collation(options.getCollation()).arrayFilters(this.toBsonDocumentList(options.getArrayFilters()));
    }

    FindAndUpdateOperation<TDocument> findOneAndUpdate(Bson filter, List<? extends Bson> update, FindOneAndUpdateOptions options) {
        return new FindAndUpdateOperation<TDocument>(this.namespace, this.writeConcern, this.retryWrites, this.getCodec(), this.toBsonDocumentList(update)).filter(this.toBsonDocument(filter)).projection(this.toBsonDocument(options.getProjection())).sort(this.toBsonDocument(options.getSort())).returnOriginal(options.getReturnDocument() == ReturnDocument.BEFORE).upsert(options.isUpsert()).maxTime(options.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).bypassDocumentValidation(options.getBypassDocumentValidation()).collation(options.getCollation()).arrayFilters(this.toBsonDocumentList(options.getArrayFilters()));
    }

    MixedBulkWriteOperation insertOne(TDocument document, InsertOneOptions options) {
        return this.bulkWrite(Collections.singletonList(new InsertOneModel<TDocument>(document)), new BulkWriteOptions().bypassDocumentValidation(options.getBypassDocumentValidation()));
    }

    MixedBulkWriteOperation replaceOne(Bson filter, TDocument replacement, ReplaceOptions options) {
        return this.bulkWrite(Collections.singletonList(new ReplaceOneModel<TDocument>(filter, replacement, options)), new BulkWriteOptions().bypassDocumentValidation(options.getBypassDocumentValidation()));
    }

    MixedBulkWriteOperation deleteOne(Bson filter, DeleteOptions options) {
        return this.bulkWrite(Collections.singletonList(new DeleteOneModel(filter, options)), new BulkWriteOptions());
    }

    MixedBulkWriteOperation deleteMany(Bson filter, DeleteOptions options) {
        return this.bulkWrite(Collections.singletonList(new DeleteManyModel(filter, options)), new BulkWriteOptions());
    }

    MixedBulkWriteOperation updateOne(Bson filter, Bson update, UpdateOptions updateOptions) {
        return this.bulkWrite(Collections.singletonList(new UpdateOneModel(filter, update, updateOptions)), new BulkWriteOptions().bypassDocumentValidation(updateOptions.getBypassDocumentValidation()));
    }

    MixedBulkWriteOperation updateOne(Bson filter, List<? extends Bson> update, UpdateOptions updateOptions) {
        return this.bulkWrite(Collections.singletonList(new UpdateOneModel(filter, update, updateOptions)), new BulkWriteOptions().bypassDocumentValidation(updateOptions.getBypassDocumentValidation()));
    }

    MixedBulkWriteOperation updateMany(Bson filter, Bson update, UpdateOptions updateOptions) {
        return this.bulkWrite(Collections.singletonList(new UpdateManyModel(filter, update, updateOptions)), new BulkWriteOptions().bypassDocumentValidation(updateOptions.getBypassDocumentValidation()));
    }

    MixedBulkWriteOperation updateMany(Bson filter, List<? extends Bson> update, UpdateOptions updateOptions) {
        return this.bulkWrite(Collections.singletonList(new UpdateManyModel(filter, update, updateOptions)), new BulkWriteOptions().bypassDocumentValidation(updateOptions.getBypassDocumentValidation()));
    }

    MixedBulkWriteOperation insertMany(List<? extends TDocument> documents, InsertManyOptions options) {
        Assertions.notNull("documents", documents);
        ArrayList<InsertRequest> requests = new ArrayList<InsertRequest>(documents.size());
        for (TDocument document : documents) {
            if (document == null) {
                throw new IllegalArgumentException("documents can not contain a null value");
            }
            if (this.getCodec() instanceof CollectibleCodec) {
                document = ((CollectibleCodec)this.getCodec()).generateIdIfAbsentFromDocument(document);
            }
            requests.add(new InsertRequest(this.documentToBsonDocument(document)));
        }
        return new MixedBulkWriteOperation(this.namespace, requests, options.isOrdered(), this.writeConcern, this.retryWrites).bypassDocumentValidation(options.getBypassDocumentValidation());
    }

    MixedBulkWriteOperation bulkWrite(List<? extends WriteModel<? extends TDocument>> requests, BulkWriteOptions options) {
        Assertions.notNull("requests", requests);
        ArrayList<InsertRequest> writeRequests = new ArrayList<InsertRequest>(requests.size());
        for (WriteModel<TDocument> writeModel : requests) {
            BsonValue update;
            WriteRequest writeRequest;
            if (writeModel == null) {
                throw new IllegalArgumentException("requests can not contain a null value");
            }
            if (writeModel instanceof InsertOneModel) {
                Object document = ((InsertOneModel)writeModel).getDocument();
                if (this.getCodec() instanceof CollectibleCodec) {
                    document = ((CollectibleCodec)this.getCodec()).generateIdIfAbsentFromDocument(document);
                }
                writeRequest = new InsertRequest(this.documentToBsonDocument(document));
            } else if (writeModel instanceof ReplaceOneModel) {
                ReplaceOneModel replaceOneModel = (ReplaceOneModel)writeModel;
                writeRequest = new UpdateRequest(this.toBsonDocument(replaceOneModel.getFilter()), this.documentToBsonDocument(replaceOneModel.getReplacement()), WriteRequest.Type.REPLACE).upsert(replaceOneModel.getReplaceOptions().isUpsert()).collation(replaceOneModel.getReplaceOptions().getCollation());
            } else if (writeModel instanceof UpdateOneModel) {
                UpdateOneModel updateOneModel = (UpdateOneModel)writeModel;
                update = updateOneModel.getUpdate() != null ? this.toBsonDocument(updateOneModel.getUpdate()) : new BsonArray(this.toBsonDocumentList(updateOneModel.getUpdatePipeline()));
                writeRequest = new UpdateRequest(this.toBsonDocument(updateOneModel.getFilter()), update, WriteRequest.Type.UPDATE).multi(false).upsert(updateOneModel.getOptions().isUpsert()).collation(updateOneModel.getOptions().getCollation()).arrayFilters(this.toBsonDocumentList(updateOneModel.getOptions().getArrayFilters()));
            } else if (writeModel instanceof UpdateManyModel) {
                UpdateManyModel updateManyModel = (UpdateManyModel)writeModel;
                update = updateManyModel.getUpdate() != null ? this.toBsonDocument(updateManyModel.getUpdate()) : new BsonArray(this.toBsonDocumentList(updateManyModel.getUpdatePipeline()));
                writeRequest = new UpdateRequest(this.toBsonDocument(updateManyModel.getFilter()), update, WriteRequest.Type.UPDATE).multi(true).upsert(updateManyModel.getOptions().isUpsert()).collation(updateManyModel.getOptions().getCollation()).arrayFilters(this.toBsonDocumentList(updateManyModel.getOptions().getArrayFilters()));
            } else if (writeModel instanceof DeleteOneModel) {
                DeleteOneModel deleteOneModel = (DeleteOneModel)writeModel;
                writeRequest = new DeleteRequest(this.toBsonDocument(deleteOneModel.getFilter())).multi(false).collation(deleteOneModel.getOptions().getCollation());
            } else if (writeModel instanceof DeleteManyModel) {
                DeleteManyModel deleteManyModel = (DeleteManyModel)writeModel;
                writeRequest = new DeleteRequest(this.toBsonDocument(deleteManyModel.getFilter())).multi(true).collation(deleteManyModel.getOptions().getCollation());
            } else {
                throw new UnsupportedOperationException(String.format("WriteModel of type %s is not supported", writeModel.getClass()));
            }
            writeRequests.add((InsertRequest)writeRequest);
        }
        return new MixedBulkWriteOperation(this.namespace, writeRequests, options.isOrdered(), this.writeConcern, this.retryWrites).bypassDocumentValidation(options.getBypassDocumentValidation());
    }

    DropCollectionOperation dropCollection() {
        return new DropCollectionOperation(this.namespace, this.writeConcern);
    }

    RenameCollectionOperation renameCollection(MongoNamespace newCollectionNamespace, RenameCollectionOptions renameCollectionOptions) {
        return new RenameCollectionOperation(this.namespace, newCollectionNamespace, this.writeConcern).dropTarget(renameCollectionOptions.isDropTarget());
    }

    CreateIndexesOperation createIndexes(List<IndexModel> indexes, CreateIndexOptions createIndexOptions) {
        Assertions.notNull("indexes", indexes);
        Assertions.notNull("createIndexOptions", createIndexOptions);
        ArrayList<IndexRequest> indexRequests = new ArrayList<IndexRequest>(indexes.size());
        for (IndexModel model : indexes) {
            if (model == null) {
                throw new IllegalArgumentException("indexes can not contain a null value");
            }
            indexRequests.add(new IndexRequest(this.toBsonDocument(model.getKeys())).name(model.getOptions().getName()).background(model.getOptions().isBackground()).unique(model.getOptions().isUnique()).sparse(model.getOptions().isSparse()).expireAfter(model.getOptions().getExpireAfter(TimeUnit.SECONDS), TimeUnit.SECONDS).version(model.getOptions().getVersion()).weights(this.toBsonDocument(model.getOptions().getWeights())).defaultLanguage(model.getOptions().getDefaultLanguage()).languageOverride(model.getOptions().getLanguageOverride()).textVersion(model.getOptions().getTextVersion()).sphereVersion(model.getOptions().getSphereVersion()).bits(model.getOptions().getBits()).min(model.getOptions().getMin()).max(model.getOptions().getMax()).bucketSize(model.getOptions().getBucketSize()).storageEngine(this.toBsonDocument(model.getOptions().getStorageEngine())).partialFilterExpression(this.toBsonDocument(model.getOptions().getPartialFilterExpression())).collation(model.getOptions().getCollation()).wildcardProjection(this.toBsonDocument(model.getOptions().getWildcardProjection())));
        }
        return new CreateIndexesOperation(this.namespace, indexRequests, this.writeConcern).maxTime(createIndexOptions.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
    }

    DropIndexOperation dropIndex(String indexName, DropIndexOptions dropIndexOptions) {
        return new DropIndexOperation(this.namespace, indexName, this.writeConcern).maxTime(dropIndexOptions.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
    }

    DropIndexOperation dropIndex(Bson keys, DropIndexOptions dropIndexOptions) {
        return new DropIndexOperation(this.namespace, keys.toBsonDocument(BsonDocument.class, this.codecRegistry), this.writeConcern).maxTime(dropIndexOptions.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
    }

    <TResult> ListCollectionsOperation<TResult> listCollections(String databaseName, Class<TResult> resultClass, Bson filter, boolean collectionNamesOnly, Integer batchSize, long maxTimeMS) {
        return new ListCollectionsOperation<TResult>(databaseName, this.codecRegistry.get(resultClass)).retryReads(this.retryReads).filter(this.toBsonDocumentOrNull(filter)).nameOnly(collectionNamesOnly).batchSize(batchSize == null ? 0 : batchSize).maxTime(maxTimeMS, TimeUnit.MILLISECONDS);
    }

    <TResult> ListDatabasesOperation<TResult> listDatabases(Class<TResult> resultClass, Bson filter, Boolean nameOnly, long maxTimeMS) {
        return new ListDatabasesOperation<TResult>(this.codecRegistry.get(resultClass)).maxTime(maxTimeMS, TimeUnit.MILLISECONDS).retryReads(this.retryReads).filter(this.toBsonDocumentOrNull(filter)).nameOnly(nameOnly);
    }

    <TResult> ListIndexesOperation<TResult> listIndexes(Class<TResult> resultClass, Integer batchSize, long maxTimeMS) {
        return new ListIndexesOperation<TResult>(this.namespace, this.codecRegistry.get(resultClass)).retryReads(this.retryReads).batchSize(batchSize == null ? 0 : batchSize).maxTime(maxTimeMS, TimeUnit.MILLISECONDS);
    }

    private Codec<TDocument> getCodec() {
        return this.codecRegistry.get(this.documentClass);
    }

    private BsonDocument documentToBsonDocument(TDocument document) {
        return BsonDocumentWrapper.asBsonDocument(document, this.codecRegistry);
    }

    private BsonDocument toBsonDocument(Bson bson) {
        return bson == null ? null : bson.toBsonDocument(this.documentClass, this.codecRegistry);
    }

    private List<BsonDocument> toBsonDocumentList(List<? extends Bson> bsonList) {
        if (bsonList == null) {
            return null;
        }
        ArrayList<BsonDocument> bsonDocumentList = new ArrayList<BsonDocument>(bsonList.size());
        for (Bson bson : bsonList) {
            bsonDocumentList.add(this.toBsonDocument(bson));
        }
        return bsonDocumentList;
    }

    BsonDocument toBsonDocumentOrNull(Bson document) {
        return document == null ? null : document.toBsonDocument(this.documentClass, this.codecRegistry);
    }
}

