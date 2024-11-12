/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.assertions.Assertions;
import com.mongodb.client.model.WriteModel;

public final class InsertOneModel<T>
extends WriteModel<T> {
    private final T document;

    public InsertOneModel(T document) {
        this.document = Assertions.notNull("document", document);
    }

    public T getDocument() {
        return this.document;
    }

    public String toString() {
        return "InsertOneModel{document=" + this.document + '}';
    }
}

