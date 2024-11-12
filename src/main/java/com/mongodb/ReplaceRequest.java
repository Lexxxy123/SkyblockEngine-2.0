/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.bulk.UpdateRequest;
import com.mongodb.bulk.WriteRequest;
import com.mongodb.client.model.Collation;
import org.bson.BsonDocumentWrapper;
import org.bson.codecs.Encoder;

class ReplaceRequest
extends com.mongodb.WriteRequest {
    private final DBObject query;
    private final DBObject document;
    private final boolean upsert;
    private final Encoder<DBObject> codec;
    private final Encoder<DBObject> replacementCodec;
    private final Collation collation;

    ReplaceRequest(DBObject query, DBObject document, boolean upsert, Encoder<DBObject> codec, Encoder<DBObject> replacementCodec, Collation collation) {
        this.query = query;
        this.document = document;
        this.upsert = upsert;
        this.codec = codec;
        this.replacementCodec = replacementCodec;
        this.collation = collation;
    }

    public DBObject getQuery() {
        return this.query;
    }

    public DBObject getDocument() {
        return this.document;
    }

    public boolean isUpsert() {
        return this.upsert;
    }

    public Collation getCollation() {
        return this.collation;
    }

    @Override
    WriteRequest toNew(DBCollection dbCollection) {
        return new UpdateRequest(new BsonDocumentWrapper<DBObject>(this.query, this.codec), new BsonDocumentWrapper<DBObject>(this.document, this.replacementCodec), WriteRequest.Type.REPLACE).upsert(this.isUpsert()).collation(this.getCollation());
    }
}

