/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.DuplicateKeyException;
import com.mongodb.ErrorCategory;
import com.mongodb.MongoCommandException;
import com.mongodb.MongoException;
import com.mongodb.MongoNamespace;
import com.mongodb.WriteConcern;
import com.mongodb.WriteConcernResult;
import com.mongodb.assertions.Assertions;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.binding.AsyncWriteBinding;
import com.mongodb.binding.WriteBinding;
import com.mongodb.bulk.IndexRequest;
import com.mongodb.connection.AsyncConnection;
import com.mongodb.connection.Connection;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.internal.async.ErrorHandlingResultCallback;
import com.mongodb.internal.operation.IndexHelper;
import com.mongodb.internal.operation.WriteConcernHelper;
import com.mongodb.operation.AsyncWriteOperation;
import com.mongodb.operation.CommandOperationHelper;
import com.mongodb.operation.DocumentHelper;
import com.mongodb.operation.OperationHelper;
import com.mongodb.operation.WriteOperation;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.bson.BsonArray;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonString;

@Deprecated
public class CreateIndexesOperation
implements AsyncWriteOperation<Void>,
WriteOperation<Void> {
    private final MongoNamespace namespace;
    private final List<IndexRequest> requests;
    private final WriteConcern writeConcern;
    private long maxTimeMS;

    @Deprecated
    public CreateIndexesOperation(MongoNamespace namespace, List<IndexRequest> requests) {
        this(namespace, requests, null);
    }

    public CreateIndexesOperation(MongoNamespace namespace, List<IndexRequest> requests, WriteConcern writeConcern) {
        this.namespace = Assertions.notNull("namespace", namespace);
        this.requests = Assertions.notNull("indexRequests", requests);
        this.writeConcern = writeConcern;
    }

    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }

    public List<IndexRequest> getRequests() {
        return this.requests;
    }

    public List<String> getIndexNames() {
        ArrayList<String> indexNames = new ArrayList<String>(this.requests.size());
        for (IndexRequest request : this.requests) {
            if (request.getName() != null) {
                indexNames.add(request.getName());
                continue;
            }
            indexNames.add(IndexHelper.generateIndexName(request.getKeys()));
        }
        return indexNames;
    }

    public long getMaxTime(TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }

    public CreateIndexesOperation maxTime(long maxTime, TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        Assertions.isTrueArgument("maxTime >= 0", maxTime >= 0L);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }

    @Override
    public Void execute(final WriteBinding binding) {
        return OperationHelper.withConnection(binding, new OperationHelper.CallableWithConnection<Void>(){

            @Override
            public Void call(Connection connection) {
                try {
                    OperationHelper.validateIndexRequestCollations(connection, CreateIndexesOperation.this.requests);
                    CommandOperationHelper.executeCommand(binding, CreateIndexesOperation.this.namespace.getDatabaseName(), CreateIndexesOperation.this.getCommand(connection.getDescription()), connection, CommandOperationHelper.writeConcernErrorTransformer());
                } catch (MongoCommandException e2) {
                    throw CreateIndexesOperation.this.checkForDuplicateKeyError(e2);
                }
                return null;
            }
        });
    }

    @Override
    public void executeAsync(final AsyncWriteBinding binding, final SingleResultCallback<Void> callback) {
        OperationHelper.withAsyncConnection(binding, new OperationHelper.AsyncCallableWithConnection(){

            @Override
            public void call(AsyncConnection connection, Throwable t2) {
                SingleResultCallback<Object> errHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER);
                if (t2 != null) {
                    errHandlingCallback.onResult(null, t2);
                } else {
                    final SingleResultCallback wrappedCallback = OperationHelper.releasingCallback(errHandlingCallback, connection);
                    OperationHelper.validateIndexRequestCollations(connection, CreateIndexesOperation.this.requests, new OperationHelper.AsyncCallableWithConnection(){

                        @Override
                        public void call(AsyncConnection connection, Throwable t2) {
                            if (t2 != null) {
                                wrappedCallback.onResult(null, t2);
                            } else {
                                CommandOperationHelper.executeCommandAsync(binding, CreateIndexesOperation.this.namespace.getDatabaseName(), CreateIndexesOperation.this.getCommand(connection.getDescription()), connection, CommandOperationHelper.writeConcernErrorWriteTransformer(), new SingleResultCallback<Void>(){

                                    @Override
                                    public void onResult(Void result, Throwable t2) {
                                        wrappedCallback.onResult(null, CreateIndexesOperation.this.translateException(t2));
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }

    private BsonDocument getIndex(IndexRequest request) {
        BsonDocument index = new BsonDocument();
        index.append("key", request.getKeys());
        index.append("name", new BsonString(request.getName() != null ? request.getName() : IndexHelper.generateIndexName(request.getKeys())));
        index.append("ns", new BsonString(this.namespace.getFullName()));
        if (request.isBackground()) {
            index.append("background", BsonBoolean.TRUE);
        }
        if (request.isUnique()) {
            index.append("unique", BsonBoolean.TRUE);
        }
        if (request.isSparse()) {
            index.append("sparse", BsonBoolean.TRUE);
        }
        if (request.getExpireAfter(TimeUnit.SECONDS) != null) {
            index.append("expireAfterSeconds", new BsonInt64(request.getExpireAfter(TimeUnit.SECONDS)));
        }
        if (request.getVersion() != null) {
            index.append("v", new BsonInt32(request.getVersion()));
        }
        if (request.getWeights() != null) {
            index.append("weights", request.getWeights());
        }
        if (request.getDefaultLanguage() != null) {
            index.append("default_language", new BsonString(request.getDefaultLanguage()));
        }
        if (request.getLanguageOverride() != null) {
            index.append("language_override", new BsonString(request.getLanguageOverride()));
        }
        if (request.getTextVersion() != null) {
            index.append("textIndexVersion", new BsonInt32(request.getTextVersion()));
        }
        if (request.getSphereVersion() != null) {
            index.append("2dsphereIndexVersion", new BsonInt32(request.getSphereVersion()));
        }
        if (request.getBits() != null) {
            index.append("bits", new BsonInt32(request.getBits()));
        }
        if (request.getMin() != null) {
            index.append("min", new BsonDouble(request.getMin()));
        }
        if (request.getMax() != null) {
            index.append("max", new BsonDouble(request.getMax()));
        }
        if (request.getBucketSize() != null) {
            index.append("bucketSize", new BsonDouble(request.getBucketSize()));
        }
        if (request.getDropDups()) {
            index.append("dropDups", BsonBoolean.TRUE);
        }
        if (request.getStorageEngine() != null) {
            index.append("storageEngine", request.getStorageEngine());
        }
        if (request.getPartialFilterExpression() != null) {
            index.append("partialFilterExpression", request.getPartialFilterExpression());
        }
        if (request.getCollation() != null) {
            index.append("collation", request.getCollation().asDocument());
        }
        if (request.getWildcardProjection() != null) {
            index.append("wildcardProjection", request.getWildcardProjection());
        }
        return index;
    }

    private BsonDocument getCommand(ConnectionDescription description) {
        BsonDocument command = new BsonDocument("createIndexes", new BsonString(this.namespace.getCollectionName()));
        ArrayList<BsonDocument> values = new ArrayList<BsonDocument>();
        for (IndexRequest request : this.requests) {
            values.add(this.getIndex(request));
        }
        command.put("indexes", new BsonArray(values));
        DocumentHelper.putIfNotZero(command, "maxTimeMS", this.maxTimeMS);
        WriteConcernHelper.appendWriteConcernToCommand(this.writeConcern, command, description);
        return command;
    }

    private MongoException translateException(Throwable t2) {
        return t2 instanceof MongoCommandException ? this.checkForDuplicateKeyError((MongoCommandException)t2) : MongoException.fromThrowable(t2);
    }

    private MongoException checkForDuplicateKeyError(MongoCommandException e2) {
        if (ErrorCategory.fromErrorCode(e2.getCode()) == ErrorCategory.DUPLICATE_KEY) {
            return new DuplicateKeyException(e2.getResponse(), e2.getServerAddress(), WriteConcernResult.acknowledged(0, false, null));
        }
        return e2;
    }
}

