/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.gridfs;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoGridFSException;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.assertions.Assertions;
import com.mongodb.client.ClientSession;
import com.mongodb.client.FindIterable;
import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.GridFSDownloadStreamImpl;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.GridFSFindIterableImpl;
import com.mongodb.client.gridfs.GridFSUploadStream;
import com.mongodb.client.gridfs.GridFSUploadStreamImpl;
import com.mongodb.client.gridfs.model.GridFSDownloadByNameOptions;
import com.mongodb.client.gridfs.model.GridFSDownloadOptions;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.lang.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
import org.bson.BsonDocument;
import org.bson.BsonObjectId;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

final class GridFSBucketImpl
implements GridFSBucket {
    private static final int DEFAULT_CHUNKSIZE_BYTES = 261120;
    private final String bucketName;
    private final int chunkSizeBytes;
    private final MongoCollection<GridFSFile> filesCollection;
    private final MongoCollection<Document> chunksCollection;
    private final boolean disableMD5;
    private volatile boolean checkedIndexes;

    GridFSBucketImpl(MongoDatabase database) {
        this(database, "fs");
    }

    GridFSBucketImpl(MongoDatabase database, String bucketName) {
        this(Assertions.notNull("bucketName", bucketName), 261120, GridFSBucketImpl.getFilesCollection(Assertions.notNull("database", database), bucketName), GridFSBucketImpl.getChunksCollection(database, bucketName), false);
    }

    GridFSBucketImpl(String bucketName, int chunkSizeBytes, MongoCollection<GridFSFile> filesCollection, MongoCollection<Document> chunksCollection, boolean disableMD5) {
        this.bucketName = Assertions.notNull("bucketName", bucketName);
        this.chunkSizeBytes = chunkSizeBytes;
        this.filesCollection = Assertions.notNull("filesCollection", filesCollection);
        this.chunksCollection = Assertions.notNull("chunksCollection", chunksCollection);
        this.disableMD5 = disableMD5;
    }

    @Override
    public String getBucketName() {
        return this.bucketName;
    }

    @Override
    public int getChunkSizeBytes() {
        return this.chunkSizeBytes;
    }

    @Override
    public ReadPreference getReadPreference() {
        return this.filesCollection.getReadPreference();
    }

    @Override
    public WriteConcern getWriteConcern() {
        return this.filesCollection.getWriteConcern();
    }

    @Override
    public ReadConcern getReadConcern() {
        return this.filesCollection.getReadConcern();
    }

    @Override
    public boolean getDisableMD5() {
        return this.disableMD5;
    }

    @Override
    public GridFSBucket withChunkSizeBytes(int chunkSizeBytes) {
        return new GridFSBucketImpl(this.bucketName, chunkSizeBytes, this.filesCollection, this.chunksCollection, this.disableMD5);
    }

    @Override
    public GridFSBucket withReadPreference(ReadPreference readPreference) {
        return new GridFSBucketImpl(this.bucketName, this.chunkSizeBytes, this.filesCollection.withReadPreference(readPreference), this.chunksCollection.withReadPreference(readPreference), this.disableMD5);
    }

    @Override
    public GridFSBucket withWriteConcern(WriteConcern writeConcern) {
        return new GridFSBucketImpl(this.bucketName, this.chunkSizeBytes, this.filesCollection.withWriteConcern(writeConcern), this.chunksCollection.withWriteConcern(writeConcern), this.disableMD5);
    }

    @Override
    public GridFSBucket withReadConcern(ReadConcern readConcern) {
        return new GridFSBucketImpl(this.bucketName, this.chunkSizeBytes, this.filesCollection.withReadConcern(readConcern), this.chunksCollection.withReadConcern(readConcern), this.disableMD5);
    }

    @Override
    public GridFSBucket withDisableMD5(boolean disableMD5) {
        return new GridFSBucketImpl(this.bucketName, this.chunkSizeBytes, this.filesCollection, this.chunksCollection, disableMD5);
    }

    @Override
    public GridFSUploadStream openUploadStream(String filename) {
        return this.openUploadStream(new BsonObjectId(), filename);
    }

    @Override
    public GridFSUploadStream openUploadStream(String filename, GridFSUploadOptions options) {
        return this.openUploadStream(new BsonObjectId(), filename, options);
    }

    @Override
    public GridFSUploadStream openUploadStream(BsonValue id, String filename) {
        return this.openUploadStream(id, filename, new GridFSUploadOptions());
    }

    @Override
    public GridFSUploadStream openUploadStream(BsonValue id, String filename, GridFSUploadOptions options) {
        return this.createGridFSUploadStream(null, id, filename, options);
    }

    @Override
    public GridFSUploadStream openUploadStream(ClientSession clientSession, String filename) {
        return this.openUploadStream(clientSession, new BsonObjectId(), filename);
    }

    @Override
    public GridFSUploadStream openUploadStream(ClientSession clientSession, String filename, GridFSUploadOptions options) {
        return this.openUploadStream(clientSession, new BsonObjectId(), filename, options);
    }

    @Override
    public GridFSUploadStream openUploadStream(ClientSession clientSession, ObjectId id, String filename) {
        return this.openUploadStream(clientSession, new BsonObjectId(id), filename);
    }

    @Override
    public GridFSUploadStream openUploadStream(ClientSession clientSession, BsonValue id, String filename) {
        return this.openUploadStream(clientSession, id, filename, new GridFSUploadOptions());
    }

    @Override
    public GridFSUploadStream openUploadStream(ClientSession clientSession, BsonValue id, String filename, GridFSUploadOptions options) {
        Assertions.notNull("clientSession", clientSession);
        return this.createGridFSUploadStream(clientSession, id, filename, options);
    }

    private GridFSUploadStream createGridFSUploadStream(@Nullable ClientSession clientSession, BsonValue id, String filename, GridFSUploadOptions options) {
        Assertions.notNull("options", options);
        Integer chunkSizeBytes = options.getChunkSizeBytes();
        int chunkSize = chunkSizeBytes == null ? this.chunkSizeBytes : chunkSizeBytes;
        this.checkCreateIndex(clientSession);
        return new GridFSUploadStreamImpl(clientSession, this.filesCollection, this.chunksCollection, id, filename, chunkSize, this.disableMD5, options.getMetadata());
    }

    @Override
    public ObjectId uploadFromStream(String filename, InputStream source) {
        return this.uploadFromStream(filename, source, new GridFSUploadOptions());
    }

    @Override
    public ObjectId uploadFromStream(String filename, InputStream source, GridFSUploadOptions options) {
        ObjectId id = new ObjectId();
        this.uploadFromStream(new BsonObjectId(id), filename, source, options);
        return id;
    }

    @Override
    public void uploadFromStream(BsonValue id, String filename, InputStream source) {
        this.uploadFromStream(id, filename, source, new GridFSUploadOptions());
    }

    @Override
    public void uploadFromStream(BsonValue id, String filename, InputStream source, GridFSUploadOptions options) {
        this.executeUploadFromStream(null, id, filename, source, options);
    }

    @Override
    public ObjectId uploadFromStream(ClientSession clientSession, String filename, InputStream source) {
        return this.uploadFromStream(clientSession, filename, source, new GridFSUploadOptions());
    }

    @Override
    public ObjectId uploadFromStream(ClientSession clientSession, String filename, InputStream source, GridFSUploadOptions options) {
        ObjectId id = new ObjectId();
        this.uploadFromStream(clientSession, new BsonObjectId(id), filename, source, options);
        return id;
    }

    @Override
    public void uploadFromStream(ClientSession clientSession, BsonValue id, String filename, InputStream source) {
        this.uploadFromStream(clientSession, id, filename, source, new GridFSUploadOptions());
    }

    @Override
    public void uploadFromStream(ClientSession clientSession, BsonValue id, String filename, InputStream source, GridFSUploadOptions options) {
        Assertions.notNull("clientSession", clientSession);
        this.executeUploadFromStream(clientSession, id, filename, source, options);
    }

    private void executeUploadFromStream(@Nullable ClientSession clientSession, BsonValue id, String filename, InputStream source, GridFSUploadOptions options) {
        GridFSUploadStream uploadStream = this.createGridFSUploadStream(clientSession, id, filename, options);
        Integer chunkSizeBytes = options.getChunkSizeBytes();
        int chunkSize = chunkSizeBytes == null ? this.chunkSizeBytes : chunkSizeBytes;
        byte[] buffer = new byte[chunkSize];
        try {
            int len;
            while ((len = source.read(buffer)) != -1) {
                uploadStream.write(buffer, 0, len);
            }
            uploadStream.close();
        } catch (IOException e2) {
            uploadStream.abort();
            throw new MongoGridFSException("IOException when reading from the InputStream", e2);
        }
    }

    @Override
    public GridFSDownloadStream openDownloadStream(ObjectId id) {
        return this.openDownloadStream(new BsonObjectId(id));
    }

    @Override
    public GridFSDownloadStream openDownloadStream(BsonValue id) {
        return this.createGridFSDownloadStream(null, this.getFileInfoById(null, id));
    }

    @Override
    public GridFSDownloadStream openDownloadStream(String filename) {
        return this.openDownloadStream(filename, new GridFSDownloadOptions());
    }

    @Override
    public GridFSDownloadStream openDownloadStream(String filename, GridFSDownloadOptions options) {
        return this.createGridFSDownloadStream(null, this.getFileByName(null, filename, options));
    }

    @Override
    public GridFSDownloadStream openDownloadStream(ClientSession clientSession, ObjectId id) {
        return this.openDownloadStream(clientSession, new BsonObjectId(id));
    }

    @Override
    public GridFSDownloadStream openDownloadStream(ClientSession clientSession, BsonValue id) {
        Assertions.notNull("clientSession", clientSession);
        return this.createGridFSDownloadStream(clientSession, this.getFileInfoById(clientSession, id));
    }

    @Override
    public GridFSDownloadStream openDownloadStream(ClientSession clientSession, String filename) {
        return this.openDownloadStream(clientSession, filename, new GridFSDownloadOptions());
    }

    @Override
    public GridFSDownloadStream openDownloadStream(ClientSession clientSession, String filename, GridFSDownloadOptions options) {
        Assertions.notNull("clientSession", clientSession);
        return this.createGridFSDownloadStream(clientSession, this.getFileByName(clientSession, filename, options));
    }

    private GridFSDownloadStream createGridFSDownloadStream(@Nullable ClientSession clientSession, GridFSFile gridFSFile) {
        return new GridFSDownloadStreamImpl(clientSession, gridFSFile, this.chunksCollection);
    }

    @Override
    public void downloadToStream(ObjectId id, OutputStream destination) {
        this.downloadToStream(new BsonObjectId(id), destination);
    }

    @Override
    public void downloadToStream(BsonValue id, OutputStream destination) {
        this.downloadToStream(this.openDownloadStream(id), destination);
    }

    @Override
    public void downloadToStream(String filename, OutputStream destination) {
        this.downloadToStream(filename, destination, new GridFSDownloadOptions());
    }

    @Override
    public void downloadToStream(String filename, OutputStream destination, GridFSDownloadOptions options) {
        this.downloadToStream(this.openDownloadStream(filename, options), destination);
    }

    @Override
    public void downloadToStream(ClientSession clientSession, ObjectId id, OutputStream destination) {
        this.downloadToStream(clientSession, new BsonObjectId(id), destination);
    }

    @Override
    public void downloadToStream(ClientSession clientSession, BsonValue id, OutputStream destination) {
        Assertions.notNull("clientSession", clientSession);
        this.downloadToStream(this.openDownloadStream(clientSession, id), destination);
    }

    @Override
    public void downloadToStream(ClientSession clientSession, String filename, OutputStream destination) {
        this.downloadToStream(clientSession, filename, destination, new GridFSDownloadOptions());
    }

    @Override
    public void downloadToStream(ClientSession clientSession, String filename, OutputStream destination, GridFSDownloadOptions options) {
        Assertions.notNull("clientSession", clientSession);
        this.downloadToStream(this.openDownloadStream(clientSession, filename, options), destination);
    }

    @Override
    public GridFSFindIterable find() {
        return this.createGridFSFindIterable(null, null);
    }

    @Override
    public GridFSFindIterable find(Bson filter) {
        Assertions.notNull("filter", filter);
        return this.createGridFSFindIterable(null, filter);
    }

    @Override
    public GridFSFindIterable find(ClientSession clientSession) {
        Assertions.notNull("clientSession", clientSession);
        return this.createGridFSFindIterable(clientSession, null);
    }

    @Override
    public GridFSFindIterable find(ClientSession clientSession, Bson filter) {
        Assertions.notNull("clientSession", clientSession);
        Assertions.notNull("filter", filter);
        return this.createGridFSFindIterable(clientSession, filter);
    }

    private GridFSFindIterable createGridFSFindIterable(@Nullable ClientSession clientSession, @Nullable Bson filter) {
        return new GridFSFindIterableImpl(this.createFindIterable(clientSession, filter));
    }

    @Override
    public void delete(ObjectId id) {
        this.delete(new BsonObjectId(id));
    }

    @Override
    public void delete(BsonValue id) {
        this.executeDelete(null, id);
    }

    @Override
    public void delete(ClientSession clientSession, ObjectId id) {
        this.delete(clientSession, new BsonObjectId(id));
    }

    @Override
    public void delete(ClientSession clientSession, BsonValue id) {
        Assertions.notNull("clientSession", clientSession);
        this.executeDelete(clientSession, id);
    }

    private void executeDelete(@Nullable ClientSession clientSession, BsonValue id) {
        DeleteResult result;
        if (clientSession != null) {
            result = this.filesCollection.deleteOne(clientSession, new BsonDocument("_id", id));
            this.chunksCollection.deleteMany(clientSession, new BsonDocument("files_id", id));
        } else {
            result = this.filesCollection.deleteOne(new BsonDocument("_id", id));
            this.chunksCollection.deleteMany(new BsonDocument("files_id", id));
        }
        if (result.wasAcknowledged() && result.getDeletedCount() == 0L) {
            throw new MongoGridFSException(String.format("No file found with the id: %s", id));
        }
    }

    @Override
    public void rename(ObjectId id, String newFilename) {
        this.rename(new BsonObjectId(id), newFilename);
    }

    @Override
    public void rename(BsonValue id, String newFilename) {
        this.executeRename(null, id, newFilename);
    }

    @Override
    public void rename(ClientSession clientSession, ObjectId id, String newFilename) {
        this.rename(clientSession, new BsonObjectId(id), newFilename);
    }

    @Override
    public void rename(ClientSession clientSession, BsonValue id, String newFilename) {
        Assertions.notNull("clientSession", clientSession);
        this.executeRename(clientSession, id, newFilename);
    }

    private void executeRename(@Nullable ClientSession clientSession, BsonValue id, String newFilename) {
        UpdateResult updateResult = clientSession != null ? this.filesCollection.updateOne(clientSession, (Bson)new BsonDocument("_id", id), new BsonDocument("$set", new BsonDocument("filename", new BsonString(newFilename)))) : this.filesCollection.updateOne((Bson)new BsonDocument("_id", id), new BsonDocument("$set", new BsonDocument("filename", new BsonString(newFilename))));
        if (updateResult.wasAcknowledged() && updateResult.getMatchedCount() == 0L) {
            throw new MongoGridFSException(String.format("No file found with the id: %s", id));
        }
    }

    @Override
    public void drop() {
        this.filesCollection.drop();
        this.chunksCollection.drop();
    }

    @Override
    public void drop(ClientSession clientSession) {
        Assertions.notNull("clientSession", clientSession);
        this.filesCollection.drop(clientSession);
        this.chunksCollection.drop(clientSession);
    }

    @Override
    public GridFSDownloadStream openDownloadStreamByName(String filename) {
        return this.openDownloadStreamByName(filename, new GridFSDownloadByNameOptions());
    }

    @Override
    public GridFSDownloadStream openDownloadStreamByName(String filename, GridFSDownloadByNameOptions options) {
        return this.openDownloadStream(filename, new GridFSDownloadOptions().revision(options.getRevision()));
    }

    @Override
    public void downloadToStreamByName(String filename, OutputStream destination) {
        this.downloadToStreamByName(filename, destination, new GridFSDownloadByNameOptions());
    }

    @Override
    public void downloadToStreamByName(String filename, OutputStream destination, GridFSDownloadByNameOptions options) {
        this.downloadToStream(filename, destination, new GridFSDownloadOptions().revision(options.getRevision()));
    }

    private static MongoCollection<GridFSFile> getFilesCollection(MongoDatabase database, String bucketName) {
        return database.getCollection(bucketName + ".files", GridFSFile.class).withCodecRegistry(CodecRegistries.fromRegistries(database.getCodecRegistry(), MongoClientSettings.getDefaultCodecRegistry()));
    }

    private static MongoCollection<Document> getChunksCollection(MongoDatabase database, String bucketName) {
        return database.getCollection(bucketName + ".chunks").withCodecRegistry(MongoClientSettings.getDefaultCodecRegistry());
    }

    private void checkCreateIndex(@Nullable ClientSession clientSession) {
        if (!this.checkedIndexes) {
            if (this.collectionIsEmpty(clientSession, this.filesCollection.withDocumentClass(Document.class).withReadPreference(ReadPreference.primary()))) {
                Document filesIndex = new Document("filename", 1).append("uploadDate", 1);
                if (!this.hasIndex(clientSession, this.filesCollection.withReadPreference(ReadPreference.primary()), filesIndex)) {
                    this.createIndex(clientSession, this.filesCollection, filesIndex, new IndexOptions());
                }
                Document chunksIndex = new Document("files_id", 1).append("n", 1);
                if (!this.hasIndex(clientSession, this.chunksCollection.withReadPreference(ReadPreference.primary()), chunksIndex)) {
                    this.createIndex(clientSession, this.chunksCollection, chunksIndex, new IndexOptions().unique(true));
                }
            }
            this.checkedIndexes = true;
        }
    }

    private <T> boolean collectionIsEmpty(@Nullable ClientSession clientSession, MongoCollection<T> collection) {
        if (clientSession != null) {
            return collection.find(clientSession).projection(new Document("_id", 1)).first() == null;
        }
        return collection.find().projection(new Document("_id", 1)).first() == null;
    }

    private <T> boolean hasIndex(@Nullable ClientSession clientSession, MongoCollection<T> collection, Document index) {
        boolean hasIndex = false;
        ListIndexesIterable<Document> listIndexesIterable = clientSession != null ? collection.listIndexes(clientSession) : collection.listIndexes();
        ArrayList indexes = listIndexesIterable.into(new ArrayList());
        for (Document result : indexes) {
            Document indexDoc = result.get((Object)"key", new Document());
            for (Map.Entry<String, Object> entry : indexDoc.entrySet()) {
                if (!(entry.getValue() instanceof Number)) continue;
                entry.setValue(((Number)entry.getValue()).intValue());
            }
            if (!indexDoc.equals(index)) continue;
            hasIndex = true;
            break;
        }
        return hasIndex;
    }

    private <T> void createIndex(@Nullable ClientSession clientSession, MongoCollection<T> collection, Document index, IndexOptions indexOptions) {
        if (clientSession != null) {
            collection.createIndex(clientSession, index, indexOptions);
        } else {
            collection.createIndex(index, indexOptions);
        }
    }

    private GridFSFile getFileByName(@Nullable ClientSession clientSession, String filename, GridFSDownloadOptions options) {
        int sort;
        int skip;
        int revision = options.getRevision();
        if (revision >= 0) {
            skip = revision;
            sort = 1;
        } else {
            skip = -revision - 1;
            sort = -1;
        }
        GridFSFile fileInfo = (GridFSFile)this.createGridFSFindIterable(clientSession, new Document("filename", filename)).skip(skip).sort(new Document("uploadDate", sort)).first();
        if (fileInfo == null) {
            throw new MongoGridFSException(String.format("No file found with the filename: %s and revision: %s", filename, revision));
        }
        return fileInfo;
    }

    private GridFSFile getFileInfoById(@Nullable ClientSession clientSession, BsonValue id) {
        Assertions.notNull("id", id);
        GridFSFile fileInfo = (GridFSFile)this.createFindIterable(clientSession, new Document("_id", id)).first();
        if (fileInfo == null) {
            throw new MongoGridFSException(String.format("No file found with the id: %s", id));
        }
        return fileInfo;
    }

    private FindIterable<GridFSFile> createFindIterable(@Nullable ClientSession clientSession, @Nullable Bson filter) {
        FindIterable<GridFSFile> findIterable = clientSession != null ? this.filesCollection.find(clientSession) : this.filesCollection.find();
        if (filter != null) {
            findIterable = findIterable.filter(filter);
        }
        return findIterable;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void downloadToStream(GridFSDownloadStream downloadStream, OutputStream destination) {
        block16: {
            byte[] buffer = new byte[downloadStream.getGridFSFile().getChunkSize()];
            MongoGridFSException savedThrowable = null;
            try {
                int len;
                while ((len = downloadStream.read(buffer)) != -1) {
                    destination.write(buffer, 0, len);
                }
            } catch (IOException e2) {
                savedThrowable = new MongoGridFSException("IOException when reading from the OutputStream", e2);
            } catch (Exception e3) {
                savedThrowable = new MongoGridFSException("Unexpected Exception when reading GridFS and writing to the Stream", e3);
            } finally {
                try {
                    downloadStream.close();
                } catch (Exception e2) {}
                if (savedThrowable == null) break block16;
                throw savedThrowable;
            }
        }
    }
}

