/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.DBCollection;

abstract class WriteRequest {
    WriteRequest() {
    }

    abstract com.mongodb.bulk.WriteRequest toNew(DBCollection var1);
}

