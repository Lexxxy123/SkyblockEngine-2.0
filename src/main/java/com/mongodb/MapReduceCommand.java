/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoInternalException;
import com.mongodb.ReadPreference;
import com.mongodb.client.model.Collation;
import com.mongodb.lang.Nullable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MapReduceCommand {
    private final String mapReduce;
    private final String map;
    private final String reduce;
    private String finalize;
    private ReadPreference readPreference;
    private final OutputType outputType;
    private final String outputCollection;
    private String outputDB;
    private final DBObject query;
    private DBObject sort;
    private int limit;
    private long maxTimeMS;
    private Map<String, Object> scope;
    private Boolean jsMode;
    private Boolean verbose;
    private Boolean bypassDocumentValidation;
    private Collation collation;

    public MapReduceCommand(DBCollection inputCollection, String map, String reduce, @Nullable String outputCollection, OutputType type, DBObject query) {
        this.mapReduce = inputCollection.getName();
        this.map = map;
        this.reduce = reduce;
        this.outputCollection = outputCollection;
        this.outputType = type;
        this.query = query;
        this.outputDB = null;
        this.verbose = true;
    }

    public void setVerbose(Boolean verbose) {
        this.verbose = verbose;
    }

    public Boolean isVerbose() {
        return this.verbose;
    }

    public String getInput() {
        return this.mapReduce;
    }

    public String getMap() {
        return this.map;
    }

    public String getReduce() {
        return this.reduce;
    }

    @Nullable
    public String getOutputTarget() {
        return this.outputCollection;
    }

    public OutputType getOutputType() {
        return this.outputType;
    }

    @Nullable
    public String getFinalize() {
        return this.finalize;
    }

    public void setFinalize(@Nullable String finalize) {
        this.finalize = finalize;
    }

    @Nullable
    public DBObject getQuery() {
        return this.query;
    }

    @Nullable
    public DBObject getSort() {
        return this.sort;
    }

    public void setSort(@Nullable DBObject sort) {
        this.sort = sort;
    }

    public int getLimit() {
        return this.limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public long getMaxTime(TimeUnit timeUnit) {
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }

    public void setMaxTime(long maxTime, TimeUnit timeUnit) {
        this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
    }

    @Nullable
    public Map<String, Object> getScope() {
        return this.scope;
    }

    public void setScope(@Nullable Map<String, Object> scope) {
        this.scope = scope;
    }

    @Nullable
    public Boolean getJsMode() {
        return this.jsMode;
    }

    public void setJsMode(@Nullable Boolean jsMode) {
        this.jsMode = jsMode;
    }

    @Nullable
    public String getOutputDB() {
        return this.outputDB;
    }

    public void setOutputDB(@Nullable String outputDB) {
        this.outputDB = outputDB;
    }

    @Nullable
    public Boolean getBypassDocumentValidation() {
        return this.bypassDocumentValidation;
    }

    public void setBypassDocumentValidation(@Nullable Boolean bypassDocumentValidation) {
        this.bypassDocumentValidation = bypassDocumentValidation;
    }

    public DBObject toDBObject() {
        BasicDBObject cmd = new BasicDBObject();
        cmd.put("mapreduce", this.mapReduce);
        cmd.put("map", this.map);
        cmd.put("reduce", this.reduce);
        if (this.verbose != null) {
            cmd.put("verbose", this.verbose);
        }
        BasicDBObject out = new BasicDBObject();
        switch (this.outputType) {
            case INLINE: {
                out.put("inline", 1);
                break;
            }
            case REPLACE: {
                out.put("replace", this.outputCollection);
                break;
            }
            case MERGE: {
                out.put("merge", this.outputCollection);
                break;
            }
            case REDUCE: {
                out.put("reduce", this.outputCollection);
                break;
            }
            default: {
                throw new IllegalArgumentException("Unexpected output type");
            }
        }
        if (this.outputDB != null) {
            out.put("db", this.outputDB);
        }
        cmd.put("out", out);
        if (this.query != null) {
            cmd.put("query", this.query);
        }
        if (this.finalize != null) {
            cmd.put("finalize", this.finalize);
        }
        if (this.sort != null) {
            cmd.put("sort", this.sort);
        }
        if (this.limit > 0) {
            cmd.put("limit", this.limit);
        }
        if (this.scope != null) {
            cmd.put("scope", this.scope);
        }
        if (this.jsMode != null) {
            cmd.put("jsMode", this.jsMode);
        }
        if (this.maxTimeMS != 0L) {
            cmd.put("maxTimeMS", this.maxTimeMS);
        }
        return cmd;
    }

    public void setReadPreference(@Nullable ReadPreference preference) {
        this.readPreference = preference;
    }

    @Nullable
    public ReadPreference getReadPreference() {
        return this.readPreference;
    }

    @Nullable
    public Collation getCollation() {
        return this.collation;
    }

    @Nullable
    public void setCollation(Collation collation) {
        this.collation = collation;
    }

    public String toString() {
        return this.toDBObject().toString();
    }

    String getOutputTargetNonNull() {
        if (this.outputCollection == null) {
            throw new MongoInternalException("outputCollection can not be null in this context");
        }
        return this.outputCollection;
    }

    public static enum OutputType {
        REPLACE,
        MERGE,
        REDUCE,
        INLINE;

    }
}

