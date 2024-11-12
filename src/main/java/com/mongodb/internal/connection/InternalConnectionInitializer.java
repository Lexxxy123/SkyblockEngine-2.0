/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.async.SingleResultCallback;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.internal.connection.InternalConnection;

interface InternalConnectionInitializer {
    public ConnectionDescription initialize(InternalConnection var1);

    public void initializeAsync(InternalConnection var1, SingleResultCallback<ConnectionDescription> var2);
}

