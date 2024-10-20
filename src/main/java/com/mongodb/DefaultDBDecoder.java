/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.DBCallback;
import com.mongodb.DBCollection;
import com.mongodb.DBDecoder;
import com.mongodb.DBDecoderFactory;
import com.mongodb.DBObject;
import com.mongodb.DefaultDBCallback;
import java.io.IOException;
import java.io.InputStream;
import org.bson.BSONCallback;
import org.bson.BasicBSONDecoder;

public class DefaultDBDecoder
extends BasicBSONDecoder
implements DBDecoder {
    public static final DBDecoderFactory FACTORY = new DBDecoderFactory(){

        @Override
        public DBDecoder create() {
            return new DefaultDBDecoder();
        }
    };

    @Override
    public DBCallback getDBCallback(DBCollection collection) {
        return new DefaultDBCallback(collection);
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

    public String toString() {
        return String.format("DBDecoder{class=%s}", this.getClass().getName());
    }
}

