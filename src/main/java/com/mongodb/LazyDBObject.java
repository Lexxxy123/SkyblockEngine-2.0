/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.DBObject;
import com.mongodb.annotations.Immutable;
import com.mongodb.util.JSON;
import org.bson.LazyBSONCallback;
import org.bson.LazyBSONObject;

@Immutable
public class LazyDBObject
extends LazyBSONObject
implements DBObject {
    private boolean isPartial = false;

    public LazyDBObject(byte[] bytes, LazyBSONCallback callback) {
        super(bytes, callback);
    }

    public LazyDBObject(byte[] bytes, int offset, LazyBSONCallback callback) {
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

    public String toString() {
        return JSON.serialize(this);
    }
}

