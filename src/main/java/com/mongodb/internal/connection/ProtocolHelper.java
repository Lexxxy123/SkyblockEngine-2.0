/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.DuplicateKeyException;
import com.mongodb.ErrorCategory;
import com.mongodb.MongoCommandException;
import com.mongodb.MongoException;
import com.mongodb.MongoExecutionTimeoutException;
import com.mongodb.MongoNodeIsRecoveringException;
import com.mongodb.MongoNotPrimaryException;
import com.mongodb.MongoQueryException;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcernException;
import com.mongodb.WriteConcernResult;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.diagnostics.logging.Logger;
import com.mongodb.diagnostics.logging.Loggers;
import com.mongodb.event.CommandFailedEvent;
import com.mongodb.event.CommandListener;
import com.mongodb.event.CommandStartedEvent;
import com.mongodb.event.CommandSucceededEvent;
import com.mongodb.internal.connection.MessageSettings;
import com.mongodb.internal.connection.NoOpSessionContext;
import com.mongodb.internal.connection.RequestMessage;
import com.mongodb.internal.connection.ResponseBuffers;
import java.util.Arrays;
import java.util.List;
import org.bson.BsonBinaryReader;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonReader;
import org.bson.BsonString;
import org.bson.BsonTimestamp;
import org.bson.BsonType;
import org.bson.BsonValue;
import org.bson.codecs.BsonValueCodecProvider;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.io.BsonOutput;
import org.bson.io.ByteBufferBsonInput;

public final class ProtocolHelper {
    private static final Logger PROTOCOL_EVENT_LOGGER = Loggers.getLogger("protocol.event");
    private static final CodecRegistry REGISTRY = CodecRegistries.fromProviders(new BsonValueCodecProvider());
    private static final List<Integer> NOT_MASTER_CODES = Arrays.asList(10107, 13435);
    private static final List<Integer> RECOVERING_CODES = Arrays.asList(11600, 11602, 13436, 189, 91);

    private static WriteConcernResult createWriteResult(BsonDocument result) {
        BsonBoolean updatedExisting = result.getBoolean("updatedExisting", BsonBoolean.FALSE);
        return WriteConcernResult.acknowledged(result.getNumber("n", new BsonInt32(0)).intValue(), updatedExisting.getValue(), result.get("upserted"));
    }

    static boolean isCommandOk(BsonDocument response) {
        BsonValue okValue = response.get("ok");
        return ProtocolHelper.isCommandOk(okValue);
    }

    static boolean isCommandOk(BsonReader bsonReader) {
        return ProtocolHelper.isCommandOk(ProtocolHelper.getField(bsonReader, "ok"));
    }

    static boolean isCommandOk(ResponseBuffers responseBuffers) {
        try {
            boolean bl2 = ProtocolHelper.isCommandOk(ProtocolHelper.createBsonReader(responseBuffers));
            return bl2;
        } finally {
            responseBuffers.reset();
        }
    }

    static MongoException createSpecialWriteConcernException(ResponseBuffers responseBuffers, ServerAddress serverAddress) {
        BsonValue writeConcernError = ProtocolHelper.getField(ProtocolHelper.createBsonReader(responseBuffers), "writeConcernError");
        if (writeConcernError == null) {
            return null;
        }
        return ProtocolHelper.createSpecialException(writeConcernError.asDocument(), serverAddress, "errmsg");
    }

    static BsonTimestamp getOperationTime(ResponseBuffers responseBuffers) {
        try {
            BsonValue operationTime = ProtocolHelper.getField(ProtocolHelper.createBsonReader(responseBuffers), "operationTime");
            if (operationTime == null) {
                BsonTimestamp bsonTimestamp = null;
                return bsonTimestamp;
            }
            BsonTimestamp bsonTimestamp = operationTime.asTimestamp();
            return bsonTimestamp;
        } finally {
            responseBuffers.reset();
        }
    }

    static BsonDocument getClusterTime(ResponseBuffers responseBuffers) {
        return ProtocolHelper.getFieldDocument(responseBuffers, "$clusterTime");
    }

    static BsonDocument getClusterTime(BsonDocument response) {
        BsonValue clusterTime = response.get("$clusterTime");
        if (clusterTime == null) {
            return null;
        }
        return clusterTime.asDocument();
    }

    static BsonDocument getRecoveryToken(ResponseBuffers responseBuffers) {
        return ProtocolHelper.getFieldDocument(responseBuffers, "recoveryToken");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static BsonDocument getFieldDocument(ResponseBuffers responseBuffers, String fieldName) {
        try {
            BsonValue fieldValue = ProtocolHelper.getField(ProtocolHelper.createBsonReader(responseBuffers), fieldName);
            if (fieldValue == null) {
                BsonDocument bsonDocument = null;
                return bsonDocument;
            }
            BsonDocument bsonDocument = fieldValue.asDocument();
            return bsonDocument;
        } finally {
            responseBuffers.reset();
        }
    }

    private static BsonBinaryReader createBsonReader(ResponseBuffers responseBuffers) {
        return new BsonBinaryReader(new ByteBufferBsonInput(responseBuffers.getBodyByteBuffer()));
    }

    private static BsonValue getField(BsonReader bsonReader, String fieldName) {
        bsonReader.readStartDocument();
        while (bsonReader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            if (bsonReader.readName().equals(fieldName)) {
                return (BsonValue)REGISTRY.get(BsonValueCodecProvider.getClassForBsonType(bsonReader.getCurrentBsonType())).decode(bsonReader, DecoderContext.builder().build());
            }
            bsonReader.skipValue();
        }
        bsonReader.readEndDocument();
        return null;
    }

    private static boolean isCommandOk(BsonValue okValue) {
        if (okValue == null) {
            return false;
        }
        if (okValue.isBoolean()) {
            return okValue.asBoolean().getValue();
        }
        if (okValue.isNumber()) {
            return okValue.asNumber().intValue() == 1;
        }
        return false;
    }

    static MongoException getCommandFailureException(BsonDocument response, ServerAddress serverAddress) {
        MongoException specialException = ProtocolHelper.createSpecialException(response, serverAddress, "errmsg");
        if (specialException != null) {
            return specialException;
        }
        return new MongoCommandException(response, serverAddress);
    }

    static int getErrorCode(BsonDocument response) {
        return response.getNumber("code", new BsonInt32(-1)).intValue();
    }

    static String getErrorMessage(BsonDocument response, String errorMessageFieldName) {
        return response.getString(errorMessageFieldName, new BsonString("")).getValue();
    }

    static MongoException getQueryFailureException(BsonDocument errorDocument, ServerAddress serverAddress) {
        MongoException specialException = ProtocolHelper.createSpecialException(errorDocument, serverAddress, "$err");
        if (specialException != null) {
            return specialException;
        }
        return new MongoQueryException(serverAddress, ProtocolHelper.getErrorCode(errorDocument), ProtocolHelper.getErrorMessage(errorDocument, "$err"));
    }

    static MessageSettings getMessageSettings(ConnectionDescription connectionDescription) {
        return MessageSettings.builder().maxDocumentSize(connectionDescription.getMaxDocumentSize()).maxMessageSize(connectionDescription.getMaxMessageSize()).maxBatchCount(connectionDescription.getMaxBatchCount()).maxWireVersion(connectionDescription.getMaxWireVersion()).serverType(connectionDescription.getServerType()).build();
    }

    static void encodeMessage(RequestMessage message, BsonOutput bsonOutput) {
        try {
            message.encode(bsonOutput, NoOpSessionContext.INSTANCE);
        } catch (RuntimeException e2) {
            bsonOutput.close();
            throw e2;
        } catch (Error e3) {
            bsonOutput.close();
            throw e3;
        }
    }

    static RequestMessage.EncodingMetadata encodeMessageWithMetadata(RequestMessage message, BsonOutput bsonOutput) {
        try {
            message.encode(bsonOutput, NoOpSessionContext.INSTANCE);
            return message.getEncodingMetadata();
        } catch (RuntimeException e2) {
            bsonOutput.close();
            throw e2;
        } catch (Error e3) {
            bsonOutput.close();
            throw e3;
        }
    }

    public static MongoException createSpecialException(BsonDocument response, ServerAddress serverAddress, String errorMessageFieldName) {
        if (response == null) {
            return null;
        }
        int errorCode = ProtocolHelper.getErrorCode(response);
        String errorMessage = ProtocolHelper.getErrorMessage(response, errorMessageFieldName);
        if (ErrorCategory.fromErrorCode(errorCode) == ErrorCategory.EXECUTION_TIMEOUT) {
            return new MongoExecutionTimeoutException(errorCode, errorMessage);
        }
        if (errorMessage.contains("not master or secondary") || errorMessage.contains("node is recovering") || RECOVERING_CODES.contains(errorCode)) {
            return new MongoNodeIsRecoveringException(response, serverAddress);
        }
        if (errorMessage.contains("not master") || NOT_MASTER_CODES.contains(errorCode)) {
            return new MongoNotPrimaryException(response, serverAddress);
        }
        if (response.containsKey("writeConcernError")) {
            MongoException writeConcernException = ProtocolHelper.createSpecialException(response.getDocument("writeConcernError"), serverAddress, "errmsg");
            if (writeConcernException != null && response.isArray("errorLabels")) {
                for (BsonValue errorLabel : response.getArray("errorLabels")) {
                    writeConcernException.addLabel(errorLabel.asString().getValue());
                }
            }
            return writeConcernException;
        }
        return null;
    }

    private static boolean hasWriteError(BsonDocument response) {
        String err = WriteConcernException.extractErrorMessage(response);
        return err != null && err.length() > 0;
    }

    private static void throwWriteException(BsonDocument result, ServerAddress serverAddress) {
        MongoException specialException = ProtocolHelper.createSpecialException(result, serverAddress, "err");
        if (specialException != null) {
            throw specialException;
        }
        int code = WriteConcernException.extractErrorCode(result);
        if (ErrorCategory.fromErrorCode(code) == ErrorCategory.DUPLICATE_KEY) {
            throw new DuplicateKeyException(result, serverAddress, ProtocolHelper.createWriteResult(result));
        }
        throw new WriteConcernException(result, serverAddress, ProtocolHelper.createWriteResult(result));
    }

    static void sendCommandStartedEvent(RequestMessage message, String databaseName, String commandName, BsonDocument command, ConnectionDescription connectionDescription, CommandListener commandListener) {
        block2: {
            try {
                commandListener.commandStarted(new CommandStartedEvent(message.getId(), connectionDescription, databaseName, commandName, command));
            } catch (Exception e2) {
                if (!PROTOCOL_EVENT_LOGGER.isWarnEnabled()) break block2;
                PROTOCOL_EVENT_LOGGER.warn(String.format("Exception thrown raising command started event to listener %s", commandListener), e2);
            }
        }
    }

    static void sendCommandSucceededEvent(RequestMessage message, String commandName, BsonDocument response, ConnectionDescription connectionDescription, long elapsedTimeNanos, CommandListener commandListener) {
        block2: {
            try {
                commandListener.commandSucceeded(new CommandSucceededEvent(message.getId(), connectionDescription, commandName, response, elapsedTimeNanos));
            } catch (Exception e2) {
                if (!PROTOCOL_EVENT_LOGGER.isWarnEnabled()) break block2;
                PROTOCOL_EVENT_LOGGER.warn(String.format("Exception thrown raising command succeeded event to listener %s", commandListener), e2);
            }
        }
    }

    static void sendCommandFailedEvent(RequestMessage message, String commandName, ConnectionDescription connectionDescription, long elapsedTimeNanos, Throwable throwable, CommandListener commandListener) {
        block2: {
            try {
                commandListener.commandFailed(new CommandFailedEvent(message.getId(), connectionDescription, commandName, elapsedTimeNanos, throwable));
            } catch (Exception e2) {
                if (!PROTOCOL_EVENT_LOGGER.isWarnEnabled()) break block2;
                PROTOCOL_EVENT_LOGGER.warn(String.format("Exception thrown raising command failed event to listener %s", commandListener), e2);
            }
        }
    }

    private ProtocolHelper() {
    }
}

