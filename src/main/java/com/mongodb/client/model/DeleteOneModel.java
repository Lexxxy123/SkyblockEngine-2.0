/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.assertions.Assertions;
import com.mongodb.client.model.DeleteOptions;
import com.mongodb.client.model.WriteModel;
import org.bson.conversions.Bson;

public class DeleteOneModel<T>
extends WriteModel<T> {
    private final Bson filter;
    private final DeleteOptions options;

    public DeleteOneModel(Bson filter) {
        this(filter, new DeleteOptions());
    }

    public DeleteOneModel(Bson filter, DeleteOptions options) {
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
        return "DeleteOneModel{filter=" + this.filter + ", options=" + this.options + '}';
    }
}

