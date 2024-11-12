/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.internal;

import com.mongodb.MongoNamespace;
import com.mongodb.ReadConcern;
import com.mongodb.assertions.Assertions;
import com.mongodb.client.internal.SimpleMongoClient;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import org.bson.BsonDocument;

class KeyRetriever
implements Closeable {
    private final SimpleMongoClient client;
    private final boolean ownsClient;
    private final MongoNamespace namespace;

    KeyRetriever(SimpleMongoClient client, boolean ownsClient, MongoNamespace namespace) {
        this.client = Assertions.notNull("client", client);
        this.ownsClient = ownsClient;
        this.namespace = Assertions.notNull("namespace", namespace);
    }

    public List<BsonDocument> find(BsonDocument keyFilter) {
        return this.client.getDatabase(this.namespace.getDatabaseName()).getCollection(this.namespace.getCollectionName(), BsonDocument.class).withReadConcern(ReadConcern.MAJORITY).find(keyFilter).into(new ArrayList());
    }

    @Override
    public void close() {
        if (this.ownsClient) {
            this.client.close();
        }
    }
}

