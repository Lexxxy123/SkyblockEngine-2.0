/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.BulkWriteOperation;
import com.mongodb.DBObject;
import com.mongodb.ReplaceRequest;
import com.mongodb.UpdateRequest;
import com.mongodb.client.model.Collation;
import com.mongodb.lang.Nullable;
import java.util.List;
import org.bson.codecs.Encoder;

public class BulkUpdateRequestBuilder {
    private final BulkWriteOperation bulkWriteOperation;
    private final DBObject query;
    private final boolean upsert;
    private final Encoder<DBObject> queryCodec;
    private final Encoder<DBObject> replacementCodec;
    private Collation collation;
    private final List<? extends DBObject> arrayFilters;

    BulkUpdateRequestBuilder(BulkWriteOperation bulkWriteOperation, DBObject query, boolean upsert, Encoder<DBObject> queryCodec, Encoder<DBObject> replacementCodec, @Nullable Collation collation, @Nullable List<? extends DBObject> arrayFilters) {
        this.bulkWriteOperation = bulkWriteOperation;
        this.query = query;
        this.upsert = upsert;
        this.queryCodec = queryCodec;
        this.replacementCodec = replacementCodec;
        this.collation = collation;
        this.arrayFilters = arrayFilters;
    }

    @Nullable
    public Collation getCollation() {
        return this.collation;
    }

    @Nullable
    public BulkUpdateRequestBuilder collation(Collation collation) {
        this.collation = collation;
        return this;
    }

    @Nullable
    public List<? extends DBObject> getArrayFilters() {
        return this.arrayFilters;
    }

    public void replaceOne(DBObject document) {
        this.bulkWriteOperation.addRequest(new ReplaceRequest(this.query, document, this.upsert, this.queryCodec, this.replacementCodec, this.collation));
    }

    public void update(DBObject update) {
        this.bulkWriteOperation.addRequest(new UpdateRequest(this.query, update, true, this.upsert, this.queryCodec, this.collation, this.arrayFilters));
    }

    public void updateOne(DBObject update) {
        this.bulkWriteOperation.addRequest(new UpdateRequest(this.query, update, false, this.upsert, this.queryCodec, this.collation, this.arrayFilters));
    }
}

