/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.assertions.Assertions;
import java.util.Arrays;
import java.util.List;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

public final class Sorts {
    private Sorts() {
    }

    public static Bson ascending(String ... fieldNames) {
        return Sorts.ascending(Arrays.asList(fieldNames));
    }

    public static Bson ascending(List<String> fieldNames) {
        Assertions.notNull("fieldNames", fieldNames);
        return Sorts.orderBy(fieldNames, new BsonInt32(1));
    }

    public static Bson descending(String ... fieldNames) {
        return Sorts.descending(Arrays.asList(fieldNames));
    }

    public static Bson descending(List<String> fieldNames) {
        Assertions.notNull("fieldNames", fieldNames);
        return Sorts.orderBy(fieldNames, new BsonInt32(-1));
    }

    public static Bson metaTextScore(String fieldName) {
        return new BsonDocument(fieldName, new BsonDocument("$meta", new BsonString("textScore")));
    }

    public static Bson orderBy(Bson ... sorts) {
        return Sorts.orderBy(Arrays.asList(sorts));
    }

    public static Bson orderBy(List<? extends Bson> sorts) {
        Assertions.notNull("sorts", sorts);
        return new CompoundSort(sorts);
    }

    private static Bson orderBy(List<String> fieldNames, BsonValue value) {
        BsonDocument document = new BsonDocument();
        for (String fieldName : fieldNames) {
            document.append(fieldName, value);
        }
        return document;
    }

    private static final class CompoundSort
    implements Bson {
        private final List<? extends Bson> sorts;

        private CompoundSort(List<? extends Bson> sorts) {
            this.sorts = sorts;
        }

        @Override
        public <TDocument> BsonDocument toBsonDocument(Class<TDocument> documentClass, CodecRegistry codecRegistry) {
            BsonDocument combinedDocument = new BsonDocument();
            for (Bson bson : this.sorts) {
                BsonDocument sortDocument = bson.toBsonDocument(documentClass, codecRegistry);
                for (String key : sortDocument.keySet()) {
                    combinedDocument.append(key, sortDocument.get(key));
                }
            }
            return combinedDocument;
        }

        public boolean equals(Object o2) {
            if (this == o2) {
                return true;
            }
            if (o2 == null || this.getClass() != o2.getClass()) {
                return false;
            }
            CompoundSort that = (CompoundSort)o2;
            return this.sorts != null ? this.sorts.equals(that.sorts) : that.sorts == null;
        }

        public int hashCode() {
            return this.sorts != null ? this.sorts.hashCode() : 0;
        }

        public String toString() {
            return "Compound Sort{sorts=" + this.sorts + '}';
        }
    }
}

