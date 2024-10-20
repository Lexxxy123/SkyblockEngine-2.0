/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.MongoNamespace;
import com.mongodb.client.model.BsonField;
import com.mongodb.client.model.BucketAutoOptions;
import com.mongodb.client.model.BucketGranularity;
import com.mongodb.client.model.BucketOptions;
import com.mongodb.client.model.BuildersHelper;
import com.mongodb.client.model.Facet;
import com.mongodb.client.model.Field;
import com.mongodb.client.model.GraphLookupOptions;
import com.mongodb.client.model.MergeOptions;
import com.mongodb.client.model.UnwindOptions;
import com.mongodb.client.model.Variable;
import com.mongodb.lang.Nullable;
import java.util.Arrays;
import java.util.List;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonDocumentWriter;
import org.bson.BsonInt32;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.assertions.Assertions;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

public final class Aggregates {
    public static Bson addFields(Field<?> ... fields) {
        return Aggregates.addFields(Arrays.asList(fields));
    }

    public static Bson addFields(List<Field<?>> fields) {
        return new AddFieldsStage(fields);
    }

    public static <TExpression, Boundary> Bson bucket(TExpression groupBy, List<Boundary> boundaries) {
        return Aggregates.bucket(groupBy, boundaries, new BucketOptions());
    }

    public static <TExpression, TBoundary> Bson bucket(TExpression groupBy, List<TBoundary> boundaries, BucketOptions options) {
        return new BucketStage<TExpression, TBoundary>(groupBy, boundaries, options);
    }

    public static <TExpression> Bson bucketAuto(TExpression groupBy, int buckets) {
        return Aggregates.bucketAuto(groupBy, buckets, new BucketAutoOptions());
    }

    public static <TExpression> Bson bucketAuto(TExpression groupBy, int buckets, BucketAutoOptions options) {
        return new BucketAutoStage<TExpression>(groupBy, buckets, options);
    }

    public static Bson count() {
        return Aggregates.count("count");
    }

    public static Bson count(String field) {
        return new BsonDocument("$count", new BsonString(field));
    }

    public static Bson match(Bson filter) {
        return new SimplePipelineStage("$match", filter);
    }

    public static Bson project(Bson projection) {
        return new SimplePipelineStage("$project", projection);
    }

    public static Bson sort(Bson sort) {
        return new SimplePipelineStage("$sort", sort);
    }

    public static <TExpression> Bson sortByCount(TExpression filter) {
        return new SortByCountStage<TExpression>(filter);
    }

    public static Bson skip(int skip) {
        return new BsonDocument("$skip", new BsonInt32(skip));
    }

    public static Bson limit(int limit) {
        return new BsonDocument("$limit", new BsonInt32(limit));
    }

    public static Bson lookup(String from, String localField, String foreignField, String as2) {
        return new BsonDocument("$lookup", new BsonDocument("from", new BsonString(from)).append("localField", new BsonString(localField)).append("foreignField", new BsonString(foreignField)).append("as", new BsonString(as2)));
    }

    public static Bson lookup(String from, List<? extends Bson> pipeline, String as2) {
        return Aggregates.lookup(from, null, pipeline, as2);
    }

    public static <TExpression> Bson lookup(String from, @Nullable List<Variable<TExpression>> let, List<? extends Bson> pipeline, String as2) {
        return new LookupStage(from, let, pipeline, as2);
    }

    public static Bson facet(List<Facet> facets) {
        return new FacetStage(facets);
    }

    public static Bson facet(Facet ... facets) {
        return new FacetStage(Arrays.asList(facets));
    }

    public static <TExpression> Bson graphLookup(String from, TExpression startWith, String connectFromField, String connectToField, String as2) {
        return Aggregates.graphLookup(from, startWith, connectFromField, connectToField, as2, new GraphLookupOptions());
    }

    public static <TExpression> Bson graphLookup(String from, TExpression startWith, String connectFromField, String connectToField, String as2, GraphLookupOptions options) {
        Assertions.notNull("options", options);
        return new GraphLookupStage(from, startWith, connectFromField, connectToField, as2, options);
    }

    public static <TExpression> Bson group(@Nullable TExpression id, BsonField ... fieldAccumulators) {
        return Aggregates.group(id, Arrays.asList(fieldAccumulators));
    }

    public static <TExpression> Bson group(@Nullable TExpression id, List<BsonField> fieldAccumulators) {
        return new GroupStage<TExpression>(id, fieldAccumulators);
    }

    public static Bson unwind(String fieldName) {
        return new BsonDocument("$unwind", new BsonString(fieldName));
    }

    public static Bson unwind(String fieldName, UnwindOptions unwindOptions) {
        String includeArrayIndex;
        Assertions.notNull("unwindOptions", unwindOptions);
        BsonDocument options = new BsonDocument("path", new BsonString(fieldName));
        Boolean preserveNullAndEmptyArrays = unwindOptions.isPreserveNullAndEmptyArrays();
        if (preserveNullAndEmptyArrays != null) {
            options.append("preserveNullAndEmptyArrays", BsonBoolean.valueOf(preserveNullAndEmptyArrays));
        }
        if ((includeArrayIndex = unwindOptions.getIncludeArrayIndex()) != null) {
            options.append("includeArrayIndex", new BsonString(includeArrayIndex));
        }
        return new BsonDocument("$unwind", options);
    }

    public static Bson out(String collectionName) {
        return new BsonDocument("$out", new BsonString(collectionName));
    }

    public static Bson merge(String collectionName) {
        return Aggregates.merge(collectionName, new MergeOptions());
    }

    public static Bson merge(MongoNamespace namespace) {
        return Aggregates.merge(namespace, new MergeOptions());
    }

    public static Bson merge(String collectionName, MergeOptions options) {
        return new MergeStage(new BsonString(collectionName), options);
    }

    public static Bson merge(MongoNamespace namespace, MergeOptions options) {
        return new MergeStage(new BsonDocument("db", new BsonString(namespace.getDatabaseName())).append("coll", new BsonString(namespace.getCollectionName())), options);
    }

    public static <TExpression> Bson replaceRoot(TExpression value) {
        return new ReplaceStage<TExpression>(value);
    }

    public static <TExpression> Bson replaceWith(TExpression value) {
        return new ReplaceStage<TExpression>(value, true);
    }

    public static Bson sample(int size) {
        return new BsonDocument("$sample", new BsonDocument("size", new BsonInt32(size)));
    }

    static void writeBucketOutput(CodecRegistry codecRegistry, BsonDocumentWriter writer, @Nullable List<BsonField> output) {
        if (output != null) {
            writer.writeName("output");
            writer.writeStartDocument();
            for (BsonField field : output) {
                writer.writeName(field.getName());
                BuildersHelper.encodeValue(writer, field.getValue(), codecRegistry);
            }
            writer.writeEndDocument();
        }
    }

    private Aggregates() {
    }

    private static class MergeStage
    implements Bson {
        private final BsonValue intoValue;
        private final MergeOptions options;

        MergeStage(BsonValue intoValue, MergeOptions options) {
            this.intoValue = intoValue;
            this.options = options;
        }

        @Override
        public <TDocument> BsonDocument toBsonDocument(Class<TDocument> documentClass, CodecRegistry codecRegistry) {
            BsonDocumentWriter writer = new BsonDocumentWriter(new BsonDocument());
            writer.writeStartDocument();
            writer.writeStartDocument("$merge");
            writer.writeName("into");
            if (this.intoValue.isString()) {
                writer.writeString(this.intoValue.asString().getValue());
            } else {
                writer.writeStartDocument();
                writer.writeString("db", this.intoValue.asDocument().getString("db").getValue());
                writer.writeString("coll", this.intoValue.asDocument().getString("coll").getValue());
                writer.writeEndDocument();
            }
            if (this.options.getUniqueIdentifier() != null) {
                if (this.options.getUniqueIdentifier().size() == 1) {
                    writer.writeString("on", this.options.getUniqueIdentifier().get(0));
                } else {
                    writer.writeStartArray("on");
                    for (String string : this.options.getUniqueIdentifier()) {
                        writer.writeString(string);
                    }
                    writer.writeEndArray();
                }
            }
            if (this.options.getVariables() != null) {
                writer.writeStartDocument("let");
                for (Variable variable : this.options.getVariables()) {
                    writer.writeName(variable.getName());
                    BuildersHelper.encodeValue(writer, variable.getValue(), codecRegistry);
                }
                writer.writeEndDocument();
            }
            if (this.options.getWhenMatched() != null) {
                writer.writeName("whenMatched");
                switch (this.options.getWhenMatched()) {
                    case REPLACE: {
                        writer.writeString("replace");
                        break;
                    }
                    case KEEP_EXISTING: {
                        writer.writeString("keepExisting");
                        break;
                    }
                    case MERGE: {
                        writer.writeString("merge");
                        break;
                    }
                    case PIPELINE: {
                        writer.writeStartArray();
                        for (Bson bson : this.options.getWhenMatchedPipeline()) {
                            BuildersHelper.encodeValue(writer, bson, codecRegistry);
                        }
                        writer.writeEndArray();
                        break;
                    }
                    case FAIL: {
                        writer.writeString("fail");
                        break;
                    }
                    default: {
                        throw new UnsupportedOperationException("Unexpected value: " + (Object)((Object)this.options.getWhenMatched()));
                    }
                }
            }
            if (this.options.getWhenNotMatched() != null) {
                writer.writeName("whenNotMatched");
                switch (this.options.getWhenNotMatched()) {
                    case INSERT: {
                        writer.writeString("insert");
                        break;
                    }
                    case DISCARD: {
                        writer.writeString("discard");
                        break;
                    }
                    case FAIL: {
                        writer.writeString("fail");
                        break;
                    }
                    default: {
                        throw new UnsupportedOperationException("Unexpected value: " + (Object)((Object)this.options.getWhenNotMatched()));
                    }
                }
            }
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
            MergeStage that = (MergeStage)o2;
            if (!this.intoValue.equals(that.intoValue)) {
                return false;
            }
            return this.options.equals(that.options);
        }

        public int hashCode() {
            int result = this.intoValue.hashCode();
            result = 31 * result + this.options.hashCode();
            return result;
        }

        public String toString() {
            return "Stage{name='$merge', , into=" + this.intoValue + ", options=" + this.options + '}';
        }
    }

    private static class ReplaceStage<TExpression>
    implements Bson {
        private final TExpression value;
        private final boolean replaceWith;

        ReplaceStage(TExpression value) {
            this(value, false);
        }

        ReplaceStage(TExpression value, boolean replaceWith) {
            this.value = value;
            this.replaceWith = replaceWith;
        }

        @Override
        public <TDocument> BsonDocument toBsonDocument(Class<TDocument> tDocumentClass, CodecRegistry codecRegistry) {
            BsonDocumentWriter writer = new BsonDocumentWriter(new BsonDocument());
            writer.writeStartDocument();
            if (this.replaceWith) {
                writer.writeName("$replaceWith");
                BuildersHelper.encodeValue(writer, this.value, codecRegistry);
            } else {
                writer.writeName("$replaceRoot");
                writer.writeStartDocument();
                writer.writeName("newRoot");
                BuildersHelper.encodeValue(writer, this.value, codecRegistry);
                writer.writeEndDocument();
            }
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
            ReplaceStage that = (ReplaceStage)o2;
            return this.value != null ? this.value.equals(that.value) : that.value == null;
        }

        public int hashCode() {
            return this.value != null ? this.value.hashCode() : 0;
        }

        public String toString() {
            return "Stage{name='$replaceRoot', value=" + this.value + '}';
        }
    }

    private static class AddFieldsStage
    implements Bson {
        private final List<Field<?>> fields;

        AddFieldsStage(List<Field<?>> fields) {
            this.fields = fields;
        }

        @Override
        public <TDocument> BsonDocument toBsonDocument(Class<TDocument> tDocumentClass, CodecRegistry codecRegistry) {
            BsonDocumentWriter writer = new BsonDocumentWriter(new BsonDocument());
            writer.writeStartDocument();
            writer.writeName("$addFields");
            writer.writeStartDocument();
            for (Field<?> field : this.fields) {
                writer.writeName(field.getName());
                BuildersHelper.encodeValue(writer, field.getValue(), codecRegistry);
            }
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
            AddFieldsStage that = (AddFieldsStage)o2;
            return this.fields != null ? this.fields.equals(that.fields) : that.fields == null;
        }

        public int hashCode() {
            return this.fields != null ? this.fields.hashCode() : 0;
        }

        public String toString() {
            return "Stage{name='$addFields', fields=" + this.fields + '}';
        }
    }

    private static class FacetStage
    implements Bson {
        private final List<Facet> facets;

        FacetStage(List<Facet> facets) {
            this.facets = facets;
        }

        @Override
        public <TDocument> BsonDocument toBsonDocument(Class<TDocument> tDocumentClass, CodecRegistry codecRegistry) {
            BsonDocumentWriter writer = new BsonDocumentWriter(new BsonDocument());
            writer.writeStartDocument();
            writer.writeName("$facet");
            writer.writeStartDocument();
            for (Facet facet : this.facets) {
                writer.writeName(facet.getName());
                writer.writeStartArray();
                for (Bson bson : facet.getPipeline()) {
                    BuildersHelper.encodeValue(writer, bson, codecRegistry);
                }
                writer.writeEndArray();
            }
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
            FacetStage that = (FacetStage)o2;
            return this.facets != null ? this.facets.equals(that.facets) : that.facets == null;
        }

        public int hashCode() {
            return this.facets != null ? this.facets.hashCode() : 0;
        }

        public String toString() {
            return "Stage{name='$facet', facets=" + this.facets + '}';
        }
    }

    private static class SortByCountStage<TExpression>
    implements Bson {
        private final TExpression filter;

        SortByCountStage(TExpression filter) {
            this.filter = filter;
        }

        @Override
        public <TDocument> BsonDocument toBsonDocument(Class<TDocument> tDocumentClass, CodecRegistry codecRegistry) {
            BsonDocumentWriter writer = new BsonDocumentWriter(new BsonDocument());
            writer.writeStartDocument();
            writer.writeName("$sortByCount");
            BuildersHelper.encodeValue(writer, this.filter, codecRegistry);
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
            SortByCountStage that = (SortByCountStage)o2;
            return this.filter != null ? this.filter.equals(that.filter) : that.filter == null;
        }

        public int hashCode() {
            return this.filter != null ? this.filter.hashCode() : 0;
        }

        public String toString() {
            return "Stage{name='$sortByCount', id=" + this.filter + '}';
        }
    }

    private static class GroupStage<TExpression>
    implements Bson {
        private final TExpression id;
        private final List<BsonField> fieldAccumulators;

        GroupStage(TExpression id, List<BsonField> fieldAccumulators) {
            this.id = id;
            this.fieldAccumulators = fieldAccumulators;
        }

        @Override
        public <TDocument> BsonDocument toBsonDocument(Class<TDocument> tDocumentClass, CodecRegistry codecRegistry) {
            BsonDocumentWriter writer = new BsonDocumentWriter(new BsonDocument());
            writer.writeStartDocument();
            writer.writeStartDocument("$group");
            writer.writeName("_id");
            BuildersHelper.encodeValue(writer, this.id, codecRegistry);
            for (BsonField fieldAccumulator : this.fieldAccumulators) {
                writer.writeName(fieldAccumulator.getName());
                BuildersHelper.encodeValue(writer, fieldAccumulator.getValue(), codecRegistry);
            }
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
            GroupStage that = (GroupStage)o2;
            if (this.id != null ? !this.id.equals(that.id) : that.id != null) {
                return false;
            }
            return this.fieldAccumulators != null ? this.fieldAccumulators.equals(that.fieldAccumulators) : that.fieldAccumulators == null;
        }

        public int hashCode() {
            int result = this.id != null ? this.id.hashCode() : 0;
            result = 31 * result + (this.fieldAccumulators != null ? this.fieldAccumulators.hashCode() : 0);
            return result;
        }

        public String toString() {
            return "Stage{name='$group', id=" + this.id + ", fieldAccumulators=" + this.fieldAccumulators + '}';
        }
    }

    private static final class GraphLookupStage<TExpression>
    implements Bson {
        private final String from;
        private final TExpression startWith;
        private final String connectFromField;
        private final String connectToField;
        private final String as;
        private final GraphLookupOptions options;

        private GraphLookupStage(String from, TExpression startWith, String connectFromField, String connectToField, String as2, GraphLookupOptions options) {
            this.from = from;
            this.startWith = startWith;
            this.connectFromField = connectFromField;
            this.connectToField = connectToField;
            this.as = as2;
            this.options = options;
        }

        @Override
        public <TDocument> BsonDocument toBsonDocument(Class<TDocument> tDocumentClass, CodecRegistry codecRegistry) {
            Bson restrictSearchWithMatch;
            String depthField;
            BsonDocumentWriter writer = new BsonDocumentWriter(new BsonDocument());
            writer.writeStartDocument();
            writer.writeStartDocument("$graphLookup");
            writer.writeString("from", this.from);
            writer.writeName("startWith");
            BuildersHelper.encodeValue(writer, this.startWith, codecRegistry);
            writer.writeString("connectFromField", this.connectFromField);
            writer.writeString("connectToField", this.connectToField);
            writer.writeString("as", this.as);
            Integer maxDepth = this.options.getMaxDepth();
            if (maxDepth != null) {
                writer.writeInt32("maxDepth", maxDepth);
            }
            if ((depthField = this.options.getDepthField()) != null) {
                writer.writeString("depthField", depthField);
            }
            if ((restrictSearchWithMatch = this.options.getRestrictSearchWithMatch()) != null) {
                writer.writeName("restrictSearchWithMatch");
                BuildersHelper.encodeValue(writer, restrictSearchWithMatch, codecRegistry);
            }
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
            GraphLookupStage that = (GraphLookupStage)o2;
            if (this.from != null ? !this.from.equals(that.from) : that.from != null) {
                return false;
            }
            if (this.startWith != null ? !this.startWith.equals(that.startWith) : that.startWith != null) {
                return false;
            }
            if (this.connectFromField != null ? !this.connectFromField.equals(that.connectFromField) : that.connectFromField != null) {
                return false;
            }
            if (this.connectToField != null ? !this.connectToField.equals(that.connectToField) : that.connectToField != null) {
                return false;
            }
            if (this.as != null ? !this.as.equals(that.as) : that.as != null) {
                return false;
            }
            return this.options != null ? this.options.equals(that.options) : that.options == null;
        }

        public int hashCode() {
            int result = this.from != null ? this.from.hashCode() : 0;
            result = 31 * result + (this.startWith != null ? this.startWith.hashCode() : 0);
            result = 31 * result + (this.connectFromField != null ? this.connectFromField.hashCode() : 0);
            result = 31 * result + (this.connectToField != null ? this.connectToField.hashCode() : 0);
            result = 31 * result + (this.as != null ? this.as.hashCode() : 0);
            result = 31 * result + (this.options != null ? this.options.hashCode() : 0);
            return result;
        }

        public String toString() {
            return "Stage{name='$graphLookup', as='" + this.as + '\'' + ", connectFromField='" + this.connectFromField + '\'' + ", connectToField='" + this.connectToField + '\'' + ", from='" + this.from + '\'' + ", options=" + this.options + ", startWith=" + this.startWith + '}';
        }
    }

    private static final class LookupStage<TExpression>
    implements Bson {
        private final String from;
        private final List<Variable<TExpression>> let;
        private final List<? extends Bson> pipeline;
        private final String as;

        private LookupStage(String from, @Nullable List<Variable<TExpression>> let, List<? extends Bson> pipeline, String as2) {
            this.from = from;
            this.let = let;
            this.pipeline = pipeline;
            this.as = as2;
        }

        @Override
        public <TDocument> BsonDocument toBsonDocument(Class<TDocument> tDocumentClass, CodecRegistry codecRegistry) {
            BsonDocumentWriter writer = new BsonDocumentWriter(new BsonDocument());
            writer.writeStartDocument();
            writer.writeStartDocument("$lookup");
            writer.writeString("from", this.from);
            if (this.let != null) {
                writer.writeStartDocument("let");
                for (Variable variable : this.let) {
                    writer.writeName(variable.getName());
                    BuildersHelper.encodeValue(writer, variable.getValue(), codecRegistry);
                }
                writer.writeEndDocument();
            }
            writer.writeName("pipeline");
            writer.writeStartArray();
            for (Bson bson : this.pipeline) {
                BuildersHelper.encodeValue(writer, bson, codecRegistry);
            }
            writer.writeEndArray();
            writer.writeString("as", this.as);
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
            LookupStage that = (LookupStage)o2;
            if (this.from != null ? !this.from.equals(that.from) : that.from != null) {
                return false;
            }
            if (this.let != null ? !this.let.equals(that.let) : that.let != null) {
                return false;
            }
            if (this.pipeline != null ? !this.pipeline.equals(that.pipeline) : that.pipeline != null) {
                return false;
            }
            return this.as != null ? this.as.equals(that.as) : that.as == null;
        }

        public int hashCode() {
            int result = this.from != null ? this.from.hashCode() : 0;
            result = 31 * result + (this.let != null ? this.let.hashCode() : 0);
            result = 31 * result + (this.pipeline != null ? this.pipeline.hashCode() : 0);
            result = 31 * result + (this.as != null ? this.as.hashCode() : 0);
            return result;
        }

        public String toString() {
            return "Stage{name='$lookup', from='" + this.from + '\'' + ", let=" + this.let + ", pipeline=" + this.pipeline + ", as='" + this.as + '\'' + '}';
        }
    }

    private static final class BucketAutoStage<TExpression>
    implements Bson {
        private final TExpression groupBy;
        private final int buckets;
        private final BucketAutoOptions options;

        BucketAutoStage(TExpression groupBy, int buckets, BucketAutoOptions options) {
            Assertions.notNull("options", options);
            this.groupBy = groupBy;
            this.buckets = buckets;
            this.options = options;
        }

        @Override
        public <TDocument> BsonDocument toBsonDocument(Class<TDocument> tDocumentClass, CodecRegistry codecRegistry) {
            BsonDocumentWriter writer = new BsonDocumentWriter(new BsonDocument());
            writer.writeStartDocument();
            writer.writeStartDocument("$bucketAuto");
            writer.writeName("groupBy");
            BuildersHelper.encodeValue(writer, this.groupBy, codecRegistry);
            writer.writeInt32("buckets", this.buckets);
            Aggregates.writeBucketOutput(codecRegistry, writer, this.options.getOutput());
            BucketGranularity granularity = this.options.getGranularity();
            if (granularity != null) {
                writer.writeString("granularity", granularity.getValue());
            }
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
            BucketAutoStage that = (BucketAutoStage)o2;
            if (this.buckets != that.buckets) {
                return false;
            }
            if (this.groupBy != null ? !this.groupBy.equals(that.groupBy) : that.groupBy != null) {
                return false;
            }
            return this.options.equals(that.options);
        }

        public int hashCode() {
            int result = this.groupBy != null ? this.groupBy.hashCode() : 0;
            result = 31 * result + this.buckets;
            result = 31 * result + this.options.hashCode();
            return result;
        }

        public String toString() {
            return "Stage{name='$bucketAuto', buckets=" + this.buckets + ", groupBy=" + this.groupBy + ", options=" + this.options + '}';
        }
    }

    private static final class BucketStage<TExpression, TBoundary>
    implements Bson {
        private final TExpression groupBy;
        private final List<TBoundary> boundaries;
        private final BucketOptions options;

        BucketStage(TExpression groupBy, List<TBoundary> boundaries, BucketOptions options) {
            Assertions.notNull("options", options);
            this.groupBy = groupBy;
            this.boundaries = boundaries;
            this.options = options;
        }

        @Override
        public <TDocument> BsonDocument toBsonDocument(Class<TDocument> tDocumentClass, CodecRegistry codecRegistry) {
            BsonDocumentWriter writer = new BsonDocumentWriter(new BsonDocument());
            writer.writeStartDocument();
            writer.writeStartDocument("$bucket");
            writer.writeName("groupBy");
            BuildersHelper.encodeValue(writer, this.groupBy, codecRegistry);
            writer.writeStartArray("boundaries");
            for (TBoundary boundary : this.boundaries) {
                BuildersHelper.encodeValue(writer, boundary, codecRegistry);
            }
            writer.writeEndArray();
            Object defaultBucket = this.options.getDefaultBucket();
            if (defaultBucket != null) {
                writer.writeName("default");
                BuildersHelper.encodeValue(writer, defaultBucket, codecRegistry);
            }
            Aggregates.writeBucketOutput(codecRegistry, writer, this.options.getOutput());
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
            BucketStage that = (BucketStage)o2;
            if (this.groupBy != null ? !this.groupBy.equals(that.groupBy) : that.groupBy != null) {
                return false;
            }
            if (this.boundaries != null ? !this.boundaries.equals(that.boundaries) : that.boundaries != null) {
                return false;
            }
            return this.options.equals(that.options);
        }

        public int hashCode() {
            int result = this.groupBy != null ? this.groupBy.hashCode() : 0;
            result = 31 * result + (this.boundaries != null ? this.boundaries.hashCode() : 0);
            result = 31 * result + this.options.hashCode();
            return result;
        }

        public String toString() {
            return "Stage{name='$bucket', boundaries=" + this.boundaries + ", groupBy=" + this.groupBy + ", options=" + this.options + '}';
        }
    }

    private static class SimplePipelineStage
    implements Bson {
        private final String name;
        private final Bson value;

        SimplePipelineStage(String name, Bson value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public <TDocument> BsonDocument toBsonDocument(Class<TDocument> documentClass, CodecRegistry codecRegistry) {
            return new BsonDocument(this.name, this.value.toBsonDocument(documentClass, codecRegistry));
        }

        public boolean equals(Object o2) {
            if (this == o2) {
                return true;
            }
            if (o2 == null || this.getClass() != o2.getClass()) {
                return false;
            }
            SimplePipelineStage that = (SimplePipelineStage)o2;
            if (this.name != null ? !this.name.equals(that.name) : that.name != null) {
                return false;
            }
            return this.value != null ? this.value.equals(that.value) : that.value == null;
        }

        public int hashCode() {
            int result = this.name != null ? this.name.hashCode() : 0;
            result = 31 * result + (this.value != null ? this.value.hashCode() : 0);
            return result;
        }

        public String toString() {
            return "Stage{name='" + this.name + '\'' + ", value=" + this.value + '}';
        }
    }
}

