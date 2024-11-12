/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoCredential;
import com.mongodb.MongoInternalException;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.internal.connection.InternalConnection;
import com.mongodb.internal.connection.MongoCredentialWithCache;
import com.mongodb.lang.NonNull;
import com.mongodb.lang.Nullable;

public abstract class Authenticator {
    private final MongoCredentialWithCache credential;

    Authenticator(@NonNull MongoCredentialWithCache credential) {
        this.credential = credential;
    }

    @NonNull
    MongoCredentialWithCache getMongoCredentialWithCache() {
        return this.credential;
    }

    @NonNull
    MongoCredential getMongoCredential() {
        return this.credential.getCredential();
    }

    @NonNull
    String getUserNameNonNull() {
        String userName = this.credential.getCredential().getUserName();
        if (userName == null) {
            throw new MongoInternalException("User name can not be null");
        }
        return userName;
    }

    @NonNull
    char[] getPasswordNonNull() {
        char[] password = this.credential.getCredential().getPassword();
        if (password == null) {
            throw new MongoInternalException("Password can not be null");
        }
        return password;
    }

    @NonNull
    <T> T getNonNullMechanismProperty(String key, @Nullable T defaultValue) {
        T mechanismProperty = this.credential.getCredential().getMechanismProperty(key, defaultValue);
        if (mechanismProperty == null) {
            throw new MongoInternalException("Mechanism property can not be null");
        }
        return mechanismProperty;
    }

    abstract void authenticate(InternalConnection var1, ConnectionDescription var2);

    abstract void authenticateAsync(InternalConnection var1, ConnectionDescription var2, SingleResultCallback<Void> var3);
}

