/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.assertions.Assertions;
import com.mongodb.lang.Nullable;
import java.util.List;
import org.bson.UuidRepresentation;

public class MongoClientURI {
    private final ConnectionString proxied;
    private final MongoClientOptions.Builder builder;

    public MongoClientURI(String uri) {
        this(uri, new MongoClientOptions.Builder());
    }

    public MongoClientURI(String uri, MongoClientOptions.Builder builder) {
        this.builder = Assertions.notNull("builder", builder);
        this.proxied = new ConnectionString(uri);
    }

    ConnectionString getProxied() {
        return this.proxied;
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

    public String getURI() {
        return this.proxied.getConnectionString();
    }

    @Nullable
    public MongoCredential getCredentials() {
        return this.proxied.getCredential();
    }

    public MongoClientOptions getOptions() {
        UuidRepresentation uuidRepresentation;
        String applicationName;
        Integer heartbeatFrequency;
        Integer localThreshold;
        Integer serverSelectionTimeout;
        Boolean sslInvalidHostnameAllowed;
        Boolean sslEnabled;
        String requiredReplicaSetName;
        Integer connectTimeout;
        Integer socketTimeout;
        Integer maxConnectionLifeTime;
        Integer maxConnectionIdleTime;
        Integer threadsAllowedToBlockForConnectionMultiplier;
        Integer maxWaitTime;
        Integer integer;
        Integer maxConnectionPoolSize;
        WriteConcern writeConcern;
        ReadConcern readConcern;
        ReadPreference readPreference = this.proxied.getReadPreference();
        if (readPreference != null) {
            this.builder.readPreference(readPreference);
        }
        if ((readConcern = this.proxied.getReadConcern()) != null) {
            this.builder.readConcern(readConcern);
        }
        if ((writeConcern = this.proxied.getWriteConcern()) != null) {
            this.builder.writeConcern(writeConcern);
        }
        if (this.proxied.getRetryWritesValue() != null) {
            this.builder.retryWrites(this.proxied.getRetryWritesValue());
        }
        if (this.proxied.getRetryReads() != null) {
            this.builder.retryReads(this.proxied.getRetryReads());
        }
        if ((maxConnectionPoolSize = this.proxied.getMaxConnectionPoolSize()) != null) {
            this.builder.connectionsPerHost(maxConnectionPoolSize);
        }
        if ((integer = this.proxied.getMinConnectionPoolSize()) != null) {
            this.builder.minConnectionsPerHost(integer);
        }
        if ((maxWaitTime = this.proxied.getMaxWaitTime()) != null) {
            this.builder.maxWaitTime(maxWaitTime);
        }
        if ((threadsAllowedToBlockForConnectionMultiplier = this.proxied.getThreadsAllowedToBlockForConnectionMultiplier()) != null) {
            this.builder.threadsAllowedToBlockForConnectionMultiplier(threadsAllowedToBlockForConnectionMultiplier);
        }
        if ((maxConnectionIdleTime = this.proxied.getMaxConnectionIdleTime()) != null) {
            this.builder.maxConnectionIdleTime(maxConnectionIdleTime);
        }
        if ((maxConnectionLifeTime = this.proxied.getMaxConnectionLifeTime()) != null) {
            this.builder.maxConnectionLifeTime(maxConnectionLifeTime);
        }
        if ((socketTimeout = this.proxied.getSocketTimeout()) != null) {
            this.builder.socketTimeout(socketTimeout);
        }
        if ((connectTimeout = this.proxied.getConnectTimeout()) != null) {
            this.builder.connectTimeout(connectTimeout);
        }
        if ((requiredReplicaSetName = this.proxied.getRequiredReplicaSetName()) != null) {
            this.builder.requiredReplicaSetName(requiredReplicaSetName);
        }
        if ((sslEnabled = this.proxied.getSslEnabled()) != null) {
            this.builder.sslEnabled(sslEnabled);
        }
        if ((sslInvalidHostnameAllowed = this.proxied.getSslInvalidHostnameAllowed()) != null) {
            this.builder.sslInvalidHostNameAllowed(sslInvalidHostnameAllowed);
        }
        if ((serverSelectionTimeout = this.proxied.getServerSelectionTimeout()) != null) {
            this.builder.serverSelectionTimeout(serverSelectionTimeout);
        }
        if ((localThreshold = this.proxied.getLocalThreshold()) != null) {
            this.builder.localThreshold(localThreshold);
        }
        if ((heartbeatFrequency = this.proxied.getHeartbeatFrequency()) != null) {
            this.builder.heartbeatFrequency(heartbeatFrequency);
        }
        if ((applicationName = this.proxied.getApplicationName()) != null) {
            this.builder.applicationName(applicationName);
        }
        if (!this.proxied.getCompressorList().isEmpty()) {
            this.builder.compressorList(this.proxied.getCompressorList());
        }
        if ((uuidRepresentation = this.proxied.getUuidRepresentation()) != null) {
            this.builder.uuidRepresentation(uuidRepresentation);
        }
        return this.builder.build();
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        MongoClientURI that = (MongoClientURI)o2;
        if (!this.getHosts().equals(that.getHosts())) {
            return false;
        }
        String database = this.getDatabase();
        if (database != null ? !database.equals(that.getDatabase()) : that.getDatabase() != null) {
            return false;
        }
        String collection = this.getCollection();
        if (collection != null ? !collection.equals(that.getCollection()) : that.getCollection() != null) {
            return false;
        }
        MongoCredential credentials = this.getCredentials();
        if (credentials != null ? !credentials.equals(that.getCredentials()) : that.getCredentials() != null) {
            return false;
        }
        return this.getOptions().equals(that.getOptions());
    }

    public int hashCode() {
        int result = this.getOptions().hashCode();
        result = 31 * result + this.getHosts().hashCode();
        MongoCredential credentials = this.getCredentials();
        result = 31 * result + (credentials != null ? credentials.hashCode() : 0);
        String database = this.getDatabase();
        result = 31 * result + (database != null ? database.hashCode() : 0);
        String collection = this.getCollection();
        result = 31 * result + (collection != null ? collection.hashCode() : 0);
        return result;
    }

    public String toString() {
        return this.proxied.toString();
    }
}

