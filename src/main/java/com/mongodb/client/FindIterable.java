/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client;

import com.mongodb.CursorType;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Collation;
import com.mongodb.lang.Nullable;
import java.util.concurrent.TimeUnit;
import org.bson.conversions.Bson;

public interface FindIterable<TResult>
extends MongoIterable<TResult> {
    public FindIterable<TResult> filter(@Nullable Bson var1);

    public FindIterable<TResult> limit(int var1);

    public FindIterable<TResult> skip(int var1);

    public FindIterable<TResult> maxTime(long var1, TimeUnit var3);

    public FindIterable<TResult> maxAwaitTime(long var1, TimeUnit var3);

    @Deprecated
    public FindIterable<TResult> modifiers(@Nullable Bson var1);

    public FindIterable<TResult> projection(@Nullable Bson var1);

    public FindIterable<TResult> sort(@Nullable Bson var1);

    public FindIterable<TResult> noCursorTimeout(boolean var1);

    public FindIterable<TResult> oplogReplay(boolean var1);

    public FindIterable<TResult> partial(boolean var1);

    public FindIterable<TResult> cursorType(CursorType var1);

    @Override
    public FindIterable<TResult> batchSize(int var1);

    public FindIterable<TResult> collation(@Nullable Collation var1);

    public FindIterable<TResult> comment(@Nullable String var1);

    public FindIterable<TResult> hint(@Nullable Bson var1);

    public FindIterable<TResult> hintString(@Nullable String var1);

    public FindIterable<TResult> max(@Nullable Bson var1);

    public FindIterable<TResult> min(@Nullable Bson var1);

    @Deprecated
    public FindIterable<TResult> maxScan(long var1);

    public FindIterable<TResult> returnKey(boolean var1);

    public FindIterable<TResult> showRecordId(boolean var1);

    @Deprecated
    public FindIterable<TResult> snapshot(boolean var1);
}

