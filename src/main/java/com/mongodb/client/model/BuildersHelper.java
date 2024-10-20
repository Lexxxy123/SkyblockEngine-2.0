/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import org.bson.BsonDocument;
import org.bson.BsonDocumentWriter;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

final class BuildersHelper {
    static <TItem> void encodeValue(BsonDocumentWriter writer, TItem value, CodecRegistry codecRegistry) {
        if (value == null) {
            writer.writeNull();
        } else if (value instanceof Bson) {
            codecRegistry.get(BsonDocument.class).encode(writer, ((Bson)value).toBsonDocument(BsonDocument.class, codecRegistry), EncoderContext.builder().build());
        } else {
            codecRegistry.get(value.getClass()).encode(writer, value, EncoderContext.builder().build());
        }
    }

    private BuildersHelper() {
    }
}

