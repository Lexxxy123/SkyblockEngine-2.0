/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.MongoClientException;
import com.mongodb.MongoConfigurationException;
import com.mongodb.MongoInternalException;
import com.mongodb.ReadPreference;
import com.mongodb.Tag;
import com.mongodb.TagSet;
import com.mongodb.annotations.Immutable;
import com.mongodb.assertions.Assertions;
import com.mongodb.connection.ClusterDescription;
import com.mongodb.connection.ClusterType;
import com.mongodb.connection.ServerDescription;
import com.mongodb.lang.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.BsonString;

@Immutable
public abstract class TaggableReadPreference
extends ReadPreference {
    private static final int SMALLEST_MAX_STALENESS_MS = 90000;
    private static final int IDLE_WRITE_PERIOD_MS = 10000;
    private final List<TagSet> tagSetList = new ArrayList<TagSet>();
    private final Long maxStalenessMS;

    TaggableReadPreference() {
        this.maxStalenessMS = null;
    }

    TaggableReadPreference(List<TagSet> tagSetList, @Nullable Long maxStaleness, TimeUnit timeUnit) {
        Assertions.notNull("tagSetList", tagSetList);
        Assertions.isTrueArgument("maxStaleness is null or >= 0", maxStaleness == null || maxStaleness >= 0L);
        this.maxStalenessMS = maxStaleness == null ? null : Long.valueOf(TimeUnit.MILLISECONDS.convert(maxStaleness, timeUnit));
        this.tagSetList.addAll(tagSetList);
    }

    @Override
    public boolean isSlaveOk() {
        return true;
    }

    @Override
    public BsonDocument toDocument() {
        BsonDocument readPrefObject = new BsonDocument("mode", new BsonString(this.getName()));
        if (!this.tagSetList.isEmpty()) {
            readPrefObject.put("tags", this.tagsListToBsonArray());
        }
        if (this.maxStalenessMS != null) {
            readPrefObject.put("maxStalenessSeconds", new BsonInt64(TimeUnit.MILLISECONDS.toSeconds(this.maxStalenessMS)));
        }
        return readPrefObject;
    }

    public List<TagSet> getTagSetList() {
        return Collections.unmodifiableList(this.tagSetList);
    }

    @Nullable
    public Long getMaxStaleness(TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        if (this.maxStalenessMS == null) {
            return null;
        }
        return timeUnit.convert(this.maxStalenessMS, TimeUnit.MILLISECONDS);
    }

    public String toString() {
        return "ReadPreference{name=" + this.getName() + (this.tagSetList.isEmpty() ? "" : ", tagSetList=" + this.tagSetList) + (this.maxStalenessMS == null ? "" : ", maxStalenessMS=" + this.maxStalenessMS) + '}';
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        TaggableReadPreference that = (TaggableReadPreference)o2;
        if (this.maxStalenessMS != null ? !this.maxStalenessMS.equals(that.maxStalenessMS) : that.maxStalenessMS != null) {
            return false;
        }
        return this.tagSetList.equals(that.tagSetList);
    }

    public int hashCode() {
        int result = this.tagSetList.hashCode();
        result = 31 * result + this.getName().hashCode();
        result = 31 * result + (this.maxStalenessMS != null ? this.maxStalenessMS.hashCode() : 0);
        return result;
    }

    @Override
    protected List<ServerDescription> chooseForNonReplicaSet(ClusterDescription clusterDescription) {
        return this.selectFreshServers(clusterDescription, clusterDescription.getAny());
    }

    protected static ClusterDescription copyClusterDescription(ClusterDescription clusterDescription, List<ServerDescription> selectedServers) {
        return new ClusterDescription(clusterDescription.getConnectionMode(), clusterDescription.getType(), selectedServers, clusterDescription.getClusterSettings(), clusterDescription.getServerSettings());
    }

    protected List<ServerDescription> selectFreshServers(ClusterDescription clusterDescription, List<ServerDescription> servers) {
        Long maxStaleness = this.getMaxStaleness(TimeUnit.MILLISECONDS);
        if (maxStaleness == null) {
            return servers;
        }
        if (clusterDescription.getServerSettings() == null) {
            throw new MongoConfigurationException("heartbeat frequency must be provided in cluster description");
        }
        if (!this.serversAreAllThreeDotFour(clusterDescription)) {
            throw new MongoConfigurationException("Servers must all be at least version 3.4 when max staleness is configured");
        }
        if (clusterDescription.getType() != ClusterType.REPLICA_SET) {
            return servers;
        }
        long heartbeatFrequencyMS = clusterDescription.getServerSettings().getHeartbeatFrequency(TimeUnit.MILLISECONDS);
        if (maxStaleness < Math.max(90000L, heartbeatFrequencyMS + 10000L)) {
            if (90000L > heartbeatFrequencyMS + 10000L) {
                throw new MongoConfigurationException(String.format("Max staleness (%d sec) must be at least 90 seconds", this.getMaxStaleness(TimeUnit.SECONDS)));
            }
            throw new MongoConfigurationException(String.format("Max staleness (%d ms) must be at least the heartbeat period (%d ms) plus the idle write period (%d ms)", maxStaleness, heartbeatFrequencyMS, 10000));
        }
        ArrayList<ServerDescription> freshServers = new ArrayList<ServerDescription>(servers.size());
        ServerDescription primary = this.findPrimary(clusterDescription);
        if (primary != null) {
            for (ServerDescription cur : servers) {
                if (cur.isPrimary()) {
                    freshServers.add(cur);
                    continue;
                }
                if (this.getStalenessOfSecondaryRelativeToPrimary(primary, cur, heartbeatFrequencyMS) > maxStaleness) continue;
                freshServers.add(cur);
            }
        } else {
            ServerDescription mostUpToDateSecondary = this.findMostUpToDateSecondary(clusterDescription);
            for (ServerDescription cur : servers) {
                if (this.getLastWriteDateNonNull(mostUpToDateSecondary).getTime() - this.getLastWriteDateNonNull(cur).getTime() + heartbeatFrequencyMS > maxStaleness) continue;
                freshServers.add(cur);
            }
        }
        return freshServers;
    }

    private long getStalenessOfSecondaryRelativeToPrimary(ServerDescription primary, ServerDescription serverDescription, long heartbeatFrequencyMS) {
        return this.getLastWriteDateNonNull(primary).getTime() + (serverDescription.getLastUpdateTime(TimeUnit.MILLISECONDS) - primary.getLastUpdateTime(TimeUnit.MILLISECONDS)) - this.getLastWriteDateNonNull(serverDescription).getTime() + heartbeatFrequencyMS;
    }

    @Nullable
    private ServerDescription findPrimary(ClusterDescription clusterDescription) {
        for (ServerDescription cur : clusterDescription.getServerDescriptions()) {
            if (!cur.isPrimary()) continue;
            return cur;
        }
        return null;
    }

    private ServerDescription findMostUpToDateSecondary(ClusterDescription clusterDescription) {
        ServerDescription mostUpdateToDateSecondary = null;
        for (ServerDescription cur : clusterDescription.getServerDescriptions()) {
            if (!cur.isSecondary() || mostUpdateToDateSecondary != null && this.getLastWriteDateNonNull(cur).getTime() <= this.getLastWriteDateNonNull(mostUpdateToDateSecondary).getTime()) continue;
            mostUpdateToDateSecondary = cur;
        }
        if (mostUpdateToDateSecondary == null) {
            throw new MongoInternalException("Expected at least one secondary in cluster description: " + clusterDescription);
        }
        return mostUpdateToDateSecondary;
    }

    private Date getLastWriteDateNonNull(ServerDescription serverDescription) {
        Date lastWriteDate = serverDescription.getLastWriteDate();
        if (lastWriteDate == null) {
            throw new MongoClientException("lastWriteDate should not be null in " + serverDescription);
        }
        return lastWriteDate;
    }

    private boolean serversAreAllThreeDotFour(ClusterDescription clusterDescription) {
        for (ServerDescription cur : clusterDescription.getServerDescriptions()) {
            if (!cur.isOk() || cur.getMaxWireVersion() >= 5) continue;
            return false;
        }
        return true;
    }

    private BsonArray tagsListToBsonArray() {
        BsonArray bsonArray = new BsonArray();
        for (TagSet tagSet : this.tagSetList) {
            bsonArray.add(this.toDocument(tagSet));
        }
        return bsonArray;
    }

    private BsonDocument toDocument(TagSet tagSet) {
        BsonDocument document = new BsonDocument();
        for (Tag tag : tagSet) {
            document.put(tag.getName(), new BsonString(tag.getValue()));
        }
        return document;
    }

    static class PrimaryPreferredReadPreference
    extends SecondaryReadPreference {
        PrimaryPreferredReadPreference() {
        }

        PrimaryPreferredReadPreference(List<TagSet> tagSetList, @Nullable Long maxStaleness, TimeUnit timeUnit) {
            super(tagSetList, maxStaleness, timeUnit);
        }

        @Override
        public String getName() {
            return "primaryPreferred";
        }

        @Override
        protected List<ServerDescription> chooseForReplicaSet(ClusterDescription clusterDescription) {
            List<ServerDescription> selectedServers = this.selectFreshServers(clusterDescription, clusterDescription.getPrimaries());
            if (selectedServers.isEmpty()) {
                selectedServers = super.chooseForReplicaSet(clusterDescription);
            }
            return selectedServers;
        }
    }

    static class NearestReadPreference
    extends TaggableReadPreference {
        NearestReadPreference() {
        }

        NearestReadPreference(List<TagSet> tagSetList, @Nullable Long maxStaleness, TimeUnit timeUnit) {
            super(tagSetList, maxStaleness, timeUnit);
        }

        @Override
        public String getName() {
            return "nearest";
        }

        @Override
        public List<ServerDescription> chooseForReplicaSet(ClusterDescription clusterDescription) {
            List<ServerDescription> selectedServers = this.selectFreshServers(clusterDescription, clusterDescription.getAnyPrimaryOrSecondary());
            if (!this.getTagSetList().isEmpty()) {
                ClusterDescription nonStaleClusterDescription = NearestReadPreference.copyClusterDescription(clusterDescription, selectedServers);
                selectedServers = Collections.emptyList();
                for (TagSet tagSet : this.getTagSetList()) {
                    List<ServerDescription> servers = nonStaleClusterDescription.getAnyPrimaryOrSecondary(tagSet);
                    if (servers.isEmpty()) continue;
                    selectedServers = servers;
                    break;
                }
            }
            return selectedServers;
        }
    }

    static class SecondaryPreferredReadPreference
    extends SecondaryReadPreference {
        SecondaryPreferredReadPreference() {
        }

        SecondaryPreferredReadPreference(List<TagSet> tagSetList, @Nullable Long maxStaleness, TimeUnit timeUnit) {
            super(tagSetList, maxStaleness, timeUnit);
        }

        @Override
        public String getName() {
            return "secondaryPreferred";
        }

        @Override
        protected List<ServerDescription> chooseForReplicaSet(ClusterDescription clusterDescription) {
            List<ServerDescription> selectedServers = super.chooseForReplicaSet(clusterDescription);
            if (selectedServers.isEmpty()) {
                selectedServers = clusterDescription.getPrimaries();
            }
            return selectedServers;
        }
    }

    static class SecondaryReadPreference
    extends TaggableReadPreference {
        SecondaryReadPreference() {
        }

        SecondaryReadPreference(List<TagSet> tagSetList, @Nullable Long maxStaleness, TimeUnit timeUnit) {
            super(tagSetList, maxStaleness, timeUnit);
        }

        @Override
        public String getName() {
            return "secondary";
        }

        @Override
        protected List<ServerDescription> chooseForReplicaSet(ClusterDescription clusterDescription) {
            List<ServerDescription> selectedServers = this.selectFreshServers(clusterDescription, clusterDescription.getSecondaries());
            if (!this.getTagSetList().isEmpty()) {
                ClusterDescription nonStaleClusterDescription = SecondaryReadPreference.copyClusterDescription(clusterDescription, selectedServers);
                selectedServers = Collections.emptyList();
                for (TagSet tagSet : this.getTagSetList()) {
                    List<ServerDescription> servers = nonStaleClusterDescription.getSecondaries(tagSet);
                    if (servers.isEmpty()) continue;
                    selectedServers = servers;
                    break;
                }
            }
            return selectedServers;
        }
    }
}

