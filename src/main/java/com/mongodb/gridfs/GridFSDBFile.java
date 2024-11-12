/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.gridfs;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.gridfs.GridFSFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class GridFSDBFile
extends GridFSFile {
    public InputStream getInputStream() {
        return new GridFSInputStream();
    }

    public long writeTo(String filename) throws IOException {
        return this.writeTo(new File(filename));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public long writeTo(File file) throws IOException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            long l2 = this.writeTo(out);
            return l2;
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public long writeTo(OutputStream out) throws IOException {
        int nc = this.numChunks();
        for (int i2 = 0; i2 < nc; ++i2) {
            out.write(this.getChunk(i2));
        }
        return this.length;
    }

    private byte[] getChunk(int chunkNumber) {
        if (this.fs == null) {
            throw new IllegalStateException("No GridFS instance defined!");
        }
        DBObject chunk = this.fs.getChunksCollection().findOne(new BasicDBObject("files_id", this.id).append("n", chunkNumber));
        if (chunk == null) {
            throw new MongoException("Can't find a chunk!  file id: " + this.id + " chunk: " + chunkNumber);
        }
        return (byte[])chunk.get("data");
    }

    void remove() {
        this.fs.getFilesCollection().remove(new BasicDBObject("_id", this.id));
        this.fs.getChunksCollection().remove(new BasicDBObject("files_id", this.id));
    }

    private class GridFSInputStream
    extends InputStream {
        private final int numberOfChunks;
        private int currentChunkId = -1;
        private int offset = 0;
        private byte[] buffer = null;

        GridFSInputStream() {
            this.numberOfChunks = GridFSDBFile.this.numChunks();
        }

        @Override
        public int available() {
            if (this.buffer == null) {
                return 0;
            }
            return this.buffer.length - this.offset;
        }

        @Override
        public int read() {
            byte[] b2 = new byte[1];
            int res = this.read(b2);
            if (res < 0) {
                return -1;
            }
            return b2[0] & 0xFF;
        }

        @Override
        public int read(byte[] b2) {
            return this.read(b2, 0, b2.length);
        }

        @Override
        public int read(byte[] b2, int off, int len) {
            if (this.buffer == null || this.offset >= this.buffer.length) {
                if (this.currentChunkId + 1 >= this.numberOfChunks) {
                    return -1;
                }
                this.buffer = GridFSDBFile.this.getChunk(++this.currentChunkId);
                this.offset = 0;
            }
            int r2 = Math.min(len, this.buffer.length - this.offset);
            System.arraycopy(this.buffer, this.offset, b2, off, r2);
            this.offset += r2;
            return r2;
        }

        @Override
        public long skip(long bytesToSkip) throws IOException {
            if (bytesToSkip <= 0L) {
                return 0L;
            }
            if (this.currentChunkId == this.numberOfChunks) {
                return 0L;
            }
            long offsetInFile = 0L;
            if (this.currentChunkId >= 0) {
                offsetInFile = (long)this.currentChunkId * GridFSDBFile.this.chunkSize + (long)this.offset;
            }
            if (bytesToSkip + offsetInFile >= GridFSDBFile.this.length) {
                this.currentChunkId = this.numberOfChunks;
                this.buffer = null;
                return GridFSDBFile.this.length - offsetInFile;
            }
            int temp = this.currentChunkId;
            this.currentChunkId = (int)((bytesToSkip + offsetInFile) / GridFSDBFile.this.chunkSize);
            if (temp != this.currentChunkId) {
                this.buffer = GridFSDBFile.this.getChunk(this.currentChunkId);
            }
            this.offset = (int)((bytesToSkip + offsetInFile) % GridFSDBFile.this.chunkSize);
            return bytesToSkip;
        }
    }
}

