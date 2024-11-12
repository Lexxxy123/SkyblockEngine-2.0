/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.ClientSessionOptions;
import com.mongodb.DBObjectCodec;
import com.mongodb.Function;
import com.mongodb.Mongo;
import com.mongodb.MongoClientException;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.MongoDriverInformation;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.assertions.Assertions;
import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.ClientSession;
import com.mongodb.client.ListDatabasesIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.internal.MongoDatabaseImpl;
import com.mongodb.client.internal.MongoIterables;
import com.mongodb.client.internal.SimpleMongoClient;
import com.mongodb.client.model.changestream.ChangeStreamLevel;
import com.mongodb.internal.connection.ServerAddressHelper;
import com.mongodb.lang.Nullable;
import java.io.Closeable;
import java.util.Collections;
import java.util.List;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

public class MongoClient
extends Mongo
implements Closeable {
    public static CodecRegistry getDefaultCodecRegistry() {
        return MongoClientSettings.getDefaultCodecRegistry();
    }

    public MongoClient() {
        this(new ServerAddress());
    }

    public MongoClient(String host) {
        this(ServerAddressHelper.createServerAddress(host));
    }

    public MongoClient(String host, MongoClientOptions options) {
        this(ServerAddressHelper.createServerAddress(host), options);
    }

    public MongoClient(String host, int port) {
        this(ServerAddressHelper.createServerAddress(host, port));
    }

    public MongoClient(ServerAddress addr) {
        this(addr, new MongoClientOptions.Builder().build());
    }

    @Deprecated
    public MongoClient(ServerAddress addr, List<MongoCredential> credentialsList) {
        this(addr, credentialsList, MongoClientOptions.builder().build());
    }

    public MongoClient(ServerAddress addr, MongoClientOptions options) {
        super(addr, options);
    }

    @Deprecated
    public MongoClient(ServerAddress addr, List<MongoCredential> credentialsList, MongoClientOptions options) {
        super(addr, credentialsList, options);
    }

    public MongoClient(ServerAddress addr, MongoCredential credential, MongoClientOptions options) {
        super(addr, Collections.singletonList(credential), options);
    }

    public MongoClient(List<ServerAddress> seeds) {
        this(seeds, new MongoClientOptions.Builder().build());
    }

    @Deprecated
    public MongoClient(List<ServerAddress> seeds, List<MongoCredential> credentialsList) {
        this(seeds, credentialsList, new MongoClientOptions.Builder().build());
    }

    public MongoClient(List<ServerAddress> seeds, MongoClientOptions options) {
        super(seeds, options);
    }

    @Deprecated
    public MongoClient(List<ServerAddress> seeds, List<MongoCredential> credentialsList, MongoClientOptions options) {
        super(seeds, credentialsList, options);
    }

    public MongoClient(List<ServerAddress> seeds, MongoCredential credential, MongoClientOptions options) {
        super(seeds, Collections.singletonList(credential), options);
    }

    public MongoClient(MongoClientURI uri) {
        super(uri);
    }

    public MongoClient(MongoClientURI uri, MongoDriverInformation mongoDriverInformation) {
        super(uri, mongoDriverInformation);
    }

    @Deprecated
    public MongoClient(ServerAddress addr, List<MongoCredential> credentialsList, MongoClientOptions options, MongoDriverInformation mongoDriverInformation) {
        super(addr, credentialsList, options, mongoDriverInformation);
    }

    public MongoClient(ServerAddress addr, MongoCredential credential, MongoClientOptions options, MongoDriverInformation mongoDriverInformation) {
        super(addr, Collections.singletonList(credential), options, mongoDriverInformation);
    }

    @Deprecated
    public MongoClient(List<ServerAddress> seeds, List<MongoCredential> credentialsList, MongoClientOptions options, MongoDriverInformation mongoDriverInformation) {
        super(seeds, credentialsList, options, mongoDriverInformation);
    }

    public MongoClient(List<ServerAddress> seeds, MongoCredential credential, MongoClientOptions options, MongoDriverInformation mongoDriverInformation) {
        super(seeds, Collections.singletonList(credential), options, mongoDriverInformation);
    }

    @Override
    public MongoClientOptions getMongoClientOptions() {
        return super.getMongoClientOptions();
    }

    @Nullable
    public MongoCredential getCredential() {
        if (this.getCredentialsList().size() > 1) {
            throw new IllegalStateException("Instance constructed with more than one MongoCredential");
        }
        if (this.getCredentialsList().isEmpty()) {
            return null;
        }
        return this.getCredentialsList().get(0);
    }

    @Override
    @Deprecated
    public List<MongoCredential> getCredentialsList() {
        return super.getCredentialsList();
    }

    public MongoIterable<String> listDatabaseNames() {
        return this.createListDatabaseNamesIterable(null);
    }

    public MongoIterable<String> listDatabaseNames(ClientSession clientSession) {
        Assertions.notNull("clientSession", clientSession);
        return this.createListDatabaseNamesIterable(clientSession);
    }

    private MongoIterable<String> createListDatabaseNamesIterable(@Nullable ClientSession clientSession) {
        return this.createListDatabasesIterable(clientSession, BsonDocument.class).nameOnly(true).map(new Function<BsonDocument, String>(){

            @Override
            public String apply(BsonDocument result) {
                return result.getString("name").getValue();
            }
        });
    }

    public ListDatabasesIterable<Document> listDatabases() {
        return this.listDatabases(Document.class);
    }

    public <T> ListDatabasesIterable<T> listDatabases(Class<T> clazz) {
        return this.createListDatabasesIterable(null, clazz);
    }

    public ListDatabasesIterable<Document> listDatabases(ClientSession clientSession) {
        return this.listDatabases(clientSession, Document.class);
    }

    public <T> ListDatabasesIterable<T> listDatabases(ClientSession clientSession, Class<T> clazz) {
        Assertions.notNull("clientSession", clientSession);
        return this.createListDatabasesIterable(clientSession, clazz);
    }

    private <T> ListDatabasesIterable<T> createListDatabasesIterable(@Nullable ClientSession clientSession, Class<T> clazz) {
        return MongoIterables.listDatabasesOf(clientSession, clazz, this.getCodecRegistry(), ReadPreference.primary(), this.createOperationExecutor(), this.getMongoClientOptions().getRetryReads());
    }

    public MongoDatabase getDatabase(String databaseName) {
        MongoClientOptions clientOptions = this.getMongoClientOptions();
        return new MongoDatabaseImpl(databaseName, this.getCodecRegistry(), clientOptions.getReadPreference(), clientOptions.getWriteConcern(), clientOptions.getRetryWrites(), clientOptions.getRetryReads(), clientOptions.getReadConcern(), clientOptions.getUuidRepresentation(), this.createOperationExecutor());
    }

    public ClientSession startSession() {
        return this.startSession(ClientSessionOptions.builder().build());
    }

    public ClientSession startSession(ClientSessionOptions options) {
        ClientSession clientSession = this.createClientSession(Assertions.notNull("options", options));
        if (clientSession == null) {
            throw new MongoClientException("Sessions are not supported by the MongoDB cluster to which this client is connected");
        }
        return clientSession;
    }

    public ChangeStreamIterable<Document> watch() {
        return this.watch(Collections.emptyList());
    }

    public <TResult> ChangeStreamIterable<TResult> watch(Class<TResult> resultClass) {
        return this.watch(Collections.emptyList(), resultClass);
    }

    public ChangeStreamIterable<Document> watch(List<? extends Bson> pipeline) {
        return this.watch(pipeline, Document.class);
    }

    public <TResult> ChangeStreamIterable<TResult> watch(List<? extends Bson> pipeline, Class<TResult> resultClass) {
        return this.createChangeStreamIterable(null, pipeline, resultClass);
    }

    public ChangeStreamIterable<Document> watch(ClientSession clientSession) {
        return this.watch(clientSession, Collections.emptyList(), Document.class);
    }

    public <TResult> ChangeStreamIterable<TResult> watch(ClientSession clientSession, Class<TResult> resultClass) {
        return this.watch(clientSession, Collections.emptyList(), resultClass);
    }

    public ChangeStreamIterable<Document> watch(ClientSession clientSession, List<? extends Bson> pipeline) {
        return this.watch(clientSession, pipeline, Document.class);
    }

    public <TResult> ChangeStreamIterable<TResult> watch(ClientSession clientSession, List<? extends Bson> pipeline, Class<TResult> resultClass) {
        Assertions.notNull("clientSession", clientSession);
        return this.createChangeStreamIterable(clientSession, pipeline, resultClass);
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    SimpleMongoClient asSimpleMongoClient() {
        return new SimpleMongoClient(){

            @Override
            public MongoDatabase getDatabase(String databaseName) {
                return MongoClient.this.getDatabase(databaseName);
            }

            @Override
            public void close() {
                MongoClient.this.close();
            }
        };
    }

    private <TResult> ChangeStreamIterable<TResult> createChangeStreamIterable(@Nullable ClientSession clientSession, List<? extends Bson> pipeline, Class<TResult> resultClass) {
        MongoClientOptions clientOptions = this.getMongoClientOptions();
        return MongoIterables.changeStreamOf(clientSession, "admin", this.getCodecRegistry(), clientOptions.getReadPreference(), clientOptions.getReadConcern(), this.createOperationExecutor(), pipeline, resultClass, ChangeStreamLevel.CLIENT, clientOptions.getRetryReads());
    }

    static DBObjectCodec getCommandCodec() {
        return new DBObjectCodec(MongoClient.getDefaultCodecRegistry());
    }
}

