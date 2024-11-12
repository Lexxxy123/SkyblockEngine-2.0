/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.bulk;

import com.mongodb.assertions.Assertions;
import org.bson.BsonDocument;

public class WriteConcernError {
    private final int code;
    private final String codeName;
    private final String message;
    private final BsonDocument details;

    public WriteConcernError(int code, String codeName, String message, BsonDocument details) {
        this.code = code;
        this.codeName = Assertions.notNull("codeName", codeName);
        this.message = Assertions.notNull("message", message);
        this.details = Assertions.notNull("details", details);
    }

    @Deprecated
    public WriteConcernError(int code, String message, BsonDocument details) {
        this(code, "", message, details);
    }

    public int getCode() {
        return this.code;
    }

    public String getCodeName() {
        return this.codeName;
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
        WriteConcernError that = (WriteConcernError)o2;
        if (this.code != that.code) {
            return false;
        }
        if (!this.codeName.equals(that.codeName)) {
            return false;
        }
        if (!this.details.equals(that.details)) {
            return false;
        }
        return this.message.equals(that.message);
    }

    public int hashCode() {
        int result = this.code;
        result = 31 * result + this.codeName.hashCode();
        result = 31 * result + this.message.hashCode();
        result = 31 * result + this.details.hashCode();
        return result;
    }

    public String toString() {
        return "WriteConcernError{code=" + this.code + ", codeName='" + this.codeName + '\'' + ", message='" + this.message + '\'' + ", details=" + this.details + '}';
    }
}

