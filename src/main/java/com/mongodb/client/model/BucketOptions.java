/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.client.model.BsonField;
import com.mongodb.lang.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BucketOptions {
    private Object defaultBucket;
    private List<BsonField> output;

    public BucketOptions defaultBucket(@Nullable Object name) {
        this.defaultBucket = name;
        return this;
    }

    @Nullable
    public Object getDefaultBucket() {
        return this.defaultBucket;
    }

    @Nullable
    public List<BsonField> getOutput() {
        return this.output == null ? null : new ArrayList<BsonField>(this.output);
    }

    public BucketOptions output(BsonField ... output) {
        this.output = Arrays.asList(output);
        return this;
    }

    public BucketOptions output(List<BsonField> output) {
        this.output = output;
        return this;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        BucketOptions that = (BucketOptions)o2;
        if (this.defaultBucket != null ? !this.defaultBucket.equals(that.defaultBucket) : that.defaultBucket != null) {
            return false;
        }
        return this.output != null ? this.output.equals(that.output) : that.output == null;
    }

    public int hashCode() {
        int result = this.defaultBucket != null ? this.defaultBucket.hashCode() : 0;
        result = 31 * result + (this.output != null ? this.output.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "BucketOptions{defaultBucket=" + this.defaultBucket + ", output=" + this.output + '}';
    }
}

