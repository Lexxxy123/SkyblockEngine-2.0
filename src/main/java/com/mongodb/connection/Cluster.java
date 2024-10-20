/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.connection;

import com.mongodb.async.SingleResultCallback;
import com.mongodb.connection.ClusterDescription;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.Server;
import com.mongodb.lang.Nullable;
import com.mongodb.selector.ServerSelector;
import java.io.Closeable;
import org.bson.BsonTimestamp;

@Deprecated
public interface Cluster
extends Closeable {
    public ClusterSettings getSettings();

    public ClusterDescription getDescription();

    public ClusterDescription getCurrentDescription();

    @Nullable
    public BsonTimestamp getClusterTime();

    public Server selectServer(ServerSelector var1);

    public void selectServerAsync(ServerSelector var1, SingleResultCallback<Server> var2);

    @Override
    public void close();

    public boolean isClosed();
}

