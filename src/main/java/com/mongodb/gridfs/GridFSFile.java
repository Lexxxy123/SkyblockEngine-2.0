/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.gridfs;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.gridfs.GridFS;
import com.mongodb.util.JSON;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bson.BSONObject;

public abstract class GridFSFile
implements DBObject {
    private static final Set<String> VALID_FIELDS = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList("_id", "filename", "contentType", "length", "chunkSize", "uploadDate", "aliases", "md5")));
    final DBObject extra = new BasicDBObject();
    GridFS fs;
    Object id;
    String filename;
    String contentType;
    long length;
    long chunkSize;
    Date uploadDate;
    String md5;

    public void save() {
        if (this.fs == null) {
            throw new MongoException("need fs");
        }
        this.fs.getFilesCollection().save(this);
    }

    @Deprecated
    public void validate() {
        if (this.fs == null) {
            throw new MongoException("no fs");
        }
        if (this.md5 == null) {
            throw new MongoException("no md5 stored");
        }
        BasicDBObject cmd = new BasicDBObject("filemd5", this.id);
        cmd.put("root", this.fs.getBucketName());
        CommandResult res = this.fs.getDB().command(cmd);
        if (res != null && res.containsField("md5")) {
            String m2 = res.get("md5").toString();
            if (m2.equals(this.md5)) {
                return;
            }
            throw new MongoException("md5 differ.  mine [" + this.md5 + "] theirs [" + m2 + "]");
        }
        throw new MongoException("no md5 returned from server: " + res);
    }

    public int numChunks() {
        double d2 = this.length;
        return (int)Math.ceil(d2 /= (double)this.chunkSize);
    }

    public Object getId() {
        return this.id;
    }

    public String getFilename() {
        return this.filename;
    }

    public String getContentType() {
        return this.contentType;
    }

    public long getLength() {
        return this.length;
    }

    public long getChunkSize() {
        return this.chunkSize;
    }

    public Date getUploadDate() {
        return this.uploadDate;
    }

    public List<String> getAliases() {
        return (List)this.extra.get("aliases");
    }

    public DBObject getMetaData() {
        return (DBObject)this.extra.get("metadata");
    }

    public void setMetaData(DBObject metadata) {
        this.extra.put("metadata", metadata);
    }

    @Deprecated
    public String getMD5() {
        return this.md5;
    }

    @Override
    public Object put(String key, Object v2) {
        if (key == null) {
            throw new RuntimeException("key should never be null");
        }
        if (key.equals("_id")) {
            this.id = v2;
        } else if (key.equals("filename")) {
            this.filename = v2 == null ? null : v2.toString();
        } else if (key.equals("contentType")) {
            this.contentType = (String)v2;
        } else if (key.equals("length")) {
            this.length = ((Number)v2).longValue();
        } else if (key.equals("chunkSize")) {
            this.chunkSize = ((Number)v2).longValue();
        } else if (key.equals("uploadDate")) {
            this.uploadDate = (Date)v2;
        } else if (key.equals("md5")) {
            this.md5 = (String)v2;
        } else {
            this.extra.put(key, v2);
        }
        return v2;
    }

    @Override
    public Object get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key should never be null");
        }
        if (key.equals("_id")) {
            return this.id;
        }
        if (key.equals("filename")) {
            return this.filename;
        }
        if (key.equals("contentType")) {
            return this.contentType;
        }
        if (key.equals("length")) {
            return this.length;
        }
        if (key.equals("chunkSize")) {
            return this.chunkSize;
        }
        if (key.equals("uploadDate")) {
            return this.uploadDate;
        }
        if (key.equals("md5")) {
            return this.md5;
        }
        return this.extra.get(key);
    }

    @Override
    @Deprecated
    public boolean containsKey(String key) {
        return this.containsField(key);
    }

    @Override
    public boolean containsField(String s2) {
        return this.keySet().contains(s2);
    }

    @Override
    public Set<String> keySet() {
        HashSet<String> keys = new HashSet<String>();
        keys.addAll(VALID_FIELDS);
        keys.addAll(this.extra.keySet());
        return keys;
    }

    @Override
    public boolean isPartialObject() {
        return false;
    }

    @Override
    public void markAsPartialObject() {
        throw new MongoException("Can't load partial GridFSFile file");
    }

    public String toString() {
        return JSON.serialize(this);
    }

    protected void setGridFS(GridFS fs) {
        this.fs = fs;
    }

    protected GridFS getGridFS() {
        return this.fs;
    }

    @Override
    public void putAll(BSONObject o2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map m2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<?, ?> toMap() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object removeField(String key) {
        throw new UnsupportedOperationException();
    }
}

