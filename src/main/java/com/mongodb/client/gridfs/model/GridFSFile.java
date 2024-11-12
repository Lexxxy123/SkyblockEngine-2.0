/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.gridfs.model;

import com.mongodb.MongoGridFSException;
import com.mongodb.assertions.Assertions;
import com.mongodb.lang.Nullable;
import java.util.Date;
import java.util.List;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.types.ObjectId;

public final class GridFSFile {
    private final BsonValue id;
    private final String filename;
    private final long length;
    private final int chunkSize;
    private final Date uploadDate;
    private final String md5;
    private final Document metadata;
    private final Document extraElements;

    @Deprecated
    public GridFSFile(BsonValue id, String filename, long length, int chunkSize, Date uploadDate, @Nullable String md5, Document metadata) {
        this(id, filename, length, chunkSize, uploadDate, md5, metadata, null);
    }

    @Deprecated
    public GridFSFile(BsonValue id, String filename, long length, int chunkSize, Date uploadDate, @Nullable String md5, @Nullable Document metadata, @Nullable Document extraElements) {
        this.id = Assertions.notNull("id", id);
        this.filename = Assertions.notNull("filename", filename);
        this.length = Assertions.notNull("length", length);
        this.chunkSize = Assertions.notNull("chunkSize", chunkSize);
        this.uploadDate = Assertions.notNull("uploadDate", uploadDate);
        this.md5 = md5;
        this.metadata = metadata != null && metadata.isEmpty() ? null : metadata;
        this.extraElements = extraElements;
    }

    public ObjectId getObjectId() {
        if (!this.id.isObjectId()) {
            throw new MongoGridFSException("Custom id type used for this GridFS file");
        }
        return this.id.asObjectId().getValue();
    }

    public BsonValue getId() {
        return this.id;
    }

    public String getFilename() {
        return this.filename;
    }

    public long getLength() {
        return this.length;
    }

    public int getChunkSize() {
        return this.chunkSize;
    }

    public Date getUploadDate() {
        return this.uploadDate;
    }

    @Deprecated
    @Nullable
    public String getMD5() {
        return this.md5;
    }

    @Nullable
    public Document getMetadata() {
        return this.metadata;
    }

    @Deprecated
    @Nullable
    public Document getExtraElements() {
        return this.extraElements;
    }

    @Deprecated
    public String getContentType() {
        if (this.extraElements != null && this.extraElements.containsKey("contentType")) {
            return this.extraElements.getString("contentType");
        }
        throw new MongoGridFSException("No contentType data for this GridFS file");
    }

    @Deprecated
    public List<String> getAliases() {
        if (this.extraElements != null && this.extraElements.containsKey("aliases")) {
            return (List)this.extraElements.get("aliases");
        }
        throw new MongoGridFSException("No aliases data for this GridFS file");
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        GridFSFile that = (GridFSFile)o2;
        if (this.id != null ? !this.id.equals(that.id) : that.id != null) {
            return false;
        }
        if (!this.filename.equals(that.filename)) {
            return false;
        }
        if (this.length != that.length) {
            return false;
        }
        if (this.chunkSize != that.chunkSize) {
            return false;
        }
        if (!this.uploadDate.equals(that.uploadDate)) {
            return false;
        }
        if (this.md5 != null ? !this.md5.equals(that.md5) : that.md5 != null) {
            return false;
        }
        if (this.metadata != null ? !this.metadata.equals(that.metadata) : that.metadata != null) {
            return false;
        }
        return !(this.extraElements != null ? !this.extraElements.equals(that.extraElements) : that.extraElements != null);
    }

    public int hashCode() {
        int result = this.id != null ? this.id.hashCode() : 0;
        result = 31 * result + this.filename.hashCode();
        result = 31 * result + (int)(this.length ^ this.length >>> 32);
        result = 31 * result + this.chunkSize;
        result = 31 * result + this.uploadDate.hashCode();
        result = 31 * result + (this.md5 != null ? this.md5.hashCode() : 0);
        result = 31 * result + (this.metadata != null ? this.metadata.hashCode() : 0);
        result = 31 * result + (this.extraElements != null ? this.extraElements.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "GridFSFile{id=" + this.id + ", filename='" + this.filename + '\'' + ", length=" + this.length + ", chunkSize=" + this.chunkSize + ", uploadDate=" + this.uploadDate + ", md5='" + this.md5 + '\'' + ", metadata=" + this.metadata + ", extraElements='" + this.extraElements + '\'' + '}';
    }
}

