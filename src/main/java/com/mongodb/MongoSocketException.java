/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.MongoException;
import com.mongodb.ServerAddress;

public class MongoSocketException
extends MongoException {
    private static final long serialVersionUID = -4415279469780082174L;
    private final ServerAddress serverAddress;

    MongoSocketException(String msg, ServerAddress serverAddress, Throwable e2) {
        super(-2, msg, e2);
        this.serverAddress = serverAddress;
    }

    public MongoSocketException(String message, ServerAddress serverAddress) {
        super(-2, message);
        this.serverAddress = serverAddress;
    }

    public ServerAddress getServerAddress() {
        return this.serverAddress;
    }
}

