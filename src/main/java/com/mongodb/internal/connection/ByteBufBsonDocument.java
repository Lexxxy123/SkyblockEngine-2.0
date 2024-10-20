/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.connection.ByteBufferBsonOutput;
import com.mongodb.internal.connection.AbstractByteBufBsonDocument;
import com.mongodb.internal.connection.CompositeByteBuf;
import com.mongodb.internal.connection.ResponseBuffers;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.StringWriter;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import org.bson.BsonBinaryReader;
import org.bson.BsonDocument;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.ByteBuf;
import org.bson.ByteBufNIO;
import org.bson.RawBsonDocument;
import org.bson.codecs.BsonDocumentCodec;
import org.bson.codecs.DecoderContext;
import org.bson.io.ByteBufferBsonInput;
import org.bson.json.JsonWriter;
import org.bson.json.JsonWriterSettings;

class ByteBufBsonDocument
extends AbstractByteBufBsonDocument {
    private static final long serialVersionUID = 2L;
    private final transient ByteBuf byteBuf;

    static List<ByteBufBsonDocument> createList(ResponseBuffers responseBuffers) {
        int numDocuments = responseBuffers.getReplyHeader().getNumberReturned();
        ByteBuf documentsBuffer = responseBuffers.getBodyByteBuffer();
        documentsBuffer.order(ByteOrder.LITTLE_ENDIAN);
        ArrayList<ByteBufBsonDocument> documents = new ArrayList<ByteBufBsonDocument>(numDocuments);
        while (documents.size() < numDocuments) {
            int documentSizeInBytes = documentsBuffer.getInt();
            documentsBuffer.position(documentsBuffer.position() - 4);
            ByteBuf documentBuffer = documentsBuffer.duplicate();
            documentBuffer.limit(documentBuffer.position() + documentSizeInBytes);
            documents.add(new ByteBufBsonDocument(new ByteBufNIO(documentBuffer.asNIO())));
            documentBuffer.release();
            documentsBuffer.position(documentsBuffer.position() + documentSizeInBytes);
        }
        return documents;
    }

    static List<ByteBufBsonDocument> createList(ByteBufferBsonOutput bsonOutput, int startPosition) {
        List<ByteBuf> duplicateByteBuffers = bsonOutput.getByteBuffers();
        CompositeByteBuf outputByteBuf = new CompositeByteBuf(duplicateByteBuffers);
        outputByteBuf.position(startPosition);
        ArrayList<ByteBufBsonDocument> documents = new ArrayList<ByteBufBsonDocument>();
        int curDocumentStartPosition = startPosition;
        while (outputByteBuf.hasRemaining()) {
            int documentSizeInBytes = outputByteBuf.getInt();
            ByteBuf slice = outputByteBuf.duplicate();
            slice.position(curDocumentStartPosition);
            slice.limit(curDocumentStartPosition + documentSizeInBytes);
            documents.add(new ByteBufBsonDocument(slice));
            curDocumentStartPosition += documentSizeInBytes;
            outputByteBuf.position(outputByteBuf.position() + documentSizeInBytes - 4);
        }
        for (ByteBuf byteBuffer : duplicateByteBuffers) {
            byteBuffer.release();
        }
        return documents;
    }

    static ByteBufBsonDocument createOne(ByteBufferBsonOutput bsonOutput, int startPosition) {
        List<ByteBuf> duplicateByteBuffers = bsonOutput.getByteBuffers();
        CompositeByteBuf outputByteBuf = new CompositeByteBuf(duplicateByteBuffers);
        outputByteBuf.position(startPosition);
        int documentSizeInBytes = outputByteBuf.getInt();
        ByteBuf slice = outputByteBuf.duplicate();
        slice.position(startPosition);
        slice.limit(startPosition + documentSizeInBytes);
        for (ByteBuf byteBuffer : duplicateByteBuffers) {
            byteBuffer.release();
        }
        return new ByteBufBsonDocument(slice);
    }

    @Override
    public String toJson() {
        return this.toJson(new JsonWriterSettings());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public String toJson(JsonWriterSettings settings) {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter, settings);
        ByteBuf duplicate = this.byteBuf.duplicate();
        BsonBinaryReader reader = new BsonBinaryReader(new ByteBufferBsonInput(duplicate));
        try {
            jsonWriter.pipe(reader);
            String string = stringWriter.toString();
            return string;
        } finally {
            duplicate.release();
            reader.close();
        }
    }

    @Override
    public BsonReader asBsonReader() {
        return new BsonBinaryReader(new ByteBufferBsonInput(this.byteBuf.duplicate()));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    <T> T findInDocument(AbstractByteBufBsonDocument.Finder<T> finder) {
        ByteBuf duplicateByteBuf = this.byteBuf.duplicate();
        BsonBinaryReader bsonReader = new BsonBinaryReader(new ByteBufferBsonInput(this.byteBuf.duplicate()));
        try {
            bsonReader.readStartDocument();
            while (bsonReader.readBsonType() != BsonType.END_OF_DOCUMENT) {
                T found = finder.find(bsonReader);
                if (found == null) continue;
                T t2 = found;
                return t2;
            }
            bsonReader.readEndDocument();
        } finally {
            duplicateByteBuf.release();
            bsonReader.close();
        }
        return finder.notFound();
    }

    @Override
    public BsonDocument clone() {
        byte[] clonedBytes = new byte[this.byteBuf.remaining()];
        this.byteBuf.get(this.byteBuf.position(), clonedBytes);
        return new RawBsonDocument(clonedBytes);
    }

    int getSizeInBytes() {
        return this.byteBuf.getInt(this.byteBuf.position());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    BsonDocument toBsonDocument() {
        ByteBuf duplicateByteBuf = this.byteBuf.duplicate();
        BsonBinaryReader bsonReader = new BsonBinaryReader(new ByteBufferBsonInput(duplicateByteBuf));
        try {
            BsonDocument bsonDocument = new BsonDocumentCodec().decode(bsonReader, DecoderContext.builder().build());
            return bsonDocument;
        } finally {
            duplicateByteBuf.release();
            bsonReader.close();
        }
    }

    ByteBufBsonDocument(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    private void readObject(ObjectInputStream stream) throws InvalidObjectException {
        throw new InvalidObjectException("Proxy required");
    }
}

