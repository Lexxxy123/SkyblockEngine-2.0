/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.validator;

import java.util.Map;
import org.bson.FieldNameValidator;

public class MappedFieldNameValidator
implements FieldNameValidator {
    private final FieldNameValidator defaultValidator;
    private final Map<String, FieldNameValidator> fieldNameToValidatorMap;

    public MappedFieldNameValidator(FieldNameValidator defaultValidator, Map<String, FieldNameValidator> fieldNameToValidatorMap) {
        this.defaultValidator = defaultValidator;
        this.fieldNameToValidatorMap = fieldNameToValidatorMap;
    }

    @Override
    public boolean validate(String fieldName) {
        return this.defaultValidator.validate(fieldName);
    }

    @Override
    public FieldNameValidator getValidatorForField(String fieldName) {
        if (this.fieldNameToValidatorMap.containsKey(fieldName)) {
            return this.fieldNameToValidatorMap.get(fieldName);
        }
        return this.defaultValidator;
    }
}

