/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.connection;

import com.mongodb.MongoNamespace;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcernResult;
import com.mongodb.annotations.ThreadSafe;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.binding.ReferenceCounted;
import com.mongodb.bulk.DeleteRequest;
import com.mongodb.bulk.InsertRequest;
import com.mongodb.bulk.UpdateRequest;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.connection.QueryResult;
import com.mongodb.connection.SplittablePayload;
import com.mongodb.session.SessionContext;
import java.util.List;
import org.bson.BsonDocument;
import org.bson.FieldNameValidator;
import org.bson.codecs.Decoder;

@ThreadSafe
@Deprecated
public interface AsyncConnection
extends ReferenceCounted {
    @Override
    public AsyncConnection retain();

    public ConnectionDescription getDescription();

    public void insertAsync(MongoNamespace var1, boolean var2, InsertRequest var3, SingleResultCallback<WriteConcernResult> var4);

    public void updateAsync(MongoNamespace var1, boolean var2, UpdateRequest var3, SingleResultCallback<WriteConcernResult> var4);

    public void deleteAsync(MongoNamespace var1, boolean var2, DeleteRequest var3, SingleResultCallback<WriteConcernResult> var4);

    @Deprecated
    public <T> void commandAsync(String var1, BsonDocument var2, boolean var3, FieldNameValidator var4, Decoder<T> var5, SingleResultCallback<T> var6);

    public <T> void commandAsync(String var1, BsonDocument var2, FieldNameValidator var3, ReadPreference var4, Decoder<T> var5, SessionContext var6, SingleResultCallback<T> var7);

    public <T> void commandAsync(String var1, BsonDocument var2, FieldNameValidator var3, ReadPreference var4, Decoder<T> var5, SessionContext var6, boolean var7, SplittablePayload var8, FieldNameValidator var9, SingleResultCallback<T> var10);

    @Deprecated
    public <T> void queryAsync(MongoNamespace var1, BsonDocument var2, BsonDocument var3, int var4, int var5, boolean var6, boolean var7, boolean var8, boolean var9, boolean var10, boolean var11, Decoder<T> var12, SingleResultCallback<QueryResult<T>> var13);

    public <T> void queryAsync(MongoNamespace var1, BsonDocument var2, BsonDocument var3, int var4, int var5, int var6, boolean var7, boolean var8, boolean var9, boolean var10, boolean var11, boolean var12, Decoder<T> var13, SingleResultCallback<QueryResult<T>> var14);

    public <T> void getMoreAsync(MongoNamespace var1, long var2, int var4, Decoder<T> var5, SingleResultCallback<QueryResult<T>> var6);

    @Deprecated
    public void killCursorAsync(List<Long> var1, SingleResultCallback<Void> var2);

    public void killCursorAsync(MongoNamespace var1, List<Long> var2, SingleResultCallback<Void> var3);
}

