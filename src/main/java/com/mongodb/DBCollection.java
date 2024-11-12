/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.AggregationOptions;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteHelper;
import com.mongodb.BulkWriteOperation;
import com.mongodb.BulkWriteResult;
import com.mongodb.Bytes;
import com.mongodb.CommandResult;
import com.mongodb.CompoundDBObjectCodec;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollectionObjectFactory;
import com.mongodb.DBCursor;
import com.mongodb.DBDecoderAdapter;
import com.mongodb.DBDecoderFactory;
import com.mongodb.DBEncoder;
import com.mongodb.DBEncoderAdapter;
import com.mongodb.DBEncoderFactory;
import com.mongodb.DBEncoderFactoryAdapter;
import com.mongodb.DBObject;
import com.mongodb.DBObjectCodec;
import com.mongodb.DBObjectCollationHelper;
import com.mongodb.DBObjectFactory;
import com.mongodb.DefaultDBDecoder;
import com.mongodb.DefaultDBEncoder;
import com.mongodb.ExplainVerbosity;
import com.mongodb.Function;
import com.mongodb.GroupCommand;
import com.mongodb.InsertOptions;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;
import com.mongodb.MongoBulkWriteException;
import com.mongodb.MongoClient;
import com.mongodb.MongoCursorAdapter;
import com.mongodb.MongoInternalException;
import com.mongodb.MongoNamespace;
import com.mongodb.MongoWriteConcernException;
import com.mongodb.ParallelScanOptions;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.WriteConcernException;
import com.mongodb.WriteConcernResult;
import com.mongodb.WriteResult;
import com.mongodb.annotations.ThreadSafe;
import com.mongodb.assertions.Assertions;
import com.mongodb.bulk.DeleteRequest;
import com.mongodb.bulk.IndexRequest;
import com.mongodb.bulk.InsertRequest;
import com.mongodb.bulk.UpdateRequest;
import com.mongodb.bulk.WriteRequest;
import com.mongodb.client.internal.MongoBatchCursorAdapter;
import com.mongodb.client.internal.MongoIterableImpl;
import com.mongodb.client.internal.OperationExecutor;
import com.mongodb.client.model.DBCollectionCountOptions;
import com.mongodb.client.model.DBCollectionDistinctOptions;
import com.mongodb.client.model.DBCollectionFindAndModifyOptions;
import com.mongodb.client.model.DBCollectionFindOptions;
import com.mongodb.client.model.DBCollectionRemoveOptions;
import com.mongodb.client.model.DBCollectionUpdateOptions;
import com.mongodb.connection.BufferProvider;
import com.mongodb.lang.Nullable;
import com.mongodb.operation.AggregateOperation;
import com.mongodb.operation.AggregateToCollectionOperation;
import com.mongodb.operation.BaseFindAndModifyOperation;
import com.mongodb.operation.BaseWriteOperation;
import com.mongodb.operation.BatchCursor;
import com.mongodb.operation.CountOperation;
import com.mongodb.operation.CreateIndexesOperation;
import com.mongodb.operation.DeleteOperation;
import com.mongodb.operation.DistinctOperation;
import com.mongodb.operation.DropCollectionOperation;
import com.mongodb.operation.DropIndexOperation;
import com.mongodb.operation.FindAndDeleteOperation;
import com.mongodb.operation.FindAndReplaceOperation;
import com.mongodb.operation.FindAndUpdateOperation;
import com.mongodb.operation.InsertOperation;
import com.mongodb.operation.ListIndexesOperation;
import com.mongodb.operation.MapReduceBatchCursor;
import com.mongodb.operation.MapReduceStatistics;
import com.mongodb.operation.MapReduceToCollectionOperation;
import com.mongodb.operation.MapReduceWithInlineResultsOperation;
import com.mongodb.operation.MixedBulkWriteOperation;
import com.mongodb.operation.ParallelCollectionScanOperation;
import com.mongodb.operation.ReadOperation;
import com.mongodb.operation.RenameCollectionOperation;
import com.mongodb.operation.UpdateOperation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.bson.BsonDocument;
import org.bson.BsonDocumentReader;
import org.bson.BsonDocumentWrapper;
import org.bson.BsonInt32;
import org.bson.BsonJavaScript;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.codecs.BsonDocumentCodec;
import org.bson.codecs.BsonValueCodec;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.Encoder;
import org.bson.types.ObjectId;

@ThreadSafe
public class DBCollection {
    public static final String ID_FIELD_NAME = "_id";
    private final String name;
    private final DB database;
    private final OperationExecutor executor;
    private final Bytes.OptionHolder optionHolder;
    private final boolean retryWrites;
    private final boolean retryReads;
    private volatile ReadPreference readPreference;
    private volatile WriteConcern writeConcern;
    private volatile ReadConcern readConcern;
    private List<DBObject> hintFields;
    private DBEncoderFactory encoderFactory;
    private DBDecoderFactory decoderFactory;
    private DBCollectionObjectFactory objectFactory;
    private volatile CompoundDBObjectCodec objectCodec;

    DBCollection(String name, DB database, OperationExecutor executor) {
        MongoNamespace.checkCollectionNameValidity(name);
        this.name = name;
        this.database = database;
        this.executor = executor;
        this.optionHolder = new Bytes.OptionHolder(database.getOptionHolder());
        this.objectFactory = new DBCollectionObjectFactory();
        this.objectCodec = new CompoundDBObjectCodec(this.getDefaultDBObjectCodec());
        this.retryWrites = database.getMongo().getMongoClientOptions().getRetryWrites();
        this.retryReads = database.getMongo().getMongoClientOptions().getRetryReads();
    }

    protected DBCollection(DB database, String name) {
        this(name, database, database.getExecutor());
    }

    private static BasicDBList toDBList(BatchCursor<DBObject> source) {
        BasicDBList dbList = new BasicDBList();
        while (source.hasNext()) {
            dbList.addAll(source.next());
        }
        return dbList;
    }

    public WriteResult insert(DBObject document, WriteConcern writeConcern) {
        return this.insert(Arrays.asList(document), writeConcern);
    }

    public WriteResult insert(DBObject ... documents) {
        return this.insert(Arrays.asList(documents), this.getWriteConcern());
    }

    public WriteResult insert(WriteConcern writeConcern, DBObject ... documents) {
        return this.insert(documents, writeConcern);
    }

    public WriteResult insert(DBObject[] documents, WriteConcern writeConcern) {
        return this.insert(Arrays.asList(documents), writeConcern);
    }

    public WriteResult insert(List<? extends DBObject> documents) {
        return this.insert(documents, this.getWriteConcern());
    }

    public WriteResult insert(List<? extends DBObject> documents, WriteConcern aWriteConcern) {
        return this.insert(documents, aWriteConcern, null);
    }

    public WriteResult insert(DBObject[] documents, WriteConcern aWriteConcern, DBEncoder encoder) {
        return this.insert(Arrays.asList(documents), aWriteConcern, encoder);
    }

    public WriteResult insert(List<? extends DBObject> documents, WriteConcern aWriteConcern, @Nullable DBEncoder dbEncoder) {
        return this.insert(documents, new InsertOptions().writeConcern(aWriteConcern).dbEncoder(dbEncoder));
    }

    public WriteResult insert(List<? extends DBObject> documents, InsertOptions insertOptions) {
        WriteConcern writeConcern = insertOptions.getWriteConcern() != null ? insertOptions.getWriteConcern() : this.getWriteConcern();
        Encoder<DBObject> encoder = this.toEncoder(insertOptions.getDbEncoder());
        ArrayList<InsertRequest> insertRequestList = new ArrayList<InsertRequest>(documents.size());
        for (DBObject dBObject : documents) {
            if (dBObject.get(ID_FIELD_NAME) == null) {
                dBObject.put(ID_FIELD_NAME, new ObjectId());
            }
            insertRequestList.add(new InsertRequest(new BsonDocumentWrapper<DBObject>(dBObject, encoder)));
        }
        return this.insert(insertRequestList, writeConcern, insertOptions.isContinueOnError(), insertOptions.getBypassDocumentValidation());
    }

    private Encoder<DBObject> toEncoder(@Nullable DBEncoder dbEncoder) {
        return dbEncoder != null ? new DBEncoderAdapter(dbEncoder) : this.objectCodec;
    }

    private WriteResult insert(List<InsertRequest> insertRequestList, WriteConcern writeConcern, boolean continueOnError, @Nullable Boolean bypassDocumentValidation) {
        return this.executeWriteOperation(new InsertOperation(this.getNamespace(), !continueOnError, writeConcern, this.retryWrites, insertRequestList).bypassDocumentValidation(bypassDocumentValidation));
    }

    WriteResult executeWriteOperation(BaseWriteOperation operation) {
        return this.translateWriteResult(this.executor.execute(operation, this.getReadConcern()));
    }

    private WriteResult translateWriteResult(WriteConcernResult writeConcernResult) {
        if (!writeConcernResult.wasAcknowledged()) {
            return WriteResult.unacknowledged();
        }
        return this.translateWriteResult(writeConcernResult.getCount(), writeConcernResult.isUpdateOfExisting(), writeConcernResult.getUpsertedId());
    }

    private WriteResult translateWriteResult(int count, boolean isUpdateOfExisting, @Nullable BsonValue upsertedId) {
        Object newUpsertedId = upsertedId == null ? null : ((DBObject)this.getObjectCodec().decode(new BsonDocumentReader(new BsonDocument(ID_FIELD_NAME, upsertedId)), DecoderContext.builder().build())).get(ID_FIELD_NAME);
        return new WriteResult(count, isUpdateOfExisting, newUpsertedId);
    }

    public WriteResult save(DBObject document) {
        return this.save(document, this.getWriteConcern());
    }

    public WriteResult save(DBObject document, WriteConcern writeConcern) {
        Object id = document.get(ID_FIELD_NAME);
        if (id == null) {
            return this.insert(document, writeConcern);
        }
        return this.replaceOrInsert(document, id, writeConcern);
    }

    private WriteResult replaceOrInsert(DBObject obj, Object id, WriteConcern writeConcern) {
        BasicDBObject filter = new BasicDBObject(ID_FIELD_NAME, id);
        UpdateRequest replaceRequest = new UpdateRequest(this.wrap(filter), this.wrap(obj, this.objectCodec), WriteRequest.Type.REPLACE).upsert(true);
        return this.executeWriteOperation(new UpdateOperation(this.getNamespace(), false, writeConcern, this.retryWrites, Collections.singletonList(replaceRequest)));
    }

    public WriteResult update(DBObject query, DBObject update, boolean upsert, boolean multi, WriteConcern aWriteConcern) {
        return this.update(query, update, upsert, multi, aWriteConcern, null);
    }

    public WriteResult update(DBObject query, DBObject update, boolean upsert, boolean multi, WriteConcern concern, @Nullable DBEncoder encoder) {
        return this.update(query, update, upsert, multi, concern, null, encoder);
    }

    public WriteResult update(DBObject query, DBObject update, boolean upsert, boolean multi, WriteConcern concern, @Nullable Boolean bypassDocumentValidation, @Nullable DBEncoder encoder) {
        return this.update(query, update, new DBCollectionUpdateOptions().upsert(upsert).multi(multi).writeConcern(concern).bypassDocumentValidation(bypassDocumentValidation).encoder(encoder));
    }

    public WriteResult update(DBObject query, DBObject update, boolean upsert, boolean multi) {
        return this.update(query, update, upsert, multi, this.getWriteConcern());
    }

    public WriteResult update(DBObject query, DBObject update) {
        return this.update(query, update, false, false);
    }

    public WriteResult updateMulti(DBObject query, DBObject update) {
        return this.update(query, update, false, true);
    }

    public WriteResult update(DBObject query, DBObject update, DBCollectionUpdateOptions options) {
        Assertions.notNull("query", query);
        Assertions.notNull("update", update);
        Assertions.notNull("options", options);
        WriteConcern writeConcern = options.getWriteConcern() != null ? options.getWriteConcern() : this.getWriteConcern();
        WriteRequest.Type updateType = !update.keySet().isEmpty() && update.keySet().iterator().next().startsWith("$") ? WriteRequest.Type.UPDATE : WriteRequest.Type.REPLACE;
        UpdateRequest updateRequest = new UpdateRequest(this.wrap(query), this.wrap(update, options.getEncoder()), updateType).upsert(options.isUpsert()).multi(options.isMulti()).collation(options.getCollation()).arrayFilters(this.wrapAllowNull(options.getArrayFilters(), options.getEncoder()));
        return this.executeWriteOperation(new UpdateOperation(this.getNamespace(), true, writeConcern, this.retryWrites, Collections.singletonList(updateRequest)).bypassDocumentValidation(options.getBypassDocumentValidation()));
    }

    public WriteResult remove(DBObject query) {
        return this.remove(query, this.getWriteConcern());
    }

    public WriteResult remove(DBObject query, WriteConcern writeConcern) {
        return this.remove(query, new DBCollectionRemoveOptions().writeConcern(writeConcern));
    }

    public WriteResult remove(DBObject query, WriteConcern writeConcern, DBEncoder encoder) {
        return this.remove(query, new DBCollectionRemoveOptions().writeConcern(writeConcern).encoder(encoder));
    }

    public WriteResult remove(DBObject query, DBCollectionRemoveOptions options) {
        Assertions.notNull("query", query);
        Assertions.notNull("options", options);
        WriteConcern writeConcern = options.getWriteConcern() != null ? options.getWriteConcern() : this.getWriteConcern();
        DeleteRequest deleteRequest = new DeleteRequest(this.wrap(query, options.getEncoder())).collation(options.getCollation());
        return this.executeWriteOperation(new DeleteOperation(this.getNamespace(), false, writeConcern, this.retryWrites, Collections.singletonList(deleteRequest)));
    }

    @Deprecated
    public DBCursor find(DBObject query, DBObject projection, int numToSkip, int batchSize, int options) {
        return new DBCursor(this, query, projection, this.getReadPreference()).batchSize(batchSize).skip(numToSkip).setOptions(options);
    }

    @Deprecated
    public DBCursor find(DBObject query, DBObject projection, int numToSkip, int batchSize) {
        return new DBCursor(this, query, projection, this.getReadPreference()).batchSize(batchSize).skip(numToSkip);
    }

    public DBCursor find(DBObject query) {
        return new DBCursor(this, query, null, this.getReadPreference());
    }

    public DBCursor find(DBObject query, DBObject projection) {
        return new DBCursor(this, query, projection, this.getReadPreference());
    }

    public DBCursor find() {
        return this.find(new BasicDBObject());
    }

    public DBCursor find(@Nullable DBObject query, DBCollectionFindOptions options) {
        return new DBCursor(this, query, options);
    }

    @Nullable
    public DBObject findOne() {
        return this.findOne(new BasicDBObject());
    }

    @Nullable
    public DBObject findOne(DBObject query) {
        return this.findOne(query, null, null, this.getReadPreference());
    }

    @Nullable
    public DBObject findOne(DBObject query, DBObject projection) {
        return this.findOne(query, projection, null, this.getReadPreference());
    }

    @Nullable
    public DBObject findOne(DBObject query, DBObject projection, DBObject sort) {
        return this.findOne(query, projection, sort, this.getReadPreference());
    }

    @Nullable
    public DBObject findOne(DBObject query, DBObject projection, ReadPreference readPreference) {
        return this.findOne(query, projection, null, readPreference);
    }

    @Nullable
    public DBObject findOne(@Nullable DBObject query, @Nullable DBObject projection, @Nullable DBObject sort, ReadPreference readPreference) {
        return this.findOne(query != null ? query : new BasicDBObject(), new DBCollectionFindOptions().projection(projection).sort(sort).readPreference(readPreference));
    }

    @Nullable
    public DBObject findOne(Object id) {
        return this.findOne((DBObject)new BasicDBObject(ID_FIELD_NAME, id), new DBCollectionFindOptions());
    }

    @Nullable
    public DBObject findOne(Object id, DBObject projection) {
        return this.findOne((DBObject)new BasicDBObject(ID_FIELD_NAME, id), new DBCollectionFindOptions().projection(projection));
    }

    @Nullable
    public DBObject findOne(@Nullable DBObject query, DBCollectionFindOptions findOptions) {
        return this.find(query, findOptions).one();
    }

    public long count() {
        return this.getCount((DBObject)new BasicDBObject(), new DBCollectionCountOptions());
    }

    public long count(@Nullable DBObject query) {
        return this.getCount(query, new DBCollectionCountOptions());
    }

    public long count(@Nullable DBObject query, ReadPreference readPreference) {
        return this.getCount(query, null, readPreference);
    }

    public long count(@Nullable DBObject query, DBCollectionCountOptions options) {
        return this.getCount(query, options);
    }

    public long getCount() {
        return this.getCount((DBObject)new BasicDBObject(), new DBCollectionCountOptions());
    }

    public long getCount(ReadPreference readPreference) {
        return this.getCount(new BasicDBObject(), null, readPreference);
    }

    public long getCount(@Nullable DBObject query) {
        return this.getCount(query, new DBCollectionCountOptions());
    }

    @Deprecated
    public long getCount(@Nullable DBObject query, DBObject projection) {
        return this.getCount(query, projection, 0L, 0L);
    }

    @Deprecated
    public long getCount(@Nullable DBObject query, @Nullable DBObject projection, ReadPreference readPreference) {
        return this.getCount(query, projection, 0L, 0L, readPreference);
    }

    @Deprecated
    public long getCount(@Nullable DBObject query, @Nullable DBObject projection, long limit, long skip) {
        return this.getCount(query, projection, limit, skip, this.getReadPreference());
    }

    @Deprecated
    public long getCount(@Nullable DBObject query, @Nullable DBObject projection, long limit, long skip, ReadPreference readPreference) {
        return this.getCount(query, new DBCollectionCountOptions().limit(limit).skip(skip).readPreference(readPreference));
    }

    public long getCount(@Nullable DBObject query, DBCollectionCountOptions options) {
        DBObject hint;
        Assertions.notNull("countOptions", options);
        CountOperation operation = new CountOperation(this.getNamespace()).skip(options.getSkip()).limit(options.getLimit()).maxTime(options.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).collation(options.getCollation()).retryReads(this.retryReads);
        if (query != null) {
            operation.filter(this.wrap(query));
        }
        if ((hint = options.getHint()) != null) {
            operation.hint(this.wrap(hint));
        } else {
            String hintString = options.getHintString();
            if (hintString != null) {
                operation.hint(new BsonString(hintString));
            }
        }
        ReadPreference optionsReadPreference = options.getReadPreference();
        ReadConcern optionsReadConcern = options.getReadConcern();
        return this.executor.execute(operation, optionsReadPreference != null ? optionsReadPreference : this.getReadPreference(), optionsReadConcern != null ? optionsReadConcern : this.getReadConcern());
    }

    public DBCollection rename(String newName) {
        return this.rename(newName, false);
    }

    public DBCollection rename(String newName, boolean dropTarget) {
        try {
            this.executor.execute(new RenameCollectionOperation(this.getNamespace(), new MongoNamespace(this.getNamespace().getDatabaseName(), newName), this.getWriteConcern()).dropTarget(dropTarget), this.getReadConcern());
            return this.getDB().getCollection(newName);
        } catch (MongoWriteConcernException e2) {
            throw DBCollection.createWriteConcernException(e2);
        }
    }

    @Deprecated
    public DBObject group(DBObject key, DBObject cond, DBObject initial, String reduce) {
        return this.group(key, cond, initial, reduce, null);
    }

    @Deprecated
    public DBObject group(DBObject key, DBObject cond, DBObject initial, String reduce, @Nullable String finalize) {
        return this.group(key, cond, initial, reduce, finalize, this.getReadPreference());
    }

    @Deprecated
    public DBObject group(DBObject key, DBObject cond, DBObject initial, String reduce, @Nullable String finalize, ReadPreference readPreference) {
        return this.group(new GroupCommand(this, key, cond, initial, reduce, finalize), readPreference);
    }

    @Deprecated
    public DBObject group(GroupCommand cmd) {
        return this.group(cmd, this.getReadPreference());
    }

    @Deprecated
    public DBObject group(GroupCommand cmd, ReadPreference readPreference) {
        return DBCollection.toDBList((BatchCursor)((Object)this.executor.execute(cmd.toOperation(this.getNamespace(), this.getDefaultDBObjectCodec(), this.retryReads), readPreference, this.getReadConcern())));
    }

    public List distinct(String fieldName) {
        return this.distinct(fieldName, this.getReadPreference());
    }

    public List distinct(String fieldName, ReadPreference readPreference) {
        return this.distinct(fieldName, new BasicDBObject(), readPreference);
    }

    public List distinct(String fieldName, DBObject query) {
        return this.distinct(fieldName, query, this.getReadPreference());
    }

    public List distinct(String fieldName, DBObject query, ReadPreference readPreference) {
        return this.distinct(fieldName, new DBCollectionDistinctOptions().filter(query).readPreference(readPreference));
    }

    public List distinct(final String fieldName, final DBCollectionDistinctOptions options) {
        Assertions.notNull("fieldName", fieldName);
        return new MongoIterableImpl<BsonValue>(null, this.executor, options.getReadConcern() != null ? options.getReadConcern() : this.getReadConcern(), options.getReadPreference() != null ? options.getReadPreference() : this.getReadPreference(), this.retryReads){

            @Override
            public ReadOperation<BatchCursor<BsonValue>> asReadOperation() {
                return new DistinctOperation<BsonValue>(DBCollection.this.getNamespace(), fieldName, new BsonValueCodec()).filter(DBCollection.this.wrapAllowNull(options.getFilter())).collation(options.getCollation()).retryReads(DBCollection.this.retryReads);
            }
        }.map(new Function<BsonValue, Object>(){

            @Override
            public Object apply(BsonValue bsonValue) {
                if (bsonValue == null) {
                    return null;
                }
                BsonDocument document = new BsonDocument("value", bsonValue);
                DBObject obj = (DBObject)DBCollection.this.getDefaultDBObjectCodec().decode(new BsonDocumentReader(document), DecoderContext.builder().build());
                return obj.get("value");
            }
        }).into(new ArrayList());
    }

    public MapReduceOutput mapReduce(String map, String reduce, String outputTarget, DBObject query) {
        MapReduceCommand command = new MapReduceCommand(this, map, reduce, outputTarget, MapReduceCommand.OutputType.REDUCE, query);
        return this.mapReduce(command);
    }

    public MapReduceOutput mapReduce(String map, String reduce, String outputTarget, MapReduceCommand.OutputType outputType, DBObject query) {
        MapReduceCommand command = new MapReduceCommand(this, map, reduce, outputTarget, outputType, query);
        return this.mapReduce(command);
    }

    public MapReduceOutput mapReduce(String map, String reduce, String outputTarget, MapReduceCommand.OutputType outputType, DBObject query, ReadPreference readPreference) {
        MapReduceCommand command = new MapReduceCommand(this, map, reduce, outputTarget, outputType, query);
        command.setReadPreference(readPreference);
        return this.mapReduce(command);
    }

    public MapReduceOutput mapReduce(MapReduceCommand command) {
        String action;
        ReadPreference readPreference = command.getReadPreference() == null ? this.getReadPreference() : command.getReadPreference();
        Map<String, Object> scope = command.getScope();
        Boolean jsMode = command.getJsMode();
        if (command.getOutputType() == MapReduceCommand.OutputType.INLINE) {
            MapReduceWithInlineResultsOperation<DBObject> operation = new MapReduceWithInlineResultsOperation<DBObject>(this.getNamespace(), new BsonJavaScript(command.getMap()), new BsonJavaScript(command.getReduce()), this.getDefaultDBObjectCodec()).filter(this.wrapAllowNull(command.getQuery())).limit(command.getLimit()).maxTime(command.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).jsMode(jsMode == null ? false : jsMode).sort(this.wrapAllowNull(command.getSort())).verbose(command.isVerbose()).collation(command.getCollation());
            if (scope != null) {
                operation.scope(this.wrap(new BasicDBObject(scope)));
            }
            if (command.getFinalize() != null) {
                operation.finalizeFunction(new BsonJavaScript(command.getFinalize()));
            }
            MapReduceBatchCursor executionResult = (MapReduceBatchCursor)((Object)this.executor.execute(operation, readPreference, this.getReadConcern()));
            return new MapReduceOutput(command.toDBObject(), executionResult);
        }
        switch (command.getOutputType()) {
            case REPLACE: {
                action = "replace";
                break;
            }
            case MERGE: {
                action = "merge";
                break;
            }
            case REDUCE: {
                action = "reduce";
                break;
            }
            default: {
                throw new IllegalArgumentException("Unexpected output type");
            }
        }
        MapReduceToCollectionOperation operation = new MapReduceToCollectionOperation(this.getNamespace(), new BsonJavaScript(command.getMap()), new BsonJavaScript(command.getReduce()), command.getOutputTarget(), this.getWriteConcern()).filter(this.wrapAllowNull(command.getQuery())).limit(command.getLimit()).maxTime(command.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).jsMode(jsMode == null ? false : jsMode).sort(this.wrapAllowNull(command.getSort())).verbose(command.isVerbose()).action(action).databaseName(command.getOutputDB()).bypassDocumentValidation(command.getBypassDocumentValidation()).collation(command.getCollation());
        if (scope != null) {
            operation.scope(this.wrap(new BasicDBObject(scope)));
        }
        if (command.getFinalize() != null) {
            operation.finalizeFunction(new BsonJavaScript(command.getFinalize()));
        }
        try {
            MapReduceStatistics mapReduceStatistics = this.executor.execute(operation, this.getReadConcern());
            DBCollection mapReduceOutputCollection = this.getMapReduceOutputCollection(command);
            DBCursor executionResult = mapReduceOutputCollection.find();
            return new MapReduceOutput(command.toDBObject(), executionResult, mapReduceStatistics, mapReduceOutputCollection);
        } catch (MongoWriteConcernException e2) {
            throw DBCollection.createWriteConcernException(e2);
        }
    }

    private DBCollection getMapReduceOutputCollection(MapReduceCommand command) {
        String requestedDatabaseName = command.getOutputDB();
        DB database = requestedDatabaseName != null ? this.getDB().getSisterDB(requestedDatabaseName) : this.getDB();
        return database.getCollection(command.getOutputTargetNonNull());
    }

    @Deprecated
    public AggregationOutput aggregate(DBObject firstOp, DBObject ... additionalOps) {
        ArrayList<DBObject> pipeline = new ArrayList<DBObject>();
        pipeline.add(firstOp);
        Collections.addAll(pipeline, additionalOps);
        return this.aggregate(pipeline);
    }

    @Deprecated
    public AggregationOutput aggregate(List<? extends DBObject> pipeline) {
        return this.aggregate(pipeline, this.getReadPreference());
    }

    @Deprecated
    public AggregationOutput aggregate(List<? extends DBObject> pipeline, ReadPreference readPreference) {
        Cursor cursor = this.aggregate(pipeline, AggregationOptions.builder().build(), readPreference, false);
        if (cursor == null) {
            return new AggregationOutput(Collections.<DBObject>emptyList());
        }
        ArrayList<DBObject> results = new ArrayList<DBObject>();
        while (cursor.hasNext()) {
            results.add((DBObject)cursor.next());
        }
        return new AggregationOutput(results);
    }

    public Cursor aggregate(List<? extends DBObject> pipeline, AggregationOptions options) {
        return this.aggregate(pipeline, options, this.getReadPreference());
    }

    public Cursor aggregate(List<? extends DBObject> pipeline, AggregationOptions options, ReadPreference readPreference) {
        Cursor cursor = this.aggregate(pipeline, options, readPreference, true);
        if (cursor == null) {
            throw new MongoInternalException("cursor can not be null in this context");
        }
        return cursor;
    }

    @Nullable
    private Cursor aggregate(List<? extends DBObject> pipeline, AggregationOptions options, ReadPreference readPreference, boolean returnCursorForOutCollection) {
        Assertions.notNull("options", options);
        List<BsonDocument> stages = this.preparePipeline(pipeline);
        BsonValue outCollection = stages.get(stages.size() - 1).get("$out");
        if (outCollection != null) {
            AggregateToCollectionOperation operation = new AggregateToCollectionOperation(this.getNamespace(), stages, this.getReadConcern(), this.getWriteConcern()).maxTime(options.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).allowDiskUse(options.getAllowDiskUse()).bypassDocumentValidation(options.getBypassDocumentValidation()).collation(options.getCollation());
            try {
                this.executor.execute(operation, this.getReadConcern());
                if (returnCursorForOutCollection) {
                    return new DBCursor(this.database.getCollection(outCollection.asString().getValue()), new BasicDBObject(), new DBCollectionFindOptions().readPreference(ReadPreference.primary()).collation(options.getCollation()));
                }
                return null;
            } catch (MongoWriteConcernException e2) {
                throw DBCollection.createWriteConcernException(e2);
            }
        }
        AggregateOperation<DBObject> operation = new AggregateOperation<DBObject>(this.getNamespace(), stages, this.getDefaultDBObjectCodec()).maxTime(options.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).allowDiskUse(options.getAllowDiskUse()).batchSize(options.getBatchSize()).useCursor(options.getOutputMode() == AggregationOptions.OutputMode.CURSOR).collation(options.getCollation()).retryReads(this.retryReads);
        BatchCursor cursor = (BatchCursor)((Object)this.executor.execute(operation, readPreference, this.getReadConcern()));
        return new MongoCursorAdapter(new MongoBatchCursorAdapter<DBObject>(cursor));
    }

    public CommandResult explainAggregate(List<? extends DBObject> pipeline, AggregationOptions options) {
        AggregateOperation<BsonDocument> operation = new AggregateOperation<BsonDocument>(this.getNamespace(), this.preparePipeline(pipeline), new BsonDocumentCodec()).maxTime(options.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).allowDiskUse(options.getAllowDiskUse()).collation(options.getCollation()).retryReads(this.retryReads);
        return new CommandResult(this.executor.execute(operation.asExplainableOperation(ExplainVerbosity.QUERY_PLANNER), ReadPreference.primaryPreferred(), this.getReadConcern()));
    }

    List<BsonDocument> preparePipeline(List<? extends DBObject> pipeline) {
        ArrayList<BsonDocument> stages = new ArrayList<BsonDocument>();
        for (DBObject dBObject : pipeline) {
            stages.add(this.wrap(dBObject));
        }
        return stages;
    }

    @Deprecated
    public List<Cursor> parallelScan(ParallelScanOptions options) {
        ArrayList<Cursor> cursors = new ArrayList<Cursor>();
        ParallelCollectionScanOperation<DBObject> operation = new ParallelCollectionScanOperation<DBObject>(this.getNamespace(), options.getNumCursors(), this.objectCodec).batchSize(options.getBatchSize()).retryReads(this.retryReads);
        ReadPreference readPreferenceFromOptions = options.getReadPreference();
        List mongoCursors = (List)((Object)this.executor.execute(operation, readPreferenceFromOptions != null ? readPreferenceFromOptions : this.getReadPreference(), this.getReadConcern()));
        for (BatchCursor mongoCursor : mongoCursors) {
            cursors.add(new MongoCursorAdapter(new MongoBatchCursorAdapter<DBObject>(mongoCursor)));
        }
        return cursors;
    }

    public String getName() {
        return this.name;
    }

    public String getFullName() {
        return this.getNamespace().getFullName();
    }

    public DBCollection getCollection(String name) {
        return this.database.getCollection(this.getName() + "." + name);
    }

    public void createIndex(String name) {
        this.createIndex(new BasicDBObject(name, (Object)1));
    }

    public void createIndex(DBObject keys, String name) {
        this.createIndex(keys, name, false);
    }

    public void createIndex(DBObject keys, @Nullable String name, boolean unique) {
        BasicDBObject options = new BasicDBObject();
        if (name != null && name.length() > 0) {
            options.put("name", name);
        }
        if (unique) {
            options.put("unique", Boolean.TRUE);
        }
        this.createIndex(keys, options);
    }

    public void createIndex(DBObject keys) {
        this.createIndex(keys, new BasicDBObject());
    }

    public void createIndex(DBObject keys, DBObject options) {
        try {
            this.executor.execute(this.createIndexOperation(keys, options), this.getReadConcern());
        } catch (MongoWriteConcernException e2) {
            throw DBCollection.createWriteConcernException(e2);
        }
    }

    @Deprecated
    @Nullable
    public List<DBObject> getHintFields() {
        return this.hintFields;
    }

    @Deprecated
    public void setHintFields(List<? extends DBObject> indexes) {
        this.hintFields = new ArrayList<DBObject>(indexes);
    }

    @Nullable
    public DBObject findAndModify(@Nullable DBObject query, @Nullable DBObject sort, DBObject update) {
        return this.findAndModify(query, null, sort, false, update, false, false);
    }

    @Nullable
    public DBObject findAndModify(@Nullable DBObject query, DBObject update) {
        return this.findAndModify(query, null, null, false, update, false, false);
    }

    @Nullable
    public DBObject findAndRemove(@Nullable DBObject query) {
        return this.findAndModify(query, null, null, true, null, false, false);
    }

    @Nullable
    public DBObject findAndModify(@Nullable DBObject query, @Nullable DBObject fields, @Nullable DBObject sort, boolean remove, @Nullable DBObject update, boolean returnNew, boolean upsert) {
        return this.findAndModify(query, fields, sort, remove, update, returnNew, upsert, 0L, TimeUnit.MILLISECONDS);
    }

    @Nullable
    public DBObject findAndModify(@Nullable DBObject query, @Nullable DBObject fields, @Nullable DBObject sort, boolean remove, DBObject update, boolean returnNew, boolean upsert, WriteConcern writeConcern) {
        return this.findAndModify(query, fields, sort, remove, update, returnNew, upsert, 0L, TimeUnit.MILLISECONDS, writeConcern);
    }

    @Nullable
    public DBObject findAndModify(@Nullable DBObject query, @Nullable DBObject fields, @Nullable DBObject sort, boolean remove, @Nullable DBObject update, boolean returnNew, boolean upsert, long maxTime, TimeUnit maxTimeUnit) {
        return this.findAndModify(query, fields, sort, remove, update, returnNew, upsert, maxTime, maxTimeUnit, this.getWriteConcern());
    }

    @Nullable
    public DBObject findAndModify(@Nullable DBObject query, @Nullable DBObject fields, @Nullable DBObject sort, boolean remove, @Nullable DBObject update, boolean returnNew, boolean upsert, long maxTime, TimeUnit maxTimeUnit, WriteConcern writeConcern) {
        return this.findAndModify(query != null ? query : new BasicDBObject(), new DBCollectionFindAndModifyOptions().projection(fields).sort(sort).remove(remove).update(update).returnNew(returnNew).upsert(upsert).maxTime(maxTime, maxTimeUnit).writeConcern(writeConcern));
    }

    @Nullable
    public DBObject findAndModify(DBObject query, DBObject fields, DBObject sort, boolean remove, @Nullable DBObject update, boolean returnNew, boolean upsert, boolean bypassDocumentValidation, long maxTime, TimeUnit maxTimeUnit) {
        return this.findAndModify(query, fields, sort, remove, update, returnNew, upsert, bypassDocumentValidation, maxTime, maxTimeUnit, this.getWriteConcern());
    }

    public DBObject findAndModify(@Nullable DBObject query, @Nullable DBObject fields, @Nullable DBObject sort, boolean remove, @Nullable DBObject update, boolean returnNew, boolean upsert, boolean bypassDocumentValidation, long maxTime, TimeUnit maxTimeUnit, WriteConcern writeConcern) {
        return this.findAndModify(query != null ? query : new BasicDBObject(), new DBCollectionFindAndModifyOptions().projection(fields).sort(sort).remove(remove).update(update).returnNew(returnNew).upsert(upsert).bypassDocumentValidation(bypassDocumentValidation).maxTime(maxTime, maxTimeUnit).writeConcern(writeConcern));
    }

    public DBObject findAndModify(DBObject query, DBCollectionFindAndModifyOptions options) {
        BaseFindAndModifyOperation operation;
        WriteConcern writeConcern;
        Assertions.notNull("query", query);
        Assertions.notNull("options", options);
        WriteConcern optionsWriteConcern = options.getWriteConcern();
        WriteConcern writeConcern2 = writeConcern = optionsWriteConcern != null ? optionsWriteConcern : this.getWriteConcern();
        if (options.isRemove()) {
            operation = new FindAndDeleteOperation<DBObject>(this.getNamespace(), writeConcern, this.retryWrites, this.objectCodec).filter(this.wrapAllowNull(query)).projection(this.wrapAllowNull(options.getProjection())).sort(this.wrapAllowNull(options.getSort())).maxTime(options.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).collation(options.getCollation());
        } else {
            DBObject update = options.getUpdate();
            if (update == null) {
                throw new IllegalArgumentException("update can not be null unless it's a remove");
            }
            operation = !update.keySet().isEmpty() && update.keySet().iterator().next().charAt(0) == '$' ? new FindAndUpdateOperation<DBObject>(this.getNamespace(), writeConcern, this.retryWrites, this.objectCodec, this.wrap(update)).filter(this.wrap(query)).projection(this.wrapAllowNull(options.getProjection())).sort(this.wrapAllowNull(options.getSort())).returnOriginal(!options.returnNew()).upsert(options.isUpsert()).maxTime(options.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).bypassDocumentValidation(options.getBypassDocumentValidation()).collation(options.getCollation()).arrayFilters(this.wrapAllowNull(options.getArrayFilters(), (Encoder<DBObject>)null)) : new FindAndReplaceOperation<DBObject>(this.getNamespace(), writeConcern, this.retryWrites, this.objectCodec, this.wrap(update)).filter(this.wrap(query)).projection(this.wrapAllowNull(options.getProjection())).sort(this.wrapAllowNull(options.getSort())).returnOriginal(!options.returnNew()).upsert(options.isUpsert()).maxTime(options.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).bypassDocumentValidation(options.getBypassDocumentValidation()).collation(options.getCollation());
        }
        try {
            return this.executor.execute(operation, this.getReadConcern());
        } catch (MongoWriteConcernException e2) {
            throw DBCollection.createWriteConcernException(e2);
        }
    }

    public DB getDB() {
        return this.database;
    }

    public WriteConcern getWriteConcern() {
        if (this.writeConcern != null) {
            return this.writeConcern;
        }
        return this.database.getWriteConcern();
    }

    public void setWriteConcern(WriteConcern writeConcern) {
        this.writeConcern = writeConcern;
    }

    public ReadPreference getReadPreference() {
        if (this.readPreference != null) {
            return this.readPreference;
        }
        return this.database.getReadPreference();
    }

    public void setReadPreference(ReadPreference preference) {
        this.readPreference = preference;
    }

    public void setReadConcern(ReadConcern readConcern) {
        this.readConcern = readConcern;
    }

    public ReadConcern getReadConcern() {
        if (this.readConcern != null) {
            return this.readConcern;
        }
        return this.database.getReadConcern();
    }

    @Deprecated
    public void slaveOk() {
        this.addOption(4);
    }

    @Deprecated
    public void addOption(int option) {
        this.optionHolder.add(option);
    }

    @Deprecated
    public void resetOptions() {
        this.optionHolder.reset();
    }

    @Deprecated
    public int getOptions() {
        return this.optionHolder.get();
    }

    @Deprecated
    public void setOptions(int options) {
        this.optionHolder.set(options);
    }

    public void drop() {
        try {
            this.executor.execute(new DropCollectionOperation(this.getNamespace(), this.getWriteConcern()), this.getReadConcern());
        } catch (MongoWriteConcernException e2) {
            throw DBCollection.createWriteConcernException(e2);
        }
    }

    public synchronized DBDecoderFactory getDBDecoderFactory() {
        return this.decoderFactory;
    }

    public synchronized void setDBDecoderFactory(@Nullable DBDecoderFactory factory) {
        this.decoderFactory = factory;
        Codec<DBObject> decoder = factory == null || factory == DefaultDBDecoder.FACTORY ? this.getDefaultDBObjectCodec() : new DBDecoderAdapter(factory.create(), this, this.getBufferPool());
        this.objectCodec = new CompoundDBObjectCodec(this.objectCodec.getEncoder(), decoder);
    }

    public synchronized DBEncoderFactory getDBEncoderFactory() {
        return this.encoderFactory;
    }

    public synchronized void setDBEncoderFactory(@Nullable DBEncoderFactory factory) {
        this.encoderFactory = factory;
        Codec<DBObject> encoder = factory == null || factory == DefaultDBEncoder.FACTORY ? this.getDefaultDBObjectCodec() : new DBEncoderFactoryAdapter(this.encoderFactory);
        this.objectCodec = new CompoundDBObjectCodec(encoder, this.objectCodec.getDecoder());
    }

    public List<DBObject> getIndexInfo() {
        return new MongoIterableImpl<DBObject>(null, this.executor, ReadConcern.DEFAULT, ReadPreference.primary(), this.retryReads){

            @Override
            public ReadOperation<BatchCursor<DBObject>> asReadOperation() {
                return new ListIndexesOperation<DBObject>(DBCollection.this.getNamespace(), DBCollection.this.getDefaultDBObjectCodec()).retryReads(DBCollection.this.retryReads);
            }
        }.into(new ArrayList());
    }

    public void dropIndex(DBObject index) {
        try {
            this.executor.execute(new DropIndexOperation(this.getNamespace(), this.wrap(index), this.getWriteConcern()), this.getReadConcern());
        } catch (MongoWriteConcernException e2) {
            throw DBCollection.createWriteConcernException(e2);
        }
    }

    public void dropIndex(String indexName) {
        try {
            this.executor.execute(new DropIndexOperation(this.getNamespace(), indexName, this.getWriteConcern()), this.getReadConcern());
        } catch (MongoWriteConcernException e2) {
            throw DBCollection.createWriteConcernException(e2);
        }
    }

    public void dropIndexes() {
        this.dropIndex("*");
    }

    public void dropIndexes(String indexName) {
        this.dropIndex(indexName);
    }

    public CommandResult getStats() {
        return this.getDB().executeCommand(new BsonDocument("collStats", new BsonString(this.getName())), this.getReadPreference());
    }

    public boolean isCapped() {
        CommandResult commandResult = this.getStats();
        Object cappedField = commandResult.get("capped");
        return cappedField != null && (cappedField.equals(1) || cappedField.equals(true));
    }

    public Class getObjectClass() {
        return this.objectFactory.getClassForPath(Collections.<String>emptyList());
    }

    public void setObjectClass(Class<? extends DBObject> aClass) {
        this.setObjectFactory(this.objectFactory.update(aClass));
    }

    public void setInternalClass(String path, Class<? extends DBObject> aClass) {
        this.setObjectFactory(this.objectFactory.update(aClass, Arrays.asList(path.split("\\."))));
    }

    protected Class<? extends DBObject> getInternalClass(String path) {
        return this.objectFactory.getClassForPath(Arrays.asList(path.split("\\.")));
    }

    public String toString() {
        return "DBCollection{database=" + this.database + ", name='" + this.name + '\'' + '}';
    }

    synchronized DBObjectFactory getObjectFactory() {
        return this.objectFactory;
    }

    synchronized void setObjectFactory(DBCollectionObjectFactory factory) {
        this.objectFactory = factory;
        this.objectCodec = new CompoundDBObjectCodec(this.objectCodec.getEncoder(), this.getDefaultDBObjectCodec());
    }

    public BulkWriteOperation initializeOrderedBulkOperation() {
        return new BulkWriteOperation(true, this);
    }

    public BulkWriteOperation initializeUnorderedBulkOperation() {
        return new BulkWriteOperation(false, this);
    }

    BulkWriteResult executeBulkWriteOperation(boolean ordered, Boolean bypassDocumentValidation, List<com.mongodb.WriteRequest> writeRequests) {
        return this.executeBulkWriteOperation(ordered, bypassDocumentValidation, writeRequests, this.getWriteConcern());
    }

    BulkWriteResult executeBulkWriteOperation(boolean ordered, Boolean bypassDocumentValidation, List<com.mongodb.WriteRequest> writeRequests, WriteConcern writeConcern) {
        try {
            return BulkWriteHelper.translateBulkWriteResult(this.executor.execute(new MixedBulkWriteOperation(this.getNamespace(), this.translateWriteRequestsToNew(writeRequests), ordered, writeConcern, false).bypassDocumentValidation(bypassDocumentValidation), this.getReadConcern()), this.getObjectCodec());
        } catch (MongoBulkWriteException e2) {
            throw BulkWriteHelper.translateBulkWriteException(e2, MongoClient.getDefaultCodecRegistry().get(DBObject.class));
        }
    }

    private List<WriteRequest> translateWriteRequestsToNew(List<com.mongodb.WriteRequest> writeRequests) {
        ArrayList<WriteRequest> retVal = new ArrayList<WriteRequest>(writeRequests.size());
        for (com.mongodb.WriteRequest cur : writeRequests) {
            retVal.add(cur.toNew(this));
        }
        return retVal;
    }

    Codec<DBObject> getDefaultDBObjectCodec() {
        return new DBObjectCodec(this.getDB().getMongoClient().getCodecRegistry(), DBObjectCodec.getDefaultBsonTypeClassMap(), this.getObjectFactory()).withUuidRepresentation(this.getDB().getMongoClient().getMongoClientOptions().getUuidRepresentation());
    }

    private <T> T convertOptionsToType(DBObject options, String field, Class<T> clazz) {
        return this.convertToType(clazz, options.get(field), String.format("'%s' should be of class %s", field, clazz.getSimpleName()));
    }

    private <T> T convertToType(Class<T> clazz, Object value, String errorMessage) {
        Object transformedValue = value;
        if (clazz == Boolean.class) {
            if (value instanceof Boolean) {
                transformedValue = value;
            } else if (value instanceof Number) {
                transformedValue = ((Number)value).doubleValue() != 0.0;
            }
        } else if (clazz == Double.class) {
            if (value instanceof Number) {
                transformedValue = ((Number)value).doubleValue();
            }
        } else if (clazz == Integer.class) {
            if (value instanceof Number) {
                transformedValue = ((Number)value).intValue();
            }
        } else if (clazz == Long.class && value instanceof Number) {
            transformedValue = ((Number)value).longValue();
        }
        if (!clazz.isAssignableFrom(transformedValue.getClass())) {
            throw new IllegalArgumentException(errorMessage);
        }
        return (T)transformedValue;
    }

    private CreateIndexesOperation createIndexOperation(DBObject key, DBObject options) {
        IndexRequest request = new IndexRequest(this.wrap(key));
        if (options.containsField("name")) {
            request.name(this.convertOptionsToType(options, "name", String.class));
        }
        if (options.containsField("background")) {
            request.background(this.convertOptionsToType(options, "background", Boolean.class));
        }
        if (options.containsField("unique")) {
            request.unique(this.convertOptionsToType(options, "unique", Boolean.class));
        }
        if (options.containsField("sparse")) {
            request.sparse(this.convertOptionsToType(options, "sparse", Boolean.class));
        }
        if (options.containsField("expireAfterSeconds")) {
            request.expireAfter(this.convertOptionsToType(options, "expireAfterSeconds", Long.class), TimeUnit.SECONDS);
        }
        if (options.containsField("v")) {
            request.version(this.convertOptionsToType(options, "v", Integer.class));
        }
        if (options.containsField("weights")) {
            request.weights(this.wrap(this.convertOptionsToType(options, "weights", DBObject.class)));
        }
        if (options.containsField("default_language")) {
            request.defaultLanguage(this.convertOptionsToType(options, "default_language", String.class));
        }
        if (options.containsField("language_override")) {
            request.languageOverride(this.convertOptionsToType(options, "language_override", String.class));
        }
        if (options.containsField("textIndexVersion")) {
            request.textVersion(this.convertOptionsToType(options, "textIndexVersion", Integer.class));
        }
        if (options.containsField("2dsphereIndexVersion")) {
            request.sphereVersion(this.convertOptionsToType(options, "2dsphereIndexVersion", Integer.class));
        }
        if (options.containsField("bits")) {
            request.bits(this.convertOptionsToType(options, "bits", Integer.class));
        }
        if (options.containsField("min")) {
            request.min(this.convertOptionsToType(options, "min", Double.class));
        }
        if (options.containsField("max")) {
            request.max(this.convertOptionsToType(options, "max", Double.class));
        }
        if (options.containsField("bucketSize")) {
            request.bucketSize(this.convertOptionsToType(options, "bucketSize", Double.class));
        }
        if (options.containsField("dropDups")) {
            request.dropDups(this.convertOptionsToType(options, "dropDups", Boolean.class));
        }
        if (options.containsField("storageEngine")) {
            request.storageEngine(this.wrap(this.convertOptionsToType(options, "storageEngine", DBObject.class)));
        }
        if (options.containsField("partialFilterExpression")) {
            request.partialFilterExpression(this.wrap(this.convertOptionsToType(options, "partialFilterExpression", DBObject.class)));
        }
        if (options.containsField("collation")) {
            request.collation(DBObjectCollationHelper.createCollationFromOptions(options));
        }
        return new CreateIndexesOperation(this.getNamespace(), Collections.singletonList(request), this.writeConcern);
    }

    Codec<DBObject> getObjectCodec() {
        return this.objectCodec;
    }

    OperationExecutor getExecutor() {
        return this.executor;
    }

    MongoNamespace getNamespace() {
        return new MongoNamespace(this.getDB().getName(), this.getName());
    }

    BufferProvider getBufferPool() {
        return this.getDB().getBufferPool();
    }

    @Nullable
    BsonDocument wrapAllowNull(@Nullable DBObject document) {
        if (document == null) {
            return null;
        }
        return this.wrap(document);
    }

    @Nullable
    List<BsonDocument> wrapAllowNull(@Nullable List<? extends DBObject> documentList, @Nullable DBEncoder encoder) {
        return this.wrapAllowNull(documentList, encoder == null ? null : new DBEncoderAdapter(encoder));
    }

    @Nullable
    List<BsonDocument> wrapAllowNull(@Nullable List<? extends DBObject> documentList, @Nullable Encoder<DBObject> encoder) {
        if (documentList == null) {
            return null;
        }
        ArrayList<BsonDocument> wrappedDocumentList = new ArrayList<BsonDocument>(documentList.size());
        for (DBObject dBObject : documentList) {
            wrappedDocumentList.add(encoder == null ? this.wrap(dBObject) : this.wrap(dBObject, encoder));
        }
        return wrappedDocumentList;
    }

    BsonDocument wrap(DBObject document) {
        return new BsonDocumentWrapper<DBObject>(document, this.getDefaultDBObjectCodec());
    }

    BsonDocument wrap(DBObject document, @Nullable DBEncoder encoder) {
        if (encoder == null) {
            return this.wrap(document);
        }
        return new BsonDocumentWrapper<DBObject>(document, new DBEncoderAdapter(encoder));
    }

    BsonDocument wrap(DBObject document, @Nullable Encoder<DBObject> encoder) {
        if (encoder == null) {
            return this.wrap(document);
        }
        return new BsonDocumentWrapper<DBObject>(document, encoder);
    }

    static WriteConcernException createWriteConcernException(MongoWriteConcernException e2) {
        return new WriteConcernException(new BsonDocument("code", new BsonInt32(e2.getWriteConcernError().getCode())).append("errmsg", new BsonString(e2.getWriteConcernError().getMessage())), e2.getServerAddress(), e2.getWriteResult());
    }
}

