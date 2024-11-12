/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.internal;

import com.mongodb.MongoNamespace;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.client.ClientSession;
import com.mongodb.client.internal.ChangeStreamIterableImpl;
import com.mongodb.client.internal.Java8ForEachHelper;
import com.mongodb.client.internal.OperationExecutor;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.ChangeStreamLevel;
import com.mongodb.lang.Nullable;
import java.util.List;
import java.util.function.Consumer;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

class Java8ChangeStreamIterableImpl<TResult>
extends ChangeStreamIterableImpl<TResult> {
    Java8ChangeStreamIterableImpl(@Nullable ClientSession clientSession, String databaseName, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, OperationExecutor executor, List<? extends Bson> pipeline, Class<TResult> resultClass, ChangeStreamLevel changeStreamLevel, boolean retryReads) {
        super(clientSession, databaseName, codecRegistry, readPreference, readConcern, executor, pipeline, resultClass, changeStreamLevel, retryReads);
    }

    Java8ChangeStreamIterableImpl(@Nullable ClientSession clientSession, MongoNamespace namespace, CodecRegistry codecRegistry, ReadPreference readPreference, ReadConcern readConcern, OperationExecutor executor, List<? extends Bson> pipeline, Class<TResult> resultClass, ChangeStreamLevel changeStreamLevel, boolean retryReads) {
        super(clientSession, namespace, codecRegistry, readPreference, readConcern, executor, pipeline, resultClass, changeStreamLevel, retryReads);
    }

    @Override
    public void forEach(Consumer<? super ChangeStreamDocument<TResult>> action) {
        Java8ForEachHelper.forEach(this, action);
    }
}

