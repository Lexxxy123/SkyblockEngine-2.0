/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.MongoException;

public class MongoChangeStreamException
extends MongoException {
    private static final long serialVersionUID = 3621370414132219001L;

    public MongoChangeStreamException(String message) {
        super(message);
    }
}

