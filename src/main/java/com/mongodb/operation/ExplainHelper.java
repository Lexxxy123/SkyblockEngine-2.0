/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.ExplainVerbosity;
import com.mongodb.MongoInternalException;
import org.bson.BsonDocument;
import org.bson.BsonString;

final class ExplainHelper {
    static BsonDocument asExplainCommand(BsonDocument command, ExplainVerbosity explainVerbosity) {
        return new BsonDocument("explain", command).append("verbosity", ExplainHelper.getVerbosityAsString(explainVerbosity));
    }

    private static BsonString getVerbosityAsString(ExplainVerbosity explainVerbosity) {
        switch (explainVerbosity) {
            case QUERY_PLANNER: {
                return new BsonString("queryPlanner");
            }
            case EXECUTION_STATS: {
                return new BsonString("executionStats");
            }
            case ALL_PLANS_EXECUTIONS: {
                return new BsonString("allPlansExecution");
            }
        }
        throw new MongoInternalException(String.format("Unsupported explain verbosity %s", new Object[]{explainVerbosity}));
    }

    private ExplainHelper() {
    }
}

