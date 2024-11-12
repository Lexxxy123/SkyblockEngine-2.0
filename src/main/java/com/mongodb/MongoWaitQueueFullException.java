/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.MongoClientException;

@Deprecated
public class MongoWaitQueueFullException
extends MongoClientException {
    private static final long serialVersionUID = 1482094507852255793L;

    public MongoWaitQueueFullException(String message) {
        super(message);
    }
}

