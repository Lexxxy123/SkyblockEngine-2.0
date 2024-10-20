/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.event;

import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.ConnectionPoolSettings;
import com.mongodb.connection.ServerSettings;
import com.mongodb.event.ClusterListener;
import com.mongodb.event.ClusterListenerAdapter;
import com.mongodb.event.CommandListener;
import com.mongodb.event.ConnectionPoolListener;
import com.mongodb.event.ConnectionPoolListenerAdapter;
import com.mongodb.event.ServerListener;
import com.mongodb.event.ServerListenerAdapter;
import com.mongodb.event.ServerMonitorListener;
import com.mongodb.event.ServerMonitorListenerAdapter;
import com.mongodb.internal.event.ClusterListenerMulticaster;
import com.mongodb.internal.event.CommandListenerMulticaster;
import com.mongodb.internal.event.ConnectionPoolListenerMulticaster;
import com.mongodb.internal.event.ServerListenerMulticaster;
import com.mongodb.internal.event.ServerMonitorListenerMulticaster;
import java.util.ArrayList;
import java.util.List;

public final class EventListenerHelper {
    public static final ServerListener NO_OP_SERVER_LISTENER = new ServerListenerAdapter(){};
    public static final ServerMonitorListener NO_OP_SERVER_MONITOR_LISTENER = new ServerMonitorListenerAdapter(){};
    public static final ClusterListener NO_OP_CLUSTER_LISTENER = new ClusterListenerAdapter(){};
    public static final ConnectionPoolListener NO_OP_CONNECTION_POOL_LISTENER = new ConnectionPoolListenerAdapter(){};

    public static ClusterListener getClusterListener(ClusterSettings clusterSettings) {
        switch (clusterSettings.getClusterListeners().size()) {
            case 0: {
                return NO_OP_CLUSTER_LISTENER;
            }
            case 1: {
                return clusterSettings.getClusterListeners().get(0);
            }
        }
        return new ClusterListenerMulticaster(clusterSettings.getClusterListeners());
    }

    public static CommandListener getCommandListener(List<CommandListener> commandListeners) {
        switch (commandListeners.size()) {
            case 0: {
                return null;
            }
            case 1: {
                return commandListeners.get(0);
            }
        }
        return new CommandListenerMulticaster(commandListeners);
    }

    public static ConnectionPoolListener getConnectionPoolListener(ConnectionPoolSettings connectionPoolSettings) {
        switch (connectionPoolSettings.getConnectionPoolListeners().size()) {
            case 0: {
                return NO_OP_CONNECTION_POOL_LISTENER;
            }
            case 1: {
                return connectionPoolSettings.getConnectionPoolListeners().get(0);
            }
        }
        return new ConnectionPoolListenerMulticaster(connectionPoolSettings.getConnectionPoolListeners());
    }

    public static ServerMonitorListener getServerMonitorListener(ServerSettings serverSettings) {
        switch (serverSettings.getServerMonitorListeners().size()) {
            case 0: {
                return NO_OP_SERVER_MONITOR_LISTENER;
            }
            case 1: {
                return serverSettings.getServerMonitorListeners().get(0);
            }
        }
        return new ServerMonitorListenerMulticaster(serverSettings.getServerMonitorListeners());
    }

    public static ServerListener createServerListener(ServerSettings serverSettings, ServerListener additionalServerListener) {
        ArrayList<ServerListener> mergedServerListeners = new ArrayList<ServerListener>();
        if (additionalServerListener != null) {
            mergedServerListeners.add(additionalServerListener);
        }
        mergedServerListeners.addAll(serverSettings.getServerListeners());
        switch (mergedServerListeners.size()) {
            case 0: {
                return NO_OP_SERVER_LISTENER;
            }
            case 1: {
                return (ServerListener)mergedServerListeners.get(0);
            }
        }
        return new ServerListenerMulticaster(mergedServerListeners);
    }

    private EventListenerHelper() {
    }
}

