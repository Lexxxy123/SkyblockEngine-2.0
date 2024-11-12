/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.validator;

import org.bson.FieldNameValidator;

public class NoOpFieldNameValidator
implements FieldNameValidator {
    @Override
    public boolean validate(String fieldName) {
        return true;
    }

    @Override
    public FieldNameValidator getValidatorForField(String fieldName) {
        return this;
    }
}

