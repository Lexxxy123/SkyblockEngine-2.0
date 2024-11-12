/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.assertions.Assertions;
import com.mongodb.connection.ClusterId;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.ClusterType;
import com.mongodb.internal.connection.AbstractMultiServerCluster;
import com.mongodb.internal.connection.ClusterableServerFactory;
import com.mongodb.internal.connection.DnsSrvRecordInitializer;
import com.mongodb.internal.connection.DnsSrvRecordMonitor;
import com.mongodb.internal.connection.DnsSrvRecordMonitorFactory;
import java.util.Collection;
import java.util.Collections;

public final class DnsMultiServerCluster
extends AbstractMultiServerCluster {
    private final DnsSrvRecordMonitor dnsSrvRecordMonitor;
    private volatile MongoException srvResolutionException;

    public DnsMultiServerCluster(ClusterId clusterId, ClusterSettings settings, ClusterableServerFactory serverFactory, DnsSrvRecordMonitorFactory dnsSrvRecordMonitorFactory) {
        super(clusterId, settings, serverFactory);
        Assertions.notNull("srvHost", settings.getSrvHost());
        this.dnsSrvRecordMonitor = dnsSrvRecordMonitorFactory.create(settings.getSrvHost(), new DnsSrvRecordInitializer(){
            private volatile boolean initialized;

            @Override
            public void initialize(Collection<ServerAddress> hosts) {
                DnsMultiServerCluster.this.srvResolutionException = null;
                if (!this.initialized) {
                    this.initialized = true;
                    DnsMultiServerCluster.this.initialize(hosts);
                } else {
                    DnsMultiServerCluster.this.onChange(hosts);
                }
            }

            @Override
            public void initialize(MongoException initializationException) {
                if (!this.initialized) {
                    DnsMultiServerCluster.this.srvResolutionException = initializationException;
                    DnsMultiServerCluster.this.initialize(Collections.<ServerAddress>emptyList());
                }
            }

            @Override
            public ClusterType getClusterType() {
                return DnsMultiServerCluster.this.getClusterType();
            }
        });
        this.dnsSrvRecordMonitor.start();
    }

    @Override
    protected MongoException getSrvResolutionException() {
        return this.srvResolutionException;
    }

    @Override
    public void close() {
        if (this.dnsSrvRecordMonitor != null) {
            this.dnsSrvRecordMonitor.close();
        }
        super.close();
    }
}

