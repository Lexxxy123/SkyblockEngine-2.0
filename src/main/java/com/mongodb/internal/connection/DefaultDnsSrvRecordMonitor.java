/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoException;
import com.mongodb.MongoInternalException;
import com.mongodb.ServerAddress;
import com.mongodb.connection.ClusterId;
import com.mongodb.connection.ClusterType;
import com.mongodb.diagnostics.logging.Logger;
import com.mongodb.diagnostics.logging.Loggers;
import com.mongodb.internal.connection.DnsSrvRecordInitializer;
import com.mongodb.internal.connection.DnsSrvRecordMonitor;
import com.mongodb.internal.connection.ServerAddressHelper;
import com.mongodb.internal.dns.DnsResolver;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class DefaultDnsSrvRecordMonitor
implements DnsSrvRecordMonitor {
    private static final Logger LOGGER = Loggers.getLogger("cluster");
    private final String hostName;
    private final long rescanFrequencyMillis;
    private final long noRecordsRescanFrequencyMillis;
    private final DnsSrvRecordInitializer dnsSrvRecordInitializer;
    private final DnsResolver dnsResolver;
    private final Thread monitorThread;
    private volatile boolean isClosed;

    DefaultDnsSrvRecordMonitor(String hostName, long rescanFrequencyMillis, long noRecordsRescanFrequencyMillis, DnsSrvRecordInitializer dnsSrvRecordInitializer, ClusterId clusterId, DnsResolver dnsResolver) {
        this.hostName = hostName;
        this.rescanFrequencyMillis = rescanFrequencyMillis;
        this.noRecordsRescanFrequencyMillis = noRecordsRescanFrequencyMillis;
        this.dnsSrvRecordInitializer = dnsSrvRecordInitializer;
        this.dnsResolver = dnsResolver;
        this.monitorThread = new Thread((Runnable)new DnsSrvRecordMonitorRunnable(), "cluster-" + clusterId + "-srv-" + hostName);
        this.monitorThread.setDaemon(true);
    }

    @Override
    public void start() {
        this.monitorThread.start();
    }

    @Override
    public void close() {
        this.isClosed = true;
        this.monitorThread.interrupt();
    }

    private class DnsSrvRecordMonitorRunnable
    implements Runnable {
        private Set<ServerAddress> currentHosts = Collections.emptySet();
        private ClusterType clusterType = ClusterType.UNKNOWN;

        private DnsSrvRecordMonitorRunnable() {
        }

        @Override
        public void run() {
            while (!DefaultDnsSrvRecordMonitor.this.isClosed && this.shouldContinueMonitoring()) {
                try {
                    List<String> resolvedHostNames = DefaultDnsSrvRecordMonitor.this.dnsResolver.resolveHostFromSrvRecords(DefaultDnsSrvRecordMonitor.this.hostName);
                    Set<ServerAddress> hosts = this.createServerAddressSet(resolvedHostNames);
                    if (DefaultDnsSrvRecordMonitor.this.isClosed) {
                        return;
                    }
                    if (!hosts.equals(this.currentHosts)) {
                        try {
                            DefaultDnsSrvRecordMonitor.this.dnsSrvRecordInitializer.initialize(hosts);
                            this.currentHosts = hosts;
                        } catch (RuntimeException e2) {
                            LOGGER.warn("Exception in monitor thread during notification of DNS resolution state change", e2);
                        }
                    }
                } catch (MongoException e3) {
                    if (this.currentHosts.isEmpty()) {
                        DefaultDnsSrvRecordMonitor.this.dnsSrvRecordInitializer.initialize(e3);
                    }
                    LOGGER.info("Exception while resolving SRV records", e3);
                } catch (RuntimeException e4) {
                    if (this.currentHosts.isEmpty()) {
                        DefaultDnsSrvRecordMonitor.this.dnsSrvRecordInitializer.initialize(new MongoInternalException("Unexpected runtime exception", e4));
                    }
                    LOGGER.info("Unexpected runtime exception while resolving SRV record", e4);
                }
                try {
                    Thread.sleep(this.getRescanFrequencyMillis());
                } catch (InterruptedException interruptedException) {
                    // empty catch block
                }
                this.clusterType = DefaultDnsSrvRecordMonitor.this.dnsSrvRecordInitializer.getClusterType();
            }
        }

        private boolean shouldContinueMonitoring() {
            return this.clusterType == ClusterType.UNKNOWN || this.clusterType == ClusterType.SHARDED;
        }

        private long getRescanFrequencyMillis() {
            return this.currentHosts.isEmpty() ? DefaultDnsSrvRecordMonitor.this.noRecordsRescanFrequencyMillis : DefaultDnsSrvRecordMonitor.this.rescanFrequencyMillis;
        }

        private Set<ServerAddress> createServerAddressSet(List<String> resolvedHostNames) {
            HashSet<ServerAddress> hosts = new HashSet<ServerAddress>(resolvedHostNames.size());
            for (String host : resolvedHostNames) {
                hosts.add(ServerAddressHelper.createServerAddress(host));
            }
            return hosts;
        }
    }
}

