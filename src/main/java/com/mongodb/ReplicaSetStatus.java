/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.ServerAddress;
import com.mongodb.connection.Cluster;
import com.mongodb.connection.ClusterDescription;
import com.mongodb.connection.ServerDescription;
import com.mongodb.lang.Nullable;
import java.util.List;

@Deprecated
public class ReplicaSetStatus {
    private final Cluster cluster;

    ReplicaSetStatus(Cluster cluster) {
        this.cluster = cluster;
    }

    @Nullable
    public String getName() {
        List<ServerDescription> any = this.getClusterDescription().getAnyPrimaryOrSecondary();
        return any.isEmpty() ? null : any.get(0).getSetName();
    }

    @Nullable
    public ServerAddress getMaster() {
        List<ServerDescription> primaries = this.getClusterDescription().getPrimaries();
        return primaries.isEmpty() ? null : primaries.get(0).getAddress();
    }

    public boolean isMaster(ServerAddress serverAddress) {
        ServerAddress masterServerAddress = this.getMaster();
        return masterServerAddress != null && masterServerAddress.equals(serverAddress);
    }

    public int getMaxBsonObjectSize() {
        List<ServerDescription> primaries = this.getClusterDescription().getPrimaries();
        return primaries.isEmpty() ? ServerDescription.getDefaultMaxDocumentSize() : primaries.get(0).getMaxDocumentSize();
    }

    private ClusterDescription getClusterDescription() {
        return this.cluster.getDescription();
    }

    public String toString() {
        return "ReplicaSetStatus{name=" + this.getName() + ", cluster=" + this.getClusterDescription() + '}';
    }
}

