/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoClientException;
import com.mongodb.MongoCompressor;
import com.mongodb.MongoException;
import com.mongodb.MongoInternalException;
import com.mongodb.MongoInterruptedException;
import com.mongodb.MongoSocketClosedException;
import com.mongodb.MongoSocketReadException;
import com.mongodb.MongoSocketReadTimeoutException;
import com.mongodb.MongoSocketWriteException;
import com.mongodb.ServerAddress;
import com.mongodb.annotations.NotThreadSafe;
import com.mongodb.assertions.Assertions;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.connection.AsyncCompletionHandler;
import com.mongodb.connection.ByteBufferBsonOutput;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.connection.ConnectionId;
import com.mongodb.connection.ServerId;
import com.mongodb.connection.Stream;
import com.mongodb.connection.StreamFactory;
import com.mongodb.diagnostics.logging.Logger;
import com.mongodb.diagnostics.logging.Loggers;
import com.mongodb.event.CommandListener;
import com.mongodb.internal.async.ErrorHandlingResultCallback;
import com.mongodb.internal.connection.CommandEventSender;
import com.mongodb.internal.connection.CommandMessage;
import com.mongodb.internal.connection.CompressedHeader;
import com.mongodb.internal.connection.CompressedMessage;
import com.mongodb.internal.connection.Compressor;
import com.mongodb.internal.connection.InternalConnection;
import com.mongodb.internal.connection.InternalConnectionInitializer;
import com.mongodb.internal.connection.LoggingCommandEventSender;
import com.mongodb.internal.connection.MessageHeader;
import com.mongodb.internal.connection.MongoWriteConcernWithResponseException;
import com.mongodb.internal.connection.NoOpCommandEventSender;
import com.mongodb.internal.connection.OpCode;
import com.mongodb.internal.connection.ProtocolHelper;
import com.mongodb.internal.connection.ReplyHeader;
import com.mongodb.internal.connection.ReplyMessage;
import com.mongodb.internal.connection.ResponseBuffers;
import com.mongodb.internal.connection.SnappyCompressor;
import com.mongodb.internal.connection.ZlibCompressor;
import com.mongodb.internal.connection.ZstdCompressor;
import com.mongodb.session.SessionContext;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.nio.channels.ClosedByInterruptException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import org.bson.BsonBinaryReader;
import org.bson.BsonDocument;
import org.bson.ByteBuf;
import org.bson.codecs.BsonDocumentCodec;
import org.bson.codecs.Decoder;
import org.bson.io.ByteBufferBsonInput;

@NotThreadSafe
public class InternalStreamConnection
implements InternalConnection {
    private static final Set<String> SECURITY_SENSITIVE_COMMANDS = new HashSet<String>(Arrays.asList("authenticate", "saslStart", "saslContinue", "getnonce", "createUser", "updateUser", "copydbgetnonce", "copydbsaslstart", "copydb"));
    private static final Logger LOGGER = Loggers.getLogger("connection");
    private final ServerId serverId;
    private final StreamFactory streamFactory;
    private final InternalConnectionInitializer connectionInitializer;
    private volatile ConnectionDescription description;
    private volatile Stream stream;
    private final AtomicBoolean isClosed = new AtomicBoolean();
    private final AtomicBoolean opened = new AtomicBoolean();
    private final List<MongoCompressor> compressorList;
    private final CommandListener commandListener;
    private volatile Compressor sendCompressor;
    private volatile Map<Byte, Compressor> compressorMap;
    private static final Logger COMMAND_PROTOCOL_LOGGER = Loggers.getLogger("protocol.command");

    public InternalStreamConnection(ServerId serverId, StreamFactory streamFactory, List<MongoCompressor> compressorList, CommandListener commandListener, InternalConnectionInitializer connectionInitializer) {
        this.serverId = Assertions.notNull("serverId", serverId);
        this.streamFactory = Assertions.notNull("streamFactory", streamFactory);
        this.compressorList = Assertions.notNull("compressorList", compressorList);
        this.compressorMap = this.createCompressorMap(compressorList);
        this.commandListener = commandListener;
        this.connectionInitializer = Assertions.notNull("connectionInitializer", connectionInitializer);
        this.description = new ConnectionDescription(serverId);
    }

    @Override
    public ConnectionDescription getDescription() {
        return this.description;
    }

    @Override
    public void open() {
        Assertions.isTrue("Open already called", this.stream == null);
        this.stream = this.streamFactory.create(this.serverId.getAddress());
        try {
            this.stream.open();
            this.description = this.connectionInitializer.initialize(this);
            this.opened.set(true);
            this.sendCompressor = this.findSendCompressor(this.description);
            LOGGER.info(String.format("Opened connection [%s] to %s", this.getId(), this.serverId.getAddress()));
        } catch (Throwable t2) {
            this.close();
            if (t2 instanceof MongoException) {
                throw (MongoException)t2;
            }
            throw new MongoException(t2.toString(), t2);
        }
    }

    @Override
    public void openAsync(final SingleResultCallback<Void> callback) {
        Assertions.isTrue("Open already called", this.stream == null, callback);
        try {
            this.stream = this.streamFactory.create(this.serverId.getAddress());
            this.stream.openAsync(new AsyncCompletionHandler<Void>(){

                @Override
                public void completed(Void aVoid) {
                    InternalStreamConnection.this.connectionInitializer.initializeAsync(InternalStreamConnection.this, new SingleResultCallback<ConnectionDescription>(){

                        @Override
                        public void onResult(ConnectionDescription result, Throwable t2) {
                            if (t2 != null) {
                                InternalStreamConnection.this.close();
                                callback.onResult(null, t2);
                            } else {
                                InternalStreamConnection.this.description = result;
                                InternalStreamConnection.this.opened.set(true);
                                InternalStreamConnection.this.sendCompressor = InternalStreamConnection.this.findSendCompressor(InternalStreamConnection.this.description);
                                if (LOGGER.isInfoEnabled()) {
                                    LOGGER.info(String.format("Opened connection [%s] to %s", InternalStreamConnection.this.getId(), InternalStreamConnection.this.serverId.getAddress()));
                                }
                                callback.onResult(null, null);
                            }
                        }
                    });
                }

                @Override
                public void failed(Throwable t2) {
                    callback.onResult(null, t2);
                }
            });
        } catch (Throwable t2) {
            callback.onResult(null, t2);
        }
    }

    private Map<Byte, Compressor> createCompressorMap(List<MongoCompressor> compressorList) {
        HashMap<Byte, Compressor> compressorMap = new HashMap<Byte, Compressor>(this.compressorList.size());
        for (MongoCompressor mongoCompressor : compressorList) {
            Compressor compressor = this.createCompressor(mongoCompressor);
            compressorMap.put(compressor.getId(), compressor);
        }
        return compressorMap;
    }

    private Compressor findSendCompressor(ConnectionDescription description) {
        if (description.getCompressors().isEmpty()) {
            return null;
        }
        String firstCompressorName = description.getCompressors().get(0);
        for (Compressor compressor : this.compressorMap.values()) {
            if (!compressor.getName().equals(firstCompressorName)) continue;
            return compressor;
        }
        throw new MongoInternalException("Unexpected compressor negotiated: " + firstCompressorName);
    }

    private Compressor createCompressor(MongoCompressor mongoCompressor) {
        if (mongoCompressor.getName().equals("zlib")) {
            return new ZlibCompressor(mongoCompressor);
        }
        if (mongoCompressor.getName().equals("snappy")) {
            return new SnappyCompressor();
        }
        if (mongoCompressor.getName().equals("zstd")) {
            return new ZstdCompressor();
        }
        throw new MongoClientException("Unsupported compressor " + mongoCompressor.getName());
    }

    @Override
    public void close() {
        if (!this.isClosed.getAndSet(true)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format("Closing connection %s", this.getId()));
            }
            if (this.stream != null) {
                this.stream.close();
            }
        }
    }

    @Override
    public boolean opened() {
        return this.opened.get();
    }

    @Override
    public boolean isClosed() {
        return this.isClosed.get();
    }

    @Override
    public <T> T sendAndReceive(CommandMessage message, Decoder<T> decoder, SessionContext sessionContext) {
        CommandEventSender commandEventSender;
        ByteBufferBsonOutput bsonOutput = new ByteBufferBsonOutput(this);
        try {
            message.encode(bsonOutput, sessionContext);
            commandEventSender = this.createCommandEventSender(message, bsonOutput);
            commandEventSender.sendStartedEvent();
        } catch (RuntimeException e2) {
            bsonOutput.close();
            throw e2;
        }
        try {
            this.sendCommandMessage(message, bsonOutput, sessionContext);
            if (message.isResponseExpected()) {
                return this.receiveCommandMessageResponse(message, decoder, commandEventSender, sessionContext);
            }
            commandEventSender.sendSucceededEventForOneWayCommand();
            return null;
        } catch (RuntimeException e3) {
            commandEventSender.sendFailedEvent(e3);
            throw e3;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void sendCommandMessage(CommandMessage message, ByteBufferBsonOutput bsonOutput, SessionContext sessionContext) {
        ByteBufferBsonOutput compressedBsonOutput;
        if (this.sendCompressor == null || SECURITY_SENSITIVE_COMMANDS.contains(message.getCommandDocument(bsonOutput).getFirstKey())) {
            try {
                this.sendMessage(bsonOutput.getByteBuffers(), message.getId());
            } finally {
                bsonOutput.close();
            }
        }
        List<ByteBuf> byteBuffers = bsonOutput.getByteBuffers();
        try {
            CompressedMessage compressedMessage = new CompressedMessage(message.getOpCode(), byteBuffers, this.sendCompressor, ProtocolHelper.getMessageSettings(this.description));
            compressedBsonOutput = new ByteBufferBsonOutput(this);
            compressedMessage.encode(compressedBsonOutput, sessionContext);
        } finally {
            this.releaseAllBuffers(byteBuffers);
            bsonOutput.close();
        }
        try {
            this.sendMessage(compressedBsonOutput.getByteBuffers(), message.getId());
        } finally {
            compressedBsonOutput.close();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private <T> T receiveCommandMessageResponse(CommandMessage message, Decoder<T> decoder, CommandEventSender commandEventSender, SessionContext sessionContext) {
        ResponseBuffers responseBuffers = this.receiveMessage(message.getId());
        try {
            this.updateSessionContext(sessionContext, responseBuffers);
            if (!ProtocolHelper.isCommandOk(responseBuffers)) {
                throw ProtocolHelper.getCommandFailureException(responseBuffers.getResponseDocument(message.getId(), new BsonDocumentCodec()), this.description.getServerAddress());
            }
            commandEventSender.sendSucceededEvent(responseBuffers);
            T t2 = this.getCommandResult(decoder, responseBuffers, message.getId());
            return t2;
        } finally {
            responseBuffers.close();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public <T> void sendAndReceiveAsync(CommandMessage message, Decoder<T> decoder, SessionContext sessionContext, SingleResultCallback<T> callback) {
        block7: {
            Assertions.notNull("stream is open", this.stream, callback);
            if (this.isClosed()) {
                callback.onResult(null, new MongoSocketClosedException("Can not read from a closed socket", this.getServerAddress()));
                return;
            }
            ByteBufferBsonOutput bsonOutput = new ByteBufferBsonOutput(this);
            ByteBufferBsonOutput compressedBsonOutput = new ByteBufferBsonOutput(this);
            try {
                message.encode(bsonOutput, sessionContext);
                CommandEventSender commandEventSender = this.createCommandEventSender(message, bsonOutput);
                commandEventSender.sendStartedEvent();
                if (this.sendCompressor == null || SECURITY_SENSITIVE_COMMANDS.contains(message.getCommandDocument(bsonOutput).getFirstKey())) {
                    this.sendCommandMessageAsync(message.getId(), decoder, sessionContext, callback, bsonOutput, commandEventSender, message.isResponseExpected());
                    break block7;
                }
                List<ByteBuf> byteBuffers = bsonOutput.getByteBuffers();
                try {
                    CompressedMessage compressedMessage = new CompressedMessage(message.getOpCode(), byteBuffers, this.sendCompressor, ProtocolHelper.getMessageSettings(this.description));
                    compressedMessage.encode(compressedBsonOutput, sessionContext);
                } finally {
                    this.releaseAllBuffers(byteBuffers);
                    bsonOutput.close();
                }
                this.sendCommandMessageAsync(message.getId(), decoder, sessionContext, callback, compressedBsonOutput, commandEventSender, message.isResponseExpected());
            } catch (Throwable t2) {
                bsonOutput.close();
                compressedBsonOutput.close();
                callback.onResult(null, t2);
            }
        }
    }

    private void releaseAllBuffers(List<ByteBuf> byteBuffers) {
        for (ByteBuf cur : byteBuffers) {
            cur.release();
        }
    }

    private <T> void sendCommandMessageAsync(final int messageId, final Decoder<T> decoder, final SessionContext sessionContext, final SingleResultCallback<T> callback, final ByteBufferBsonOutput bsonOutput, final CommandEventSender commandEventSender, final boolean responseExpected) {
        this.sendMessageAsync(bsonOutput.getByteBuffers(), messageId, new SingleResultCallback<Void>(){

            @Override
            public void onResult(Void result, Throwable t2) {
                bsonOutput.close();
                if (t2 != null) {
                    commandEventSender.sendFailedEvent(t2);
                    callback.onResult(null, t2);
                } else if (!responseExpected) {
                    commandEventSender.sendSucceededEventForOneWayCommand();
                    callback.onResult(null, null);
                } else {
                    InternalStreamConnection.this.readAsync(16, new MessageHeaderCallback(new SingleResultCallback<ResponseBuffers>(){

                        /*
                         * WARNING - Removed try catching itself - possible behaviour change.
                         */
                        @Override
                        public void onResult(ResponseBuffers responseBuffers, Throwable t2) {
                            if (t2 != null) {
                                commandEventSender.sendFailedEvent(t2);
                                callback.onResult(null, t2);
                                return;
                            }
                            try {
                                InternalStreamConnection.this.updateSessionContext(sessionContext, responseBuffers);
                                boolean commandOk = ProtocolHelper.isCommandOk(new BsonBinaryReader(new ByteBufferBsonInput(responseBuffers.getBodyByteBuffer())));
                                responseBuffers.reset();
                                if (!commandOk) {
                                    MongoException commandFailureException = ProtocolHelper.getCommandFailureException(responseBuffers.getResponseDocument(messageId, new BsonDocumentCodec()), InternalStreamConnection.this.description.getServerAddress());
                                    commandEventSender.sendFailedEvent(commandFailureException);
                                    throw commandFailureException;
                                }
                                commandEventSender.sendSucceededEvent(responseBuffers);
                                Object result = InternalStreamConnection.this.getCommandResult(decoder, responseBuffers, messageId);
                                callback.onResult(result, null);
                            } catch (Throwable localThrowable) {
                                callback.onResult(null, localThrowable);
                            } finally {
                                responseBuffers.close();
                            }
                        }
                    }));
                }
            }
        });
    }

    private <T> T getCommandResult(Decoder<T> decoder, ResponseBuffers responseBuffers, int messageId) {
        T result = new ReplyMessage<T>(responseBuffers, decoder, messageId).getDocuments().get(0);
        MongoException writeConcernBasedError = ProtocolHelper.createSpecialWriteConcernException(responseBuffers, this.description.getServerAddress());
        if (writeConcernBasedError != null) {
            throw new MongoWriteConcernWithResponseException(writeConcernBasedError, result);
        }
        return result;
    }

    @Override
    public void sendMessage(List<ByteBuf> byteBuffers, int lastRequestId) {
        Assertions.notNull("stream is open", this.stream);
        if (this.isClosed()) {
            throw new MongoSocketClosedException("Cannot write to a closed stream", this.getServerAddress());
        }
        try {
            this.stream.write(byteBuffers);
        } catch (Exception e2) {
            this.close();
            throw this.translateWriteException(e2);
        }
    }

    @Override
    public ResponseBuffers receiveMessage(int responseTo) {
        Assertions.notNull("stream is open", this.stream);
        if (this.isClosed()) {
            throw new MongoSocketClosedException("Cannot read from a closed stream", this.getServerAddress());
        }
        try {
            return this.receiveResponseBuffers();
        } catch (Throwable t2) {
            this.close();
            throw this.translateReadException(t2);
        }
    }

    @Override
    public void sendMessageAsync(List<ByteBuf> byteBuffers, int lastRequestId, SingleResultCallback<Void> callback) {
        Assertions.notNull("stream is open", this.stream, callback);
        if (this.isClosed()) {
            callback.onResult(null, new MongoSocketClosedException("Can not read from a closed socket", this.getServerAddress()));
            return;
        }
        this.writeAsync(byteBuffers, ErrorHandlingResultCallback.errorHandlingCallback(callback, LOGGER));
    }

    private void writeAsync(List<ByteBuf> byteBuffers, final SingleResultCallback<Void> callback) {
        this.stream.writeAsync(byteBuffers, new AsyncCompletionHandler<Void>(){

            @Override
            public void completed(Void v2) {
                callback.onResult(null, null);
            }

            @Override
            public void failed(Throwable t2) {
                InternalStreamConnection.this.close();
                callback.onResult(null, InternalStreamConnection.this.translateWriteException(t2));
            }
        });
    }

    @Override
    public void receiveMessageAsync(int responseTo, final SingleResultCallback<ResponseBuffers> callback) {
        Assertions.isTrue("stream is open", this.stream != null, callback);
        if (this.isClosed()) {
            callback.onResult(null, new MongoSocketClosedException("Can not read from a closed socket", this.getServerAddress()));
            return;
        }
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(String.format("Start receiving response on %s", this.getId()));
        }
        this.readAsync(16, new MessageHeaderCallback(new SingleResultCallback<ResponseBuffers>(){

            @Override
            public void onResult(ResponseBuffers result, Throwable t2) {
                if (t2 != null) {
                    InternalStreamConnection.this.close();
                    callback.onResult(null, t2);
                } else {
                    callback.onResult(result, null);
                }
            }
        }));
    }

    private void readAsync(int numBytes, final SingleResultCallback<ByteBuf> callback) {
        if (this.isClosed()) {
            callback.onResult(null, new MongoSocketClosedException("Cannot read from a closed stream", this.getServerAddress()));
            return;
        }
        try {
            this.stream.readAsync(numBytes, new AsyncCompletionHandler<ByteBuf>(){

                @Override
                public void completed(ByteBuf buffer) {
                    callback.onResult(buffer, null);
                }

                @Override
                public void failed(Throwable t2) {
                    InternalStreamConnection.this.close();
                    callback.onResult(null, InternalStreamConnection.this.translateReadException(t2));
                }
            });
        } catch (Exception e2) {
            callback.onResult(null, this.translateReadException(e2));
        }
    }

    private ConnectionId getId() {
        return this.description.getConnectionId();
    }

    private ServerAddress getServerAddress() {
        return this.description.getServerAddress();
    }

    private void updateSessionContext(SessionContext sessionContext, ResponseBuffers responseBuffers) {
        BsonDocument recoveryToken;
        sessionContext.advanceOperationTime(ProtocolHelper.getOperationTime(responseBuffers));
        sessionContext.advanceClusterTime(ProtocolHelper.getClusterTime(responseBuffers));
        if (sessionContext.hasActiveTransaction() && (recoveryToken = ProtocolHelper.getRecoveryToken(responseBuffers)) != null) {
            sessionContext.setRecoveryToken(recoveryToken);
        }
    }

    private MongoException translateWriteException(Throwable e2) {
        if (e2 instanceof MongoException) {
            return (MongoException)e2;
        }
        if (e2 instanceof IOException) {
            return new MongoSocketWriteException("Exception sending message", this.getServerAddress(), e2);
        }
        if (e2 instanceof InterruptedException) {
            return new MongoInternalException("Thread interrupted exception", e2);
        }
        return new MongoInternalException("Unexpected exception", e2);
    }

    private MongoException translateReadException(Throwable e2) {
        if (e2 instanceof MongoException) {
            return (MongoException)e2;
        }
        if (e2 instanceof SocketTimeoutException) {
            return new MongoSocketReadTimeoutException("Timeout while receiving message", this.getServerAddress(), e2);
        }
        if (e2 instanceof InterruptedIOException) {
            return new MongoInterruptedException("Interrupted while receiving message", (InterruptedIOException)e2);
        }
        if (e2 instanceof ClosedByInterruptException) {
            return new MongoInterruptedException("Interrupted while receiving message", (ClosedByInterruptException)e2);
        }
        if (e2 instanceof IOException) {
            return new MongoSocketReadException("Exception receiving message", this.getServerAddress(), e2);
        }
        if (e2 instanceof RuntimeException) {
            return new MongoInternalException("Unexpected runtime exception", e2);
        }
        if (e2 instanceof InterruptedException) {
            return new MongoInternalException("Interrupted exception", e2);
        }
        return new MongoInternalException("Unexpected exception", e2);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private ResponseBuffers receiveResponseBuffers() throws IOException {
        MessageHeader messageHeader;
        ByteBuf messageHeaderBuffer = this.stream.read(16);
        try {
            messageHeader = new MessageHeader(messageHeaderBuffer, this.description.getMaxMessageSize());
        } finally {
            messageHeaderBuffer.release();
        }
        ByteBuf messageBuffer = this.stream.read(messageHeader.getMessageLength() - 16);
        boolean releaseMessageBuffer = true;
        try {
            if (messageHeader.getOpCode() == OpCode.OP_COMPRESSED.getValue()) {
                CompressedHeader compressedHeader = new CompressedHeader(messageBuffer, messageHeader);
                Compressor compressor = this.getCompressor(compressedHeader);
                ByteBuf buffer = this.getBuffer(compressedHeader.getUncompressedSize());
                compressor.uncompress(messageBuffer, buffer);
                buffer.flip();
                ResponseBuffers responseBuffers = new ResponseBuffers(new ReplyHeader(buffer, compressedHeader), buffer);
                return responseBuffers;
            }
            ResponseBuffers responseBuffers = new ResponseBuffers(new ReplyHeader(messageBuffer, messageHeader), messageBuffer);
            releaseMessageBuffer = false;
            ResponseBuffers responseBuffers2 = responseBuffers;
            return responseBuffers2;
        } finally {
            if (releaseMessageBuffer) {
                messageBuffer.release();
            }
        }
    }

    private Compressor getCompressor(CompressedHeader compressedHeader) {
        Compressor compressor = this.compressorMap.get(compressedHeader.getCompressorId());
        if (compressor == null) {
            throw new MongoClientException("Unsupported compressor with identifier " + compressedHeader.getCompressorId());
        }
        return compressor;
    }

    @Override
    public ByteBuf getBuffer(int size) {
        Assertions.notNull("open", this.stream);
        return this.stream.getBuffer(size);
    }

    private CommandEventSender createCommandEventSender(CommandMessage message, ByteBufferBsonOutput bsonOutput) {
        if (this.opened() && (this.commandListener != null || COMMAND_PROTOCOL_LOGGER.isDebugEnabled())) {
            return new LoggingCommandEventSender(SECURITY_SENSITIVE_COMMANDS, this.description, this.commandListener, message, bsonOutput, COMMAND_PROTOCOL_LOGGER);
        }
        return new NoOpCommandEventSender();
    }

    private class MessageHeaderCallback
    implements SingleResultCallback<ByteBuf> {
        private final SingleResultCallback<ResponseBuffers> callback;

        MessageHeaderCallback(SingleResultCallback<ResponseBuffers> callback) {
            this.callback = callback;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void onResult(ByteBuf result, Throwable t2) {
            if (t2 != null) {
                this.callback.onResult(null, t2);
                return;
            }
            try {
                MessageHeader messageHeader = new MessageHeader(result, InternalStreamConnection.this.description.getMaxMessageSize());
                InternalStreamConnection.this.readAsync(messageHeader.getMessageLength() - 16, new MessageCallback(messageHeader));
            } catch (Throwable localThrowable) {
                this.callback.onResult(null, localThrowable);
            } finally {
                if (result != null) {
                    result.release();
                }
            }
        }

        private class MessageCallback
        implements SingleResultCallback<ByteBuf> {
            private final MessageHeader messageHeader;

            MessageCallback(MessageHeader messageHeader) {
                this.messageHeader = messageHeader;
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public void onResult(ByteBuf result, Throwable t2) {
                if (t2 != null) {
                    MessageHeaderCallback.this.callback.onResult(null, t2);
                    return;
                }
                boolean releaseResult = true;
                try {
                    ByteBuf responseBuffer;
                    ReplyHeader replyHeader;
                    if (this.messageHeader.getOpCode() == OpCode.OP_COMPRESSED.getValue()) {
                        try {
                            CompressedHeader compressedHeader = new CompressedHeader(result, this.messageHeader);
                            Compressor compressor = InternalStreamConnection.this.getCompressor(compressedHeader);
                            ByteBuf buffer = InternalStreamConnection.this.getBuffer(compressedHeader.getUncompressedSize());
                            compressor.uncompress(result, buffer);
                            buffer.flip();
                            replyHeader = new ReplyHeader(buffer, compressedHeader);
                            responseBuffer = buffer;
                        } finally {
                            releaseResult = false;
                            result.release();
                        }
                    } else {
                        replyHeader = new ReplyHeader(result, this.messageHeader);
                        responseBuffer = result;
                        releaseResult = false;
                    }
                    MessageHeaderCallback.this.callback.onResult(new ResponseBuffers(replyHeader, responseBuffer), null);
                } catch (Throwable localThrowable) {
                    MessageHeaderCallback.this.callback.onResult(null, localThrowable);
                } finally {
                    if (releaseResult) {
                        result.release();
                    }
                }
            }
        }
    }
}

