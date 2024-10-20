/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.MongoChangeStreamException;
import com.mongodb.MongoCursorNotFoundException;
import com.mongodb.MongoException;
import com.mongodb.MongoInterruptedException;
import com.mongodb.MongoNotPrimaryException;
import com.mongodb.MongoSocketException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

final class ChangeStreamBatchCursorHelper {
    private static final List<Integer> UNRETRYABLE_SERVER_ERROR_CODES = Arrays.asList(136, 237, 280, 11601);
    private static final List<String> NONRESUMABLE_CHANGE_STREAM_ERROR_LABELS = Arrays.asList("NonResumableChangeStreamError");

    static boolean isRetryableError(Throwable t2) {
        if (!(t2 instanceof MongoException) || t2 instanceof MongoChangeStreamException || t2 instanceof MongoInterruptedException) {
            return false;
        }
        if (t2 instanceof MongoNotPrimaryException || t2 instanceof MongoCursorNotFoundException || t2 instanceof MongoSocketException) {
            return true;
        }
        return !UNRETRYABLE_SERVER_ERROR_CODES.contains(((MongoException)t2).getCode()) && Collections.disjoint(NONRESUMABLE_CHANGE_STREAM_ERROR_LABELS, ((MongoException)t2).getErrorLabels());
    }

    private ChangeStreamBatchCursorHelper() {
    }
}

