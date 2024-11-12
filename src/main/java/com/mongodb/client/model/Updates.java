/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.assertions.Assertions;
import com.mongodb.client.model.BuildersHelper;
import com.mongodb.client.model.PushOptions;
import com.mongodb.lang.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.bson.BsonDocument;
import org.bson.BsonDocumentWriter;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

public final class Updates {
    public static Bson combine(Bson ... updates) {
        return Updates.combine(Arrays.asList(updates));
    }

    public static Bson combine(List<? extends Bson> updates) {
        Assertions.notNull("updates", updates);
        return new CompositeUpdate(updates);
    }

    public static <TItem> Bson set(String fieldName, @Nullable TItem value) {
        return new SimpleUpdate<TItem>(fieldName, value, "$set");
    }

    public static Bson unset(String fieldName) {
        return new SimpleUpdate<String>(fieldName, "", "$unset");
    }

    public static Bson setOnInsert(Bson value) {
        return new SimpleBsonKeyValue("$setOnInsert", value);
    }

    public static <TItem> Bson setOnInsert(String fieldName, @Nullable TItem value) {
        return new SimpleUpdate<TItem>(fieldName, value, "$setOnInsert");
    }

    public static Bson rename(String fieldName, String newFieldName) {
        Assertions.notNull("newFieldName", newFieldName);
        return new SimpleUpdate<String>(fieldName, newFieldName, "$rename");
    }

    public static Bson inc(String fieldName, Number number) {
        Assertions.notNull("number", number);
        return new SimpleUpdate<Number>(fieldName, number, "$inc");
    }

    public static Bson mul(String fieldName, Number number) {
        Assertions.notNull("number", number);
        return new SimpleUpdate<Number>(fieldName, number, "$mul");
    }

    public static <TItem> Bson min(String fieldName, TItem value) {
        return new SimpleUpdate<TItem>(fieldName, value, "$min");
    }

    public static <TItem> Bson max(String fieldName, TItem value) {
        return new SimpleUpdate<TItem>(fieldName, value, "$max");
    }

    public static Bson currentDate(String fieldName) {
        return new SimpleUpdate<Boolean>(fieldName, true, "$currentDate");
    }

    public static Bson currentTimestamp(String fieldName) {
        return new SimpleUpdate<BsonDocument>(fieldName, new BsonDocument("$type", new BsonString("timestamp")), "$currentDate");
    }

    public static <TItem> Bson addToSet(String fieldName, @Nullable TItem value) {
        return new SimpleUpdate<TItem>(fieldName, value, "$addToSet");
    }

    public static <TItem> Bson addEachToSet(String fieldName, List<TItem> values) {
        return new WithEachUpdate<TItem>(fieldName, values, "$addToSet");
    }

    public static <TItem> Bson push(String fieldName, @Nullable TItem value) {
        return new SimpleUpdate<TItem>(fieldName, value, "$push");
    }

    public static <TItem> Bson pushEach(String fieldName, List<TItem> values) {
        return new PushUpdate<TItem>(fieldName, values, new PushOptions());
    }

    public static <TItem> Bson pushEach(String fieldName, List<TItem> values, PushOptions options) {
        return new PushUpdate<TItem>(fieldName, values, options);
    }

    public static <TItem> Bson pull(String fieldName, @Nullable TItem value) {
        return new SimpleUpdate<TItem>(fieldName, value, "$pull");
    }

    public static Bson pullByFilter(final Bson filter) {
        return new Bson(){

            @Override
            public <TDocument> BsonDocument toBsonDocument(Class<TDocument> tDocumentClass, CodecRegistry codecRegistry) {
                BsonDocumentWriter writer = new BsonDocumentWriter(new BsonDocument());
                writer.writeStartDocument();
                writer.writeName("$pull");
                BuildersHelper.encodeValue(writer, filter, codecRegistry);
                writer.writeEndDocument();
                return writer.getDocument();
            }
        };
    }

    public static <TItem> Bson pullAll(String fieldName, List<TItem> values) {
        return new PullAllUpdate<TItem>(fieldName, values);
    }

    public static Bson popFirst(String fieldName) {
        return new SimpleUpdate<Integer>(fieldName, -1, "$pop");
    }

    public static Bson popLast(String fieldName) {
        return new SimpleUpdate<Integer>(fieldName, 1, "$pop");
    }

    public static Bson bitwiseAnd(String fieldName, int value) {
        return Updates.createBitUpdateDocument(fieldName, "and", value);
    }

    public static Bson bitwiseAnd(String fieldName, long value) {
        return Updates.createBitUpdateDocument(fieldName, "and", value);
    }

    public static Bson bitwiseOr(String fieldName, int value) {
        return Updates.createBitUpdateDocument(fieldName, "or", value);
    }

    public static Bson bitwiseOr(String fieldName, long value) {
        return Updates.createBitUpdateDocument(fieldName, "or", value);
    }

    public static Bson bitwiseXor(String fieldName, int value) {
        return Updates.createBitUpdateDocument(fieldName, "xor", value);
    }

    public static Bson bitwiseXor(String fieldName, long value) {
        return Updates.createBitUpdateDocument(fieldName, "xor", value);
    }

    private static Bson createBitUpdateDocument(String fieldName, String bitwiseOperator, int value) {
        return Updates.createBitUpdateDocument(fieldName, bitwiseOperator, new BsonInt32(value));
    }

    private static Bson createBitUpdateDocument(String fieldName, String bitwiseOperator, long value) {
        return Updates.createBitUpdateDocument(fieldName, bitwiseOperator, new BsonInt64(value));
    }

    private static Bson createBitUpdateDocument(String fieldName, String bitwiseOperator, BsonValue value) {
        return new BsonDocument("$bit", new BsonDocument(fieldName, new BsonDocument(bitwiseOperator, value)));
    }

    private Updates() {
    }

    private static class CompositeUpdate
    implements Bson {
        private final List<? extends Bson> updates;

        CompositeUpdate(List<? extends Bson> updates) {
            this.updates = updates;
        }

        @Override
        public <TDocument> BsonDocument toBsonDocument(Class<TDocument> tDocumentClass, CodecRegistry codecRegistry) {
            BsonDocument document = new BsonDocument();
            for (Bson bson : this.updates) {
                BsonDocument rendered = bson.toBsonDocument(tDocumentClass, codecRegistry);
                for (Map.Entry<String, BsonValue> element : rendered.entrySet()) {
                    if (document.containsKey(element.getKey())) {
                        BsonDocument currentOperatorDocument = (BsonDocument)element.getValue();
                        BsonDocument existingOperatorDocument = document.getDocument(element.getKey());
                        for (Map.Entry<String, BsonValue> currentOperationDocumentElements : currentOperatorDocument.entrySet()) {
                            existingOperatorDocument.append(currentOperationDocumentElements.getKey(), currentOperationDocumentElements.getValue());
                        }
                        continue;
                    }
                    document.append(element.getKey(), element.getValue());
                }
            }
            return document;
        }

        public boolean equals(Object o2) {
            if (this == o2) {
                return true;
            }
            if (o2 == null || this.getClass() != o2.getClass()) {
                return false;
            }
            CompositeUpdate that = (CompositeUpdate)o2;
            return this.updates != null ? this.updates.equals(that.updates) : that.updates == null;
        }

        public int hashCode() {
            return this.updates != null ? this.updates.hashCode() : 0;
        }

        public String toString() {
            return "Updates{updates=" + this.updates + '}';
        }
    }

    private static class PullAllUpdate<TItem>
    implements Bson {
        private final String fieldName;
        private final List<TItem> values;

        PullAllUpdate(String fieldName, List<TItem> values) {
            this.fieldName = Assertions.notNull("fieldName", fieldName);
            this.values = Assertions.notNull("values", values);
        }

        @Override
        public <TDocument> BsonDocument toBsonDocument(Class<TDocument> tDocumentClass, CodecRegistry codecRegistry) {
            BsonDocumentWriter writer = new BsonDocumentWriter(new BsonDocument());
            writer.writeStartDocument();
            writer.writeName("$pullAll");
            writer.writeStartDocument();
            writer.writeName(this.fieldName);
            writer.writeStartArray();
            for (TItem value : this.values) {
                BuildersHelper.encodeValue(writer, value, codecRegistry);
            }
            writer.writeEndArray();
            writer.writeEndDocument();
            writer.writeEndDocument();
            return writer.getDocument();
        }

        public boolean equals(Object o2) {
            if (this == o2) {
                return true;
            }
            if (o2 == null || this.getClass() != o2.getClass()) {
                return false;
            }
            PullAllUpdate that = (PullAllUpdate)o2;
            if (!this.fieldName.equals(that.fieldName)) {
                return false;
            }
            return this.values.equals(that.values);
        }

        public int hashCode() {
            int result = this.fieldName.hashCode();
            result = 31 * result + this.values.hashCode();
            return result;
        }

        public String toString() {
            return "Update{fieldName='" + this.fieldName + '\'' + ", operator='$pullAll', value=" + this.values + '}';
        }
    }

    private static class PushUpdate<TItem>
    extends WithEachUpdate<TItem> {
        private final PushOptions options;

        PushUpdate(String fieldName, List<TItem> values, PushOptions options) {
            super(fieldName, values, "$push");
            this.options = Assertions.notNull("options", options);
        }

        @Override
        protected <TDocument> void writeAdditionalFields(BsonDocumentWriter writer, Class<TDocument> tDocumentClass, CodecRegistry codecRegistry) {
            Integer sort;
            Integer slice;
            Integer position = this.options.getPosition();
            if (position != null) {
                writer.writeInt32("$position", position);
            }
            if ((slice = this.options.getSlice()) != null) {
                writer.writeInt32("$slice", slice);
            }
            if ((sort = this.options.getSort()) != null) {
                writer.writeInt32("$sort", sort);
            } else {
                Bson sortDocument = this.options.getSortDocument();
                if (sortDocument != null) {
                    writer.writeName("$sort");
                    BuildersHelper.encodeValue(writer, sortDocument, codecRegistry);
                }
            }
        }

        @Override
        public boolean equals(Object o2) {
            if (this == o2) {
                return true;
            }
            if (o2 == null || this.getClass() != o2.getClass()) {
                return false;
            }
            if (!super.equals(o2)) {
                return false;
            }
            PushUpdate that = (PushUpdate)o2;
            return this.options.equals(that.options);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + this.options.hashCode();
            return result;
        }

        @Override
        protected String additionalFieldsToString() {
            return ", options=" + this.options;
        }
    }

    private static class WithEachUpdate<TItem>
    implements Bson {
        private final String fieldName;
        private final List<TItem> values;
        private final String operator;

        WithEachUpdate(String fieldName, List<TItem> values, String operator) {
            this.fieldName = Assertions.notNull("fieldName", fieldName);
            this.values = Assertions.notNull("values", values);
            this.operator = operator;
        }

        @Override
        public <TDocument> BsonDocument toBsonDocument(Class<TDocument> tDocumentClass, CodecRegistry codecRegistry) {
            BsonDocumentWriter writer = new BsonDocumentWriter(new BsonDocument());
            writer.writeStartDocument();
            writer.writeName(this.operator);
            writer.writeStartDocument();
            writer.writeName(this.fieldName);
            writer.writeStartDocument();
            writer.writeStartArray("$each");
            for (TItem value : this.values) {
                BuildersHelper.encodeValue(writer, value, codecRegistry);
            }
            writer.writeEndArray();
            this.writeAdditionalFields(writer, tDocumentClass, codecRegistry);
            writer.writeEndDocument();
            writer.writeEndDocument();
            writer.writeEndDocument();
            return writer.getDocument();
        }

        protected <TDocument> void writeAdditionalFields(BsonDocumentWriter writer, Class<TDocument> tDocumentClass, CodecRegistry codecRegistry) {
        }

        protected String additionalFieldsToString() {
            return "";
        }

        public boolean equals(Object o2) {
            if (this == o2) {
                return true;
            }
            if (o2 == null || this.getClass() != o2.getClass()) {
                return false;
            }
            WithEachUpdate that = (WithEachUpdate)o2;
            if (!this.fieldName.equals(that.fieldName)) {
                return false;
            }
            if (!this.values.equals(that.values)) {
                return false;
            }
            return this.operator != null ? this.operator.equals(that.operator) : that.operator == null;
        }

        public int hashCode() {
            int result = this.fieldName.hashCode();
            result = 31 * result + this.values.hashCode();
            result = 31 * result + (this.operator != null ? this.operator.hashCode() : 0);
            return result;
        }

        public String toString() {
            return "Each Update{fieldName='" + this.fieldName + '\'' + ", operator='" + this.operator + '\'' + ", values=" + this.values + this.additionalFieldsToString() + '}';
        }
    }

    private static class SimpleUpdate<TItem>
    implements Bson {
        private final String fieldName;
        private final TItem value;
        private final String operator;

        SimpleUpdate(String fieldName, TItem value, String operator) {
            this.fieldName = Assertions.notNull("fieldName", fieldName);
            this.value = value;
            this.operator = operator;
        }

        @Override
        public <TDocument> BsonDocument toBsonDocument(Class<TDocument> tDocumentClass, CodecRegistry codecRegistry) {
            BsonDocumentWriter writer = new BsonDocumentWriter(new BsonDocument());
            writer.writeStartDocument();
            writer.writeName(this.operator);
            writer.writeStartDocument();
            writer.writeName(this.fieldName);
            BuildersHelper.encodeValue(writer, this.value, codecRegistry);
            writer.writeEndDocument();
            writer.writeEndDocument();
            return writer.getDocument();
        }

        public boolean equals(Object o2) {
            if (this == o2) {
                return true;
            }
            if (o2 == null || this.getClass() != o2.getClass()) {
                return false;
            }
            SimpleUpdate that = (SimpleUpdate)o2;
            if (!this.fieldName.equals(that.fieldName)) {
                return false;
            }
            if (this.value != null ? !this.value.equals(that.value) : that.value != null) {
                return false;
            }
            return this.operator != null ? this.operator.equals(that.operator) : that.operator == null;
        }

        public int hashCode() {
            int result = this.fieldName.hashCode();
            result = 31 * result + (this.value != null ? this.value.hashCode() : 0);
            result = 31 * result + (this.operator != null ? this.operator.hashCode() : 0);
            return result;
        }

        public String toString() {
            return "Update{fieldName='" + this.fieldName + '\'' + ", operator='" + this.operator + '\'' + ", value=" + this.value + '}';
        }
    }

    private static class SimpleBsonKeyValue
    implements Bson {
        private final String fieldName;
        private final Bson value;

        SimpleBsonKeyValue(String fieldName, Bson value) {
            this.fieldName = Assertions.notNull("fieldName", fieldName);
            this.value = Assertions.notNull("value", value);
        }

        @Override
        public <TDocument> BsonDocument toBsonDocument(Class<TDocument> tDocumentClass, CodecRegistry codecRegistry) {
            BsonDocumentWriter writer = new BsonDocumentWriter(new BsonDocument());
            writer.writeStartDocument();
            writer.writeName(this.fieldName);
            BuildersHelper.encodeValue(writer, this.value, codecRegistry);
            writer.writeEndDocument();
            return writer.getDocument();
        }

        public boolean equals(Object o2) {
            if (this == o2) {
                return true;
            }
            if (o2 == null || this.getClass() != o2.getClass()) {
                return false;
            }
            SimpleBsonKeyValue that = (SimpleBsonKeyValue)o2;
            if (!this.fieldName.equals(that.fieldName)) {
                return false;
            }
            return this.value.equals(that.value);
        }

        public int hashCode() {
            int result = this.fieldName.hashCode();
            result = 31 * result + this.value.hashCode();
            return result;
        }

        public String toString() {
            return "SimpleBsonKeyValue{fieldName='" + this.fieldName + '\'' + ", value=" + this.value + '}';
        }
    }
}

