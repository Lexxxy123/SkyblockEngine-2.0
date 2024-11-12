/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.connection;

import com.mongodb.MongoException;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.TagSet;
import com.mongodb.annotations.Immutable;
import com.mongodb.assertions.Assertions;
import com.mongodb.connection.ClusterConnectionMode;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.ClusterType;
import com.mongodb.connection.ServerDescription;
import com.mongodb.connection.ServerSettings;
import com.mongodb.selector.ReadPreferenceServerSelector;
import com.mongodb.selector.WritableServerSelector;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Immutable
public class ClusterDescription {
    private final ClusterConnectionMode connectionMode;
    private final ClusterType type;
    private final List<ServerDescription> serverDescriptions;
    private final ClusterSettings clusterSettings;
    private final ServerSettings serverSettings;
    private final MongoException srvResolutionException;

    public ClusterDescription(ClusterConnectionMode connectionMode, ClusterType type, List<ServerDescription> serverDescriptions) {
        this(connectionMode, type, serverDescriptions, null, null);
    }

    public ClusterDescription(ClusterConnectionMode connectionMode, ClusterType type, List<ServerDescription> serverDescriptions, ClusterSettings clusterSettings, ServerSettings serverSettings) {
        this(connectionMode, type, null, serverDescriptions, clusterSettings, serverSettings);
    }

    public ClusterDescription(ClusterConnectionMode connectionMode, ClusterType type, MongoException srvResolutionException, List<ServerDescription> serverDescriptions, ClusterSettings clusterSettings, ServerSettings serverSettings) {
        Assertions.notNull("all", serverDescriptions);
        this.connectionMode = Assertions.notNull("connectionMode", connectionMode);
        this.type = Assertions.notNull("type", type);
        this.srvResolutionException = srvResolutionException;
        this.serverDescriptions = new ArrayList<ServerDescription>(serverDescriptions);
        this.clusterSettings = clusterSettings;
        this.serverSettings = serverSettings;
    }

    public ClusterSettings getClusterSettings() {
        return this.clusterSettings;
    }

    public ServerSettings getServerSettings() {
        return this.serverSettings;
    }

    public boolean isCompatibleWithDriver() {
        for (ServerDescription cur : this.serverDescriptions) {
            if (cur.isCompatibleWithDriver()) continue;
            return false;
        }
        return true;
    }

    public ServerDescription findServerIncompatiblyOlderThanDriver() {
        for (ServerDescription cur : this.serverDescriptions) {
            if (!cur.isIncompatiblyOlderThanDriver()) continue;
            return cur;
        }
        return null;
    }

    public ServerDescription findServerIncompatiblyNewerThanDriver() {
        for (ServerDescription cur : this.serverDescriptions) {
            if (!cur.isIncompatiblyNewerThanDriver()) continue;
            return cur;
        }
        return null;
    }

    public boolean hasReadableServer(ReadPreference readPreference) {
        Assertions.notNull("readPreference", readPreference);
        return !new ReadPreferenceServerSelector(readPreference).select(this).isEmpty();
    }

    public boolean hasWritableServer() {
        return !new WritableServerSelector().select(this).isEmpty();
    }

    public ClusterConnectionMode getConnectionMode() {
        return this.connectionMode;
    }

    public ClusterType getType() {
        return this.type;
    }

    public MongoException getSrvResolutionException() {
        return this.srvResolutionException;
    }

    public List<ServerDescription> getServerDescriptions() {
        return Collections.unmodifiableList(this.serverDescriptions);
    }

    public Integer getLogicalSessionTimeoutMinutes() {
        Integer retVal = null;
        for (ServerDescription cur : this.getServersByPredicate(new Predicate(){

            @Override
            public boolean apply(ServerDescription serverDescription) {
                return serverDescription.isPrimary() || serverDescription.isSecondary();
            }
        })) {
            if (cur.getLogicalSessionTimeoutMinutes() == null) {
                return null;
            }
            if (retVal == null) {
                retVal = cur.getLogicalSessionTimeoutMinutes();
                continue;
            }
            retVal = Math.min(retVal, cur.getLogicalSessionTimeoutMinutes());
        }
        return retVal;
    }

    @Deprecated
    public Set<ServerDescription> getAll() {
        TreeSet<ServerDescription> serverDescriptionSet = new TreeSet<ServerDescription>(new Comparator<ServerDescription>(){

            @Override
            public int compare(ServerDescription o1, ServerDescription o2) {
                int val = o1.getAddress().getHost().compareTo(o2.getAddress().getHost());
                if (val != 0) {
                    return val;
                }
                return this.integerCompare(o1.getAddress().getPort(), o2.getAddress().getPort());
            }

            private int integerCompare(int p1, int p2) {
                return p1 < p2 ? -1 : (p1 == p2 ? 0 : 1);
            }
        });
        serverDescriptionSet.addAll(this.serverDescriptions);
        return Collections.unmodifiableSet(serverDescriptionSet);
    }

    @Deprecated
    public ServerDescription getByServerAddress(ServerAddress serverAddress) {
        for (ServerDescription cur : this.serverDescriptions) {
            if (!cur.isOk() || !cur.getAddress().equals(serverAddress)) continue;
            return cur;
        }
        return null;
    }

    @Deprecated
    public List<ServerDescription> getPrimaries() {
        return this.getServersByPredicate(new Predicate(){

            @Override
            public boolean apply(ServerDescription serverDescription) {
                return serverDescription.isPrimary();
            }
        });
    }

    @Deprecated
    public List<ServerDescription> getSecondaries() {
        return this.getServersByPredicate(new Predicate(){

            @Override
            public boolean apply(ServerDescription serverDescription) {
                return serverDescription.isSecondary();
            }
        });
    }

    @Deprecated
    public List<ServerDescription> getSecondaries(final TagSet tagSet) {
        return this.getServersByPredicate(new Predicate(){

            @Override
            public boolean apply(ServerDescription serverDescription) {
                return serverDescription.isSecondary() && serverDescription.hasTags(tagSet);
            }
        });
    }

    @Deprecated
    public List<ServerDescription> getAny() {
        return this.getServersByPredicate(new Predicate(){

            @Override
            public boolean apply(ServerDescription serverDescription) {
                return serverDescription.isOk();
            }
        });
    }

    @Deprecated
    public List<ServerDescription> getAnyPrimaryOrSecondary() {
        return this.getServersByPredicate(new Predicate(){

            @Override
            public boolean apply(ServerDescription serverDescription) {
                return serverDescription.isPrimary() || serverDescription.isSecondary();
            }
        });
    }

    @Deprecated
    public List<ServerDescription> getAnyPrimaryOrSecondary(final TagSet tagSet) {
        return this.getServersByPredicate(new Predicate(){

            @Override
            public boolean apply(ServerDescription serverDescription) {
                return (serverDescription.isPrimary() || serverDescription.isSecondary()) && serverDescription.hasTags(tagSet);
            }
        });
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
        ClusterDescription that = (ClusterDescription)o2;
        if (this.connectionMode != that.connectionMode) {
            return false;
        }
        if (this.type != that.type) {
            return false;
        }
        if (this.serverDescriptions.size() != that.serverDescriptions.size()) {
            return false;
        }
        if (!this.serverDescriptions.containsAll(that.serverDescriptions)) {
            return false;
        }
        Class<?> thisExceptionClass = this.srvResolutionException != null ? this.srvResolutionException.getClass() : null;
        Class<?> clazz = thatExceptionClass = that.srvResolutionException != null ? that.srvResolutionException.getClass() : null;
        if (thisExceptionClass != null ? !thisExceptionClass.equals(thatExceptionClass) : thatExceptionClass != null) {
            return false;
        }
        String thisExceptionMessage = this.srvResolutionException != null ? this.srvResolutionException.getMessage() : null;
        String string = thatExceptionMessage = that.srvResolutionException != null ? that.srvResolutionException.getMessage() : null;
        return !(thisExceptionMessage != null ? !thisExceptionMessage.equals(thatExceptionMessage) : thatExceptionMessage != null);
    }

    public int hashCode() {
        int result = this.connectionMode.hashCode();
        result = 31 * result + this.type.hashCode();
        result = 31 * result + (this.srvResolutionException == null ? 0 : this.srvResolutionException.hashCode());
        result = 31 * result + this.serverDescriptions.hashCode();
        return result;
    }

    public String toString() {
        return "ClusterDescription{type=" + (Object)((Object)this.getType()) + (this.srvResolutionException == null ? "" : ", srvResolutionException=" + this.srvResolutionException) + ", connectionMode=" + (Object)((Object)this.connectionMode) + ", serverDescriptions=" + this.serverDescriptions + '}';
    }

    public String getShortDescription() {
        StringBuilder serverDescriptions = new StringBuilder();
        String delimiter = "";
        for (ServerDescription cur : this.serverDescriptions) {
            serverDescriptions.append(delimiter).append(cur.getShortDescription());
            delimiter = ", ";
        }
        if (this.srvResolutionException == null) {
            return String.format("{type=%s, servers=[%s]", new Object[]{this.type, serverDescriptions});
        }
        return String.format("{type=%s, srvResolutionException=%s, servers=[%s]", new Object[]{this.type, this.srvResolutionException, serverDescriptions});
    }

    private List<ServerDescription> getServersByPredicate(Predicate predicate) {
        ArrayList<ServerDescription> membersByTag = new ArrayList<ServerDescription>();
        for (ServerDescription cur : this.serverDescriptions) {
            if (!predicate.apply(cur)) continue;
            membersByTag.add(cur);
        }
        return membersByTag;
    }

    private static interface Predicate {
        public boolean apply(ServerDescription var1);
    }
}

