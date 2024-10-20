/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import org.bson.BSONObject;
import org.bson.io.OutputBuffer;

public interface DBEncoder {
    public int writeObject(OutputBuffer var1, BSONObject var2);
}

