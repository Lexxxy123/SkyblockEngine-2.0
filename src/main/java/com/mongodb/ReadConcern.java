/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.ReadConcernLevel;
import com.mongodb.assertions.Assertions;
import com.mongodb.lang.Nullable;
import org.bson.BsonDocument;
import org.bson.BsonString;

public final class ReadConcern {
    private final ReadConcernLevel level;
    public static final ReadConcern DEFAULT = new ReadConcern();
    public static final ReadConcern LOCAL = new ReadConcern(ReadConcernLevel.LOCAL);
    public static final ReadConcern MAJORITY = new ReadConcern(ReadConcernLevel.MAJORITY);
    public static final ReadConcern LINEARIZABLE = new ReadConcern(ReadConcernLevel.LINEARIZABLE);
    public static final ReadConcern SNAPSHOT = new ReadConcern(ReadConcernLevel.SNAPSHOT);
    public static final ReadConcern AVAILABLE = new ReadConcern(ReadConcernLevel.AVAILABLE);

    public ReadConcern(ReadConcernLevel level) {
        this.level = Assertions.notNull("level", level);
    }

    @Nullable
    public ReadConcernLevel getLevel() {
        return this.level;
    }

    public boolean isServerDefault() {
        return this.level == null;
    }

    public BsonDocument asDocument() {
        BsonDocument readConcern = new BsonDocument();
        if (this.level != null) {
            readConcern.put("level", new BsonString(this.level.getValue()));
        }
        return readConcern;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        ReadConcern that = (ReadConcern)o2;
        return this.level == that.level;
    }

    public int hashCode() {
        return this.level != null ? this.level.hashCode() : 0;
    }

    private ReadConcern() {
        this.level = null;
    }
}

