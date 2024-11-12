/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.util;

@Deprecated
public interface ObjectSerializer {
    public void serialize(Object var1, StringBuilder var2);

    public String serialize(Object var1);
}

