/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.bulk.WriteRequest;
import com.mongodb.client.model.Collation;
import java.util.List;
import org.bson.BsonDocumentWrapper;
import org.bson.codecs.Encoder;

class UpdateRequest
extends com.mongodb.WriteRequest {
    private final DBObject query;
    private final DBObject update;
    private final boolean multi;
    private final boolean upsert;
    private final Encoder<DBObject> codec;
    private final Collation collation;
    private final List<? extends DBObject> arrayFilters;

    UpdateRequest(DBObject query, DBObject update, boolean multi, boolean upsert, Encoder<DBObject> codec, Collation collation, List<? extends DBObject> arrayFilters) {
        this.query = query;
        this.update = update;
        this.multi = multi;
        this.upsert = upsert;
        this.codec = codec;
        this.collation = collation;
        this.arrayFilters = arrayFilters;
    }

    public DBObject getQuery() {
        return this.query;
    }

    public DBObject getUpdate() {
        return this.update;
    }

    public boolean isUpsert() {
        return this.upsert;
    }

    public boolean isMulti() {
        return this.multi;
    }

    public Collation getCollation() {
        return this.collation;
    }

    public List<? extends DBObject> getArrayFilters() {
        return this.arrayFilters;
    }

    @Override
    WriteRequest toNew(DBCollection dbCollection) {
        return new com.mongodb.bulk.UpdateRequest(new BsonDocumentWrapper<DBObject>(this.query, this.codec), new BsonDocumentWrapper<DBObject>(this.update, this.codec), WriteRequest.Type.UPDATE).upsert(this.isUpsert()).multi(this.isMulti()).collation(this.getCollation()).arrayFilters(dbCollection.wrapAllowNull(this.arrayFilters, this.codec));
    }
}

