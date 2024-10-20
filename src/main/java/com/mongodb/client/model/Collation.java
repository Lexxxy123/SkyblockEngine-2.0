/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.annotations.NotThreadSafe;
import com.mongodb.client.model.CollationAlternate;
import com.mongodb.client.model.CollationCaseFirst;
import com.mongodb.client.model.CollationMaxVariable;
import com.mongodb.client.model.CollationStrength;
import com.mongodb.lang.Nullable;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;

public final class Collation {
    private final String locale;
    private final Boolean caseLevel;
    private final CollationCaseFirst caseFirst;
    private final CollationStrength strength;
    private final Boolean numericOrdering;
    private final CollationAlternate alternate;
    private final CollationMaxVariable maxVariable;
    private final Boolean normalization;
    private final Boolean backwards;

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(Collation options) {
        return new Builder(options);
    }

    @Nullable
    public String getLocale() {
        return this.locale;
    }

    @Nullable
    public Boolean getCaseLevel() {
        return this.caseLevel;
    }

    @Nullable
    public CollationCaseFirst getCaseFirst() {
        return this.caseFirst;
    }

    @Nullable
    public CollationStrength getStrength() {
        return this.strength;
    }

    @Nullable
    public Boolean getNumericOrdering() {
        return this.numericOrdering;
    }

    @Nullable
    public CollationAlternate getAlternate() {
        return this.alternate;
    }

    @Nullable
    public CollationMaxVariable getMaxVariable() {
        return this.maxVariable;
    }

    @Nullable
    public Boolean getNormalization() {
        return this.normalization;
    }

    @Nullable
    public Boolean getBackwards() {
        return this.backwards;
    }

    public BsonDocument asDocument() {
        BsonDocument collation = new BsonDocument();
        if (this.locale != null) {
            collation.put("locale", new BsonString(this.locale));
        }
        if (this.caseLevel != null) {
            collation.put("caseLevel", new BsonBoolean(this.caseLevel));
        }
        if (this.caseFirst != null) {
            collation.put("caseFirst", new BsonString(this.caseFirst.getValue()));
        }
        if (this.strength != null) {
            collation.put("strength", new BsonInt32(this.strength.getIntRepresentation()));
        }
        if (this.numericOrdering != null) {
            collation.put("numericOrdering", new BsonBoolean(this.numericOrdering));
        }
        if (this.alternate != null) {
            collation.put("alternate", new BsonString(this.alternate.getValue()));
        }
        if (this.maxVariable != null) {
            collation.put("maxVariable", new BsonString(this.maxVariable.getValue()));
        }
        if (this.normalization != null) {
            collation.put("normalization", new BsonBoolean(this.normalization));
        }
        if (this.backwards != null) {
            collation.put("backwards", new BsonBoolean(this.backwards));
        }
        return collation;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        Collation that = (Collation)o2;
        if (this.locale != null ? !this.locale.equals(that.getLocale()) : that.getLocale() != null) {
            return false;
        }
        if (this.caseLevel != null ? !this.caseLevel.equals(that.getCaseLevel()) : that.getCaseLevel() != null) {
            return false;
        }
        if (this.getCaseFirst() != that.getCaseFirst()) {
            return false;
        }
        if (this.getStrength() != that.getStrength()) {
            return false;
        }
        if (this.numericOrdering != null ? !this.numericOrdering.equals(that.getNumericOrdering()) : that.getNumericOrdering() != null) {
            return false;
        }
        if (this.getAlternate() != that.getAlternate()) {
            return false;
        }
        if (this.getMaxVariable() != that.getMaxVariable()) {
            return false;
        }
        if (this.normalization != null ? !this.normalization.equals(that.getNormalization()) : that.getNormalization() != null) {
            return false;
        }
        return !(this.backwards != null ? !this.backwards.equals(that.getBackwards()) : that.getBackwards() != null);
    }

    public int hashCode() {
        int result = this.locale != null ? this.locale.hashCode() : 0;
        result = 31 * result + (this.caseLevel != null ? this.caseLevel.hashCode() : 0);
        result = 31 * result + (this.caseFirst != null ? this.caseFirst.hashCode() : 0);
        result = 31 * result + (this.strength != null ? this.strength.hashCode() : 0);
        result = 31 * result + (this.numericOrdering != null ? this.numericOrdering.hashCode() : 0);
        result = 31 * result + (this.alternate != null ? this.alternate.hashCode() : 0);
        result = 31 * result + (this.maxVariable != null ? this.maxVariable.hashCode() : 0);
        result = 31 * result + (this.normalization != null ? this.normalization.hashCode() : 0);
        result = 31 * result + (this.backwards != null ? this.backwards.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "Collation{locale='" + this.locale + "', caseLevel=" + this.caseLevel + ", caseFirst=" + (Object)((Object)this.caseFirst) + ", strength=" + (Object)((Object)this.strength) + ", numericOrdering=" + this.numericOrdering + ", alternate=" + (Object)((Object)this.alternate) + ", maxVariable=" + (Object)((Object)this.maxVariable) + ", normalization=" + this.normalization + ", backwards=" + this.backwards + "}";
    }

    private Collation(Builder builder) {
        this.locale = builder.locale;
        this.caseLevel = builder.caseLevel;
        this.caseFirst = builder.caseFirst;
        this.strength = builder.strength;
        this.numericOrdering = builder.numericOrdering;
        this.alternate = builder.alternate;
        this.maxVariable = builder.maxVariable;
        this.normalization = builder.normalization;
        this.backwards = builder.backwards;
    }

    @NotThreadSafe
    public static final class Builder {
        private String locale;
        private Boolean caseLevel;
        private CollationCaseFirst caseFirst;
        private CollationStrength strength;
        private Boolean numericOrdering;
        private CollationAlternate alternate;
        private CollationMaxVariable maxVariable;
        private Boolean normalization;
        private Boolean backwards;

        private Builder() {
        }

        private Builder(Collation options) {
            this.locale = options.getLocale();
            this.caseLevel = options.getCaseLevel();
            this.caseFirst = options.getCaseFirst();
            this.strength = options.getStrength();
            this.numericOrdering = options.getNumericOrdering();
            this.alternate = options.getAlternate();
            this.maxVariable = options.getMaxVariable();
            this.normalization = options.getNormalization();
            this.backwards = options.getBackwards();
        }

        public Builder locale(@Nullable String locale) {
            this.locale = locale;
            return this;
        }

        public Builder caseLevel(@Nullable Boolean caseLevel) {
            this.caseLevel = caseLevel;
            return this;
        }

        public Builder collationCaseFirst(@Nullable CollationCaseFirst caseFirst) {
            this.caseFirst = caseFirst;
            return this;
        }

        public Builder collationStrength(@Nullable CollationStrength strength) {
            this.strength = strength;
            return this;
        }

        public Builder numericOrdering(@Nullable Boolean numericOrdering) {
            this.numericOrdering = numericOrdering;
            return this;
        }

        public Builder collationAlternate(@Nullable CollationAlternate alternate) {
            this.alternate = alternate;
            return this;
        }

        public Builder collationMaxVariable(@Nullable CollationMaxVariable maxVariable) {
            this.maxVariable = maxVariable;
            return this;
        }

        public Builder normalization(@Nullable Boolean normalization) {
            this.normalization = normalization;
            return this;
        }

        public Builder backwards(@Nullable Boolean backwards) {
            this.backwards = backwards;
            return this;
        }

        public Collation build() {
            return new Collation(this);
        }
    }
}

