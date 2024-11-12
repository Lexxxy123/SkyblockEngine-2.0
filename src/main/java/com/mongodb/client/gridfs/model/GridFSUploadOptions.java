/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.gridfs.model;

import com.mongodb.lang.Nullable;
import org.bson.Document;

public final class GridFSUploadOptions {
    private Integer chunkSizeBytes;
    private Document metadata;

    @Nullable
    public Integer getChunkSizeBytes() {
        return this.chunkSizeBytes;
    }

    public GridFSUploadOptions chunkSizeBytes(@Nullable Integer chunkSizeBytes) {
        this.chunkSizeBytes = chunkSizeBytes;
        return this;
    }

    @Nullable
    public Document getMetadata() {
        return this.metadata;
    }

    public GridFSUploadOptions metadata(@Nullable Document metadata) {
        this.metadata = metadata;
        return this;
    }
}

