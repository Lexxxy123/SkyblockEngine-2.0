/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.ErrorCategory;
import com.mongodb.assertions.Assertions;
import org.bson.BsonDocument;

public class WriteError {
    private final int code;
    private final String message;
    private final BsonDocument details;

    public WriteError(int code, String message, BsonDocument details) {
        this.code = code;
        this.message = Assertions.notNull("message", message);
        this.details = Assertions.notNull("details", details);
    }

    public WriteError(WriteError writeError) {
        this.code = writeError.code;
        this.message = writeError.message;
        this.details = writeError.details;
    }

    public ErrorCategory getCategory() {
        return ErrorCategory.fromErrorCode(this.code);
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public BsonDocument getDetails() {
        return this.details;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        WriteError that = (WriteError)o2;
        if (this.code != that.code) {
            return false;
        }
        if (!this.details.equals(that.details)) {
            return false;
        }
        return this.message.equals(that.message);
    }

    public int hashCode() {
        int result = this.code;
        result = 31 * result + this.message.hashCode();
        result = 31 * result + this.details.hashCode();
        return result;
    }

    public String toString() {
        return "WriteError{code=" + this.code + ", message='" + this.message + '\'' + ", details=" + this.details + '}';
    }
}

