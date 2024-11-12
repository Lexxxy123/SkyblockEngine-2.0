/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.MongoNamespace;
import com.mongodb.WriteConcern;
import com.mongodb.assertions.Assertions;
import com.mongodb.client.model.Collation;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.connection.ServerDescription;
import com.mongodb.internal.validator.NoOpFieldNameValidator;
import com.mongodb.operation.BaseFindAndModifyOperation;
import com.mongodb.operation.CommandOperationHelper;
import com.mongodb.operation.DocumentHelper;
import com.mongodb.operation.OperationHelper;
import com.mongodb.session.SessionContext;
import java.util.concurrent.TimeUnit;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.FieldNameValidator;
import org.bson.codecs.Decoder;

@Deprecated
public class FindAndDeleteOperation<T>
extends BaseFindAndModifyOperation<T> {
    private BsonDocument filter;
    private BsonDocument projection;
    private BsonDocument sort;
    private long maxTimeMS;
    private Collation collation;

    @Deprecated
    public FindAndDeleteOperation(MongoNamespace namespace, Decoder<T> decoder) {
        this(namespace, WriteConcern.ACKNOWLEDGED, false, decoder);
    }

    @Deprecated
    public FindAndDeleteOperation(MongoNamespace namespace, WriteConcern writeConcern, Decoder<T> decoder) {
        this(namespace, writeConcern, false, decoder);
    }

    public FindAndDeleteOperation(MongoNamespace namespace, WriteConcern writeConcern, boolean retryWrites, Decoder<T> decoder) {
        super(namespace, writeConcern, retryWrites, decoder);
    }

    public BsonDocument getFilter() {
        return this.filter;
    }

    public FindAndDeleteOperation<T> filter(BsonDocument filter) {
        this.filter = filter;
        return this;
    }

    public BsonDocument getProjection() {
        return this.projection;
    }

    public FindAndDeleteOperation<T> projection(BsonDocument projection) {
        this.projection = projection;
        return this;
    }

    public long getMaxTime(TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }

    public FindAndDeleteOperation<T> maxTime(long maxTime, TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }

    public BsonDocument getSort() {
        return this.sort;
    }

    public FindAndDeleteOperation<T> sort(BsonDocument sort) {
        this.sort = sort;
        return this;
    }

    public Collation getCollation() {
        return this.collation;
    }

    public FindAndDeleteOperation<T> collation(Collation collation) {
        this.collation = collation;
        return this;
    }

    @Override
    protected String getDatabaseName() {
        return this.getNamespace().getDatabaseName();
    }

    @Override
    protected CommandOperationHelper.CommandCreator getCommandCreator(final SessionContext sessionContext) {
        return new CommandOperationHelper.CommandCreator(){

            @Override
            public BsonDocument create(ServerDescription serverDescription, ConnectionDescription connectionDescription) {
                return FindAndDeleteOperation.this.createCommand(sessionContext, serverDescription, connectionDescription);
            }
        };
    }

    private BsonDocument createCommand(SessionContext sessionContext, ServerDescription serverDescription, ConnectionDescription connectionDescription) {
        OperationHelper.validateCollation(connectionDescription, this.collation);
        BsonDocument commandDocument = new BsonDocument("findAndModify", new BsonString(this.getNamespace().getCollectionName()));
        DocumentHelper.putIfNotNull(commandDocument, "query", this.getFilter());
        DocumentHelper.putIfNotNull(commandDocument, "fields", this.getProjection());
        DocumentHelper.putIfNotNull(commandDocument, "sort", this.getSort());
        DocumentHelper.putIfNotZero(commandDocument, "maxTimeMS", this.getMaxTime(TimeUnit.MILLISECONDS));
        commandDocument.put("remove", BsonBoolean.TRUE);
        this.addWriteConcernToCommand(connectionDescription, commandDocument, sessionContext);
        if (this.collation != null) {
            commandDocument.put("collation", this.collation.asDocument());
        }
        this.addTxnNumberToCommand(serverDescription, connectionDescription, commandDocument, sessionContext);
        return commandDocument;
    }

    @Override
    protected FieldNameValidator getFieldNameValidator() {
        return new NoOpFieldNameValidator();
    }
}

