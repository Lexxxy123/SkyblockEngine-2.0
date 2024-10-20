/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.assertions.Assertions;
import com.mongodb.client.model.SimpleExpression;
import java.util.Arrays;
import java.util.List;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

public final class Projections {
    private Projections() {
    }

    public static <TExpression> Bson computed(String fieldName, TExpression expression) {
        return new SimpleExpression<TExpression>(fieldName, expression);
    }

    public static Bson include(String ... fieldNames) {
        return Projections.include(Arrays.asList(fieldNames));
    }

    public static Bson include(List<String> fieldNames) {
        return Projections.combine(fieldNames, new BsonInt32(1));
    }

    public static Bson exclude(String ... fieldNames) {
        return Projections.exclude(Arrays.asList(fieldNames));
    }

    public static Bson exclude(List<String> fieldNames) {
        return Projections.combine(fieldNames, new BsonInt32(0));
    }

    public static Bson excludeId() {
        return new BsonDocument("_id", new BsonInt32(0));
    }

    public static Bson elemMatch(String fieldName) {
        return new BsonDocument(fieldName + ".$", new BsonInt32(1));
    }

    public static Bson elemMatch(String fieldName, Bson filter) {
        return new ElemMatchFilterProjection(fieldName, filter);
    }

    public static Bson metaTextScore(String fieldName) {
        return new BsonDocument(fieldName, new BsonDocument("$meta", new BsonString("textScore")));
    }

    public static Bson slice(String fieldName, int limit) {
        return new BsonDocument(fieldName, new BsonDocument("$slice", new BsonInt32(limit)));
    }

    public static Bson slice(String fieldName, int skip, int limit) {
        return new BsonDocument(fieldName, new BsonDocument("$slice", new BsonArray(Arrays.asList(new BsonInt32(skip), new BsonInt32(limit)))));
    }

    public static Bson fields(Bson ... projections) {
        return Projections.fields(Arrays.asList(projections));
    }

    public static Bson fields(List<? extends Bson> projections) {
        Assertions.notNull("projections", projections);
        return new FieldsProjection(projections);
    }

    private static Bson combine(List<String> fieldNames, BsonValue value) {
        BsonDocument document = new BsonDocument();
        for (String fieldName : fieldNames) {
            document.remove(fieldName);
            document.append(fieldName, value);
        }
        return document;
    }

    private static class ElemMatchFilterProjection
    implements Bson {
        private final String fieldName;
        private final Bson filter;

        ElemMatchFilterProjection(String fieldName, Bson filter) {
            this.fieldName = fieldName;
            this.filter = filter;
        }

        @Override
        public <TDocument> BsonDocument toBsonDocument(Class<TDocument> documentClass, CodecRegistry codecRegistry) {
            return new BsonDocument(this.fieldName, new BsonDocument("$elemMatch", this.filter.toBsonDocument(documentClass, codecRegistry)));
        }

        public boolean equals(Object o2) {
            if (this == o2) {
                return true;
            }
            if (o2 == null || this.getClass() != o2.getClass()) {
                return false;
            }
            ElemMatchFilterProjection that = (ElemMatchFilterProjection)o2;
            if (this.fieldName != null ? !this.fieldName.equals(that.fieldName) : that.fieldName != null) {
                return false;
            }
            return this.filter != null ? this.filter.equals(that.filter) : that.filter == null;
        }

        public int hashCode() {
            int result = this.fieldName != null ? this.fieldName.hashCode() : 0;
            result = 31 * result + (this.filter != null ? this.filter.hashCode() : 0);
            return result;
        }

        public String toString() {
            return "ElemMatch Projection{fieldName='" + this.fieldName + '\'' + ", filter=" + this.filter + '}';
        }
    }

    private static class FieldsProjection
    implements Bson {
        private final List<? extends Bson> projections;

        FieldsProjection(List<? extends Bson> projections) {
            this.projections = projections;
        }

        @Override
        public <TDocument> BsonDocument toBsonDocument(Class<TDocument> documentClass, CodecRegistry codecRegistry) {
            BsonDocument combinedDocument = new BsonDocument();
            for (Bson bson : this.projections) {
                BsonDocument sortDocument = bson.toBsonDocument(documentClass, codecRegistry);
                for (String key : sortDocument.keySet()) {
                    combinedDocument.remove(key);
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
            FieldsProjection that = (FieldsProjection)o2;
            return this.projections != null ? this.projections.equals(that.projections) : that.projections == null;
        }

        public int hashCode() {
            return this.projections != null ? this.projections.hashCode() : 0;
        }

        public String toString() {
            return "Projections{projections=" + this.projections + '}';
        }
    }
}

