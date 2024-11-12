/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.internal.connection.ChangeEvent;

interface ChangeListener<T> {
    public void stateChanged(ChangeEvent<T> var1);
}

