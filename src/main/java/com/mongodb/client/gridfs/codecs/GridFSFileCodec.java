/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.gridfs.codecs;

import com.mongodb.assertions.Assertions;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.lang.Nullable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.bson.BsonDateTime;
import org.bson.BsonDocument;
import org.bson.BsonDocumentReader;
import org.bson.BsonDocumentWrapper;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonReader;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

public final class GridFSFileCodec
implements Codec<GridFSFile> {
    private static final List<String> VALID_FIELDS = Arrays.asList("_id", "filename", "length", "chunkSize", "uploadDate", "md5", "metadata");
    private final Codec<Document> documentCodec;
    private final Codec<BsonDocument> bsonDocumentCodec;

    public GridFSFileCodec(CodecRegistry registry) {
        this.documentCodec = Assertions.notNull("DocumentCodec", Assertions.notNull("registry", registry).get(Document.class));
        this.bsonDocumentCodec = Assertions.notNull("BsonDocumentCodec", registry.get(BsonDocument.class));
    }

    @Override
    public GridFSFile decode(BsonReader reader, DecoderContext decoderContext) {
        BsonDocument bsonDocument = (BsonDocument)this.bsonDocumentCodec.decode(reader, decoderContext);
        BsonValue id = bsonDocument.get("_id");
        String filename = bsonDocument.get("filename", new BsonString("")).asString().getValue();
        long length = bsonDocument.getNumber("length").longValue();
        int chunkSize = bsonDocument.getNumber("chunkSize").intValue();
        Date uploadDate = new Date(bsonDocument.getDateTime("uploadDate").getValue());
        String md5 = bsonDocument.containsKey("md5") ? bsonDocument.getString("md5").getValue() : null;
        BsonDocument metadataBsonDocument = bsonDocument.getDocument("metadata", new BsonDocument());
        Document optionalMetadata = this.asDocumentOrNull(metadataBsonDocument);
        for (String key : VALID_FIELDS) {
            bsonDocument.remove(key);
        }
        Document deprecatedExtraElements = this.asDocumentOrNull(bsonDocument);
        return new GridFSFile(id, filename, length, chunkSize, uploadDate, md5, optionalMetadata, deprecatedExtraElements);
    }

    @Override
    public void encode(BsonWriter writer, GridFSFile value, EncoderContext encoderContext) {
        Document extraElements;
        Document metadata;
        BsonDocument bsonDocument = new BsonDocument();
        bsonDocument.put("_id", value.getId());
        bsonDocument.put("filename", new BsonString(value.getFilename()));
        bsonDocument.put("length", new BsonInt64(value.getLength()));
        bsonDocument.put("chunkSize", new BsonInt32(value.getChunkSize()));
        bsonDocument.put("uploadDate", new BsonDateTime(value.getUploadDate().getTime()));
        if (value.getMD5() != null) {
            bsonDocument.put("md5", new BsonString(value.getMD5()));
        }
        if ((metadata = value.getMetadata()) != null) {
            bsonDocument.put("metadata", new BsonDocumentWrapper<Document>(metadata, this.documentCodec));
        }
        if ((extraElements = value.getExtraElements()) != null) {
            bsonDocument.putAll((Map<? extends String, ? extends BsonValue>)new BsonDocumentWrapper<Document>(extraElements, this.documentCodec));
        }
        this.bsonDocumentCodec.encode(writer, bsonDocument, encoderContext);
    }

    @Override
    public Class<GridFSFile> getEncoderClass() {
        return GridFSFile.class;
    }

    @Nullable
    private Document asDocumentOrNull(BsonDocument bsonDocument) {
        if (bsonDocument.isEmpty()) {
            return null;
        }
        BsonDocumentReader reader = new BsonDocumentReader(bsonDocument);
        return (Document)this.documentCodec.decode(reader, DecoderContext.builder().build());
    }
}

