/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.internal;

import com.mongodb.MongoNamespace;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.internal.AggregateIterableImpl;
import com.mongodb.client.internal.Java8ForEachHelper;
import com.mongodb.client.internal.OperationExecutor;
import com.mongodb.client.model.AggregationLevel;
import com.mongodb.lang.Nullable;
import java.util.List;
import java.util.function.Consumer;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

class Java8AggregateIterableImpl<TDocument, TResult>
extends AggregateIterableImpl<TDocument, TResult> {
    Java8AggregateIterableImpl(@Nullable ClientSession clientSession, String databaseName, Class<TDocument> documentClass, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, WriteConcern writeConcern, OperationExecutor executor, List<? extends Bson> pipeline, AggregationLevel aggregationLevel, boolean retryReads) {
        super(clientSession, databaseName, documentClass, resultClass, codecRegistry, readPreference, readConcern, writeConcern, executor, pipeline, aggregationLevel, retryReads);
    }

    Java8AggregateIterableImpl(@Nullable ClientSession clientSession, MongoNamespace namespace, Class<TDocument> documentClass, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, WriteConcern writeConcern, OperationExecutor executor, List<? extends Bson> pipeline, AggregationLevel aggregationLevel, boolean retryReads) {
        super(clientSession, namespace, documentClass, resultClass, codecRegistry, readPreference, readConcern, writeConcern, executor, pipeline, aggregationLevel, retryReads);
    }

    @Override
    public void forEach(Consumer<? super TResult> action) {
        Java8ForEachHelper.forEach(this, action);
    }
}

