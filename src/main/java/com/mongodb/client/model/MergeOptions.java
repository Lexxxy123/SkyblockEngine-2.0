/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.client.model.Variable;
import java.util.Collections;
import java.util.List;
import org.bson.conversions.Bson;

public final class MergeOptions {
    private List<String> uniqueIdentifier;
    private WhenMatched whenMatched;
    private List<Variable<?>> variables;
    private List<Bson> whenMatchedPipeline;
    private WhenNotMatched whenNotMatched;

    public List<String> getUniqueIdentifier() {
        return this.uniqueIdentifier;
    }

    public MergeOptions uniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = Collections.singletonList(uniqueIdentifier);
        return this;
    }

    public MergeOptions uniqueIdentifier(List<String> uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
        return this;
    }

    public WhenMatched getWhenMatched() {
        return this.whenMatched;
    }

    public MergeOptions whenMatched(WhenMatched whenMatched) {
        this.whenMatched = whenMatched;
        return this;
    }

    public List<Variable<?>> getVariables() {
        return this.variables;
    }

    public MergeOptions variables(List<Variable<?>> variables) {
        this.variables = variables;
        return this;
    }

    public List<Bson> getWhenMatchedPipeline() {
        return this.whenMatchedPipeline;
    }

    public MergeOptions whenMatchedPipeline(List<Bson> whenMatchedPipeline) {
        this.whenMatchedPipeline = whenMatchedPipeline;
        return this;
    }

    public WhenNotMatched getWhenNotMatched() {
        return this.whenNotMatched;
    }

    public MergeOptions whenNotMatched(WhenNotMatched whenNotMatched) {
        this.whenNotMatched = whenNotMatched;
        return this;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        MergeOptions that = (MergeOptions)o2;
        if (this.uniqueIdentifier != null ? !this.uniqueIdentifier.equals(that.uniqueIdentifier) : that.uniqueIdentifier != null) {
            return false;
        }
        if (this.whenMatched != that.whenMatched) {
            return false;
        }
        if (this.variables != null ? !this.variables.equals(that.variables) : that.variables != null) {
            return false;
        }
        if (this.whenMatchedPipeline != null ? !this.whenMatchedPipeline.equals(that.whenMatchedPipeline) : that.whenMatchedPipeline != null) {
            return false;
        }
        return this.whenNotMatched == that.whenNotMatched;
    }

    public int hashCode() {
        int result = this.uniqueIdentifier != null ? this.uniqueIdentifier.hashCode() : 0;
        result = 31 * result + (this.whenMatched != null ? this.whenMatched.hashCode() : 0);
        result = 31 * result + (this.variables != null ? this.variables.hashCode() : 0);
        result = 31 * result + (this.whenMatchedPipeline != null ? this.whenMatchedPipeline.hashCode() : 0);
        result = 31 * result + (this.whenNotMatched != null ? this.whenNotMatched.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "MergeOptions{uniqueIdentifier=" + this.uniqueIdentifier + ", whenMatched=" + (Object)((Object)this.whenMatched) + ", variables=" + this.variables + ", whenMatchedPipeline=" + this.whenMatchedPipeline + ", whenNotMatched=" + (Object)((Object)this.whenNotMatched) + '}';
    }

    public static enum WhenNotMatched {
        INSERT,
        DISCARD,
        FAIL;

    }

    public static enum WhenMatched {
        REPLACE,
        KEEP_EXISTING,
        MERGE,
        PIPELINE,
        FAIL;

    }
}

