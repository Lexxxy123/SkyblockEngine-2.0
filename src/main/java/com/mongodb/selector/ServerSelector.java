/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.selector;

import com.mongodb.annotations.ThreadSafe;
import com.mongodb.connection.ClusterDescription;
import com.mongodb.connection.ServerDescription;
import java.util.List;

@ThreadSafe
public interface ServerSelector {
    public List<ServerDescription> select(ClusterDescription var1);
}

