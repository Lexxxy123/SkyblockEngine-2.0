/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoCommandException;
import com.mongodb.MongoException;
import com.mongodb.MongoNodeIsRecoveringException;
import com.mongodb.MongoNotPrimaryException;
import com.mongodb.MongoSecurityException;
import com.mongodb.MongoSocketException;
import com.mongodb.MongoSocketReadTimeoutException;
import com.mongodb.assertions.Assertions;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.connection.AsyncConnection;
import com.mongodb.connection.ClusterConnectionMode;
import com.mongodb.connection.Connection;
import com.mongodb.connection.ServerConnectionState;
import com.mongodb.connection.ServerDescription;
import com.mongodb.connection.ServerId;
import com.mongodb.connection.ServerType;
import com.mongodb.diagnostics.logging.Logger;
import com.mongodb.diagnostics.logging.Loggers;
import com.mongodb.event.CommandListener;
import com.mongodb.event.ServerClosedEvent;
import com.mongodb.event.ServerDescriptionChangedEvent;
import com.mongodb.event.ServerListener;
import com.mongodb.event.ServerOpeningEvent;
import com.mongodb.internal.async.ErrorHandlingResultCallback;
import com.mongodb.internal.connection.ChangeEvent;
import com.mongodb.internal.connection.ChangeListener;
import com.mongodb.internal.connection.ClusterClock;
import com.mongodb.internal.connection.ClusterClockAdvancingSessionContext;
import com.mongodb.internal.connection.ClusterableServer;
import com.mongodb.internal.connection.CommandProtocol;
import com.mongodb.internal.connection.ConnectionFactory;
import com.mongodb.internal.connection.ConnectionPool;
import com.mongodb.internal.connection.InternalConnection;
import com.mongodb.internal.connection.LegacyProtocol;
import com.mongodb.internal.connection.MongoWriteConcernWithResponseException;
import com.mongodb.internal.connection.ProtocolExecutor;
import com.mongodb.internal.connection.ServerMonitor;
import com.mongodb.internal.connection.ServerMonitorFactory;
import com.mongodb.session.SessionContext;
import java.util.Arrays;
import java.util.List;

class DefaultServer
implements ClusterableServer {
    private static final Logger LOGGER = Loggers.getLogger("connection");
    private static final List<Integer> SHUTDOWN_CODES = Arrays.asList(91, 11600);
    private final ServerId serverId;
    private final ConnectionPool connectionPool;
    private final ClusterConnectionMode clusterConnectionMode;
    private final ConnectionFactory connectionFactory;
    private final ServerMonitor serverMonitor;
    private final ChangeListener<ServerDescription> serverStateListener;
    private final ServerListener serverListener;
    private final CommandListener commandListener;
    private final ClusterClock clusterClock;
    private volatile ServerDescription description;
    private volatile boolean isClosed;

    DefaultServer(ServerId serverId, ClusterConnectionMode clusterConnectionMode, ConnectionPool connectionPool, ConnectionFactory connectionFactory, ServerMonitorFactory serverMonitorFactory, ServerListener serverListener, CommandListener commandListener, ClusterClock clusterClock) {
        this.serverListener = Assertions.notNull("serverListener", serverListener);
        this.commandListener = commandListener;
        this.clusterClock = Assertions.notNull("clusterClock", clusterClock);
        Assertions.notNull("serverAddress", serverId);
        Assertions.notNull("serverMonitorFactory", serverMonitorFactory);
        this.clusterConnectionMode = Assertions.notNull("clusterConnectionMode", clusterConnectionMode);
        this.connectionFactory = Assertions.notNull("connectionFactory", connectionFactory);
        this.connectionPool = Assertions.notNull("connectionPool", connectionPool);
        this.serverStateListener = new DefaultServerStateListener();
        this.serverId = serverId;
        serverListener.serverOpening(new ServerOpeningEvent(this.serverId));
        this.description = ServerDescription.builder().state(ServerConnectionState.CONNECTING).address(serverId.getAddress()).build();
        this.serverMonitor = serverMonitorFactory.create(this.serverStateListener);
        this.serverMonitor.start();
    }

    @Override
    public Connection getConnection() {
        Assertions.isTrue("open", !this.isClosed());
        try {
            return this.connectionFactory.create(this.connectionPool.get(), new DefaultServerProtocolExecutor(), this.clusterConnectionMode);
        } catch (MongoSecurityException e2) {
            this.connectionPool.invalidate();
            throw e2;
        } catch (MongoSocketException e3) {
            this.invalidate();
            throw e3;
        }
    }

    @Override
    public void getConnectionAsync(final SingleResultCallback<AsyncConnection> callback) {
        Assertions.isTrue("open", !this.isClosed());
        this.connectionPool.getAsync(new SingleResultCallback<InternalConnection>(){

            @Override
            public void onResult(InternalConnection result, Throwable t2) {
                if (t2 instanceof MongoSecurityException) {
                    DefaultServer.this.connectionPool.invalidate();
                } else if (t2 instanceof MongoSocketException) {
                    DefaultServer.this.invalidate();
                }
                if (t2 != null) {
                    callback.onResult(null, t2);
                } else {
                    callback.onResult(DefaultServer.this.connectionFactory.createAsync(result, new DefaultServerProtocolExecutor(), DefaultServer.this.clusterConnectionMode), null);
                }
            }
        });
    }

    @Override
    public ServerDescription getDescription() {
        Assertions.isTrue("open", !this.isClosed());
        return this.description;
    }

    @Override
    public void invalidate() {
        if (!this.isClosed()) {
            this.serverStateListener.stateChanged(new ChangeEvent<ServerDescription>(this.description, ServerDescription.builder().state(ServerConnectionState.CONNECTING).address(this.serverId.getAddress()).build()));
            this.connectionPool.invalidate();
            this.connect();
        }
    }

    @Override
    public void invalidate(Throwable t2) {
        if (!this.isClosed()) {
            if (t2 instanceof MongoSocketException && !(t2 instanceof MongoSocketReadTimeoutException)) {
                this.invalidate();
            } else if (t2 instanceof MongoNotPrimaryException || t2 instanceof MongoNodeIsRecoveringException) {
                if (this.description.getMaxWireVersion() < 8) {
                    this.invalidate();
                } else if (SHUTDOWN_CODES.contains(((MongoCommandException)t2).getErrorCode())) {
                    this.invalidate();
                } else {
                    ChangeEvent<ServerDescription> event = new ChangeEvent<ServerDescription>(this.description, ServerDescription.builder().state(ServerConnectionState.CONNECTING).type(ServerType.UNKNOWN).address(this.serverId.getAddress()).exception(t2).build());
                    this.serverStateListener.stateChanged(event);
                    this.connect();
                }
            }
        }
    }

    public void invalidate(Throwable t2, SessionContext sessionContext) {
        Assertions.notNull("sessionContext", sessionContext);
        this.invalidate(t2);
        if (t2 instanceof MongoSocketException && sessionContext.hasSession()) {
            sessionContext.markSessionDirty();
        }
    }

    @Override
    public void close() {
        if (!this.isClosed()) {
            this.connectionPool.close();
            this.serverMonitor.close();
            this.isClosed = true;
            this.serverListener.serverClosed(new ServerClosedEvent(this.serverId));
        }
    }

    @Override
    public boolean isClosed() {
        return this.isClosed;
    }

    @Override
    public void connect() {
        this.serverMonitor.connect();
    }

    ConnectionPool getConnectionPool() {
        return this.connectionPool;
    }

    private final class DefaultServerStateListener
    implements ChangeListener<ServerDescription> {
        private DefaultServerStateListener() {
        }

        @Override
        public void stateChanged(ChangeEvent<ServerDescription> event) {
            ServerDescription oldDescription = DefaultServer.this.description;
            DefaultServer.this.description = event.getNewValue();
            DefaultServer.this.serverListener.serverDescriptionChanged(new ServerDescriptionChangedEvent(DefaultServer.this.serverId, DefaultServer.this.description, oldDescription));
        }
    }

    private class DefaultServerProtocolExecutor
    implements ProtocolExecutor {
        private DefaultServerProtocolExecutor() {
        }

        @Override
        public <T> T execute(LegacyProtocol<T> protocol, InternalConnection connection) {
            try {
                protocol.setCommandListener(DefaultServer.this.commandListener);
                return protocol.execute(connection);
            } catch (MongoException e2) {
                DefaultServer.this.invalidate(e2);
                throw e2;
            }
        }

        @Override
        public <T> void executeAsync(LegacyProtocol<T> protocol, InternalConnection connection, final SingleResultCallback<T> callback) {
            protocol.setCommandListener(DefaultServer.this.commandListener);
            protocol.executeAsync(connection, ErrorHandlingResultCallback.errorHandlingCallback(new SingleResultCallback<T>(){

                @Override
                public void onResult(T result, Throwable t2) {
                    if (t2 != null) {
                        DefaultServer.this.invalidate(t2);
                    }
                    callback.onResult(result, t2);
                }
            }, LOGGER));
        }

        @Override
        public <T> T execute(CommandProtocol<T> protocol, InternalConnection connection, SessionContext sessionContext) {
            try {
                protocol.sessionContext(new ClusterClockAdvancingSessionContext(sessionContext, DefaultServer.this.clusterClock));
                return protocol.execute(connection);
            } catch (MongoWriteConcernWithResponseException e2) {
                DefaultServer.this.invalidate();
                return (T)e2.getResponse();
            } catch (MongoException e3) {
                DefaultServer.this.invalidate(e3, sessionContext);
                throw e3;
            }
        }

        @Override
        public <T> void executeAsync(CommandProtocol<T> protocol, InternalConnection connection, final SessionContext sessionContext, final SingleResultCallback<T> callback) {
            protocol.sessionContext(new ClusterClockAdvancingSessionContext(sessionContext, DefaultServer.this.clusterClock));
            protocol.executeAsync(connection, ErrorHandlingResultCallback.errorHandlingCallback(new SingleResultCallback<T>(){

                @Override
                public void onResult(T result, Throwable t2) {
                    if (t2 != null) {
                        if (t2 instanceof MongoWriteConcernWithResponseException) {
                            DefaultServer.this.invalidate();
                            callback.onResult(((MongoWriteConcernWithResponseException)t2).getResponse(), null);
                        } else {
                            DefaultServer.this.invalidate(t2, sessionContext);
                            callback.onResult(null, t2);
                        }
                    } else {
                        callback.onResult(result, null);
                    }
                }
            }, LOGGER));
        }
    }
}

