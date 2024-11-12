/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.DBCallback;
import com.mongodb.DBCollection;

public interface DBCallbackFactory {
    public DBCallback create(DBCollection var1);
}

