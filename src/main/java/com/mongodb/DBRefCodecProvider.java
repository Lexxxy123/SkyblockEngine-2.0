/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.DBRef;
import com.mongodb.DBRefCodec;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class DBRefCodecProvider
implements CodecProvider {
    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz == DBRef.class) {
            return new DBRefCodec(registry);
        }
        return null;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        return o2 != null && this.getClass() == o2.getClass();
    }

    public int hashCode() {
        return 0;
    }
}

