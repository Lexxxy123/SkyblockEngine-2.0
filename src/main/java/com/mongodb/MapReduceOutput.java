/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.lang.Nullable;
import com.mongodb.operation.MapReduceBatchCursor;
import com.mongodb.operation.MapReduceStatistics;
import java.util.ArrayList;
import java.util.List;

public class MapReduceOutput {
    private final DBCollection collection;
    private final DBObject command;
    private final List<DBObject> inlineResults;
    private final MapReduceStatistics mapReduceStatistics;
    private final DBCursor resultsFromCollection;

    MapReduceOutput(DBObject command, MapReduceBatchCursor<DBObject> results) {
        this.command = command;
        this.mapReduceStatistics = results.getStatistics();
        this.collection = null;
        this.resultsFromCollection = null;
        this.inlineResults = new ArrayList<DBObject>();
        while (results.hasNext()) {
            this.inlineResults.addAll(results.next());
        }
        results.close();
    }

    MapReduceOutput(DBObject command, DBCursor resultsFromCollection, MapReduceStatistics mapReduceStatistics, DBCollection outputCollection) {
        this.command = command;
        this.inlineResults = null;
        this.mapReduceStatistics = mapReduceStatistics;
        this.collection = outputCollection;
        this.resultsFromCollection = resultsFromCollection;
    }

    public Iterable<DBObject> results() {
        if (this.inlineResults != null) {
            return this.inlineResults;
        }
        return this.resultsFromCollection;
    }

    public void drop() {
        if (this.collection != null) {
            this.collection.drop();
        }
    }

    public DBCollection getOutputCollection() {
        return this.collection;
    }

    public DBObject getCommand() {
        return this.command;
    }

    public String toString() {
        return "MapReduceOutput{collection=" + this.collection + ", command=" + this.command + ", inlineResults=" + this.inlineResults + ", resultsFromCollection=" + this.resultsFromCollection + '}';
    }

    @Nullable
    public final String getCollectionName() {
        return this.collection == null ? null : this.collection.getName();
    }

    public String getDatabaseName() {
        return this.collection.getDB().getName();
    }

    public int getDuration() {
        return this.mapReduceStatistics.getDuration();
    }

    public int getInputCount() {
        return this.mapReduceStatistics.getInputCount();
    }

    public int getOutputCount() {
        return this.mapReduceStatistics.getOutputCount();
    }

    public int getEmitCount() {
        return this.mapReduceStatistics.getEmitCount();
    }
}

