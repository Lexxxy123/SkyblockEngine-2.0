/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.internal;

import com.mongodb.MongoNamespace;
import com.mongodb.ReadPreference;
import com.mongodb.client.ClientSession;
import com.mongodb.client.internal.Java8ForEachHelper;
import com.mongodb.client.internal.ListIndexesIterableImpl;
import com.mongodb.client.internal.OperationExecutor;
import com.mongodb.lang.Nullable;
import java.util.function.Consumer;
import org.bson.codecs.configuration.CodecRegistry;

class Java8ListIndexesIterableImpl<TResult>
extends ListIndexesIterableImpl<TResult> {
    Java8ListIndexesIterableImpl(@Nullable ClientSession clientSession, MongoNamespace namespace, Class<TResult> resultClass, CodecRegistry codecRegistry, ReadPreference readPreference, OperationExecutor executor, boolean retryReads) {
        super(clientSession, namespace, resultClass, codecRegistry, readPreference, executor, retryReads);
    }

    @Override
    public void forEach(Consumer<? super TResult> action) {
        Java8ForEachHelper.forEach(this, action);
    }
}

