/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.MongoCommandException;
import com.mongodb.MongoServerException;
import com.mongodb.ServerAddress;

public class MongoQueryException
extends MongoServerException {
    private static final long serialVersionUID = -5113350133297015801L;
    private final String errorMessage;

    public MongoQueryException(ServerAddress address, int errorCode, String errorMessage) {
        super(errorCode, String.format("Query failed with error code %d and error message '%s' on server %s", errorCode, errorMessage, address), address);
        this.errorMessage = errorMessage;
    }

    public MongoQueryException(MongoCommandException commandException) {
        this(commandException.getServerAddress(), commandException.getErrorCode(), commandException.getErrorMessage());
        for (String label : commandException.getErrorLabels()) {
            this.addLabel(label);
        }
    }

    public int getErrorCode() {
        return this.getCode();
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}

