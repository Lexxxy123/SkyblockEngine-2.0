/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.Function;
import com.mongodb.MongoException;
import com.mongodb.MongoExecutionTimeoutException;
import com.mongodb.MongoNodeIsRecoveringException;
import com.mongodb.MongoNotPrimaryException;
import com.mongodb.MongoSocketException;
import com.mongodb.MongoTimeoutException;
import com.mongodb.MongoWriteConcernException;
import com.mongodb.WriteConcern;
import com.mongodb.assertions.Assertions;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.binding.AsyncWriteBinding;
import com.mongodb.binding.WriteBinding;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.connection.ServerDescription;
import com.mongodb.lang.Nullable;
import com.mongodb.operation.CommandOperationHelper;
import com.mongodb.operation.TransactionOperation;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonInt64;

@Deprecated
public class CommitTransactionOperation
extends TransactionOperation {
    private final boolean alreadyCommitted;
    private BsonDocument recoveryToken;
    private Long maxCommitTimeMS;
    private static final List<Integer> NON_RETRYABLE_WRITE_CONCERN_ERROR_CODES = Arrays.asList(79, 100);

    public CommitTransactionOperation(WriteConcern writeConcern) {
        this(writeConcern, false);
    }

    public CommitTransactionOperation(WriteConcern writeConcern, boolean alreadyCommitted) {
        super(writeConcern);
        this.alreadyCommitted = alreadyCommitted;
    }

    public CommitTransactionOperation recoveryToken(BsonDocument recoveryToken) {
        this.recoveryToken = recoveryToken;
        return this;
    }

    public CommitTransactionOperation maxCommitTime(@Nullable Long maxCommitTime, TimeUnit timeUnit) {
        if (maxCommitTime == null) {
            this.maxCommitTimeMS = null;
        } else {
            Assertions.notNull("timeUnit", timeUnit);
            Assertions.isTrueArgument("maxCommitTime > 0", maxCommitTime > 0L);
            this.maxCommitTimeMS = TimeUnit.MILLISECONDS.convert(maxCommitTime, timeUnit);
        }
        return this;
    }

    @Nullable
    public Long getMaxCommitTime(TimeUnit timeUnit) {
        Assertions.notNull("timeUnit", timeUnit);
        if (this.maxCommitTimeMS == null) {
            return null;
        }
        return timeUnit.convert(this.maxCommitTimeMS, TimeUnit.MILLISECONDS);
    }

    @Override
    public Void execute(WriteBinding binding) {
        try {
            return super.execute(binding);
        } catch (MongoException e2) {
            this.addErrorLabels(e2);
            throw e2;
        }
    }

    @Override
    public void executeAsync(AsyncWriteBinding binding, final SingleResultCallback<Void> callback) {
        super.executeAsync(binding, new SingleResultCallback<Void>(){

            @Override
            public void onResult(Void result, Throwable t2) {
                if (t2 instanceof MongoException) {
                    CommitTransactionOperation.this.addErrorLabels((MongoException)t2);
                }
                callback.onResult(result, t2);
            }
        });
    }

    private void addErrorLabels(MongoException e2) {
        if (CommitTransactionOperation.shouldAddUnknownTransactionCommitResultLabel(e2)) {
            e2.addLabel("UnknownTransactionCommitResult");
        }
    }

    private static boolean shouldAddUnknownTransactionCommitResultLabel(Throwable t2) {
        if (!(t2 instanceof MongoException)) {
            return false;
        }
        MongoException e2 = (MongoException)t2;
        if (e2 instanceof MongoSocketException || e2 instanceof MongoTimeoutException || e2 instanceof MongoNotPrimaryException || e2 instanceof MongoNodeIsRecoveringException || e2 instanceof MongoExecutionTimeoutException) {
            return true;
        }
        if (e2 instanceof MongoWriteConcernException) {
            return !NON_RETRYABLE_WRITE_CONCERN_ERROR_CODES.contains(e2.getCode());
        }
        return false;
    }

    @Override
    protected String getCommandName() {
        return "commitTransaction";
    }

    @Override
    CommandOperationHelper.CommandCreator getCommandCreator() {
        final CommandOperationHelper.CommandCreator creator = new CommandOperationHelper.CommandCreator(){

            @Override
            public BsonDocument create(ServerDescription serverDescription, ConnectionDescription connectionDescription) {
                BsonDocument command = CommitTransactionOperation.super.getCommandCreator().create(serverDescription, connectionDescription);
                if (CommitTransactionOperation.this.maxCommitTimeMS != null) {
                    command.append("maxTimeMS", CommitTransactionOperation.this.maxCommitTimeMS > Integer.MAX_VALUE ? new BsonInt64(CommitTransactionOperation.this.maxCommitTimeMS) : new BsonInt32(CommitTransactionOperation.this.maxCommitTimeMS.intValue()));
                }
                return command;
            }
        };
        if (this.alreadyCommitted) {
            return new CommandOperationHelper.CommandCreator(){

                @Override
                public BsonDocument create(ServerDescription serverDescription, ConnectionDescription connectionDescription) {
                    return CommitTransactionOperation.this.getRetryCommandModifier().apply(creator.create(serverDescription, connectionDescription));
                }
            };
        }
        if (this.recoveryToken != null) {
            return new CommandOperationHelper.CommandCreator(){

                @Override
                public BsonDocument create(ServerDescription serverDescription, ConnectionDescription connectionDescription) {
                    return creator.create(serverDescription, connectionDescription).append("recoveryToken", CommitTransactionOperation.this.recoveryToken);
                }
            };
        }
        return creator;
    }

    @Override
    protected Function<BsonDocument, BsonDocument> getRetryCommandModifier() {
        return new Function<BsonDocument, BsonDocument>(){

            @Override
            public BsonDocument apply(BsonDocument command) {
                WriteConcern retryWriteConcern = CommitTransactionOperation.this.getWriteConcern().withW("majority");
                if (retryWriteConcern.getWTimeout(TimeUnit.MILLISECONDS) == null) {
                    retryWriteConcern = retryWriteConcern.withWTimeout(10000L, TimeUnit.MILLISECONDS);
                }
                command.put("writeConcern", retryWriteConcern.asDocument());
                if (CommitTransactionOperation.this.recoveryToken != null) {
                    command.put("recoveryToken", CommitTransactionOperation.this.recoveryToken);
                }
                return command;
            }
        };
    }
}

