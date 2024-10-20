/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.util;

import com.mongodb.util.ObjectSerializer;

abstract class AbstractObjectSerializer
implements ObjectSerializer {
    AbstractObjectSerializer() {
    }

    @Override
    public String serialize(Object obj) {
        StringBuilder builder = new StringBuilder();
        this.serialize(obj, builder);
        return builder.toString();
    }
}

