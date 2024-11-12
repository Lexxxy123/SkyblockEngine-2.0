/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.AutoEncryptionSettings;
import com.mongodb.DBDecoderFactory;
import com.mongodb.DBEncoderFactory;
import com.mongodb.DefaultDBDecoder;
import com.mongodb.DefaultDBEncoder;
import com.mongodb.MongoClient;
import com.mongodb.MongoCompressor;
import com.mongodb.MongoInternalException;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.annotations.Immutable;
import com.mongodb.annotations.NotThreadSafe;
import com.mongodb.assertions.Assertions;
import com.mongodb.connection.ConnectionPoolSettings;
import com.mongodb.connection.ServerSettings;
import com.mongodb.connection.SocketSettings;
import com.mongodb.connection.SslSettings;
import com.mongodb.event.ClusterListener;
import com.mongodb.event.CommandListener;
import com.mongodb.event.ConnectionPoolListener;
import com.mongodb.event.ServerListener;
import com.mongodb.event.ServerMonitorListener;
import com.mongodb.lang.Nullable;
import com.mongodb.selector.ServerSelector;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistry;

@Immutable
public class MongoClientOptions {
    private static final SocketFactory DEFAULT_SSL_SOCKET_FACTORY = SSLSocketFactory.getDefault();
    private static final SocketFactory DEFAULT_SOCKET_FACTORY = SocketFactory.getDefault();
    private final String description;
    private final String applicationName;
    private final List<MongoCompressor> compressorList;
    private final ReadPreference readPreference;
    private final WriteConcern writeConcern;
    private final boolean retryWrites;
    private final boolean retryReads;
    private final ReadConcern readConcern;
    private final CodecRegistry codecRegistry;
    private final UuidRepresentation uuidRepresentation;
    private final ServerSelector serverSelector;
    private final int minConnectionsPerHost;
    private final int maxConnectionsPerHost;
    private final int threadsAllowedToBlockForConnectionMultiplier;
    private final int serverSelectionTimeout;
    private final int maxWaitTime;
    private final int maxConnectionIdleTime;
    private final int maxConnectionLifeTime;
    private final int connectTimeout;
    private final int socketTimeout;
    private final boolean socketKeepAlive;
    private final boolean sslEnabled;
    private final boolean sslInvalidHostNameAllowed;
    private final SSLContext sslContext;
    private final boolean alwaysUseMBeans;
    private final int heartbeatFrequency;
    private final int minHeartbeatFrequency;
    private final int heartbeatConnectTimeout;
    private final int heartbeatSocketTimeout;
    private final int localThreshold;
    private final String requiredReplicaSetName;
    private final DBDecoderFactory dbDecoderFactory;
    private final DBEncoderFactory dbEncoderFactory;
    private final SocketFactory socketFactory;
    private final boolean cursorFinalizerEnabled;
    private final ConnectionPoolSettings connectionPoolSettings;
    private final SocketSettings socketSettings;
    private final ServerSettings serverSettings;
    private final SocketSettings heartbeatSocketSettings;
    private final SslSettings sslSettings;
    private final List<ClusterListener> clusterListeners;
    private final List<CommandListener> commandListeners;
    private final AutoEncryptionSettings autoEncryptionSettings;

    private MongoClientOptions(Builder builder) {
        this.description = builder.description;
        this.applicationName = builder.applicationName;
        this.compressorList = builder.compressorList;
        this.minConnectionsPerHost = builder.minConnectionsPerHost;
        this.maxConnectionsPerHost = builder.maxConnectionsPerHost;
        this.threadsAllowedToBlockForConnectionMultiplier = builder.threadsAllowedToBlockForConnectionMultiplier;
        this.serverSelectionTimeout = builder.serverSelectionTimeout;
        this.maxWaitTime = builder.maxWaitTime;
        this.maxConnectionIdleTime = builder.maxConnectionIdleTime;
        this.maxConnectionLifeTime = builder.maxConnectionLifeTime;
        this.connectTimeout = builder.connectTimeout;
        this.socketTimeout = builder.socketTimeout;
        this.socketKeepAlive = builder.socketKeepAlive;
        this.readPreference = builder.readPreference;
        this.writeConcern = builder.writeConcern;
        this.retryWrites = builder.retryWrites;
        this.retryReads = builder.retryReads;
        this.readConcern = builder.readConcern;
        this.codecRegistry = builder.codecRegistry;
        this.uuidRepresentation = builder.uuidRepresentation;
        this.serverSelector = builder.serverSelector;
        this.sslEnabled = builder.sslEnabled;
        this.sslInvalidHostNameAllowed = builder.sslInvalidHostNameAllowed;
        this.sslContext = builder.sslContext;
        this.alwaysUseMBeans = builder.alwaysUseMBeans;
        this.heartbeatFrequency = builder.heartbeatFrequency;
        this.minHeartbeatFrequency = builder.minHeartbeatFrequency;
        this.heartbeatConnectTimeout = builder.heartbeatConnectTimeout;
        this.heartbeatSocketTimeout = builder.heartbeatSocketTimeout;
        this.localThreshold = builder.localThreshold;
        this.requiredReplicaSetName = builder.requiredReplicaSetName;
        this.dbDecoderFactory = builder.dbDecoderFactory;
        this.dbEncoderFactory = builder.dbEncoderFactory;
        this.socketFactory = builder.socketFactory;
        this.cursorFinalizerEnabled = builder.cursorFinalizerEnabled;
        this.clusterListeners = Collections.unmodifiableList(builder.clusterListeners);
        this.commandListeners = Collections.unmodifiableList(builder.commandListeners);
        this.autoEncryptionSettings = builder.autoEncryptionSettings;
        ConnectionPoolSettings.Builder connectionPoolSettingsBuilder = ConnectionPoolSettings.builder().minSize(this.getMinConnectionsPerHost()).maxSize(this.getConnectionsPerHost()).maxWaitQueueSize(this.getThreadsAllowedToBlockForConnectionMultiplier() * this.getConnectionsPerHost()).maxWaitTime(this.getMaxWaitTime(), TimeUnit.MILLISECONDS).maxConnectionIdleTime(this.getMaxConnectionIdleTime(), TimeUnit.MILLISECONDS).maxConnectionLifeTime(this.getMaxConnectionLifeTime(), TimeUnit.MILLISECONDS);
        for (Iterator connectionPoolListener : builder.connectionPoolListeners) {
            connectionPoolSettingsBuilder.addConnectionPoolListener((ConnectionPoolListener)((Object)connectionPoolListener));
        }
        this.connectionPoolSettings = connectionPoolSettingsBuilder.build();
        this.socketSettings = SocketSettings.builder().connectTimeout(this.getConnectTimeout(), TimeUnit.MILLISECONDS).readTimeout(this.getSocketTimeout(), TimeUnit.MILLISECONDS).keepAlive(this.isSocketKeepAlive()).build();
        this.heartbeatSocketSettings = SocketSettings.builder().connectTimeout(this.getHeartbeatConnectTimeout(), TimeUnit.MILLISECONDS).readTimeout(this.getHeartbeatSocketTimeout(), TimeUnit.MILLISECONDS).keepAlive(this.isSocketKeepAlive()).build();
        ServerSettings.Builder serverSettingsBuilder = ServerSettings.builder().heartbeatFrequency(this.getHeartbeatFrequency(), TimeUnit.MILLISECONDS).minHeartbeatFrequency(this.getMinHeartbeatFrequency(), TimeUnit.MILLISECONDS);
        for (ServerListener serverListener : builder.serverListeners) {
            serverSettingsBuilder.addServerListener(serverListener);
        }
        for (ServerMonitorListener serverMonitorListener : builder.serverMonitorListeners) {
            serverSettingsBuilder.addServerMonitorListener(serverMonitorListener);
        }
        this.serverSettings = serverSettingsBuilder.build();
        try {
            this.sslSettings = SslSettings.builder().enabled(this.sslEnabled).invalidHostNameAllowed(this.sslInvalidHostNameAllowed).context(this.sslContext).build();
        } catch (MongoInternalException e2) {
            throw new MongoInternalException("By default, SSL connections are only supported on Java 7 or later.  If the application must run on Java 6, you must set the MongoClientOptions.sslInvalidHostNameAllowed property to true");
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(MongoClientOptions options) {
        return new Builder(options);
    }

    @Deprecated
    public String getDescription() {
        return this.description;
    }

    public String getApplicationName() {
        return this.applicationName;
    }

    public List<MongoCompressor> getCompressorList() {
        return this.compressorList;
    }

    public int getConnectionsPerHost() {
        return this.maxConnectionsPerHost;
    }

    public int getMinConnectionsPerHost() {
        return this.minConnectionsPerHost;
    }

    @Deprecated
    public int getThreadsAllowedToBlockForConnectionMultiplier() {
        return this.threadsAllowedToBlockForConnectionMultiplier;
    }

    public int getServerSelectionTimeout() {
        return this.serverSelectionTimeout;
    }

    public int getMaxWaitTime() {
        return this.maxWaitTime;
    }

    public int getMaxConnectionIdleTime() {
        return this.maxConnectionIdleTime;
    }

    public int getMaxConnectionLifeTime() {
        return this.maxConnectionLifeTime;
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public int getSocketTimeout() {
        return this.socketTimeout;
    }

    @Deprecated
    public boolean isSocketKeepAlive() {
        return this.socketKeepAlive;
    }

    public int getHeartbeatFrequency() {
        return this.heartbeatFrequency;
    }

    public int getMinHeartbeatFrequency() {
        return this.minHeartbeatFrequency;
    }

    public int getHeartbeatConnectTimeout() {
        return this.heartbeatConnectTimeout;
    }

    public int getHeartbeatSocketTimeout() {
        return this.heartbeatSocketTimeout;
    }

    public int getLocalThreshold() {
        return this.localThreshold;
    }

    @Nullable
    public String getRequiredReplicaSetName() {
        return this.requiredReplicaSetName;
    }

    public boolean isSslEnabled() {
        return this.sslEnabled;
    }

    public boolean isSslInvalidHostNameAllowed() {
        return this.sslInvalidHostNameAllowed;
    }

    public SSLContext getSslContext() {
        return this.sslContext;
    }

    public ReadPreference getReadPreference() {
        return this.readPreference;
    }

    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }

    public boolean getRetryWrites() {
        return this.retryWrites;
    }

    public boolean getRetryReads() {
        return this.retryReads;
    }

    public ReadConcern getReadConcern() {
        return this.readConcern;
    }

    public CodecRegistry getCodecRegistry() {
        return this.codecRegistry;
    }

    public UuidRepresentation getUuidRepresentation() {
        return this.uuidRepresentation;
    }

    public ServerSelector getServerSelector() {
        return this.serverSelector;
    }

    public List<ClusterListener> getClusterListeners() {
        return this.clusterListeners;
    }

    public List<CommandListener> getCommandListeners() {
        return this.commandListeners;
    }

    public List<ConnectionPoolListener> getConnectionPoolListeners() {
        return this.connectionPoolSettings.getConnectionPoolListeners();
    }

    public List<ServerListener> getServerListeners() {
        return this.serverSettings.getServerListeners();
    }

    public List<ServerMonitorListener> getServerMonitorListeners() {
        return this.serverSettings.getServerMonitorListeners();
    }

    public DBDecoderFactory getDbDecoderFactory() {
        return this.dbDecoderFactory;
    }

    public DBEncoderFactory getDbEncoderFactory() {
        return this.dbEncoderFactory;
    }

    @Deprecated
    public boolean isAlwaysUseMBeans() {
        return this.alwaysUseMBeans;
    }

    @Deprecated
    public SocketFactory getSocketFactory() {
        if (this.socketFactory != null) {
            return this.socketFactory;
        }
        if (this.getSslSettings().isEnabled()) {
            return this.sslContext == null ? DEFAULT_SSL_SOCKET_FACTORY : this.sslContext.getSocketFactory();
        }
        return DEFAULT_SOCKET_FACTORY;
    }

    public boolean isCursorFinalizerEnabled() {
        return this.cursorFinalizerEnabled;
    }

    @Nullable
    public AutoEncryptionSettings getAutoEncryptionSettings() {
        return this.autoEncryptionSettings;
    }

    ConnectionPoolSettings getConnectionPoolSettings() {
        return this.connectionPoolSettings;
    }

    SocketSettings getSocketSettings() {
        return this.socketSettings;
    }

    ServerSettings getServerSettings() {
        return this.serverSettings;
    }

    SocketSettings getHeartbeatSocketSettings() {
        return this.heartbeatSocketSettings;
    }

    SslSettings getSslSettings() {
        return this.sslSettings;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        MongoClientOptions that = (MongoClientOptions)o2;
        if (this.localThreshold != that.localThreshold) {
            return false;
        }
        if (this.alwaysUseMBeans != that.alwaysUseMBeans) {
            return false;
        }
        if (this.connectTimeout != that.connectTimeout) {
            return false;
        }
        if (this.cursorFinalizerEnabled != that.cursorFinalizerEnabled) {
            return false;
        }
        if (this.minHeartbeatFrequency != that.minHeartbeatFrequency) {
            return false;
        }
        if (this.heartbeatConnectTimeout != that.heartbeatConnectTimeout) {
            return false;
        }
        if (this.heartbeatFrequency != that.heartbeatFrequency) {
            return false;
        }
        if (this.heartbeatSocketTimeout != that.heartbeatSocketTimeout) {
            return false;
        }
        if (this.maxConnectionIdleTime != that.maxConnectionIdleTime) {
            return false;
        }
        if (this.maxConnectionLifeTime != that.maxConnectionLifeTime) {
            return false;
        }
        if (this.maxConnectionsPerHost != that.maxConnectionsPerHost) {
            return false;
        }
        if (this.serverSelectionTimeout != that.serverSelectionTimeout) {
            return false;
        }
        if (this.maxWaitTime != that.maxWaitTime) {
            return false;
        }
        if (this.minConnectionsPerHost != that.minConnectionsPerHost) {
            return false;
        }
        if (this.socketKeepAlive != that.socketKeepAlive) {
            return false;
        }
        if (this.socketTimeout != that.socketTimeout) {
            return false;
        }
        if (this.sslEnabled != that.sslEnabled) {
            return false;
        }
        if (this.sslInvalidHostNameAllowed != that.sslInvalidHostNameAllowed) {
            return false;
        }
        if (this.sslContext != null ? !this.sslContext.equals(that.sslContext) : that.sslContext != null) {
            return false;
        }
        if (this.threadsAllowedToBlockForConnectionMultiplier != that.threadsAllowedToBlockForConnectionMultiplier) {
            return false;
        }
        if (this.dbDecoderFactory != null ? !this.dbDecoderFactory.equals(that.dbDecoderFactory) : that.dbDecoderFactory != null) {
            return false;
        }
        if (this.dbEncoderFactory != null ? !this.dbEncoderFactory.equals(that.dbEncoderFactory) : that.dbEncoderFactory != null) {
            return false;
        }
        if (this.description != null ? !this.description.equals(that.description) : that.description != null) {
            return false;
        }
        if (this.applicationName != null ? !this.applicationName.equals(that.applicationName) : that.applicationName != null) {
            return false;
        }
        if (!this.readPreference.equals(that.readPreference)) {
            return false;
        }
        if (!this.writeConcern.equals(that.writeConcern)) {
            return false;
        }
        if (this.retryWrites != that.retryWrites) {
            return false;
        }
        if (this.retryReads != that.retryReads) {
            return false;
        }
        if (!this.readConcern.equals(that.readConcern)) {
            return false;
        }
        if (!this.codecRegistry.equals(that.codecRegistry)) {
            return false;
        }
        if (!this.uuidRepresentation.equals((Object)that.uuidRepresentation)) {
            return false;
        }
        if (this.serverSelector != null ? !this.serverSelector.equals(that.serverSelector) : that.serverSelector != null) {
            return false;
        }
        if (!this.clusterListeners.equals(that.clusterListeners)) {
            return false;
        }
        if (!this.commandListeners.equals(that.commandListeners)) {
            return false;
        }
        if (this.requiredReplicaSetName != null ? !this.requiredReplicaSetName.equals(that.requiredReplicaSetName) : that.requiredReplicaSetName != null) {
            return false;
        }
        if (this.socketFactory != null ? !this.socketFactory.equals(that.socketFactory) : that.socketFactory != null) {
            return false;
        }
        if (!this.compressorList.equals(that.compressorList)) {
            return false;
        }
        return !(this.autoEncryptionSettings != null ? !this.autoEncryptionSettings.equals(that.autoEncryptionSettings) : that.autoEncryptionSettings != null);
    }

    public int hashCode() {
        int result = this.description != null ? this.description.hashCode() : 0;
        result = 31 * result + (this.applicationName != null ? this.applicationName.hashCode() : 0);
        result = 31 * result + this.readPreference.hashCode();
        result = 31 * result + this.writeConcern.hashCode();
        result = 31 * result + (this.retryWrites ? 1 : 0);
        result = 31 * result + (this.retryReads ? 1 : 0);
        result = 31 * result + (this.readConcern != null ? this.readConcern.hashCode() : 0);
        result = 31 * result + this.codecRegistry.hashCode();
        result = 31 * result + this.uuidRepresentation.hashCode();
        result = 31 * result + (this.serverSelector != null ? this.serverSelector.hashCode() : 0);
        result = 31 * result + this.clusterListeners.hashCode();
        result = 31 * result + this.commandListeners.hashCode();
        result = 31 * result + this.minConnectionsPerHost;
        result = 31 * result + this.maxConnectionsPerHost;
        result = 31 * result + this.threadsAllowedToBlockForConnectionMultiplier;
        result = 31 * result + this.serverSelectionTimeout;
        result = 31 * result + this.maxWaitTime;
        result = 31 * result + this.maxConnectionIdleTime;
        result = 31 * result + this.maxConnectionLifeTime;
        result = 31 * result + this.connectTimeout;
        result = 31 * result + this.socketTimeout;
        result = 31 * result + (this.socketKeepAlive ? 1 : 0);
        result = 31 * result + (this.sslEnabled ? 1 : 0);
        result = 31 * result + (this.sslInvalidHostNameAllowed ? 1 : 0);
        result = 31 * result + (this.sslContext != null ? this.sslContext.hashCode() : 0);
        result = 31 * result + (this.alwaysUseMBeans ? 1 : 0);
        result = 31 * result + this.heartbeatFrequency;
        result = 31 * result + this.minHeartbeatFrequency;
        result = 31 * result + this.heartbeatConnectTimeout;
        result = 31 * result + this.heartbeatSocketTimeout;
        result = 31 * result + this.localThreshold;
        result = 31 * result + (this.requiredReplicaSetName != null ? this.requiredReplicaSetName.hashCode() : 0);
        result = 31 * result + (this.dbDecoderFactory != null ? this.dbDecoderFactory.hashCode() : 0);
        result = 31 * result + (this.dbEncoderFactory != null ? this.dbEncoderFactory.hashCode() : 0);
        result = 31 * result + (this.cursorFinalizerEnabled ? 1 : 0);
        result = 31 * result + (this.socketFactory != null ? this.socketFactory.hashCode() : 0);
        result = 31 * result + this.compressorList.hashCode();
        result = 31 * result + (this.autoEncryptionSettings != null ? this.autoEncryptionSettings.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "MongoClientOptions{description='" + this.description + '\'' + ", applicationName='" + this.applicationName + '\'' + ", compressors='" + this.compressorList + '\'' + ", readPreference=" + this.readPreference + ", writeConcern=" + this.writeConcern + ", retryWrites=" + this.retryWrites + ", retryReads=" + this.retryReads + ", readConcern=" + this.readConcern + ", codecRegistry=" + this.codecRegistry + ", uuidRepresentation=" + (Object)((Object)this.uuidRepresentation) + ", serverSelector=" + this.serverSelector + ", clusterListeners=" + this.clusterListeners + ", commandListeners=" + this.commandListeners + ", minConnectionsPerHost=" + this.minConnectionsPerHost + ", maxConnectionsPerHost=" + this.maxConnectionsPerHost + ", threadsAllowedToBlockForConnectionMultiplier=" + this.threadsAllowedToBlockForConnectionMultiplier + ", serverSelectionTimeout=" + this.serverSelectionTimeout + ", maxWaitTime=" + this.maxWaitTime + ", maxConnectionIdleTime=" + this.maxConnectionIdleTime + ", maxConnectionLifeTime=" + this.maxConnectionLifeTime + ", connectTimeout=" + this.connectTimeout + ", socketTimeout=" + this.socketTimeout + ", socketKeepAlive=" + this.socketKeepAlive + ", sslEnabled=" + this.sslEnabled + ", sslInvalidHostNamesAllowed=" + this.sslInvalidHostNameAllowed + ", sslContext=" + this.sslContext + ", alwaysUseMBeans=" + this.alwaysUseMBeans + ", heartbeatFrequency=" + this.heartbeatFrequency + ", minHeartbeatFrequency=" + this.minHeartbeatFrequency + ", heartbeatConnectTimeout=" + this.heartbeatConnectTimeout + ", heartbeatSocketTimeout=" + this.heartbeatSocketTimeout + ", localThreshold=" + this.localThreshold + ", requiredReplicaSetName='" + this.requiredReplicaSetName + '\'' + ", dbDecoderFactory=" + this.dbDecoderFactory + ", dbEncoderFactory=" + this.dbEncoderFactory + ", socketFactory=" + this.socketFactory + ", cursorFinalizerEnabled=" + this.cursorFinalizerEnabled + ", connectionPoolSettings=" + this.connectionPoolSettings + ", socketSettings=" + this.socketSettings + ", serverSettings=" + this.serverSettings + ", heartbeatSocketSettings=" + this.heartbeatSocketSettings + ", autoEncryptionSettings=" + this.autoEncryptionSettings + '}';
    }

    @NotThreadSafe
    public static class Builder {
        private final List<ClusterListener> clusterListeners = new ArrayList<ClusterListener>();
        private final List<CommandListener> commandListeners = new ArrayList<CommandListener>();
        private final List<ConnectionPoolListener> connectionPoolListeners = new ArrayList<ConnectionPoolListener>();
        private final List<ServerListener> serverListeners = new ArrayList<ServerListener>();
        private final List<ServerMonitorListener> serverMonitorListeners = new ArrayList<ServerMonitorListener>();
        private String description;
        private String applicationName;
        private List<MongoCompressor> compressorList = Collections.emptyList();
        private ReadPreference readPreference = ReadPreference.primary();
        private WriteConcern writeConcern = WriteConcern.ACKNOWLEDGED;
        private boolean retryWrites = true;
        private boolean retryReads = true;
        private ReadConcern readConcern = ReadConcern.DEFAULT;
        private CodecRegistry codecRegistry = MongoClient.getDefaultCodecRegistry();
        private UuidRepresentation uuidRepresentation = UuidRepresentation.JAVA_LEGACY;
        private ServerSelector serverSelector;
        private int minConnectionsPerHost;
        private int maxConnectionsPerHost = 100;
        private int threadsAllowedToBlockForConnectionMultiplier = 5;
        private int serverSelectionTimeout = 30000;
        private int maxWaitTime = 120000;
        private int maxConnectionIdleTime;
        private int maxConnectionLifeTime;
        private int connectTimeout = 10000;
        private int socketTimeout = 0;
        private boolean socketKeepAlive = true;
        private boolean sslEnabled = false;
        private boolean sslInvalidHostNameAllowed = false;
        private SSLContext sslContext;
        private boolean alwaysUseMBeans = false;
        private int heartbeatFrequency = 10000;
        private int minHeartbeatFrequency = 500;
        private int heartbeatConnectTimeout = 20000;
        private int heartbeatSocketTimeout = 20000;
        private int localThreshold = 15;
        private String requiredReplicaSetName;
        private DBDecoderFactory dbDecoderFactory = DefaultDBDecoder.FACTORY;
        private DBEncoderFactory dbEncoderFactory = DefaultDBEncoder.FACTORY;
        private SocketFactory socketFactory;
        private boolean cursorFinalizerEnabled = true;
        private AutoEncryptionSettings autoEncryptionSettings;

        public Builder() {
            this.heartbeatFrequency(Integer.parseInt(System.getProperty("com.mongodb.updaterIntervalMS", "10000")));
            this.minHeartbeatFrequency(Integer.parseInt(System.getProperty("com.mongodb.updaterIntervalNoMasterMS", "500")));
            this.heartbeatConnectTimeout(Integer.parseInt(System.getProperty("com.mongodb.updaterConnectTimeoutMS", "20000")));
            this.heartbeatSocketTimeout(Integer.parseInt(System.getProperty("com.mongodb.updaterSocketTimeoutMS", "20000")));
            this.localThreshold(Integer.parseInt(System.getProperty("com.mongodb.slaveAcceptableLatencyMS", "15")));
        }

        public Builder(MongoClientOptions options) {
            this.description = options.getDescription();
            this.applicationName = options.getApplicationName();
            this.compressorList = options.getCompressorList();
            this.minConnectionsPerHost = options.getMinConnectionsPerHost();
            this.maxConnectionsPerHost = options.getConnectionsPerHost();
            this.threadsAllowedToBlockForConnectionMultiplier = options.getThreadsAllowedToBlockForConnectionMultiplier();
            this.serverSelectionTimeout = options.getServerSelectionTimeout();
            this.maxWaitTime = options.getMaxWaitTime();
            this.maxConnectionIdleTime = options.getMaxConnectionIdleTime();
            this.maxConnectionLifeTime = options.getMaxConnectionLifeTime();
            this.connectTimeout = options.getConnectTimeout();
            this.socketTimeout = options.getSocketTimeout();
            this.socketKeepAlive = options.isSocketKeepAlive();
            this.readPreference = options.getReadPreference();
            this.writeConcern = options.getWriteConcern();
            this.retryWrites = options.getRetryWrites();
            this.retryReads = options.getRetryReads();
            this.readConcern = options.getReadConcern();
            this.codecRegistry = options.getCodecRegistry();
            this.uuidRepresentation = options.getUuidRepresentation();
            this.serverSelector = options.getServerSelector();
            this.sslEnabled = options.isSslEnabled();
            this.sslInvalidHostNameAllowed = options.isSslInvalidHostNameAllowed();
            this.sslContext = options.getSslContext();
            this.alwaysUseMBeans = options.isAlwaysUseMBeans();
            this.heartbeatFrequency = options.getHeartbeatFrequency();
            this.minHeartbeatFrequency = options.getMinHeartbeatFrequency();
            this.heartbeatConnectTimeout = options.getHeartbeatConnectTimeout();
            this.heartbeatSocketTimeout = options.getHeartbeatSocketTimeout();
            this.localThreshold = options.getLocalThreshold();
            this.requiredReplicaSetName = options.getRequiredReplicaSetName();
            this.dbDecoderFactory = options.getDbDecoderFactory();
            this.dbEncoderFactory = options.getDbEncoderFactory();
            this.socketFactory = options.socketFactory;
            this.cursorFinalizerEnabled = options.isCursorFinalizerEnabled();
            this.clusterListeners.addAll(options.getClusterListeners());
            this.commandListeners.addAll(options.getCommandListeners());
            this.connectionPoolListeners.addAll(options.getConnectionPoolListeners());
            this.serverListeners.addAll(options.getServerListeners());
            this.serverMonitorListeners.addAll(options.getServerMonitorListeners());
            this.autoEncryptionSettings = options.getAutoEncryptionSettings();
        }

        @Deprecated
        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder applicationName(String applicationName) {
            if (applicationName != null) {
                Assertions.isTrueArgument("applicationName UTF-8 encoding length <= 128", applicationName.getBytes(Charset.forName("UTF-8")).length <= 128);
            }
            this.applicationName = applicationName;
            return this;
        }

        public Builder compressorList(List<MongoCompressor> compressorList) {
            Assertions.notNull("compressorList", compressorList);
            this.compressorList = Collections.unmodifiableList(new ArrayList<MongoCompressor>(compressorList));
            return this;
        }

        public Builder minConnectionsPerHost(int minConnectionsPerHost) {
            Assertions.isTrueArgument("minConnectionsPerHost must be >= 0", minConnectionsPerHost >= 0);
            this.minConnectionsPerHost = minConnectionsPerHost;
            return this;
        }

        public Builder connectionsPerHost(int connectionsPerHost) {
            Assertions.isTrueArgument("connectionPerHost must be > 0", connectionsPerHost > 0);
            this.maxConnectionsPerHost = connectionsPerHost;
            return this;
        }

        @Deprecated
        public Builder threadsAllowedToBlockForConnectionMultiplier(int threadsAllowedToBlockForConnectionMultiplier) {
            Assertions.isTrueArgument("threadsAllowedToBlockForConnectionMultiplier must be > 0", threadsAllowedToBlockForConnectionMultiplier > 0);
            this.threadsAllowedToBlockForConnectionMultiplier = threadsAllowedToBlockForConnectionMultiplier;
            return this;
        }

        public Builder serverSelectionTimeout(int serverSelectionTimeout) {
            this.serverSelectionTimeout = serverSelectionTimeout;
            return this;
        }

        public Builder maxWaitTime(int maxWaitTime) {
            this.maxWaitTime = maxWaitTime;
            return this;
        }

        public Builder maxConnectionIdleTime(int maxConnectionIdleTime) {
            this.maxConnectionIdleTime = maxConnectionIdleTime;
            return this;
        }

        public Builder maxConnectionLifeTime(int maxConnectionLifeTime) {
            this.maxConnectionLifeTime = maxConnectionLifeTime;
            return this;
        }

        public Builder connectTimeout(int connectTimeout) {
            Assertions.isTrueArgument("connectTimeout must be >= 0", connectTimeout >= 0);
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder socketTimeout(int socketTimeout) {
            this.socketTimeout = socketTimeout;
            return this;
        }

        @Deprecated
        public Builder socketKeepAlive(boolean socketKeepAlive) {
            this.socketKeepAlive = socketKeepAlive;
            return this;
        }

        public Builder sslEnabled(boolean sslEnabled) {
            this.sslEnabled = sslEnabled;
            return this;
        }

        public Builder sslInvalidHostNameAllowed(boolean sslInvalidHostNameAllowed) {
            this.sslInvalidHostNameAllowed = sslInvalidHostNameAllowed;
            return this;
        }

        public Builder sslContext(SSLContext sslContext) {
            this.sslContext = sslContext;
            return this;
        }

        public Builder readPreference(ReadPreference readPreference) {
            this.readPreference = Assertions.notNull("readPreference", readPreference);
            return this;
        }

        public Builder writeConcern(WriteConcern writeConcern) {
            this.writeConcern = Assertions.notNull("writeConcern", writeConcern);
            return this;
        }

        public Builder retryWrites(boolean retryWrites) {
            this.retryWrites = retryWrites;
            return this;
        }

        public Builder retryReads(boolean retryReads) {
            this.retryReads = retryReads;
            return this;
        }

        public Builder readConcern(ReadConcern readConcern) {
            this.readConcern = Assertions.notNull("readConcern", readConcern);
            return this;
        }

        public Builder codecRegistry(CodecRegistry codecRegistry) {
            this.codecRegistry = Assertions.notNull("codecRegistry", codecRegistry);
            return this;
        }

        public Builder uuidRepresentation(UuidRepresentation uuidRepresentation) {
            this.uuidRepresentation = Assertions.notNull("uuidRepresentation", uuidRepresentation);
            return this;
        }

        public Builder serverSelector(ServerSelector serverSelector) {
            this.serverSelector = serverSelector;
            return this;
        }

        public Builder addCommandListener(CommandListener commandListener) {
            this.commandListeners.add(Assertions.notNull("commandListener", commandListener));
            return this;
        }

        public Builder addConnectionPoolListener(ConnectionPoolListener connectionPoolListener) {
            this.connectionPoolListeners.add(Assertions.notNull("connectionPoolListener", connectionPoolListener));
            return this;
        }

        public Builder addClusterListener(ClusterListener clusterListener) {
            this.clusterListeners.add(Assertions.notNull("clusterListener", clusterListener));
            return this;
        }

        public Builder addServerListener(ServerListener serverListener) {
            this.serverListeners.add(Assertions.notNull("serverListener", serverListener));
            return this;
        }

        public Builder addServerMonitorListener(ServerMonitorListener serverMonitorListener) {
            this.serverMonitorListeners.add(Assertions.notNull("serverMonitorListener", serverMonitorListener));
            return this;
        }

        @Deprecated
        public Builder socketFactory(SocketFactory socketFactory) {
            this.socketFactory = socketFactory;
            return this;
        }

        public Builder cursorFinalizerEnabled(boolean cursorFinalizerEnabled) {
            this.cursorFinalizerEnabled = cursorFinalizerEnabled;
            return this;
        }

        @Deprecated
        public Builder alwaysUseMBeans(boolean alwaysUseMBeans) {
            this.alwaysUseMBeans = alwaysUseMBeans;
            return this;
        }

        public Builder dbDecoderFactory(DBDecoderFactory dbDecoderFactory) {
            if (dbDecoderFactory == null) {
                throw new IllegalArgumentException("null is not a legal value");
            }
            this.dbDecoderFactory = dbDecoderFactory;
            return this;
        }

        public Builder dbEncoderFactory(DBEncoderFactory dbEncoderFactory) {
            if (dbEncoderFactory == null) {
                throw new IllegalArgumentException("null is not a legal value");
            }
            this.dbEncoderFactory = dbEncoderFactory;
            return this;
        }

        public Builder heartbeatFrequency(int heartbeatFrequency) {
            Assertions.isTrueArgument("heartbeatFrequency must be > 0", heartbeatFrequency > 0);
            this.heartbeatFrequency = heartbeatFrequency;
            return this;
        }

        public Builder minHeartbeatFrequency(int minHeartbeatFrequency) {
            Assertions.isTrueArgument("minHeartbeatFrequency must be > 0", minHeartbeatFrequency > 0);
            this.minHeartbeatFrequency = minHeartbeatFrequency;
            return this;
        }

        public Builder heartbeatConnectTimeout(int connectTimeout) {
            this.heartbeatConnectTimeout = connectTimeout;
            return this;
        }

        public Builder heartbeatSocketTimeout(int socketTimeout) {
            this.heartbeatSocketTimeout = socketTimeout;
            return this;
        }

        public Builder localThreshold(int localThreshold) {
            Assertions.isTrueArgument("localThreshold must be >= 0", localThreshold >= 0);
            this.localThreshold = localThreshold;
            return this;
        }

        public Builder requiredReplicaSetName(String requiredReplicaSetName) {
            this.requiredReplicaSetName = requiredReplicaSetName;
            return this;
        }

        public Builder autoEncryptionSettings(AutoEncryptionSettings autoEncryptionSettings) {
            this.autoEncryptionSettings = autoEncryptionSettings;
            return this;
        }

        @Deprecated
        public Builder legacyDefaults() {
            this.connectionsPerHost(10).writeConcern(WriteConcern.UNACKNOWLEDGED);
            return this;
        }

        public MongoClientOptions build() {
            return new MongoClientOptions(this);
        }
    }
}

