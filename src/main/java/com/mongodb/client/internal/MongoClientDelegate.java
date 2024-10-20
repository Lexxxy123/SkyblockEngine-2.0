/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.internal;

import com.mongodb.ClientSessionOptions;
import com.mongodb.MongoClientException;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.MongoInternalException;
import com.mongodb.MongoQueryException;
import com.mongodb.MongoSocketException;
import com.mongodb.MongoTimeoutException;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.assertions.Assertions;
import com.mongodb.binding.ClusterBinding;
import com.mongodb.binding.ReadBinding;
import com.mongodb.binding.ReadWriteBinding;
import com.mongodb.binding.WriteBinding;
import com.mongodb.client.ClientSession;
import com.mongodb.client.internal.ClientSessionBinding;
import com.mongodb.client.internal.ClientSessionImpl;
import com.mongodb.client.internal.Crypt;
import com.mongodb.client.internal.CryptBinding;
import com.mongodb.client.internal.OperationExecutor;
import com.mongodb.connection.Cluster;
import com.mongodb.connection.ClusterConnectionMode;
import com.mongodb.connection.ClusterDescription;
import com.mongodb.connection.ServerDescription;
import com.mongodb.internal.binding.ClusterAwareReadWriteBinding;
import com.mongodb.internal.session.ServerSessionPool;
import com.mongodb.lang.Nullable;
import com.mongodb.operation.ReadOperation;
import com.mongodb.operation.WriteOperation;
import com.mongodb.selector.ServerSelector;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.bson.codecs.configuration.CodecRegistry;

public class MongoClientDelegate {
    private final Cluster cluster;
    private final ServerSessionPool serverSessionPool;
    private final List<MongoCredential> credentialList;
    private final Object originator;
    private final OperationExecutor operationExecutor;
    private final Crypt crypt;
    private final CodecRegistry codecRegistry;
    private final AtomicBoolean closed;

    public MongoClientDelegate(Cluster cluster, CodecRegistry codecRegistry, List<MongoCredential> credentialList, Object originator, @Nullable Crypt crypt) {
        this(cluster, codecRegistry, credentialList, originator, null, crypt);
    }

    MongoClientDelegate(Cluster cluster, CodecRegistry codecRegistry, List<MongoCredential> credentialList, Object originator, @Nullable OperationExecutor operationExecutor, @Nullable Crypt crypt) {
        this.cluster = cluster;
        this.codecRegistry = codecRegistry;
        this.serverSessionPool = new ServerSessionPool(cluster);
        this.credentialList = credentialList;
        this.originator = originator;
        this.operationExecutor = operationExecutor == null ? new DelegateOperationExecutor() : operationExecutor;
        this.crypt = crypt;
        this.closed = new AtomicBoolean();
    }

    public OperationExecutor getOperationExecutor() {
        return this.operationExecutor;
    }

    @Nullable
    public ClientSession createClientSession(ClientSessionOptions options, ReadConcern readConcern, WriteConcern writeConcern, ReadPreference readPreference) {
        Assertions.notNull("readConcern", readConcern);
        Assertions.notNull("writeConcern", writeConcern);
        Assertions.notNull("readPreference", readPreference);
        if (this.credentialList.size() > 1) {
            return null;
        }
        ClusterDescription connectedClusterDescription = this.getConnectedClusterDescription();
        if (connectedClusterDescription.getLogicalSessionTimeoutMinutes() == null) {
            return null;
        }
        ClientSessionOptions mergedOptions = ClientSessionOptions.builder(options).defaultTransactionOptions(TransactionOptions.merge(options.getDefaultTransactionOptions(), TransactionOptions.builder().readConcern(readConcern).writeConcern(writeConcern).readPreference(readPreference).build())).build();
        return new ClientSessionImpl(this.serverSessionPool, this.originator, mergedOptions, this);
    }

    public List<ServerAddress> getServerAddressList() {
        ArrayList<ServerAddress> serverAddresses = new ArrayList<ServerAddress>();
        for (ServerDescription cur : this.cluster.getDescription().getServerDescriptions()) {
            serverAddresses.add(cur.getAddress());
        }
        return serverAddresses;
    }

    public void close() {
        if (!this.closed.getAndSet(true)) {
            if (this.crypt != null) {
                this.crypt.close();
            }
            this.serverSessionPool.close();
            this.cluster.close();
        }
    }

    public Cluster getCluster() {
        return this.cluster;
    }

    public CodecRegistry getCodecRegistry() {
        return this.codecRegistry;
    }

    public ServerSessionPool getServerSessionPool() {
        return this.serverSessionPool;
    }

    private ClusterDescription getConnectedClusterDescription() {
        ClusterDescription clusterDescription = this.cluster.getDescription();
        if (this.getServerDescriptionListToConsiderForSessionSupport(clusterDescription).isEmpty()) {
            this.cluster.selectServer(new ServerSelector(){

                @Override
                public List<ServerDescription> select(ClusterDescription clusterDescription) {
                    return MongoClientDelegate.this.getServerDescriptionListToConsiderForSessionSupport(clusterDescription);
                }
            });
            clusterDescription = this.cluster.getDescription();
        }
        return clusterDescription;
    }

    private List<ServerDescription> getServerDescriptionListToConsiderForSessionSupport(ClusterDescription clusterDescription) {
        if (clusterDescription.getConnectionMode() == ClusterConnectionMode.SINGLE) {
            return clusterDescription.getAny();
        }
        return clusterDescription.getAnyPrimaryOrSecondary();
    }

    private class DelegateOperationExecutor
    implements OperationExecutor {
        private DelegateOperationExecutor() {
        }

        @Override
        public <T> T execute(ReadOperation<T> operation, ReadPreference readPreference, ReadConcern readConcern) {
            return this.execute(operation, readPreference, readConcern, null);
        }

        @Override
        public <T> T execute(WriteOperation<T> operation, ReadConcern readConcern) {
            return this.execute(operation, readConcern, null);
        }

        @Override
        public <T> T execute(ReadOperation<T> operation, ReadPreference readPreference, ReadConcern readConcern, @Nullable ClientSession session) {
            ClientSession actualClientSession = this.getClientSession(session);
            ReadBinding binding = this.getReadBinding(readPreference, readConcern, actualClientSession, session == null && actualClientSession != null);
            try {
                if (session != null && session.hasActiveTransaction() && !binding.getReadPreference().equals(ReadPreference.primary())) {
                    throw new MongoClientException("Read preference in a transaction must be primary");
                }
                T t2 = operation.execute(binding);
                return t2;
            } catch (MongoException e2) {
                this.labelException(session, e2);
                this.unpinServerAddressOnTransientTransactionError(session, e2);
                throw e2;
            } finally {
                binding.release();
            }
        }

        @Override
        public <T> T execute(WriteOperation<T> operation, ReadConcern readConcern, @Nullable ClientSession session) {
            ClientSession actualClientSession = this.getClientSession(session);
            WriteBinding binding = this.getWriteBinding(readConcern, actualClientSession, session == null && actualClientSession != null);
            try {
                T t2 = operation.execute(binding);
                return t2;
            } catch (MongoException e2) {
                this.labelException(session, e2);
                this.unpinServerAddressOnTransientTransactionError(session, e2);
                throw e2;
            } finally {
                binding.release();
            }
        }

        WriteBinding getWriteBinding(ReadConcern readConcern, @Nullable ClientSession session, boolean ownsSession) {
            return this.getReadWriteBinding(ReadPreference.primary(), readConcern, session, ownsSession);
        }

        ReadBinding getReadBinding(ReadPreference readPreference, ReadConcern readConcern, @Nullable ClientSession session, boolean ownsSession) {
            return this.getReadWriteBinding(readPreference, readConcern, session, ownsSession);
        }

        ReadWriteBinding getReadWriteBinding(ReadPreference readPreference, ReadConcern readConcern, @Nullable ClientSession session, boolean ownsSession) {
            ClusterAwareReadWriteBinding readWriteBinding = new ClusterBinding(MongoClientDelegate.this.cluster, this.getReadPreferenceForBinding(readPreference, session), readConcern);
            if (MongoClientDelegate.this.crypt != null) {
                readWriteBinding = new CryptBinding(readWriteBinding, MongoClientDelegate.this.crypt);
            }
            if (session != null) {
                return new ClientSessionBinding(session, ownsSession, readWriteBinding);
            }
            return readWriteBinding;
        }

        private void labelException(@Nullable ClientSession session, MongoException e2) {
            if (session != null && session.hasActiveTransaction() && (e2 instanceof MongoSocketException || e2 instanceof MongoTimeoutException || e2 instanceof MongoQueryException && e2.getCode() == 91) && !e2.hasErrorLabel("UnknownTransactionCommitResult")) {
                e2.addLabel("TransientTransactionError");
            }
        }

        private void unpinServerAddressOnTransientTransactionError(@Nullable ClientSession session, MongoException e2) {
            if (session != null && e2.hasErrorLabel("TransientTransactionError")) {
                session.setPinnedServerAddress(null);
            }
        }

        private ReadPreference getReadPreferenceForBinding(ReadPreference readPreference, @Nullable ClientSession session) {
            if (session == null) {
                return readPreference;
            }
            if (session.hasActiveTransaction()) {
                ReadPreference readPreferenceForBinding = session.getTransactionOptions().getReadPreference();
                if (readPreferenceForBinding == null) {
                    throw new MongoInternalException("Invariant violated.  Transaction options read preference can not be null");
                }
                return readPreferenceForBinding;
            }
            return readPreference;
        }

        @Nullable
        ClientSession getClientSession(@Nullable ClientSession clientSessionFromOperation) {
            ClientSession session;
            if (clientSessionFromOperation != null) {
                Assertions.isTrue("ClientSession from same MongoClient", clientSessionFromOperation.getOriginator() == MongoClientDelegate.this.originator);
                session = clientSessionFromOperation;
            } else {
                session = MongoClientDelegate.this.createClientSession(ClientSessionOptions.builder().causallyConsistent(false).build(), ReadConcern.DEFAULT, WriteConcern.ACKNOWLEDGED, ReadPreference.primary());
            }
            return session;
        }
    }
}

