/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObjects;
import com.mongodb.MongoCommandException;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.assertions.Assertions;
import com.mongodb.lang.Nullable;
import org.bson.BsonDocument;

public class CommandResult
extends BasicDBObject {
    private static final long serialVersionUID = 5907909423864204060L;
    private final BsonDocument response;
    private final ServerAddress address;

    CommandResult(BsonDocument response) {
        this(response, null);
    }

    CommandResult(BsonDocument response, @Nullable ServerAddress address) {
        this.address = address;
        this.response = Assertions.notNull("response", response);
        this.putAll(DBObjects.toDBObject(response));
    }

    public boolean ok() {
        Object okValue = this.get("ok");
        if (okValue instanceof Boolean) {
            return (Boolean)okValue;
        }
        if (okValue instanceof Number) {
            return ((Number)okValue).intValue() == 1;
        }
        return false;
    }

    @Nullable
    public String getErrorMessage() {
        Object foo = this.get("errmsg");
        if (foo == null) {
            return null;
        }
        return foo.toString();
    }

    @Nullable
    public MongoException getException() {
        if (!this.ok()) {
            return this.createException();
        }
        return null;
    }

    public void throwOnError() {
        if (!this.ok()) {
            throw this.createException();
        }
    }

    private MongoException createException() {
        return new MongoCommandException(this.response, this.address);
    }
}

