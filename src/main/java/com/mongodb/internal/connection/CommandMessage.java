/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoClientException;
import com.mongodb.MongoNamespace;
import com.mongodb.ReadPreference;
import com.mongodb.assertions.Assertions;
import com.mongodb.connection.ByteBufferBsonOutput;
import com.mongodb.connection.ClusterConnectionMode;
import com.mongodb.connection.ServerType;
import com.mongodb.connection.SplittablePayload;
import com.mongodb.internal.connection.BsonWriterHelper;
import com.mongodb.internal.connection.ByteBufBsonDocument;
import com.mongodb.internal.connection.MessageSettings;
import com.mongodb.internal.connection.OpCode;
import com.mongodb.internal.connection.ReadConcernHelper;
import com.mongodb.internal.connection.RequestMessage;
import com.mongodb.internal.connection.SplittablePayloadBsonWriter;
import com.mongodb.internal.validator.MappedFieldNameValidator;
import com.mongodb.session.SessionContext;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bson.BsonArray;
import org.bson.BsonBinaryWriter;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonElement;
import org.bson.BsonInt64;
import org.bson.BsonString;
import org.bson.FieldNameValidator;
import org.bson.codecs.EncoderContext;
import org.bson.io.BsonOutput;

public final class CommandMessage
extends RequestMessage {
    private final MongoNamespace namespace;
    private final BsonDocument command;
    private final FieldNameValidator commandFieldNameValidator;
    private final ReadPreference readPreference;
    private final SplittablePayload payload;
    private final FieldNameValidator payloadFieldNameValidator;
    private final boolean responseExpected;
    private final ClusterConnectionMode clusterConnectionMode;

    CommandMessage(MongoNamespace namespace, BsonDocument command, FieldNameValidator commandFieldNameValidator, ReadPreference readPreference, MessageSettings settings) {
        this(namespace, command, commandFieldNameValidator, readPreference, settings, true, null, null, ClusterConnectionMode.MULTIPLE);
    }

    CommandMessage(MongoNamespace namespace, BsonDocument command, FieldNameValidator commandFieldNameValidator, ReadPreference readPreference, MessageSettings settings, boolean responseExpected, SplittablePayload payload, FieldNameValidator payloadFieldNameValidator, ClusterConnectionMode clusterConnectionMode) {
        super(namespace.getFullName(), CommandMessage.getOpCode(settings), settings);
        this.namespace = namespace;
        this.command = command;
        this.commandFieldNameValidator = commandFieldNameValidator;
        this.readPreference = readPreference;
        this.responseExpected = responseExpected;
        this.payload = payload;
        this.payloadFieldNameValidator = payloadFieldNameValidator;
        this.clusterConnectionMode = clusterConnectionMode;
    }

    BsonDocument getCommandDocument(ByteBufferBsonOutput bsonOutput) {
        BsonDocument commandBsonDocument;
        ByteBufBsonDocument byteBufBsonDocument = ByteBufBsonDocument.createOne(bsonOutput, this.getEncodingMetadata().getFirstDocumentPosition());
        if (this.useOpMsg() && this.containsPayload()) {
            commandBsonDocument = byteBufBsonDocument.toBsonDocument();
            int payloadStartPosition = this.getEncodingMetadata().getFirstDocumentPosition() + byteBufBsonDocument.getSizeInBytes() + 1 + 4 + this.payload.getPayloadName().getBytes(Charset.forName("UTF-8")).length + 1;
            commandBsonDocument.append(this.payload.getPayloadName(), new BsonArray(ByteBufBsonDocument.createList(bsonOutput, payloadStartPosition)));
        } else {
            commandBsonDocument = byteBufBsonDocument;
        }
        if (commandBsonDocument.containsKey("$query")) {
            commandBsonDocument = commandBsonDocument.getDocument("$query");
        }
        return commandBsonDocument;
    }

    boolean containsPayload() {
        return this.payload != null;
    }

    boolean isResponseExpected() {
        Assertions.isTrue("The message must be encoded before determining if a response is expected", this.getEncodingMetadata() != null);
        return !this.useOpMsg() || this.requireOpMsgResponse();
    }

    MongoNamespace getNamespace() {
        return this.namespace;
    }

    @Override
    protected RequestMessage.EncodingMetadata encodeMessageBodyWithMetadata(BsonOutput bsonOutput, SessionContext sessionContext) {
        int commandStartPosition;
        int messageStartPosition = bsonOutput.getPosition() - 16;
        if (this.useOpMsg()) {
            int flagPosition = bsonOutput.getPosition();
            bsonOutput.writeInt32(0);
            bsonOutput.writeByte(0);
            commandStartPosition = bsonOutput.getPosition();
            this.addDocument(this.getCommandToEncode(), bsonOutput, this.commandFieldNameValidator, this.getExtraElements(sessionContext));
            if (this.payload != null) {
                bsonOutput.writeByte(1);
                int payloadBsonOutputStartPosition = bsonOutput.getPosition();
                bsonOutput.writeInt32(0);
                bsonOutput.writeCString(this.payload.getPayloadName());
                BsonWriterHelper.writePayload(new BsonBinaryWriter(bsonOutput, this.payloadFieldNameValidator), bsonOutput, this.getSettings(), messageStartPosition, this.payload, this.getSettings().getMaxDocumentSize());
                int payloadBsonOutputLength = bsonOutput.getPosition() - payloadBsonOutputStartPosition;
                bsonOutput.writeInt32(payloadBsonOutputStartPosition, payloadBsonOutputLength);
            }
            bsonOutput.writeInt32(flagPosition, this.getOpMsgFlagBits());
        } else {
            bsonOutput.writeInt32(this.getOpQueryFlagBits());
            bsonOutput.writeCString(this.namespace.getFullName());
            bsonOutput.writeInt32(0);
            bsonOutput.writeInt32(-1);
            commandStartPosition = bsonOutput.getPosition();
            if (this.payload == null) {
                this.addDocument(this.getCommandToEncode(), bsonOutput, this.commandFieldNameValidator, null);
            } else {
                this.addDocumentWithPayload(bsonOutput, messageStartPosition);
            }
        }
        return new RequestMessage.EncodingMetadata(commandStartPosition);
    }

    private FieldNameValidator getPayloadArrayFieldNameValidator() {
        HashMap<String, FieldNameValidator> rootMap = new HashMap<String, FieldNameValidator>();
        rootMap.put(this.payload.getPayloadName(), this.payloadFieldNameValidator);
        return new MappedFieldNameValidator(this.commandFieldNameValidator, rootMap);
    }

    private void addDocumentWithPayload(BsonOutput bsonOutput, int messageStartPosition) {
        BsonBinaryWriter bsonBinaryWriter = new BsonBinaryWriter(bsonOutput, this.getPayloadArrayFieldNameValidator());
        SplittablePayloadBsonWriter bsonWriter = new SplittablePayloadBsonWriter(bsonBinaryWriter, bsonOutput, messageStartPosition, this.getSettings(), this.payload);
        BsonDocument commandToEncode = this.getCommandToEncode();
        this.getCodec(commandToEncode).encode(bsonWriter, commandToEncode, EncoderContext.builder().build());
    }

    private int getOpMsgFlagBits() {
        return this.getOpMsgResponseExpectedFlagBit();
    }

    private int getOpMsgResponseExpectedFlagBit() {
        if (this.requireOpMsgResponse()) {
            return 0;
        }
        return 2;
    }

    private boolean requireOpMsgResponse() {
        if (this.responseExpected) {
            return true;
        }
        return this.payload != null && this.payload.hasAnotherSplit();
    }

    private int getOpQueryFlagBits() {
        return this.getOpQuerySlaveOkFlagBit();
    }

    private int getOpQuerySlaveOkFlagBit() {
        if (this.isSlaveOk()) {
            return 4;
        }
        return 0;
    }

    private boolean isSlaveOk() {
        return this.readPreference != null && this.readPreference.isSlaveOk() || this.isDirectConnectionToReplicaSetMember();
    }

    private boolean isDirectConnectionToReplicaSetMember() {
        return this.clusterConnectionMode == ClusterConnectionMode.SINGLE && this.getSettings().getServerType() != ServerType.SHARD_ROUTER && this.getSettings().getServerType() != ServerType.STANDALONE;
    }

    private boolean useOpMsg() {
        return this.getOpCode().equals((Object)OpCode.OP_MSG);
    }

    private BsonDocument getCommandToEncode() {
        BsonDocument commandToEncode = this.command;
        if (!this.useOpMsg() && this.readPreference != null && !this.readPreference.equals(ReadPreference.primary())) {
            commandToEncode = new BsonDocument("$query", this.command).append("$readPreference", this.readPreference.toDocument());
        }
        return commandToEncode;
    }

    private List<BsonElement> getExtraElements(SessionContext sessionContext) {
        ArrayList<BsonElement> extraElements = new ArrayList<BsonElement>();
        extraElements.add(new BsonElement("$db", new BsonString(new MongoNamespace(this.getCollectionName()).getDatabaseName())));
        if (sessionContext.getClusterTime() != null) {
            extraElements.add(new BsonElement("$clusterTime", sessionContext.getClusterTime()));
        }
        if (sessionContext.hasSession() && this.responseExpected) {
            extraElements.add(new BsonElement("lsid", sessionContext.getSessionId()));
        }
        boolean firstMessageInTransaction = sessionContext.notifyMessageSent();
        if (sessionContext.hasActiveTransaction()) {
            this.checkServerVersionForTransactionSupport();
            extraElements.add(new BsonElement("txnNumber", new BsonInt64(sessionContext.getTransactionNumber())));
            if (firstMessageInTransaction) {
                extraElements.add(new BsonElement("startTransaction", BsonBoolean.TRUE));
                this.addReadConcernDocument(extraElements, sessionContext);
            }
            extraElements.add(new BsonElement("autocommit", BsonBoolean.FALSE));
        }
        if (this.readPreference != null) {
            if (!this.readPreference.equals(ReadPreference.primary())) {
                extraElements.add(new BsonElement("$readPreference", this.readPreference.toDocument()));
            } else if (this.isDirectConnectionToReplicaSetMember()) {
                extraElements.add(new BsonElement("$readPreference", ReadPreference.primaryPreferred().toDocument()));
            }
        }
        return extraElements;
    }

    private void checkServerVersionForTransactionSupport() {
        int wireVersion = this.getSettings().getMaxWireVersion();
        if (wireVersion < 7 || wireVersion < 8 && this.getSettings().getServerType() == ServerType.SHARD_ROUTER) {
            throw new MongoClientException("Transactions are not supported by the MongoDB cluster to which this client is connected.");
        }
    }

    private void addReadConcernDocument(List<BsonElement> extraElements, SessionContext sessionContext) {
        BsonDocument readConcernDocument = ReadConcernHelper.getReadConcernDocument(sessionContext);
        if (!readConcernDocument.isEmpty()) {
            extraElements.add(new BsonElement("readConcern", readConcernDocument));
        }
    }

    private static OpCode getOpCode(MessageSettings settings) {
        return CommandMessage.isServerVersionAtLeastThreeDotSix(settings) ? OpCode.OP_MSG : OpCode.OP_QUERY;
    }

    private static boolean isServerVersionAtLeastThreeDotSix(MessageSettings settings) {
        return settings.getMaxWireVersion() >= 6;
    }
}

