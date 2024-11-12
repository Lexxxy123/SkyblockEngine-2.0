/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.connection;

import com.mongodb.assertions.Assertions;
import java.util.List;
import org.bson.BsonDocument;

@Deprecated
public final class SplittablePayload {
    private final Type payloadType;
    private final List<BsonDocument> payload;
    private int position = 0;

    public SplittablePayload(Type payloadType, List<BsonDocument> payload) {
        this.payloadType = Assertions.notNull("batchType", payloadType);
        this.payload = Assertions.notNull("payload", payload);
    }

    public Type getPayloadType() {
        return this.payloadType;
    }

    public String getPayloadName() {
        if (this.payloadType == Type.INSERT) {
            return "documents";
        }
        if (this.payloadType == Type.UPDATE || this.payloadType == Type.REPLACE) {
            return "updates";
        }
        return "deletes";
    }

    public List<BsonDocument> getPayload() {
        return this.payload;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean hasAnotherSplit() {
        return this.payload.size() > this.position;
    }

    public SplittablePayload getNextSplit() {
        Assertions.isTrue("hasAnotherSplit", this.hasAnotherSplit());
        List<BsonDocument> nextPayLoad = this.payload.subList(this.position, this.payload.size());
        return new SplittablePayload(this.payloadType, nextPayLoad);
    }

    public boolean isEmpty() {
        return this.payload.isEmpty();
    }

    public static enum Type {
        INSERT,
        UPDATE,
        REPLACE,
        DELETE;

    }
}

