/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.AuthenticationMechanism;
import com.mongodb.MongoCommandException;
import com.mongodb.MongoSecurityException;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.internal.connection.Authenticator;
import com.mongodb.internal.connection.CommandHelper;
import com.mongodb.internal.connection.InternalConnection;
import com.mongodb.internal.connection.MongoCredentialWithCache;
import com.mongodb.internal.operation.ServerVersionHelper;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;

class X509Authenticator
extends Authenticator {
    X509Authenticator(MongoCredentialWithCache credential) {
        super(credential);
    }

    @Override
    void authenticate(InternalConnection connection, ConnectionDescription connectionDescription) {
        try {
            this.validateUserName(connectionDescription);
            BsonDocument authCommand = this.getAuthCommand(this.getMongoCredential().getUserName());
            CommandHelper.executeCommand(this.getMongoCredential().getSource(), authCommand, connection);
        } catch (MongoCommandException e2) {
            throw new MongoSecurityException(this.getMongoCredential(), "Exception authenticating", (Throwable)e2);
        }
    }

    @Override
    void authenticateAsync(InternalConnection connection, ConnectionDescription connectionDescription, final SingleResultCallback<Void> callback) {
        try {
            this.validateUserName(connectionDescription);
            CommandHelper.executeCommandAsync(this.getMongoCredential().getSource(), this.getAuthCommand(this.getMongoCredential().getUserName()), connection, new SingleResultCallback<BsonDocument>(){

                @Override
                public void onResult(BsonDocument nonceResult, Throwable t2) {
                    if (t2 != null) {
                        callback.onResult(null, X509Authenticator.this.translateThrowable(t2));
                    } else {
                        callback.onResult(null, null);
                    }
                }
            });
        } catch (Throwable t2) {
            callback.onResult(null, t2);
        }
    }

    private BsonDocument getAuthCommand(String userName) {
        BsonDocument cmd = new BsonDocument();
        cmd.put("authenticate", new BsonInt32(1));
        if (userName != null) {
            cmd.put("user", new BsonString(userName));
        }
        cmd.put("mechanism", new BsonString(AuthenticationMechanism.MONGODB_X509.getMechanismName()));
        return cmd;
    }

    private Throwable translateThrowable(Throwable t2) {
        if (t2 instanceof MongoCommandException) {
            return new MongoSecurityException(this.getMongoCredential(), "Exception authenticating", t2);
        }
        return t2;
    }

    private void validateUserName(ConnectionDescription connectionDescription) {
        if (this.getMongoCredential().getUserName() == null && ServerVersionHelper.serverIsLessThanVersionThreeDotFour(connectionDescription)) {
            throw new MongoSecurityException(this.getMongoCredential(), "User name is required for the MONGODB-X509 authentication mechanism on server versions less than 3.4");
        }
    }
}

