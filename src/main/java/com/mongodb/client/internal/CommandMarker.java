/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.internal;

import com.mongodb.MongoClientException;
import com.mongodb.MongoException;
import com.mongodb.MongoTimeoutException;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.assertions.Assertions;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.internal.capi.MongoCryptHelper;
import java.io.Closeable;
import java.util.Map;
import org.bson.RawBsonDocument;
import org.bson.conversions.Bson;

class CommandMarker
implements Closeable {
    private final MongoClient client;
    private final ProcessBuilder processBuilder;

    CommandMarker(boolean isBypassAutoEncryption, Map<String, Object> options) {
        if (isBypassAutoEncryption) {
            this.processBuilder = null;
            this.client = null;
            return;
        }
        if (!options.containsKey("mongocryptdBypassSpawn") || !((Boolean)options.get("mongocryptdBypassSpawn")).booleanValue()) {
            this.processBuilder = MongoCryptHelper.createProcessBuilder(options);
            MongoCryptHelper.startProcess(this.processBuilder);
        } else {
            this.processBuilder = null;
        }
        this.client = MongoClients.create(MongoCryptHelper.createMongocryptdClientSettings((String)options.get("mongocryptdURI")));
    }

    RawBsonDocument mark(String databaseName, RawBsonDocument command) {
        Assertions.notNull("client", this.client);
        try {
            try {
                return this.executeCommand(databaseName, command);
            } catch (MongoTimeoutException e2) {
                if (this.processBuilder == null) {
                    throw e2;
                }
                MongoCryptHelper.startProcess(this.processBuilder);
                return this.executeCommand(databaseName, command);
            }
        } catch (MongoException e3) {
            throw this.wrapInClientException(e3);
        }
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.close();
        }
    }

    private RawBsonDocument executeCommand(String databaseName, RawBsonDocument markableCommand) {
        return this.client.getDatabase(databaseName).withReadConcern(ReadConcern.DEFAULT).withReadPreference(ReadPreference.primary()).runCommand((Bson)markableCommand, RawBsonDocument.class);
    }

    private MongoClientException wrapInClientException(MongoException e2) {
        return new MongoClientException("Exception in encryption library: " + e2.getMessage(), e2);
    }
}

