/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.gridfs;

import com.mongodb.MongoGridFSException;
import com.mongodb.assertions.Assertions;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.gridfs.GridFSUploadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.internal.HexUtils;
import com.mongodb.lang.Nullable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.types.Binary;
import org.bson.types.ObjectId;

final class GridFSUploadStreamImpl
extends GridFSUploadStream {
    private final ClientSession clientSession;
    private final MongoCollection<GridFSFile> filesCollection;
    private final MongoCollection<Document> chunksCollection;
    private final BsonValue fileId;
    private final String filename;
    private final int chunkSizeBytes;
    private final Document metadata;
    private final MessageDigest md5;
    private byte[] buffer;
    private long lengthInBytes;
    private int bufferOffset;
    private int chunkIndex;
    private final Object closeLock = new Object();
    private boolean closed = false;

    GridFSUploadStreamImpl(@Nullable ClientSession clientSession, MongoCollection<GridFSFile> filesCollection, MongoCollection<Document> chunksCollection, BsonValue fileId, String filename, int chunkSizeBytes, boolean disableMD5, @Nullable Document metadata) {
        this.clientSession = clientSession;
        this.filesCollection = Assertions.notNull("files collection", filesCollection);
        this.chunksCollection = Assertions.notNull("chunks collection", chunksCollection);
        this.fileId = Assertions.notNull("File Id", fileId);
        this.filename = Assertions.notNull("filename", filename);
        this.chunkSizeBytes = chunkSizeBytes;
        this.md5 = this.createMD5Digest(disableMD5);
        this.metadata = metadata;
        this.chunkIndex = 0;
        this.bufferOffset = 0;
        this.buffer = new byte[chunkSizeBytes];
    }

    @Override
    public ObjectId getFileId() {
        return this.getObjectId();
    }

    @Override
    public ObjectId getObjectId() {
        if (!this.fileId.isObjectId()) {
            throw new MongoGridFSException("Custom id type used for this GridFS upload stream");
        }
        return this.fileId.asObjectId().getValue();
    }

    @Override
    public BsonValue getId() {
        return this.fileId;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void abort() {
        Object object = this.closeLock;
        synchronized (object) {
            this.checkClosed();
            this.closed = true;
        }
        if (this.clientSession != null) {
            this.chunksCollection.deleteMany(this.clientSession, new Document("files_id", this.fileId));
        } else {
            this.chunksCollection.deleteMany(new Document("files_id", this.fileId));
        }
    }

    @Override
    public void write(int b2) {
        byte[] byteArray = new byte[]{(byte)(0xFF & b2)};
        this.write(byteArray, 0, 1);
    }

    @Override
    public void write(byte[] b2) {
        this.write(b2, 0, b2.length);
    }

    @Override
    public void write(byte[] b2, int off, int len) {
        this.checkClosed();
        Assertions.notNull("b", b2);
        if (off < 0 || off > b2.length || len < 0 || off + len > b2.length || off + len < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (len == 0) {
            return;
        }
        int currentOffset = off;
        int lengthToWrite = len;
        int amountToCopy = 0;
        while (lengthToWrite > 0) {
            amountToCopy = lengthToWrite;
            if (amountToCopy > this.chunkSizeBytes - this.bufferOffset) {
                amountToCopy = this.chunkSizeBytes - this.bufferOffset;
            }
            System.arraycopy(b2, currentOffset, this.buffer, this.bufferOffset, amountToCopy);
            this.bufferOffset += amountToCopy;
            currentOffset += amountToCopy;
            lengthToWrite -= amountToCopy;
            this.lengthInBytes += (long)amountToCopy;
            if (this.bufferOffset != this.chunkSizeBytes) continue;
            this.writeChunk();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void close() {
        Object object = this.closeLock;
        synchronized (object) {
            if (this.closed) {
                return;
            }
            this.closed = true;
        }
        this.writeChunk();
        GridFSFile gridFSFile = new GridFSFile(this.fileId, this.filename, this.lengthInBytes, this.chunkSizeBytes, new Date(), this.getMD5Digest(), this.metadata);
        if (this.clientSession != null) {
            this.filesCollection.insertOne(this.clientSession, gridFSFile);
        } else {
            this.filesCollection.insertOne(gridFSFile);
        }
        this.buffer = null;
    }

    private void writeChunk() {
        if (this.bufferOffset > 0) {
            if (this.clientSession != null) {
                this.chunksCollection.insertOne(this.clientSession, new Document("files_id", this.fileId).append("n", this.chunkIndex).append("data", this.getData()));
            } else {
                this.chunksCollection.insertOne(new Document("files_id", this.fileId).append("n", this.chunkIndex).append("data", this.getData()));
            }
            this.updateMD5();
            ++this.chunkIndex;
            this.bufferOffset = 0;
        }
    }

    private Binary getData() {
        if (this.bufferOffset < this.chunkSizeBytes) {
            byte[] sizedBuffer = new byte[this.bufferOffset];
            System.arraycopy(this.buffer, 0, sizedBuffer, 0, this.bufferOffset);
            this.buffer = sizedBuffer;
        }
        return new Binary(this.buffer);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void checkClosed() {
        Object object = this.closeLock;
        synchronized (object) {
            if (this.closed) {
                throw new MongoGridFSException("The OutputStream has been closed");
            }
        }
    }

    @Nullable
    private MessageDigest createMD5Digest(boolean disableMD5) {
        if (disableMD5) {
            return null;
        }
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e2) {
            throw new MongoGridFSException("No MD5 message digest available. Use `GridFSBucket.withDisableMD5(true)` to disable creating a MD5 hash.", e2);
        }
    }

    @Nullable
    private String getMD5Digest() {
        return this.md5 != null ? HexUtils.toHex(this.md5.digest()) : null;
    }

    private void updateMD5() {
        if (this.md5 != null) {
            this.md5.update(this.buffer);
        }
    }
}

