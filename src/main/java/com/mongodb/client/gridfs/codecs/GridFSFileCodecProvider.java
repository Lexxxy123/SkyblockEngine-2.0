/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.gridfs.codecs;

import com.mongodb.client.gridfs.codecs.GridFSFileCodec;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public final class GridFSFileCodecProvider
implements CodecProvider {
    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz.equals(GridFSFile.class)) {
            return new GridFSFileCodec(registry);
        }
        return null;
    }
}

