/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.gridfs;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bson.types.ObjectId;

public class GridFS {
    public static final int DEFAULT_CHUNKSIZE = 261120;
    @Deprecated
    public static final long MAX_CHUNKSIZE = 3500000L;
    public static final String DEFAULT_BUCKET = "fs";
    private final DB database;
    private final String bucketName;
    private final DBCollection filesCollection;
    private final DBCollection chunksCollection;

    public GridFS(DB db) {
        this(db, DEFAULT_BUCKET);
    }

    public GridFS(DB db, String bucket) {
        this.database = db;
        this.bucketName = bucket;
        this.filesCollection = this.database.getCollection(this.bucketName + ".files");
        this.chunksCollection = this.database.getCollection(this.bucketName + ".chunks");
        try {
            if (this.filesCollection.count() < 1000L) {
                this.filesCollection.createIndex(new BasicDBObject("filename", (Object)1).append("uploadDate", 1));
            }
            if (this.chunksCollection.count() < 1000L) {
                this.chunksCollection.createIndex((DBObject)new BasicDBObject("files_id", (Object)1).append("n", 1), new BasicDBObject("unique", true));
            }
        } catch (MongoException mongoException) {
            // empty catch block
        }
        this.filesCollection.setObjectClass(GridFSDBFile.class);
    }

    public DBCursor getFileList() {
        return this.filesCollection.find().sort(new BasicDBObject("filename", (Object)1));
    }

    public DBCursor getFileList(DBObject query) {
        return this.filesCollection.find(query).sort(new BasicDBObject("filename", (Object)1));
    }

    public DBCursor getFileList(DBObject query, DBObject sort) {
        return this.filesCollection.find(query).sort(sort);
    }

    public GridFSDBFile find(ObjectId objectId) {
        return this.findOne(objectId);
    }

    public GridFSDBFile findOne(ObjectId objectId) {
        return this.findOne(new BasicDBObject("_id", objectId));
    }

    public GridFSDBFile findOne(String filename) {
        return this.findOne(new BasicDBObject("filename", filename));
    }

    public GridFSDBFile findOne(DBObject query) {
        return this.injectGridFSInstance(this.filesCollection.findOne(query));
    }

    public List<GridFSDBFile> find(String filename) {
        return this.find(new BasicDBObject("filename", filename));
    }

    public List<GridFSDBFile> find(String filename, DBObject sort) {
        return this.find(new BasicDBObject("filename", filename), sort);
    }

    public List<GridFSDBFile> find(DBObject query) {
        return this.find(query, null);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public List<GridFSDBFile> find(DBObject query, DBObject sort) {
        ArrayList<GridFSDBFile> files = new ArrayList<GridFSDBFile>();
        DBCursor cursor = this.filesCollection.find(query);
        if (sort != null) {
            cursor.sort(sort);
        }
        try {
            while (cursor.hasNext()) {
                files.add(this.injectGridFSInstance(cursor.next()));
            }
        } finally {
            cursor.close();
        }
        return Collections.unmodifiableList(files);
    }

    private GridFSDBFile injectGridFSInstance(Object o2) {
        if (o2 == null) {
            return null;
        }
        if (!(o2 instanceof GridFSDBFile)) {
            throw new IllegalArgumentException("somehow didn't get a GridFSDBFile");
        }
        GridFSDBFile f2 = (GridFSDBFile)o2;
        f2.fs = this;
        return f2;
    }

    public void remove(ObjectId id) {
        if (id == null) {
            throw new IllegalArgumentException("file id can not be null");
        }
        this.filesCollection.remove(new BasicDBObject("_id", id));
        this.chunksCollection.remove(new BasicDBObject("files_id", id));
    }

    public void remove(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("filename can not be null");
        }
        this.remove(new BasicDBObject("filename", filename));
    }

    public void remove(DBObject query) {
        if (query == null) {
            throw new IllegalArgumentException("query can not be null");
        }
        for (GridFSDBFile f2 : this.find(query)) {
            f2.remove();
        }
    }

    public GridFSInputFile createFile(byte[] data) {
        return this.createFile((InputStream)new ByteArrayInputStream(data), true);
    }

    public GridFSInputFile createFile(File file) throws IOException {
        return this.createFile(new FileInputStream(file), file.getName(), true);
    }

    public GridFSInputFile createFile(InputStream in) {
        return this.createFile(in, null);
    }

    public GridFSInputFile createFile(InputStream in, boolean closeStreamOnPersist) {
        return this.createFile(in, null, closeStreamOnPersist);
    }

    public GridFSInputFile createFile(InputStream in, String filename) {
        return new GridFSInputFile(this, in, filename);
    }

    public GridFSInputFile createFile(InputStream in, String filename, boolean closeStreamOnPersist) {
        return new GridFSInputFile(this, in, filename, closeStreamOnPersist);
    }

    public GridFSInputFile createFile(String filename) {
        return new GridFSInputFile(this, filename);
    }

    public GridFSInputFile createFile() {
        return new GridFSInputFile(this);
    }

    public String getBucketName() {
        return this.bucketName;
    }

    public DB getDB() {
        return this.database;
    }

    protected DBCollection getFilesCollection() {
        return this.filesCollection;
    }

    protected DBCollection getChunksCollection() {
        return this.chunksCollection;
    }
}

