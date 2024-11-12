/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.connection.ClusterId;
import com.mongodb.connection.ServerSettings;
import com.mongodb.internal.connection.DefaultDnsSrvRecordMonitor;
import com.mongodb.internal.connection.DnsSrvRecordInitializer;
import com.mongodb.internal.connection.DnsSrvRecordMonitor;
import com.mongodb.internal.connection.DnsSrvRecordMonitorFactory;
import com.mongodb.internal.dns.DefaultDnsResolver;
import java.util.concurrent.TimeUnit;

public class DefaultDnsSrvRecordMonitorFactory
implements DnsSrvRecordMonitorFactory {
    private static final long DEFAULT_RESCAN_FREQUENCY_MILLIS = 60000L;
    private final ClusterId clusterId;
    private final long noRecordsRescanFrequency;

    public DefaultDnsSrvRecordMonitorFactory(ClusterId clusterId, ServerSettings serverSettings) {
        this.clusterId = clusterId;
        this.noRecordsRescanFrequency = serverSettings.getHeartbeatFrequency(TimeUnit.MILLISECONDS);
    }

    @Override
    public DnsSrvRecordMonitor create(String hostName, DnsSrvRecordInitializer dnsSrvRecordInitializer) {
        return new DefaultDnsSrvRecordMonitor(hostName, 60000L, this.noRecordsRescanFrequency, dnsSrvRecordInitializer, this.clusterId, new DefaultDnsResolver());
    }
}

