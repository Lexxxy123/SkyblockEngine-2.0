/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.client.model.ValidationAction;
import com.mongodb.client.model.ValidationLevel;
import com.mongodb.lang.Nullable;
import org.bson.conversions.Bson;

public final class ValidationOptions {
    private Bson validator;
    private ValidationLevel validationLevel;
    private ValidationAction validationAction;

    @Nullable
    public Bson getValidator() {
        return this.validator;
    }

    public ValidationOptions validator(@Nullable Bson validator) {
        this.validator = validator;
        return this;
    }

    @Nullable
    public ValidationLevel getValidationLevel() {
        return this.validationLevel;
    }

    public ValidationOptions validationLevel(@Nullable ValidationLevel validationLevel) {
        this.validationLevel = validationLevel;
        return this;
    }

    @Nullable
    public ValidationAction getValidationAction() {
        return this.validationAction;
    }

    public ValidationOptions validationAction(@Nullable ValidationAction validationAction) {
        this.validationAction = validationAction;
        return this;
    }

    public String toString() {
        return "ValidationOptions{validator=" + this.validator + ", validationLevel=" + (Object)((Object)this.validationLevel) + ", validationAction=" + (Object)((Object)this.validationAction) + '}';
    }
}

