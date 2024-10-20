/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.bulk;

import com.mongodb.assertions.Assertions;
import com.mongodb.bulk.WriteRequest;
import com.mongodb.client.model.Collation;
import java.util.List;
import org.bson.BsonDocument;
import org.bson.BsonValue;

@Deprecated
public final class UpdateRequest
extends WriteRequest {
    private final BsonValue update;
    private final WriteRequest.Type updateType;
    private final BsonDocument filter;
    private boolean isMulti = true;
    private boolean isUpsert = false;
    private Collation collation;
    private List<BsonDocument> arrayFilters;

    public UpdateRequest(BsonDocument filter, BsonValue update, WriteRequest.Type updateType) {
        if (updateType != WriteRequest.Type.UPDATE && updateType != WriteRequest.Type.REPLACE) {
            throw new IllegalArgumentException("Update type must be UPDATE or REPLACE");
        }
        if (update != null && !update.isDocument() && !update.isArray()) {
            throw new IllegalArgumentException("Update operation type must be a document or a pipeline");
        }
        this.filter = Assertions.notNull("filter", filter);
        this.update = Assertions.notNull("update", update);
        this.updateType = updateType;
        this.isMulti = updateType == WriteRequest.Type.UPDATE;
    }

    @Override
    public WriteRequest.Type getType() {
        return this.updateType;
    }

    public BsonDocument getFilter() {
        return this.filter;
    }

    @Deprecated
    public BsonDocument getUpdate() {
        if (this.update.isDocument()) {
            return this.update.asDocument();
        }
        return null;
    }

    public BsonValue getUpdateValue() {
        return this.update;
    }

    public boolean isMulti() {
        return this.isMulti;
    }

    public UpdateRequest multi(boolean isMulti) {
        if (isMulti && this.updateType == WriteRequest.Type.REPLACE) {
            throw new IllegalArgumentException("Replacements can not be multi");
        }
        this.isMulti = isMulti;
        return this;
    }

    public boolean isUpsert() {
        return this.isUpsert;
    }

    public UpdateRequest upsert(boolean isUpsert) {
        this.isUpsert = isUpsert;
        return this;
    }

    public Collation getCollation() {
        return this.collation;
    }

    public UpdateRequest collation(Collation collation) {
        this.collation = collation;
        return this;
    }

    public UpdateRequest arrayFilters(List<BsonDocument> arrayFilters) {
        this.arrayFilters = arrayFilters;
        return this;
    }

    public List<BsonDocument> getArrayFilters() {
        return this.arrayFilters;
    }
}

