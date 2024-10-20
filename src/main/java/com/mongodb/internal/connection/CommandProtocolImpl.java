/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoNamespace;
import com.mongodb.ReadPreference;
import com.mongodb.assertions.Assertions;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.connection.ClusterConnectionMode;
import com.mongodb.connection.SplittablePayload;
import com.mongodb.internal.connection.CommandMessage;
import com.mongodb.internal.connection.CommandProtocol;
import com.mongodb.internal.connection.InternalConnection;
import com.mongodb.internal.connection.ProtocolHelper;
import com.mongodb.session.SessionContext;
import org.bson.BsonDocument;
import org.bson.FieldNameValidator;
import org.bson.codecs.Decoder;

class CommandProtocolImpl<T>
implements CommandProtocol<T> {
    private final MongoNamespace namespace;
    private final BsonDocument command;
    private final SplittablePayload payload;
    private final ReadPreference readPreference;
    private final FieldNameValidator commandFieldNameValidator;
    private final FieldNameValidator payloadFieldNameValidator;
    private final Decoder<T> commandResultDecoder;
    private final boolean responseExpected;
    private final ClusterConnectionMode clusterConnectionMode;
    private SessionContext sessionContext;

    CommandProtocolImpl(String database, BsonDocument command, FieldNameValidator commandFieldNameValidator, ReadPreference readPreference, Decoder<T> commandResultDecoder) {
        this(database, command, commandFieldNameValidator, readPreference, commandResultDecoder, true, null, null, ClusterConnectionMode.MULTIPLE);
    }

    CommandProtocolImpl(String database, BsonDocument command, FieldNameValidator commandFieldNameValidator, ReadPreference readPreference, Decoder<T> commandResultDecoder, boolean responseExpected, SplittablePayload payload, FieldNameValidator payloadFieldNameValidator, ClusterConnectionMode clusterConnectionMode) {
        Assertions.notNull("database", database);
        this.namespace = new MongoNamespace(Assertions.notNull("database", database), "$cmd");
        this.command = Assertions.notNull("command", command);
        this.commandFieldNameValidator = Assertions.notNull("commandFieldNameValidator", commandFieldNameValidator);
        this.readPreference = readPreference;
        this.commandResultDecoder = Assertions.notNull("commandResultDecoder", commandResultDecoder);
        this.responseExpected = responseExpected;
        this.payload = payload;
        this.payloadFieldNameValidator = payloadFieldNameValidator;
        this.clusterConnectionMode = Assertions.notNull("clusterConnectionMode", clusterConnectionMode);
        Assertions.isTrueArgument("payloadFieldNameValidator cannot be null if there is a payload.", payload == null || payloadFieldNameValidator != null);
    }

    @Override
    public T execute(InternalConnection connection) {
        return connection.sendAndReceive(this.getCommandMessage(connection), this.commandResultDecoder, this.sessionContext);
    }

    @Override
    public void executeAsync(InternalConnection connection, final SingleResultCallback<T> callback) {
        try {
            connection.sendAndReceiveAsync(this.getCommandMessage(connection), this.commandResultDecoder, this.sessionContext, new SingleResultCallback<T>(){

                @Override
                public void onResult(T result, Throwable t2) {
                    if (t2 != null) {
                        callback.onResult(null, t2);
                    } else {
                        callback.onResult(result, null);
                    }
                }
            });
        } catch (Throwable t2) {
            callback.onResult(null, t2);
        }
    }

    @Override
    public CommandProtocolImpl<T> sessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
        return this;
    }

    private CommandMessage getCommandMessage(InternalConnection connection) {
        return new CommandMessage(this.namespace, this.command, this.commandFieldNameValidator, this.readPreference, ProtocolHelper.getMessageSettings(connection.getDescription()), this.responseExpected, this.payload, this.payloadFieldNameValidator, this.clusterConnectionMode);
    }
}

