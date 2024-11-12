/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import org.bson.BSONObject;

public interface DBObject
extends BSONObject {
    public void markAsPartialObject();

    public boolean isPartialObject();
}

