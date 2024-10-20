/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoCompressor;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.MongoSecurityException;
import com.mongodb.assertions.Assertions;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.connection.ConnectionId;
import com.mongodb.connection.ServerType;
import com.mongodb.internal.connection.Authenticator;
import com.mongodb.internal.connection.CommandHelper;
import com.mongodb.internal.connection.DefaultAuthenticator;
import com.mongodb.internal.connection.DescriptionHelper;
import com.mongodb.internal.connection.InternalConnection;
import com.mongodb.internal.connection.InternalConnectionInitializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;

public class InternalStreamConnectionInitializer
implements InternalConnectionInitializer {
    private final List<Authenticator> authenticators;
    private final BsonDocument clientMetadataDocument;
    private final List<MongoCompressor> requestedCompressors;
    private final boolean checkSaslSupportedMechs;

    public InternalStreamConnectionInitializer(List<Authenticator> authenticators, BsonDocument clientMetadataDocument, List<MongoCompressor> requestedCompressors) {
        this.authenticators = new ArrayList<Authenticator>((Collection)Assertions.notNull("authenticators", authenticators));
        this.clientMetadataDocument = clientMetadataDocument;
        this.requestedCompressors = Assertions.notNull("requestedCompressors", requestedCompressors);
        this.checkSaslSupportedMechs = this.authenticators.size() > 0 && this.authenticators.get(0) instanceof DefaultAuthenticator;
    }

    @Override
    public ConnectionDescription initialize(InternalConnection internalConnection) {
        Assertions.notNull("internalConnection", internalConnection);
        ConnectionDescription connectionDescription = this.initializeConnectionDescription(internalConnection);
        this.authenticateAll(internalConnection, connectionDescription);
        return this.completeConnectionDescriptionInitialization(internalConnection, connectionDescription);
    }

    @Override
    public void initializeAsync(InternalConnection internalConnection, SingleResultCallback<ConnectionDescription> callback) {
        this.initializeConnectionDescriptionAsync(internalConnection, this.createConnectionDescriptionCallback(internalConnection, callback));
    }

    private SingleResultCallback<ConnectionDescription> createConnectionDescriptionCallback(final InternalConnection internalConnection, final SingleResultCallback<ConnectionDescription> callback) {
        return new SingleResultCallback<ConnectionDescription>(){

            @Override
            public void onResult(final ConnectionDescription connectionDescription, Throwable t2) {
                if (t2 != null) {
                    callback.onResult(null, t2);
                } else {
                    new CompoundAuthenticator(internalConnection, connectionDescription, new SingleResultCallback<Void>(){

                        @Override
                        public void onResult(Void result, Throwable t2) {
                            if (t2 != null) {
                                callback.onResult(null, t2);
                            } else {
                                InternalStreamConnectionInitializer.this.completeConnectionDescriptionInitializationAsync(internalConnection, connectionDescription, callback);
                            }
                        }
                    }).start();
                }
            }
        };
    }

    private ConnectionDescription initializeConnectionDescription(InternalConnection internalConnection) {
        BsonDocument isMasterResult;
        BsonDocument isMasterCommandDocument = this.createIsMasterCommand();
        try {
            isMasterResult = CommandHelper.executeCommand("admin", isMasterCommandDocument, internalConnection);
        } catch (MongoException e2) {
            if (this.checkSaslSupportedMechs && e2.getCode() == 11) {
                MongoCredential credential = this.authenticators.get(0).getMongoCredential();
                throw new MongoSecurityException(credential, String.format("Exception authenticating %s", credential), (Throwable)e2);
            }
            throw e2;
        }
        BsonDocument buildInfoResult = CommandHelper.executeCommand("admin", new BsonDocument("buildinfo", new BsonInt32(1)), internalConnection);
        ConnectionDescription connectionDescription = DescriptionHelper.createConnectionDescription(internalConnection.getDescription().getConnectionId(), isMasterResult, buildInfoResult);
        this.setFirstAuthenticator(isMasterResult, connectionDescription);
        return connectionDescription;
    }

    private BsonDocument createIsMasterCommand() {
        BsonDocument isMasterCommandDocument = new BsonDocument("ismaster", new BsonInt32(1));
        if (this.clientMetadataDocument != null) {
            isMasterCommandDocument.append("client", this.clientMetadataDocument);
        }
        if (!this.requestedCompressors.isEmpty()) {
            BsonArray compressors = new BsonArray();
            for (MongoCompressor cur : this.requestedCompressors) {
                compressors.add(new BsonString(cur.getName()));
            }
            isMasterCommandDocument.append("compression", compressors);
        }
        if (this.checkSaslSupportedMechs) {
            MongoCredential credential = this.authenticators.get(0).getMongoCredential();
            isMasterCommandDocument.append("saslSupportedMechs", new BsonString(credential.getSource() + "." + credential.getUserName()));
        }
        return isMasterCommandDocument;
    }

    private ConnectionDescription completeConnectionDescriptionInitialization(InternalConnection internalConnection, ConnectionDescription connectionDescription) {
        if (connectionDescription.getConnectionId().getServerValue() != null) {
            return connectionDescription;
        }
        return this.applyGetLastErrorResult(CommandHelper.executeCommandWithoutCheckingForFailure("admin", new BsonDocument("getlasterror", new BsonInt32(1)), internalConnection), connectionDescription);
    }

    private void authenticateAll(InternalConnection internalConnection, ConnectionDescription connectionDescription) {
        if (connectionDescription.getServerType() != ServerType.REPLICA_SET_ARBITER) {
            for (Authenticator cur : this.authenticators) {
                cur.authenticate(internalConnection, connectionDescription);
            }
        }
    }

    private void initializeConnectionDescriptionAsync(final InternalConnection internalConnection, final SingleResultCallback<ConnectionDescription> callback) {
        CommandHelper.executeCommandAsync("admin", this.createIsMasterCommand(), internalConnection, new SingleResultCallback<BsonDocument>(){

            @Override
            public void onResult(final BsonDocument isMasterResult, Throwable t2) {
                if (t2 != null) {
                    if (InternalStreamConnectionInitializer.this.checkSaslSupportedMechs && t2 instanceof MongoException && ((MongoException)t2).getCode() == 11) {
                        MongoCredential credential = ((Authenticator)InternalStreamConnectionInitializer.this.authenticators.get(0)).getMongoCredential();
                        callback.onResult(null, new MongoSecurityException(credential, String.format("Exception authenticating %s", credential), t2));
                    } else {
                        callback.onResult(null, t2);
                    }
                } else {
                    CommandHelper.executeCommandAsync("admin", new BsonDocument("buildinfo", new BsonInt32(1)), internalConnection, new SingleResultCallback<BsonDocument>(){

                        @Override
                        public void onResult(BsonDocument buildInfoResult, Throwable t2) {
                            if (t2 != null) {
                                callback.onResult(null, t2);
                            } else {
                                ConnectionId connectionId = internalConnection.getDescription().getConnectionId();
                                ConnectionDescription connectionDescription = DescriptionHelper.createConnectionDescription(connectionId, isMasterResult, buildInfoResult);
                                InternalStreamConnectionInitializer.this.setFirstAuthenticator(isMasterResult, connectionDescription);
                                callback.onResult(connectionDescription, null);
                            }
                        }
                    });
                }
            }
        });
    }

    private void setFirstAuthenticator(BsonDocument isMasterResult, ConnectionDescription connectionDescription) {
        if (this.checkSaslSupportedMechs) {
            this.authenticators.set(0, ((DefaultAuthenticator)this.authenticators.get(0)).getAuthenticatorFromIsMasterResult(isMasterResult, connectionDescription));
        }
    }

    private void completeConnectionDescriptionInitializationAsync(InternalConnection internalConnection, final ConnectionDescription connectionDescription, final SingleResultCallback<ConnectionDescription> callback) {
        if (connectionDescription.getConnectionId().getServerValue() != null) {
            callback.onResult(connectionDescription, null);
            return;
        }
        CommandHelper.executeCommandAsync("admin", new BsonDocument("getlasterror", new BsonInt32(1)), internalConnection, new SingleResultCallback<BsonDocument>(){

            @Override
            public void onResult(BsonDocument result, Throwable t2) {
                if (result == null) {
                    callback.onResult(connectionDescription, null);
                } else {
                    callback.onResult(InternalStreamConnectionInitializer.this.applyGetLastErrorResult(result, connectionDescription), null);
                }
            }
        });
    }

    private ConnectionDescription applyGetLastErrorResult(BsonDocument getLastErrorResult, ConnectionDescription connectionDescription) {
        ConnectionId connectionId = getLastErrorResult.containsKey("connectionId") ? connectionDescription.getConnectionId().withServerValue(getLastErrorResult.getNumber("connectionId").intValue()) : connectionDescription.getConnectionId();
        return connectionDescription.withConnectionId(connectionId);
    }

    private class CompoundAuthenticator
    implements SingleResultCallback<Void> {
        private final InternalConnection internalConnection;
        private final ConnectionDescription connectionDescription;
        private final SingleResultCallback<Void> callback;
        private final AtomicInteger currentAuthenticatorIndex = new AtomicInteger(-1);

        CompoundAuthenticator(InternalConnection internalConnection, ConnectionDescription connectionDescription, SingleResultCallback<Void> callback) {
            this.internalConnection = internalConnection;
            this.connectionDescription = connectionDescription;
            this.callback = callback;
        }

        @Override
        public void onResult(Void result, Throwable t2) {
            if (t2 != null) {
                this.callback.onResult(null, t2);
            } else if (this.completedAuthentication()) {
                this.callback.onResult(null, null);
            } else {
                this.authenticateNext();
            }
        }

        public void start() {
            if (this.connectionDescription.getServerType() == ServerType.REPLICA_SET_ARBITER || InternalStreamConnectionInitializer.this.authenticators.isEmpty()) {
                this.callback.onResult(null, null);
            } else {
                this.authenticateNext();
            }
        }

        private boolean completedAuthentication() {
            return this.currentAuthenticatorIndex.get() == InternalStreamConnectionInitializer.this.authenticators.size() - 1;
        }

        private void authenticateNext() {
            ((Authenticator)InternalStreamConnectionInitializer.this.authenticators.get(this.currentAuthenticatorIndex.incrementAndGet())).authenticateAsync(this.internalConnection, this.connectionDescription, this);
        }
    }
}

