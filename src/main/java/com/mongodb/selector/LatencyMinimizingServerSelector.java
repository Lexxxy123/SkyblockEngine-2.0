/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.selector;

import com.mongodb.connection.ClusterConnectionMode;
import com.mongodb.connection.ClusterDescription;
import com.mongodb.connection.ServerDescription;
import com.mongodb.selector.ServerSelector;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Deprecated
public class LatencyMinimizingServerSelector
implements ServerSelector {
    private final long acceptableLatencyDifferenceNanos;

    public LatencyMinimizingServerSelector(long acceptableLatencyDifference, TimeUnit timeUnit) {
        this.acceptableLatencyDifferenceNanos = TimeUnit.NANOSECONDS.convert(acceptableLatencyDifference, timeUnit);
    }

    public long getAcceptableLatencyDifference(TimeUnit timeUnit) {
        return timeUnit.convert(this.acceptableLatencyDifferenceNanos, TimeUnit.NANOSECONDS);
    }

    @Override
    public List<ServerDescription> select(ClusterDescription clusterDescription) {
        if (clusterDescription.getConnectionMode() != ClusterConnectionMode.MULTIPLE) {
            return clusterDescription.getAny();
        }
        return this.getServersWithAcceptableLatencyDifference(clusterDescription.getAny(), this.getFastestRoundTripTimeNanos(clusterDescription.getServerDescriptions()));
    }

    public String toString() {
        return "LatencyMinimizingServerSelector{acceptableLatencyDifference=" + TimeUnit.MILLISECONDS.convert(this.acceptableLatencyDifferenceNanos, TimeUnit.NANOSECONDS) + " ms" + '}';
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        LatencyMinimizingServerSelector that = (LatencyMinimizingServerSelector)o2;
        return this.acceptableLatencyDifferenceNanos == that.acceptableLatencyDifferenceNanos;
    }

    public int hashCode() {
        return (int)(this.acceptableLatencyDifferenceNanos ^ this.acceptableLatencyDifferenceNanos >>> 32);
    }

    private long getFastestRoundTripTimeNanos(List<ServerDescription> members) {
        long fastestRoundTripTime = Long.MAX_VALUE;
        for (ServerDescription cur : members) {
            if (!cur.isOk() || cur.getRoundTripTimeNanos() >= fastestRoundTripTime) continue;
            fastestRoundTripTime = cur.getRoundTripTimeNanos();
        }
        return fastestRoundTripTime;
    }

    private List<ServerDescription> getServersWithAcceptableLatencyDifference(List<ServerDescription> servers, long bestPingTime) {
        ArrayList<ServerDescription> goodSecondaries = new ArrayList<ServerDescription>(servers.size());
        for (ServerDescription cur : servers) {
            if (!cur.isOk() || cur.getRoundTripTimeNanos() - this.acceptableLatencyDifferenceNanos > bestPingTime) continue;
            goodSecondaries.add(cur);
        }
        return goodSecondaries;
    }
}

