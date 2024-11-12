/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.connection.ServerId;
import com.mongodb.internal.connection.InternalConnection;

interface InternalConnectionFactory {
    public InternalConnection create(ServerId var1);
}

