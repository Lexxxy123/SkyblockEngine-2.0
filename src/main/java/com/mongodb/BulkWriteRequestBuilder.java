/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.BulkUpdateRequestBuilder;
import com.mongodb.BulkWriteOperation;
import com.mongodb.DBObject;
import com.mongodb.RemoveRequest;
import com.mongodb.client.model.Collation;
import com.mongodb.lang.Nullable;
import java.util.List;
import org.bson.codecs.Encoder;

public class BulkWriteRequestBuilder {
    private final BulkWriteOperation bulkWriteOperation;
    private final DBObject query;
    private final Encoder<DBObject> codec;
    private final Encoder<DBObject> replacementCodec;
    private Collation collation;

    BulkWriteRequestBuilder(BulkWriteOperation bulkWriteOperation, DBObject query, Encoder<DBObject> queryCodec, Encoder<DBObject> replacementCodec) {
        this.bulkWriteOperation = bulkWriteOperation;
        this.query = query;
        this.codec = queryCodec;
        this.replacementCodec = replacementCodec;
    }

    @Nullable
    public Collation getCollation() {
        return this.collation;
    }

    @Nullable
    public BulkWriteRequestBuilder collation(Collation collation) {
        this.collation = collation;
        return this;
    }

    public void remove() {
        this.bulkWriteOperation.addRequest(new RemoveRequest(this.query, true, this.codec, this.collation));
    }

    public void removeOne() {
        this.bulkWriteOperation.addRequest(new RemoveRequest(this.query, false, this.codec, this.collation));
    }

    public void replaceOne(DBObject document) {
        new BulkUpdateRequestBuilder(this.bulkWriteOperation, this.query, false, this.codec, this.replacementCodec, this.collation, null).replaceOne(document);
    }

    public void update(DBObject update) {
        new BulkUpdateRequestBuilder(this.bulkWriteOperation, this.query, false, this.codec, this.replacementCodec, this.collation, null).update(update);
    }

    public void updateOne(DBObject update) {
        new BulkUpdateRequestBuilder(this.bulkWriteOperation, this.query, false, this.codec, this.replacementCodec, this.collation, null).updateOne(update);
    }

    public BulkUpdateRequestBuilder upsert() {
        return new BulkUpdateRequestBuilder(this.bulkWriteOperation, this.query, true, this.codec, this.replacementCodec, this.collation, null);
    }

    public BulkUpdateRequestBuilder arrayFilters(List<? extends DBObject> arrayFilters) {
        return new BulkUpdateRequestBuilder(this.bulkWriteOperation, this.query, false, this.codec, this.replacementCodec, this.collation, arrayFilters);
    }
}

