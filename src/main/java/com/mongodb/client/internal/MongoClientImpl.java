/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.internal;

import com.mongodb.AutoEncryptionSettings;
import com.mongodb.ClientSessionOptions;
import com.mongodb.Function;
import com.mongodb.MongoClientException;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.MongoDriverInformation;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.assertions.Assertions;
import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.ClientSession;
import com.mongodb.client.ListDatabasesIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.internal.Crypts;
import com.mongodb.client.internal.MongoClientDelegate;
import com.mongodb.client.internal.MongoDatabaseImpl;
import com.mongodb.client.internal.MongoIterables;
import com.mongodb.client.internal.OperationExecutor;
import com.mongodb.client.internal.SimpleMongoClients;
import com.mongodb.client.model.changestream.ChangeStreamLevel;
import com.mongodb.connection.Cluster;
import com.mongodb.connection.ClusterDescription;
import com.mongodb.connection.DefaultClusterFactory;
import com.mongodb.connection.SocketSettings;
import com.mongodb.connection.SocketStreamFactory;
import com.mongodb.connection.StreamFactory;
import com.mongodb.connection.StreamFactoryFactory;
import com.mongodb.internal.event.EventListenerHelper;
import com.mongodb.lang.Nullable;
import java.util.Collections;
import java.util.List;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.bson.internal.CodecRegistryHelper;

public final class MongoClientImpl
implements MongoClient {
    private final MongoClientSettings settings;
    private final MongoClientDelegate delegate;

    public MongoClientImpl(MongoClientSettings settings, @Nullable MongoDriverInformation mongoDriverInformation) {
        this(MongoClientImpl.createCluster(settings, mongoDriverInformation), settings, null);
    }

    public MongoClientImpl(Cluster cluster, MongoClientSettings settings, @Nullable OperationExecutor operationExecutor) {
        this.settings = Assertions.notNull("settings", settings);
        AutoEncryptionSettings autoEncryptionSettings = settings.getAutoEncryptionSettings();
        this.delegate = new MongoClientDelegate(Assertions.notNull("cluster", cluster), CodecRegistryHelper.createRegistry(settings.getCodecRegistry(), settings.getUuidRepresentation()), Collections.singletonList(settings.getCredential()), this, operationExecutor, autoEncryptionSettings == null ? null : Crypts.createCrypt(SimpleMongoClients.create(this), autoEncryptionSettings));
    }

    @Override
    public MongoDatabase getDatabase(String databaseName) {
        return new MongoDatabaseImpl(databaseName, this.delegate.getCodecRegistry(), this.settings.getReadPreference(), this.settings.getWriteConcern(), this.settings.getRetryWrites(), this.settings.getRetryReads(), this.settings.getReadConcern(), this.settings.getUuidRepresentation(), this.delegate.getOperationExecutor());
    }

    @Override
    public MongoIterable<String> listDatabaseNames() {
        return this.createListDatabaseNamesIterable(null);
    }

    @Override
    public MongoIterable<String> listDatabaseNames(ClientSession clientSession) {
        Assertions.notNull("clientSession", clientSession);
        return this.createListDatabaseNamesIterable(clientSession);
    }

    @Override
    public ListDatabasesIterable<Document> listDatabases() {
        return this.listDatabases((Class<T>)Document.class);
    }

    public <T> ListDatabasesIterable<T> listDatabases(Class<T> clazz) {
        return this.createListDatabasesIterable(null, clazz);
    }

    @Override
    public ListDatabasesIterable<Document> listDatabases(ClientSession clientSession) {
        return this.listDatabases(clientSession, (Class<T>)Document.class);
    }

    public <T> ListDatabasesIterable<T> listDatabases(ClientSession clientSession, Class<T> clazz) {
        Assertions.notNull("clientSession", clientSession);
        return this.createListDatabasesIterable(clientSession, clazz);
    }

    @Override
    public ClientSession startSession() {
        return this.startSession(ClientSessionOptions.builder().defaultTransactionOptions(TransactionOptions.builder().readConcern(this.settings.getReadConcern()).writeConcern(this.settings.getWriteConcern()).build()).build());
    }

    @Override
    public ClientSession startSession(ClientSessionOptions options) {
        ClientSession clientSession = this.delegate.createClientSession(Assertions.notNull("options", options), this.settings.getReadConcern(), this.settings.getWriteConcern(), this.settings.getReadPreference());
        if (clientSession == null) {
            throw new MongoClientException("Sessions are not supported by the MongoDB cluster to which this client is connected");
        }
        return clientSession;
    }

    @Override
    public void close() {
        this.delegate.close();
    }

    @Override
    public ChangeStreamIterable<Document> watch() {
        return this.watch(Collections.emptyList());
    }

    @Override
    public <TResult> ChangeStreamIterable<TResult> watch(Class<TResult> resultClass) {
        return this.watch(Collections.emptyList(), resultClass);
    }

    @Override
    public ChangeStreamIterable<Document> watch(List<? extends Bson> pipeline) {
        return this.watch(pipeline, Document.class);
    }

    @Override
    public <TResult> ChangeStreamIterable<TResult> watch(List<? extends Bson> pipeline, Class<TResult> resultClass) {
        return this.createChangeStreamIterable(null, pipeline, resultClass);
    }

    @Override
    public ChangeStreamIterable<Document> watch(ClientSession clientSession) {
        return this.watch(clientSession, Collections.emptyList(), Document.class);
    }

    @Override
    public <TResult> ChangeStreamIterable<TResult> watch(ClientSession clientSession, Class<TResult> resultClass) {
        return this.watch(clientSession, Collections.emptyList(), resultClass);
    }

    @Override
    public ChangeStreamIterable<Document> watch(ClientSession clientSession, List<? extends Bson> pipeline) {
        return this.watch(clientSession, pipeline, Document.class);
    }

    @Override
    public <TResult> ChangeStreamIterable<TResult> watch(ClientSession clientSession, List<? extends Bson> pipeline, Class<TResult> resultClass) {
        Assertions.notNull("clientSession", clientSession);
        return this.createChangeStreamIterable(clientSession, pipeline, resultClass);
    }

    @Override
    public ClusterDescription getClusterDescription() {
        return this.delegate.getCluster().getCurrentDescription();
    }

    private <TResult> ChangeStreamIterable<TResult> createChangeStreamIterable(@Nullable ClientSession clientSession, List<? extends Bson> pipeline, Class<TResult> resultClass) {
        return MongoIterables.changeStreamOf(clientSession, "admin", this.settings.getCodecRegistry(), this.settings.getReadPreference(), this.settings.getReadConcern(), this.delegate.getOperationExecutor(), pipeline, resultClass, ChangeStreamLevel.CLIENT, this.settings.getRetryReads());
    }

    public Cluster getCluster() {
        return this.delegate.getCluster();
    }

    public CodecRegistry getCodecRegistry() {
        return this.delegate.getCodecRegistry();
    }

    private static Cluster createCluster(MongoClientSettings settings, @Nullable MongoDriverInformation mongoDriverInformation) {
        Assertions.notNull("settings", settings);
        List<MongoCredential> credentialList = settings.getCredential() != null ? Collections.singletonList(settings.getCredential()) : Collections.emptyList();
        return new DefaultClusterFactory().createCluster(settings.getClusterSettings(), settings.getServerSettings(), settings.getConnectionPoolSettings(), MongoClientImpl.getStreamFactory(settings, false), MongoClientImpl.getStreamFactory(settings, true), credentialList, EventListenerHelper.getCommandListener(settings.getCommandListeners()), settings.getApplicationName(), mongoDriverInformation, settings.getCompressorList());
    }

    private static StreamFactory getStreamFactory(MongoClientSettings settings, boolean isHeartbeat) {
        SocketSettings socketSettings;
        StreamFactoryFactory streamFactoryFactory = settings.getStreamFactoryFactory();
        SocketSettings socketSettings2 = socketSettings = isHeartbeat ? settings.getHeartbeatSocketSettings() : settings.getSocketSettings();
        if (streamFactoryFactory == null) {
            return new SocketStreamFactory(socketSettings, settings.getSslSettings());
        }
        return streamFactoryFactory.create(socketSettings, settings.getSslSettings());
    }

    private <T> ListDatabasesIterable<T> createListDatabasesIterable(@Nullable ClientSession clientSession, Class<T> clazz) {
        return MongoIterables.listDatabasesOf(clientSession, clazz, this.delegate.getCodecRegistry(), ReadPreference.primary(), this.delegate.getOperationExecutor(), this.settings.getRetryReads());
    }

    private MongoIterable<String> createListDatabaseNamesIterable(@Nullable ClientSession clientSession) {
        return this.createListDatabasesIterable(clientSession, BsonDocument.class).nameOnly(true).map(new Function<BsonDocument, String>(){

            @Override
            public String apply(BsonDocument result) {
                return result.getString("name").getValue();
            }
        });
    }
}

