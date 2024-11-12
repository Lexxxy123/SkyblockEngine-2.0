/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.MongoSocketException;
import com.mongodb.ServerAddress;

public class MongoSocketOpenException
extends MongoSocketException {
    private static final long serialVersionUID = 4176754100200191238L;

    public MongoSocketOpenException(String message, ServerAddress address, Throwable cause) {
        super(message, address, cause);
    }

    public MongoSocketOpenException(String message, ServerAddress address) {
        super(message, address);
    }
}

