/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoNamespace;
import com.mongodb.MongoServerException;
import com.mongodb.ReadPreference;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.internal.connection.ClusterClock;
import com.mongodb.internal.connection.ClusterClockAdvancingSessionContext;
import com.mongodb.internal.connection.CommandMessage;
import com.mongodb.internal.connection.InternalConnection;
import com.mongodb.internal.connection.MessageSettings;
import com.mongodb.internal.connection.NoOpSessionContext;
import com.mongodb.internal.validator.NoOpFieldNameValidator;
import com.mongodb.session.SessionContext;
import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.bson.codecs.BsonDocumentCodec;

public final class CommandHelper {
    static BsonDocument executeCommand(String database, BsonDocument command, InternalConnection internalConnection) {
        return CommandHelper.sendAndReceive(database, command, null, internalConnection);
    }

    public static BsonDocument executeCommand(String database, BsonDocument command, ClusterClock clusterClock, InternalConnection internalConnection) {
        return CommandHelper.sendAndReceive(database, command, clusterClock, internalConnection);
    }

    static BsonDocument executeCommandWithoutCheckingForFailure(String database, BsonDocument command, InternalConnection internalConnection) {
        try {
            return CommandHelper.sendAndReceive(database, command, null, internalConnection);
        } catch (MongoServerException e2) {
            return new BsonDocument();
        }
    }

    static void executeCommandAsync(String database, BsonDocument command, InternalConnection internalConnection, final SingleResultCallback<BsonDocument> callback) {
        internalConnection.sendAndReceiveAsync(CommandHelper.getCommandMessage(database, command, internalConnection), new BsonDocumentCodec(), NoOpSessionContext.INSTANCE, new SingleResultCallback<BsonDocument>(){

            @Override
            public void onResult(BsonDocument result, Throwable t2) {
                if (t2 != null) {
                    callback.onResult(null, t2);
                } else {
                    callback.onResult(result, null);
                }
            }
        });
    }

    static boolean isCommandOk(BsonDocument response) {
        if (!response.containsKey("ok")) {
            return false;
        }
        BsonValue okValue = response.get("ok");
        if (okValue.isBoolean()) {
            return okValue.asBoolean().getValue();
        }
        if (okValue.isNumber()) {
            return okValue.asNumber().intValue() == 1;
        }
        return false;
    }

    private static BsonDocument sendAndReceive(String database, BsonDocument command, ClusterClock clusterClock, InternalConnection internalConnection) {
        SessionContext sessionContext = clusterClock == null ? NoOpSessionContext.INSTANCE : new ClusterClockAdvancingSessionContext(NoOpSessionContext.INSTANCE, clusterClock);
        return internalConnection.sendAndReceive(CommandHelper.getCommandMessage(database, command, internalConnection), new BsonDocumentCodec(), sessionContext);
    }

    private static CommandMessage getCommandMessage(String database, BsonDocument command, InternalConnection internalConnection) {
        return new CommandMessage(new MongoNamespace(database, "$cmd"), command, new NoOpFieldNameValidator(), ReadPreference.primary(), MessageSettings.builder().maxWireVersion(internalConnection.getDescription().getMaxWireVersion()).build());
    }

    private CommandHelper() {
    }
}

