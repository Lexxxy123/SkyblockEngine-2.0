/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.assertions.Assertions;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.WriteModel;
import org.bson.conversions.Bson;

public final class ReplaceOneModel<T>
extends WriteModel<T> {
    private final Bson filter;
    private final T replacement;
    private final ReplaceOptions options;

    public ReplaceOneModel(Bson filter, T replacement) {
        this(filter, replacement, new ReplaceOptions());
    }

    @Deprecated
    public ReplaceOneModel(Bson filter, T replacement, UpdateOptions options) {
        this(filter, replacement, ReplaceOptions.createReplaceOptions(options));
    }

    public ReplaceOneModel(Bson filter, T replacement, ReplaceOptions options) {
        this.filter = Assertions.notNull("filter", filter);
        this.options = Assertions.notNull("options", options);
        this.replacement = Assertions.notNull("replacement", replacement);
    }

    public Bson getFilter() {
        return this.filter;
    }

    public T getReplacement() {
        return this.replacement;
    }

    @Deprecated
    public UpdateOptions getOptions() {
        return new UpdateOptions().bypassDocumentValidation(this.options.getBypassDocumentValidation()).collation(this.options.getCollation()).upsert(this.options.isUpsert());
    }

    public ReplaceOptions getReplaceOptions() {
        return this.options;
    }

    public String toString() {
        return "ReplaceOneModel{filter=" + this.filter + ", replacement=" + this.replacement + ", options=" + this.options + '}';
    }
}

