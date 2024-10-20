/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.management;

import com.mongodb.ServerAddress;
import com.mongodb.connection.ConnectionPoolSettings;
import com.mongodb.event.ConnectionAddedEvent;
import com.mongodb.event.ConnectionCheckedInEvent;
import com.mongodb.event.ConnectionCheckedOutEvent;
import com.mongodb.event.ConnectionPoolListenerAdapter;
import com.mongodb.event.ConnectionPoolOpenedEvent;
import com.mongodb.event.ConnectionPoolWaitQueueEnteredEvent;
import com.mongodb.event.ConnectionPoolWaitQueueExitedEvent;
import com.mongodb.event.ConnectionRemovedEvent;
import com.mongodb.management.ConnectionPoolStatisticsMBean;
import java.util.concurrent.atomic.AtomicInteger;

final class ConnectionPoolStatistics
extends ConnectionPoolListenerAdapter
implements ConnectionPoolStatisticsMBean {
    private final ServerAddress serverAddress;
    private final ConnectionPoolSettings settings;
    private final AtomicInteger size = new AtomicInteger();
    private final AtomicInteger checkedOutCount = new AtomicInteger();
    private final AtomicInteger waitQueueSize = new AtomicInteger();

    ConnectionPoolStatistics(ConnectionPoolOpenedEvent event) {
        this.serverAddress = event.getServerId().getAddress();
        this.settings = event.getSettings();
    }

    @Override
    public String getHost() {
        return this.serverAddress.getHost();
    }

    @Override
    public int getPort() {
        return this.serverAddress.getPort();
    }

    @Override
    public int getMinSize() {
        return this.settings.getMinSize();
    }

    @Override
    public int getMaxSize() {
        return this.settings.getMaxSize();
    }

    @Override
    public int getSize() {
        return this.size.get();
    }

    @Override
    public int getCheckedOutCount() {
        return this.checkedOutCount.get();
    }

    @Override
    public int getWaitQueueSize() {
        return this.waitQueueSize.get();
    }

    @Override
    public void connectionCheckedOut(ConnectionCheckedOutEvent event) {
        this.checkedOutCount.incrementAndGet();
    }

    @Override
    public void connectionCheckedIn(ConnectionCheckedInEvent event) {
        this.checkedOutCount.decrementAndGet();
    }

    @Override
    public void connectionAdded(ConnectionAddedEvent event) {
        this.size.incrementAndGet();
    }

    @Override
    public void connectionRemoved(ConnectionRemovedEvent event) {
        this.size.decrementAndGet();
    }

    @Override
    public void waitQueueEntered(ConnectionPoolWaitQueueEnteredEvent event) {
        this.waitQueueSize.incrementAndGet();
    }

    @Override
    public void waitQueueExited(ConnectionPoolWaitQueueExitedEvent event) {
        this.waitQueueSize.decrementAndGet();
    }
}

