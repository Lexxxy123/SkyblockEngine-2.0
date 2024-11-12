/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.MongoException;
import com.mongodb.ServerAddress;

public abstract class MongoServerException
extends MongoException {
    private static final long serialVersionUID = -5213859742051776206L;
    private final ServerAddress serverAddress;

    public MongoServerException(String message, ServerAddress serverAddress) {
        super(message);
        this.serverAddress = serverAddress;
    }

    public MongoServerException(int code, String message, ServerAddress serverAddress) {
        super(code, message);
        this.serverAddress = serverAddress;
    }

    public ServerAddress getServerAddress() {
        return this.serverAddress;
    }
}

