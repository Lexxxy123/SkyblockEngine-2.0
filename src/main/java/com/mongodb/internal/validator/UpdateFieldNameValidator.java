/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.validator;

import com.mongodb.internal.validator.NoOpFieldNameValidator;
import org.bson.FieldNameValidator;

public class UpdateFieldNameValidator
implements FieldNameValidator {
    @Override
    public boolean validate(String fieldName) {
        return fieldName.startsWith("$");
    }

    @Override
    public FieldNameValidator getValidatorForField(String fieldName) {
        return new NoOpFieldNameValidator();
    }
}

