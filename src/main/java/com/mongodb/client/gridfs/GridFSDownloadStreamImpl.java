/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.gridfs;

import com.mongodb.MongoGridFSException;
import com.mongodb.assertions.Assertions;
import com.mongodb.client.ClientSession;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.lang.Nullable;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.types.Binary;

class GridFSDownloadStreamImpl
extends GridFSDownloadStream {
    private final ClientSession clientSession;
    private final GridFSFile fileInfo;
    private final MongoCollection<Document> chunksCollection;
    private final BsonValue fileId;
    private final long length;
    private final int chunkSizeInBytes;
    private final int numberOfChunks;
    private MongoCursor<Document> cursor;
    private int batchSize;
    private int chunkIndex;
    private int bufferOffset;
    private long currentPosition;
    private byte[] buffer = null;
    private long markPosition;
    private final Object closeLock = new Object();
    private final Object cursorLock = new Object();
    private boolean closed = false;

    GridFSDownloadStreamImpl(@Nullable ClientSession clientSession, GridFSFile fileInfo, MongoCollection<Document> chunksCollection) {
        this.clientSession = clientSession;
        this.fileInfo = Assertions.notNull("file information", fileInfo);
        this.chunksCollection = Assertions.notNull("chunks collection", chunksCollection);
        this.fileId = fileInfo.getId();
        this.length = fileInfo.getLength();
        this.chunkSizeInBytes = fileInfo.getChunkSize();
        this.numberOfChunks = (int)Math.ceil((double)this.length / (double)this.chunkSizeInBytes);
    }

    @Override
    public GridFSFile getGridFSFile() {
        return this.fileInfo;
    }

    @Override
    public GridFSDownloadStream batchSize(int batchSize) {
        Assertions.isTrueArgument("batchSize cannot be negative", batchSize >= 0);
        this.batchSize = batchSize;
        this.discardCursor();
        return this;
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
        this.checkClosed();
        if (this.currentPosition == this.length) {
            return -1;
        }
        if (this.buffer == null) {
            this.buffer = this.getBuffer(this.chunkIndex);
        } else if (this.bufferOffset == this.buffer.length) {
            ++this.chunkIndex;
            this.buffer = this.getBuffer(this.chunkIndex);
            this.bufferOffset = 0;
        }
        int r2 = Math.min(len, this.buffer.length - this.bufferOffset);
        System.arraycopy(this.buffer, this.bufferOffset, b2, off, r2);
        this.bufferOffset += r2;
        this.currentPosition += (long)r2;
        return r2;
    }

    @Override
    public long skip(long bytesToSkip) {
        this.checkClosed();
        if (bytesToSkip <= 0L) {
            return 0L;
        }
        long skippedPosition = this.currentPosition + bytesToSkip;
        this.bufferOffset = (int)(skippedPosition % (long)this.chunkSizeInBytes);
        if (skippedPosition >= this.length) {
            long skipped = this.length - this.currentPosition;
            this.chunkIndex = this.numberOfChunks - 1;
            this.currentPosition = this.length;
            this.buffer = null;
            this.discardCursor();
            return skipped;
        }
        int newChunkIndex = (int)Math.floor((double)skippedPosition / (double)this.chunkSizeInBytes);
        if (this.chunkIndex != newChunkIndex) {
            this.chunkIndex = newChunkIndex;
            this.buffer = null;
            this.discardCursor();
        }
        this.currentPosition += bytesToSkip;
        return bytesToSkip;
    }

    @Override
    public int available() {
        this.checkClosed();
        if (this.buffer == null) {
            return 0;
        }
        return this.buffer.length - this.bufferOffset;
    }

    @Override
    public void mark() {
        this.mark(Integer.MAX_VALUE);
    }

    @Override
    public synchronized void mark(int readlimit) {
        this.markPosition = this.currentPosition;
    }

    @Override
    public synchronized void reset() {
        this.checkClosed();
        if (this.currentPosition == this.markPosition) {
            return;
        }
        this.bufferOffset = (int)(this.markPosition % (long)this.chunkSizeInBytes);
        this.currentPosition = this.markPosition;
        int markChunkIndex = (int)Math.floor((double)this.markPosition / (double)this.chunkSizeInBytes);
        if (markChunkIndex != this.chunkIndex) {
            this.chunkIndex = markChunkIndex;
            this.buffer = null;
            this.discardCursor();
        }
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void close() {
        Object object = this.closeLock;
        synchronized (object) {
            if (!this.closed) {
                this.closed = true;
            }
            this.discardCursor();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void checkClosed() {
        Object object = this.closeLock;
        synchronized (object) {
            if (this.closed) {
                throw new MongoGridFSException("The InputStream has been closed");
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void discardCursor() {
        Object object = this.cursorLock;
        synchronized (object) {
            if (this.cursor != null) {
                this.cursor.close();
                this.cursor = null;
            }
        }
    }

    @Nullable
    private Document getChunk(int startChunkIndex) {
        if (this.cursor == null) {
            this.cursor = this.getCursor(startChunkIndex);
        }
        Document chunk = null;
        if (this.cursor.hasNext()) {
            chunk = this.cursor.next();
            if (this.batchSize == 1) {
                this.discardCursor();
            }
            if (chunk.getInteger("n") != startChunkIndex) {
                throw new MongoGridFSException(String.format("Could not find file chunk for file_id: %s at chunk index %s.", this.fileId, startChunkIndex));
            }
        }
        return chunk;
    }

    private MongoCursor<Document> getCursor(int startChunkIndex) {
        Document filter = new Document("files_id", this.fileId).append("n", new Document("$gte", startChunkIndex));
        FindIterable<Document> findIterable = this.clientSession != null ? this.chunksCollection.find(this.clientSession, filter) : this.chunksCollection.find(filter);
        return findIterable.batchSize(this.batchSize).sort(new Document("n", 1)).iterator();
    }

    private byte[] getBufferFromChunk(@Nullable Document chunk, int expectedChunkIndex) {
        if (chunk == null || chunk.getInteger("n") != expectedChunkIndex) {
            throw new MongoGridFSException(String.format("Could not find file chunk for file_id: %s at chunk index %s.", this.fileId, expectedChunkIndex));
        }
        if (!(chunk.get("data") instanceof Binary)) {
            throw new MongoGridFSException("Unexpected data format for the chunk");
        }
        byte[] data = ((Binary)((Object)chunk.get((Object)"data", Binary.class))).getData();
        long expectedDataLength = 0L;
        boolean extraChunk = false;
        if (expectedChunkIndex + 1 > this.numberOfChunks) {
            extraChunk = true;
        } else {
            expectedDataLength = expectedChunkIndex + 1 == this.numberOfChunks ? this.length - (long)expectedChunkIndex * (long)this.chunkSizeInBytes : (long)this.chunkSizeInBytes;
        }
        if (extraChunk && (long)data.length > expectedDataLength) {
            throw new MongoGridFSException(String.format("Extra chunk data for file_id: %s. Unexpected chunk at chunk index %s.The size was %s and it should be %s bytes.", this.fileId, expectedChunkIndex, data.length, expectedDataLength));
        }
        if ((long)data.length != expectedDataLength) {
            throw new MongoGridFSException(String.format("Chunk size data length is not the expected size. The size was %s for file_id: %s chunk index %s it should be %s bytes.", data.length, this.fileId, expectedChunkIndex, expectedDataLength));
        }
        return data;
    }

    private byte[] getBuffer(int chunkIndexToFetch) {
        return this.getBufferFromChunk(this.getChunk(chunkIndexToFetch), chunkIndexToFetch);
    }
}

