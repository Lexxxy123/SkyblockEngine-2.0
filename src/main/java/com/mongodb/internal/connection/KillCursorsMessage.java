/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.assertions.Assertions;
import com.mongodb.internal.connection.LegacyMessage;
import com.mongodb.internal.connection.MessageSettings;
import com.mongodb.internal.connection.OpCode;
import com.mongodb.internal.connection.RequestMessage;
import java.util.List;
import org.bson.io.BsonOutput;

class KillCursorsMessage
extends LegacyMessage {
    private final List<Long> cursors;

    KillCursorsMessage(List<Long> cursors) {
        super(OpCode.OP_KILL_CURSORS, MessageSettings.builder().build());
        this.cursors = Assertions.notNull("cursors", cursors);
    }

    @Override
    protected RequestMessage.EncodingMetadata encodeMessageBodyWithMetadata(BsonOutput bsonOutput) {
        this.writeKillCursorsPrologue(this.cursors.size(), bsonOutput);
        for (Long cur : this.cursors) {
            bsonOutput.writeInt64(cur);
        }
        return new RequestMessage.EncodingMetadata(bsonOutput.getPosition());
    }

    private void writeKillCursorsPrologue(int numCursors, BsonOutput bsonOutput) {
        bsonOutput.writeInt32(0);
        bsonOutput.writeInt32(numCursors);
    }
}

