/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.internal.connection.LegacyMessage;
import com.mongodb.internal.connection.MessageSettings;
import com.mongodb.internal.connection.OpCode;
import org.bson.io.BsonOutput;

abstract class BaseQueryMessage
extends LegacyMessage {
    private final int skip;
    private final int numberToReturn;
    private boolean tailableCursor;
    private boolean slaveOk;
    private boolean oplogReplay;
    private boolean noCursorTimeout;
    private boolean awaitData;
    private boolean partial;

    BaseQueryMessage(String collectionName, int skip, int numberToReturn, MessageSettings settings) {
        super(collectionName, OpCode.OP_QUERY, settings);
        this.skip = skip;
        this.numberToReturn = numberToReturn;
    }

    public boolean isTailableCursor() {
        return this.tailableCursor;
    }

    public BaseQueryMessage tailableCursor(boolean tailableCursor) {
        this.tailableCursor = tailableCursor;
        return this;
    }

    public boolean isSlaveOk() {
        return this.slaveOk;
    }

    public BaseQueryMessage slaveOk(boolean slaveOk) {
        this.slaveOk = slaveOk;
        return this;
    }

    public boolean isOplogReplay() {
        return this.oplogReplay;
    }

    public BaseQueryMessage oplogReplay(boolean oplogReplay) {
        this.oplogReplay = oplogReplay;
        return this;
    }

    public boolean isNoCursorTimeout() {
        return this.noCursorTimeout;
    }

    public BaseQueryMessage noCursorTimeout(boolean noCursorTimeout) {
        this.noCursorTimeout = noCursorTimeout;
        return this;
    }

    public boolean isAwaitData() {
        return this.awaitData;
    }

    public BaseQueryMessage awaitData(boolean awaitData) {
        this.awaitData = awaitData;
        return this;
    }

    public boolean isPartial() {
        return this.partial;
    }

    public BaseQueryMessage partial(boolean partial) {
        this.partial = partial;
        return this;
    }

    private int getCursorFlag() {
        int cursorFlag = 0;
        if (this.isTailableCursor()) {
            cursorFlag |= 2;
        }
        if (this.isSlaveOk()) {
            cursorFlag |= 4;
        }
        if (this.isOplogReplay()) {
            cursorFlag |= 8;
        }
        if (this.isNoCursorTimeout()) {
            cursorFlag |= 0x10;
        }
        if (this.isAwaitData()) {
            cursorFlag |= 0x20;
        }
        if (this.isPartial()) {
            cursorFlag |= 0x80;
        }
        return cursorFlag;
    }

    protected void writeQueryPrologue(BsonOutput bsonOutput) {
        bsonOutput.writeInt32(this.getCursorFlag());
        bsonOutput.writeCString(this.getCollectionName());
        bsonOutput.writeInt32(this.skip);
        bsonOutput.writeInt32(this.numberToReturn);
    }
}

