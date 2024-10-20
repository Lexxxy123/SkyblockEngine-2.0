/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.AutoEncryptionSettings;
import com.mongodb.BasicDBObject;
import com.mongodb.Bytes;
import com.mongodb.ClientSessionOptions;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.DBObjects;
import com.mongodb.Function;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.MongoDriverInformation;
import com.mongodb.MongoNamespace;
import com.mongodb.MongoOptions;
import com.mongodb.MongoURI;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.ReplicaSetStatus;
import com.mongodb.ServerAddress;
import com.mongodb.ServerCursor;
import com.mongodb.WriteConcern;
import com.mongodb.annotations.ThreadSafe;
import com.mongodb.binding.ConnectionSource;
import com.mongodb.binding.SingleServerBinding;
import com.mongodb.client.ClientSession;
import com.mongodb.client.internal.Crypts;
import com.mongodb.client.internal.MongoClientDelegate;
import com.mongodb.client.internal.MongoIterableImpl;
import com.mongodb.client.internal.OperationExecutor;
import com.mongodb.client.internal.SimpleMongoClient;
import com.mongodb.connection.BufferProvider;
import com.mongodb.connection.Cluster;
import com.mongodb.connection.ClusterConnectionMode;
import com.mongodb.connection.ClusterDescription;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.ClusterType;
import com.mongodb.connection.Connection;
import com.mongodb.connection.DefaultClusterFactory;
import com.mongodb.connection.ServerDescription;
import com.mongodb.connection.SocketStreamFactory;
import com.mongodb.event.ClusterListener;
import com.mongodb.internal.connection.PowerOfTwoBufferPool;
import com.mongodb.internal.event.EventListenerHelper;
import com.mongodb.internal.session.ServerSessionPool;
import com.mongodb.internal.thread.DaemonThreadFactory;
import com.mongodb.lang.Nullable;
import com.mongodb.operation.BatchCursor;
import com.mongodb.operation.CurrentOpOperation;
import com.mongodb.operation.FsyncUnlockOperation;
import com.mongodb.operation.ListDatabasesOperation;
import com.mongodb.operation.ReadOperation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.bson.BsonBoolean;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.internal.CodecRegistryHelper;

@ThreadSafe
@Deprecated
public class Mongo {
    static final String ADMIN_DATABASE_NAME = "admin";
    private final ConcurrentMap<String, DB> dbCache = new ConcurrentHashMap<String, DB>();
    private volatile WriteConcern writeConcern;
    private volatile ReadPreference readPreference;
    private final ReadConcern readConcern;
    private final MongoClientOptions options;
    private final List<MongoCredential> credentialsList;
    private final Bytes.OptionHolder optionHolder;
    private final BufferProvider bufferProvider = new PowerOfTwoBufferPool();
    private final ConcurrentLinkedQueue<ServerCursorAndNamespace> orphanedCursors = new ConcurrentLinkedQueue();
    private final ExecutorService cursorCleaningService;
    private final MongoClientDelegate delegate;
    private final AtomicBoolean closed;

    @Deprecated
    public Mongo() {
        this(new ServerAddress(), Mongo.createLegacyOptions());
    }

    @Deprecated
    public Mongo(String host) {
        this(new ServerAddress(host), Mongo.createLegacyOptions());
    }

    @Deprecated
    public Mongo(String host, MongoOptions options) {
        this(new ServerAddress(host), options.toClientOptions());
    }

    @Deprecated
    public Mongo(String host, int port) {
        this(new ServerAddress(host, port), Mongo.createLegacyOptions());
    }

    @Deprecated
    public Mongo(ServerAddress address) {
        this(address, Mongo.createLegacyOptions());
    }

    @Deprecated
    public Mongo(ServerAddress address, MongoOptions options) {
        this(address, options.toClientOptions());
    }

    @Deprecated
    public Mongo(ServerAddress left, ServerAddress right) {
        this(Arrays.asList(left, right), Mongo.createLegacyOptions());
    }

    @Deprecated
    public Mongo(ServerAddress left, ServerAddress right, MongoOptions options) {
        this(Arrays.asList(left, right), options.toClientOptions());
    }

    @Deprecated
    public Mongo(List<ServerAddress> seeds) {
        this(seeds, Mongo.createLegacyOptions());
    }

    @Deprecated
    public Mongo(List<ServerAddress> seeds, MongoOptions options) {
        this(seeds, options.toClientOptions());
    }

    @Deprecated
    public Mongo(MongoURI uri) {
        this(uri.toClientURI());
    }

    Mongo(List<ServerAddress> seedList, MongoClientOptions options) {
        this(seedList, Collections.emptyList(), options);
    }

    Mongo(ServerAddress serverAddress, MongoClientOptions options) {
        this(serverAddress, Collections.emptyList(), options);
    }

    Mongo(ServerAddress serverAddress, List<MongoCredential> credentialsList, MongoClientOptions options) {
        this(serverAddress, credentialsList, options, null);
    }

    Mongo(ServerAddress serverAddress, List<MongoCredential> credentialsList, MongoClientOptions options, @Nullable MongoDriverInformation mongoDriverInformation) {
        this(Mongo.createCluster(serverAddress, credentialsList, options, mongoDriverInformation), options, credentialsList);
    }

    Mongo(List<ServerAddress> seedList, List<MongoCredential> credentialsList, MongoClientOptions options) {
        this(seedList, credentialsList, options, null);
    }

    Mongo(List<ServerAddress> seedList, List<MongoCredential> credentialsList, MongoClientOptions options, @Nullable MongoDriverInformation mongoDriverInformation) {
        this(Mongo.createCluster(seedList, credentialsList, options, mongoDriverInformation), options, credentialsList);
    }

    Mongo(MongoClientURI mongoURI) {
        this(mongoURI, null);
    }

    Mongo(MongoClientURI mongoURI, @Nullable MongoDriverInformation mongoDriverInformation) {
        this(Mongo.createCluster(mongoURI, mongoDriverInformation), mongoURI.getOptions(), mongoURI.getCredentials() != null ? Arrays.asList(mongoURI.getCredentials()) : Collections.emptyList());
    }

    Mongo(Cluster cluster, MongoClientOptions options, List<MongoCredential> credentialsList) {
        this.options = options;
        this.readPreference = options.getReadPreference();
        this.writeConcern = options.getWriteConcern();
        this.readConcern = options.getReadConcern();
        this.optionHolder = new Bytes.OptionHolder(null);
        this.credentialsList = Collections.unmodifiableList(credentialsList);
        AutoEncryptionSettings autoEncryptionSettings = options.getAutoEncryptionSettings();
        this.delegate = new MongoClientDelegate(cluster, CodecRegistryHelper.createRegistry(options.getCodecRegistry(), options.getUuidRepresentation()), credentialsList, this, autoEncryptionSettings == null ? null : Crypts.createCrypt(this.asSimpleMongoClient(), autoEncryptionSettings));
        this.cursorCleaningService = options.isCursorFinalizerEnabled() ? this.createCursorCleaningService() : null;
        this.closed = new AtomicBoolean();
    }

    SimpleMongoClient asSimpleMongoClient() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public void setWriteConcern(WriteConcern writeConcern) {
        this.writeConcern = writeConcern;
    }

    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }

    public ReadConcern getReadConcern() {
        return this.readConcern;
    }

    @Deprecated
    public void setReadPreference(ReadPreference readPreference) {
        this.readPreference = readPreference;
    }

    public ReadPreference getReadPreference() {
        return this.readPreference;
    }

    @Deprecated
    public List<ServerAddress> getAllAddress() {
        return this.delegate.getCluster().getSettings().getHosts();
    }

    @Deprecated
    public List<ServerAddress> getServerAddressList() {
        return this.delegate.getServerAddressList();
    }

    private ClusterDescription getClusterDescription() {
        return this.delegate.getCluster().getDescription();
    }

    @Deprecated
    @Nullable
    public ServerAddress getAddress() {
        ClusterDescription description = this.getClusterDescription();
        if (description.getPrimaries().isEmpty()) {
            return null;
        }
        return description.getPrimaries().get(0).getAddress();
    }

    @Deprecated
    public MongoOptions getMongoOptions() {
        return new MongoOptions(this.getMongoClientOptions());
    }

    @Deprecated
    @Nullable
    public ReplicaSetStatus getReplicaSetStatus() {
        ClusterDescription clusterDescription = this.getClusterDescription();
        return clusterDescription.getType() == ClusterType.REPLICA_SET && clusterDescription.getConnectionMode() == ClusterConnectionMode.MULTIPLE ? new ReplicaSetStatus(this.delegate.getCluster()) : null;
    }

    @Deprecated
    public List<String> getDatabaseNames() {
        return new MongoIterableImpl<DBObject>(null, this.createOperationExecutor(), ReadConcern.DEFAULT, ReadPreference.primary(), this.options.getRetryReads()){

            @Override
            public ReadOperation<BatchCursor<DBObject>> asReadOperation() {
                return new ListDatabasesOperation<DBObject>(MongoClient.getCommandCodec());
            }
        }.map(new Function<DBObject, String>(){

            @Override
            public String apply(DBObject result) {
                return (String)result.get("name");
            }
        }).into(new ArrayList());
    }

    @Deprecated
    public DB getDB(String dbName) {
        DB db = (DB)this.dbCache.get(dbName);
        if (db != null) {
            return db;
        }
        db = new DB(this, dbName, this.createOperationExecutor());
        DB temp = this.dbCache.putIfAbsent(dbName, db);
        if (temp != null) {
            return temp;
        }
        return db;
    }

    @Deprecated
    public Collection<DB> getUsedDatabases() {
        return this.dbCache.values();
    }

    public void dropDatabase(String dbName) {
        this.getDB(dbName).dropDatabase();
    }

    public void close() {
        if (!this.closed.getAndSet(true)) {
            this.delegate.close();
            if (this.cursorCleaningService != null) {
                this.cursorCleaningService.shutdownNow();
            }
        }
    }

    @Deprecated
    public void slaveOk() {
        this.addOption(4);
    }

    @Deprecated
    public void setOptions(int options) {
        this.optionHolder.set(options);
    }

    @Deprecated
    public void resetOptions() {
        this.optionHolder.reset();
    }

    @Deprecated
    public void addOption(int option) {
        this.optionHolder.add(option);
    }

    @Deprecated
    public int getOptions() {
        return this.optionHolder.get();
    }

    @Deprecated
    public CommandResult fsync(boolean async) {
        BasicDBObject command = new BasicDBObject("fsync", (Object)1);
        if (async) {
            command.put("async", (Object)1);
        }
        return this.getDB(ADMIN_DATABASE_NAME).command(command);
    }

    @Deprecated
    public CommandResult fsyncAndLock() {
        BasicDBObject command = new BasicDBObject("fsync", (Object)1);
        command.put("lock", (Object)1);
        return this.getDB(ADMIN_DATABASE_NAME).command(command);
    }

    @Deprecated
    public DBObject unlock() {
        return DBObjects.toDBObject(this.createOperationExecutor().execute(new FsyncUnlockOperation(), this.readPreference, this.readConcern));
    }

    @Deprecated
    public boolean isLocked() {
        return this.createOperationExecutor().execute(new CurrentOpOperation(), ReadPreference.primary(), this.readConcern).getBoolean("fsyncLock", BsonBoolean.FALSE).getValue();
    }

    public String toString() {
        return "Mongo{options=" + this.getMongoClientOptions() + '}';
    }

    @Deprecated
    public int getMaxBsonObjectSize() {
        List<ServerDescription> primaries = this.getClusterDescription().getPrimaries();
        return primaries.isEmpty() ? ServerDescription.getDefaultMaxDocumentSize() : primaries.get(0).getMaxDocumentSize();
    }

    @Deprecated
    @Nullable
    public String getConnectPoint() {
        ServerAddress master = this.getAddress();
        return master != null ? String.format("%s:%d", master.getHost(), master.getPort()) : null;
    }

    private static MongoClientOptions createLegacyOptions() {
        return MongoClientOptions.builder().legacyDefaults().build();
    }

    private static Cluster createCluster(MongoClientURI mongoURI, @Nullable MongoDriverInformation mongoDriverInformation) {
        List<MongoCredential> credentialList = mongoURI.getCredentials() != null ? Collections.singletonList(mongoURI.getCredentials()) : Collections.emptyList();
        return Mongo.createCluster(Mongo.getClusterSettings(ClusterSettings.builder().applyConnectionString(mongoURI.getProxied()), mongoURI.getOptions()), credentialList, mongoURI.getOptions(), mongoDriverInformation);
    }

    private static Cluster createCluster(List<ServerAddress> seedList, List<MongoCredential> credentialsList, MongoClientOptions options, @Nullable MongoDriverInformation mongoDriverInformation) {
        return Mongo.createCluster(Mongo.getClusterSettings(seedList, options, ClusterConnectionMode.MULTIPLE), credentialsList, options, mongoDriverInformation);
    }

    private static Cluster createCluster(ServerAddress serverAddress, List<MongoCredential> credentialsList, MongoClientOptions options, @Nullable MongoDriverInformation mongoDriverInformation) {
        return Mongo.createCluster(Mongo.getClusterSettings(Collections.singletonList(serverAddress), options, Mongo.getSingleServerClusterMode(options)), credentialsList, options, mongoDriverInformation);
    }

    private static Cluster createCluster(ClusterSettings clusterSettings, List<MongoCredential> credentialsList, MongoClientOptions options, @Nullable MongoDriverInformation mongoDriverInformation) {
        MongoDriverInformation.Builder builder = mongoDriverInformation == null ? MongoDriverInformation.builder() : MongoDriverInformation.builder(mongoDriverInformation);
        return new DefaultClusterFactory().createCluster(clusterSettings, options.getServerSettings(), options.getConnectionPoolSettings(), new SocketStreamFactory(options.getSocketSettings(), options.getSslSettings(), options.getSocketFactory()), new SocketStreamFactory(options.getHeartbeatSocketSettings(), options.getSslSettings(), options.getSocketFactory()), credentialsList, EventListenerHelper.getCommandListener(options.getCommandListeners()), options.getApplicationName(), builder.driverName("legacy").build(), options.getCompressorList());
    }

    private static ClusterSettings getClusterSettings(ClusterSettings.Builder builder, MongoClientOptions options) {
        builder.requiredReplicaSetName(options.getRequiredReplicaSetName()).serverSelectionTimeout(options.getServerSelectionTimeout(), TimeUnit.MILLISECONDS).localThreshold(options.getLocalThreshold(), TimeUnit.MILLISECONDS).serverSelector(options.getServerSelector()).description(options.getDescription()).maxWaitQueueSize(options.getConnectionPoolSettings().getMaxWaitQueueSize());
        for (ClusterListener clusterListener : options.getClusterListeners()) {
            builder.addClusterListener(clusterListener);
        }
        return builder.build();
    }

    private static ClusterSettings getClusterSettings(List<ServerAddress> seedList, MongoClientOptions options, ClusterConnectionMode clusterConnectionMode) {
        return Mongo.getClusterSettings(ClusterSettings.builder().hosts(new ArrayList<ServerAddress>(seedList)).mode(clusterConnectionMode), options);
    }

    Cluster getCluster() {
        return this.delegate.getCluster();
    }

    MongoClientDelegate getDelegate() {
        return this.delegate;
    }

    CodecRegistry getCodecRegistry() {
        return this.delegate.getCodecRegistry();
    }

    ServerSessionPool getServerSessionPool() {
        return this.delegate.getServerSessionPool();
    }

    Bytes.OptionHolder getOptionHolder() {
        return this.optionHolder;
    }

    BufferProvider getBufferProvider() {
        return this.bufferProvider;
    }

    MongoClientOptions getMongoClientOptions() {
        return this.options;
    }

    List<MongoCredential> getCredentialsList() {
        return this.credentialsList;
    }

    void addOrphanedCursor(ServerCursor serverCursor, MongoNamespace namespace) {
        this.orphanedCursors.add(new ServerCursorAndNamespace(serverCursor, namespace));
    }

    OperationExecutor createOperationExecutor() {
        return this.delegate.getOperationExecutor();
    }

    @Nullable
    ClientSession createClientSession(ClientSessionOptions options) {
        return this.delegate.createClientSession(options, this.readConcern, this.writeConcern, this.readPreference);
    }

    private ExecutorService createCursorCleaningService() {
        ScheduledExecutorService newTimer = Executors.newSingleThreadScheduledExecutor(new DaemonThreadFactory("CleanCursors"));
        newTimer.scheduleAtFixedRate(new Runnable(){

            @Override
            public void run() {
                Mongo.this.cleanCursors();
            }
        }, 1L, 1L, TimeUnit.SECONDS);
        return newTimer;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void cleanCursors() {
        ServerCursorAndNamespace cur;
        while ((cur = this.orphanedCursors.poll()) != null) {
            SingleServerBinding binding = new SingleServerBinding(this.delegate.getCluster(), cur.serverCursor.getAddress());
            try {
                ConnectionSource source = binding.getReadConnectionSource();
                try {
                    Connection connection = source.getConnection();
                    try {
                        connection.killCursor(cur.namespace, Collections.singletonList(cur.serverCursor.getId()));
                    } finally {
                        connection.release();
                    }
                } finally {
                    source.release();
                }
            } finally {
                binding.release();
            }
        }
    }

    private static ClusterConnectionMode getSingleServerClusterMode(MongoClientOptions options) {
        if (options.getRequiredReplicaSetName() == null) {
            return ClusterConnectionMode.SINGLE;
        }
        return ClusterConnectionMode.MULTIPLE;
    }

    @Deprecated
    public static class Holder {
        private static final Holder INSTANCE = new Holder();
        private final ConcurrentMap<String, Mongo> clients = new ConcurrentHashMap<String, Mongo>();

        public static Holder singleton() {
            return INSTANCE;
        }

        @Deprecated
        public Mongo connect(MongoURI uri) {
            return this.connect(uri.toClientURI());
        }

        public Mongo connect(MongoClientURI uri) {
            String key = this.toKey(uri);
            Mongo client = (Mongo)this.clients.get(key);
            if (client == null) {
                MongoClient newbie = new MongoClient(uri);
                client = this.clients.putIfAbsent(key, newbie);
                if (client == null) {
                    client = newbie;
                } else {
                    ((Mongo)newbie).close();
                }
            }
            return client;
        }

        private String toKey(MongoClientURI uri) {
            return uri.toString();
        }
    }

    private static class ServerCursorAndNamespace {
        private final ServerCursor serverCursor;
        private final MongoNamespace namespace;

        ServerCursorAndNamespace(ServerCursor serverCursor, MongoNamespace namespace) {
            this.serverCursor = serverCursor;
            this.namespace = namespace;
        }
    }
}

