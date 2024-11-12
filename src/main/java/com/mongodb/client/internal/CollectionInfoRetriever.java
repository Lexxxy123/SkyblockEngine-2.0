/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.internal;

import com.mongodb.assertions.Assertions;
import com.mongodb.client.internal.SimpleMongoClient;
import com.mongodb.lang.Nullable;
import org.bson.BsonDocument;

class CollectionInfoRetriever {
    private final SimpleMongoClient client;

    CollectionInfoRetriever(SimpleMongoClient client) {
        this.client = Assertions.notNull("client", client);
    }

    @Nullable
    public BsonDocument filter(String databaseName, BsonDocument filter) {
        return (BsonDocument)this.client.getDatabase(databaseName).listCollections(BsonDocument.class).filter(filter).first();
    }
}

