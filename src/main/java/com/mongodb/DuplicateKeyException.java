/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.ServerAddress;
import com.mongodb.WriteConcernException;
import com.mongodb.WriteConcernResult;
import org.bson.BsonDocument;

public class DuplicateKeyException
extends WriteConcernException {
    private static final long serialVersionUID = -4415279469780082174L;

    public DuplicateKeyException(BsonDocument response, ServerAddress address, WriteConcernResult writeConcernResult) {
        super(response, address, writeConcernResult);
    }
}

