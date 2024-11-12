/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoException;

public class MongoWriteConcernWithResponseException
extends MongoException {
    private static final long serialVersionUID = 1707360842648550287L;
    private final MongoException cause;
    private final Object response;

    public MongoWriteConcernWithResponseException(MongoException exception, Object response) {
        super(exception.getCode(), exception.getMessage(), exception);
        this.cause = exception;
        this.response = response;
    }

    @Override
    public MongoException getCause() {
        return this.cause;
    }

    public Object getResponse() {
        return this.response;
    }
}

