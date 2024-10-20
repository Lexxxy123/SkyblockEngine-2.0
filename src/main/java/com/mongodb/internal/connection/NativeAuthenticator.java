/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoCommandException;
import com.mongodb.MongoSecurityException;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.internal.authentication.NativeAuthenticationHelper;
import com.mongodb.internal.connection.Authenticator;
import com.mongodb.internal.connection.CommandHelper;
import com.mongodb.internal.connection.InternalConnection;
import com.mongodb.internal.connection.MongoCredentialWithCache;
import org.bson.BsonDocument;
import org.bson.BsonString;

class NativeAuthenticator
extends Authenticator {
    NativeAuthenticator(MongoCredentialWithCache credential) {
        super(credential);
    }

    @Override
    public void authenticate(InternalConnection connection, ConnectionDescription connectionDescription) {
        try {
            BsonDocument nonceResponse = CommandHelper.executeCommand(this.getMongoCredential().getSource(), NativeAuthenticationHelper.getNonceCommand(), connection);
            BsonDocument authCommand = NativeAuthenticationHelper.getAuthCommand(this.getUserNameNonNull(), this.getPasswordNonNull(), ((BsonString)nonceResponse.get("nonce")).getValue());
            CommandHelper.executeCommand(this.getMongoCredential().getSource(), authCommand, connection);
        } catch (MongoCommandException e2) {
            throw new MongoSecurityException(this.getMongoCredential(), "Exception authenticating", (Throwable)e2);
        }
    }

    @Override
    void authenticateAsync(final InternalConnection connection, ConnectionDescription connectionDescription, final SingleResultCallback<Void> callback) {
        CommandHelper.executeCommandAsync(this.getMongoCredential().getSource(), NativeAuthenticationHelper.getNonceCommand(), connection, new SingleResultCallback<BsonDocument>(){

            @Override
            public void onResult(BsonDocument nonceResult, Throwable t2) {
                if (t2 != null) {
                    callback.onResult(null, NativeAuthenticator.this.translateThrowable(t2));
                } else {
                    CommandHelper.executeCommandAsync(NativeAuthenticator.this.getMongoCredential().getSource(), NativeAuthenticationHelper.getAuthCommand(NativeAuthenticator.this.getUserNameNonNull(), NativeAuthenticator.this.getPasswordNonNull(), ((BsonString)nonceResult.get("nonce")).getValue()), connection, new SingleResultCallback<BsonDocument>(){

                        @Override
                        public void onResult(BsonDocument result, Throwable t2) {
                            if (t2 != null) {
                                callback.onResult(null, NativeAuthenticator.this.translateThrowable(t2));
                            } else {
                                callback.onResult(null, null);
                            }
                        }
                    });
                }
            }
        });
    }

    private Throwable translateThrowable(Throwable t2) {
        if (t2 instanceof MongoCommandException) {
            return new MongoSecurityException(this.getMongoCredential(), "Exception authenticating", t2);
        }
        return t2;
    }
}

