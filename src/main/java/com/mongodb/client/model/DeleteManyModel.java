/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.assertions.Assertions;
import com.mongodb.client.model.DeleteOptions;
import com.mongodb.client.model.WriteModel;
import org.bson.conversions.Bson;

public final class DeleteManyModel<T>
extends WriteModel<T> {
    private final Bson filter;
    private final DeleteOptions options;

    public DeleteManyModel(Bson filter) {
        this(filter, new DeleteOptions());
    }

    public DeleteManyModel(Bson filter, DeleteOptions options) {
        this.filter = Assertions.notNull("filter", filter);
        this.options = Assertions.notNull("options", options);
    }

    public Bson getFilter() {
        return this.filter;
    }

    public DeleteOptions getOptions() {
        return this.options;
    }

    public String toString() {
        return "DeleteManyModel{filter=" + this.filter + ", options=" + this.options + '}';
    }
}

