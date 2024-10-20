/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import org.bson.BsonDocument;
import org.bson.BsonDocumentReader;
import org.bson.codecs.DecoderContext;

final class DBObjects {
    public static DBObject toDBObject(BsonDocument document) {
        return (DBObject)MongoClient.getDefaultCodecRegistry().get(DBObject.class).decode(new BsonDocumentReader(document), DecoderContext.builder().build());
    }

    private DBObjects() {
    }
}

