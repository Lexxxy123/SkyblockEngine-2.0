/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.validator;

import java.util.Arrays;
import java.util.List;
import org.bson.FieldNameValidator;

public class CollectibleDocumentFieldNameValidator
implements FieldNameValidator {
    private static final List<String> EXCEPTIONS = Arrays.asList("$db", "$ref", "$id");

    @Override
    public boolean validate(String fieldName) {
        if (fieldName == null) {
            throw new IllegalArgumentException("Field name can not be null");
        }
        if (fieldName.contains(".")) {
            return false;
        }
        return !fieldName.startsWith("$") || EXCEPTIONS.contains(fieldName);
    }

    @Override
    public FieldNameValidator getValidatorForField(String fieldName) {
        return this;
    }
}

