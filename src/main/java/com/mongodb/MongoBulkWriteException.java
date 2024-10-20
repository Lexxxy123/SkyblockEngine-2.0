/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.MongoServerException;
import com.mongodb.ServerAddress;
import com.mongodb.bulk.BulkWriteError;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.bulk.WriteConcernError;
import com.mongodb.lang.Nullable;
import java.util.List;

public class MongoBulkWriteException
extends MongoServerException {
    private static final long serialVersionUID = -4345399805987210275L;
    private final BulkWriteResult writeResult;
    private final List<BulkWriteError> errors;
    private final ServerAddress serverAddress;
    private final WriteConcernError writeConcernError;

    public MongoBulkWriteException(BulkWriteResult writeResult, List<BulkWriteError> writeErrors, @Nullable WriteConcernError writeConcernError, ServerAddress serverAddress) {
        super("Bulk write operation error on server " + serverAddress + ". " + (writeErrors.isEmpty() ? "" : "Write errors: " + writeErrors + ". ") + (writeConcernError == null ? "" : "Write concern error: " + writeConcernError + ". "), serverAddress);
        this.writeResult = writeResult;
        this.errors = writeErrors;
        this.writeConcernError = writeConcernError;
        this.serverAddress = serverAddress;
    }

    public BulkWriteResult getWriteResult() {
        return this.writeResult;
    }

    public List<BulkWriteError> getWriteErrors() {
        return this.errors;
    }

    public WriteConcernError getWriteConcernError() {
        return this.writeConcernError;
    }

    @Override
    public ServerAddress getServerAddress() {
        return this.serverAddress;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        MongoBulkWriteException that = (MongoBulkWriteException)o2;
        if (!this.errors.equals(that.errors)) {
            return false;
        }
        if (!this.serverAddress.equals(that.serverAddress)) {
            return false;
        }
        if (this.writeConcernError != null ? !this.writeConcernError.equals(that.writeConcernError) : that.writeConcernError != null) {
            return false;
        }
        return this.writeResult.equals(that.writeResult);
    }

    public int hashCode() {
        int result = this.writeResult.hashCode();
        result = 31 * result + this.errors.hashCode();
        result = 31 * result + this.serverAddress.hashCode();
        result = 31 * result + (this.writeConcernError != null ? this.writeConcernError.hashCode() : 0);
        return result;
    }
}

