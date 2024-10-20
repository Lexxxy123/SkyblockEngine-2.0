/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.gridfs;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSFile;
import com.mongodb.util.Util;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import org.bson.types.ObjectId;

public class GridFSInputFile
extends GridFSFile {
    private final InputStream inputStream;
    private final boolean closeStreamOnPersist;
    private boolean savedChunks = false;
    private byte[] buffer = null;
    private int currentChunkNumber = 0;
    private int currentBufferPosition = 0;
    private long totalBytes = 0L;
    private OutputStream outputStream = null;
    private MessageDigest messageDigester = null;

    protected GridFSInputFile(GridFS gridFS, InputStream inputStream, String filename, boolean closeStreamOnPersist) {
        this.fs = gridFS;
        this.inputStream = inputStream;
        this.filename = filename;
        this.closeStreamOnPersist = closeStreamOnPersist;
        this.id = new ObjectId();
        this.chunkSize = 261120L;
        this.uploadDate = new Date();
        try {
            this.messageDigester = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e2) {
            throw new RuntimeException("No MD5!");
        }
        this.messageDigester.reset();
        this.buffer = new byte[(int)this.chunkSize];
    }

    protected GridFSInputFile(GridFS gridFS, InputStream inputStream, String filename) {
        this(gridFS, inputStream, filename, false);
    }

    protected GridFSInputFile(GridFS gridFS, String filename) {
        this(gridFS, null, filename);
    }

    protected GridFSInputFile(GridFS gridFS) {
        this(gridFS, null, null);
    }

    public void setId(Object id) {
        this.id = id;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setChunkSize(long chunkSize) {
        if (this.outputStream != null || this.savedChunks) {
            return;
        }
        this.chunkSize = chunkSize;
        this.buffer = new byte[(int)this.chunkSize];
    }

    @Override
    public void save() {
        this.save(this.chunkSize);
    }

    public void save(long chunkSize) {
        if (this.outputStream != null) {
            throw new MongoException("cannot mix OutputStream and regular save()");
        }
        if (!this.savedChunks) {
            try {
                this.saveChunks(chunkSize);
            } catch (IOException ioe) {
                throw new MongoException("couldn't save chunks", ioe);
            }
        }
        super.save();
    }

    public int saveChunks() throws IOException {
        return this.saveChunks(this.chunkSize);
    }

    public int saveChunks(long chunkSize) throws IOException {
        if (this.outputStream != null) {
            throw new MongoException("Cannot mix OutputStream and regular save()");
        }
        if (this.savedChunks) {
            throw new MongoException("Chunks already saved!");
        }
        if (chunkSize <= 0L) {
            throw new MongoException("chunkSize must be greater than zero");
        }
        if (this.chunkSize != chunkSize) {
            this.chunkSize = chunkSize;
            this.buffer = new byte[(int)this.chunkSize];
        }
        int bytesRead = 0;
        while (bytesRead >= 0) {
            this.currentBufferPosition = 0;
            bytesRead = this._readStream2Buffer();
            this.dumpBuffer(true);
        }
        this.finishData();
        return this.currentChunkNumber;
    }

    public OutputStream getOutputStream() {
        if (this.outputStream == null) {
            this.outputStream = new GridFSOutputStream();
        }
        return this.outputStream;
    }

    private void dumpBuffer(boolean writePartial) {
        if ((long)this.currentBufferPosition < this.chunkSize && !writePartial) {
            return;
        }
        if (this.currentBufferPosition == 0) {
            return;
        }
        byte[] writeBuffer = this.buffer;
        if ((long)this.currentBufferPosition != this.chunkSize) {
            writeBuffer = new byte[this.currentBufferPosition];
            System.arraycopy(this.buffer, 0, writeBuffer, 0, this.currentBufferPosition);
        }
        DBObject chunk = this.createChunk(this.id, this.currentChunkNumber, writeBuffer);
        this.fs.getChunksCollection().save(chunk);
        ++this.currentChunkNumber;
        this.totalBytes += (long)writeBuffer.length;
        this.messageDigester.update(writeBuffer);
        this.currentBufferPosition = 0;
    }

    protected DBObject createChunk(Object id, int currentChunkNumber, byte[] writeBuffer) {
        return new BasicDBObject("files_id", id).append("n", currentChunkNumber).append("data", writeBuffer);
    }

    private int _readStream2Buffer() throws IOException {
        int bytesRead = 0;
        while ((long)this.currentBufferPosition < this.chunkSize && bytesRead >= 0) {
            bytesRead = this.inputStream.read(this.buffer, this.currentBufferPosition, (int)this.chunkSize - this.currentBufferPosition);
            if (bytesRead > 0) {
                this.currentBufferPosition += bytesRead;
                continue;
            }
            if (bytesRead != 0) continue;
            throw new RuntimeException("i'm doing something wrong");
        }
        return bytesRead;
    }

    private void finishData() {
        if (!this.savedChunks) {
            this.md5 = Util.toHex(this.messageDigester.digest());
            this.messageDigester = null;
            this.length = this.totalBytes;
            this.savedChunks = true;
            try {
                if (this.inputStream != null && this.closeStreamOnPersist) {
                    this.inputStream.close();
                }
            } catch (IOException iOException) {
                // empty catch block
            }
        }
    }

    private class GridFSOutputStream
    extends OutputStream {
        private GridFSOutputStream() {
        }

        @Override
        public void write(int b2) throws IOException {
            byte[] byteArray = new byte[]{(byte)(b2 & 0xFF)};
            this.write(byteArray, 0, 1);
        }

        @Override
        public void write(byte[] b2, int off, int len) throws IOException {
            int offset = off;
            int toCopy = 0;
            for (int length = len; length > 0; length -= toCopy) {
                toCopy = length;
                if ((long)toCopy > GridFSInputFile.this.chunkSize - (long)GridFSInputFile.this.currentBufferPosition) {
                    toCopy = (int)GridFSInputFile.this.chunkSize - GridFSInputFile.this.currentBufferPosition;
                }
                System.arraycopy(b2, offset, GridFSInputFile.this.buffer, GridFSInputFile.this.currentBufferPosition, toCopy);
                GridFSInputFile.this.currentBufferPosition += toCopy;
                offset += toCopy;
                if ((long)GridFSInputFile.this.currentBufferPosition != GridFSInputFile.this.chunkSize) continue;
                GridFSInputFile.this.dumpBuffer(false);
            }
        }

        @Override
        public void close() {
            GridFSInputFile.this.dumpBuffer(true);
            GridFSInputFile.this.finishData();
            GridFSInputFile.super.save();
        }
    }
}

