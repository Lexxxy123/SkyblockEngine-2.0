/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.bulk;

import com.mongodb.assertions.Assertions;
import com.mongodb.bulk.WriteRequest;
import com.mongodb.client.model.Collation;
import org.bson.BsonDocument;

@Deprecated
public final class DeleteRequest
extends WriteRequest {
    private final BsonDocument filter;
    private boolean isMulti = true;
    private Collation collation;

    public DeleteRequest(BsonDocument filter) {
        this.filter = Assertions.notNull("filter", filter);
    }

    public BsonDocument getFilter() {
        return this.filter;
    }

    public DeleteRequest multi(boolean isMulti) {
        this.isMulti = isMulti;
        return this;
    }

    public boolean isMulti() {
        return this.isMulti;
    }

    public Collation getCollation() {
        return this.collation;
    }

    public DeleteRequest collation(Collation collation) {
        this.collation = collation;
        return this;
    }

    @Override
    public WriteRequest.Type getType() {
        return WriteRequest.Type.DELETE;
    }
}

