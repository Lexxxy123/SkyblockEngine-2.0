/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.bson.LazyBSONCallback;
import org.bson.LazyBSONList;

public class LazyDBList
extends LazyBSONList
implements DBObject {
    private boolean isPartial;

    public LazyDBList(byte[] bytes, LazyBSONCallback callback) {
        super(bytes, callback);
    }

    public LazyDBList(byte[] bytes, int offset, LazyBSONCallback callback) {
        super(bytes, offset, callback);
    }

    @Override
    public void markAsPartialObject() {
        this.isPartial = true;
    }

    @Override
    public boolean isPartialObject() {
        return this.isPartial;
    }

    @Deprecated
    public String toString() {
        return JSON.serialize(this);
    }
}

