/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.annotations.Immutable;
import com.mongodb.assertions.Assertions;
import com.mongodb.lang.Nullable;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;

@Immutable
public class WriteConcern
implements Serializable {
    private static final long serialVersionUID = 1884671104750417011L;
    private static final Map<String, WriteConcern> NAMED_CONCERNS;
    private final Object w;
    private final Integer wTimeoutMS;
    private final Boolean fsync;
    private final Boolean journal;
    public static final WriteConcern ACKNOWLEDGED;
    public static final WriteConcern W1;
    public static final WriteConcern W2;
    public static final WriteConcern W3;
    public static final WriteConcern UNACKNOWLEDGED;
    @Deprecated
    public static final WriteConcern FSYNCED;
    public static final WriteConcern JOURNALED;
    @Deprecated
    public static final WriteConcern REPLICA_ACKNOWLEDGED;
    @Deprecated
    public static final WriteConcern NORMAL;
    @Deprecated
    public static final WriteConcern SAFE;
    public static final WriteConcern MAJORITY;
    @Deprecated
    public static final WriteConcern FSYNC_SAFE;
    @Deprecated
    public static final WriteConcern JOURNAL_SAFE;
    @Deprecated
    public static final WriteConcern REPLICAS_SAFE;

    @Deprecated
    public WriteConcern() {
        this(0);
    }

    public WriteConcern(int w2) {
        this((Object)w2, null, null, null);
    }

    public WriteConcern(String w2) {
        this((Object)w2, null, null, null);
        Assertions.notNull("w", w2);
    }

    public WriteConcern(int w2, int wTimeoutMS) {
        this((Object)w2, (Integer)wTimeoutMS, null, null);
    }

    @Deprecated
    public WriteConcern(boolean fsync) {
        this(null, null, (Boolean)fsync, null);
    }

    @Deprecated
    public WriteConcern(int w2, int wTimeoutMS, boolean fsync) {
        this((Object)w2, (Integer)wTimeoutMS, (Boolean)fsync, null);
    }

    @Deprecated
    public WriteConcern(int w2, int wTimeoutMS, boolean fsync, boolean journal) {
        this((Object)w2, (Integer)wTimeoutMS, (Boolean)fsync, (Boolean)journal);
    }

    @Deprecated
    public WriteConcern(String w2, int wTimeoutMS, boolean fsync, boolean journal) {
        this((Object)Assertions.notNull("w", w2), (Integer)wTimeoutMS, (Boolean)fsync, (Boolean)journal);
    }

    private WriteConcern(@Nullable Object w2, @Nullable Integer wTimeoutMS, @Nullable Boolean fsync, @Nullable Boolean journal) {
        if (w2 instanceof Integer) {
            Assertions.isTrueArgument("w >= 0", (Integer)w2 >= 0);
            if ((Integer)w2 == 0) {
                Assertions.isTrueArgument("fsync is false when w is 0", fsync == null || fsync == false);
                Assertions.isTrueArgument("journal is false when w is 0", journal == null || journal == false);
            }
        } else if (w2 != null) {
            Assertions.isTrueArgument("w must be String or int", w2 instanceof String);
        }
        Assertions.isTrueArgument("wtimeout >= 0", wTimeoutMS == null || wTimeoutMS >= 0);
        this.w = w2;
        this.wTimeoutMS = wTimeoutMS;
        this.fsync = fsync;
        this.journal = journal;
    }

    public Object getWObject() {
        return this.w;
    }

    public int getW() {
        Assertions.isTrue("w is an Integer", this.w != null && this.w instanceof Integer);
        return (Integer)this.w;
    }

    public String getWString() {
        Assertions.isTrue("w is a String", this.w != null && this.w instanceof String);
        return (String)this.w;
    }

    @Nullable
    public Integer getWTimeout(TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        return this.wTimeoutMS == null ? null : Integer.valueOf((int)timeUnit.convert(this.wTimeoutMS.intValue(), TimeUnit.MILLISECONDS));
    }

    @Deprecated
    public int getWtimeout() {
        return this.wTimeoutMS == null ? 0 : this.wTimeoutMS;
    }

    public Boolean getJournal() {
        return this.journal;
    }

    @Deprecated
    public boolean getJ() {
        return this.journal == null ? false : this.journal;
    }

    @Deprecated
    public boolean getFsync() {
        return this.fsync == null ? false : this.fsync;
    }

    @Deprecated
    public boolean fsync() {
        return this.getFsync();
    }

    @Deprecated
    public boolean callGetLastError() {
        return this.isAcknowledged();
    }

    public boolean isServerDefault() {
        return this.equals(ACKNOWLEDGED);
    }

    public BsonDocument asDocument() {
        BsonDocument document = new BsonDocument();
        this.addW(document);
        this.addWTimeout(document);
        this.addFSync(document);
        this.addJ(document);
        return document;
    }

    public boolean isAcknowledged() {
        if (this.w instanceof Integer) {
            return (Integer)this.w > 0 || this.journal != null && this.journal != false || this.fsync != null && this.fsync != false;
        }
        return true;
    }

    public static WriteConcern valueOf(String name) {
        return NAMED_CONCERNS.get(name.toLowerCase());
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        WriteConcern that = (WriteConcern)o2;
        if (this.w != null ? !this.w.equals(that.w) : that.w != null) {
            return false;
        }
        if (this.wTimeoutMS != null ? !this.wTimeoutMS.equals(that.wTimeoutMS) : that.wTimeoutMS != null) {
            return false;
        }
        if (this.fsync != null ? !this.fsync.equals(that.fsync) : that.fsync != null) {
            return false;
        }
        return !(this.journal != null ? !this.journal.equals(that.journal) : that.journal != null);
    }

    public int hashCode() {
        int result = this.w != null ? this.w.hashCode() : 0;
        result = 31 * result + (this.wTimeoutMS != null ? this.wTimeoutMS.hashCode() : 0);
        result = 31 * result + (this.fsync != null ? this.fsync.hashCode() : 0);
        result = 31 * result + (this.journal != null ? this.journal.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "WriteConcern{w=" + this.w + ", wTimeout=" + this.wTimeoutMS + " ms, fsync=" + this.fsync + ", journal=" + this.journal;
    }

    public WriteConcern withW(int w2) {
        return new WriteConcern((Object)w2, this.wTimeoutMS, this.fsync, this.journal);
    }

    public WriteConcern withW(String w2) {
        Assertions.notNull("w", w2);
        return new WriteConcern((Object)w2, this.wTimeoutMS, this.fsync, this.journal);
    }

    @Deprecated
    public WriteConcern withFsync(boolean fsync) {
        return new WriteConcern(this.w, this.wTimeoutMS, (Boolean)fsync, this.journal);
    }

    public WriteConcern withJournal(Boolean journal) {
        return new WriteConcern(this.w, this.wTimeoutMS, this.fsync, journal);
    }

    @Deprecated
    public WriteConcern withJ(boolean journal) {
        return this.withJournal(journal);
    }

    public WriteConcern withWTimeout(long wTimeout, TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        long newWTimeOutMS = TimeUnit.MILLISECONDS.convert(wTimeout, timeUnit);
        Assertions.isTrueArgument("wTimeout >= 0", wTimeout >= 0L);
        Assertions.isTrueArgument("wTimeout <= 2147483647 ms", newWTimeOutMS <= Integer.MAX_VALUE);
        return new WriteConcern(this.w, (Integer)((int)newWTimeOutMS), this.fsync, this.journal);
    }

    private void addW(BsonDocument document) {
        if (this.w instanceof String) {
            document.put("w", new BsonString((String)this.w));
        } else if (this.w instanceof Integer) {
            document.put("w", new BsonInt32((Integer)this.w));
        }
    }

    private void addJ(BsonDocument document) {
        if (this.journal != null) {
            document.put("j", BsonBoolean.valueOf(this.journal));
        }
    }

    private void addFSync(BsonDocument document) {
        if (this.fsync != null) {
            document.put("fsync", BsonBoolean.valueOf(this.fsync));
        }
    }

    private void addWTimeout(BsonDocument document) {
        if (this.wTimeoutMS != null) {
            document.put("wtimeout", new BsonInt32(this.wTimeoutMS));
        }
    }

    @Deprecated
    public static Majority majorityWriteConcern(int wtimeout, boolean fsync, boolean j2) {
        return new Majority(wtimeout, fsync, j2);
    }

    static {
        ACKNOWLEDGED = new WriteConcern(null, null, null, null);
        W1 = new WriteConcern(1);
        W2 = new WriteConcern(2);
        W3 = new WriteConcern(3);
        UNACKNOWLEDGED = new WriteConcern(0);
        FSYNCED = ACKNOWLEDGED.withFsync(true);
        JOURNALED = ACKNOWLEDGED.withJournal(true);
        REPLICA_ACKNOWLEDGED = new WriteConcern(2);
        NORMAL = UNACKNOWLEDGED;
        SAFE = ACKNOWLEDGED;
        MAJORITY = new WriteConcern("majority");
        FSYNC_SAFE = FSYNCED;
        JOURNAL_SAFE = JOURNALED;
        REPLICAS_SAFE = REPLICA_ACKNOWLEDGED;
        NAMED_CONCERNS = new HashMap<String, WriteConcern>();
        for (Field f2 : WriteConcern.class.getFields()) {
            if (!Modifier.isStatic(f2.getModifiers()) || !f2.getType().equals(WriteConcern.class)) continue;
            String key = f2.getName().toLowerCase();
            try {
                NAMED_CONCERNS.put(key, (WriteConcern)f2.get(null));
            } catch (IllegalAccessException e2) {
                throw new RuntimeException(e2);
            }
        }
    }

    @Deprecated
    public static class Majority
    extends WriteConcern {
        private static final long serialVersionUID = -4128295115883875212L;

        public Majority() {
            this(0, false, false);
        }

        public Majority(int wtimeout, boolean fsync, boolean j2) {
            super("majority", wtimeout, fsync, j2);
        }
    }
}

