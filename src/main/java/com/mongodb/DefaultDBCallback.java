/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.BasicDBList;
import com.mongodb.DBCallback;
import com.mongodb.DBCallbackFactory;
import com.mongodb.DBCollection;
import com.mongodb.DBCollectionObjectFactory;
import com.mongodb.DBObjectFactory;
import com.mongodb.DBRef;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.bson.BSONObject;
import org.bson.BasicBSONCallback;
import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;

public class DefaultDBCallback
extends BasicBSONCallback
implements DBCallback {
    private final DBObjectFactory objectFactory;
    public static final DBCallbackFactory FACTORY = new DBCallbackFactory(){

        @Override
        public DBCallback create(DBCollection collection) {
            return new DefaultDBCallback(collection);
        }
    };

    public DefaultDBCallback(DBCollection collection) {
        this.objectFactory = collection != null ? collection.getObjectFactory() : new DBCollectionObjectFactory();
    }

    @Override
    public BSONObject create() {
        return this.objectFactory.getInstance();
    }

    @Override
    public BSONObject create(boolean array, List<String> path) {
        return array ? new BasicDBList() : this.objectFactory.getInstance(path != null ? path : Collections.emptyList());
    }

    @Override
    public void gotDBRef(String name, String namespace, ObjectId id) {
        this._put(name, new DBRef(namespace, id));
    }

    @Override
    public Object objectDone() {
        Iterator<String> iterator;
        String name = this.curName();
        BSONObject document = (BSONObject)super.objectDone();
        if (!(document instanceof BasicBSONList) && (iterator = document.keySet().iterator()).hasNext() && iterator.next().equals("$ref") && iterator.hasNext() && iterator.next().equals("$id")) {
            this._put(name, new DBRef((String)document.get("$db"), (String)document.get("$ref"), document.get("$id")));
        }
        return document;
    }
}

