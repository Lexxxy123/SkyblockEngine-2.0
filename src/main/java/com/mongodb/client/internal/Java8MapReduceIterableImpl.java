/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.internal;

import com.mongodb.MongoNamespace;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.internal.Java8ForEachHelper;
import com.mongodb.client.internal.MapReduceIterableImpl;
import com.mongodb.client.internal.OperationExecutor;
import com.mongodb.lang.Nullable;
import java.util.function.Consumer;
import org.bson.codecs.configuration.CodecRegistry;

class Java8MapReduceIterableImpl<TDocument, TResult>
extends MapReduceIterableImpl<TDocument, TResult> {
    Java8MapReduceIterableImpl(@Nullable ClientSession clientSession, MongoNamespace namespace, Class<TDocument> documentClass, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, WriteConcern writeConcern, OperationExecutor executor, String mapFunction, String reduceFunction) {
        super(clientSession, namespace, documentClass, resultClass, codecRegistry, readPreference, readConcern, writeConcern, executor, mapFunction, reduceFunction);
    }

    @Override
    public void forEach(Consumer<? super TResult> action) {
        Java8ForEachHelper.forEach(this, action);
    }
}

