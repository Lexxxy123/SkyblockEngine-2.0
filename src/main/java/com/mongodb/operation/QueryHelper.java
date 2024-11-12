/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.MongoCommandException;
import com.mongodb.MongoCursorNotFoundException;
import com.mongodb.MongoQueryException;
import com.mongodb.ServerCursor;

final class QueryHelper {
    static MongoQueryException translateCommandException(MongoCommandException commandException, ServerCursor cursor) {
        if (commandException.getErrorCode() == 43) {
            return new MongoCursorNotFoundException(cursor.getId(), cursor.getAddress());
        }
        return new MongoQueryException(cursor.getAddress(), commandException.getErrorCode(), commandException.getErrorMessage());
    }

    private QueryHelper() {
    }
}

