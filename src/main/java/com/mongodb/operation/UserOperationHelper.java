/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.MongoCommandException;
import com.mongodb.MongoCredential;
import com.mongodb.MongoInternalException;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.connection.ServerVersion;
import com.mongodb.internal.authentication.NativeAuthenticationHelper;
import com.mongodb.internal.operation.ServerVersionHelper;
import com.mongodb.internal.operation.WriteConcernHelper;
import com.mongodb.lang.NonNull;
import java.util.Collections;
import org.bson.BsonArray;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonString;

final class UserOperationHelper {
    private static final ServerVersion FOUR_ZERO = new ServerVersion(4, 0);

    static BsonDocument asCommandDocument(MongoCredential credential, ConnectionDescription connectionDescription, boolean readOnly, String commandName) {
        boolean serverDigestPassword = ServerVersionHelper.serverIsAtLeastVersionFourDotZero(connectionDescription);
        BsonDocument document = new BsonDocument();
        document.put(commandName, new BsonString(UserOperationHelper.getUserNameNonNull(credential)));
        if (serverDigestPassword) {
            document.put("pwd", new BsonString(new String(UserOperationHelper.getPasswordNonNull(credential))));
        } else {
            document.put("pwd", new BsonString(NativeAuthenticationHelper.createAuthenticationHash(UserOperationHelper.getUserNameNonNull(credential), UserOperationHelper.getPasswordNonNull(credential))));
        }
        document.put("digestPassword", BsonBoolean.valueOf(serverDigestPassword));
        document.put("roles", new BsonArray(Collections.singletonList(new BsonString(UserOperationHelper.getRoleName(credential, readOnly)))));
        return document;
    }

    private static String getRoleName(MongoCredential credential, boolean readOnly) {
        return credential.getSource().equals("admin") ? (readOnly ? "readAnyDatabase" : "root") : (readOnly ? "read" : "dbOwner");
    }

    static void translateUserCommandException(MongoCommandException e2) {
        if (e2.getErrorCode() == 100 && WriteConcernHelper.hasWriteConcernError(e2.getResponse())) {
            throw WriteConcernHelper.createWriteConcernException(e2.getResponse(), e2.getServerAddress());
        }
        throw e2;
    }

    static SingleResultCallback<Void> userCommandCallback(final SingleResultCallback<Void> wrappedCallback) {
        return new SingleResultCallback<Void>(){

            @Override
            public void onResult(Void result, Throwable t2) {
                if (t2 != null) {
                    if (t2 instanceof MongoCommandException && WriteConcernHelper.hasWriteConcernError(((MongoCommandException)t2).getResponse())) {
                        wrappedCallback.onResult(null, WriteConcernHelper.createWriteConcernException(((MongoCommandException)t2).getResponse(), ((MongoCommandException)t2).getServerAddress()));
                    } else {
                        wrappedCallback.onResult(null, t2);
                    }
                } else {
                    wrappedCallback.onResult(null, null);
                }
            }
        };
    }

    @NonNull
    private static String getUserNameNonNull(MongoCredential credential) {
        String userName = credential.getUserName();
        if (userName == null) {
            throw new MongoInternalException("User name can not be null");
        }
        return userName;
    }

    @NonNull
    private static char[] getPasswordNonNull(MongoCredential credential) {
        char[] password = credential.getPassword();
        if (password == null) {
            throw new MongoInternalException("Password can not be null");
        }
        return password;
    }

    private UserOperationHelper() {
    }
}

