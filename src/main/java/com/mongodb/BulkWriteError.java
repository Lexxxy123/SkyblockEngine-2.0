/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.DBObject;
import com.mongodb.assertions.Assertions;

public class BulkWriteError {
    private final int index;
    private final int code;
    private final String message;
    private final DBObject details;

    public BulkWriteError(int code, String message, DBObject details, int index) {
        this.code = code;
        this.message = Assertions.notNull("message", message);
        this.details = Assertions.notNull("details", details);
        this.index = index;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public DBObject getDetails() {
        return this.details;
    }

    public int getIndex() {
        return this.index;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        BulkWriteError that = (BulkWriteError)o2;
        if (this.code != that.code) {
            return false;
        }
        if (this.index != that.index) {
            return false;
        }
        if (!this.details.equals(that.details)) {
            return false;
        }
        return this.message.equals(that.message);
    }

    public int hashCode() {
        int result = this.index;
        result = 31 * result + this.code;
        result = 31 * result + this.message.hashCode();
        result = 31 * result + this.details.hashCode();
        return result;
    }

    public String toString() {
        return "BulkWriteError{index=" + this.index + ", code=" + this.code + ", message='" + this.message + '\'' + ", details=" + this.details + '}';
    }
}

