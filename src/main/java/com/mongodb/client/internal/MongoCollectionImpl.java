/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.internal;

import com.mongodb.MongoBulkWriteException;
import com.mongodb.MongoInternalException;
import com.mongodb.MongoNamespace;
import com.mongodb.MongoWriteConcernException;
import com.mongodb.MongoWriteException;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.WriteConcernResult;
import com.mongodb.WriteError;
import com.mongodb.assertions.Assertions;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.bulk.WriteRequest;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.ClientSession;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.MapReduceIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.internal.MongoIterables;
import com.mongodb.client.internal.OperationExecutor;
import com.mongodb.client.model.AggregationLevel;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.CountOptions;
import com.mongodb.client.model.CreateIndexOptions;
import com.mongodb.client.model.DeleteOptions;
import com.mongodb.client.model.DropIndexOptions;
import com.mongodb.client.model.EstimatedDocumentCountOptions;
import com.mongodb.client.model.FindOneAndDeleteOptions;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.IndexModel;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.InsertManyOptions;
import com.mongodb.client.model.InsertOneOptions;
import com.mongodb.client.model.RenameCollectionOptions;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.WriteModel;
import com.mongodb.client.model.changestream.ChangeStreamLevel;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.internal.client.model.CountOptionsHelper;
import com.mongodb.internal.client.model.CountStrategy;
import com.mongodb.internal.operation.IndexHelper;
import com.mongodb.internal.operation.SyncOperations;
import com.mongodb.lang.Nullable;
import com.mongodb.operation.RenameCollectionOperation;
import com.mongodb.operation.WriteOperation;
import java.util.Collections;
import java.util.List;
import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.bson.internal.CodecRegistryHelper;

class MongoCollectionImpl<TDocument>
implements MongoCollection<TDocument> {
    private final MongoNamespace namespace;
    private final Class<TDocument> documentClass;
    private final ReadPreference readPreference;
    private final CodecRegistry codecRegistry;
    private final WriteConcern writeConcern;
    private final boolean retryWrites;
    private final boolean retryReads;
    private final ReadConcern readConcern;
    private final SyncOperations<TDocument> operations;
    private final UuidRepresentation uuidRepresentation;
    private final OperationExecutor executor;

    MongoCollectionImpl(MongoNamespace namespace, Class<TDocument> documentClass, CodecRegistry codecRegistry, ReadPreference readPreference, WriteConcern writeConcern, boolean retryWrites, boolean retryReads, ReadConcern readConcern, UuidRepresentation uuidRepresentation, OperationExecutor executor) {
        this.namespace = Assertions.notNull("namespace", namespace);
        this.documentClass = Assertions.notNull("documentClass", documentClass);
        this.codecRegistry = Assertions.notNull("codecRegistry", codecRegistry);
        this.readPreference = Assertions.notNull("readPreference", readPreference);
        this.writeConcern = Assertions.notNull("writeConcern", writeConcern);
        this.retryWrites = retryWrites;
        this.retryReads = retryReads;
        this.readConcern = Assertions.notNull("readConcern", readConcern);
        this.executor = Assertions.notNull("executor", executor);
        this.uuidRepresentation = Assertions.notNull("uuidRepresentation", uuidRepresentation);
        this.operations = new SyncOperations<TDocument>(namespace, documentClass, readPreference, codecRegistry, readConcern, writeConcern, retryWrites, retryReads);
    }

    @Override
    public MongoNamespace getNamespace() {
        return this.namespace;
    }

    @Override
    public Class<TDocument> getDocumentClass() {
        return this.documentClass;
    }

    @Override
    public CodecRegistry getCodecRegistry() {
        return this.codecRegistry;
    }

    @Override
    public ReadPreference getReadPreference() {
        return this.readPreference;
    }

    @Override
    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }

    @Override
    public ReadConcern getReadConcern() {
        return this.readConcern;
    }

    @Override
    public <NewTDocument> MongoCollection<NewTDocument> withDocumentClass(Class<NewTDocument> clazz) {
        return new MongoCollectionImpl<NewTDocument>(this.namespace, clazz, this.codecRegistry, this.readPreference, this.writeConcern, this.retryWrites, this.retryReads, this.readConcern, this.uuidRepresentation, this.executor);
    }

    @Override
    public MongoCollection<TDocument> withCodecRegistry(CodecRegistry codecRegistry) {
        return new MongoCollectionImpl<TDocument>(this.namespace, this.documentClass, CodecRegistryHelper.createRegistry(codecRegistry, this.uuidRepresentation), this.readPreference, this.writeConcern, this.retryWrites, this.retryReads, this.readConcern, this.uuidRepresentation, this.executor);
    }

    @Override
    public MongoCollection<TDocument> withReadPreference(ReadPreference readPreference) {
        return new MongoCollectionImpl<TDocument>(this.namespace, this.documentClass, this.codecRegistry, readPreference, this.writeConcern, this.retryWrites, this.retryReads, this.readConcern, this.uuidRepresentation, this.executor);
    }

    @Override
    public MongoCollection<TDocument> withWriteConcern(WriteConcern writeConcern) {
        return new MongoCollectionImpl<TDocument>(this.namespace, this.documentClass, this.codecRegistry, this.readPreference, writeConcern, this.retryWrites, this.retryReads, this.readConcern, this.uuidRepresentation, this.executor);
    }

    @Override
    public MongoCollection<TDocument> withReadConcern(ReadConcern readConcern) {
        return new MongoCollectionImpl<TDocument>(this.namespace, this.documentClass, this.codecRegistry, this.readPreference, this.writeConcern, this.retryWrites, this.retryReads, readConcern, this.uuidRepresentation, this.executor);
    }

    @Override
    public long count() {
        return this.count(new BsonDocument(), new CountOptions());
    }

    @Override
    public long count(Bson filter) {
        return this.count(filter, new CountOptions());
    }

    @Override
    public long count(Bson filter, CountOptions options) {
        return this.executeCount(null, filter, options, CountStrategy.COMMAND);
    }

    @Override
    public long count(ClientSession clientSession) {
        return this.count(clientSession, new BsonDocument());
    }

    @Override
    public long count(ClientSession clientSession, Bson filter) {
        return this.count(clientSession, filter, new CountOptions());
    }

    @Override
    public long count(ClientSession clientSession, Bson filter, CountOptions options) {
        Assertions.notNull("clientSession", clientSession);
        return this.executeCount(clientSession, filter, options, CountStrategy.COMMAND);
    }

    @Override
    public long countDocuments() {
        return this.countDocuments(new BsonDocument());
    }

    @Override
    public long countDocuments(Bson filter) {
        return this.countDocuments(filter, new CountOptions());
    }

    @Override
    public long countDocuments(Bson filter, CountOptions options) {
        return this.executeCount(null, filter, options, CountStrategy.AGGREGATE);
    }

    @Override
    public long countDocuments(ClientSession clientSession) {
        return this.countDocuments(clientSession, new BsonDocument());
    }

    @Override
    public long countDocuments(ClientSession clientSession, Bson filter) {
        return this.countDocuments(clientSession, filter, new CountOptions());
    }

    @Override
    public long countDocuments(ClientSession clientSession, Bson filter, CountOptions options) {
        Assertions.notNull("clientSession", clientSession);
        return this.executeCount(clientSession, filter, options, CountStrategy.AGGREGATE);
    }

    @Override
    public long estimatedDocumentCount() {
        return this.estimatedDocumentCount(new EstimatedDocumentCountOptions());
    }

    @Override
    public long estimatedDocumentCount(EstimatedDocumentCountOptions options) {
        return this.executeCount(null, new BsonDocument(), CountOptionsHelper.fromEstimatedDocumentCountOptions(options), CountStrategy.COMMAND);
    }

    private long executeCount(@Nullable ClientSession clientSession, Bson filter, CountOptions options, CountStrategy countStrategy) {
        return this.executor.execute(this.operations.count(filter, options, countStrategy), this.readPreference, this.readConcern, clientSession);
    }

    @Override
    public <TResult> DistinctIterable<TResult> distinct(String fieldName, Class<TResult> resultClass) {
        return this.distinct(fieldName, new BsonDocument(), resultClass);
    }

    @Override
    public <TResult> DistinctIterable<TResult> distinct(String fieldName, Bson filter, Class<TResult> resultClass) {
        return this.createDistinctIterable(null, fieldName, filter, resultClass);
    }

    @Override
    public <TResult> DistinctIterable<TResult> distinct(ClientSession clientSession, String fieldName, Class<TResult> resultClass) {
        return this.distinct(clientSession, fieldName, new BsonDocument(), resultClass);
    }

    @Override
    public <TResult> DistinctIterable<TResult> distinct(ClientSession clientSession, String fieldName, Bson filter, Class<TResult> resultClass) {
        Assertions.notNull("clientSession", clientSession);
        return this.createDistinctIterable(clientSession, fieldName, filter, resultClass);
    }

    private <TResult> DistinctIterable<TResult> createDistinctIterable(@Nullable ClientSession clientSession, String fieldName, Bson filter, Class<TResult> resultClass) {
        return MongoIterables.distinctOf(clientSession, this.namespace, this.documentClass, resultClass, this.codecRegistry, this.readPreference, this.readConcern, this.executor, fieldName, filter, this.retryReads);
    }

    @Override
    public FindIterable<TDocument> find() {
        return this.find(new BsonDocument(), this.documentClass);
    }

    @Override
    public <TResult> FindIterable<TResult> find(Class<TResult> resultClass) {
        return this.find(new BsonDocument(), resultClass);
    }

    @Override
    public FindIterable<TDocument> find(Bson filter) {
        return this.find(filter, this.documentClass);
    }

    @Override
    public <TResult> FindIterable<TResult> find(Bson filter, Class<TResult> resultClass) {
        return this.createFindIterable(null, filter, resultClass);
    }

    @Override
    public FindIterable<TDocument> find(ClientSession clientSession) {
        Assertions.notNull("clientSession", clientSession);
        return this.find(clientSession, new BsonDocument(), this.documentClass);
    }

    @Override
    public <TResult> FindIterable<TResult> find(ClientSession clientSession, Class<TResult> resultClass) {
        Assertions.notNull("clientSession", clientSession);
        return this.find(clientSession, new BsonDocument(), resultClass);
    }

    @Override
    public FindIterable<TDocument> find(ClientSession clientSession, Bson filter) {
        Assertions.notNull("clientSession", clientSession);
        return this.find(clientSession, filter, this.documentClass);
    }

    @Override
    public <TResult> FindIterable<TResult> find(ClientSession clientSession, Bson filter, Class<TResult> resultClass) {
        Assertions.notNull("clientSession", clientSession);
        return this.createFindIterable(clientSession, filter, resultClass);
    }

    private <TResult> FindIterable<TResult> createFindIterable(@Nullable ClientSession clientSession, Bson filter, Class<TResult> resultClass) {
        return MongoIterables.findOf(clientSession, this.namespace, this.documentClass, resultClass, this.codecRegistry, this.readPreference, this.readConcern, this.executor, filter, this.retryReads);
    }

    @Override
    public AggregateIterable<TDocument> aggregate(List<? extends Bson> pipeline) {
        return this.aggregate(pipeline, this.documentClass);
    }

    @Override
    public <TResult> AggregateIterable<TResult> aggregate(List<? extends Bson> pipeline, Class<TResult> resultClass) {
        return this.createAggregateIterable(null, pipeline, resultClass);
    }

    @Override
    public AggregateIterable<TDocument> aggregate(ClientSession clientSession, List<? extends Bson> pipeline) {
        return this.aggregate(clientSession, pipeline, this.documentClass);
    }

    @Override
    public <TResult> AggregateIterable<TResult> aggregate(ClientSession clientSession, List<? extends Bson> pipeline, Class<TResult> resultClass) {
        Assertions.notNull("clientSession", clientSession);
        return this.createAggregateIterable(clientSession, pipeline, resultClass);
    }

    private <TResult> AggregateIterable<TResult> createAggregateIterable(@Nullable ClientSession clientSession, List<? extends Bson> pipeline, Class<TResult> resultClass) {
        return MongoIterables.aggregateOf(clientSession, this.namespace, this.documentClass, resultClass, this.codecRegistry, this.readPreference, this.readConcern, this.writeConcern, this.executor, pipeline, AggregationLevel.COLLECTION, this.retryReads);
    }

    @Override
    public ChangeStreamIterable<TDocument> watch() {
        return this.watch(Collections.emptyList());
    }

    @Override
    public <TResult> ChangeStreamIterable<TResult> watch(Class<TResult> resultClass) {
        return this.watch(Collections.emptyList(), resultClass);
    }

    @Override
    public ChangeStreamIterable<TDocument> watch(List<? extends Bson> pipeline) {
        return this.watch(pipeline, this.documentClass);
    }

    @Override
    public <TResult> ChangeStreamIterable<TResult> watch(List<? extends Bson> pipeline, Class<TResult> resultClass) {
        return this.createChangeStreamIterable(null, pipeline, resultClass);
    }

    @Override
    public ChangeStreamIterable<TDocument> watch(ClientSession clientSession) {
        return this.watch(clientSession, Collections.emptyList(), this.documentClass);
    }

    @Override
    public <TResult> ChangeStreamIterable<TResult> watch(ClientSession clientSession, Class<TResult> resultClass) {
        return this.watch(clientSession, Collections.emptyList(), resultClass);
    }

    @Override
    public ChangeStreamIterable<TDocument> watch(ClientSession clientSession, List<? extends Bson> pipeline) {
        return this.watch(clientSession, pipeline, this.documentClass);
    }

    @Override
    public <TResult> ChangeStreamIterable<TResult> watch(ClientSession clientSession, List<? extends Bson> pipeline, Class<TResult> resultClass) {
        Assertions.notNull("clientSession", clientSession);
        return this.createChangeStreamIterable(clientSession, pipeline, resultClass);
    }

    private <TResult> ChangeStreamIterable<TResult> createChangeStreamIterable(@Nullable ClientSession clientSession, List<? extends Bson> pipeline, Class<TResult> resultClass) {
        return MongoIterables.changeStreamOf(clientSession, this.namespace, this.codecRegistry, this.readPreference, this.readConcern, this.executor, pipeline, resultClass, ChangeStreamLevel.COLLECTION, this.retryReads);
    }

    @Override
    public MapReduceIterable<TDocument> mapReduce(String mapFunction, String reduceFunction) {
        return this.mapReduce(mapFunction, reduceFunction, this.documentClass);
    }

    @Override
    public <TResult> MapReduceIterable<TResult> mapReduce(String mapFunction, String reduceFunction, Class<TResult> resultClass) {
        return this.createMapReduceIterable(null, mapFunction, reduceFunction, resultClass);
    }

    @Override
    public MapReduceIterable<TDocument> mapReduce(ClientSession clientSession, String mapFunction, String reduceFunction) {
        return this.mapReduce(clientSession, mapFunction, reduceFunction, this.documentClass);
    }

    @Override
    public <TResult> MapReduceIterable<TResult> mapReduce(ClientSession clientSession, String mapFunction, String reduceFunction, Class<TResult> resultClass) {
        Assertions.notNull("clientSession", clientSession);
        return this.createMapReduceIterable(clientSession, mapFunction, reduceFunction, resultClass);
    }

    private <TResult> MapReduceIterable<TResult> createMapReduceIterable(@Nullable ClientSession clientSession, String mapFunction, String reduceFunction, Class<TResult> resultClass) {
        return MongoIterables.mapReduceOf(clientSession, this.namespace, this.documentClass, resultClass, this.codecRegistry, this.readPreference, this.readConcern, this.writeConcern, this.executor, mapFunction, reduceFunction);
    }

    @Override
    public BulkWriteResult bulkWrite(List<? extends WriteModel<? extends TDocument>> requests) {
        return this.bulkWrite(requests, new BulkWriteOptions());
    }

    @Override
    public BulkWriteResult bulkWrite(List<? extends WriteModel<? extends TDocument>> requests, BulkWriteOptions options) {
        return this.executeBulkWrite(null, requests, options);
    }

    @Override
    public BulkWriteResult bulkWrite(ClientSession clientSession, List<? extends WriteModel<? extends TDocument>> requests) {
        return this.bulkWrite(clientSession, requests, new BulkWriteOptions());
    }

    @Override
    public BulkWriteResult bulkWrite(ClientSession clientSession, List<? extends WriteModel<? extends TDocument>> requests, BulkWriteOptions options) {
        Assertions.notNull("clientSession", clientSession);
        return this.executeBulkWrite(clientSession, requests, options);
    }

    private BulkWriteResult executeBulkWrite(@Nullable ClientSession clientSession, List<? extends WriteModel<? extends TDocument>> requests, BulkWriteOptions options) {
        Assertions.notNull("requests", requests);
        return this.executor.execute(this.operations.bulkWrite(requests, options), this.readConcern, clientSession);
    }

    @Override
    public void insertOne(TDocument document) {
        this.insertOne(document, new InsertOneOptions());
    }

    @Override
    public void insertOne(TDocument document, InsertOneOptions options) {
        Assertions.notNull("document", document);
        this.executeInsertOne(null, document, options);
    }

    @Override
    public void insertOne(ClientSession clientSession, TDocument document) {
        this.insertOne(clientSession, document, new InsertOneOptions());
    }

    @Override
    public void insertOne(ClientSession clientSession, TDocument document, InsertOneOptions options) {
        Assertions.notNull("clientSession", clientSession);
        Assertions.notNull("document", document);
        this.executeInsertOne(clientSession, document, options);
    }

    private void executeInsertOne(@Nullable ClientSession clientSession, TDocument document, InsertOneOptions options) {
        this.executeSingleWriteRequest(clientSession, this.operations.insertOne(document, options), WriteRequest.Type.INSERT);
    }

    @Override
    public void insertMany(List<? extends TDocument> documents) {
        this.insertMany(documents, new InsertManyOptions());
    }

    @Override
    public void insertMany(List<? extends TDocument> documents, InsertManyOptions options) {
        this.executeInsertMany(null, documents, options);
    }

    @Override
    public void insertMany(ClientSession clientSession, List<? extends TDocument> documents) {
        this.insertMany(clientSession, documents, new InsertManyOptions());
    }

    @Override
    public void insertMany(ClientSession clientSession, List<? extends TDocument> documents, InsertManyOptions options) {
        Assertions.notNull("clientSession", clientSession);
        this.executeInsertMany(clientSession, documents, options);
    }

    private void executeInsertMany(@Nullable ClientSession clientSession, List<? extends TDocument> documents, InsertManyOptions options) {
        this.executor.execute(this.operations.insertMany(documents, options), this.readConcern, clientSession);
    }

    @Override
    public DeleteResult deleteOne(Bson filter) {
        return this.deleteOne(filter, new DeleteOptions());
    }

    @Override
    public DeleteResult deleteOne(Bson filter, DeleteOptions options) {
        return this.executeDelete(null, filter, options, false);
    }

    @Override
    public DeleteResult deleteOne(ClientSession clientSession, Bson filter) {
        return this.deleteOne(clientSession, filter, new DeleteOptions());
    }

    @Override
    public DeleteResult deleteOne(ClientSession clientSession, Bson filter, DeleteOptions options) {
        Assertions.notNull("clientSession", clientSession);
        return this.executeDelete(clientSession, filter, options, false);
    }

    @Override
    public DeleteResult deleteMany(Bson filter) {
        return this.deleteMany(filter, new DeleteOptions());
    }

    @Override
    public DeleteResult deleteMany(Bson filter, DeleteOptions options) {
        return this.executeDelete(null, filter, options, true);
    }

    @Override
    public DeleteResult deleteMany(ClientSession clientSession, Bson filter) {
        return this.deleteMany(clientSession, filter, new DeleteOptions());
    }

    @Override
    public DeleteResult deleteMany(ClientSession clientSession, Bson filter, DeleteOptions options) {
        Assertions.notNull("clientSession", clientSession);
        return this.executeDelete(clientSession, filter, options, true);
    }

    @Override
    public UpdateResult replaceOne(Bson filter, TDocument replacement) {
        return this.replaceOne(filter, replacement, new ReplaceOptions());
    }

    @Override
    public UpdateResult replaceOne(Bson filter, TDocument replacement, UpdateOptions updateOptions) {
        return this.replaceOne(filter, replacement, ReplaceOptions.createReplaceOptions(updateOptions));
    }

    @Override
    public UpdateResult replaceOne(Bson filter, TDocument replacement, ReplaceOptions replaceOptions) {
        return this.executeReplaceOne(null, filter, replacement, replaceOptions);
    }

    @Override
    public UpdateResult replaceOne(ClientSession clientSession, Bson filter, TDocument replacement) {
        return this.replaceOne(clientSession, filter, replacement, new ReplaceOptions());
    }

    @Override
    public UpdateResult replaceOne(ClientSession clientSession, Bson filter, TDocument replacement, UpdateOptions updateOptions) {
        return this.replaceOne(clientSession, filter, replacement, ReplaceOptions.createReplaceOptions(updateOptions));
    }

    @Override
    public UpdateResult replaceOne(ClientSession clientSession, Bson filter, TDocument replacement, ReplaceOptions replaceOptions) {
        Assertions.notNull("clientSession", clientSession);
        return this.executeReplaceOne(clientSession, filter, replacement, replaceOptions);
    }

    private UpdateResult executeReplaceOne(@Nullable ClientSession clientSession, Bson filter, TDocument replacement, ReplaceOptions replaceOptions) {
        return this.toUpdateResult(this.executeSingleWriteRequest(clientSession, this.operations.replaceOne(filter, replacement, replaceOptions), WriteRequest.Type.REPLACE));
    }

    @Override
    public UpdateResult updateOne(Bson filter, Bson update) {
        return this.updateOne(filter, update, new UpdateOptions());
    }

    @Override
    public UpdateResult updateOne(Bson filter, Bson update, UpdateOptions updateOptions) {
        return this.executeUpdate(null, filter, update, updateOptions, false);
    }

    @Override
    public UpdateResult updateOne(ClientSession clientSession, Bson filter, Bson update) {
        return this.updateOne(clientSession, filter, update, new UpdateOptions());
    }

    @Override
    public UpdateResult updateOne(ClientSession clientSession, Bson filter, Bson update, UpdateOptions updateOptions) {
        Assertions.notNull("clientSession", clientSession);
        return this.executeUpdate(clientSession, filter, update, updateOptions, false);
    }

    @Override
    public UpdateResult updateOne(Bson filter, List<? extends Bson> update) {
        return this.updateOne(filter, update, new UpdateOptions());
    }

    @Override
    public UpdateResult updateOne(Bson filter, List<? extends Bson> update, UpdateOptions updateOptions) {
        return this.executeUpdate(null, filter, update, updateOptions, false);
    }

    @Override
    public UpdateResult updateOne(ClientSession clientSession, Bson filter, List<? extends Bson> update) {
        return this.updateOne(clientSession, filter, update, new UpdateOptions());
    }

    @Override
    public UpdateResult updateOne(ClientSession clientSession, Bson filter, List<? extends Bson> update, UpdateOptions updateOptions) {
        Assertions.notNull("clientSession", clientSession);
        return this.executeUpdate(clientSession, filter, update, updateOptions, false);
    }

    @Override
    public UpdateResult updateMany(Bson filter, Bson update) {
        return this.updateMany(filter, update, new UpdateOptions());
    }

    @Override
    public UpdateResult updateMany(Bson filter, Bson update, UpdateOptions updateOptions) {
        return this.executeUpdate(null, filter, update, updateOptions, true);
    }

    @Override
    public UpdateResult updateMany(ClientSession clientSession, Bson filter, Bson update) {
        return this.updateMany(clientSession, filter, update, new UpdateOptions());
    }

    @Override
    public UpdateResult updateMany(ClientSession clientSession, Bson filter, Bson update, UpdateOptions updateOptions) {
        Assertions.notNull("clientSession", clientSession);
        return this.executeUpdate(clientSession, filter, update, updateOptions, true);
    }

    @Override
    public UpdateResult updateMany(Bson filter, List<? extends Bson> update) {
        return this.updateMany(filter, update, new UpdateOptions());
    }

    @Override
    public UpdateResult updateMany(Bson filter, List<? extends Bson> update, UpdateOptions updateOptions) {
        return this.executeUpdate(null, filter, update, updateOptions, true);
    }

    @Override
    public UpdateResult updateMany(ClientSession clientSession, Bson filter, List<? extends Bson> update) {
        return this.updateMany(clientSession, filter, update, new UpdateOptions());
    }

    @Override
    public UpdateResult updateMany(ClientSession clientSession, Bson filter, List<? extends Bson> update, UpdateOptions updateOptions) {
        Assertions.notNull("clientSession", clientSession);
        return this.executeUpdate(clientSession, filter, update, updateOptions, true);
    }

    @Override
    @Nullable
    public TDocument findOneAndDelete(Bson filter) {
        return this.findOneAndDelete(filter, new FindOneAndDeleteOptions());
    }

    @Override
    @Nullable
    public TDocument findOneAndDelete(Bson filter, FindOneAndDeleteOptions options) {
        return this.executeFindOneAndDelete(null, filter, options);
    }

    @Override
    @Nullable
    public TDocument findOneAndDelete(ClientSession clientSession, Bson filter) {
        return this.findOneAndDelete(clientSession, filter, new FindOneAndDeleteOptions());
    }

    @Override
    @Nullable
    public TDocument findOneAndDelete(ClientSession clientSession, Bson filter, FindOneAndDeleteOptions options) {
        Assertions.notNull("clientSession", clientSession);
        return this.executeFindOneAndDelete(clientSession, filter, options);
    }

    @Nullable
    private TDocument executeFindOneAndDelete(@Nullable ClientSession clientSession, Bson filter, FindOneAndDeleteOptions options) {
        return this.executor.execute(this.operations.findOneAndDelete(filter, options), this.readConcern, clientSession);
    }

    @Override
    @Nullable
    public TDocument findOneAndReplace(Bson filter, TDocument replacement) {
        return this.findOneAndReplace(filter, replacement, new FindOneAndReplaceOptions());
    }

    @Override
    @Nullable
    public TDocument findOneAndReplace(Bson filter, TDocument replacement, FindOneAndReplaceOptions options) {
        return this.executeFindOneAndReplace(null, filter, replacement, options);
    }

    @Override
    @Nullable
    public TDocument findOneAndReplace(ClientSession clientSession, Bson filter, TDocument replacement) {
        return this.findOneAndReplace(clientSession, filter, replacement, new FindOneAndReplaceOptions());
    }

    @Override
    @Nullable
    public TDocument findOneAndReplace(ClientSession clientSession, Bson filter, TDocument replacement, FindOneAndReplaceOptions options) {
        Assertions.notNull("clientSession", clientSession);
        return this.executeFindOneAndReplace(clientSession, filter, replacement, options);
    }

    @Nullable
    private TDocument executeFindOneAndReplace(@Nullable ClientSession clientSession, Bson filter, TDocument replacement, FindOneAndReplaceOptions options) {
        return this.executor.execute(this.operations.findOneAndReplace(filter, replacement, options), this.readConcern, clientSession);
    }

    @Override
    @Nullable
    public TDocument findOneAndUpdate(Bson filter, Bson update) {
        return this.findOneAndUpdate(filter, update, new FindOneAndUpdateOptions());
    }

    @Override
    @Nullable
    public TDocument findOneAndUpdate(Bson filter, Bson update, FindOneAndUpdateOptions options) {
        return this.executeFindOneAndUpdate(null, filter, update, options);
    }

    @Override
    @Nullable
    public TDocument findOneAndUpdate(ClientSession clientSession, Bson filter, Bson update) {
        return this.findOneAndUpdate(clientSession, filter, update, new FindOneAndUpdateOptions());
    }

    @Override
    @Nullable
    public TDocument findOneAndUpdate(ClientSession clientSession, Bson filter, Bson update, FindOneAndUpdateOptions options) {
        Assertions.notNull("clientSession", clientSession);
        return this.executeFindOneAndUpdate(clientSession, filter, update, options);
    }

    @Nullable
    private TDocument executeFindOneAndUpdate(@Nullable ClientSession clientSession, Bson filter, Bson update, FindOneAndUpdateOptions options) {
        return this.executor.execute(this.operations.findOneAndUpdate(filter, update, options), this.readConcern, clientSession);
    }

    @Override
    @Nullable
    public TDocument findOneAndUpdate(Bson filter, List<? extends Bson> update) {
        return this.findOneAndUpdate(filter, update, new FindOneAndUpdateOptions());
    }

    @Override
    @Nullable
    public TDocument findOneAndUpdate(Bson filter, List<? extends Bson> update, FindOneAndUpdateOptions options) {
        return this.executeFindOneAndUpdate(null, filter, update, options);
    }

    @Override
    @Nullable
    public TDocument findOneAndUpdate(ClientSession clientSession, Bson filter, List<? extends Bson> update) {
        return this.findOneAndUpdate(clientSession, filter, update, new FindOneAndUpdateOptions());
    }

    @Override
    @Nullable
    public TDocument findOneAndUpdate(ClientSession clientSession, Bson filter, List<? extends Bson> update, FindOneAndUpdateOptions options) {
        Assertions.notNull("clientSession", clientSession);
        return this.executeFindOneAndUpdate(clientSession, filter, update, options);
    }

    @Nullable
    private TDocument executeFindOneAndUpdate(@Nullable ClientSession clientSession, Bson filter, List<? extends Bson> update, FindOneAndUpdateOptions options) {
        return this.executor.execute(this.operations.findOneAndUpdate(filter, update, options), this.readConcern, clientSession);
    }

    @Override
    public void drop() {
        this.executeDrop(null);
    }

    @Override
    public void drop(ClientSession clientSession) {
        Assertions.notNull("clientSession", clientSession);
        this.executeDrop(clientSession);
    }

    private void executeDrop(@Nullable ClientSession clientSession) {
        this.executor.execute(this.operations.dropCollection(), this.readConcern, clientSession);
    }

    @Override
    public String createIndex(Bson keys) {
        return this.createIndex(keys, new IndexOptions());
    }

    @Override
    public String createIndex(Bson keys, IndexOptions indexOptions) {
        return this.createIndexes(Collections.singletonList(new IndexModel(keys, indexOptions))).get(0);
    }

    @Override
    public String createIndex(ClientSession clientSession, Bson keys) {
        return this.createIndex(clientSession, keys, new IndexOptions());
    }

    @Override
    public String createIndex(ClientSession clientSession, Bson keys, IndexOptions indexOptions) {
        return this.createIndexes(clientSession, Collections.singletonList(new IndexModel(keys, indexOptions))).get(0);
    }

    @Override
    public List<String> createIndexes(List<IndexModel> indexes) {
        return this.createIndexes(indexes, new CreateIndexOptions());
    }

    @Override
    public List<String> createIndexes(List<IndexModel> indexes, CreateIndexOptions createIndexOptions) {
        return this.executeCreateIndexes(null, indexes, createIndexOptions);
    }

    @Override
    public List<String> createIndexes(ClientSession clientSession, List<IndexModel> indexes) {
        return this.createIndexes(clientSession, indexes, new CreateIndexOptions());
    }

    @Override
    public List<String> createIndexes(ClientSession clientSession, List<IndexModel> indexes, CreateIndexOptions createIndexOptions) {
        Assertions.notNull("clientSession", clientSession);
        return this.executeCreateIndexes(clientSession, indexes, createIndexOptions);
    }

    private List<String> executeCreateIndexes(@Nullable ClientSession clientSession, List<IndexModel> indexes, CreateIndexOptions createIndexOptions) {
        this.executor.execute(this.operations.createIndexes(indexes, createIndexOptions), this.readConcern, clientSession);
        return IndexHelper.getIndexNames(indexes, this.codecRegistry);
    }

    @Override
    public ListIndexesIterable<Document> listIndexes() {
        return this.listIndexes(Document.class);
    }

    @Override
    public <TResult> ListIndexesIterable<TResult> listIndexes(Class<TResult> resultClass) {
        return this.createListIndexesIterable(null, resultClass);
    }

    @Override
    public ListIndexesIterable<Document> listIndexes(ClientSession clientSession) {
        return this.listIndexes(clientSession, Document.class);
    }

    @Override
    public <TResult> ListIndexesIterable<TResult> listIndexes(ClientSession clientSession, Class<TResult> resultClass) {
        Assertions.notNull("clientSession", clientSession);
        return this.createListIndexesIterable(clientSession, resultClass);
    }

    private <TResult> ListIndexesIterable<TResult> createListIndexesIterable(@Nullable ClientSession clientSession, Class<TResult> resultClass) {
        return MongoIterables.listIndexesOf(clientSession, this.getNamespace(), resultClass, this.codecRegistry, ReadPreference.primary(), this.executor, this.retryReads);
    }

    @Override
    public void dropIndex(String indexName) {
        this.dropIndex(indexName, new DropIndexOptions());
    }

    @Override
    public void dropIndex(String indexName, DropIndexOptions dropIndexOptions) {
        this.executeDropIndex(null, indexName, dropIndexOptions);
    }

    @Override
    public void dropIndex(Bson keys) {
        this.dropIndex(keys, new DropIndexOptions());
    }

    @Override
    public void dropIndex(Bson keys, DropIndexOptions dropIndexOptions) {
        this.executeDropIndex(null, keys, dropIndexOptions);
    }

    @Override
    public void dropIndex(ClientSession clientSession, String indexName) {
        this.dropIndex(clientSession, indexName, new DropIndexOptions());
    }

    @Override
    public void dropIndex(ClientSession clientSession, Bson keys) {
        this.dropIndex(clientSession, keys, new DropIndexOptions());
    }

    @Override
    public void dropIndex(ClientSession clientSession, String indexName, DropIndexOptions dropIndexOptions) {
        Assertions.notNull("clientSession", clientSession);
        this.executeDropIndex(clientSession, indexName, dropIndexOptions);
    }

    @Override
    public void dropIndex(ClientSession clientSession, Bson keys, DropIndexOptions dropIndexOptions) {
        Assertions.notNull("clientSession", clientSession);
        this.executeDropIndex(clientSession, keys, dropIndexOptions);
    }

    @Override
    public void dropIndexes() {
        this.dropIndex("*");
    }

    @Override
    public void dropIndexes(ClientSession clientSession) {
        Assertions.notNull("clientSession", clientSession);
        this.executeDropIndex(clientSession, "*", new DropIndexOptions());
    }

    @Override
    public void dropIndexes(DropIndexOptions dropIndexOptions) {
        this.dropIndex("*", dropIndexOptions);
    }

    @Override
    public void dropIndexes(ClientSession clientSession, DropIndexOptions dropIndexOptions) {
        this.dropIndex(clientSession, "*", dropIndexOptions);
    }

    private void executeDropIndex(@Nullable ClientSession clientSession, String indexName, DropIndexOptions dropIndexOptions) {
        Assertions.notNull("dropIndexOptions", dropIndexOptions);
        this.executor.execute(this.operations.dropIndex(indexName, dropIndexOptions), this.readConcern, clientSession);
    }

    private void executeDropIndex(@Nullable ClientSession clientSession, Bson keys, DropIndexOptions dropIndexOptions) {
        this.executor.execute(this.operations.dropIndex(keys, dropIndexOptions), this.readConcern, clientSession);
    }

    @Override
    public void renameCollection(MongoNamespace newCollectionNamespace) {
        this.renameCollection(newCollectionNamespace, new RenameCollectionOptions());
    }

    @Override
    public void renameCollection(MongoNamespace newCollectionNamespace, RenameCollectionOptions renameCollectionOptions) {
        this.executeRenameCollection(null, newCollectionNamespace, renameCollectionOptions);
    }

    @Override
    public void renameCollection(ClientSession clientSession, MongoNamespace newCollectionNamespace) {
        this.renameCollection(clientSession, newCollectionNamespace, new RenameCollectionOptions());
    }

    @Override
    public void renameCollection(ClientSession clientSession, MongoNamespace newCollectionNamespace, RenameCollectionOptions renameCollectionOptions) {
        Assertions.notNull("clientSession", clientSession);
        this.executeRenameCollection(clientSession, newCollectionNamespace, renameCollectionOptions);
    }

    private void executeRenameCollection(@Nullable ClientSession clientSession, MongoNamespace newCollectionNamespace, RenameCollectionOptions renameCollectionOptions) {
        this.executor.execute(new RenameCollectionOperation(this.getNamespace(), newCollectionNamespace, this.writeConcern).dropTarget(renameCollectionOptions.isDropTarget()), this.readConcern, clientSession);
    }

    private DeleteResult executeDelete(@Nullable ClientSession clientSession, Bson filter, DeleteOptions deleteOptions, boolean multi) {
        BulkWriteResult result = this.executeSingleWriteRequest(clientSession, multi ? this.operations.deleteMany(filter, deleteOptions) : this.operations.deleteOne(filter, deleteOptions), WriteRequest.Type.DELETE);
        if (result.wasAcknowledged()) {
            return DeleteResult.acknowledged(result.getDeletedCount());
        }
        return DeleteResult.unacknowledged();
    }

    private UpdateResult executeUpdate(@Nullable ClientSession clientSession, Bson filter, Bson update, UpdateOptions updateOptions, boolean multi) {
        return this.toUpdateResult(this.executeSingleWriteRequest(clientSession, multi ? this.operations.updateMany(filter, update, updateOptions) : this.operations.updateOne(filter, update, updateOptions), WriteRequest.Type.UPDATE));
    }

    private UpdateResult executeUpdate(@Nullable ClientSession clientSession, Bson filter, List<? extends Bson> update, UpdateOptions updateOptions, boolean multi) {
        return this.toUpdateResult(this.executeSingleWriteRequest(clientSession, multi ? this.operations.updateMany(filter, update, updateOptions) : this.operations.updateOne(filter, update, updateOptions), WriteRequest.Type.UPDATE));
    }

    private BulkWriteResult executeSingleWriteRequest(@Nullable ClientSession clientSession, WriteOperation<BulkWriteResult> writeOperation, WriteRequest.Type type) {
        try {
            return this.executor.execute(writeOperation, this.readConcern, clientSession);
        } catch (MongoBulkWriteException e2) {
            if (e2.getWriteErrors().isEmpty()) {
                throw new MongoWriteConcernException(e2.getWriteConcernError(), this.translateBulkWriteResult(type, e2.getWriteResult()), e2.getServerAddress());
            }
            throw new MongoWriteException(new WriteError(e2.getWriteErrors().get(0)), e2.getServerAddress());
        }
    }

    private WriteConcernResult translateBulkWriteResult(WriteRequest.Type type, BulkWriteResult writeResult) {
        switch (type) {
            case INSERT: {
                return WriteConcernResult.acknowledged(writeResult.getInsertedCount(), false, null);
            }
            case DELETE: {
                return WriteConcernResult.acknowledged(writeResult.getDeletedCount(), false, null);
            }
            case UPDATE: 
            case REPLACE: {
                return WriteConcernResult.acknowledged(writeResult.getMatchedCount() + writeResult.getUpserts().size(), writeResult.getMatchedCount() > 0, writeResult.getUpserts().isEmpty() ? null : writeResult.getUpserts().get(0).getId());
            }
        }
        throw new MongoInternalException("Unhandled write request type: " + (Object)((Object)type));
    }

    private UpdateResult toUpdateResult(BulkWriteResult result) {
        if (result.wasAcknowledged()) {
            BsonValue upsertedId = result.getUpserts().isEmpty() ? null : result.getUpserts().get(0).getId();
            return UpdateResult.acknowledged(result.getMatchedCount(), Long.valueOf(result.getModifiedCount()), upsertedId);
        }
        return UpdateResult.unacknowledged();
    }
}

