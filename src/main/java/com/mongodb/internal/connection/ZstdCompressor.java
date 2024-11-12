/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.github.luben.zstd.Zstd
 *  com.github.luben.zstd.ZstdInputStream
 */
package com.mongodb.internal.connection;

import com.github.luben.zstd.Zstd;
import com.github.luben.zstd.ZstdInputStream;
import com.mongodb.MongoInternalException;
import com.mongodb.internal.connection.Compressor;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.bson.ByteBuf;
import org.bson.io.BsonOutput;

class ZstdCompressor
extends Compressor {
    private static final int DEFAULT_COMPRESSION_LEVEL = 3;

    ZstdCompressor() {
    }

    @Override
    public String getName() {
        return "zstd";
    }

    @Override
    public byte getId() {
        return 3;
    }

    @Override
    public void compress(List<ByteBuf> source, BsonOutput target) {
        int uncompressedSize = this.getUncompressedSize(source);
        byte[] singleByteArraySource = new byte[uncompressedSize];
        this.copy(source, singleByteArraySource);
        try {
            byte[] out = new byte[(int)Zstd.compressBound((long)uncompressedSize)];
            int compressedSize = (int)Zstd.compress((byte[])out, (byte[])singleByteArraySource, (int)3);
            target.writeBytes(out, 0, compressedSize);
        } catch (RuntimeException e2) {
            throw new MongoInternalException("Unexpected RuntimeException", e2);
        }
    }

    private int getUncompressedSize(List<ByteBuf> source) {
        int uncompressedSize = 0;
        for (ByteBuf cur : source) {
            uncompressedSize += cur.remaining();
        }
        return uncompressedSize;
    }

    private void copy(List<ByteBuf> source, byte[] in) {
        int offset = 0;
        for (ByteBuf cur : source) {
            int remaining = cur.remaining();
            cur.get(in, offset, remaining);
            offset += remaining;
        }
    }

    @Override
    InputStream getInputStream(InputStream source) throws IOException {
        return new ZstdInputStream(source);
    }
}

