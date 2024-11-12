/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.MongoNamespace;
import com.mongodb.WriteConcern;
import com.mongodb.assertions.Assertions;
import com.mongodb.bulk.UpdateRequest;
import com.mongodb.bulk.WriteRequest;
import com.mongodb.operation.BaseWriteOperation;
import java.util.List;

@Deprecated
public class UpdateOperation
extends BaseWriteOperation {
    private final List<UpdateRequest> updates;

    @Deprecated
    public UpdateOperation(MongoNamespace namespace, boolean ordered, WriteConcern writeConcern, List<UpdateRequest> updates) {
        this(namespace, ordered, writeConcern, false, updates);
    }

    public UpdateOperation(MongoNamespace namespace, boolean ordered, WriteConcern writeConcern, boolean retryWrites, List<UpdateRequest> updates) {
        super(namespace, ordered, writeConcern, retryWrites);
        this.updates = Assertions.notNull("update", updates);
        Assertions.isTrueArgument("updateRequests not empty", !updates.isEmpty());
    }

    public List<UpdateRequest> getUpdateRequests() {
        return this.updates;
    }

    @Override
    protected List<? extends WriteRequest> getWriteRequests() {
        return this.getUpdateRequests();
    }

    @Override
    protected WriteRequest.Type getType() {
        return WriteRequest.Type.UPDATE;
    }
}

