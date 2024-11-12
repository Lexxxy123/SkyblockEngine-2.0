/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.MongoSocketException;
import com.mongodb.ServerAddress;

public class MongoSocketReadTimeoutException
extends MongoSocketException {
    private static final long serialVersionUID = -7237059971254608960L;

    public MongoSocketReadTimeoutException(String message, ServerAddress address, Throwable cause) {
        super(message, address, cause);
    }
}

