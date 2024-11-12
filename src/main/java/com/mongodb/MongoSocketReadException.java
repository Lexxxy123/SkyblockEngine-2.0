/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.MongoSocketException;
import com.mongodb.ServerAddress;

public class MongoSocketReadException
extends MongoSocketException {
    private static final long serialVersionUID = -1142547119966956531L;

    public MongoSocketReadException(String message, ServerAddress address) {
        super(message, address);
    }

    public MongoSocketReadException(String message, ServerAddress address, Throwable cause) {
        super(message, address, cause);
    }
}

