/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoInternalException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.bson.ByteBuf;
import org.bson.io.BsonOutput;

abstract class Compressor {
    static final int BUFFER_SIZE = 256;

    Compressor() {
    }

    abstract String getName();

    abstract byte getId();

    void compress(List<ByteBuf> source, BsonOutput target) {
        BufferExposingByteArrayOutputStream baos = new BufferExposingByteArrayOutputStream(1024);
        OutputStream outputStream = null;
        try {
            outputStream = this.getOutputStream(baos);
            byte[] scratch = new byte[256];
            for (ByteBuf cur : source) {
                while (cur.hasRemaining()) {
                    int numBytes = Math.min(cur.remaining(), scratch.length);
                    cur.get(scratch, 0, numBytes);
                    outputStream.write(scratch, 0, numBytes);
                }
            }
        } catch (IOException e2) {
            throw new MongoInternalException("Unexpected IOException", e2);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException iOException) {}
        }
        target.writeBytes(baos.getInternalBytes(), 0, baos.size());
    }

    void uncompress(ByteBuf source, ByteBuf target) {
        InputStream inputStream = null;
        try {
            inputStream = this.getInputStream(new ByteBufInputStream(source));
            byte[] scratch = new byte[256];
            int numBytes = inputStream.read(scratch);
            while (numBytes != -1) {
                target.put(scratch, 0, numBytes);
                numBytes = inputStream.read(scratch);
            }
        } catch (IOException e2) {
            throw new MongoInternalException("Unexpected IOException", e2);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException iOException) {}
        }
    }

    OutputStream getOutputStream(OutputStream source) throws IOException {
        throw new UnsupportedEncodingException();
    }

    InputStream getInputStream(InputStream source) throws IOException {
        throw new UnsupportedOperationException();
    }

    private static final class BufferExposingByteArrayOutputStream
    extends ByteArrayOutputStream {
        BufferExposingByteArrayOutputStream(int size) {
            super(size);
        }

        byte[] getInternalBytes() {
            return this.buf;
        }
    }

    private static final class ByteBufInputStream
    extends InputStream {
        private final ByteBuf source;

        ByteBufInputStream(ByteBuf source) {
            this.source = source;
        }

        @Override
        public int read(byte[] bytes, int offset, int length) {
            if (!this.source.hasRemaining()) {
                return -1;
            }
            int bytesToRead = length > this.source.remaining() ? this.source.remaining() : length;
            this.source.get(bytes, offset, bytesToRead);
            return bytesToRead;
        }

        @Override
        public int read() {
            throw new UnsupportedOperationException();
        }
    }
}

