/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoCompressor;
import com.mongodb.internal.connection.Compressor;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

class ZlibCompressor
extends Compressor {
    private final int level;

    ZlibCompressor(MongoCompressor mongoCompressor) {
        this.level = mongoCompressor.getPropertyNonNull("LEVEL", -1);
    }

    @Override
    public String getName() {
        return "zlib";
    }

    @Override
    public byte getId() {
        return 2;
    }

    @Override
    InputStream getInputStream(InputStream source) {
        return new InflaterInputStream(source);
    }

    @Override
    OutputStream getOutputStream(OutputStream source) {
        return new DeflaterOutputStream(source, new Deflater(this.level));
    }
}

