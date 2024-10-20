/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.assertions.Assertions;
import com.mongodb.client.model.BuildersHelper;
import com.mongodb.client.model.TextSearchOptions;
import com.mongodb.client.model.geojson.Geometry;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.lang.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.bson.BsonArray;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonDocumentWriter;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonRegularExpression;
import org.bson.BsonString;
import org.bson.BsonType;
import org.bson.BsonValue;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

public final class Filters {
    private Filters() {
    }

    public static <TItem> Bson eq(@Nullable TItem value) {
        return Filters.eq("_id", value);
    }

    public static <TItem> Bson eq(String fieldName, @Nullable TItem value) {
        return new SimpleEncodingFilter<TItem>(fieldName, value);
    }

    public static <TItem> Bson ne(String fieldName, @Nullable TItem value) {
        return new OperatorFilter<TItem>("$ne", fieldName, value);
    }

    public static <TItem> Bson gt(String fieldName, TItem value) {
        return new OperatorFilter<TItem>("$gt", fieldName, value);
    }

    public static <TItem> Bson lt(String fieldName, TItem value) {
        return new OperatorFilter<TItem>("$lt", fieldName, value);
    }

    public static <TItem> Bson gte(String fieldName, TItem value) {
        return new OperatorFilter<TItem>("$gte", fieldName, value);
    }

    public static <TItem> Bson lte(String fieldName, TItem value) {
        return new OperatorFilter<TItem>("$lte", fieldName, value);
    }

    public static <TItem> Bson in(String fieldName, TItem ... values) {
        return Filters.in(fieldName, Arrays.asList(values));
    }

    public static <TItem> Bson in(String fieldName, Iterable<TItem> values) {
        return new IterableOperatorFilter<TItem>(fieldName, "$in", values);
    }

    public static <TItem> Bson nin(String fieldName, TItem ... values) {
        return Filters.nin(fieldName, Arrays.asList(values));
    }

    public static <TItem> Bson nin(String fieldName, Iterable<TItem> values) {
        return new IterableOperatorFilter<TItem>(fieldName, "$nin", values);
    }

    public static Bson and(Iterable<Bson> filters) {
        return new AndFilter(filters);
    }

    public static Bson and(Bson ... filters) {
        return Filters.and(Arrays.asList(filters));
    }

    public static Bson or(Iterable<Bson> filters) {
        return new OrNorFilter(OrNorFilter.Operator.OR, filters);
    }

    public static Bson or(Bson ... filters) {
        return Filters.or(Arrays.asList(filters));
    }

    public static Bson not(Bson filter) {
        return new NotFilter(filter);
    }

    public static Bson nor(Bson ... filters) {
        return Filters.nor(Arrays.asList(filters));
    }

    public static Bson nor(Iterable<Bson> filters) {
        return new OrNorFilter(OrNorFilter.Operator.NOR, filters);
    }

    public static Bson exists(String fieldName) {
        return Filters.exists(fieldName, true);
    }

    public static Bson exists(String fieldName, boolean exists) {
        return new OperatorFilter<BsonBoolean>("$exists", fieldName, BsonBoolean.valueOf(exists));
    }

    public static Bson type(String fieldName, BsonType type) {
        return new OperatorFilter<BsonInt32>("$type", fieldName, new BsonInt32(type.getValue()));
    }

    public static Bson type(String fieldName, String type) {
        return new OperatorFilter<BsonString>("$type", fieldName, new BsonString(type));
    }

    public static Bson mod(String fieldName, long divisor, long remainder) {
        return new OperatorFilter<BsonArray>("$mod", fieldName, new BsonArray(Arrays.asList(new BsonInt64(divisor), new BsonInt64(remainder))));
    }

    public static Bson regex(String fieldName, String pattern) {
        return Filters.regex(fieldName, pattern, null);
    }

    public static Bson regex(String fieldName, String pattern, @Nullable String options) {
        Assertions.notNull("pattern", pattern);
        return new SimpleFilter(fieldName, new BsonRegularExpression(pattern, options));
    }

    public static Bson regex(String fieldName, Pattern pattern) {
        Assertions.notNull("pattern", pattern);
        return new SimpleEncodingFilter<Pattern>(fieldName, pattern);
    }

    public static Bson text(String search) {
        Assertions.notNull("search", search);
        return Filters.text(search, new TextSearchOptions());
    }

    @Deprecated
    public static Bson text(String search, String language) {
        Assertions.notNull("search", search);
        return Filters.text(search, new TextSearchOptions().language(language));
    }

    public static Bson text(String search, TextSearchOptions textSearchOptions) {
        Assertions.notNull("search", search);
        Assertions.notNull("textSearchOptions", textSearchOptions);
        return new TextFilter(search, textSearchOptions);
    }

    public static Bson where(String javaScriptExpression) {
        Assertions.notNull("javaScriptExpression", javaScriptExpression);
        return new BsonDocument("$where", new BsonString(javaScriptExpression));
    }

    public static <TExpression> Bson expr(TExpression expression) {
        return new SimpleEncodingFilter<TExpression>("$expr", expression);
    }

    public static <TItem> Bson all(String fieldName, TItem ... values) {
        return Filters.all(fieldName, Arrays.asList(values));
    }

    public static <TItem> Bson all(String fieldName, Iterable<TItem> values) {
        return new IterableOperatorFilter<TItem>(fieldName, "$all", values);
    }

    public static Bson elemMatch(final String fieldName, final Bson filter) {
        return new Bson(){

            @Override
            public <TDocument> BsonDocument toBsonDocument(Class<TDocument> documentClass, CodecRegistry codecRegistry) {
                return new BsonDocument(fieldName, new BsonDocument("$elemMatch", filter.toBsonDocument(documentClass, codecRegistry)));
            }
        };
    }

    public static Bson size(String fieldName, int size) {
        return new OperatorFilter<Integer>("$size", fieldName, size);
    }

    public static Bson bitsAllClear(String fieldName, long bitmask) {
        return new OperatorFilter<Long>("$bitsAllClear", fieldName, bitmask);
    }

    public static Bson bitsAllSet(String fieldName, long bitmask) {
        return new OperatorFilter<Long>("$bitsAllSet", fieldName, bitmask);
    }

    public static Bson bitsAnyClear(String fieldName, long bitmask) {
        return new OperatorFilter<Long>("$bitsAnyClear", fieldName, bitmask);
    }

    public static Bson bitsAnySet(String fieldName, long bitmask) {
        return new OperatorFilter<Long>("$bitsAnySet", fieldName, bitmask);
    }

    public static Bson geoWithin(String fieldName, Geometry geometry) {
        return new GeometryOperatorFilter<Geometry>("$geoWithin", fieldName, geometry);
    }

    public static Bson geoWithin(String fieldName, Bson geometry) {
        return new GeometryOperatorFilter<Bson>("$geoWithin", fieldName, geometry);
    }

    public static Bson geoWithinBox(String fieldName, double lowerLeftX, double lowerLeftY, double upperRightX, double upperRightY) {
        BsonDocument box = new BsonDocument("$box", new BsonArray(Arrays.asList(new BsonArray(Arrays.asList(new BsonDouble(lowerLeftX), new BsonDouble(lowerLeftY))), new BsonArray(Arrays.asList(new BsonDouble(upperRightX), new BsonDouble(upperRightY))))));
        return new OperatorFilter<BsonDocument>("$geoWithin", fieldName, box);
    }

    public static Bson geoWithinPolygon(String fieldName, List<List<Double>> points) {
        BsonArray pointsArray = new BsonArray();
        for (List<Double> point : points) {
            pointsArray.add(new BsonArray(Arrays.asList(new BsonDouble(point.get(0)), new BsonDouble(point.get(1)))));
        }
        BsonDocument polygon = new BsonDocument("$polygon", pointsArray);
        return new OperatorFilter<BsonDocument>("$geoWithin", fieldName, polygon);
    }

    public static Bson geoWithinCenter(String fieldName, double x2, double y2, double radius) {
        BsonDocument center = new BsonDocument("$center", new BsonArray(Arrays.asList(new BsonArray(Arrays.asList(new BsonDouble(x2), new BsonDouble(y2))), new BsonDouble(radius))));
        return new OperatorFilter<BsonDocument>("$geoWithin", fieldName, center);
    }

    public static Bson geoWithinCenterSphere(String fieldName, double x2, double y2, double radius) {
        BsonDocument centerSphere = new BsonDocument("$centerSphere", new BsonArray(Arrays.asList(new BsonArray(Arrays.asList(new BsonDouble(x2), new BsonDouble(y2))), new BsonDouble(radius))));
        return new OperatorFilter<BsonDocument>("$geoWithin", fieldName, centerSphere);
    }

    public static Bson geoIntersects(String fieldName, Bson geometry) {
        return new GeometryOperatorFilter<Bson>("$geoIntersects", fieldName, geometry);
    }

    public static Bson geoIntersects(String fieldName, Geometry geometry) {
        return new GeometryOperatorFilter<Geometry>("$geoIntersects", fieldName, geometry);
    }

    public static Bson near(String fieldName, Point geometry, @Nullable Double maxDistance, @Nullable Double minDistance) {
        return new GeometryOperatorFilter<Point>("$near", fieldName, geometry, maxDistance, minDistance);
    }

    public static Bson near(String fieldName, Bson geometry, @Nullable Double maxDistance, @Nullable Double minDistance) {
        return new GeometryOperatorFilter<Bson>("$near", fieldName, geometry, maxDistance, minDistance);
    }

    public static Bson near(String fieldName, double x2, double y2, @Nullable Double maxDistance, @Nullable Double minDistance) {
        return Filters.createNearFilterDocument(fieldName, x2, y2, maxDistance, minDistance, "$near");
    }

    public static Bson nearSphere(String fieldName, Point geometry, @Nullable Double maxDistance, @Nullable Double minDistance) {
        return new GeometryOperatorFilter<Point>("$nearSphere", fieldName, geometry, maxDistance, minDistance);
    }

    public static Bson nearSphere(String fieldName, Bson geometry, @Nullable Double maxDistance, @Nullable Double minDistance) {
        return new GeometryOperatorFilter<Bson>("$nearSphere", fieldName, geometry, maxDistance, minDistance);
    }

    public static Bson nearSphere(String fieldName, double x2, double y2, @Nullable Double maxDistance, @Nullable Double minDistance) {
        return Filters.createNearFilterDocument(fieldName, x2, y2, maxDistance, minDistance, "$nearSphere");
    }

    public static Bson jsonSchema(Bson schema) {
        return new SimpleEncodingFilter<Bson>("$jsonSchema", schema);
    }

    private static Bson createNearFilterDocument(String fieldName, double x2, double y2, @Nullable Double maxDistance, @Nullable Double minDistance, String operator) {
        BsonDocument nearFilter = new BsonDocument(operator, new BsonArray(Arrays.asList(new BsonDouble(x2), new BsonDouble(y2))));
        if (maxDistance != null) {
            nearFilter.append("$maxDistance", new BsonDouble(maxDistance));
        }
        if (minDistance != null) {
            nearFilter.append("$minDistance", new BsonDouble(minDistance));
        }
        return new BsonDocument(fieldName, nearFilter);
    }

    private static String operatorFilterToString(String fieldName, String operator, Object value) {
        return "Operator Filter{fieldName='" + fieldName + '\'' + ", operator='" + operator + '\'' + ", value=" + value + '}';
    }

    private static class TextFilter
    implements Bson {
        private final String search;
        private final TextSearchOptions textSearchOptions;

        TextFilter(String search, TextSearchOptions textSearchOptions) {
            this.search = search;
            this.textSearchOptions = textSearchOptions;
        }

        @Override
        public <TDocument> BsonDocument toBsonDocument(Class<TDocument> documentClass, CodecRegistry codecRegistry) {
            Boolean diacriticSensitive;
            Boolean caseSensitive;
            BsonDocument searchDocument = new BsonDocument("$search", new BsonString(this.search));
            String language = this.textSearchOptions.getLanguage();
            if (language != null) {
                searchDocument.put("$language", new BsonString(language));
            }
            if ((caseSensitive = this.textSearchOptions.getCaseSensitive()) != null) {
                searchDocument.put("$caseSensitive", BsonBoolean.valueOf(caseSensitive));
            }
            if ((diacriticSensitive = this.textSearchOptions.getDiacriticSensitive()) != null) {
                searchDocument.put("$diacriticSensitive", BsonBoolean.valueOf(diacriticSensitive));
            }
            return new BsonDocument("$text", searchDocument);
        }

        public boolean equals(Object o2) {
            if (this == o2) {
                return true;
            }
            if (o2 == null || this.getClass() != o2.getClass()) {
                return false;
            }
            TextFilter that = (TextFilter)o2;
            if (this.search != null ? !this.search.equals(that.search) : that.search != null) {
                return false;
            }
            return this.textSearchOptions != null ? this.textSearchOptions.equals(that.textSearchOptions) : that.textSearchOptions == null;
        }

        public int hashCode() {
            int result = this.search != null ? this.search.hashCode() : 0;
            result = 31 * result + (this.textSearchOptions != null ? this.textSearchOptions.hashCode() : 0);
            return result;
        }

        public String toString() {
            return "Text Filter{search='" + this.search + '\'' + ", textSearchOptions=" + this.textSearchOptions + '}';
        }
    }

    private static class GeometryOperatorFilter<TItem>
    implements Bson {
        private final String operatorName;
        private final String fieldName;
        private final TItem geometry;
        private final Double maxDistance;
        private final Double minDistance;

        GeometryOperatorFilter(String operatorName, String fieldName, TItem geometry) {
            this(operatorName, fieldName, geometry, null, null);
        }

        GeometryOperatorFilter(String operatorName, String fieldName, TItem geometry, @Nullable Double maxDistance, @Nullable Double minDistance) {
            this.operatorName = operatorName;
            this.fieldName = Assertions.notNull("fieldName", fieldName);
            this.geometry = Assertions.notNull("geometry", geometry);
            this.maxDistance = maxDistance;
            this.minDistance = minDistance;
        }

        @Override
        public <TDocument> BsonDocument toBsonDocument(Class<TDocument> documentClass, CodecRegistry codecRegistry) {
            BsonDocumentWriter writer = new BsonDocumentWriter(new BsonDocument());
            writer.writeStartDocument();
            writer.writeName(this.fieldName);
            writer.writeStartDocument();
            writer.writeName(this.operatorName);
            writer.writeStartDocument();
            writer.writeName("$geometry");
            BuildersHelper.encodeValue(writer, this.geometry, codecRegistry);
            if (this.maxDistance != null) {
                writer.writeDouble("$maxDistance", this.maxDistance);
            }
            if (this.minDistance != null) {
                writer.writeDouble("$minDistance", this.minDistance);
            }
            writer.writeEndDocument();
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
            GeometryOperatorFilter that = (GeometryOperatorFilter)o2;
            if (this.operatorName != null ? !this.operatorName.equals(that.operatorName) : that.operatorName != null) {
                return false;
            }
            if (!this.fieldName.equals(that.fieldName)) {
                return false;
            }
            if (!this.geometry.equals(that.geometry)) {
                return false;
            }
            if (this.maxDistance != null ? !this.maxDistance.equals(that.maxDistance) : that.maxDistance != null) {
                return false;
            }
            return this.minDistance != null ? this.minDistance.equals(that.minDistance) : that.minDistance == null;
        }

        public int hashCode() {
            int result = this.operatorName != null ? this.operatorName.hashCode() : 0;
            result = 31 * result + this.fieldName.hashCode();
            result = 31 * result + this.geometry.hashCode();
            result = 31 * result + (this.maxDistance != null ? this.maxDistance.hashCode() : 0);
            result = 31 * result + (this.minDistance != null ? this.minDistance.hashCode() : 0);
            return result;
        }

        public String toString() {
            return "Geometry Operator Filter{fieldName='" + this.fieldName + '\'' + ", operator='" + this.operatorName + '\'' + ", geometry=" + this.geometry + ", maxDistance=" + this.maxDistance + ", minDistance=" + this.minDistance + '}';
        }
    }

    private static class NotFilter
    implements Bson {
        private static final Set<String> DBREF_KEYS = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList("$ref", "$id")));
        private static final Set<String> DBREF_KEYS_WITH_DB = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList("$ref", "$id", "$db")));
        private final Bson filter;

        NotFilter(Bson filter) {
            this.filter = Assertions.notNull("filter", filter);
        }

        @Override
        public <TDocument> BsonDocument toBsonDocument(Class<TDocument> documentClass, CodecRegistry codecRegistry) {
            BsonDocument filterDocument = this.filter.toBsonDocument(documentClass, codecRegistry);
            if (filterDocument.size() == 1) {
                Map.Entry<String, BsonValue> entry = filterDocument.entrySet().iterator().next();
                return this.createFilter(entry.getKey(), entry.getValue());
            }
            BsonArray values = new BsonArray();
            for (Map.Entry<String, BsonValue> docs : filterDocument.entrySet()) {
                values.add(new BsonDocument(docs.getKey(), docs.getValue()));
            }
            return this.createFilter("$and", values);
        }

        private boolean containsOperator(BsonDocument value) {
            Set<String> keys = value.keySet();
            if (keys.equals(DBREF_KEYS) || keys.equals(DBREF_KEYS_WITH_DB)) {
                return false;
            }
            for (String key : keys) {
                if (!key.startsWith("$")) continue;
                return true;
            }
            return false;
        }

        private BsonDocument createFilter(String fieldName, BsonValue value) {
            if (fieldName.startsWith("$")) {
                return new BsonDocument("$not", new BsonDocument(fieldName, value));
            }
            if (value.isDocument() && this.containsOperator(value.asDocument()) || value.isRegularExpression()) {
                return new BsonDocument(fieldName, new BsonDocument("$not", value));
            }
            return new BsonDocument(fieldName, new BsonDocument("$not", new BsonDocument("$eq", value)));
        }

        public boolean equals(Object o2) {
            if (this == o2) {
                return true;
            }
            if (o2 == null || this.getClass() != o2.getClass()) {
                return false;
            }
            NotFilter notFilter = (NotFilter)o2;
            return this.filter.equals(notFilter.filter);
        }

        public int hashCode() {
            return this.filter.hashCode();
        }

        public String toString() {
            return "Not Filter{filter=" + this.filter + '}';
        }
    }

    private static class SimpleEncodingFilter<TItem>
    implements Bson {
        private final String fieldName;
        private final TItem value;

        SimpleEncodingFilter(String fieldName, TItem value) {
            this.fieldName = Assertions.notNull("fieldName", fieldName);
            this.value = value;
        }

        @Override
        public <TDocument> BsonDocument toBsonDocument(Class<TDocument> documentClass, CodecRegistry codecRegistry) {
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
            SimpleEncodingFilter that = (SimpleEncodingFilter)o2;
            if (!this.fieldName.equals(that.fieldName)) {
                return false;
            }
            return this.value != null ? this.value.equals(that.value) : that.value == null;
        }

        public int hashCode() {
            int result = this.fieldName.hashCode();
            result = 31 * result + (this.value != null ? this.value.hashCode() : 0);
            return result;
        }

        public String toString() {
            return "Filter{fieldName='" + this.fieldName + '\'' + ", value=" + this.value + '}';
        }
    }

    private static class IterableOperatorFilter<TItem>
    implements Bson {
        private final String fieldName;
        private final String operatorName;
        private final Iterable<TItem> values;

        IterableOperatorFilter(String fieldName, String operatorName, Iterable<TItem> values) {
            this.fieldName = Assertions.notNull("fieldName", fieldName);
            this.operatorName = Assertions.notNull("operatorName", operatorName);
            this.values = Assertions.notNull("values", values);
        }

        @Override
        public <TDocument> BsonDocument toBsonDocument(Class<TDocument> documentClass, CodecRegistry codecRegistry) {
            BsonDocumentWriter writer = new BsonDocumentWriter(new BsonDocument());
            writer.writeStartDocument();
            writer.writeName(this.fieldName);
            writer.writeStartDocument();
            writer.writeName(this.operatorName);
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
            IterableOperatorFilter that = (IterableOperatorFilter)o2;
            if (!this.fieldName.equals(that.fieldName)) {
                return false;
            }
            if (!this.operatorName.equals(that.operatorName)) {
                return false;
            }
            return this.values.equals(that.values);
        }

        public int hashCode() {
            int result = this.fieldName.hashCode();
            result = 31 * result + this.operatorName.hashCode();
            result = 31 * result + this.values.hashCode();
            return result;
        }

        public String toString() {
            return Filters.operatorFilterToString(this.fieldName, this.operatorName, this.values);
        }
    }

    private static class OrNorFilter
    implements Bson {
        private final Operator operator;
        private final Iterable<Bson> filters;

        OrNorFilter(Operator operator, Iterable<Bson> filters) {
            this.operator = Assertions.notNull("operator", operator);
            this.filters = Assertions.notNull("filters", filters);
        }

        @Override
        public <TDocument> BsonDocument toBsonDocument(Class<TDocument> documentClass, CodecRegistry codecRegistry) {
            BsonDocument orRenderable = new BsonDocument();
            BsonArray filtersArray = new BsonArray();
            for (Bson filter : this.filters) {
                filtersArray.add(filter.toBsonDocument(documentClass, codecRegistry));
            }
            orRenderable.put(this.operator.name, filtersArray);
            return orRenderable;
        }

        public boolean equals(Object o2) {
            if (this == o2) {
                return true;
            }
            if (o2 == null || this.getClass() != o2.getClass()) {
                return false;
            }
            OrNorFilter that = (OrNorFilter)o2;
            if (this.operator != that.operator) {
                return false;
            }
            return this.filters.equals(that.filters);
        }

        public int hashCode() {
            int result = this.operator.hashCode();
            result = 31 * result + this.filters.hashCode();
            return result;
        }

        public String toString() {
            return this.operator.toStringName + " Filter{filters=" + this.filters + '}';
        }

        private static enum Operator {
            OR("$or", "Or"),
            NOR("$nor", "Nor");

            private final String name;
            private final String toStringName;

            private Operator(String name, String toStringName) {
                this.name = name;
                this.toStringName = toStringName;
            }
        }
    }

    private static class AndFilter
    implements Bson {
        private final Iterable<Bson> filters;

        AndFilter(Iterable<Bson> filters) {
            this.filters = Assertions.notNull("filters", filters);
        }

        @Override
        public <TDocument> BsonDocument toBsonDocument(Class<TDocument> documentClass, CodecRegistry codecRegistry) {
            BsonDocument andRenderable = new BsonDocument();
            for (Bson filter : this.filters) {
                BsonDocument renderedRenderable = filter.toBsonDocument(documentClass, codecRegistry);
                for (Map.Entry<String, BsonValue> element : renderedRenderable.entrySet()) {
                    this.addClause(andRenderable, element);
                }
            }
            if (andRenderable.isEmpty()) {
                andRenderable.append("$and", new BsonArray());
            }
            return andRenderable;
        }

        private void addClause(BsonDocument document, Map.Entry<String, BsonValue> clause) {
            if (clause.getKey().equals("$and")) {
                for (BsonValue value : clause.getValue().asArray()) {
                    for (Map.Entry<String, BsonValue> element : value.asDocument().entrySet()) {
                        this.addClause(document, element);
                    }
                }
            } else if (document.size() == 1 && document.keySet().iterator().next().equals("$and")) {
                document.get("$and").asArray().add(new BsonDocument(clause.getKey(), clause.getValue()));
            } else if (document.containsKey(clause.getKey())) {
                if (document.get(clause.getKey()).isDocument() && clause.getValue().isDocument()) {
                    BsonDocument existingClauseValue = document.get(clause.getKey()).asDocument();
                    BsonDocument clauseValue = clause.getValue().asDocument();
                    if (this.keysIntersect(clauseValue, existingClauseValue)) {
                        this.promoteRenderableToDollarForm(document, clause);
                    } else {
                        existingClauseValue.putAll(clauseValue);
                    }
                } else {
                    this.promoteRenderableToDollarForm(document, clause);
                }
            } else {
                document.append(clause.getKey(), clause.getValue());
            }
        }

        private boolean keysIntersect(BsonDocument first, BsonDocument second) {
            for (String name : first.keySet()) {
                if (!second.containsKey(name)) continue;
                return true;
            }
            return false;
        }

        private void promoteRenderableToDollarForm(BsonDocument document, Map.Entry<String, BsonValue> clause) {
            BsonArray clauses = new BsonArray();
            for (Map.Entry<String, BsonValue> queryElement : document.entrySet()) {
                clauses.add(new BsonDocument(queryElement.getKey(), queryElement.getValue()));
            }
            clauses.add(new BsonDocument(clause.getKey(), clause.getValue()));
            document.clear();
            document.put("$and", clauses);
        }

        public boolean equals(Object o2) {
            if (this == o2) {
                return true;
            }
            if (o2 == null || this.getClass() != o2.getClass()) {
                return false;
            }
            AndFilter andFilter = (AndFilter)o2;
            return this.filters.equals(andFilter.filters);
        }

        public int hashCode() {
            return this.filters.hashCode();
        }

        public String toString() {
            return "And Filter{filters=" + this.filters + '}';
        }
    }

    private static final class OperatorFilter<TItem>
    implements Bson {
        private final String operatorName;
        private final String fieldName;
        private final TItem value;

        OperatorFilter(String operatorName, String fieldName, TItem value) {
            this.operatorName = Assertions.notNull("operatorName", operatorName);
            this.fieldName = Assertions.notNull("fieldName", fieldName);
            this.value = value;
        }

        @Override
        public <TDocument> BsonDocument toBsonDocument(Class<TDocument> documentClass, CodecRegistry codecRegistry) {
            BsonDocumentWriter writer = new BsonDocumentWriter(new BsonDocument());
            writer.writeStartDocument();
            writer.writeName(this.fieldName);
            writer.writeStartDocument();
            writer.writeName(this.operatorName);
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
            OperatorFilter that = (OperatorFilter)o2;
            if (!this.operatorName.equals(that.operatorName)) {
                return false;
            }
            if (!this.fieldName.equals(that.fieldName)) {
                return false;
            }
            return this.value != null ? this.value.equals(that.value) : that.value == null;
        }

        public int hashCode() {
            int result = this.operatorName.hashCode();
            result = 31 * result + this.fieldName.hashCode();
            result = 31 * result + (this.value != null ? this.value.hashCode() : 0);
            return result;
        }

        public String toString() {
            return Filters.operatorFilterToString(this.fieldName, this.operatorName, this.value);
        }
    }

    private static final class SimpleFilter
    implements Bson {
        private final String fieldName;
        private final BsonValue value;

        private SimpleFilter(String fieldName, BsonValue value) {
            this.fieldName = Assertions.notNull("fieldName", fieldName);
            this.value = Assertions.notNull("value", value);
        }

        @Override
        public <TDocument> BsonDocument toBsonDocument(Class<TDocument> documentClass, CodecRegistry codecRegistry) {
            return new BsonDocument(this.fieldName, this.value);
        }

        public boolean equals(Object o2) {
            if (this == o2) {
                return true;
            }
            if (o2 == null || this.getClass() != o2.getClass()) {
                return false;
            }
            SimpleFilter that = (SimpleFilter)o2;
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
            return Filters.operatorFilterToString(this.fieldName, "$eq", this.value);
        }
    }
}

