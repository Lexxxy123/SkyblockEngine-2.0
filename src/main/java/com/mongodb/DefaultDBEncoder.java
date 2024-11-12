/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.DBEncoder;
import com.mongodb.DBEncoderFactory;
import com.mongodb.DBRef;
import org.bson.BSONObject;
import org.bson.BasicBSONEncoder;
import org.bson.io.OutputBuffer;

public class DefaultDBEncoder
extends BasicBSONEncoder
implements DBEncoder {
    public static final DBEncoderFactory FACTORY = new DBEncoderFactory(){

        @Override
        public DBEncoder create() {
            return new DefaultDBEncoder();
        }
    };

    @Override
    public int writeObject(OutputBuffer outputBuffer, BSONObject document) {
        this.set(outputBuffer);
        int x2 = this.putObject(document);
        this.done();
        return x2;
    }

    @Override
    protected boolean putSpecial(String name, Object value) {
        if (value instanceof DBRef) {
            this.putDBRef(name, (DBRef)value);
            return true;
        }
        return false;
    }

    protected void putDBRef(String name, DBRef ref) {
        BasicDBObject dbRefDocument = new BasicDBObject("$ref", ref.getCollectionName()).append("$id", ref.getId());
        if (ref.getDatabaseName() != null) {
            dbRefDocument.put("$db", ref.getDatabaseName());
        }
        this.putObject(name, dbRefDocument);
    }

    public String toString() {
        return String.format("DBEncoder{class=%s}", this.getClass().getName());
    }
}

