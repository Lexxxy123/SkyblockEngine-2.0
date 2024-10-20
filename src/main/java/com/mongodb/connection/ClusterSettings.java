/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.connection;

import com.mongodb.ConnectionString;
import com.mongodb.ServerAddress;
import com.mongodb.annotations.Immutable;
import com.mongodb.annotations.NotThreadSafe;
import com.mongodb.assertions.Assertions;
import com.mongodb.connection.ClusterConnectionMode;
import com.mongodb.connection.ClusterType;
import com.mongodb.event.ClusterListener;
import com.mongodb.internal.connection.ServerAddressHelper;
import com.mongodb.selector.CompositeServerSelector;
import com.mongodb.selector.LatencyMinimizingServerSelector;
import com.mongodb.selector.ServerSelector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Immutable
public final class ClusterSettings {
    private final String srvHost;
    private final List<ServerAddress> hosts;
    private final ClusterConnectionMode mode;
    private final ClusterType requiredClusterType;
    private final String requiredReplicaSetName;
    private final ServerSelector serverSelector;
    private final String description;
    private final long localThresholdMS;
    private final long serverSelectionTimeoutMS;
    private final int maxWaitQueueSize;
    private final List<ClusterListener> clusterListeners;

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(ClusterSettings clusterSettings) {
        return ClusterSettings.builder().applySettings(clusterSettings);
    }

    @Deprecated
    public String getDescription() {
        return this.description;
    }

    public String getSrvHost() {
        return this.srvHost;
    }

    public List<ServerAddress> getHosts() {
        return this.hosts;
    }

    public ClusterConnectionMode getMode() {
        return this.mode;
    }

    public ClusterType getRequiredClusterType() {
        return this.requiredClusterType;
    }

    public String getRequiredReplicaSetName() {
        return this.requiredReplicaSetName;
    }

    public ServerSelector getServerSelector() {
        return this.serverSelector;
    }

    public long getServerSelectionTimeout(TimeUnit timeUnit) {
        return timeUnit.convert(this.serverSelectionTimeoutMS, TimeUnit.MILLISECONDS);
    }

    public long getLocalThreshold(TimeUnit timeUnit) {
        return timeUnit.convert(this.localThresholdMS, TimeUnit.MILLISECONDS);
    }

    @Deprecated
    public int getMaxWaitQueueSize() {
        return this.maxWaitQueueSize;
    }

    public List<ClusterListener> getClusterListeners() {
        return this.clusterListeners;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        ClusterSettings that = (ClusterSettings)o2;
        if (this.maxWaitQueueSize != that.maxWaitQueueSize) {
            return false;
        }
        if (this.serverSelectionTimeoutMS != that.serverSelectionTimeoutMS) {
            return false;
        }
        if (this.localThresholdMS != that.localThresholdMS) {
            return false;
        }
        if (this.description != null ? !this.description.equals(that.description) : that.description != null) {
            return false;
        }
        if (this.srvHost != null ? !this.srvHost.equals(that.srvHost) : that.srvHost != null) {
            return false;
        }
        if (!this.hosts.equals(that.hosts)) {
            return false;
        }
        if (this.mode != that.mode) {
            return false;
        }
        if (this.requiredClusterType != that.requiredClusterType) {
            return false;
        }
        if (this.requiredReplicaSetName != null ? !this.requiredReplicaSetName.equals(that.requiredReplicaSetName) : that.requiredReplicaSetName != null) {
            return false;
        }
        if (this.serverSelector != null ? !this.serverSelector.equals(that.serverSelector) : that.serverSelector != null) {
            return false;
        }
        return this.clusterListeners.equals(that.clusterListeners);
    }

    public int hashCode() {
        int result = this.hosts.hashCode();
        result = 31 * result + (this.srvHost != null ? this.srvHost.hashCode() : 0);
        result = 31 * result + this.mode.hashCode();
        result = 31 * result + this.requiredClusterType.hashCode();
        result = 31 * result + (this.requiredReplicaSetName != null ? this.requiredReplicaSetName.hashCode() : 0);
        result = 31 * result + (this.serverSelector != null ? this.serverSelector.hashCode() : 0);
        result = 31 * result + (this.description != null ? this.description.hashCode() : 0);
        result = 31 * result + (int)(this.serverSelectionTimeoutMS ^ this.serverSelectionTimeoutMS >>> 32);
        result = 31 * result + (int)(this.localThresholdMS ^ this.localThresholdMS >>> 32);
        result = 31 * result + this.maxWaitQueueSize;
        result = 31 * result + this.clusterListeners.hashCode();
        return result;
    }

    public String toString() {
        return "{" + (this.hosts.isEmpty() ? "" : "hosts=" + this.hosts) + (this.srvHost == null ? "" : ", srvHost=" + this.srvHost) + ", mode=" + (Object)((Object)this.mode) + ", requiredClusterType=" + (Object)((Object)this.requiredClusterType) + ", requiredReplicaSetName='" + this.requiredReplicaSetName + '\'' + ", serverSelector='" + this.serverSelector + '\'' + ", clusterListeners='" + this.clusterListeners + '\'' + ", serverSelectionTimeout='" + this.serverSelectionTimeoutMS + " ms" + '\'' + ", localThreshold='" + this.serverSelectionTimeoutMS + " ms" + '\'' + ", maxWaitQueueSize=" + this.maxWaitQueueSize + ", description='" + this.description + '\'' + '}';
    }

    public String getShortDescription() {
        return "{" + (this.hosts.isEmpty() ? "" : "hosts=" + this.hosts) + (this.srvHost == null ? "" : ", srvHost=" + this.srvHost) + ", mode=" + (Object)((Object)this.mode) + ", requiredClusterType=" + (Object)((Object)this.requiredClusterType) + ", serverSelectionTimeout='" + this.serverSelectionTimeoutMS + " ms" + '\'' + ", maxWaitQueueSize=" + this.maxWaitQueueSize + (this.requiredReplicaSetName == null ? "" : ", requiredReplicaSetName='" + this.requiredReplicaSetName + '\'') + (this.description == null ? "" : ", description='" + this.description + '\'') + '}';
    }

    private ClusterSettings(Builder builder) {
        if (builder.srvHost != null) {
            if (builder.srvHost.contains(":")) {
                throw new IllegalArgumentException("The srvHost can not contain a host name that specifies a port");
            }
            if (builder.srvHost.split("\\.").length < 3) {
                throw new IllegalArgumentException(String.format("An SRV host name '%s' was provided that does not contain at least three parts. It must contain a hostname, domain name and a top level domain.", ((ServerAddress)builder.hosts.get(0)).getHost()));
            }
        }
        if (builder.hosts.size() > 1 && builder.requiredClusterType == ClusterType.STANDALONE) {
            throw new IllegalArgumentException("Multiple hosts cannot be specified when using ClusterType.STANDALONE.");
        }
        if (builder.requiredReplicaSetName != null) {
            if (builder.requiredClusterType == ClusterType.UNKNOWN) {
                builder.requiredClusterType = ClusterType.REPLICA_SET;
            } else if (builder.requiredClusterType != ClusterType.REPLICA_SET) {
                throw new IllegalArgumentException("When specifying a replica set name, only ClusterType.UNKNOWN and ClusterType.REPLICA_SET are valid.");
            }
        }
        this.description = builder.description;
        this.srvHost = builder.srvHost;
        this.hosts = builder.hosts;
        if (this.srvHost != null) {
            if (builder.mode == ClusterConnectionMode.SINGLE) {
                throw new IllegalArgumentException("An SRV host name was provided but the connection mode is not MULTIPLE");
            }
            this.mode = ClusterConnectionMode.MULTIPLE;
        } else {
            if (builder.mode == ClusterConnectionMode.SINGLE && builder.hosts.size() > 1) {
                throw new IllegalArgumentException("Can not directly connect to more than one server");
            }
            this.mode = builder.mode != null ? builder.mode : (this.hosts.size() == 1 ? ClusterConnectionMode.SINGLE : ClusterConnectionMode.MULTIPLE);
        }
        this.requiredReplicaSetName = builder.requiredReplicaSetName;
        this.requiredClusterType = builder.requiredClusterType;
        this.localThresholdMS = builder.localThresholdMS;
        this.serverSelector = builder.packServerSelector();
        this.serverSelectionTimeoutMS = builder.serverSelectionTimeoutMS;
        this.maxWaitQueueSize = builder.maxWaitQueueSize;
        this.clusterListeners = Collections.unmodifiableList(builder.clusterListeners);
    }

    @NotThreadSafe
    public static final class Builder {
        private static final List<ServerAddress> DEFAULT_HOSTS = Collections.singletonList(new ServerAddress());
        private String srvHost;
        private List<ServerAddress> hosts = DEFAULT_HOSTS;
        private ClusterConnectionMode mode;
        private ClusterType requiredClusterType = ClusterType.UNKNOWN;
        private String requiredReplicaSetName;
        private ServerSelector serverSelector;
        private String description;
        private long serverSelectionTimeoutMS = TimeUnit.MILLISECONDS.convert(30L, TimeUnit.SECONDS);
        private long localThresholdMS = TimeUnit.MILLISECONDS.convert(15L, TimeUnit.MILLISECONDS);
        private int maxWaitQueueSize = 500;
        private List<ClusterListener> clusterListeners = new ArrayList<ClusterListener>();

        private Builder() {
        }

        public Builder applySettings(ClusterSettings clusterSettings) {
            Assertions.notNull("clusterSettings", clusterSettings);
            this.description = clusterSettings.description;
            this.srvHost = clusterSettings.srvHost;
            this.hosts = clusterSettings.hosts;
            this.mode = clusterSettings.mode;
            this.requiredReplicaSetName = clusterSettings.requiredReplicaSetName;
            this.requiredClusterType = clusterSettings.requiredClusterType;
            this.localThresholdMS = clusterSettings.localThresholdMS;
            this.serverSelectionTimeoutMS = clusterSettings.serverSelectionTimeoutMS;
            this.maxWaitQueueSize = clusterSettings.maxWaitQueueSize;
            this.clusterListeners = new ArrayList<ClusterListener>(clusterSettings.clusterListeners);
            this.serverSelector = this.unpackServerSelector(clusterSettings.serverSelector);
            return this;
        }

        @Deprecated
        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder srvHost(String srvHost) {
            if (this.hosts != DEFAULT_HOSTS) {
                throw new IllegalArgumentException("Can not set both hosts and srvHost");
            }
            this.srvHost = srvHost;
            return this;
        }

        public Builder hosts(List<ServerAddress> hosts) {
            Assertions.notNull("hosts", hosts);
            if (hosts.isEmpty()) {
                throw new IllegalArgumentException("hosts list may not be empty");
            }
            if (this.srvHost != null) {
                throw new IllegalArgumentException("srvHost must be null");
            }
            LinkedHashSet<ServerAddress> hostsSet = new LinkedHashSet<ServerAddress>(hosts.size());
            for (ServerAddress serverAddress : hosts) {
                Assertions.notNull("serverAddress", serverAddress);
                hostsSet.add(ServerAddressHelper.createServerAddress(serverAddress.getHost(), serverAddress.getPort()));
            }
            this.hosts = Collections.unmodifiableList(new ArrayList(hostsSet));
            return this;
        }

        public Builder mode(ClusterConnectionMode mode) {
            this.mode = Assertions.notNull("mode", mode);
            return this;
        }

        public Builder requiredReplicaSetName(String requiredReplicaSetName) {
            this.requiredReplicaSetName = requiredReplicaSetName;
            return this;
        }

        public Builder requiredClusterType(ClusterType requiredClusterType) {
            this.requiredClusterType = Assertions.notNull("requiredClusterType", requiredClusterType);
            return this;
        }

        public Builder localThreshold(long localThreshold, TimeUnit timeUnit) {
            Assertions.isTrueArgument("localThreshold must be >= 0", localThreshold >= 0L);
            this.localThresholdMS = TimeUnit.MILLISECONDS.convert(localThreshold, timeUnit);
            return this;
        }

        public Builder serverSelector(ServerSelector serverSelector) {
            this.serverSelector = serverSelector;
            return this;
        }

        public Builder serverSelectionTimeout(long serverSelectionTimeout, TimeUnit timeUnit) {
            this.serverSelectionTimeoutMS = TimeUnit.MILLISECONDS.convert(serverSelectionTimeout, timeUnit);
            return this;
        }

        @Deprecated
        public Builder maxWaitQueueSize(int maxWaitQueueSize) {
            this.maxWaitQueueSize = maxWaitQueueSize;
            return this;
        }

        public Builder addClusterListener(ClusterListener clusterListener) {
            Assertions.notNull("clusterListener", clusterListener);
            this.clusterListeners.add(clusterListener);
            return this;
        }

        public Builder applyConnectionString(ConnectionString connectionString) {
            Integer localThreshold;
            if (connectionString.isSrvProtocol()) {
                this.mode(ClusterConnectionMode.MULTIPLE);
                this.srvHost(connectionString.getHosts().get(0));
            } else if (connectionString.getHosts().size() == 1 && connectionString.getRequiredReplicaSetName() == null) {
                this.mode(ClusterConnectionMode.SINGLE).hosts(Collections.singletonList(ServerAddressHelper.createServerAddress(connectionString.getHosts().get(0))));
            } else {
                ArrayList<ServerAddress> seedList = new ArrayList<ServerAddress>();
                for (String cur : connectionString.getHosts()) {
                    seedList.add(ServerAddressHelper.createServerAddress(cur));
                }
                this.mode(ClusterConnectionMode.MULTIPLE).hosts(seedList);
            }
            this.requiredReplicaSetName(connectionString.getRequiredReplicaSetName());
            Integer maxConnectionPoolSize = connectionString.getMaxConnectionPoolSize();
            int maxSize = maxConnectionPoolSize != null ? maxConnectionPoolSize : 100;
            Integer threadsAllowedToBlockForConnectionMultiplier = connectionString.getThreadsAllowedToBlockForConnectionMultiplier();
            int waitQueueMultiple = threadsAllowedToBlockForConnectionMultiplier != null ? threadsAllowedToBlockForConnectionMultiplier : 5;
            this.maxWaitQueueSize(waitQueueMultiple * maxSize);
            Integer serverSelectionTimeout = connectionString.getServerSelectionTimeout();
            if (serverSelectionTimeout != null) {
                this.serverSelectionTimeout(serverSelectionTimeout.intValue(), TimeUnit.MILLISECONDS);
            }
            if ((localThreshold = connectionString.getLocalThreshold()) != null) {
                this.localThreshold(localThreshold.intValue(), TimeUnit.MILLISECONDS);
            }
            return this;
        }

        private ServerSelector unpackServerSelector(ServerSelector serverSelector) {
            if (serverSelector instanceof CompositeServerSelector) {
                return ((CompositeServerSelector)serverSelector).getServerSelectors().get(0);
            }
            return null;
        }

        private ServerSelector packServerSelector() {
            LatencyMinimizingServerSelector latencyMinimizingServerSelector = new LatencyMinimizingServerSelector(this.localThresholdMS, TimeUnit.MILLISECONDS);
            if (this.serverSelector == null) {
                return latencyMinimizingServerSelector;
            }
            return new CompositeServerSelector(Arrays.asList(this.serverSelector, latencyMinimizingServerSelector));
        }

        public ClusterSettings build() {
            return new ClusterSettings(this);
        }
    }
}

