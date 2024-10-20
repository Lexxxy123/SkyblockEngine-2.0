/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.DBObject;
import java.util.List;

interface DBObjectFactory {
    public DBObject getInstance();

    public DBObject getInstance(List<String> var1);
}

