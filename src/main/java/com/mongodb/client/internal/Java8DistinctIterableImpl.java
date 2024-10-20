/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.internal;

import com.mongodb.MongoNamespace;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.client.ClientSession;
import com.mongodb.client.internal.DistinctIterableImpl;
import com.mongodb.client.internal.Java8ForEachHelper;
import com.mongodb.client.internal.OperationExecutor;
import com.mongodb.lang.Nullable;
import java.util.function.Consumer;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

class Java8DistinctIterableImpl<TDocument, TResult>
extends DistinctIterableImpl<TDocument, TResult> {
    Java8DistinctIterableImpl(@Nullable ClientSession clientSession, MongoNamespace namespace, Class<TDocument> documentClass, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, OperationExecutor executor, String fieldName, Bson filter, boolean retryReads) {
        super(clientSession, namespace, documentClass, resultClass, codecRegistry, readPreference, readConcern, executor, fieldName, filter, retryReads);
    }

    @Override
    public void forEach(Consumer<? super TResult> action) {
        Java8ForEachHelper.forEach(this, action);
    }
}

