/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.operation.BsonArrayWrapper;
import java.util.ArrayList;
import org.bson.BsonArray;
import org.bson.BsonDocumentWrapper;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonValue;
import org.bson.codecs.BsonArrayCodec;
import org.bson.codecs.Decoder;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.configuration.CodecRegistry;

class CommandResultArrayCodec<T>
extends BsonArrayCodec {
    private final Decoder<T> decoder;

    CommandResultArrayCodec(CodecRegistry registry, Decoder<T> decoder) {
        super(registry);
        this.decoder = decoder;
    }

    @Override
    public BsonArray decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartArray();
        ArrayList<T> list = new ArrayList<T>();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            if (reader.getCurrentBsonType() == BsonType.NULL) {
                reader.readNull();
                list.add(null);
                continue;
            }
            list.add(this.decoder.decode(reader, decoderContext));
        }
        reader.readEndArray();
        return new BsonArrayWrapper(list);
    }

    @Override
    protected BsonValue readValue(BsonReader reader, DecoderContext decoderContext) {
        if (reader.getCurrentBsonType() == BsonType.DOCUMENT) {
            return new BsonDocumentWrapper<T>(this.decoder.decode(reader, decoderContext), null);
        }
        return super.readValue(reader, decoderContext);
    }
}

