/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.CursorType;
import com.mongodb.DBCollection;
import com.mongodb.DBDecoderAdapter;
import com.mongodb.DBDecoderFactory;
import com.mongodb.DBObject;
import com.mongodb.DBObjects;
import com.mongodb.ExplainVerbosity;
import com.mongodb.Mongo;
import com.mongodb.MongoNamespace;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.ServerCursor;
import com.mongodb.annotations.NotThreadSafe;
import com.mongodb.assertions.Assertions;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.internal.MongoBatchCursorAdapter;
import com.mongodb.client.internal.OperationExecutor;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.DBCollectionCountOptions;
import com.mongodb.client.model.DBCollectionFindOptions;
import com.mongodb.lang.Nullable;
import com.mongodb.operation.BatchCursor;
import com.mongodb.operation.FindOperation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.bson.codecs.Decoder;

@NotThreadSafe
public class DBCursor
implements Cursor,
Iterable<DBObject> {
    private final DBCollection collection;
    private final DBObject filter;
    private final DBCollectionFindOptions findOptions;
    private final OperationExecutor executor;
    private final boolean retryReads;
    private int options;
    private DBDecoderFactory decoderFactory;
    private Decoder<DBObject> decoder;
    private IteratorOrArray iteratorOrArray;
    private DBObject currentObject;
    private int numSeen;
    private boolean closed;
    private final List<DBObject> all = new ArrayList<DBObject>();
    private MongoCursor<DBObject> cursor;
    private OptionalFinalizer optionalFinalizer;

    public DBCursor(DBCollection collection, DBObject query, @Nullable DBObject fields, @Nullable ReadPreference readPreference) {
        this(collection, query, fields, readPreference, true);
    }

    public DBCursor(DBCollection collection, DBObject query, @Nullable DBObject fields, @Nullable ReadPreference readPreference, boolean retryReads) {
        this(collection, query, new DBCollectionFindOptions().projection(fields).readPreference(readPreference), retryReads);
        this.addOption(collection.getOptions());
        DBObject indexKeys = DBCursor.lookupSuitableHints(query, collection.getHintFields());
        if (indexKeys != null) {
            this.hint(indexKeys);
        }
    }

    DBCursor(DBCollection collection, @Nullable DBObject filter, DBCollectionFindOptions findOptions) {
        this(collection, filter, findOptions, true);
    }

    DBCursor(DBCollection collection, @Nullable DBObject filter, DBCollectionFindOptions findOptions, boolean retryReads) {
        this(collection, filter, findOptions, collection.getExecutor(), collection.getDBDecoderFactory(), collection.getObjectCodec(), retryReads);
    }

    private DBCursor(DBCollection collection, @Nullable DBObject filter, DBCollectionFindOptions findOptions, OperationExecutor executor, DBDecoderFactory decoderFactory, Decoder<DBObject> decoder, boolean retryReads) {
        this.collection = Assertions.notNull("collection", collection);
        this.filter = filter;
        this.executor = Assertions.notNull("executor", executor);
        this.findOptions = Assertions.notNull("findOptions", findOptions.copy());
        this.decoderFactory = decoderFactory;
        this.decoder = Assertions.notNull("decoder", decoder);
        this.retryReads = retryReads;
    }

    public DBCursor copy() {
        return new DBCursor(this.collection, this.filter, this.findOptions, this.executor, this.decoderFactory, this.decoder, this.retryReads);
    }

    @Override
    public boolean hasNext() {
        if (this.closed) {
            throw new IllegalStateException("Cursor has been closed");
        }
        if (this.cursor == null) {
            FindOperation<DBObject> operation = this.getQueryOperation(this.decoder);
            if (operation.getCursorType() == CursorType.Tailable) {
                operation.cursorType(CursorType.TailableAwait);
            }
            this.initializeCursor(operation);
        }
        boolean hasNext = this.cursor.hasNext();
        this.setServerCursorOnFinalizer(this.cursor.getServerCursor());
        return hasNext;
    }

    @Override
    public DBObject next() {
        this.checkIteratorOrArray(IteratorOrArray.ITERATOR);
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        return this.nextInternal();
    }

    @Nullable
    public DBObject tryNext() {
        if (this.cursor == null) {
            FindOperation<DBObject> operation = this.getQueryOperation(this.decoder);
            if (!operation.getCursorType().isTailable()) {
                throw new IllegalArgumentException("Can only be used with a tailable cursor");
            }
            this.initializeCursor(operation);
        }
        DBObject next = this.cursor.tryNext();
        this.setServerCursorOnFinalizer(this.cursor.getServerCursor());
        return this.currentObject(next);
    }

    public DBObject curr() {
        return this.currentObject;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public DBCursor addOption(int option) {
        this.setOptions(this.options |= option);
        return this;
    }

    @Deprecated
    public DBCursor setOptions(int options) {
        if ((options & 0x40) != 0) {
            throw new UnsupportedOperationException("exhaust query option is not supported");
        }
        this.options = options;
        return this;
    }

    @Deprecated
    public DBCursor resetOptions() {
        this.options = 0;
        return this;
    }

    @Deprecated
    public int getOptions() {
        return this.options;
    }

    public int getLimit() {
        return this.findOptions.getLimit();
    }

    public int getBatchSize() {
        return this.findOptions.getBatchSize();
    }

    @Deprecated
    public DBCursor addSpecial(@Nullable String name, @Nullable Object value) {
        if (name == null || value == null) {
            return this;
        }
        if ("$comment".equals(name)) {
            this.comment(value.toString());
        } else if ("$explain".equals(name)) {
            this.findOptions.getModifiers().put("$explain", true);
        } else if ("$hint".equals(name)) {
            if (value instanceof String) {
                this.hint((String)value);
            } else {
                this.hint((DBObject)value);
            }
        } else if ("$maxScan".equals(name)) {
            this.maxScan(((Number)value).intValue());
        } else if ("$maxTimeMS".equals(name)) {
            this.maxTime(((Number)value).longValue(), TimeUnit.MILLISECONDS);
        } else if ("$max".equals(name)) {
            this.max((DBObject)value);
        } else if ("$min".equals(name)) {
            this.min((DBObject)value);
        } else if ("$orderby".equals(name)) {
            this.sort((DBObject)value);
        } else if ("$returnKey".equals(name)) {
            this.returnKey();
        } else if ("$showDiskLoc".equals(name)) {
            this.showDiskLoc();
        } else if ("$snapshot".equals(name)) {
            this.snapshot();
        } else if ("$natural".equals(name)) {
            this.sort(new BasicDBObject("$natural", (Object)((Number)value).intValue()));
        } else {
            throw new IllegalArgumentException(name + "is not a supported modifier");
        }
        return this;
    }

    public DBCursor comment(String comment) {
        this.findOptions.comment(comment);
        return this;
    }

    @Deprecated
    public DBCursor maxScan(int max) {
        this.findOptions.getModifiers().put("$maxScan", max);
        return this;
    }

    public DBCursor max(DBObject max) {
        this.findOptions.max(max);
        return this;
    }

    public DBCursor min(DBObject min) {
        this.findOptions.min(min);
        return this;
    }

    public DBCursor returnKey() {
        this.findOptions.returnKey(true);
        return this;
    }

    @Deprecated
    public DBCursor showDiskLoc() {
        this.findOptions.getModifiers().put("$showDiskLoc", true);
        return this;
    }

    public DBCursor hint(DBObject indexKeys) {
        this.findOptions.hint(indexKeys);
        return this;
    }

    @Deprecated
    public DBCursor hint(String indexName) {
        this.findOptions.getModifiers().put("$hint", indexName);
        return this;
    }

    public DBCursor maxTime(long maxTime, TimeUnit timeUnit) {
        this.findOptions.maxTime(maxTime, timeUnit);
        return this;
    }

    @Deprecated
    public DBCursor snapshot() {
        this.findOptions.getModifiers().put("$snapshot", true);
        return this;
    }

    @Deprecated
    public DBObject explain() {
        return DBObjects.toDBObject(this.executor.execute(this.getQueryOperation(this.collection.getObjectCodec()).asExplainableOperation(ExplainVerbosity.QUERY_PLANNER), this.getReadPreference(), this.getReadConcern()));
    }

    public DBCursor cursorType(CursorType cursorType) {
        this.findOptions.cursorType(cursorType);
        return this;
    }

    public DBCursor oplogReplay(boolean oplogReplay) {
        this.findOptions.oplogReplay(oplogReplay);
        return this;
    }

    public DBCursor noCursorTimeout(boolean noCursorTimeout) {
        this.findOptions.noCursorTimeout(noCursorTimeout);
        return this;
    }

    public DBCursor partial(boolean partial) {
        this.findOptions.partial(partial);
        return this;
    }

    private FindOperation<DBObject> getQueryOperation(Decoder<DBObject> decoder) {
        FindOperation<DBObject> operation = new FindOperation<DBObject>(this.collection.getNamespace(), decoder).filter(this.collection.wrapAllowNull(this.filter)).batchSize(this.findOptions.getBatchSize()).skip(this.findOptions.getSkip()).limit(this.findOptions.getLimit()).maxAwaitTime(this.findOptions.getMaxAwaitTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).maxTime(this.findOptions.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS).modifiers(this.collection.wrapAllowNull(this.findOptions.getModifiers())).projection(this.collection.wrapAllowNull(this.findOptions.getProjection())).sort(this.collection.wrapAllowNull(this.findOptions.getSort())).collation(this.findOptions.getCollation()).comment(this.findOptions.getComment()).hint(this.collection.wrapAllowNull(this.findOptions.getHint())).min(this.collection.wrapAllowNull(this.findOptions.getMin())).max(this.collection.wrapAllowNull(this.findOptions.getMax())).returnKey(this.findOptions.isReturnKey()).showRecordId(this.findOptions.isShowRecordId()).retryReads(this.retryReads);
        if ((this.options & 2) != 0) {
            if ((this.options & 0x20) != 0) {
                operation.cursorType(CursorType.TailableAwait);
            } else {
                operation.cursorType(CursorType.Tailable);
            }
        } else {
            operation.cursorType(this.findOptions.getCursorType());
        }
        if ((this.options & 8) != 0) {
            operation.oplogReplay(true);
        } else {
            operation.oplogReplay(this.findOptions.isOplogReplay());
        }
        if ((this.options & 0x10) != 0) {
            operation.noCursorTimeout(true);
        } else {
            operation.noCursorTimeout(this.findOptions.isNoCursorTimeout());
        }
        if ((this.options & 0x80) != 0) {
            operation.partial(true);
        } else {
            operation.partial(this.findOptions.isPartial());
        }
        return operation;
    }

    public DBCursor sort(DBObject orderBy) {
        this.findOptions.sort(orderBy);
        return this;
    }

    public DBCursor limit(int limit) {
        this.findOptions.limit(limit);
        return this;
    }

    public DBCursor batchSize(int numberOfElements) {
        this.findOptions.batchSize(numberOfElements);
        return this;
    }

    public DBCursor skip(int numberOfElements) {
        this.findOptions.skip(numberOfElements);
        return this;
    }

    @Override
    public long getCursorId() {
        if (this.cursor != null) {
            ServerCursor serverCursor = this.cursor.getServerCursor();
            if (serverCursor == null) {
                return 0L;
            }
            return serverCursor.getId();
        }
        return 0L;
    }

    public int numSeen() {
        return this.numSeen;
    }

    @Override
    public void close() {
        this.closed = true;
        if (this.cursor != null) {
            this.cursor.close();
            this.cursor = null;
            this.setServerCursorOnFinalizer(null);
        }
        this.currentObject = null;
    }

    @Deprecated
    public DBCursor slaveOk() {
        return this.addOption(4);
    }

    @Override
    public Iterator<DBObject> iterator() {
        return this.copy();
    }

    public List<DBObject> toArray() {
        return this.toArray(Integer.MAX_VALUE);
    }

    public List<DBObject> toArray(int max) {
        this.checkIteratorOrArray(IteratorOrArray.ARRAY);
        this.fillArray(max - 1);
        return this.all;
    }

    public int count() {
        DBCollectionCountOptions countOptions = this.getDbCollectionCountOptions();
        return (int)this.collection.getCount(this.getQuery(), countOptions);
    }

    @Nullable
    public DBObject one() {
        DBCursor findOneCursor = this.copy().limit(-1);
        try {
            DBObject dBObject = findOneCursor.hasNext() ? findOneCursor.next() : null;
            return dBObject;
        } finally {
            findOneCursor.close();
        }
    }

    public int length() {
        this.checkIteratorOrArray(IteratorOrArray.ARRAY);
        this.fillArray(Integer.MAX_VALUE);
        return this.all.size();
    }

    public int itcount() {
        int n2 = 0;
        while (this.hasNext()) {
            this.next();
            ++n2;
        }
        return n2;
    }

    public int size() {
        DBCollectionCountOptions countOptions = this.getDbCollectionCountOptions().skip(this.findOptions.getSkip()).limit(this.findOptions.getLimit());
        return (int)this.collection.getCount(this.getQuery(), countOptions);
    }

    @Nullable
    public DBObject getKeysWanted() {
        return this.findOptions.getProjection();
    }

    public DBObject getQuery() {
        return this.filter;
    }

    public DBCollection getCollection() {
        return this.collection;
    }

    @Override
    @Nullable
    public ServerAddress getServerAddress() {
        if (this.cursor != null) {
            return this.cursor.getServerAddress();
        }
        return null;
    }

    public DBCursor setReadPreference(ReadPreference readPreference) {
        this.findOptions.readPreference(readPreference);
        return this;
    }

    public ReadPreference getReadPreference() {
        ReadPreference readPreference = this.findOptions.getReadPreference();
        if (readPreference != null) {
            return readPreference;
        }
        return this.collection.getReadPreference();
    }

    DBCursor setReadConcern(@Nullable ReadConcern readConcern) {
        this.findOptions.readConcern(readConcern);
        return this;
    }

    ReadConcern getReadConcern() {
        ReadConcern readConcern = this.findOptions.getReadConcern();
        if (readConcern != null) {
            return readConcern;
        }
        return this.collection.getReadConcern();
    }

    @Nullable
    public Collation getCollation() {
        return this.findOptions.getCollation();
    }

    public DBCursor setCollation(@Nullable Collation collation) {
        this.findOptions.collation(collation);
        return this;
    }

    public DBCursor setDecoderFactory(DBDecoderFactory factory) {
        this.decoderFactory = factory;
        this.decoder = new DBDecoderAdapter(factory.create(), this.collection, this.getCollection().getBufferPool());
        return this;
    }

    public DBDecoderFactory getDecoderFactory() {
        return this.decoderFactory;
    }

    public String toString() {
        return "DBCursor{collection=" + this.collection + ", find=" + this.findOptions + (this.cursor != null ? ", cursor=" + this.cursor.getServerCursor() : "") + '}';
    }

    private void initializeCursor(FindOperation<DBObject> operation) {
        this.cursor = new MongoBatchCursorAdapter<DBObject>((BatchCursor)((Object)this.executor.execute(operation, this.getReadPreferenceForCursor(), this.getReadConcern())));
        if (this.isCursorFinalizerEnabled() && this.cursor.getServerCursor() != null) {
            this.optionalFinalizer = new OptionalFinalizer(this.collection.getDB().getMongo(), this.collection.getNamespace());
        }
    }

    private boolean isCursorFinalizerEnabled() {
        return this.collection.getDB().getMongo().getMongoClientOptions().isCursorFinalizerEnabled();
    }

    private void setServerCursorOnFinalizer(@Nullable ServerCursor serverCursor) {
        if (this.optionalFinalizer != null) {
            this.optionalFinalizer.setServerCursor(serverCursor);
        }
    }

    private void checkIteratorOrArray(IteratorOrArray expected) {
        if (this.iteratorOrArray == null) {
            this.iteratorOrArray = expected;
            return;
        }
        if (expected == this.iteratorOrArray) {
            return;
        }
        throw new IllegalArgumentException("Can't switch cursor access methods");
    }

    private ReadPreference getReadPreferenceForCursor() {
        ReadPreference readPreference = this.getReadPreference();
        if ((this.options & 4) != 0 && !readPreference.isSlaveOk()) {
            readPreference = ReadPreference.secondaryPreferred();
        }
        return readPreference;
    }

    private void fillArray(int n2) {
        this.checkIteratorOrArray(IteratorOrArray.ARRAY);
        while (n2 >= this.all.size() && this.hasNext()) {
            this.all.add(this.nextInternal());
        }
    }

    private DBObject nextInternal() {
        if (this.iteratorOrArray == null) {
            this.checkIteratorOrArray(IteratorOrArray.ITERATOR);
        }
        DBObject next = this.cursor.next();
        this.setServerCursorOnFinalizer(this.cursor.getServerCursor());
        return this.currentObjectNonNull(next);
    }

    @Nullable
    private DBObject currentObject(@Nullable DBObject newCurrentObject) {
        if (newCurrentObject != null) {
            this.currentObject = newCurrentObject;
            ++this.numSeen;
            DBObject projection = this.findOptions.getProjection();
            if (projection != null && !projection.keySet().isEmpty()) {
                this.currentObject.markAsPartialObject();
            }
        }
        return newCurrentObject;
    }

    private DBObject currentObjectNonNull(DBObject newCurrentObject) {
        this.currentObject = newCurrentObject;
        ++this.numSeen;
        DBObject projection = this.findOptions.getProjection();
        if (projection != null && !projection.keySet().isEmpty()) {
            this.currentObject.markAsPartialObject();
        }
        return newCurrentObject;
    }

    @Nullable
    private static DBObject lookupSuitableHints(DBObject query, @Nullable List<DBObject> hints) {
        if (hints == null) {
            return null;
        }
        Set<String> keys = query.keySet();
        for (DBObject hint : hints) {
            if (!keys.containsAll(hint.keySet())) continue;
            return hint;
        }
        return null;
    }

    private DBCollectionCountOptions getDbCollectionCountOptions() {
        DBObject hint;
        DBCollectionCountOptions countOptions = new DBCollectionCountOptions().readPreference(this.getReadPreferenceForCursor()).readConcern(this.getReadConcern()).collation(this.getCollation()).maxTime(this.findOptions.getMaxTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
        DBObject dBObject = hint = this.findOptions.getHint() != null ? this.findOptions.getHint() : this.findOptions.getModifiers().get("$hint");
        if (hint != null) {
            if (hint instanceof String) {
                countOptions.hintString((String)((Object)hint));
            } else {
                countOptions.hint(hint);
            }
        }
        return countOptions;
    }

    private static class OptionalFinalizer {
        private final Mongo mongo;
        private final MongoNamespace namespace;
        private volatile ServerCursor serverCursor;

        private OptionalFinalizer(Mongo mongo, MongoNamespace namespace) {
            this.namespace = Assertions.notNull("namespace", namespace);
            this.mongo = Assertions.notNull("mongo", mongo);
        }

        private void setServerCursor(@Nullable ServerCursor serverCursor) {
            this.serverCursor = serverCursor;
        }

        protected void finalize() {
            if (this.serverCursor != null) {
                this.mongo.addOrphanedCursor(this.serverCursor, this.namespace);
            }
        }
    }

    private static enum IteratorOrArray {
        ITERATOR,
        ARRAY;

    }
}

