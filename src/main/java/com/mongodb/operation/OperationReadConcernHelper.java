/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.assertions.Assertions;
import com.mongodb.internal.connection.ReadConcernHelper;
import com.mongodb.session.SessionContext;
import org.bson.BsonDocument;

final class OperationReadConcernHelper {
    static void appendReadConcernToCommand(SessionContext sessionContext, BsonDocument commandDocument) {
        Assertions.notNull("commandDocument", commandDocument);
        Assertions.notNull("sessionContext", sessionContext);
        if (sessionContext.hasActiveTransaction()) {
            return;
        }
        BsonDocument readConcernDocument = ReadConcernHelper.getReadConcernDocument(sessionContext);
        if (!readConcernDocument.isEmpty()) {
            commandDocument.append("readConcern", readConcernDocument);
        }
    }

    private OperationReadConcernHelper() {
    }
}

