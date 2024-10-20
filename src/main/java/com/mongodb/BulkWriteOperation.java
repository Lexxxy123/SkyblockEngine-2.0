/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.BulkWriteRequestBuilder;
import com.mongodb.BulkWriteResult;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.InsertRequest;
import com.mongodb.WriteConcern;
import com.mongodb.WriteRequest;
import com.mongodb.assertions.Assertions;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

public class BulkWriteOperation {
    private static final String ID_FIELD_NAME = "_id";
    private final boolean ordered;
    private final DBCollection collection;
    private final List<WriteRequest> requests = new ArrayList<WriteRequest>();
    private Boolean bypassDocumentValidation;
    private boolean closed;

    BulkWriteOperation(boolean ordered, DBCollection collection) {
        this.ordered = ordered;
        this.collection = collection;
    }

    public boolean isOrdered() {
        return this.ordered;
    }

    public Boolean getBypassDocumentValidation() {
        return this.bypassDocumentValidation;
    }

    public void setBypassDocumentValidation(Boolean bypassDocumentValidation) {
        this.bypassDocumentValidation = bypassDocumentValidation;
    }

    public void insert(DBObject document) {
        Assertions.isTrue("already executed", !this.closed);
        if (document.get(ID_FIELD_NAME) == null) {
            document.put(ID_FIELD_NAME, new ObjectId());
        }
        this.addRequest(new InsertRequest(document, this.collection.getObjectCodec()));
    }

    public BulkWriteRequestBuilder find(DBObject query) {
        Assertions.isTrue("already executed", !this.closed);
        return new BulkWriteRequestBuilder(this, query, this.collection.getDefaultDBObjectCodec(), this.collection.getObjectCodec());
    }

    public BulkWriteResult execute() {
        Assertions.isTrue("already executed", !this.closed);
        this.closed = true;
        return this.collection.executeBulkWriteOperation(this.ordered, this.bypassDocumentValidation, this.requests);
    }

    public BulkWriteResult execute(WriteConcern writeConcern) {
        Assertions.isTrue("already executed", !this.closed);
        this.closed = true;
        return this.collection.executeBulkWriteOperation(this.ordered, this.bypassDocumentValidation, this.requests, writeConcern);
    }

    void addRequest(WriteRequest request) {
        Assertions.isTrue("already executed", !this.closed);
        this.requests.add(request);
    }
}

