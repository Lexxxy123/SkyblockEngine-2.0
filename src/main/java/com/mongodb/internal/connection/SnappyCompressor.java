/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.xerial.snappy.Snappy
 *  org.xerial.snappy.SnappyInputStream
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoInternalException;
import com.mongodb.internal.connection.Compressor;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.bson.ByteBuf;
import org.bson.io.BsonOutput;
import org.xerial.snappy.Snappy;
import org.xerial.snappy.SnappyInputStream;

class SnappyCompressor
extends Compressor {
    SnappyCompressor() {
    }

    @Override
    public String getName() {
        return "snappy";
    }

    @Override
    public byte getId() {
        return 1;
    }

    @Override
    public void compress(List<ByteBuf> source, BsonOutput target) {
        int uncompressedSize = this.getUncompressedSize(source);
        byte[] singleByteArraySource = new byte[uncompressedSize];
        this.copy(source, singleByteArraySource);
        try {
            byte[] out = new byte[Snappy.maxCompressedLength((int)uncompressedSize)];
            int compressedSize = Snappy.compress((byte[])singleByteArraySource, (int)0, (int)singleByteArraySource.length, (byte[])out, (int)0);
            target.writeBytes(out, 0, compressedSize);
        } catch (IOException e2) {
            throw new MongoInternalException("Unexpected IOException", e2);
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
        return new SnappyInputStream(source);
    }
}

