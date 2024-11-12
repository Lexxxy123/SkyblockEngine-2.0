/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.DBEncoderAdapter;
import com.mongodb.DBEncoderFactory;
import com.mongodb.DBObject;
import org.bson.BsonWriter;
import org.bson.codecs.Encoder;
import org.bson.codecs.EncoderContext;

class DBEncoderFactoryAdapter
implements Encoder<DBObject> {
    private final DBEncoderFactory encoderFactory;

    DBEncoderFactoryAdapter(DBEncoderFactory encoderFactory) {
        this.encoderFactory = encoderFactory;
    }

    @Override
    public void encode(BsonWriter writer, DBObject value, EncoderContext encoderContext) {
        new DBEncoderAdapter(this.encoderFactory.create()).encode(writer, value, encoderContext);
    }

    @Override
    public Class<DBObject> getEncoderClass() {
        return DBObject.class;
    }
}

