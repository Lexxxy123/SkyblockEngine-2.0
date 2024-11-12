/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.MongoSocketException;
import com.mongodb.ServerAddress;

public class MongoSocketClosedException
extends MongoSocketException {
    private static final long serialVersionUID = -6855036625330867705L;

    public MongoSocketClosedException(String message, ServerAddress address) {
        super(message, address);
    }
}

