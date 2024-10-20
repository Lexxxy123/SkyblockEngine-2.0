/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.operation.BsonArrayWrapper;
import java.util.List;
import org.bson.BsonDocument;
import org.bson.BsonDocumentWrapper;

final class BsonDocumentWrapperHelper {
    static <T> List<T> toList(BsonDocument result, String fieldContainingWrappedArray) {
        return ((BsonArrayWrapper)result.getArray(fieldContainingWrappedArray)).getWrappedArray();
    }

    static <T> T toDocument(BsonDocument document) {
        if (document == null) {
            return null;
        }
        return ((BsonDocumentWrapper)document).getWrappedDocument();
    }

    private BsonDocumentWrapperHelper() {
    }
}

