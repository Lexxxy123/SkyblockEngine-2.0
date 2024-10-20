/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.connection;

import com.mongodb.assertions.Assertions;
import org.bson.types.ObjectId;

public final class ClusterId {
    private final String value;
    private final String description;

    public ClusterId() {
        this(null);
    }

    public ClusterId(String description) {
        this.value = new ObjectId().toHexString();
        this.description = description;
    }

    ClusterId(String value, String description) {
        this.value = Assertions.notNull("value", value);
        this.description = description;
    }

    public String getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        ClusterId clusterId = (ClusterId)o2;
        if (!this.value.equals(clusterId.value)) {
            return false;
        }
        return !(this.description != null ? !this.description.equals(clusterId.description) : clusterId.description != null);
    }

    public int hashCode() {
        int result = this.value.hashCode();
        result = 31 * result + (this.description != null ? this.description.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "ClusterId{value='" + this.value + '\'' + ", description='" + this.description + '\'' + '}';
    }
}

