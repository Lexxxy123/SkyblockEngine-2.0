/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import java.util.Arrays;
import java.util.List;

public enum ErrorCategory {
    UNCATEGORIZED,
    DUPLICATE_KEY,
    EXECUTION_TIMEOUT;

    private static final List<Integer> DUPLICATE_KEY_ERROR_CODES;
    private static final List<Integer> EXECUTION_TIMEOUT_ERROR_CODES;

    public static ErrorCategory fromErrorCode(int code) {
        if (DUPLICATE_KEY_ERROR_CODES.contains(code)) {
            return DUPLICATE_KEY;
        }
        if (EXECUTION_TIMEOUT_ERROR_CODES.contains(code)) {
            return EXECUTION_TIMEOUT;
        }
        return UNCATEGORIZED;
    }

    static {
        DUPLICATE_KEY_ERROR_CODES = Arrays.asList(11000, 11001, 12582);
        EXECUTION_TIMEOUT_ERROR_CODES = Arrays.asList(50);
    }
}

