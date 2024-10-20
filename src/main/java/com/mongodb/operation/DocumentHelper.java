/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonValue;

final class DocumentHelper {
    private DocumentHelper() {
    }

    static void putIfTrue(BsonDocument command, String key, boolean condition) {
        if (condition) {
            command.put(key, BsonBoolean.TRUE);
        }
    }

    static void putIfFalse(BsonDocument command, String key, boolean condition) {
        if (!condition) {
            command.put(key, BsonBoolean.FALSE);
        }
    }

    static void putIfNotNullOrEmpty(BsonDocument command, String key, BsonDocument documentValue) {
        if (documentValue != null && !documentValue.isEmpty()) {
            command.put(key, documentValue);
        }
    }

    static void putIfNotNull(BsonDocument command, String key, BsonValue value) {
        if (value != null) {
            command.put(key, value);
        }
    }

    static void putIfNotZero(BsonDocument command, String key, int value) {
        if (value != 0) {
            command.put(key, new BsonInt32(value));
        }
    }

    static void putIfNotZero(BsonDocument command, String key, long value) {
        if (value != 0L) {
            command.put(key, new BsonInt64(value));
        }
    }
}

