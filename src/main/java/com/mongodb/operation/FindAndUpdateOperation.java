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
import com.mongodb.internal.operation.ServerVersionHelper;
import com.mongodb.internal.validator.MappedFieldNameValidator;
import com.mongodb.internal.validator.NoOpFieldNameValidator;
import com.mongodb.internal.validator.UpdateFieldNameValidator;
import com.mongodb.lang.Nullable;
import com.mongodb.operation.BaseFindAndModifyOperation;
import com.mongodb.operation.CommandOperationHelper;
import com.mongodb.operation.DocumentHelper;
import com.mongodb.operation.OperationHelper;
import com.mongodb.session.SessionContext;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.bson.BsonArray;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.FieldNameValidator;
import org.bson.codecs.Decoder;

@Deprecated
public class FindAndUpdateOperation<T>
extends BaseFindAndModifyOperation<T> {
    private final BsonDocument update;
    private final List<BsonDocument> updatePipeline;
    private BsonDocument filter;
    private BsonDocument projection;
    private BsonDocument sort;
    private long maxTimeMS;
    private boolean returnOriginal = true;
    private boolean upsert;
    private Boolean bypassDocumentValidation;
    private Collation collation;
    private List<BsonDocument> arrayFilters;

    @Deprecated
    public FindAndUpdateOperation(MongoNamespace namespace, Decoder<T> decoder, BsonDocument update) {
        this(namespace, WriteConcern.ACKNOWLEDGED, false, decoder, update);
    }

    @Deprecated
    public FindAndUpdateOperation(MongoNamespace namespace, WriteConcern writeConcern, Decoder<T> decoder, BsonDocument update) {
        this(namespace, writeConcern, false, decoder, update);
    }

    public FindAndUpdateOperation(MongoNamespace namespace, WriteConcern writeConcern, boolean retryWrites, Decoder<T> decoder, BsonDocument update) {
        super(namespace, writeConcern, retryWrites, decoder);
        this.update = Assertions.notNull("decoder", update);
        this.updatePipeline = null;
    }

    public FindAndUpdateOperation(MongoNamespace namespace, WriteConcern writeConcern, boolean retryWrites, Decoder<T> decoder, List<BsonDocument> update) {
        super(namespace, writeConcern, retryWrites, decoder);
        this.updatePipeline = update;
        this.update = null;
    }

    @Nullable
    public BsonDocument getUpdate() {
        return this.update;
    }

    @Nullable
    public List<BsonDocument> getUpdatePipeline() {
        return this.updatePipeline;
    }

    public BsonDocument getFilter() {
        return this.filter;
    }

    public FindAndUpdateOperation<T> filter(BsonDocument filter) {
        this.filter = filter;
        return this;
    }

    public BsonDocument getProjection() {
        return this.projection;
    }

    public FindAndUpdateOperation<T> projection(BsonDocument projection) {
        this.projection = projection;
        return this;
    }

    public long getMaxTime(TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }

    public FindAndUpdateOperation<T> maxTime(long maxTime, TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
        return this;
    }

    public BsonDocument getSort() {
        return this.sort;
    }

    public FindAndUpdateOperation<T> sort(BsonDocument sort) {
        this.sort = sort;
        return this;
    }

    public boolean isReturnOriginal() {
        return this.returnOriginal;
    }

    public FindAndUpdateOperation<T> returnOriginal(boolean returnOriginal) {
        this.returnOriginal = returnOriginal;
        return this;
    }

    public boolean isUpsert() {
        return this.upsert;
    }

    public FindAndUpdateOperation<T> upsert(boolean upsert) {
        this.upsert = upsert;
        return this;
    }

    public Boolean getBypassDocumentValidation() {
        return this.bypassDocumentValidation;
    }

    public FindAndUpdateOperation<T> bypassDocumentValidation(Boolean bypassDocumentValidation) {
        this.bypassDocumentValidation = bypassDocumentValidation;
        return this;
    }

    public Collation getCollation() {
        return this.collation;
    }

    public FindAndUpdateOperation<T> collation(Collation collation) {
        this.collation = collation;
        return this;
    }

    public FindAndUpdateOperation<T> arrayFilters(List<BsonDocument> arrayFilters) {
        this.arrayFilters = arrayFilters;
        return this;
    }

    public List<BsonDocument> getArrayFilters() {
        return this.arrayFilters;
    }

    @Override
    protected String getDatabaseName() {
        return this.getNamespace().getDatabaseName();
    }

    @Override
    protected FieldNameValidator getFieldNameValidator() {
        HashMap<String, FieldNameValidator> map = new HashMap<String, FieldNameValidator>();
        map.put("update", new UpdateFieldNameValidator());
        return new MappedFieldNameValidator(new NoOpFieldNameValidator(), map);
    }

    @Override
    protected CommandOperationHelper.CommandCreator getCommandCreator(final SessionContext sessionContext) {
        return new CommandOperationHelper.CommandCreator(){

            @Override
            public BsonDocument create(ServerDescription serverDescription, ConnectionDescription connectionDescription) {
                return FindAndUpdateOperation.this.createCommand(sessionContext, serverDescription, connectionDescription);
            }
        };
    }

    private BsonDocument createCommand(SessionContext sessionContext, ServerDescription serverDescription, ConnectionDescription connectionDescription) {
        OperationHelper.validateCollation(connectionDescription, this.collation);
        BsonDocument commandDocument = new BsonDocument("findAndModify", new BsonString(this.getNamespace().getCollectionName()));
        DocumentHelper.putIfNotNull(commandDocument, "query", this.getFilter());
        DocumentHelper.putIfNotNull(commandDocument, "fields", this.getProjection());
        DocumentHelper.putIfNotNull(commandDocument, "sort", this.getSort());
        commandDocument.put("new", new BsonBoolean(!this.isReturnOriginal()));
        DocumentHelper.putIfTrue(commandDocument, "upsert", this.isUpsert());
        DocumentHelper.putIfNotZero(commandDocument, "maxTimeMS", this.getMaxTime(TimeUnit.MILLISECONDS));
        if (this.getUpdatePipeline() != null) {
            commandDocument.put("update", new BsonArray(this.getUpdatePipeline()));
        } else {
            DocumentHelper.putIfNotNull(commandDocument, "update", this.getUpdate());
        }
        if (this.bypassDocumentValidation != null && ServerVersionHelper.serverIsAtLeastVersionThreeDotTwo(connectionDescription)) {
            commandDocument.put("bypassDocumentValidation", BsonBoolean.valueOf(this.bypassDocumentValidation));
        }
        this.addWriteConcernToCommand(connectionDescription, commandDocument, sessionContext);
        if (this.collation != null) {
            commandDocument.put("collation", this.collation.asDocument());
        }
        if (this.arrayFilters != null) {
            commandDocument.put("arrayFilters", new BsonArray(this.arrayFilters));
        }
        this.addTxnNumberToCommand(serverDescription, connectionDescription, commandDocument, sessionContext);
        return commandDocument;
    }
}

