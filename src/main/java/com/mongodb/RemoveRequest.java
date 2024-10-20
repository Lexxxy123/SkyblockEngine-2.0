/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.bulk.DeleteRequest;
import com.mongodb.bulk.WriteRequest;
import com.mongodb.client.model.Collation;
import org.bson.BsonDocumentWrapper;
import org.bson.codecs.Encoder;

class RemoveRequest
extends com.mongodb.WriteRequest {
    private final DBObject query;
    private final boolean multi;
    private final Encoder<DBObject> codec;
    private final Collation collation;

    RemoveRequest(DBObject query, boolean multi, Encoder<DBObject> codec, Collation collation) {
        this.query = query;
        this.multi = multi;
        this.codec = codec;
        this.collation = collation;
    }

    public DBObject getQuery() {
        return this.query;
    }

    public boolean isMulti() {
        return this.multi;
    }

    @Override
    WriteRequest toNew(DBCollection dbCollection) {
        return new DeleteRequest(new BsonDocumentWrapper<DBObject>(this.query, this.codec)).multi(this.isMulti()).collation(this.collation);
    }
}

