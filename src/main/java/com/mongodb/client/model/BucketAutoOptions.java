/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.client.model.BsonField;
import com.mongodb.client.model.BucketGranularity;
import com.mongodb.lang.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BucketAutoOptions {
    private List<BsonField> output;
    private BucketGranularity granularity;

    @Nullable
    public BucketGranularity getGranularity() {
        return this.granularity;
    }

    @Nullable
    public List<BsonField> getOutput() {
        return this.output == null ? null : new ArrayList<BsonField>(this.output);
    }

    public BucketAutoOptions granularity(@Nullable BucketGranularity granularity) {
        this.granularity = granularity;
        return this;
    }

    public BucketAutoOptions output(BsonField ... output) {
        this.output = Arrays.asList(output);
        return this;
    }

    public BucketAutoOptions output(@Nullable List<BsonField> output) {
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
        BucketAutoOptions that = (BucketAutoOptions)o2;
        if (this.output != null ? !this.output.equals(that.output) : that.output != null) {
            return false;
        }
        return this.granularity == that.granularity;
    }

    public int hashCode() {
        int result = this.output != null ? this.output.hashCode() : 0;
        result = 31 * result + (this.granularity != null ? this.granularity.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "BucketAutoOptions{output=" + this.output + ", granularity=" + (Object)((Object)this.granularity) + '}';
    }
}

