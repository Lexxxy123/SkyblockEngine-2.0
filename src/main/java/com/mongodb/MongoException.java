/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.assertions.Assertions;
import com.mongodb.lang.Nullable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MongoException
extends RuntimeException {
    public static final String TRANSIENT_TRANSACTION_ERROR_LABEL = "TransientTransactionError";
    public static final String UNKNOWN_TRANSACTION_COMMIT_RESULT_LABEL = "UnknownTransactionCommitResult";
    private static final long serialVersionUID = -4415279469780082174L;
    private final int code;
    private final Set<String> errorLabels = new HashSet<String>();

    @Nullable
    public static MongoException fromThrowable(@Nullable Throwable t2) {
        if (t2 == null) {
            return null;
        }
        return MongoException.fromThrowableNonNull(t2);
    }

    public static MongoException fromThrowableNonNull(Throwable t2) {
        if (t2 instanceof MongoException) {
            return (MongoException)t2;
        }
        return new MongoException(t2.getMessage(), t2);
    }

    public MongoException(String msg) {
        super(msg);
        this.code = -3;
    }

    public MongoException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public MongoException(String msg, Throwable t2) {
        super(msg, t2);
        this.code = -4;
    }

    public MongoException(int code, String msg, Throwable t2) {
        super(msg, t2);
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public void addLabel(String errorLabel) {
        Assertions.notNull("errorLabel", errorLabel);
        this.errorLabels.add(errorLabel);
    }

    public void removeLabel(String errorLabel) {
        Assertions.notNull("errorLabel", errorLabel);
        this.errorLabels.remove(errorLabel);
    }

    public Set<String> getErrorLabels() {
        return Collections.unmodifiableSet(this.errorLabels);
    }

    public boolean hasErrorLabel(String errorLabel) {
        Assertions.notNull("errorLabel", errorLabel);
        return this.errorLabels.contains(errorLabel);
    }
}

