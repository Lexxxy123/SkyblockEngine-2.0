/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.MongoNamespace;
import com.mongodb.binding.ReadBinding;
import com.mongodb.connection.Connection;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.connection.ServerDescription;
import com.mongodb.internal.operation.ServerVersionHelper;
import com.mongodb.operation.CommandOperationHelper;
import com.mongodb.operation.OperationHelper;
import com.mongodb.operation.ReadOperation;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.codecs.BsonDocumentCodec;

@Deprecated
public class CurrentOpOperation
implements ReadOperation<BsonDocument> {
    @Override
    public BsonDocument execute(final ReadBinding binding) {
        return OperationHelper.withConnection(binding, new OperationHelper.CallableWithConnection<BsonDocument>(){

            @Override
            public BsonDocument call(Connection connection) {
                if (ServerVersionHelper.serverIsAtLeastVersionThreeDotTwo(connection.getDescription())) {
                    return CommandOperationHelper.executeCommand(binding, "admin", CurrentOpOperation.this.getCommandCreator(), true);
                }
                return connection.query(new MongoNamespace("admin", "$cmd.sys.inprog"), new BsonDocument(), null, 0, 1, 0, binding.getReadPreference().isSlaveOk(), false, false, false, false, false, new BsonDocumentCodec()).getResults().get(0);
            }
        });
    }

    private CommandOperationHelper.CommandCreator getCommandCreator() {
        return new CommandOperationHelper.CommandCreator(){

            @Override
            public BsonDocument create(ServerDescription serverDescription, ConnectionDescription connectionDescription) {
                return new BsonDocument("currentOp", new BsonInt32(1));
            }
        };
    }
}

