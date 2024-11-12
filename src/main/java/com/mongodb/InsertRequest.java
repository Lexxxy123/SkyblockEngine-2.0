/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.bulk.WriteRequest;
import org.bson.BsonDocumentWrapper;
import org.bson.codecs.Encoder;

class InsertRequest
extends com.mongodb.WriteRequest {
    private final DBObject document;
    private final Encoder<DBObject> codec;

    InsertRequest(DBObject document, Encoder<DBObject> codec) {
        this.document = document;
        this.codec = codec;
    }

    public DBObject getDocument() {
        return this.document;
    }

    @Override
    WriteRequest toNew(DBCollection dbCollection) {
        return new com.mongodb.bulk.InsertRequest(new BsonDocumentWrapper<DBObject>(this.document, this.codec));
    }
}

