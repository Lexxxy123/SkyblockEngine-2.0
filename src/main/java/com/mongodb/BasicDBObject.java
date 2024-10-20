/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.mongodb.DBObjectCodec;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.bson.BsonBinaryWriter;
import org.bson.BsonDocument;
import org.bson.BsonDocumentWrapper;
import org.bson.codecs.Decoder;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.Encoder;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.bson.io.BasicOutputBuffer;
import org.bson.json.JsonReader;
import org.bson.json.JsonWriter;
import org.bson.json.JsonWriterSettings;
import org.bson.types.BasicBSONList;

public class BasicDBObject
extends BasicBSONObject
implements DBObject,
Bson {
    private static final long serialVersionUID = -4415279469780082174L;
    private boolean isPartialObject;

    public static BasicDBObject parse(String json) {
        return BasicDBObject.parse(json, DBObjectCodec.getDefaultRegistry().get(BasicDBObject.class));
    }

    public static BasicDBObject parse(String json, Decoder<BasicDBObject> decoder) {
        return decoder.decode(new JsonReader(json), DecoderContext.builder().build());
    }

    public BasicDBObject() {
    }

    public BasicDBObject(int size) {
        super(size);
    }

    public BasicDBObject(String key, Object value) {
        super(key, value);
    }

    public BasicDBObject(Map map) {
        super(map);
    }

    @Override
    public BasicDBObject append(String key, Object val) {
        this.put(key, val);
        return this;
    }

    @Override
    public boolean isPartialObject() {
        return this.isPartialObject;
    }

    public String toJson() {
        return this.toJson(new JsonWriterSettings());
    }

    public String toJson(JsonWriterSettings writerSettings) {
        return this.toJson(writerSettings, DBObjectCodec.getDefaultRegistry().get(BasicDBObject.class));
    }

    public String toJson(Encoder<BasicDBObject> encoder) {
        return this.toJson(new JsonWriterSettings(), encoder);
    }

    public String toJson(JsonWriterSettings writerSettings, Encoder<BasicDBObject> encoder) {
        JsonWriter writer = new JsonWriter(new StringWriter(), writerSettings);
        encoder.encode(writer, this, EncoderContext.builder().build());
        return writer.getWriter().toString();
    }

    @Override
    public boolean equals(Object o2) {
        if (o2 == this) {
            return true;
        }
        if (!(o2 instanceof BSONObject)) {
            return false;
        }
        BSONObject other = (BSONObject)o2;
        if (!this.keySet().equals(other.keySet())) {
            return false;
        }
        return Arrays.equals(BasicDBObject.toBson(BasicDBObject.canonicalizeBSONObject(this)), BasicDBObject.toBson(BasicDBObject.canonicalizeBSONObject(other)));
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(BasicDBObject.toBson(BasicDBObject.canonicalizeBSONObject(this)));
    }

    private static byte[] toBson(DBObject dbObject) {
        BasicOutputBuffer outputBuffer = new BasicOutputBuffer();
        DBObjectCodec.getDefaultRegistry().get(DBObject.class).encode(new BsonBinaryWriter(outputBuffer), dbObject, EncoderContext.builder().build());
        return outputBuffer.toByteArray();
    }

    @Override
    public String toString() {
        return this.toJson();
    }

    @Override
    public void markAsPartialObject() {
        this.isPartialObject = true;
    }

    public Object copy() {
        BasicDBObject newCopy = new BasicDBObject(this.toMap());
        for (String field : this.keySet()) {
            Object val = this.get(field);
            if (val instanceof BasicDBObject) {
                newCopy.put(field, ((BasicDBObject)val).copy());
                continue;
            }
            if (!(val instanceof BasicDBList)) continue;
            newCopy.put(field, ((BasicDBList)val).copy());
        }
        return newCopy;
    }

    @Override
    public <TDocument> BsonDocument toBsonDocument(Class<TDocument> documentClass, CodecRegistry codecRegistry) {
        return new BsonDocumentWrapper<BasicDBObject>(this, codecRegistry.get(BasicDBObject.class));
    }

    private static Object canonicalize(Object from) {
        if (from instanceof BSONObject && !(from instanceof BasicBSONList)) {
            return BasicDBObject.canonicalizeBSONObject((BSONObject)from);
        }
        if (from instanceof List) {
            return BasicDBObject.canonicalizeList((List)from);
        }
        if (from instanceof Map) {
            return BasicDBObject.canonicalizeMap((Map)from);
        }
        return from;
    }

    private static Map<String, Object> canonicalizeMap(Map<String, Object> from) {
        LinkedHashMap<String, Object> canonicalized = new LinkedHashMap<String, Object>(from.size());
        TreeSet<String> keysInOrder = new TreeSet<String>(from.keySet());
        for (String key : keysInOrder) {
            Object val = from.get(key);
            canonicalized.put(key, BasicDBObject.canonicalize(val));
        }
        return canonicalized;
    }

    private static DBObject canonicalizeBSONObject(BSONObject from) {
        BasicDBObject canonicalized = new BasicDBObject();
        TreeSet<String> keysInOrder = new TreeSet<String>(from.keySet());
        for (String key : keysInOrder) {
            Object val = from.get(key);
            canonicalized.put(key, BasicDBObject.canonicalize(val));
        }
        return canonicalized;
    }

    private static List canonicalizeList(List<Object> list) {
        ArrayList<Object> canonicalized = new ArrayList<Object>(list.size());
        for (Object cur : list) {
            canonicalized.add(BasicDBObject.canonicalize(cur));
        }
        return canonicalized;
    }
}

