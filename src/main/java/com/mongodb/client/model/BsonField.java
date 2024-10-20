/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.assertions.Assertions;
import org.bson.conversions.Bson;

public final class BsonField {
    private final String name;
    private final Bson value;

    public BsonField(String name, Bson value) {
        this.name = Assertions.notNull("name", name);
        this.value = Assertions.notNull("value", value);
    }

    public String getName() {
        return this.name;
    }

    public Bson getValue() {
        return this.value;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        BsonField bsonField = (BsonField)o2;
        if (!this.name.equals(bsonField.name)) {
            return false;
        }
        return this.value.equals(bsonField.value);
    }

    public int hashCode() {
        int result = this.name.hashCode();
        result = 31 * result + this.value.hashCode();
        return result;
    }

    public String toString() {
        return "BsonField{name='" + this.name + '\'' + ", value=" + this.value + '}';
    }
}

