/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.internal;

import com.mongodb.Function;
import com.mongodb.MongoClientException;
import com.mongodb.MongoNamespace;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.assertions.Assertions;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.ClientSession;
import com.mongodb.client.ListCollectionsIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.internal.MongoCollectionImpl;
import com.mongodb.client.internal.MongoIterables;
import com.mongodb.client.internal.OperationExecutor;
import com.mongodb.client.model.AggregationLevel;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.CreateViewOptions;
import com.mongodb.client.model.IndexOptionDefaults;
import com.mongodb.client.model.ValidationOptions;
import com.mongodb.client.model.changestream.ChangeStreamLevel;
import com.mongodb.lang.Nullable;
import com.mongodb.operation.CommandReadOperation;
import com.mongodb.operation.CreateCollectionOperation;
import com.mongodb.operation.CreateViewOperation;
import com.mongodb.operation.DropDatabaseOperation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.bson.internal.CodecRegistryHelper;

public class MongoDatabaseImpl
implements MongoDatabase {
    private final String name;
    private final ReadPreference readPreference;
    private final CodecRegistry codecRegistry;
    private final WriteConcern writeConcern;
    private final boolean retryWrites;
    private final boolean retryReads;
    private final ReadConcern readConcern;
    private final OperationExecutor executor;
    private UuidRepresentation uuidRepresentation;

    public MongoDatabaseImpl(String name, CodecRegistry codecRegistry, ReadPreference readPreference, WriteConcern writeConcern, boolean retryWrites, boolean retryReads, ReadConcern readConcern, UuidRepresentation uuidRepresentation, OperationExecutor executor) {
        MongoNamespace.checkDatabaseNameValidity(name);
        this.name = Assertions.notNull("name", name);
        this.codecRegistry = Assertions.notNull("codecRegistry", codecRegistry);
        this.readPreference = Assertions.notNull("readPreference", readPreference);
        this.writeConcern = Assertions.notNull("writeConcern", writeConcern);
        this.retryWrites = retryWrites;
        this.retryReads = retryReads;
        this.readConcern = Assertions.notNull("readConcern", readConcern);
        this.uuidRepresentation = Assertions.notNull("uuidRepresentation", uuidRepresentation);
        this.executor = Assertions.notNull("executor", executor);
    }

    @Override
    public String getName() {
        return this.name;
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
    public MongoDatabase withCodecRegistry(CodecRegistry codecRegistry) {
        return new MongoDatabaseImpl(this.name, CodecRegistryHelper.createRegistry(codecRegistry, this.uuidRepresentation), this.readPreference, this.writeConcern, this.retryWrites, this.retryReads, this.readConcern, this.uuidRepresentation, this.executor);
    }

    @Override
    public MongoDatabase withReadPreference(ReadPreference readPreference) {
        return new MongoDatabaseImpl(this.name, this.codecRegistry, readPreference, this.writeConcern, this.retryWrites, this.retryReads, this.readConcern, this.uuidRepresentation, this.executor);
    }

    @Override
    public MongoDatabase withWriteConcern(WriteConcern writeConcern) {
        return new MongoDatabaseImpl(this.name, this.codecRegistry, this.readPreference, writeConcern, this.retryWrites, this.retryReads, this.readConcern, this.uuidRepresentation, this.executor);
    }

    @Override
    public MongoDatabase withReadConcern(ReadConcern readConcern) {
        return new MongoDatabaseImpl(this.name, this.codecRegistry, this.readPreference, this.writeConcern, this.retryWrites, this.retryReads, readConcern, this.uuidRepresentation, this.executor);
    }

    @Override
    public MongoCollection<Document> getCollection(String collectionName) {
        return this.getCollection(collectionName, Document.class);
    }

    @Override
    public <TDocument> MongoCollection<TDocument> getCollection(String collectionName, Class<TDocument> documentClass) {
        return new MongoCollectionImpl<TDocument>(new MongoNamespace(this.name, collectionName), documentClass, this.codecRegistry, this.readPreference, this.writeConcern, this.retryWrites, this.retryReads, this.readConcern, this.uuidRepresentation, this.executor);
    }

    @Override
    public Document runCommand(Bson command) {
        return this.runCommand(command, Document.class);
    }

    @Override
    public Document runCommand(Bson command, ReadPreference readPreference) {
        return this.runCommand(command, readPreference, Document.class);
    }

    @Override
    public <TResult> TResult runCommand(Bson command, Class<TResult> resultClass) {
        return this.runCommand(command, ReadPreference.primary(), resultClass);
    }

    @Override
    public <TResult> TResult runCommand(Bson command, ReadPreference readPreference, Class<TResult> resultClass) {
        return this.executeCommand(null, command, readPreference, resultClass);
    }

    @Override
    public Document runCommand(ClientSession clientSession, Bson command) {
        return this.runCommand(clientSession, command, ReadPreference.primary(), Document.class);
    }

    @Override
    public Document runCommand(ClientSession clientSession, Bson command, ReadPreference readPreference) {
        return this.runCommand(clientSession, command, readPreference, Document.class);
    }

    @Override
    public <TResult> TResult runCommand(ClientSession clientSession, Bson command, Class<TResult> resultClass) {
        return this.runCommand(clientSession, command, ReadPreference.primary(), resultClass);
    }

    @Override
    public <TResult> TResult runCommand(ClientSession clientSession, Bson command, ReadPreference readPreference, Class<TResult> resultClass) {
        Assertions.notNull("clientSession", clientSession);
        return this.executeCommand(clientSession, command, readPreference, resultClass);
    }

    private <TResult> TResult executeCommand(@Nullable ClientSession clientSession, Bson command, ReadPreference readPreference, Class<TResult> resultClass) {
        Assertions.notNull("readPreference", readPreference);
        if (clientSession != null && clientSession.hasActiveTransaction() && !readPreference.equals(ReadPreference.primary())) {
            throw new MongoClientException("Read preference in a transaction must be primary");
        }
        return this.executor.execute(new CommandReadOperation<TResult>(this.getName(), this.toBsonDocument(command), this.codecRegistry.get(resultClass)), readPreference, this.readConcern, clientSession);
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
        this.executor.execute(new DropDatabaseOperation(this.name, this.getWriteConcern()), this.readConcern, clientSession);
    }

    @Override
    public MongoIterable<String> listCollectionNames() {
        return this.createListCollectionNamesIterable(null);
    }

    @Override
    public MongoIterable<String> listCollectionNames(ClientSession clientSession) {
        Assertions.notNull("clientSession", clientSession);
        return this.createListCollectionNamesIterable(clientSession);
    }

    private MongoIterable<String> createListCollectionNamesIterable(@Nullable ClientSession clientSession) {
        return this.createListCollectionsIterable(clientSession, BsonDocument.class, true).map(new Function<BsonDocument, String>(){

            @Override
            public String apply(BsonDocument result) {
                return result.getString("name").getValue();
            }
        });
    }

    @Override
    public ListCollectionsIterable<Document> listCollections() {
        return this.listCollections(Document.class);
    }

    @Override
    public <TResult> ListCollectionsIterable<TResult> listCollections(Class<TResult> resultClass) {
        return this.createListCollectionsIterable(null, resultClass, false);
    }

    @Override
    public ListCollectionsIterable<Document> listCollections(ClientSession clientSession) {
        return this.listCollections(clientSession, Document.class);
    }

    @Override
    public <TResult> ListCollectionsIterable<TResult> listCollections(ClientSession clientSession, Class<TResult> resultClass) {
        Assertions.notNull("clientSession", clientSession);
        return this.createListCollectionsIterable(clientSession, resultClass, false);
    }

    private <TResult> ListCollectionsIterable<TResult> createListCollectionsIterable(@Nullable ClientSession clientSession, Class<TResult> resultClass, boolean collectionNamesOnly) {
        return MongoIterables.listCollectionsOf(clientSession, this.name, collectionNamesOnly, resultClass, this.codecRegistry, ReadPreference.primary(), this.executor, this.retryReads);
    }

    @Override
    public void createCollection(String collectionName) {
        this.createCollection(collectionName, new CreateCollectionOptions());
    }

    @Override
    public void createCollection(String collectionName, CreateCollectionOptions createCollectionOptions) {
        this.executeCreateCollection(null, collectionName, createCollectionOptions);
    }

    @Override
    public void createCollection(ClientSession clientSession, String collectionName) {
        this.createCollection(clientSession, collectionName, new CreateCollectionOptions());
    }

    @Override
    public void createCollection(ClientSession clientSession, String collectionName, CreateCollectionOptions createCollectionOptions) {
        Assertions.notNull("clientSession", clientSession);
        this.executeCreateCollection(clientSession, collectionName, createCollectionOptions);
    }

    private void executeCreateCollection(@Nullable ClientSession clientSession, String collectionName, CreateCollectionOptions createCollectionOptions) {
        ValidationOptions validationOptions;
        Bson validator;
        CreateCollectionOperation operation = new CreateCollectionOperation(this.name, collectionName, this.writeConcern).collation(createCollectionOptions.getCollation()).capped(createCollectionOptions.isCapped()).sizeInBytes(createCollectionOptions.getSizeInBytes()).autoIndex(createCollectionOptions.isAutoIndex()).maxDocuments(createCollectionOptions.getMaxDocuments()).usePowerOf2Sizes(createCollectionOptions.isUsePowerOf2Sizes()).storageEngineOptions(this.toBsonDocument(createCollectionOptions.getStorageEngineOptions()));
        IndexOptionDefaults indexOptionDefaults = createCollectionOptions.getIndexOptionDefaults();
        Bson storageEngine = indexOptionDefaults.getStorageEngine();
        if (storageEngine != null) {
            operation.indexOptionDefaults(new BsonDocument("storageEngine", this.toBsonDocument(storageEngine)));
        }
        if ((validator = (validationOptions = createCollectionOptions.getValidationOptions()).getValidator()) != null) {
            operation.validator(this.toBsonDocument(validator));
        }
        if (validationOptions.getValidationLevel() != null) {
            operation.validationLevel(validationOptions.getValidationLevel());
        }
        if (validationOptions.getValidationAction() != null) {
            operation.validationAction(validationOptions.getValidationAction());
        }
        this.executor.execute(operation, this.readConcern, clientSession);
    }

    @Override
    public void createView(String viewName, String viewOn, List<? extends Bson> pipeline) {
        this.createView(viewName, viewOn, pipeline, new CreateViewOptions());
    }

    @Override
    public void createView(String viewName, String viewOn, List<? extends Bson> pipeline, CreateViewOptions createViewOptions) {
        this.executeCreateView(null, viewName, viewOn, pipeline, createViewOptions);
    }

    @Override
    public void createView(ClientSession clientSession, String viewName, String viewOn, List<? extends Bson> pipeline) {
        this.createView(clientSession, viewName, viewOn, pipeline, new CreateViewOptions());
    }

    @Override
    public void createView(ClientSession clientSession, String viewName, String viewOn, List<? extends Bson> pipeline, CreateViewOptions createViewOptions) {
        Assertions.notNull("clientSession", clientSession);
        this.executeCreateView(clientSession, viewName, viewOn, pipeline, createViewOptions);
    }

    @Override
    public ChangeStreamIterable<Document> watch() {
        return this.watch(Collections.emptyList());
    }

    @Override
    public <TResult> ChangeStreamIterable<TResult> watch(Class<TResult> resultClass) {
        return this.watch(Collections.emptyList(), resultClass);
    }

    @Override
    public ChangeStreamIterable<Document> watch(List<? extends Bson> pipeline) {
        return this.watch(pipeline, Document.class);
    }

    @Override
    public <TResult> ChangeStreamIterable<TResult> watch(List<? extends Bson> pipeline, Class<TResult> resultClass) {
        return this.createChangeStreamIterable(null, pipeline, resultClass);
    }

    @Override
    public ChangeStreamIterable<Document> watch(ClientSession clientSession) {
        return this.watch(clientSession, Collections.emptyList(), Document.class);
    }

    @Override
    public <TResult> ChangeStreamIterable<TResult> watch(ClientSession clientSession, Class<TResult> resultClass) {
        return this.watch(clientSession, Collections.emptyList(), resultClass);
    }

    @Override
    public ChangeStreamIterable<Document> watch(ClientSession clientSession, List<? extends Bson> pipeline) {
        return this.watch(clientSession, pipeline, Document.class);
    }

    @Override
    public <TResult> ChangeStreamIterable<TResult> watch(ClientSession clientSession, List<? extends Bson> pipeline, Class<TResult> resultClass) {
        Assertions.notNull("clientSession", clientSession);
        return this.createChangeStreamIterable(clientSession, pipeline, resultClass);
    }

    @Override
    public AggregateIterable<Document> aggregate(List<? extends Bson> pipeline) {
        return this.aggregate(pipeline, Document.class);
    }

    @Override
    public <TResult> AggregateIterable<TResult> aggregate(List<? extends Bson> pipeline, Class<TResult> resultClass) {
        return this.createAggregateIterable(null, pipeline, resultClass);
    }

    @Override
    public AggregateIterable<Document> aggregate(ClientSession clientSession, List<? extends Bson> pipeline) {
        return this.aggregate(clientSession, pipeline, Document.class);
    }

    @Override
    public <TResult> AggregateIterable<TResult> aggregate(ClientSession clientSession, List<? extends Bson> pipeline, Class<TResult> resultClass) {
        Assertions.notNull("clientSession", clientSession);
        return this.createAggregateIterable(clientSession, pipeline, resultClass);
    }

    private <TResult> AggregateIterable<TResult> createAggregateIterable(@Nullable ClientSession clientSession, List<? extends Bson> pipeline, Class<TResult> resultClass) {
        return MongoIterables.aggregateOf(clientSession, this.name, Document.class, resultClass, this.codecRegistry, this.readPreference, this.readConcern, this.writeConcern, this.executor, pipeline, AggregationLevel.DATABASE, this.retryReads);
    }

    private <TResult> ChangeStreamIterable<TResult> createChangeStreamIterable(@Nullable ClientSession clientSession, List<? extends Bson> pipeline, Class<TResult> resultClass) {
        return MongoIterables.changeStreamOf(clientSession, this.name, this.codecRegistry, this.readPreference, this.readConcern, this.executor, pipeline, resultClass, ChangeStreamLevel.DATABASE, this.retryReads);
    }

    private void executeCreateView(@Nullable ClientSession clientSession, String viewName, String viewOn, List<? extends Bson> pipeline, CreateViewOptions createViewOptions) {
        Assertions.notNull("createViewOptions", createViewOptions);
        this.executor.execute(new CreateViewOperation(this.name, viewName, viewOn, this.createBsonDocumentList(pipeline), this.writeConcern).collation(createViewOptions.getCollation()), this.readConcern, clientSession);
    }

    private List<BsonDocument> createBsonDocumentList(List<? extends Bson> pipeline) {
        Assertions.notNull("pipeline", pipeline);
        ArrayList<BsonDocument> bsonDocumentPipeline = new ArrayList<BsonDocument>(pipeline.size());
        for (Bson bson : pipeline) {
            if (bson == null) {
                throw new IllegalArgumentException("pipeline can not contain a null value");
            }
            bsonDocumentPipeline.add(bson.toBsonDocument(BsonDocument.class, this.codecRegistry));
        }
        return bsonDocumentPipeline;
    }

    @Nullable
    private BsonDocument toBsonDocument(@Nullable Bson document) {
        return document == null ? null : document.toBsonDocument(BsonDocument.class, this.codecRegistry);
    }
}

