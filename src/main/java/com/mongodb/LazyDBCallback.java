/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.DBCallback;
import com.mongodb.DBCollection;
import com.mongodb.DBRef;
import com.mongodb.LazyDBList;
import com.mongodb.LazyDBObject;
import com.mongodb.lang.Nullable;
import java.util.Iterator;
import java.util.List;
import org.bson.LazyBSONCallback;
import org.bson.types.ObjectId;

public class LazyDBCallback
extends LazyBSONCallback
implements DBCallback {
    public LazyDBCallback(@Nullable DBCollection collection) {
    }

    @Override
    public Object createObject(byte[] bytes, int offset) {
        LazyDBObject document = new LazyDBObject(bytes, offset, this);
        Iterator<String> iterator = document.keySet().iterator();
        if (iterator.hasNext() && iterator.next().equals("$ref") && iterator.hasNext() && iterator.next().equals("$id")) {
            return new DBRef((String)document.get("$db"), (String)document.get("$ref"), document.get("$id"));
        }
        return document;
    }

    @Override
    public List createArray(byte[] bytes, int offset) {
        return new LazyDBList(bytes, offset, this);
    }

    @Override
    public Object createDBRef(String ns, ObjectId id) {
        return new DBRef(ns, id);
    }
}

