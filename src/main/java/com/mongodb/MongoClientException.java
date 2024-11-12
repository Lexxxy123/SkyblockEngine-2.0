/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.MongoException;

public class MongoClientException
extends MongoException {
    private static final long serialVersionUID = -5127414714432646066L;

    public MongoClientException(String message) {
        super(message);
    }

    public MongoClientException(String message, Throwable cause) {
        super(message, cause);
    }
}

