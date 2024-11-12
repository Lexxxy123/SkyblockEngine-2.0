/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.ReadConcernLevel;
import com.mongodb.assertions.Assertions;
import com.mongodb.session.SessionContext;
import org.bson.BsonDocument;
import org.bson.BsonString;

public final class ReadConcernHelper {
    public static BsonDocument getReadConcernDocument(SessionContext sessionContext) {
        Assertions.notNull("sessionContext", sessionContext);
        BsonDocument readConcernDocument = new BsonDocument();
        ReadConcernLevel level = sessionContext.getReadConcern().getLevel();
        if (level != null) {
            readConcernDocument.append("level", new BsonString(level.getValue()));
        }
        if (ReadConcernHelper.shouldAddAfterClusterTime(sessionContext)) {
            readConcernDocument.append("afterClusterTime", sessionContext.getOperationTime());
        }
        return readConcernDocument;
    }

    private static boolean shouldAddAfterClusterTime(SessionContext sessionContext) {
        return sessionContext.isCausallyConsistent() && sessionContext.getOperationTime() != null;
    }

    private ReadConcernHelper() {
    }
}

