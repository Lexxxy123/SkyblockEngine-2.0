/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.assertions.Assertions;

public class Variable<TExpression> {
    private final String name;
    private final TExpression value;

    public Variable(String name, TExpression value) {
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
        if (!(o2 instanceof Variable)) {
            return false;
        }
        Variable variable = (Variable)o2;
        if (!this.name.equals(variable.name)) {
            return false;
        }
        return this.value != null ? this.value.equals(variable.value) : variable.value == null;
    }

    public int hashCode() {
        int result = this.name.hashCode();
        result = 31 * result + (this.value != null ? this.value.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "Variable{name='" + this.name + '\'' + ", value=" + this.value + '}';
    }
}

