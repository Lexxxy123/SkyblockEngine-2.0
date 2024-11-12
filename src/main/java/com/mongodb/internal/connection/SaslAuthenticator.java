/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoException;
import com.mongodb.MongoInterruptedException;
import com.mongodb.MongoSecurityException;
import com.mongodb.ServerAddress;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.internal.connection.Authenticator;
import com.mongodb.internal.connection.CommandHelper;
import com.mongodb.internal.connection.InternalConnection;
import com.mongodb.internal.connection.MongoCredentialWithCache;
import com.mongodb.lang.Nullable;
import java.security.PrivilegedAction;
import javax.security.auth.Subject;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;
import org.bson.BsonBinary;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;

abstract class SaslAuthenticator
extends Authenticator {
    SaslAuthenticator(MongoCredentialWithCache credential) {
        super(credential);
    }

    @Override
    public void authenticate(final InternalConnection connection, ConnectionDescription connectionDescription) {
        this.doAsSubject(new PrivilegedAction<Void>(){

            @Override
            public Void run() {
                SaslClient saslClient = SaslAuthenticator.this.createSaslClient(connection.getDescription().getServerAddress());
                SaslAuthenticator.this.throwIfSaslClientIsNull(saslClient);
                try {
                    byte[] response = saslClient.hasInitialResponse() ? saslClient.evaluateChallenge(new byte[0]) : null;
                    BsonDocument res = SaslAuthenticator.this.sendSaslStart(response, connection);
                    BsonInt32 conversationId = res.getInt32("conversationId");
                    while (!res.getBoolean("done").getValue()) {
                        response = saslClient.evaluateChallenge(res.getBinary("payload").getData());
                        if (response == null) {
                            throw new MongoSecurityException(SaslAuthenticator.this.getMongoCredential(), "SASL protocol error: no client response to challenge for credential " + SaslAuthenticator.this.getMongoCredential());
                        }
                        res = SaslAuthenticator.this.sendSaslContinue(conversationId, response, connection);
                    }
                    if (!saslClient.isComplete()) {
                        saslClient.evaluateChallenge(res.getBinary("payload").getData());
                        if (!saslClient.isComplete()) {
                            throw new MongoSecurityException(SaslAuthenticator.this.getMongoCredential(), "SASL protocol error: server completed challenges before client completed responses " + SaslAuthenticator.this.getMongoCredential());
                        }
                    }
                } catch (Exception e2) {
                    throw SaslAuthenticator.this.wrapException(e2);
                } finally {
                    SaslAuthenticator.this.disposeOfSaslClient(saslClient);
                }
                return null;
            }
        });
    }

    @Override
    void authenticateAsync(final InternalConnection connection, ConnectionDescription connectionDescription, final SingleResultCallback<Void> callback) {
        try {
            this.doAsSubject(new PrivilegedAction<Void>(){

                @Override
                public Void run() {
                    final SaslClient saslClient = SaslAuthenticator.this.createSaslClient(connection.getDescription().getServerAddress());
                    SaslAuthenticator.this.throwIfSaslClientIsNull(saslClient);
                    try {
                        byte[] response = saslClient.hasInitialResponse() ? saslClient.evaluateChallenge(new byte[0]) : null;
                        SaslAuthenticator.this.sendSaslStartAsync(response, connection, new SingleResultCallback<BsonDocument>(){

                            @Override
                            public void onResult(BsonDocument result, Throwable t2) {
                                if (t2 != null) {
                                    callback.onResult(null, SaslAuthenticator.this.wrapException(t2));
                                } else if (result.getBoolean("done").getValue()) {
                                    SaslAuthenticator.this.verifySaslClientComplete(saslClient, result, callback);
                                } else {
                                    new Continuator(saslClient, result, connection, callback).start();
                                }
                            }
                        });
                    } catch (SaslException e2) {
                        throw SaslAuthenticator.this.wrapException(e2);
                    }
                    return null;
                }
            });
        } catch (Throwable t2) {
            callback.onResult(null, t2);
        }
    }

    public abstract String getMechanismName();

    protected abstract SaslClient createSaslClient(ServerAddress var1);

    private void throwIfSaslClientIsNull(SaslClient saslClient) {
        if (saslClient == null) {
            throw new MongoSecurityException(this.getMongoCredential(), String.format("This JDK does not support the %s SASL mechanism", this.getMechanismName()));
        }
    }

    private void verifySaslClientComplete(SaslClient saslClient, BsonDocument result, SingleResultCallback<Void> callback) {
        if (saslClient.isComplete()) {
            callback.onResult(null, null);
        } else {
            try {
                saslClient.evaluateChallenge(result.getBinary("payload").getData());
                if (saslClient.isComplete()) {
                    callback.onResult(null, null);
                } else {
                    callback.onResult(null, new MongoSecurityException(this.getMongoCredential(), "SASL protocol error: server completed challenges before client completed responses " + this.getMongoCredential()));
                }
            } catch (SaslException e2) {
                callback.onResult(null, this.wrapException(e2));
            }
        }
    }

    @Nullable
    private Subject getSubject() {
        return this.getMongoCredential().getMechanismProperty("JAVA_SUBJECT", null);
    }

    private BsonDocument sendSaslStart(byte[] outToken, InternalConnection connection) {
        return CommandHelper.executeCommand(this.getMongoCredential().getSource(), this.createSaslStartCommandDocument(outToken), connection);
    }

    private BsonDocument sendSaslContinue(BsonInt32 conversationId, byte[] outToken, InternalConnection connection) {
        return CommandHelper.executeCommand(this.getMongoCredential().getSource(), this.createSaslContinueDocument(conversationId, outToken), connection);
    }

    private void sendSaslStartAsync(byte[] outToken, InternalConnection connection, SingleResultCallback<BsonDocument> callback) {
        CommandHelper.executeCommandAsync(this.getMongoCredential().getSource(), this.createSaslStartCommandDocument(outToken), connection, callback);
    }

    private void sendSaslContinueAsync(BsonInt32 conversationId, byte[] outToken, InternalConnection connection, SingleResultCallback<BsonDocument> callback) {
        CommandHelper.executeCommandAsync(this.getMongoCredential().getSource(), this.createSaslContinueDocument(conversationId, outToken), connection, callback);
    }

    private BsonDocument createSaslStartCommandDocument(byte[] outToken) {
        return new BsonDocument("saslStart", new BsonInt32(1)).append("mechanism", new BsonString(this.getMechanismName())).append("payload", new BsonBinary(outToken != null ? outToken : new byte[]{}));
    }

    private BsonDocument createSaslContinueDocument(BsonInt32 conversationId, byte[] outToken) {
        return new BsonDocument("saslContinue", new BsonInt32(1)).append("conversationId", conversationId).append("payload", new BsonBinary(outToken));
    }

    private void disposeOfSaslClient(SaslClient saslClient) {
        try {
            saslClient.dispose();
        } catch (SaslException saslException) {
            // empty catch block
        }
    }

    private MongoException wrapException(Throwable t2) {
        if (t2 instanceof MongoInterruptedException) {
            return (MongoInterruptedException)t2;
        }
        if (t2 instanceof MongoSecurityException) {
            return (MongoSecurityException)t2;
        }
        return new MongoSecurityException(this.getMongoCredential(), "Exception authenticating " + this.getMongoCredential(), t2);
    }

    void doAsSubject(PrivilegedAction<Void> action) {
        if (this.getSubject() == null) {
            action.run();
        } else {
            Subject.doAs(this.getSubject(), action);
        }
    }

    private final class Continuator
    implements SingleResultCallback<BsonDocument> {
        private final SaslClient saslClient;
        private final BsonDocument saslStartDocument;
        private final InternalConnection connection;
        private final SingleResultCallback<Void> callback;

        Continuator(SaslClient saslClient, BsonDocument saslStartDocument, InternalConnection connection, SingleResultCallback<Void> callback) {
            this.saslClient = saslClient;
            this.saslStartDocument = saslStartDocument;
            this.connection = connection;
            this.callback = callback;
        }

        @Override
        public void onResult(BsonDocument result, Throwable t2) {
            if (t2 != null) {
                this.callback.onResult(null, SaslAuthenticator.this.wrapException(t2));
                SaslAuthenticator.this.disposeOfSaslClient(this.saslClient);
            } else if (result.getBoolean("done").getValue()) {
                SaslAuthenticator.this.verifySaslClientComplete(this.saslClient, result, this.callback);
                SaslAuthenticator.this.disposeOfSaslClient(this.saslClient);
            } else {
                this.continueConversation(result);
            }
        }

        public void start() {
            this.continueConversation(this.saslStartDocument);
        }

        private void continueConversation(final BsonDocument result) {
            try {
                SaslAuthenticator.this.doAsSubject(new PrivilegedAction<Void>(){

                    @Override
                    public Void run() {
                        try {
                            SaslAuthenticator.this.sendSaslContinueAsync(Continuator.this.saslStartDocument.getInt32("conversationId"), Continuator.this.saslClient.evaluateChallenge(result.getBinary("payload").getData()), Continuator.this.connection, Continuator.this);
                        } catch (SaslException e2) {
                            throw SaslAuthenticator.this.wrapException(e2);
                        }
                        return null;
                    }
                });
            } catch (Throwable t2) {
                this.callback.onResult(null, t2);
                SaslAuthenticator.this.disposeOfSaslClient(this.saslClient);
            }
        }
    }
}

