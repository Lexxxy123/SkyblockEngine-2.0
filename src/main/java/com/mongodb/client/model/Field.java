/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.assertions.Assertions;

public class Field<TExpression> {
    private final String name;
    private TExpression value;

    public Field(String name, TExpression value) {
        this.name = Assertions.notNull("name", name);
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public TExpression getValue() {
        return this.value;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (!(o2 instanceof Field)) {
            return false;
        }
        Field field = (Field)o2;
        if (!this.name.equals(field.name)) {
            return false;
        }
        return this.value != null ? this.value.equals(field.value) : field.value == null;
    }

    public int hashCode() {
        int result = this.name.hashCode();
        result = 31 * result + (this.value != null ? this.value.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "Field{name='" + this.name + '\'' + ", value=" + this.value + '}';
    }
}

