/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.MongoServerException;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcernResult;
import com.mongodb.assertions.Assertions;
import com.mongodb.bulk.WriteConcernError;
import com.mongodb.lang.Nullable;

public class MongoWriteConcernException
extends MongoServerException {
    private static final long serialVersionUID = 4577579466973523211L;
    private final WriteConcernError writeConcernError;
    private final WriteConcernResult writeConcernResult;

    public MongoWriteConcernException(WriteConcernError writeConcernError, ServerAddress serverAddress) {
        this(writeConcernError, null, serverAddress);
    }

    public MongoWriteConcernException(WriteConcernError writeConcernError, @Nullable WriteConcernResult writeConcernResult, ServerAddress serverAddress) {
        super(writeConcernError.getCode(), writeConcernError.getMessage(), serverAddress);
        this.writeConcernResult = writeConcernResult;
        this.writeConcernError = Assertions.notNull("writeConcernError", writeConcernError);
    }

    public WriteConcernError getWriteConcernError() {
        return this.writeConcernError;
    }

    public WriteConcernResult getWriteResult() {
        return this.writeConcernResult;
    }
}

