/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.MongoCommandException;
import com.mongodb.ServerAddress;
import org.bson.BsonDocument;

public class MongoNodeIsRecoveringException
extends MongoCommandException {
    private static final long serialVersionUID = 6062524147327071635L;

    public MongoNodeIsRecoveringException(BsonDocument response, ServerAddress serverAddress) {
        super(response, serverAddress);
    }

    @Deprecated
    public MongoNodeIsRecoveringException(ServerAddress serverAddress) {
        super(new BsonDocument(), serverAddress);
    }
}

