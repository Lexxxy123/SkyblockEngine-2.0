/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBObjectFactory;
import java.util.List;

class BasicDBObjectFactory
implements DBObjectFactory {
    BasicDBObjectFactory() {
    }

    @Override
    public DBObject getInstance() {
        return new BasicDBObject();
    }

    @Override
    public DBObject getInstance(List<String> path) {
        return new BasicDBObject();
    }
}

