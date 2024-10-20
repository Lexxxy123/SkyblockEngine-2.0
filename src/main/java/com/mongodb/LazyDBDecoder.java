/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.DBCallback;
import com.mongodb.DBCollection;
import com.mongodb.DBDecoder;
import com.mongodb.DBDecoderFactory;
import com.mongodb.DBObject;
import com.mongodb.LazyDBCallback;
import com.mongodb.lang.Nullable;
import java.io.IOException;
import java.io.InputStream;
import org.bson.BSONCallback;
import org.bson.LazyBSONDecoder;

public class LazyDBDecoder
extends LazyBSONDecoder
implements DBDecoder {
    public static final DBDecoderFactory FACTORY = new DBDecoderFactory(){

        @Override
        public DBDecoder create() {
            return new LazyDBDecoder();
        }
    };

    @Override
    public DBCallback getDBCallback(@Nullable DBCollection collection) {
        return new LazyDBCallback(collection);
    }

    @Override
    public DBObject readObject(InputStream in) throws IOException {
        DBCallback dbCallback = this.getDBCallback(null);
        this.decode(in, (BSONCallback)dbCallback);
        return (DBObject)dbCallback.get();
    }

    @Override
    public DBObject decode(InputStream input, DBCollection collection) throws IOException {
        DBCallback callback = this.getDBCallback(collection);
        this.decode(input, (BSONCallback)callback);
        return (DBObject)callback.get();
    }

    @Override
    public DBObject decode(byte[] bytes, DBCollection collection) {
        DBCallback callback = this.getDBCallback(collection);
        this.decode(bytes, (BSONCallback)callback);
        return (DBObject)callback.get();
    }
}

