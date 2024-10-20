/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.operation.MapReduceStatistics;
import org.bson.BsonDocument;
import org.bson.BsonInt32;

final class MapReduceHelper {
    static MapReduceStatistics createStatistics(BsonDocument result) {
        return new MapReduceStatistics(MapReduceHelper.getInputCount(result), MapReduceHelper.getOutputCount(result), MapReduceHelper.getEmitCount(result), MapReduceHelper.getDuration(result));
    }

    private static int getInputCount(BsonDocument result) {
        return result.getDocument("counts", new BsonDocument()).getNumber("input", new BsonInt32(0)).intValue();
    }

    private static int getOutputCount(BsonDocument result) {
        return result.getDocument("counts", new BsonDocument()).getNumber("output", new BsonInt32(0)).intValue();
    }

    private static int getEmitCount(BsonDocument result) {
        return result.getDocument("counts", new BsonDocument()).getNumber("emit", new BsonInt32(0)).intValue();
    }

    private static int getDuration(BsonDocument result) {
        return result.getNumber("timeMillis", new BsonInt32(0)).intValue();
    }

    private MapReduceHelper() {
    }
}

