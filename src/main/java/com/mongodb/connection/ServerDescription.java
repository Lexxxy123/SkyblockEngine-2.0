/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.connection;

import com.mongodb.ServerAddress;
import com.mongodb.TagSet;
import com.mongodb.annotations.Immutable;
import com.mongodb.annotations.NotThreadSafe;
import com.mongodb.assertions.Assertions;
import com.mongodb.connection.ClusterType;
import com.mongodb.connection.ServerConnectionState;
import com.mongodb.connection.ServerType;
import com.mongodb.connection.ServerVersion;
import com.mongodb.internal.connection.DecimalFormatHelper;
import com.mongodb.internal.connection.Time;
import com.mongodb.lang.Nullable;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.bson.types.ObjectId;

@Immutable
public class ServerDescription {
    public static final String MIN_DRIVER_SERVER_VERSION = "2.6";
    public static final int MIN_DRIVER_WIRE_VERSION = 2;
    public static final int MAX_DRIVER_WIRE_VERSION = 15;
    private static final int DEFAULT_MAX_DOCUMENT_SIZE = 0x1000000;
    private final ServerAddress address;
    private final ServerType type;
    private final String canonicalAddress;
    private final Set<String> hosts;
    private final Set<String> passives;
    private final Set<String> arbiters;
    private final String primary;
    private final int maxDocumentSize;
    private final TagSet tagSet;
    private final String setName;
    private final long roundTripTimeNanos;
    private final boolean ok;
    private final ServerConnectionState state;
    private final ServerVersion version;
    private final int minWireVersion;
    private final int maxWireVersion;
    private final ObjectId electionId;
    private final Integer setVersion;
    private final Date lastWriteDate;
    private final long lastUpdateTimeNanos;
    private final Integer logicalSessionTimeoutMinutes;
    private final Throwable exception;

    public static Builder builder() {
        return new Builder();
    }

    public String getCanonicalAddress() {
        return this.canonicalAddress;
    }

    public Integer getLogicalSessionTimeoutMinutes() {
        return this.logicalSessionTimeoutMinutes;
    }

    public boolean isCompatibleWithDriver() {
        if (this.isIncompatiblyOlderThanDriver()) {
            return false;
        }
        return !this.isIncompatiblyNewerThanDriver();
    }

    public boolean isIncompatiblyNewerThanDriver() {
        return this.ok && this.minWireVersion > 15;
    }

    public boolean isIncompatiblyOlderThanDriver() {
        return this.ok && this.maxWireVersion < 2;
    }

    public static int getDefaultMaxDocumentSize() {
        return 0x1000000;
    }

    public static int getDefaultMinWireVersion() {
        return 0;
    }

    public static int getDefaultMaxWireVersion() {
        return 0;
    }

    public ServerAddress getAddress() {
        return this.address;
    }

    public boolean isReplicaSetMember() {
        return this.type.getClusterType() == ClusterType.REPLICA_SET;
    }

    public boolean isShardRouter() {
        return this.type == ServerType.SHARD_ROUTER;
    }

    public boolean isStandAlone() {
        return this.type == ServerType.STANDALONE;
    }

    public boolean isPrimary() {
        return this.ok && (this.type == ServerType.REPLICA_SET_PRIMARY || this.type == ServerType.SHARD_ROUTER || this.type == ServerType.STANDALONE);
    }

    public boolean isSecondary() {
        return this.ok && (this.type == ServerType.REPLICA_SET_SECONDARY || this.type == ServerType.SHARD_ROUTER || this.type == ServerType.STANDALONE);
    }

    public Set<String> getHosts() {
        return this.hosts;
    }

    public Set<String> getPassives() {
        return this.passives;
    }

    public Set<String> getArbiters() {
        return this.arbiters;
    }

    public String getPrimary() {
        return this.primary;
    }

    public int getMaxDocumentSize() {
        return this.maxDocumentSize;
    }

    public TagSet getTagSet() {
        return this.tagSet;
    }

    public int getMinWireVersion() {
        return this.minWireVersion;
    }

    public int getMaxWireVersion() {
        return this.maxWireVersion;
    }

    public ObjectId getElectionId() {
        return this.electionId;
    }

    public Integer getSetVersion() {
        return this.setVersion;
    }

    @Nullable
    public Date getLastWriteDate() {
        return this.lastWriteDate;
    }

    public long getLastUpdateTime(TimeUnit timeUnit) {
        return timeUnit.convert(this.lastUpdateTimeNanos, TimeUnit.NANOSECONDS);
    }

    public boolean hasTags(TagSet desiredTags) {
        if (!this.ok) {
            return false;
        }
        if (this.type == ServerType.STANDALONE || this.type == ServerType.SHARD_ROUTER) {
            return true;
        }
        return this.tagSet.containsAll(desiredTags);
    }

    public String getSetName() {
        return this.setName;
    }

    public boolean isOk() {
        return this.ok;
    }

    public ServerConnectionState getState() {
        return this.state;
    }

    public ServerType getType() {
        return this.type;
    }

    public ClusterType getClusterType() {
        return this.type.getClusterType();
    }

    @Deprecated
    public ServerVersion getVersion() {
        return this.version;
    }

    public long getRoundTripTimeNanos() {
        return this.roundTripTimeNanos;
    }

    public Throwable getException() {
        return this.exception;
    }

    public boolean equals(Object o2) {
        String thatExceptionMessage;
        Class<?> thatExceptionClass;
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        ServerDescription that = (ServerDescription)o2;
        if (this.maxDocumentSize != that.maxDocumentSize) {
            return false;
        }
        if (this.ok != that.ok) {
            return false;
        }
        if (!this.address.equals(that.address)) {
            return false;
        }
        if (!this.arbiters.equals(that.arbiters)) {
            return false;
        }
        if (this.canonicalAddress != null ? !this.canonicalAddress.equals(that.canonicalAddress) : that.canonicalAddress != null) {
            return false;
        }
        if (!this.hosts.equals(that.hosts)) {
            return false;
        }
        if (!this.passives.equals(that.passives)) {
            return false;
        }
        if (this.primary != null ? !this.primary.equals(that.primary) : that.primary != null) {
            return false;
        }
        if (this.setName != null ? !this.setName.equals(that.setName) : that.setName != null) {
            return false;
        }
        if (this.state != that.state) {
            return false;
        }
        if (!this.tagSet.equals(that.tagSet)) {
            return false;
        }
        if (this.type != that.type) {
            return false;
        }
        if (!this.version.equals(that.version)) {
            return false;
        }
        if (this.minWireVersion != that.minWireVersion) {
            return false;
        }
        if (this.maxWireVersion != that.maxWireVersion) {
            return false;
        }
        if (this.electionId != null ? !this.electionId.equals(that.electionId) : that.electionId != null) {
            return false;
        }
        if (this.setVersion != null ? !this.setVersion.equals(that.setVersion) : that.setVersion != null) {
            return false;
        }
        if (this.lastWriteDate != null ? !this.lastWriteDate.equals(that.lastWriteDate) : that.lastWriteDate != null) {
            return false;
        }
        if (this.lastUpdateTimeNanos != that.lastUpdateTimeNanos) {
            return false;
        }
        if (this.logicalSessionTimeoutMinutes != null ? !this.logicalSessionTimeoutMinutes.equals(that.logicalSessionTimeoutMinutes) : that.logicalSessionTimeoutMinutes != null) {
            return false;
        }
        Class<?> thisExceptionClass = this.exception != null ? this.exception.getClass() : null;
        Class<?> clazz = thatExceptionClass = that.exception != null ? that.exception.getClass() : null;
        if (thisExceptionClass != null ? !thisExceptionClass.equals(thatExceptionClass) : thatExceptionClass != null) {
            return false;
        }
        String thisExceptionMessage = this.exception != null ? this.exception.getMessage() : null;
        String string = thatExceptionMessage = that.exception != null ? that.exception.getMessage() : null;
        return !(thisExceptionMessage != null ? !thisExceptionMessage.equals(thatExceptionMessage) : thatExceptionMessage != null);
    }

    public int hashCode() {
        int result = this.address.hashCode();
        result = 31 * result + this.type.hashCode();
        result = 31 * result + (this.canonicalAddress != null ? this.canonicalAddress.hashCode() : 0);
        result = 31 * result + this.hosts.hashCode();
        result = 31 * result + this.passives.hashCode();
        result = 31 * result + this.arbiters.hashCode();
        result = 31 * result + (this.primary != null ? this.primary.hashCode() : 0);
        result = 31 * result + this.maxDocumentSize;
        result = 31 * result + this.tagSet.hashCode();
        result = 31 * result + (this.setName != null ? this.setName.hashCode() : 0);
        result = 31 * result + (this.electionId != null ? this.electionId.hashCode() : 0);
        result = 31 * result + (this.setVersion != null ? this.setVersion.hashCode() : 0);
        result = 31 * result + (this.lastWriteDate != null ? this.lastWriteDate.hashCode() : 0);
        result = 31 * result + (int)(this.lastUpdateTimeNanos ^ this.lastUpdateTimeNanos >>> 32);
        result = 31 * result + (this.ok ? 1 : 0);
        result = 31 * result + this.state.hashCode();
        result = 31 * result + this.version.hashCode();
        result = 31 * result + this.minWireVersion;
        result = 31 * result + this.maxWireVersion;
        result = 31 * result + (this.logicalSessionTimeoutMinutes != null ? this.logicalSessionTimeoutMinutes.hashCode() : 0);
        result = 31 * result + (this.exception == null ? 0 : this.exception.getClass().hashCode());
        result = 31 * result + (this.exception == null ? 0 : this.exception.getMessage().hashCode());
        return result;
    }

    public String toString() {
        return "ServerDescription{address=" + this.address + ", type=" + (Object)((Object)this.type) + ", state=" + (Object)((Object)this.state) + (this.state == ServerConnectionState.CONNECTED ? ", ok=" + this.ok + ", version=" + this.version + ", minWireVersion=" + this.minWireVersion + ", maxWireVersion=" + this.maxWireVersion + ", maxDocumentSize=" + this.maxDocumentSize + ", logicalSessionTimeoutMinutes=" + this.logicalSessionTimeoutMinutes + ", roundTripTimeNanos=" + this.roundTripTimeNanos : "") + (this.isReplicaSetMember() ? ", setName='" + this.setName + '\'' + ", canonicalAddress=" + this.canonicalAddress + ", hosts=" + this.hosts + ", passives=" + this.passives + ", arbiters=" + this.arbiters + ", primary='" + this.primary + '\'' + ", tagSet=" + this.tagSet + ", electionId=" + this.electionId + ", setVersion=" + this.setVersion + ", lastWriteDate=" + this.lastWriteDate + ", lastUpdateTimeNanos=" + this.lastUpdateTimeNanos : "") + (this.exception == null ? "" : ", exception=" + this.translateExceptionToString()) + '}';
    }

    public String getShortDescription() {
        return "{address=" + this.address + ", type=" + (Object)((Object)this.type) + (!this.tagSet.iterator().hasNext() ? "" : ", " + this.tagSet) + (this.state == ServerConnectionState.CONNECTED ? ", roundTripTime=" + this.getRoundTripFormattedInMilliseconds() + " ms" : "") + ", state=" + (Object)((Object)this.state) + (this.exception == null ? "" : ", exception=" + this.translateExceptionToString()) + '}';
    }

    private String translateExceptionToString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append(this.exception);
        builder.append("}");
        for (Throwable cur = this.exception.getCause(); cur != null; cur = cur.getCause()) {
            builder.append(", caused by ");
            builder.append("{");
            builder.append(cur);
            builder.append("}");
        }
        return builder.toString();
    }

    private String getRoundTripFormattedInMilliseconds() {
        return DecimalFormatHelper.format("#0.0", (double)this.roundTripTimeNanos / 1000.0 / 1000.0);
    }

    ServerDescription(Builder builder) {
        this.address = Assertions.notNull("address", builder.address);
        this.type = Assertions.notNull("type", builder.type);
        this.state = Assertions.notNull("state", builder.state);
        this.version = Assertions.notNull("version", builder.version);
        this.canonicalAddress = builder.canonicalAddress;
        this.hosts = builder.hosts;
        this.passives = builder.passives;
        this.arbiters = builder.arbiters;
        this.primary = builder.primary;
        this.maxDocumentSize = builder.maxDocumentSize;
        this.tagSet = builder.tagSet;
        this.setName = builder.setName;
        this.roundTripTimeNanos = builder.roundTripTimeNanos;
        this.ok = builder.ok;
        this.minWireVersion = builder.minWireVersion;
        this.maxWireVersion = builder.maxWireVersion;
        this.electionId = builder.electionId;
        this.setVersion = builder.setVersion;
        this.lastWriteDate = builder.lastWriteDate;
        this.lastUpdateTimeNanos = builder.lastUpdateTimeNanos;
        this.logicalSessionTimeoutMinutes = builder.logicalSessionTimeoutMinutes;
        this.exception = builder.exception;
    }

    @NotThreadSafe
    public static class Builder {
        private ServerAddress address;
        private ServerType type = ServerType.UNKNOWN;
        private String canonicalAddress;
        private Set<String> hosts = Collections.emptySet();
        private Set<String> passives = Collections.emptySet();
        private Set<String> arbiters = Collections.emptySet();
        private String primary;
        private int maxDocumentSize = 0x1000000;
        private TagSet tagSet = new TagSet();
        private String setName;
        private long roundTripTimeNanos;
        private boolean ok;
        private ServerConnectionState state;
        private ServerVersion version = new ServerVersion();
        private int minWireVersion = 0;
        private int maxWireVersion = 0;
        private ObjectId electionId;
        private Integer setVersion;
        private Date lastWriteDate;
        private long lastUpdateTimeNanos = Time.nanoTime();
        private Integer logicalSessionTimeoutMinutes;
        private Throwable exception;

        public Builder address(ServerAddress address) {
            this.address = address;
            return this;
        }

        public Builder canonicalAddress(String canonicalAddress) {
            this.canonicalAddress = canonicalAddress;
            return this;
        }

        public Builder type(ServerType type) {
            this.type = Assertions.notNull("type", type);
            return this;
        }

        public Builder hosts(Set<String> hosts) {
            this.hosts = hosts == null ? Collections.emptySet() : Collections.unmodifiableSet(new HashSet<String>(hosts));
            return this;
        }

        public Builder passives(Set<String> passives) {
            this.passives = passives == null ? Collections.emptySet() : Collections.unmodifiableSet(new HashSet<String>(passives));
            return this;
        }

        public Builder arbiters(Set<String> arbiters) {
            this.arbiters = arbiters == null ? Collections.emptySet() : Collections.unmodifiableSet(new HashSet<String>(arbiters));
            return this;
        }

        public Builder primary(String primary) {
            this.primary = primary;
            return this;
        }

        public Builder maxDocumentSize(int maxDocumentSize) {
            this.maxDocumentSize = maxDocumentSize;
            return this;
        }

        public Builder tagSet(TagSet tagSet) {
            this.tagSet = tagSet == null ? new TagSet() : tagSet;
            return this;
        }

        public Builder roundTripTime(long roundTripTime, TimeUnit timeUnit) {
            this.roundTripTimeNanos = timeUnit.toNanos(roundTripTime);
            return this;
        }

        public Builder setName(String setName) {
            this.setName = setName;
            return this;
        }

        public Builder ok(boolean ok) {
            this.ok = ok;
            return this;
        }

        public Builder state(ServerConnectionState state) {
            this.state = state;
            return this;
        }

        @Deprecated
        public Builder version(ServerVersion version) {
            Assertions.notNull("version", version);
            this.version = version;
            return this;
        }

        public Builder minWireVersion(int minWireVersion) {
            this.minWireVersion = minWireVersion;
            return this;
        }

        public Builder maxWireVersion(int maxWireVersion) {
            this.maxWireVersion = maxWireVersion;
            return this;
        }

        public Builder electionId(ObjectId electionId) {
            this.electionId = electionId;
            return this;
        }

        public Builder setVersion(Integer setVersion) {
            this.setVersion = setVersion;
            return this;
        }

        public Builder lastWriteDate(Date lastWriteDate) {
            this.lastWriteDate = lastWriteDate;
            return this;
        }

        public Builder lastUpdateTimeNanos(long lastUpdateTimeNanos) {
            this.lastUpdateTimeNanos = lastUpdateTimeNanos;
            return this;
        }

        public Builder logicalSessionTimeoutMinutes(Integer logicalSessionTimeoutMinutes) {
            this.logicalSessionTimeoutMinutes = logicalSessionTimeoutMinutes;
            return this;
        }

        public Builder exception(Throwable exception) {
            this.exception = exception;
            return this;
        }

        public ServerDescription build() {
            return new ServerDescription(this);
        }
    }
}

