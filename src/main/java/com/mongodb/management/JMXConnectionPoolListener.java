/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.management;

import com.mongodb.connection.ConnectionId;
import com.mongodb.connection.ServerId;
import com.mongodb.event.ConnectionAddedEvent;
import com.mongodb.event.ConnectionCheckedInEvent;
import com.mongodb.event.ConnectionCheckedOutEvent;
import com.mongodb.event.ConnectionPoolClosedEvent;
import com.mongodb.event.ConnectionPoolListener;
import com.mongodb.event.ConnectionPoolOpenedEvent;
import com.mongodb.event.ConnectionPoolWaitQueueEnteredEvent;
import com.mongodb.event.ConnectionPoolWaitQueueExitedEvent;
import com.mongodb.event.ConnectionRemovedEvent;
import com.mongodb.management.ConnectionPoolStatistics;
import com.mongodb.management.ConnectionPoolStatisticsMBean;
import com.mongodb.management.MBeanServerFactory;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.management.ObjectName;

public class JMXConnectionPoolListener
implements ConnectionPoolListener {
    private final ConcurrentMap<ServerId, ConnectionPoolStatistics> map = new ConcurrentHashMap<ServerId, ConnectionPoolStatistics>();

    @Override
    public void connectionPoolOpened(ConnectionPoolOpenedEvent event) {
        ConnectionPoolStatistics statistics = new ConnectionPoolStatistics(event);
        this.map.put(event.getServerId(), statistics);
        MBeanServerFactory.getMBeanServer().registerMBean(statistics, this.getMBeanObjectName(event.getServerId()));
    }

    @Override
    public void connectionPoolClosed(ConnectionPoolClosedEvent event) {
        this.map.remove(event.getServerId());
        MBeanServerFactory.getMBeanServer().unregisterMBean(this.getMBeanObjectName(event.getServerId()));
    }

    @Override
    public void connectionCheckedOut(ConnectionCheckedOutEvent event) {
        ConnectionPoolStatistics statistics = this.getStatistics(event.getConnectionId());
        if (statistics != null) {
            statistics.connectionCheckedOut(event);
        }
    }

    @Override
    public void connectionCheckedIn(ConnectionCheckedInEvent event) {
        ConnectionPoolStatistics statistics = this.getStatistics(event.getConnectionId());
        if (statistics != null) {
            statistics.connectionCheckedIn(event);
        }
    }

    @Override
    public void waitQueueEntered(ConnectionPoolWaitQueueEnteredEvent event) {
        ConnectionPoolStatistics statistics = this.getStatistics(event.getServerId());
        if (statistics != null) {
            statistics.waitQueueEntered(event);
        }
    }

    @Override
    public void waitQueueExited(ConnectionPoolWaitQueueExitedEvent event) {
        ConnectionPoolStatistics statistics = this.getStatistics(event.getServerId());
        if (statistics != null) {
            statistics.waitQueueExited(event);
        }
    }

    @Override
    public void connectionAdded(ConnectionAddedEvent event) {
        ConnectionPoolStatistics statistics = this.getStatistics(event.getConnectionId());
        if (statistics != null) {
            statistics.connectionAdded(event);
        }
    }

    @Override
    public void connectionRemoved(ConnectionRemovedEvent event) {
        ConnectionPoolStatistics statistics = this.getStatistics(event.getConnectionId());
        if (statistics != null) {
            statistics.connectionRemoved(event);
        }
    }

    String getMBeanObjectName(ServerId serverId) {
        String name = String.format("org.mongodb.driver:type=ConnectionPool,clusterId=%s,host=%s,port=%s", this.ensureValidValue(serverId.getClusterId().getValue()), this.ensureValidValue(serverId.getAddress().getHost()), serverId.getAddress().getPort());
        if (serverId.getClusterId().getDescription() != null) {
            name = String.format("%s,description=%s", name, this.ensureValidValue(serverId.getClusterId().getDescription()));
        }
        return name;
    }

    ConnectionPoolStatisticsMBean getMBean(ServerId serverId) {
        return this.getStatistics(serverId);
    }

    private ConnectionPoolStatistics getStatistics(ConnectionId connectionId) {
        return this.getStatistics(connectionId.getServerId());
    }

    private ConnectionPoolStatistics getStatistics(ServerId serverId) {
        return (ConnectionPoolStatistics)this.map.get(serverId);
    }

    private String ensureValidValue(String value) {
        if (this.containsQuotableCharacter(value)) {
            return ObjectName.quote(value);
        }
        return value;
    }

    private boolean containsQuotableCharacter(String value) {
        if (value == null || value.length() == 0) {
            return false;
        }
        List<String> quoteableCharacters = Arrays.asList(",", ":", "?", "*", "=", "\"", "\\", "\n");
        for (String quotable : quoteableCharacters) {
            if (!value.contains(quotable)) continue;
            return true;
        }
        return false;
    }
}

