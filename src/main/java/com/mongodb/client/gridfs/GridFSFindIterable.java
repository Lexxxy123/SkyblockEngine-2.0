/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.gridfs;

import com.mongodb.client.MongoIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.model.Collation;
import com.mongodb.lang.Nullable;
import java.util.concurrent.TimeUnit;
import org.bson.conversions.Bson;

public interface GridFSFindIterable
extends MongoIterable<GridFSFile> {
    public GridFSFindIterable filter(@Nullable Bson var1);

    public GridFSFindIterable limit(int var1);

    public GridFSFindIterable skip(int var1);

    public GridFSFindIterable sort(@Nullable Bson var1);

    public GridFSFindIterable noCursorTimeout(boolean var1);

    public GridFSFindIterable maxTime(long var1, TimeUnit var3);

    public GridFSFindIterable batchSize(int var1);

    public GridFSFindIterable collation(@Nullable Collation var1);
}

