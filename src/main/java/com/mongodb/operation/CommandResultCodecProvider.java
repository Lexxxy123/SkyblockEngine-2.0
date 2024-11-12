/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.operation.CommandResultDocumentCodec;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.bson.codecs.BsonArrayCodec;
import org.bson.codecs.BsonBinaryCodec;
import org.bson.codecs.BsonBooleanCodec;
import org.bson.codecs.BsonDBPointerCodec;
import org.bson.codecs.BsonDateTimeCodec;
import org.bson.codecs.BsonDecimal128Codec;
import org.bson.codecs.BsonDocumentCodec;
import org.bson.codecs.BsonDoubleCodec;
import org.bson.codecs.BsonInt32Codec;
import org.bson.codecs.BsonInt64Codec;
import org.bson.codecs.BsonJavaScriptCodec;
import org.bson.codecs.BsonJavaScriptWithScopeCodec;
import org.bson.codecs.BsonMaxKeyCodec;
import org.bson.codecs.BsonMinKeyCodec;
import org.bson.codecs.BsonNullCodec;
import org.bson.codecs.BsonObjectIdCodec;
import org.bson.codecs.BsonRegularExpressionCodec;
import org.bson.codecs.BsonStringCodec;
import org.bson.codecs.BsonSymbolCodec;
import org.bson.codecs.BsonTimestampCodec;
import org.bson.codecs.BsonUndefinedCodec;
import org.bson.codecs.Codec;
import org.bson.codecs.Decoder;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

class CommandResultCodecProvider<P>
implements CodecProvider {
    private final Map<Class<?>, Codec<?>> codecs = new HashMap();
    private final Decoder<P> payloadDecoder;
    private final List<String> fieldsContainingPayload;

    CommandResultCodecProvider(Decoder<P> payloadDecoder, List<String> fieldContainingPayload) {
        this.payloadDecoder = payloadDecoder;
        this.fieldsContainingPayload = fieldContainingPayload;
        this.addCodecs();
    }

    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (this.codecs.containsKey(clazz)) {
            return this.codecs.get(clazz);
        }
        if (clazz == BsonArray.class) {
            return new BsonArrayCodec(registry);
        }
        if (clazz == BsonDocument.class) {
            return new CommandResultDocumentCodec<P>(registry, this.payloadDecoder, this.fieldsContainingPayload);
        }
        return null;
    }

    private void addCodecs() {
        this.addCodec(new BsonNullCodec());
        this.addCodec(new BsonBinaryCodec());
        this.addCodec(new BsonBooleanCodec());
        this.addCodec(new BsonDateTimeCodec());
        this.addCodec(new BsonDBPointerCodec());
        this.addCodec(new BsonDoubleCodec());
        this.addCodec(new BsonInt32Codec());
        this.addCodec(new BsonInt64Codec());
        this.addCodec(new BsonDecimal128Codec());
        this.addCodec(new BsonMinKeyCodec());
        this.addCodec(new BsonMaxKeyCodec());
        this.addCodec(new BsonJavaScriptCodec());
        this.addCodec(new BsonObjectIdCodec());
        this.addCodec(new BsonRegularExpressionCodec());
        this.addCodec(new BsonStringCodec());
        this.addCodec(new BsonSymbolCodec());
        this.addCodec(new BsonTimestampCodec());
        this.addCodec(new BsonUndefinedCodec());
        this.addCodec(new BsonJavaScriptWithScopeCodec(new BsonDocumentCodec()));
    }

    private <T extends BsonValue> void addCodec(Codec<T> codec) {
        this.codecs.put(codec.getEncoderClass(), codec);
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        CommandResultCodecProvider that = (CommandResultCodecProvider)o2;
        if (!this.fieldsContainingPayload.equals(that.fieldsContainingPayload)) {
            return false;
        }
        return this.payloadDecoder.getClass().equals(that.payloadDecoder.getClass());
    }

    public int hashCode() {
        int result = this.payloadDecoder.getClass().hashCode();
        result = 31 * result + this.fieldsContainingPayload.hashCode();
        return result;
    }
}

