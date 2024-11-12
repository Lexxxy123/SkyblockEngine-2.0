/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import org.bson.BsonDocument;
import org.bson.BsonReader;
import org.bson.BsonValue;
import org.bson.codecs.BsonValueCodecProvider;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

abstract class AbstractByteBufBsonDocument
extends BsonDocument {
    private static final long serialVersionUID = 1L;
    private static final CodecRegistry REGISTRY = CodecRegistries.fromProviders(new BsonValueCodecProvider());

    AbstractByteBufBsonDocument() {
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("ByteBufBsonDocument instances are immutable");
    }

    @Override
    public BsonValue put(String key, BsonValue value) {
        throw new UnsupportedOperationException("ByteBufBsonDocument instances are immutable");
    }

    @Override
    public BsonDocument append(String key, BsonValue value) {
        throw new UnsupportedOperationException("ByteBufBsonDocument instances are immutable");
    }

    @Override
    public void putAll(Map<? extends String, ? extends BsonValue> m2) {
        throw new UnsupportedOperationException("ByteBufBsonDocument instances are immutable");
    }

    @Override
    public BsonValue remove(Object key) {
        throw new UnsupportedOperationException("ByteBufBsonDocument instances are immutable");
    }

    @Override
    public boolean isEmpty() {
        return this.findInDocument(new Finder<Boolean>(){

            @Override
            public Boolean find(BsonReader bsonReader) {
                return false;
            }

            @Override
            public Boolean notFound() {
                return true;
            }
        });
    }

    @Override
    public int size() {
        return this.findInDocument(new Finder<Integer>(){
            private int size;

            @Override
            public Integer find(BsonReader bsonReader) {
                ++this.size;
                bsonReader.readName();
                bsonReader.skipValue();
                return null;
            }

            @Override
            public Integer notFound() {
                return this.size;
            }
        });
    }

    @Override
    public Set<Map.Entry<String, BsonValue>> entrySet() {
        return this.toBsonDocument().entrySet();
    }

    @Override
    public Collection<BsonValue> values() {
        return this.toBsonDocument().values();
    }

    @Override
    public Set<String> keySet() {
        return this.toBsonDocument().keySet();
    }

    @Override
    public boolean containsKey(final Object key) {
        if (key == null) {
            throw new IllegalArgumentException("key can not be null");
        }
        Boolean containsKey = this.findInDocument(new Finder<Boolean>(){

            @Override
            public Boolean find(BsonReader bsonReader) {
                if (bsonReader.readName().equals(key)) {
                    return true;
                }
                bsonReader.skipValue();
                return null;
            }

            @Override
            public Boolean notFound() {
                return false;
            }
        });
        return containsKey != null ? containsKey : false;
    }

    @Override
    public boolean containsValue(final Object value) {
        Boolean containsValue = this.findInDocument(new Finder<Boolean>(){

            @Override
            public Boolean find(BsonReader bsonReader) {
                bsonReader.skipName();
                if (AbstractByteBufBsonDocument.this.deserializeBsonValue(bsonReader).equals(value)) {
                    return true;
                }
                return null;
            }

            @Override
            public Boolean notFound() {
                return false;
            }
        });
        return containsValue != null ? containsValue : false;
    }

    @Override
    public BsonValue get(final Object key) {
        if (key == null) {
            throw new IllegalArgumentException("key can not be null");
        }
        return this.findInDocument(new Finder<BsonValue>(){

            @Override
            public BsonValue find(BsonReader bsonReader) {
                if (bsonReader.readName().equals(key)) {
                    return AbstractByteBufBsonDocument.this.deserializeBsonValue(bsonReader);
                }
                bsonReader.skipValue();
                return null;
            }

            @Override
            public BsonValue notFound() {
                return null;
            }
        });
    }

    @Override
    public String getFirstKey() {
        return this.findInDocument(new Finder<String>(){

            @Override
            public String find(BsonReader bsonReader) {
                return bsonReader.readName();
            }

            @Override
            public String notFound() {
                throw new NoSuchElementException();
            }
        });
    }

    abstract <T> T findInDocument(Finder<T> var1);

    abstract BsonDocument toBsonDocument();

    @Override
    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        return this.toBsonDocument().equals(o2);
    }

    @Override
    public int hashCode() {
        return this.toBsonDocument().hashCode();
    }

    private BsonValue deserializeBsonValue(BsonReader bsonReader) {
        return (BsonValue)REGISTRY.get(BsonValueCodecProvider.getClassForBsonType(bsonReader.getCurrentBsonType())).decode(bsonReader, DecoderContext.builder().build());
    }

    Object writeReplace() {
        return this.toBsonDocument();
    }

    static interface Finder<T> {
        public T find(BsonReader var1);

        public T notFound();
    }
}

