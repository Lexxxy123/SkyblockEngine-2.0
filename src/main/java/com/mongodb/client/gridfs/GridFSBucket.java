/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.gridfs;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.annotations.ThreadSafe;
import com.mongodb.client.ClientSession;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.GridFSUploadStream;
import com.mongodb.client.gridfs.model.GridFSDownloadByNameOptions;
import com.mongodb.client.gridfs.model.GridFSDownloadOptions;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import java.io.InputStream;
import java.io.OutputStream;
import org.bson.BsonValue;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

@ThreadSafe
public interface GridFSBucket {
    public String getBucketName();

    public int getChunkSizeBytes();

    public WriteConcern getWriteConcern();

    public ReadPreference getReadPreference();

    public ReadConcern getReadConcern();

    @Deprecated
    public boolean getDisableMD5();

    public GridFSBucket withChunkSizeBytes(int var1);

    public GridFSBucket withReadPreference(ReadPreference var1);

    public GridFSBucket withWriteConcern(WriteConcern var1);

    public GridFSBucket withReadConcern(ReadConcern var1);

    @Deprecated
    public GridFSBucket withDisableMD5(boolean var1);

    public GridFSUploadStream openUploadStream(String var1);

    public GridFSUploadStream openUploadStream(String var1, GridFSUploadOptions var2);

    public GridFSUploadStream openUploadStream(BsonValue var1, String var2);

    public GridFSUploadStream openUploadStream(BsonValue var1, String var2, GridFSUploadOptions var3);

    public GridFSUploadStream openUploadStream(ClientSession var1, String var2);

    public GridFSUploadStream openUploadStream(ClientSession var1, String var2, GridFSUploadOptions var3);

    public GridFSUploadStream openUploadStream(ClientSession var1, BsonValue var2, String var3);

    public GridFSUploadStream openUploadStream(ClientSession var1, ObjectId var2, String var3);

    public GridFSUploadStream openUploadStream(ClientSession var1, BsonValue var2, String var3, GridFSUploadOptions var4);

    public ObjectId uploadFromStream(String var1, InputStream var2);

    public ObjectId uploadFromStream(String var1, InputStream var2, GridFSUploadOptions var3);

    public void uploadFromStream(BsonValue var1, String var2, InputStream var3);

    public void uploadFromStream(BsonValue var1, String var2, InputStream var3, GridFSUploadOptions var4);

    public ObjectId uploadFromStream(ClientSession var1, String var2, InputStream var3);

    public ObjectId uploadFromStream(ClientSession var1, String var2, InputStream var3, GridFSUploadOptions var4);

    public void uploadFromStream(ClientSession var1, BsonValue var2, String var3, InputStream var4);

    public void uploadFromStream(ClientSession var1, BsonValue var2, String var3, InputStream var4, GridFSUploadOptions var5);

    public GridFSDownloadStream openDownloadStream(ObjectId var1);

    public GridFSDownloadStream openDownloadStream(BsonValue var1);

    public GridFSDownloadStream openDownloadStream(String var1);

    public GridFSDownloadStream openDownloadStream(String var1, GridFSDownloadOptions var2);

    public GridFSDownloadStream openDownloadStream(ClientSession var1, ObjectId var2);

    public GridFSDownloadStream openDownloadStream(ClientSession var1, BsonValue var2);

    public GridFSDownloadStream openDownloadStream(ClientSession var1, String var2);

    public GridFSDownloadStream openDownloadStream(ClientSession var1, String var2, GridFSDownloadOptions var3);

    public void downloadToStream(ObjectId var1, OutputStream var2);

    public void downloadToStream(BsonValue var1, OutputStream var2);

    public void downloadToStream(String var1, OutputStream var2);

    public void downloadToStream(String var1, OutputStream var2, GridFSDownloadOptions var3);

    public void downloadToStream(ClientSession var1, ObjectId var2, OutputStream var3);

    public void downloadToStream(ClientSession var1, BsonValue var2, OutputStream var3);

    public void downloadToStream(ClientSession var1, String var2, OutputStream var3);

    public void downloadToStream(ClientSession var1, String var2, OutputStream var3, GridFSDownloadOptions var4);

    public GridFSFindIterable find();

    public GridFSFindIterable find(Bson var1);

    public GridFSFindIterable find(ClientSession var1);

    public GridFSFindIterable find(ClientSession var1, Bson var2);

    public void delete(ObjectId var1);

    public void delete(BsonValue var1);

    public void delete(ClientSession var1, ObjectId var2);

    public void delete(ClientSession var1, BsonValue var2);

    public void rename(ObjectId var1, String var2);

    public void rename(BsonValue var1, String var2);

    public void rename(ClientSession var1, ObjectId var2, String var3);

    public void rename(ClientSession var1, BsonValue var2, String var3);

    public void drop();

    public void drop(ClientSession var1);

    @Deprecated
    public GridFSDownloadStream openDownloadStreamByName(String var1);

    @Deprecated
    public GridFSDownloadStream openDownloadStreamByName(String var1, GridFSDownloadByNameOptions var2);

    @Deprecated
    public void downloadToStreamByName(String var1, OutputStream var2);

    @Deprecated
    public void downloadToStreamByName(String var1, OutputStream var2, GridFSDownloadByNameOptions var3);
}

