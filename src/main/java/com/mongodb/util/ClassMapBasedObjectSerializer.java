/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.util;

import com.mongodb.Bytes;
import com.mongodb.util.AbstractObjectSerializer;
import com.mongodb.util.ObjectSerializer;
import java.util.Iterator;
import java.util.List;
import org.bson.util.ClassMap;

class ClassMapBasedObjectSerializer
extends AbstractObjectSerializer {
    private final ClassMap<ObjectSerializer> _serializers = new ClassMap();

    ClassMapBasedObjectSerializer() {
    }

    void addObjectSerializer(Class c2, ObjectSerializer serializer) {
        this._serializers.put(c2, serializer);
    }

    @Override
    public void serialize(Object obj, StringBuilder buf) {
        Class<?> ancestor;
        Object objectToSerialize = obj;
        if ((objectToSerialize = Bytes.applyEncodingHooks(objectToSerialize)) == null) {
            buf.append(" null ");
            return;
        }
        ObjectSerializer serializer = null;
        List<Class<?>> ancestors = ClassMap.getAncestry(objectToSerialize.getClass());
        Iterator<Class<?>> iterator = ancestors.iterator();
        while (iterator.hasNext() && (serializer = this._serializers.get(ancestor = iterator.next())) == null) {
        }
        if (serializer == null && objectToSerialize.getClass().isArray()) {
            serializer = this._serializers.get(Object[].class);
        }
        if (serializer == null) {
            throw new RuntimeException("json can't serialize type : " + objectToSerialize.getClass());
        }
        serializer.serialize(objectToSerialize, buf);
    }
}

