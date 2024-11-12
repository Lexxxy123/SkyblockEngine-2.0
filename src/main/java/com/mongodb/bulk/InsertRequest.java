/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.bulk;

import com.mongodb.assertions.Assertions;
import com.mongodb.bulk.WriteRequest;
import org.bson.BsonDocument;

@Deprecated
public final class InsertRequest
extends WriteRequest {
    private final BsonDocument document;

    public InsertRequest(BsonDocument document) {
        this.document = Assertions.notNull("document", document);
    }

    public BsonDocument getDocument() {
        return this.document;
    }

    @Override
    public WriteRequest.Type getType() {
        return WriteRequest.Type.INSERT;
    }
}

