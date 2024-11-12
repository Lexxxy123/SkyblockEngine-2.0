/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.connection;

import com.mongodb.MongoBulkWriteException;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.assertions.Assertions;
import com.mongodb.bulk.BulkWriteError;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.bulk.BulkWriteUpsert;
import com.mongodb.bulk.WriteConcernError;
import com.mongodb.internal.connection.IndexMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Deprecated
public class BulkWriteBatchCombiner {
    private final ServerAddress serverAddress;
    private final boolean ordered;
    private final WriteConcern writeConcern;
    private int insertedCount;
    private int matchedCount;
    private int deletedCount;
    private int modifiedCount = 0;
    private final Set<BulkWriteUpsert> writeUpserts = new TreeSet<BulkWriteUpsert>(new Comparator<BulkWriteUpsert>(){

        @Override
        public int compare(BulkWriteUpsert o1, BulkWriteUpsert o2) {
            return o1.getIndex() < o2.getIndex() ? -1 : (o1.getIndex() == o2.getIndex() ? 0 : 1);
        }
    });
    private final Set<BulkWriteError> writeErrors = new TreeSet<BulkWriteError>(new Comparator<BulkWriteError>(){

        @Override
        public int compare(BulkWriteError o1, BulkWriteError o2) {
            return o1.getIndex() < o2.getIndex() ? -1 : (o1.getIndex() == o2.getIndex() ? 0 : 1);
        }
    });
    private final List<WriteConcernError> writeConcernErrors = new ArrayList<WriteConcernError>();

    public BulkWriteBatchCombiner(ServerAddress serverAddress, boolean ordered, WriteConcern writeConcern) {
        this.writeConcern = Assertions.notNull("writeConcern", writeConcern);
        this.ordered = ordered;
        this.serverAddress = Assertions.notNull("serverAddress", serverAddress);
    }

    public void addResult(BulkWriteResult result, IndexMap indexMap) {
        this.insertedCount += result.getInsertedCount();
        this.matchedCount += result.getMatchedCount();
        this.deletedCount += result.getDeletedCount();
        this.modifiedCount += result.getModifiedCount();
        this.mergeUpserts(result.getUpserts(), indexMap);
    }

    public void addErrorResult(MongoBulkWriteException exception, IndexMap indexMap) {
        this.addResult(exception.getWriteResult(), indexMap);
        this.mergeWriteErrors(exception.getWriteErrors(), indexMap);
        this.mergeWriteConcernError(exception.getWriteConcernError());
    }

    public void addWriteErrorResult(BulkWriteError writeError, IndexMap indexMap) {
        Assertions.notNull("writeError", writeError);
        this.mergeWriteErrors(Arrays.asList(writeError), indexMap);
    }

    public void addWriteConcernErrorResult(WriteConcernError writeConcernError) {
        Assertions.notNull("writeConcernError", writeConcernError);
        this.mergeWriteConcernError(writeConcernError);
    }

    public void addErrorResult(List<BulkWriteError> writeErrors, WriteConcernError writeConcernError, IndexMap indexMap) {
        this.mergeWriteErrors(writeErrors, indexMap);
        this.mergeWriteConcernError(writeConcernError);
    }

    public BulkWriteResult getResult() {
        this.throwOnError();
        return this.createResult();
    }

    public boolean shouldStopSendingMoreBatches() {
        return this.ordered && this.hasWriteErrors();
    }

    public boolean hasErrors() {
        return this.hasWriteErrors() || this.hasWriteConcernErrors();
    }

    public MongoBulkWriteException getError() {
        return this.hasErrors() ? new MongoBulkWriteException(this.createResult(), new ArrayList<BulkWriteError>(this.writeErrors), this.writeConcernErrors.isEmpty() ? null : this.writeConcernErrors.get(this.writeConcernErrors.size() - 1), this.serverAddress) : null;
    }

    private void mergeWriteConcernError(WriteConcernError writeConcernError) {
        if (writeConcernError != null) {
            if (this.writeConcernErrors.isEmpty()) {
                this.writeConcernErrors.add(writeConcernError);
            } else if (!writeConcernError.equals(this.writeConcernErrors.get(this.writeConcernErrors.size() - 1))) {
                this.writeConcernErrors.add(writeConcernError);
            }
        }
    }

    private void mergeWriteErrors(List<BulkWriteError> newWriteErrors, IndexMap indexMap) {
        for (BulkWriteError cur : newWriteErrors) {
            this.writeErrors.add(new BulkWriteError(cur.getCode(), cur.getMessage(), cur.getDetails(), indexMap.map(cur.getIndex())));
        }
    }

    private void mergeUpserts(List<BulkWriteUpsert> upserts, IndexMap indexMap) {
        for (BulkWriteUpsert bulkWriteUpsert : upserts) {
            this.writeUpserts.add(new BulkWriteUpsert(indexMap.map(bulkWriteUpsert.getIndex()), bulkWriteUpsert.getId()));
        }
    }

    private void throwOnError() {
        if (this.hasErrors()) {
            throw this.getError();
        }
    }

    private BulkWriteResult createResult() {
        return this.writeConcern.isAcknowledged() ? BulkWriteResult.acknowledged(this.insertedCount, this.matchedCount, this.deletedCount, this.modifiedCount, new ArrayList<BulkWriteUpsert>(this.writeUpserts)) : BulkWriteResult.unacknowledged();
    }

    private boolean hasWriteErrors() {
        return !this.writeErrors.isEmpty();
    }

    private boolean hasWriteConcernErrors() {
        return !this.writeConcernErrors.isEmpty();
    }
}

