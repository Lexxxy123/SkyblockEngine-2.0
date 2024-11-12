/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.MongoNamespace;
import com.mongodb.WriteConcern;
import com.mongodb.assertions.Assertions;
import com.mongodb.bulk.DeleteRequest;
import com.mongodb.bulk.WriteRequest;
import com.mongodb.operation.BaseWriteOperation;
import java.util.List;

@Deprecated
public class DeleteOperation
extends BaseWriteOperation {
    private final List<DeleteRequest> deleteRequests;

    @Deprecated
    public DeleteOperation(MongoNamespace namespace, boolean ordered, WriteConcern writeConcern, List<DeleteRequest> deleteRequests) {
        this(namespace, ordered, writeConcern, false, deleteRequests);
    }

    public DeleteOperation(MongoNamespace namespace, boolean ordered, WriteConcern writeConcern, boolean retryWrites, List<DeleteRequest> deleteRequests) {
        super(namespace, ordered, writeConcern, retryWrites);
        this.deleteRequests = Assertions.notNull("removes", deleteRequests);
        Assertions.isTrueArgument("deleteRequests not empty", !deleteRequests.isEmpty());
    }

    public List<DeleteRequest> getDeleteRequests() {
        return this.deleteRequests;
    }

    @Override
    protected List<? extends WriteRequest> getWriteRequests() {
        return this.getDeleteRequests();
    }

    @Override
    protected WriteRequest.Type getType() {
        return WriteRequest.Type.DELETE;
    }
}

