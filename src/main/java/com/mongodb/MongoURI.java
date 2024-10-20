/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoClientException;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.MongoOptions;
import com.mongodb.WriteConcern;
import com.mongodb.lang.Nullable;
import java.util.List;

@Deprecated
public class MongoURI {
    public static final String MONGODB_PREFIX = "mongodb://";
    private final MongoClientURI proxied;
    private final MongoOptions options;

    @Deprecated
    public MongoURI(String uri) {
        this.proxied = new MongoClientURI(uri, MongoClientOptions.builder().connectionsPerHost(10).writeConcern(WriteConcern.UNACKNOWLEDGED));
        this.options = new MongoOptions(this.proxied.getOptions());
    }

    @Deprecated
    public MongoURI(MongoClientURI proxied) {
        this.proxied = proxied;
        this.options = new MongoOptions(proxied.getOptions());
    }

    @Nullable
    public String getUsername() {
        return this.proxied.getUsername();
    }

    @Nullable
    public char[] getPassword() {
        return this.proxied.getPassword();
    }

    public List<String> getHosts() {
        return this.proxied.getHosts();
    }

    @Nullable
    public String getDatabase() {
        return this.proxied.getDatabase();
    }

    @Nullable
    public String getCollection() {
        return this.proxied.getCollection();
    }

    @Nullable
    public MongoCredential getCredentials() {
        return this.proxied.getCredentials();
    }

    public MongoOptions getOptions() {
        return this.options;
    }

    public Mongo connect() {
        return new Mongo(this);
    }

    public DB connectDB() {
        return this.connect().getDB(this.getDatabaseNonNull());
    }

    public DB connectDB(Mongo mongo) {
        return mongo.getDB(this.getDatabaseNonNull());
    }

    public DBCollection connectCollection(DB db) {
        return db.getCollection(this.getCollectionNonNull());
    }

    public DBCollection connectCollection(Mongo mongo) {
        return this.connectDB(mongo).getCollection(this.getCollectionNonNull());
    }

    public String toString() {
        return this.proxied.toString();
    }

    MongoClientURI toClientURI() {
        return this.proxied;
    }

    private String getDatabaseNonNull() {
        String database = this.getDatabase();
        if (database == null) {
            throw new MongoClientException("Database name can not be null in this context");
        }
        return database;
    }

    private String getCollectionNonNull() {
        String collection = this.getCollection();
        if (collection == null) {
            throw new MongoClientException("Collection name can not be null in this context");
        }
        return collection;
    }
}

