/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.internal;

import com.mongodb.MongoNamespace;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.ClientSession;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.ListCollectionsIterable;
import com.mongodb.client.ListDatabasesIterable;
import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.MapReduceIterable;
import com.mongodb.client.internal.OperationExecutor;
import com.mongodb.client.model.AggregationLevel;
import com.mongodb.client.model.changestream.ChangeStreamLevel;
import com.mongodb.lang.Nullable;
import java.util.List;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

interface MongoIterableFactory {
    public <TDocument, TResult> FindIterable<TResult> findOf(@Nullable ClientSession var1, MongoNamespace var2, Class<TDocument> var3, Class<TResult> var4, CodecRegistry var5, ReadPreference var6, ReadConcern var7, OperationExecutor var8, Bson var9, boolean var10);

    public <TDocument, TResult> AggregateIterable<TResult> aggregateOf(@Nullable ClientSession var1, MongoNamespace var2, Class<TDocument> var3, Class<TResult> var4, CodecRegistry var5, ReadPreference var6, ReadConcern var7, WriteConcern var8, OperationExecutor var9, List<? extends Bson> var10, AggregationLevel var11, boolean var12);

    public <TDocument, TResult> AggregateIterable<TResult> aggregateOf(@Nullable ClientSession var1, String var2, Class<TDocument> var3, Class<TResult> var4, CodecRegistry var5, ReadPreference var6, ReadConcern var7, WriteConcern var8, OperationExecutor var9, List<? extends Bson> var10, AggregationLevel var11, boolean var12);

    public <TResult> ChangeStreamIterable<TResult> changeStreamOf(@Nullable ClientSession var1, String var2, CodecRegistry var3, ReadPreference var4, ReadConcern var5, OperationExecutor var6, List<? extends Bson> var7, Class<TResult> var8, ChangeStreamLevel var9, boolean var10);

    public <TResult> ChangeStreamIterable<TResult> changeStreamOf(@Nullable ClientSession var1, MongoNamespace var2, CodecRegistry var3, ReadPreference var4, ReadConcern var5, OperationExecutor var6, List<? extends Bson> var7, Class<TResult> var8, ChangeStreamLevel var9, boolean var10);

    public <TDocument, TResult> DistinctIterable<TResult> distinctOf(@Nullable ClientSession var1, MongoNamespace var2, Class<TDocument> var3, Class<TResult> var4, CodecRegistry var5, ReadPreference var6, ReadConcern var7, OperationExecutor var8, String var9, Bson var10, boolean var11);

    public <TResult> ListDatabasesIterable<TResult> listDatabasesOf(@Nullable ClientSession var1, Class<TResult> var2, CodecRegistry var3, ReadPreference var4, OperationExecutor var5, boolean var6);

    public <TResult> ListCollectionsIterable<TResult> listCollectionsOf(@Nullable ClientSession var1, String var2, boolean var3, Class<TResult> var4, CodecRegistry var5, ReadPreference var6, OperationExecutor var7, boolean var8);

    public <TResult> ListIndexesIterable<TResult> listIndexesOf(@Nullable ClientSession var1, MongoNamespace var2, Class<TResult> var3, CodecRegistry var4, ReadPreference var5, OperationExecutor var6, boolean var7);

    public <TDocument, TResult> MapReduceIterable<TResult> mapReduceOf(@Nullable ClientSession var1, MongoNamespace var2, Class<TDocument> var3, Class<TResult> var4, CodecRegistry var5, ReadPreference var6, ReadConcern var7, WriteConcern var8, OperationExecutor var9, String var10, String var11);
}

