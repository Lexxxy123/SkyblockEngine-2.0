/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.annotations.Immutable;
import com.mongodb.assertions.Assertions;

@Immutable
public final class Tag {
    private final String name;
    private final String value;

    public Tag(String name, String value) {
        this.name = Assertions.notNull("name", name);
        this.value = Assertions.notNull("value", value);
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        Tag that = (Tag)o2;
        if (!this.name.equals(that.name)) {
            return false;
        }
        return this.value.equals(that.value);
    }

    public int hashCode() {
        int result = this.name.hashCode();
        result = 31 * result + this.value.hashCode();
        return result;
    }

    public String toString() {
        return "Tag{name='" + this.name + '\'' + ", value='" + this.value + '\'' + '}';
    }
}

