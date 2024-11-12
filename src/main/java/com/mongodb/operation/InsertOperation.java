/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.MongoNamespace;
import com.mongodb.WriteConcern;
import com.mongodb.assertions.Assertions;
import com.mongodb.bulk.InsertRequest;
import com.mongodb.bulk.WriteRequest;
import com.mongodb.operation.BaseWriteOperation;
import java.util.List;

@Deprecated
public class InsertOperation
extends BaseWriteOperation {
    private final List<InsertRequest> insertRequests;

    @Deprecated
    public InsertOperation(MongoNamespace namespace, boolean ordered, WriteConcern writeConcern, List<InsertRequest> insertRequests) {
        this(namespace, ordered, writeConcern, false, insertRequests);
    }

    public InsertOperation(MongoNamespace namespace, boolean ordered, WriteConcern writeConcern, boolean retryWrites, List<InsertRequest> insertRequests) {
        super(namespace, ordered, writeConcern, retryWrites);
        this.insertRequests = Assertions.notNull("insertRequests", insertRequests);
        Assertions.isTrueArgument("insertRequests not empty", !insertRequests.isEmpty());
    }

    public List<InsertRequest> getInsertRequests() {
        return this.insertRequests;
    }

    @Override
    protected WriteRequest.Type getType() {
        return WriteRequest.Type.INSERT;
    }

    @Override
    protected List<? extends WriteRequest> getWriteRequests() {
        return this.getInsertRequests();
    }
}

