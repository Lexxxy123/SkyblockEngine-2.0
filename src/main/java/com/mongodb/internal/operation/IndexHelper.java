/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.operation;

import com.mongodb.client.model.IndexModel;
import java.util.ArrayList;
import java.util.List;
import org.bson.BsonDocument;
import org.bson.BsonNumber;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.codecs.configuration.CodecRegistry;

public final class IndexHelper {
    public static List<String> getIndexNames(List<IndexModel> indexes, CodecRegistry codecRegistry) {
        ArrayList<String> indexNames = new ArrayList<String>(indexes.size());
        for (IndexModel index : indexes) {
            String name = index.getOptions().getName();
            if (name != null) {
                indexNames.add(name);
                continue;
            }
            indexNames.add(IndexHelper.generateIndexName(index.getKeys().toBsonDocument(BsonDocument.class, codecRegistry)));
        }
        return indexNames;
    }

    public static String generateIndexName(BsonDocument index) {
        StringBuilder indexName = new StringBuilder();
        for (String keyNames : index.keySet()) {
            if (indexName.length() != 0) {
                indexName.append('_');
            }
            indexName.append(keyNames).append('_');
            BsonValue ascOrDescValue = index.get(keyNames);
            if (ascOrDescValue instanceof BsonNumber) {
                indexName.append(((BsonNumber)ascOrDescValue).intValue());
                continue;
            }
            if (!(ascOrDescValue instanceof BsonString)) continue;
            indexName.append(((BsonString)ascOrDescValue).getValue().replace(' ', '_'));
        }
        return indexName.toString();
    }

    private IndexHelper() {
    }
}

