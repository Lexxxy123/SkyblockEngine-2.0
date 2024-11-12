/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.internal;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.internal.SimpleMongoClient;

final class SimpleMongoClients {
    static SimpleMongoClient create(final MongoClient mongoClient) {
        return new SimpleMongoClient(){

            @Override
            public MongoDatabase getDatabase(String databaseName) {
                return mongoClient.getDatabase(databaseName);
            }

            @Override
            public void close() {
                mongoClient.close();
            }
        };
    }

    private SimpleMongoClients() {
    }
}

