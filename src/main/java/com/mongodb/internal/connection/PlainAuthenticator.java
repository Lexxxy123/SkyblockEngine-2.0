/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.AuthenticationMechanism;
import com.mongodb.MongoCredential;
import com.mongodb.MongoSecurityException;
import com.mongodb.ServerAddress;
import com.mongodb.assertions.Assertions;
import com.mongodb.internal.connection.MongoCredentialWithCache;
import com.mongodb.internal.connection.SaslAuthenticator;
import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;

class PlainAuthenticator
extends SaslAuthenticator {
    private static final String DEFAULT_PROTOCOL = "mongodb";

    PlainAuthenticator(MongoCredentialWithCache credential) {
        super(credential);
    }

    @Override
    public String getMechanismName() {
        return AuthenticationMechanism.PLAIN.getMechanismName();
    }

    @Override
    protected SaslClient createSaslClient(ServerAddress serverAddress) {
        final MongoCredential credential = this.getMongoCredential();
        Assertions.isTrue("mechanism is PLAIN", credential.getAuthenticationMechanism() == AuthenticationMechanism.PLAIN);
        try {
            return Sasl.createSaslClient(new String[]{AuthenticationMechanism.PLAIN.getMechanismName()}, credential.getUserName(), DEFAULT_PROTOCOL, serverAddress.getHost(), null, new CallbackHandler(){

                @Override
                public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
                    for (Callback callback : callbacks) {
                        if (callback instanceof PasswordCallback) {
                            ((PasswordCallback)callback).setPassword(credential.getPassword());
                            continue;
                        }
                        if (!(callback instanceof NameCallback)) continue;
                        ((NameCallback)callback).setName(credential.getUserName());
                    }
                }
            });
        } catch (SaslException e2) {
            throw new MongoSecurityException(credential, "Exception initializing SASL client", (Throwable)e2);
        }
    }
}

