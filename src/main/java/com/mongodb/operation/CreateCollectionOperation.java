/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.WriteConcern;
import com.mongodb.assertions.Assertions;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.binding.AsyncWriteBinding;
import com.mongodb.binding.WriteBinding;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.ValidationAction;
import com.mongodb.client.model.ValidationLevel;
import com.mongodb.connection.AsyncConnection;
import com.mongodb.connection.Connection;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.internal.async.ErrorHandlingResultCallback;
import com.mongodb.internal.operation.WriteConcernHelper;
import com.mongodb.operation.AsyncWriteOperation;
import com.mongodb.operation.CommandOperationHelper;
import com.mongodb.operation.DocumentHelper;
import com.mongodb.operation.OperationHelper;
import com.mongodb.operation.WriteOperation;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;

@Deprecated
public class CreateCollectionOperation
implements AsyncWriteOperation<Void>,
WriteOperation<Void> {
    private final String databaseName;
    private final String collectionName;
    private final WriteConcern writeConcern;
    private boolean capped = false;
    private long sizeInBytes = 0L;
    private boolean autoIndex = true;
    private long maxDocuments = 0L;
    private Boolean usePowerOf2Sizes = null;
    private BsonDocument storageEngineOptions;
    private BsonDocument indexOptionDefaults;
    private BsonDocument validator;
    private ValidationLevel validationLevel = null;
    private ValidationAction validationAction = null;
    private Collation collation = null;

    public CreateCollectionOperation(String databaseName, String collectionName) {
        this(databaseName, collectionName, null);
    }

    public CreateCollectionOperation(String databaseName, String collectionName, WriteConcern writeConcern) {
        this.databaseName = Assertions.notNull("databaseName", databaseName);
        this.collectionName = Assertions.notNull("collectionName", collectionName);
        this.writeConcern = writeConcern;
    }

    public String getCollectionName() {
        return this.collectionName;
    }

    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }

    public boolean isAutoIndex() {
        return this.autoIndex;
    }

    public CreateCollectionOperation autoIndex(boolean autoIndex) {
        this.autoIndex = autoIndex;
        return this;
    }

    public long getMaxDocuments() {
        return this.maxDocuments;
    }

    public CreateCollectionOperation maxDocuments(long maxDocuments) {
        this.maxDocuments = maxDocuments;
        return this;
    }

    public boolean isCapped() {
        return this.capped;
    }

    public CreateCollectionOperation capped(boolean capped) {
        this.capped = capped;
        return this;
    }

    public long getSizeInBytes() {
        return this.sizeInBytes;
    }

    public CreateCollectionOperation sizeInBytes(long sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
        return this;
    }

    @Deprecated
    public Boolean isUsePowerOf2Sizes() {
        return this.usePowerOf2Sizes;
    }

    @Deprecated
    public CreateCollectionOperation usePowerOf2Sizes(Boolean usePowerOf2Sizes) {
        this.usePowerOf2Sizes = usePowerOf2Sizes;
        return this;
    }

    public BsonDocument getStorageEngineOptions() {
        return this.storageEngineOptions;
    }

    public CreateCollectionOperation storageEngineOptions(BsonDocument storageEngineOptions) {
        this.storageEngineOptions = storageEngineOptions;
        return this;
    }

    public BsonDocument getIndexOptionDefaults() {
        return this.indexOptionDefaults;
    }

    public CreateCollectionOperation indexOptionDefaults(BsonDocument indexOptionDefaults) {
        this.indexOptionDefaults = indexOptionDefaults;
        return this;
    }

    public BsonDocument getValidator() {
        return this.validator;
    }

    public CreateCollectionOperation validator(BsonDocument validator) {
        this.validator = validator;
        return this;
    }

    public ValidationLevel getValidationLevel() {
        return this.validationLevel;
    }

    public CreateCollectionOperation validationLevel(ValidationLevel validationLevel) {
        this.validationLevel = validationLevel;
        return this;
    }

    public ValidationAction getValidationAction() {
        return this.validationAction;
    }

    public CreateCollectionOperation validationAction(ValidationAction validationAction) {
        this.validationAction = validationAction;
        return this;
    }

    public Collation getCollation() {
        return this.collation;
    }

    public CreateCollectionOperation collation(Collation collation) {
        this.collation = collation;
        return this;
    }

    @Override
    public Void execute(final WriteBinding binding) {
        return OperationHelper.withConnection(binding, new OperationHelper.CallableWithConnection<Void>(){

            @Override
            public Void call(Connection connection) {
                OperationHelper.validateCollation(connection, CreateCollectionOperation.this.collation);
                CommandOperationHelper.executeCommand(binding, CreateCollectionOperation.this.databaseName, CreateCollectionOperation.this.getCommand(connection.getDescription()), connection, CommandOperationHelper.writeConcernErrorTransformer());
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
                    OperationHelper.validateCollation(connection, CreateCollectionOperation.this.collation, new OperationHelper.AsyncCallableWithConnection(){

                        @Override
                        public void call(AsyncConnection connection, Throwable t2) {
                            if (t2 != null) {
                                wrappedCallback.onResult(null, t2);
                            } else {
                                CommandOperationHelper.executeCommandAsync(binding, CreateCollectionOperation.this.databaseName, CreateCollectionOperation.this.getCommand(connection.getDescription()), connection, CommandOperationHelper.writeConcernErrorWriteTransformer(), wrappedCallback);
                            }
                        }
                    });
                }
            }
        });
    }

    private BsonDocument getCommand(ConnectionDescription description) {
        BsonDocument document = new BsonDocument("create", new BsonString(this.collectionName));
        DocumentHelper.putIfFalse(document, "autoIndexId", this.autoIndex);
        document.put("capped", BsonBoolean.valueOf(this.capped));
        if (this.capped) {
            DocumentHelper.putIfNotZero(document, "size", this.sizeInBytes);
            DocumentHelper.putIfNotZero(document, "max", this.maxDocuments);
        }
        if (this.usePowerOf2Sizes != null) {
            document.put("flags", new BsonInt32(1));
        }
        if (this.storageEngineOptions != null) {
            document.put("storageEngine", this.storageEngineOptions);
        }
        if (this.indexOptionDefaults != null) {
            document.put("indexOptionDefaults", this.indexOptionDefaults);
        }
        if (this.validator != null) {
            document.put("validator", this.validator);
        }
        if (this.validationLevel != null) {
            document.put("validationLevel", new BsonString(this.validationLevel.getValue()));
        }
        if (this.validationAction != null) {
            document.put("validationAction", new BsonString(this.validationAction.getValue()));
        }
        WriteConcernHelper.appendWriteConcernToCommand(this.writeConcern, document, description);
        if (this.collation != null) {
            document.put("collation", this.collation.asDocument());
        }
        return document;
    }
}

