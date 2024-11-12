/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.Bytes;
import com.mongodb.CommandResult;
import com.mongodb.DBCollection;
import com.mongodb.DBEncoder;
import com.mongodb.DBEncoderAdapter;
import com.mongodb.DBObject;
import com.mongodb.DBObjectCollationHelper;
import com.mongodb.DefaultDBDecoder;
import com.mongodb.DefaultDBEncoder;
import com.mongodb.Function;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCommandException;
import com.mongodb.MongoCredential;
import com.mongodb.MongoNamespace;
import com.mongodb.MongoWriteConcernException;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import com.mongodb.annotations.ThreadSafe;
import com.mongodb.assertions.Assertions;
import com.mongodb.client.internal.MongoIterableImpl;
import com.mongodb.client.internal.OperationExecutor;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.DBCreateViewOptions;
import com.mongodb.client.model.ValidationAction;
import com.mongodb.client.model.ValidationLevel;
import com.mongodb.connection.BufferProvider;
import com.mongodb.lang.Nullable;
import com.mongodb.operation.BatchCursor;
import com.mongodb.operation.CommandReadOperation;
import com.mongodb.operation.CommandWriteOperation;
import com.mongodb.operation.CreateCollectionOperation;
import com.mongodb.operation.CreateUserOperation;
import com.mongodb.operation.CreateViewOperation;
import com.mongodb.operation.DropDatabaseOperation;
import com.mongodb.operation.DropUserOperation;
import com.mongodb.operation.ListCollectionsOperation;
import com.mongodb.operation.ReadOperation;
import com.mongodb.operation.UpdateUserOperation;
import com.mongodb.operation.UserExistsOperation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.bson.BsonDocument;
import org.bson.BsonDocumentWrapper;
import org.bson.BsonInt32;
import org.bson.codecs.BsonDocumentCodec;
import org.bson.codecs.Codec;

@ThreadSafe
public class DB {
    private final Mongo mongo;
    private final String name;
    private final OperationExecutor executor;
    private final ConcurrentHashMap<String, DBCollection> collectionCache;
    private final Bytes.OptionHolder optionHolder;
    private final Codec<DBObject> commandCodec;
    private volatile ReadPreference readPreference;
    private volatile WriteConcern writeConcern;
    private volatile ReadConcern readConcern;
    private static final Set<String> OBEDIENT_COMMANDS = new HashSet<String>();

    DB(Mongo mongo, String name, OperationExecutor executor) {
        MongoNamespace.checkDatabaseNameValidity(name);
        this.mongo = mongo;
        this.name = name;
        this.executor = executor;
        this.collectionCache = new ConcurrentHashMap();
        this.optionHolder = new Bytes.OptionHolder(mongo.getOptionHolder());
        this.commandCodec = MongoClient.getCommandCodec();
    }

    @Deprecated
    public DB(Mongo mongo, String name) {
        this(mongo, name, mongo.createOperationExecutor());
    }

    @Deprecated
    public Mongo getMongo() {
        return this.mongo;
    }

    public MongoClient getMongoClient() {
        if (this.mongo instanceof MongoClient) {
            return (MongoClient)this.mongo;
        }
        throw new IllegalStateException("This DB was not created from a MongoClient.  Use getMongo instead");
    }

    public void setReadPreference(ReadPreference readPreference) {
        this.readPreference = readPreference;
    }

    public void setWriteConcern(WriteConcern writeConcern) {
        this.writeConcern = writeConcern;
    }

    public ReadPreference getReadPreference() {
        return this.readPreference != null ? this.readPreference : this.mongo.getReadPreference();
    }

    public WriteConcern getWriteConcern() {
        return this.writeConcern != null ? this.writeConcern : this.mongo.getWriteConcern();
    }

    public void setReadConcern(ReadConcern readConcern) {
        this.readConcern = readConcern;
    }

    public ReadConcern getReadConcern() {
        return this.readConcern != null ? this.readConcern : this.mongo.getReadConcern();
    }

    @Deprecated
    protected DBCollection doGetCollection(String name) {
        return this.getCollection(name);
    }

    public DBCollection getCollection(String name) {
        DBCollection old;
        DBCollection collection = this.collectionCache.get(name);
        if (collection != null) {
            return collection;
        }
        collection = new DBCollection(name, this, this.executor);
        if (this.mongo.getMongoClientOptions().getDbDecoderFactory() != DefaultDBDecoder.FACTORY) {
            collection.setDBDecoderFactory(this.mongo.getMongoClientOptions().getDbDecoderFactory());
        }
        if (this.mongo.getMongoClientOptions().getDbEncoderFactory() != DefaultDBEncoder.FACTORY) {
            collection.setDBEncoderFactory(this.mongo.getMongoClientOptions().getDbEncoderFactory());
        }
        return (old = this.collectionCache.putIfAbsent(name, collection)) != null ? old : collection;
    }

    public void dropDatabase() {
        try {
            this.getExecutor().execute(new DropDatabaseOperation(this.getName(), this.getWriteConcern()), this.getReadConcern());
        } catch (MongoWriteConcernException e2) {
            throw DBCollection.createWriteConcernException(e2);
        }
    }

    @Deprecated
    public DBCollection getCollectionFromString(String collectionName) {
        return this.getCollection(collectionName);
    }

    public String getName() {
        return this.name;
    }

    public Set<String> getCollectionNames() {
        List collectionNames = new MongoIterableImpl<DBObject>(null, this.executor, ReadConcern.DEFAULT, ReadPreference.primary(), this.mongo.getMongoClientOptions().getRetryReads()){

            @Override
            public ReadOperation<BatchCursor<DBObject>> asReadOperation() {
                return new ListCollectionsOperation(DB.this.name, DB.this.commandCodec).nameOnly(true);
            }
        }.map(new Function<DBObject, String>(){

            @Override
            public String apply(DBObject result) {
                return (String)result.get("name");
            }
        }).into(new ArrayList());
        Collections.sort(collectionNames);
        return new LinkedHashSet<String>(collectionNames);
    }

    public DBCollection createCollection(String collectionName, @Nullable DBObject options) {
        if (options != null) {
            try {
                this.executor.execute(this.getCreateCollectionOperation(collectionName, options), this.getReadConcern());
            } catch (MongoWriteConcernException e2) {
                throw DBCollection.createWriteConcernException(e2);
            }
        }
        return this.getCollection(collectionName);
    }

    public DBCollection createView(String viewName, String viewOn, List<? extends DBObject> pipeline) {
        return this.createView(viewName, viewOn, pipeline, new DBCreateViewOptions());
    }

    public DBCollection createView(String viewName, String viewOn, List<? extends DBObject> pipeline, DBCreateViewOptions options) {
        try {
            Assertions.notNull("options", options);
            DBCollection view = this.getCollection(viewName);
            this.executor.execute(new CreateViewOperation(this.name, viewName, viewOn, view.preparePipeline(pipeline), this.writeConcern).collation(options.getCollation()), this.getReadConcern());
            return view;
        } catch (MongoWriteConcernException e2) {
            throw DBCollection.createWriteConcernException(e2);
        }
    }

    private CreateCollectionOperation getCreateCollectionOperation(String collectionName, DBObject options) {
        if (options.get("size") != null && !(options.get("size") instanceof Number)) {
            throw new IllegalArgumentException("'size' should be Number");
        }
        if (options.get("max") != null && !(options.get("max") instanceof Number)) {
            throw new IllegalArgumentException("'max' should be Number");
        }
        if (options.get("capped") != null && !(options.get("capped") instanceof Boolean)) {
            throw new IllegalArgumentException("'capped' should be Boolean");
        }
        if (options.get("autoIndexId") != null && !(options.get("autoIndexId") instanceof Boolean)) {
            throw new IllegalArgumentException("'autoIndexId' should be Boolean");
        }
        if (options.get("storageEngine") != null && !(options.get("storageEngine") instanceof DBObject)) {
            throw new IllegalArgumentException("'storageEngine' should be DBObject");
        }
        if (options.get("indexOptionDefaults") != null && !(options.get("indexOptionDefaults") instanceof DBObject)) {
            throw new IllegalArgumentException("'indexOptionDefaults' should be DBObject");
        }
        if (options.get("validator") != null && !(options.get("validator") instanceof DBObject)) {
            throw new IllegalArgumentException("'validator' should be DBObject");
        }
        if (options.get("validationLevel") != null && !(options.get("validationLevel") instanceof String)) {
            throw new IllegalArgumentException("'validationLevel' should be String");
        }
        if (options.get("validationAction") != null && !(options.get("validationAction") instanceof String)) {
            throw new IllegalArgumentException("'validationAction' should be String");
        }
        boolean capped = false;
        boolean autoIndex = true;
        long sizeInBytes = 0L;
        long maxDocuments = 0L;
        Boolean usePowerOfTwoSizes = null;
        BsonDocument storageEngineOptions = null;
        BsonDocument indexOptionDefaults = null;
        BsonDocument validator = null;
        ValidationLevel validationLevel = null;
        ValidationAction validationAction = null;
        if (options.get("capped") != null) {
            capped = (Boolean)options.get("capped");
        }
        if (options.get("size") != null) {
            sizeInBytes = ((Number)options.get("size")).longValue();
        }
        if (options.get("autoIndexId") != null) {
            autoIndex = (Boolean)options.get("autoIndexId");
        }
        if (options.get("max") != null) {
            maxDocuments = ((Number)options.get("max")).longValue();
        }
        if (options.get("usePowerOfTwoSizes") != null) {
            usePowerOfTwoSizes = (Boolean)options.get("usePowerOfTwoSizes");
        }
        if (options.get("storageEngine") != null) {
            storageEngineOptions = this.wrap((DBObject)options.get("storageEngine"));
        }
        if (options.get("indexOptionDefaults") != null) {
            indexOptionDefaults = this.wrap((DBObject)options.get("indexOptionDefaults"));
        }
        if (options.get("validator") != null) {
            validator = this.wrap((DBObject)options.get("validator"));
        }
        if (options.get("validationLevel") != null) {
            validationLevel = ValidationLevel.fromString((String)options.get("validationLevel"));
        }
        if (options.get("validationAction") != null) {
            validationAction = ValidationAction.fromString((String)options.get("validationAction"));
        }
        Collation collation = DBObjectCollationHelper.createCollationFromOptions(options);
        return new CreateCollectionOperation(this.getName(), collectionName, this.getWriteConcern()).capped(capped).collation(collation).sizeInBytes(sizeInBytes).autoIndex(autoIndex).maxDocuments(maxDocuments).usePowerOf2Sizes(usePowerOfTwoSizes).storageEngineOptions(storageEngineOptions).indexOptionDefaults(indexOptionDefaults).validator(validator).validationLevel(validationLevel).validationAction(validationAction);
    }

    public CommandResult command(String command) {
        return this.command((DBObject)new BasicDBObject(command, Boolean.TRUE), this.getReadPreference());
    }

    public CommandResult command(DBObject command) {
        return this.command(command, this.getReadPreference());
    }

    public CommandResult command(DBObject command, DBEncoder encoder) {
        return this.command(command, this.getReadPreference(), encoder);
    }

    public CommandResult command(DBObject command, ReadPreference readPreference, @Nullable DBEncoder encoder) {
        try {
            return this.executeCommand(this.wrap(command, encoder), this.getCommandReadPreference(command, readPreference));
        } catch (MongoCommandException ex) {
            return new CommandResult(ex.getResponse(), ex.getServerAddress());
        }
    }

    public CommandResult command(DBObject command, ReadPreference readPreference) {
        return this.command(command, readPreference, null);
    }

    public CommandResult command(String command, ReadPreference readPreference) {
        return this.command((DBObject)new BasicDBObject(command, true), readPreference);
    }

    public DB getSisterDB(String name) {
        return this.mongo.getDB(name);
    }

    public boolean collectionExists(String collectionName) {
        Set<String> collectionNames = this.getCollectionNames();
        for (String name : collectionNames) {
            if (!name.equalsIgnoreCase(collectionName)) continue;
            return true;
        }
        return false;
    }

    @Deprecated
    public CommandResult doEval(String code, Object ... args) {
        BasicDBObject commandDocument = new BasicDBObject("$eval", code).append("args", Arrays.asList(args));
        return this.executeCommand(this.wrap(commandDocument));
    }

    @Deprecated
    public Object eval(String code, Object ... args) {
        CommandResult result = this.doEval(code, args);
        result.throwOnError();
        return result.get("retval");
    }

    @Deprecated
    public CommandResult getStats() {
        BsonDocument commandDocument = new BsonDocument("dbStats", new BsonInt32(1)).append("scale", new BsonInt32(1));
        return this.executeCommand(commandDocument);
    }

    @Deprecated
    public WriteResult addUser(String userName, char[] password) {
        return this.addUser(userName, password, false);
    }

    @Deprecated
    public WriteResult addUser(String userName, char[] password, boolean readOnly) {
        boolean userExists;
        MongoCredential credential;
        block5: {
            credential = MongoCredential.createCredential(userName, this.getName(), password);
            userExists = false;
            try {
                userExists = this.executor.execute(new UserExistsOperation(this.getName(), userName), ReadPreference.primary(), this.getReadConcern());
            } catch (MongoCommandException e2) {
                if (e2.getCode() == 13) break block5;
                throw e2;
            }
        }
        try {
            if (userExists) {
                this.executor.execute(new UpdateUserOperation(credential, readOnly, this.getWriteConcern()), this.getReadConcern());
                return new WriteResult(1, true, null);
            }
            this.executor.execute(new CreateUserOperation(credential, readOnly, this.getWriteConcern()), this.getReadConcern());
            return new WriteResult(1, false, null);
        } catch (MongoWriteConcernException e3) {
            throw DBCollection.createWriteConcernException(e3);
        }
    }

    @Deprecated
    public WriteResult removeUser(String userName) {
        try {
            this.executor.execute(new DropUserOperation(this.getName(), userName, this.getWriteConcern()), this.getReadConcern());
            return new WriteResult(1, true, null);
        } catch (MongoWriteConcernException e2) {
            throw DBCollection.createWriteConcernException(e2);
        }
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
    public void setOptions(int options) {
        this.optionHolder.set(options);
    }

    @Deprecated
    public void resetOptions() {
        this.optionHolder.reset();
    }

    @Deprecated
    public int getOptions() {
        return this.optionHolder.get();
    }

    public String toString() {
        return "DB{name='" + this.name + '\'' + '}';
    }

    CommandResult executeCommand(BsonDocument commandDocument) {
        return new CommandResult(this.executor.execute(new CommandWriteOperation<BsonDocument>(this.getName(), commandDocument, new BsonDocumentCodec()), this.getReadConcern()));
    }

    CommandResult executeCommand(BsonDocument commandDocument, ReadPreference readPreference) {
        return new CommandResult(this.executor.execute(new CommandReadOperation<BsonDocument>(this.getName(), commandDocument, new BsonDocumentCodec()), readPreference, this.getReadConcern()));
    }

    OperationExecutor getExecutor() {
        return this.executor;
    }

    Bytes.OptionHolder getOptionHolder() {
        return this.optionHolder;
    }

    BufferProvider getBufferPool() {
        return this.getMongo().getBufferProvider();
    }

    private BsonDocument wrap(DBObject document) {
        return new BsonDocumentWrapper<DBObject>(document, this.commandCodec);
    }

    private BsonDocument wrap(DBObject document, @Nullable DBEncoder encoder) {
        if (encoder == null) {
            return this.wrap(document);
        }
        return new BsonDocumentWrapper<DBObject>(document, new DBEncoderAdapter(encoder));
    }

    ReadPreference getCommandReadPreference(DBObject command, @Nullable ReadPreference requestedPreference) {
        boolean primaryRequired;
        String comString = command.keySet().iterator().next().toLowerCase();
        boolean bl2 = primaryRequired = !OBEDIENT_COMMANDS.contains(comString);
        if (primaryRequired) {
            return ReadPreference.primary();
        }
        if (requestedPreference == null) {
            return ReadPreference.primary();
        }
        return requestedPreference;
    }

    static {
        OBEDIENT_COMMANDS.add("aggregate");
        OBEDIENT_COMMANDS.add("collstats");
        OBEDIENT_COMMANDS.add("count");
        OBEDIENT_COMMANDS.add("dbstats");
        OBEDIENT_COMMANDS.add("distinct");
        OBEDIENT_COMMANDS.add("geonear");
        OBEDIENT_COMMANDS.add("geosearch");
        OBEDIENT_COMMANDS.add("geowalk");
        OBEDIENT_COMMANDS.add("group");
        OBEDIENT_COMMANDS.add("listcollections");
        OBEDIENT_COMMANDS.add("listindexes");
        OBEDIENT_COMMANDS.add("parallelcollectionscan");
        OBEDIENT_COMMANDS.add("text");
    }
}

