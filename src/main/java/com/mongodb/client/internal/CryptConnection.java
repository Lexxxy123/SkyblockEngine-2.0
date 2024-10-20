/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.internal;

import com.mongodb.MongoClientException;
import com.mongodb.MongoNamespace;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcernResult;
import com.mongodb.bulk.DeleteRequest;
import com.mongodb.bulk.InsertRequest;
import com.mongodb.bulk.UpdateRequest;
import com.mongodb.client.internal.Crypt;
import com.mongodb.connection.Connection;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.connection.QueryResult;
import com.mongodb.connection.SplittablePayload;
import com.mongodb.internal.connection.MessageSettings;
import com.mongodb.internal.connection.SplittablePayloadBsonWriter;
import com.mongodb.internal.operation.ServerVersionHelper;
import com.mongodb.internal.validator.MappedFieldNameValidator;
import com.mongodb.lang.Nullable;
import com.mongodb.session.SessionContext;
import java.util.HashMap;
import java.util.List;
import org.bson.BsonBinaryReader;
import org.bson.BsonBinaryWriter;
import org.bson.BsonBinaryWriterSettings;
import org.bson.BsonDocument;
import org.bson.BsonWriter;
import org.bson.BsonWriterSettings;
import org.bson.FieldNameValidator;
import org.bson.RawBsonDocument;
import org.bson.codecs.BsonValueCodecProvider;
import org.bson.codecs.Codec;
import org.bson.codecs.Decoder;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.RawBsonDocumentCodec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.io.BasicOutputBuffer;
import org.bson.io.BsonOutput;

class CryptConnection
implements Connection {
    private static final CodecRegistry REGISTRY = CodecRegistries.fromProviders(new BsonValueCodecProvider());
    private static final int MAX_SPLITTABLE_DOCUMENT_SIZE = 0x200000;
    private final Connection wrapped;
    private final Crypt crypt;

    CryptConnection(Connection wrapped, Crypt crypt) {
        this.wrapped = wrapped;
        this.crypt = crypt;
    }

    @Override
    public int getCount() {
        return this.wrapped.getCount();
    }

    @Override
    public CryptConnection retain() {
        this.wrapped.retain();
        return this;
    }

    @Override
    public void release() {
        this.wrapped.release();
    }

    @Override
    public ConnectionDescription getDescription() {
        return this.wrapped.getDescription();
    }

    @Override
    public <T> T command(String database, BsonDocument command, FieldNameValidator commandFieldNameValidator, ReadPreference readPreference, Decoder<T> commandResultDecoder, SessionContext sessionContext, boolean responseExpected, @Nullable SplittablePayload payload, @Nullable FieldNameValidator payloadFieldNameValidator) {
        if (ServerVersionHelper.serverIsLessThanVersionFourDotTwo(this.wrapped.getDescription())) {
            throw new MongoClientException("Auto-encryption requires a minimum MongoDB version of 4.2");
        }
        BasicOutputBuffer bsonOutput = new BasicOutputBuffer();
        BsonBinaryWriter bsonBinaryWriter = new BsonBinaryWriter(new BsonWriterSettings(), new BsonBinaryWriterSettings(this.getDescription().getMaxDocumentSize()), bsonOutput, this.getFieldNameValidator(payload, commandFieldNameValidator, payloadFieldNameValidator));
        BsonWriter writer = payload == null ? bsonBinaryWriter : new SplittablePayloadBsonWriter(bsonBinaryWriter, (BsonOutput)bsonOutput, this.createSplittablePayloadMessageSettings(), payload, 0x200000);
        this.getEncoder(command).encode(writer, command, EncoderContext.builder().build());
        RawBsonDocument encryptedCommand = this.crypt.encrypt(database, new RawBsonDocument(bsonOutput.getInternalBuffer(), 0, bsonOutput.getSize()));
        RawBsonDocument encryptedResponse = this.wrapped.command(database, encryptedCommand, commandFieldNameValidator, readPreference, new RawBsonDocumentCodec(), sessionContext, responseExpected, null, null);
        RawBsonDocument decryptedResponse = this.crypt.decrypt(encryptedResponse);
        BsonBinaryReader reader = new BsonBinaryReader(decryptedResponse.getByteBuffer().asNIO());
        return commandResultDecoder.decode(reader, DecoderContext.builder().build());
    }

    @Override
    public <T> T command(String database, BsonDocument command, FieldNameValidator fieldNameValidator, ReadPreference readPreference, Decoder<T> commandResultDecoder, SessionContext sessionContext) {
        return this.command(database, command, fieldNameValidator, readPreference, commandResultDecoder, sessionContext, true, null, null);
    }

    private Codec<BsonDocument> getEncoder(BsonDocument command) {
        return REGISTRY.get(command.getClass());
    }

    private FieldNameValidator getFieldNameValidator(@Nullable SplittablePayload payload, FieldNameValidator commandFieldNameValidator, @Nullable FieldNameValidator payloadFieldNameValidator) {
        if (payload == null) {
            return commandFieldNameValidator;
        }
        HashMap<String, FieldNameValidator> rootMap = new HashMap<String, FieldNameValidator>();
        rootMap.put(payload.getPayloadName(), payloadFieldNameValidator);
        return new MappedFieldNameValidator(commandFieldNameValidator, rootMap);
    }

    private MessageSettings createSplittablePayloadMessageSettings() {
        return MessageSettings.builder().maxBatchCount(this.getDescription().getMaxBatchCount()).maxMessageSize(this.getDescription().getMaxMessageSize()).maxDocumentSize(this.getDescription().getMaxDocumentSize()).build();
    }

    @Override
    public <T> T command(String database, BsonDocument command, boolean slaveOk, FieldNameValidator fieldNameValidator, Decoder<T> commandResultDecoder) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WriteConcernResult insert(MongoNamespace namespace, boolean ordered, InsertRequest insertRequest) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WriteConcernResult update(MongoNamespace namespace, boolean ordered, UpdateRequest updateRequest) {
        throw new UnsupportedOperationException();
    }

    @Override
    public WriteConcernResult delete(MongoNamespace namespace, boolean ordered, DeleteRequest deleteRequest) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> QueryResult<T> query(MongoNamespace namespace, BsonDocument queryDocument, BsonDocument fields, int numberToReturn, int skip, boolean slaveOk, boolean tailableCursor, boolean awaitData, boolean noCursorTimeout, boolean partial, boolean oplogReplay, Decoder<T> resultDecoder) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> QueryResult<T> query(MongoNamespace namespace, BsonDocument queryDocument, BsonDocument fields, int skip, int limit, int batchSize, boolean slaveOk, boolean tailableCursor, boolean awaitData, boolean noCursorTimeout, boolean partial, boolean oplogReplay, Decoder<T> resultDecoder) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> QueryResult<T> getMore(MongoNamespace namespace, long cursorId, int numberToReturn, Decoder<T> resultDecoder) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void killCursor(List<Long> cursors) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void killCursor(MongoNamespace namespace, List<Long> cursors) {
        throw new UnsupportedOperationException();
    }
}

