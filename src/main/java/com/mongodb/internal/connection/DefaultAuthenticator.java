/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.AuthenticationMechanism;
import com.mongodb.MongoException;
import com.mongodb.MongoSecurityException;
import com.mongodb.assertions.Assertions;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.connection.ServerVersion;
import com.mongodb.internal.connection.Authenticator;
import com.mongodb.internal.connection.CommandHelper;
import com.mongodb.internal.connection.InternalConnection;
import com.mongodb.internal.connection.MongoCredentialWithCache;
import com.mongodb.internal.connection.NativeAuthenticator;
import com.mongodb.internal.connection.ScramShaAuthenticator;
import com.mongodb.internal.operation.ServerVersionHelper;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;

class DefaultAuthenticator
extends Authenticator {
    static final int USER_NOT_FOUND_CODE = 11;
    private static final ServerVersion FOUR_ZERO = new ServerVersion(4, 0);
    private static final ServerVersion THREE_ZERO = new ServerVersion(3, 0);
    private static final BsonString DEFAULT_MECHANISM_NAME = new BsonString(AuthenticationMechanism.SCRAM_SHA_256.getMechanismName());

    DefaultAuthenticator(MongoCredentialWithCache credential) {
        super(credential);
        Assertions.isTrueArgument("unspecified authentication mechanism", credential.getAuthenticationMechanism() == null);
    }

    @Override
    void authenticate(InternalConnection connection, ConnectionDescription connectionDescription) {
        if (ServerVersionHelper.serverIsLessThanVersionFourDotZero(connectionDescription)) {
            this.getLegacyDefaultAuthenticator(connectionDescription).authenticate(connection, connectionDescription);
        } else {
            try {
                BsonDocument isMasterResult = CommandHelper.executeCommand("admin", this.createIsMasterCommand(), connection);
                this.getAuthenticatorFromIsMasterResult(isMasterResult, connectionDescription).authenticate(connection, connectionDescription);
            } catch (Exception e2) {
                throw this.wrapException(e2);
            }
        }
    }

    @Override
    void authenticateAsync(final InternalConnection connection, final ConnectionDescription connectionDescription, final SingleResultCallback<Void> callback) {
        if (ServerVersionHelper.serverIsLessThanVersionFourDotZero(connectionDescription)) {
            this.getLegacyDefaultAuthenticator(connectionDescription).authenticateAsync(connection, connectionDescription, callback);
        } else {
            CommandHelper.executeCommandAsync("admin", this.createIsMasterCommand(), connection, new SingleResultCallback<BsonDocument>(){

                @Override
                public void onResult(BsonDocument result, Throwable t2) {
                    if (t2 != null) {
                        callback.onResult(null, DefaultAuthenticator.this.wrapException(t2));
                    } else {
                        DefaultAuthenticator.this.getAuthenticatorFromIsMasterResult(result, connectionDescription).authenticateAsync(connection, connectionDescription, callback);
                    }
                }
            });
        }
    }

    Authenticator getAuthenticatorFromIsMasterResult(BsonDocument isMasterResult, ConnectionDescription connectionDescription) {
        if (isMasterResult.containsKey("saslSupportedMechs")) {
            BsonArray saslSupportedMechs = isMasterResult.getArray("saslSupportedMechs");
            AuthenticationMechanism mechanism = saslSupportedMechs.contains(DEFAULT_MECHANISM_NAME) ? AuthenticationMechanism.SCRAM_SHA_256 : AuthenticationMechanism.SCRAM_SHA_1;
            return new ScramShaAuthenticator(this.getMongoCredentialWithCache().withMechanism(mechanism));
        }
        return this.getLegacyDefaultAuthenticator(connectionDescription);
    }

    private Authenticator getLegacyDefaultAuthenticator(ConnectionDescription connectionDescription) {
        if (ServerVersionHelper.serverIsAtLeastVersionThreeDotZero(connectionDescription)) {
            return new ScramShaAuthenticator(this.getMongoCredentialWithCache().withMechanism(AuthenticationMechanism.SCRAM_SHA_1));
        }
        return new NativeAuthenticator(this.getMongoCredentialWithCache());
    }

    private BsonDocument createIsMasterCommand() {
        BsonDocument isMasterCommandDocument = new BsonDocument("ismaster", new BsonInt32(1));
        isMasterCommandDocument.append("saslSupportedMechs", new BsonString(String.format("%s.%s", this.getMongoCredential().getSource(), this.getMongoCredential().getUserName())));
        return isMasterCommandDocument;
    }

    private MongoException wrapException(Throwable t2) {
        if (t2 instanceof MongoSecurityException) {
            return (MongoSecurityException)t2;
        }
        if (t2 instanceof MongoException && ((MongoException)t2).getCode() == 11) {
            return new MongoSecurityException(this.getMongoCredential(), String.format("Exception authenticating %s", this.getMongoCredential()), t2);
        }
        return MongoException.fromThrowable(t2);
    }
}

