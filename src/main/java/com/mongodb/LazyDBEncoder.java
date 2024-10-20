/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.DBEncoder;
import com.mongodb.LazyDBObject;
import com.mongodb.MongoException;
import java.io.IOException;
import org.bson.BSONObject;
import org.bson.io.OutputBuffer;

public class LazyDBEncoder
implements DBEncoder {
    @Override
    public int writeObject(OutputBuffer outputBuffer, BSONObject document) {
        if (!(document instanceof LazyDBObject)) {
            throw new IllegalArgumentException("LazyDBEncoder can only encode BSONObject instances of type LazyDBObject");
        }
        LazyDBObject lazyDBObject = (LazyDBObject)document;
        try {
            return lazyDBObject.pipe(outputBuffer);
        } catch (IOException e2) {
            throw new MongoException("Exception serializing a LazyDBObject", e2);
        }
    }
}

