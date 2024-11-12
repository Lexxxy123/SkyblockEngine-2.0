/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.session;

import com.mongodb.ClientSessionOptions;
import com.mongodb.ServerAddress;
import com.mongodb.annotations.NotThreadSafe;
import com.mongodb.lang.Nullable;
import com.mongodb.session.ServerSession;
import java.io.Closeable;
import org.bson.BsonDocument;
import org.bson.BsonTimestamp;

@NotThreadSafe
public interface ClientSession
extends Closeable {
    @Nullable
    public ServerAddress getPinnedServerAddress();

    public void setPinnedServerAddress(ServerAddress var1);

    @Nullable
    public BsonDocument getRecoveryToken();

    public void setRecoveryToken(BsonDocument var1);

    public ClientSessionOptions getOptions();

    public boolean isCausallyConsistent();

    public Object getOriginator();

    public ServerSession getServerSession();

    public BsonTimestamp getOperationTime();

    public void advanceOperationTime(BsonTimestamp var1);

    public void advanceClusterTime(BsonDocument var1);

    public BsonDocument getClusterTime();

    @Override
    public void close();
}

