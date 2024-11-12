/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.MongoNamespace;
import com.mongodb.binding.ReadBinding;
import com.mongodb.binding.WriteBinding;
import com.mongodb.connection.Connection;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.connection.ServerDescription;
import com.mongodb.internal.operation.ServerVersionHelper;
import com.mongodb.operation.CommandOperationHelper;
import com.mongodb.operation.OperationHelper;
import com.mongodb.operation.ReadOperation;
import com.mongodb.operation.WriteOperation;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.codecs.BsonDocumentCodec;

@Deprecated
public class FsyncUnlockOperation
implements WriteOperation<BsonDocument>,
ReadOperation<BsonDocument> {
    private static final BsonDocument FSYNC_UNLOCK_COMMAND = new BsonDocument("fsyncUnlock", new BsonInt32(1));

    @Override
    @Deprecated
    public BsonDocument execute(final WriteBinding binding) {
        return OperationHelper.withConnection(binding, new OperationHelper.CallableWithConnection<BsonDocument>(){

            @Override
            public BsonDocument call(Connection connection) {
                if (ServerVersionHelper.serverIsAtLeastVersionThreeDotTwo(connection.getDescription())) {
                    return CommandOperationHelper.executeCommand(binding, "admin", FSYNC_UNLOCK_COMMAND, connection);
                }
                return FsyncUnlockOperation.this.queryUnlock(connection);
            }
        });
    }

    @Override
    public BsonDocument execute(final ReadBinding binding) {
        return OperationHelper.withConnection(binding, new OperationHelper.CallableWithConnection<BsonDocument>(){

            @Override
            public BsonDocument call(Connection connection) {
                if (ServerVersionHelper.serverIsAtLeastVersionThreeDotTwo(connection.getDescription())) {
                    return CommandOperationHelper.executeCommand(binding, "admin", FsyncUnlockOperation.this.getCommandCreator(), false);
                }
                return FsyncUnlockOperation.this.queryUnlock(connection);
            }
        });
    }

    private BsonDocument queryUnlock(Connection connection) {
        return connection.query(new MongoNamespace("admin", "$cmd.sys.unlock"), new BsonDocument(), null, 0, 1, 0, false, false, false, false, false, false, new BsonDocumentCodec()).getResults().get(0);
    }

    private CommandOperationHelper.CommandCreator getCommandCreator() {
        return new CommandOperationHelper.CommandCreator(){

            @Override
            public BsonDocument create(ServerDescription serverDescription, ConnectionDescription connectionDescription) {
                return FSYNC_UNLOCK_COMMAND;
            }
        };
    }
}

