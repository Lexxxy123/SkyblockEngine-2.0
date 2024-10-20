/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.Function;
import com.mongodb.MongoClientException;
import com.mongodb.MongoCommandException;
import com.mongodb.MongoException;
import com.mongodb.MongoNodeIsRecoveringException;
import com.mongodb.MongoNotPrimaryException;
import com.mongodb.MongoSocketException;
import com.mongodb.MongoWriteConcernException;
import com.mongodb.ReadPreference;
import com.mongodb.assertions.Assertions;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.binding.AsyncConnectionSource;
import com.mongodb.binding.AsyncReadBinding;
import com.mongodb.binding.AsyncWriteBinding;
import com.mongodb.binding.ConnectionSource;
import com.mongodb.binding.ReadBinding;
import com.mongodb.binding.WriteBinding;
import com.mongodb.connection.AsyncConnection;
import com.mongodb.connection.Connection;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.connection.ServerDescription;
import com.mongodb.internal.async.ErrorHandlingResultCallback;
import com.mongodb.internal.operation.WriteConcernHelper;
import com.mongodb.internal.validator.NoOpFieldNameValidator;
import com.mongodb.lang.Nullable;
import com.mongodb.operation.OperationHelper;
import com.mongodb.session.SessionContext;
import java.util.Arrays;
import java.util.List;
import org.bson.BsonDocument;
import org.bson.FieldNameValidator;
import org.bson.codecs.BsonDocumentCodec;
import org.bson.codecs.Decoder;

final class CommandOperationHelper {
    private static final List<Integer> RETRYABLE_ERROR_CODES = Arrays.asList(6, 7, 89, 91, 189, 9001, 13436, 13435, 11602, 11600, 10107);

    static CommandWriteTransformer<BsonDocument, Void> writeConcernErrorTransformer() {
        return new CommandWriteTransformer<BsonDocument, Void>(){

            @Override
            public Void apply(BsonDocument result, Connection connection) {
                WriteConcernHelper.throwOnWriteConcernError(result, connection.getDescription().getServerAddress());
                return null;
            }
        };
    }

    static CommandWriteTransformerAsync<BsonDocument, Void> writeConcernErrorWriteTransformer() {
        return new CommandWriteTransformerAsync<BsonDocument, Void>(){

            @Override
            public Void apply(BsonDocument result, AsyncConnection connection) {
                WriteConcernHelper.throwOnWriteConcernError(result, connection.getDescription().getServerAddress());
                return null;
            }
        };
    }

    static CommandWriteTransformerAsync<BsonDocument, Void> writeConcernErrorTransformerAsync() {
        return new CommandWriteTransformerAsync<BsonDocument, Void>(){

            @Override
            public Void apply(BsonDocument result, AsyncConnection connection) {
                WriteConcernHelper.throwOnWriteConcernError(result, connection.getDescription().getServerAddress());
                return null;
            }
        };
    }

    static Function<BsonDocument, BsonDocument> noOpRetryCommandModifier() {
        return new Function<BsonDocument, BsonDocument>(){

            @Override
            public BsonDocument apply(BsonDocument command) {
                return command;
            }
        };
    }

    static BsonDocument executeCommand(ReadBinding binding, String database, CommandCreator commandCreator, boolean retryReads) {
        return CommandOperationHelper.executeCommand(binding, database, commandCreator, new BsonDocumentCodec(), retryReads);
    }

    static <T> T executeCommand(ReadBinding binding, String database, CommandCreator commandCreator, CommandReadTransformer<BsonDocument, T> transformer, boolean retryReads) {
        return CommandOperationHelper.executeCommand(binding, database, commandCreator, new BsonDocumentCodec(), transformer, retryReads);
    }

    static <T> T executeCommand(ReadBinding binding, String database, CommandCreator commandCreator, Decoder<T> decoder, boolean retryReads) {
        return CommandOperationHelper.executeCommand(binding, database, commandCreator, decoder, new IdentityReadTransformer(), retryReads);
    }

    static <D, T> T executeCommand(final ReadBinding binding, final String database, final CommandCreator commandCreator, final Decoder<D> decoder, final CommandReadTransformer<D, T> transformer, final boolean retryReads) {
        return OperationHelper.withReadConnectionSource(binding, new OperationHelper.CallableWithSource<T>(){

            @Override
            public T call(ConnectionSource source) {
                return CommandOperationHelper.executeCommandWithConnection(binding, source, database, commandCreator, decoder, transformer, retryReads, source.getConnection());
            }
        });
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static <D, T> T executeCommandWithConnection(final ReadBinding binding, ConnectionSource source, final String database, final CommandCreator commandCreator, final Decoder<D> decoder, final CommandReadTransformer<D, T> transformer, boolean retryReads, Connection connection) {
        MongoException exception;
        BsonDocument command = null;
        try {
            command = commandCreator.create(source.getServerDescription(), connection.getDescription());
            T t2 = CommandOperationHelper.executeCommand(database, command, decoder, source, connection, binding.getReadPreference(), transformer, binding.getSessionContext());
            return t2;
        } catch (MongoException e2) {
            exception = e2;
            if (!CommandOperationHelper.shouldAttemptToRetryRead(retryReads, e2)) {
                if (retryReads) {
                    CommandOperationHelper.logUnableToRetry(command.getFirstKey(), e2);
                }
                throw exception;
            }
        } finally {
            connection.release();
        }
        final MongoException originalException = exception;
        return OperationHelper.withReleasableConnection(binding, originalException, new OperationHelper.CallableWithConnectionAndSource<T>(){

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public T call(ConnectionSource source, Connection connection) {
                try {
                    if (!OperationHelper.canRetryRead(source.getServerDescription(), connection.getDescription(), binding.getSessionContext())) {
                        throw originalException;
                    }
                    BsonDocument retryCommand = commandCreator.create(source.getServerDescription(), connection.getDescription());
                    CommandOperationHelper.logRetryExecute(retryCommand.getFirstKey(), originalException);
                    Object object = CommandOperationHelper.executeCommand(database, retryCommand, decoder, source, connection, binding.getReadPreference(), transformer, binding.getSessionContext());
                    return object;
                } finally {
                    connection.release();
                }
            }
        });
    }

    static BsonDocument executeCommand(WriteBinding binding, String database, BsonDocument command) {
        return (BsonDocument)CommandOperationHelper.executeCommand(binding, database, command, new IdentityWriteTransformer());
    }

    static <T> T executeCommand(WriteBinding binding, String database, BsonDocument command, Decoder<T> decoder) {
        return CommandOperationHelper.executeCommand(binding, database, command, decoder, new IdentityWriteTransformer());
    }

    static <T> T executeCommand(WriteBinding binding, String database, BsonDocument command, CommandWriteTransformer<BsonDocument, T> transformer) {
        return CommandOperationHelper.executeCommand(binding, database, command, new BsonDocumentCodec(), transformer);
    }

    static <D, T> T executeCommand(WriteBinding binding, String database, BsonDocument command, Decoder<D> decoder, CommandWriteTransformer<D, T> transformer) {
        return CommandOperationHelper.executeCommand(binding, database, command, new NoOpFieldNameValidator(), decoder, transformer);
    }

    static <T> T executeCommand(WriteBinding binding, String database, BsonDocument command, Connection connection, CommandWriteTransformer<BsonDocument, T> transformer) {
        return CommandOperationHelper.executeCommand(binding, database, command, new BsonDocumentCodec(), connection, transformer);
    }

    static <T> T executeCommand(WriteBinding binding, String database, BsonDocument command, Decoder<BsonDocument> decoder, Connection connection, CommandWriteTransformer<BsonDocument, T> transformer) {
        Assertions.notNull("binding", binding);
        return CommandOperationHelper.executeWriteCommand(database, command, decoder, connection, ReadPreference.primary(), transformer, binding.getSessionContext());
    }

    static <T> T executeCommand(WriteBinding binding, String database, BsonDocument command, FieldNameValidator fieldNameValidator, Decoder<BsonDocument> decoder, Connection connection, CommandWriteTransformer<BsonDocument, T> transformer) {
        Assertions.notNull("binding", binding);
        return CommandOperationHelper.executeWriteCommand(database, command, fieldNameValidator, decoder, connection, ReadPreference.primary(), transformer, binding.getSessionContext());
    }

    static <D, T> T executeCommand(WriteBinding binding, final String database, final BsonDocument command, final FieldNameValidator fieldNameValidator, final Decoder<D> decoder, final CommandWriteTransformer<D, T> transformer) {
        return OperationHelper.withReleasableConnection(binding, new OperationHelper.CallableWithConnectionAndSource<T>(){

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public T call(ConnectionSource source, Connection connection) {
                try {
                    Object r2 = transformer.apply(CommandOperationHelper.executeCommand(database, command, fieldNameValidator, decoder, source, connection, ReadPreference.primary()), connection);
                    return r2;
                } finally {
                    connection.release();
                }
            }
        });
    }

    static BsonDocument executeCommand(WriteBinding binding, String database, BsonDocument command, Connection connection) {
        Assertions.notNull("binding", binding);
        return CommandOperationHelper.executeWriteCommand(database, command, new BsonDocumentCodec(), connection, ReadPreference.primary(), binding.getSessionContext());
    }

    private static <T> T executeCommand(String database, BsonDocument command, FieldNameValidator fieldNameValidator, Decoder<T> decoder, ConnectionSource source, Connection connection, ReadPreference readPreference) {
        return CommandOperationHelper.executeCommand(database, command, fieldNameValidator, decoder, source, connection, readPreference, new IdentityReadTransformer(), source.getSessionContext());
    }

    private static <D, T> T executeCommand(String database, BsonDocument command, Decoder<D> decoder, ConnectionSource source, Connection connection, ReadPreference readPreference, CommandReadTransformer<D, T> transformer, SessionContext sessionContext) {
        return CommandOperationHelper.executeCommand(database, command, new NoOpFieldNameValidator(), decoder, source, connection, readPreference, transformer, sessionContext);
    }

    private static <D, T> T executeCommand(String database, BsonDocument command, FieldNameValidator fieldNameValidator, Decoder<D> decoder, ConnectionSource source, Connection connection, ReadPreference readPreference, CommandReadTransformer<D, T> transformer, SessionContext sessionContext) {
        return transformer.apply(connection.command(database, command, fieldNameValidator, readPreference, decoder, sessionContext), source, connection);
    }

    private static <T> T executeWriteCommand(String database, BsonDocument command, Decoder<T> decoder, Connection connection, ReadPreference readPreference, SessionContext sessionContext) {
        return CommandOperationHelper.executeWriteCommand(database, command, new NoOpFieldNameValidator(), decoder, connection, readPreference, new IdentityWriteTransformer(), sessionContext);
    }

    private static <D, T> T executeWriteCommand(String database, BsonDocument command, Decoder<D> decoder, Connection connection, ReadPreference readPreference, CommandWriteTransformer<D, T> transformer, SessionContext sessionContext) {
        return CommandOperationHelper.executeWriteCommand(database, command, new NoOpFieldNameValidator(), decoder, connection, readPreference, transformer, sessionContext);
    }

    private static <D, T> T executeWriteCommand(String database, BsonDocument command, FieldNameValidator fieldNameValidator, Decoder<D> decoder, Connection connection, ReadPreference readPreference, CommandWriteTransformer<D, T> transformer, SessionContext sessionContext) {
        return transformer.apply(connection.command(database, command, fieldNameValidator, readPreference, decoder, sessionContext), connection);
    }

    static void executeCommandAsync(AsyncReadBinding binding, String database, CommandCreator commandCreator, boolean retryReads, SingleResultCallback<BsonDocument> callback) {
        CommandOperationHelper.executeCommandAsync(binding, database, commandCreator, new BsonDocumentCodec(), retryReads, callback);
    }

    static <T> void executeCommandAsync(AsyncReadBinding binding, String database, CommandCreator commandCreator, Decoder<T> decoder, boolean retryReads, SingleResultCallback<T> callback) {
        CommandOperationHelper.executeCommandAsync(binding, database, commandCreator, decoder, new IdentityTransformerAsync(), retryReads, callback);
    }

    static <T> void executeCommandAsync(AsyncReadBinding binding, String database, CommandCreator commandCreator, CommandReadTransformerAsync<BsonDocument, T> transformer, boolean retryReads, SingleResultCallback<T> callback) {
        CommandOperationHelper.executeCommandAsync(binding, database, commandCreator, new BsonDocumentCodec(), transformer, retryReads, callback);
    }

    static <D, T> void executeCommandAsync(final AsyncReadBinding binding, final String database, final CommandCreator commandCreator, final Decoder<D> decoder, final CommandReadTransformerAsync<D, T> transformer, final boolean retryReads, SingleResultCallback<T> originalCallback) {
        final SingleResultCallback<T> errorHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(originalCallback, OperationHelper.LOGGER);
        OperationHelper.withAsyncReadConnection(binding, new OperationHelper.AsyncCallableWithConnectionAndSource(){

            @Override
            public void call(AsyncConnectionSource source, AsyncConnection connection, Throwable t2) {
                if (t2 != null) {
                    OperationHelper.releasingCallback(errorHandlingCallback, source, connection).onResult(null, t2);
                } else {
                    CommandOperationHelper.executeCommandAsyncWithConnection(binding, source, database, commandCreator, decoder, transformer, retryReads, connection, errorHandlingCallback);
                }
            }
        });
    }

    static <D, T> void executeCommandAsync(final AsyncReadBinding binding, final String database, final CommandCreator commandCreator, final Decoder<D> decoder, final CommandReadTransformerAsync<D, T> transformer, final boolean retryReads, final AsyncConnection connection, SingleResultCallback<T> originalCallback) {
        final SingleResultCallback<T> errorHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(originalCallback, OperationHelper.LOGGER);
        binding.getReadConnectionSource(new SingleResultCallback<AsyncConnectionSource>(){

            @Override
            public void onResult(AsyncConnectionSource source, Throwable t2) {
                CommandOperationHelper.executeCommandAsyncWithConnection(binding, source, database, commandCreator, decoder, transformer, retryReads, connection, errorHandlingCallback);
            }
        });
    }

    static <D, T> void executeCommandAsyncWithConnection(AsyncReadBinding binding, AsyncConnectionSource source, String database, CommandCreator commandCreator, Decoder<D> decoder, CommandReadTransformerAsync<D, T> transformer, boolean retryReads, AsyncConnection connection, SingleResultCallback<T> callback) {
        try {
            BsonDocument command = commandCreator.create(source.getServerDescription(), connection.getDescription());
            connection.commandAsync(database, command, new NoOpFieldNameValidator(), binding.getReadPreference(), decoder, binding.getSessionContext(), CommandOperationHelper.createCommandCallback(binding, source, connection, database, binding.getReadPreference(), command, commandCreator, new NoOpFieldNameValidator(), decoder, transformer, retryReads, callback));
        } catch (IllegalArgumentException e2) {
            connection.release();
            callback.onResult(null, e2);
        }
    }

    private static <T, R> SingleResultCallback<T> createCommandCallback(final AsyncReadBinding binding, final AsyncConnectionSource oldSource, final AsyncConnection oldConnection, final String database, final ReadPreference readPreference, final BsonDocument originalCommand, final CommandCreator commandCreator, final FieldNameValidator fieldNameValidator, final Decoder<T> commandResultDecoder, final CommandReadTransformerAsync<T, R> transformer, final boolean retryReads, final SingleResultCallback<R> callback) {
        return new SingleResultCallback<T>(){

            @Override
            public void onResult(T result, Throwable originalError) {
                SingleResultCallback releasingCallback = OperationHelper.releasingCallback(callback, oldSource, oldConnection);
                if (originalError != null) {
                    this.checkRetryableException(originalError, releasingCallback);
                } else {
                    try {
                        releasingCallback.onResult(transformer.apply(result, oldSource, oldConnection), null);
                    } catch (Throwable transformError) {
                        this.checkRetryableException(transformError, releasingCallback);
                    }
                }
            }

            private void checkRetryableException(Throwable originalError, SingleResultCallback<R> callback2) {
                if (!CommandOperationHelper.shouldAttemptToRetryRead(retryReads, originalError)) {
                    if (retryReads) {
                        CommandOperationHelper.logUnableToRetry(originalCommand.getFirstKey(), originalError);
                    }
                    callback2.onResult(null, originalError);
                } else {
                    oldSource.release();
                    oldConnection.release();
                    this.retryableCommand(originalError);
                }
            }

            private void retryableCommand(final Throwable originalError) {
                OperationHelper.withAsyncReadConnection(binding, new OperationHelper.AsyncCallableWithConnectionAndSource(){

                    @Override
                    public void call(AsyncConnectionSource source, AsyncConnection connection, Throwable t2) {
                        if (t2 != null) {
                            callback.onResult(null, originalError);
                        } else if (!OperationHelper.canRetryRead(source.getServerDescription(), connection.getDescription(), binding.getSessionContext())) {
                            OperationHelper.releasingCallback(callback, source, connection).onResult(null, originalError);
                        } else {
                            BsonDocument retryCommand = commandCreator.create(source.getServerDescription(), connection.getDescription());
                            CommandOperationHelper.logRetryExecute(retryCommand.getFirstKey(), originalError);
                            connection.commandAsync(database, retryCommand, fieldNameValidator, readPreference, commandResultDecoder, binding.getSessionContext(), new TransformingReadResultCallback(transformer, source, connection, OperationHelper.releasingCallback(callback, source, connection)));
                        }
                    }
                });
            }
        };
    }

    static void executeCommandAsync(AsyncWriteBinding binding, String database, BsonDocument command, SingleResultCallback<BsonDocument> callback) {
        CommandOperationHelper.executeCommandAsync(binding, database, command, new BsonDocumentCodec(), callback);
    }

    static <T> void executeCommandAsync(AsyncWriteBinding binding, String database, BsonDocument command, Decoder<T> decoder, SingleResultCallback<T> callback) {
        CommandOperationHelper.executeCommandAsync(binding, database, command, decoder, new IdentityWriteTransformerAsync(), callback);
    }

    static <T> void executeCommandAsync(AsyncWriteBinding binding, String database, BsonDocument command, CommandWriteTransformerAsync<BsonDocument, T> transformer, SingleResultCallback<T> callback) {
        CommandOperationHelper.executeCommandAsync(binding, database, command, new BsonDocumentCodec(), transformer, callback);
    }

    static <D, T> void executeCommandAsync(AsyncWriteBinding binding, String database, BsonDocument command, Decoder<D> decoder, CommandWriteTransformerAsync<D, T> transformer, SingleResultCallback<T> callback) {
        CommandOperationHelper.executeCommandAsync(binding, database, command, new NoOpFieldNameValidator(), decoder, transformer, callback);
    }

    static <T> void executeCommandAsync(AsyncWriteBinding binding, String database, BsonDocument command, Decoder<BsonDocument> decoder, AsyncConnection connection, CommandWriteTransformerAsync<BsonDocument, T> transformer, SingleResultCallback<T> callback) {
        Assertions.notNull("binding", binding);
        CommandOperationHelper.executeCommandAsync(database, command, decoder, connection, ReadPreference.primary(), transformer, binding.getSessionContext(), callback);
    }

    static <T> void executeCommandAsync(AsyncWriteBinding binding, String database, BsonDocument command, FieldNameValidator fieldNameValidator, Decoder<BsonDocument> decoder, AsyncConnection connection, CommandWriteTransformerAsync<BsonDocument, T> transformer, SingleResultCallback<T> callback) {
        Assertions.notNull("binding", binding);
        CommandOperationHelper.executeCommandAsync(database, command, fieldNameValidator, decoder, connection, ReadPreference.primary(), transformer, binding.getSessionContext(), callback);
    }

    static <D, T> void executeCommandAsync(AsyncWriteBinding binding, String database, BsonDocument command, FieldNameValidator fieldNameValidator, Decoder<D> decoder, CommandWriteTransformerAsync<D, T> transformer, SingleResultCallback<T> callback) {
        binding.getWriteConnectionSource(new CommandProtocolExecutingCallback<D, T>(database, command, fieldNameValidator, decoder, ReadPreference.primary(), transformer, binding.getSessionContext(), ErrorHandlingResultCallback.errorHandlingCallback(callback, OperationHelper.LOGGER)));
    }

    static void executeCommandAsync(AsyncWriteBinding binding, String database, BsonDocument command, AsyncConnection connection, SingleResultCallback<BsonDocument> callback) {
        CommandOperationHelper.executeCommandAsync(binding, database, command, connection, new IdentityWriteTransformerAsync(), callback);
    }

    static <T> void executeCommandAsync(AsyncWriteBinding binding, String database, BsonDocument command, AsyncConnection connection, CommandWriteTransformerAsync<BsonDocument, T> transformer, SingleResultCallback<T> callback) {
        Assertions.notNull("binding", binding);
        CommandOperationHelper.executeCommandAsync(database, command, new BsonDocumentCodec(), connection, ReadPreference.primary(), transformer, binding.getSessionContext(), callback);
    }

    private static <D, T> void executeCommandAsync(String database, BsonDocument command, Decoder<D> decoder, final AsyncConnection connection, ReadPreference readPreference, final CommandWriteTransformerAsync<D, T> transformer, SessionContext sessionContext, final SingleResultCallback<T> callback) {
        connection.commandAsync(database, command, new NoOpFieldNameValidator(), readPreference, decoder, sessionContext, new SingleResultCallback<D>(){

            @Override
            public void onResult(D result, Throwable t2) {
                if (t2 != null) {
                    callback.onResult(null, t2);
                } else {
                    try {
                        Object transformedResult = transformer.apply(result, connection);
                        callback.onResult(transformedResult, null);
                    } catch (Exception e2) {
                        callback.onResult(null, e2);
                    }
                }
            }
        });
    }

    private static <D, T> void executeCommandAsync(String database, BsonDocument command, FieldNameValidator fieldNameValidator, Decoder<D> decoder, final AsyncConnection connection, ReadPreference readPreference, final CommandWriteTransformerAsync<D, T> transformer, SessionContext sessionContext, final SingleResultCallback<T> callback) {
        connection.commandAsync(database, command, fieldNameValidator, readPreference, decoder, sessionContext, true, null, null, new SingleResultCallback<D>(){

            @Override
            public void onResult(D result, Throwable t2) {
                if (t2 != null) {
                    callback.onResult(null, t2);
                } else {
                    try {
                        Object transformedResult = transformer.apply(result, connection);
                        callback.onResult(transformedResult, null);
                    } catch (Exception e2) {
                        callback.onResult(null, e2);
                    }
                }
            }
        });
    }

    static <T, R> R executeRetryableCommand(WriteBinding binding, String database, ReadPreference readPreference, FieldNameValidator fieldNameValidator, Decoder<T> commandResultDecoder, CommandCreator commandCreator, CommandWriteTransformer<T, R> transformer) {
        return CommandOperationHelper.executeRetryableCommand(binding, database, readPreference, fieldNameValidator, commandResultDecoder, commandCreator, transformer, CommandOperationHelper.noOpRetryCommandModifier());
    }

    static <T, R> R executeRetryableCommand(final WriteBinding binding, final String database, final ReadPreference readPreference, final FieldNameValidator fieldNameValidator, final Decoder<T> commandResultDecoder, final CommandCreator commandCreator, final CommandWriteTransformer<T, R> transformer, final Function<BsonDocument, BsonDocument> retryCommandModifier) {
        return (R)OperationHelper.withReleasableConnection(binding, new OperationHelper.CallableWithConnectionAndSource<R>(){

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public R call(ConnectionSource source, Connection connection) {
                MongoException exception;
                BsonDocument command = null;
                try {
                    command = commandCreator.create(source.getServerDescription(), connection.getDescription());
                    Object r2 = transformer.apply(connection.command(database, command, fieldNameValidator, readPreference, commandResultDecoder, binding.getSessionContext()), connection);
                    return r2;
                } catch (MongoException e2) {
                    exception = e2;
                    if (!CommandOperationHelper.shouldAttemptToRetryWrite(command, e2)) {
                        if (CommandOperationHelper.isRetryWritesEnabled(command)) {
                            CommandOperationHelper.logUnableToRetry(command.getFirstKey(), e2);
                        }
                        throw CommandOperationHelper.transformWriteException(exception);
                    }
                } finally {
                    connection.release();
                }
                if (binding.getSessionContext().hasActiveTransaction()) {
                    binding.getSessionContext().unpinServerAddress();
                }
                final BsonDocument originalCommand = command;
                final MongoException originalException = exception;
                return OperationHelper.withReleasableConnection(binding, originalException, new OperationHelper.CallableWithConnectionAndSource<R>(){

                    /*
                     * WARNING - Removed try catching itself - possible behaviour change.
                     */
                    @Override
                    public R call(ConnectionSource source, Connection connection) {
                        try {
                            if (!OperationHelper.canRetryWrite(source.getServerDescription(), connection.getDescription(), binding.getSessionContext())) {
                                throw originalException;
                            }
                            BsonDocument retryCommand = (BsonDocument)retryCommandModifier.apply(originalCommand);
                            CommandOperationHelper.logRetryExecute(retryCommand.getFirstKey(), originalException);
                            Object r2 = transformer.apply(connection.command(database, retryCommand, fieldNameValidator, readPreference, commandResultDecoder, binding.getSessionContext()), connection);
                            return r2;
                        } finally {
                            connection.release();
                        }
                    }
                });
            }
        });
    }

    static <T, R> void executeRetryableCommand(AsyncWriteBinding binding, String database, ReadPreference readPreference, FieldNameValidator fieldNameValidator, Decoder<T> commandResultDecoder, CommandCreator commandCreator, CommandWriteTransformerAsync<T, R> transformer, SingleResultCallback<R> originalCallback) {
        CommandOperationHelper.executeRetryableCommand(binding, database, readPreference, fieldNameValidator, commandResultDecoder, commandCreator, transformer, CommandOperationHelper.noOpRetryCommandModifier(), originalCallback);
    }

    static <T, R> void executeRetryableCommand(final AsyncWriteBinding binding, final String database, final ReadPreference readPreference, final FieldNameValidator fieldNameValidator, final Decoder<T> commandResultDecoder, final CommandCreator commandCreator, final CommandWriteTransformerAsync<T, R> transformer, final Function<BsonDocument, BsonDocument> retryCommandModifier, SingleResultCallback<R> originalCallback) {
        final SingleResultCallback<R> errorHandlingCallback = ErrorHandlingResultCallback.errorHandlingCallback(originalCallback, OperationHelper.LOGGER);
        binding.getWriteConnectionSource(new SingleResultCallback<AsyncConnectionSource>(){

            @Override
            public void onResult(final AsyncConnectionSource source, Throwable t2) {
                if (t2 != null) {
                    errorHandlingCallback.onResult(null, t2);
                } else {
                    source.getConnection(new SingleResultCallback<AsyncConnection>(){

                        @Override
                        public void onResult(AsyncConnection connection, Throwable t2) {
                            if (t2 != null) {
                                OperationHelper.releasingCallback(errorHandlingCallback, source).onResult(null, t2);
                            } else {
                                try {
                                    BsonDocument command = commandCreator.create(source.getServerDescription(), connection.getDescription());
                                    connection.commandAsync(database, command, fieldNameValidator, readPreference, commandResultDecoder, binding.getSessionContext(), CommandOperationHelper.createCommandCallback(binding, source, connection, database, readPreference, command, fieldNameValidator, commandResultDecoder, transformer, retryCommandModifier, errorHandlingCallback));
                                } catch (Throwable t1) {
                                    OperationHelper.releasingCallback(errorHandlingCallback, source, connection).onResult(null, t1);
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private static <T, R> SingleResultCallback<T> createCommandCallback(final AsyncWriteBinding binding, final AsyncConnectionSource oldSource, final AsyncConnection oldConnection, final String database, final ReadPreference readPreference, final BsonDocument command, final FieldNameValidator fieldNameValidator, final Decoder<T> commandResultDecoder, final CommandWriteTransformerAsync<T, R> transformer, final Function<BsonDocument, BsonDocument> retryCommandModifier, final SingleResultCallback<R> callback) {
        return new SingleResultCallback<T>(){

            @Override
            public void onResult(T result, Throwable originalError) {
                SingleResultCallback releasingCallback = OperationHelper.releasingCallback(callback, oldSource, oldConnection);
                if (originalError != null) {
                    this.checkRetryableException(originalError, releasingCallback);
                } else {
                    try {
                        releasingCallback.onResult(transformer.apply(result, oldConnection), null);
                    } catch (Throwable transformError) {
                        this.checkRetryableException(transformError, releasingCallback);
                    }
                }
            }

            private void checkRetryableException(Throwable originalError, SingleResultCallback<R> releasingCallback) {
                if (!CommandOperationHelper.shouldAttemptToRetryWrite(command, originalError)) {
                    if (CommandOperationHelper.isRetryWritesEnabled(command)) {
                        CommandOperationHelper.logUnableToRetry(command.getFirstKey(), originalError);
                    }
                    releasingCallback.onResult(null, originalError instanceof MongoException ? CommandOperationHelper.transformWriteException((MongoException)originalError) : originalError);
                } else {
                    oldConnection.release();
                    oldSource.release();
                    if (binding.getSessionContext().hasActiveTransaction()) {
                        binding.getSessionContext().unpinServerAddress();
                    }
                    this.retryableCommand(originalError);
                }
            }

            private void retryableCommand(final Throwable originalError) {
                final BsonDocument retryCommand = (BsonDocument)retryCommandModifier.apply(command);
                CommandOperationHelper.logRetryExecute(retryCommand.getFirstKey(), originalError);
                OperationHelper.withAsyncConnection(binding, new OperationHelper.AsyncCallableWithConnectionAndSource(){

                    @Override
                    public void call(AsyncConnectionSource source, AsyncConnection connection, Throwable t2) {
                        if (t2 != null) {
                            callback.onResult(null, originalError);
                        } else if (!OperationHelper.canRetryWrite(source.getServerDescription(), connection.getDescription(), binding.getSessionContext())) {
                            OperationHelper.releasingCallback(callback, source, connection).onResult(null, originalError);
                        } else {
                            connection.commandAsync(database, retryCommand, fieldNameValidator, readPreference, commandResultDecoder, binding.getSessionContext(), new TransformingWriteResultCallback(transformer, connection, OperationHelper.releasingCallback(callback, source, connection)));
                        }
                    }
                });
            }
        };
    }

    static boolean isRetryableException(Throwable t2) {
        if (!(t2 instanceof MongoException)) {
            return false;
        }
        if (t2 instanceof MongoSocketException || t2 instanceof MongoNotPrimaryException || t2 instanceof MongoNodeIsRecoveringException) {
            return true;
        }
        String errorMessage = t2.getMessage();
        if (t2 instanceof MongoWriteConcernException) {
            errorMessage = ((MongoWriteConcernException)t2).getWriteConcernError().getMessage();
        }
        if (errorMessage.contains("not master") || errorMessage.contains("node is recovering")) {
            return true;
        }
        return RETRYABLE_ERROR_CODES.contains(((MongoException)t2).getCode());
    }

    static void rethrowIfNotNamespaceError(MongoCommandException e2) {
        CommandOperationHelper.rethrowIfNotNamespaceError(e2, null);
    }

    static <T> T rethrowIfNotNamespaceError(MongoCommandException e2, T defaultValue) {
        if (!CommandOperationHelper.isNamespaceError(e2)) {
            throw e2;
        }
        return defaultValue;
    }

    static boolean isNamespaceError(Throwable t2) {
        if (t2 instanceof MongoCommandException) {
            MongoCommandException e2 = (MongoCommandException)t2;
            return e2.getErrorMessage().contains("ns not found") || e2.getErrorCode() == 26;
        }
        return false;
    }

    private static boolean shouldAttemptToRetryRead(boolean retryReadsEnabled, Throwable exception) {
        return retryReadsEnabled && CommandOperationHelper.isRetryableException(exception);
    }

    private static boolean shouldAttemptToRetryWrite(@Nullable BsonDocument command, Throwable exception) {
        return CommandOperationHelper.isRetryWritesEnabled(command) && CommandOperationHelper.isRetryableException(exception);
    }

    private static boolean isRetryWritesEnabled(@Nullable BsonDocument command) {
        return command != null && (command.containsKey("txnNumber") || command.getFirstKey().equals("commitTransaction") || command.getFirstKey().equals("abortTransaction"));
    }

    static boolean shouldAttemptToRetryWrite(boolean retryWritesEnabled, Throwable exception) {
        return retryWritesEnabled && CommandOperationHelper.isRetryableException(exception);
    }

    static void logRetryExecute(String operation, Throwable originalError) {
        if (OperationHelper.LOGGER.isDebugEnabled()) {
            OperationHelper.LOGGER.debug(String.format("Retrying operation %s due to an error \"%s\"", operation, originalError));
        }
    }

    static void logUnableToRetry(String operation, Throwable originalError) {
        if (OperationHelper.LOGGER.isDebugEnabled()) {
            OperationHelper.LOGGER.debug(String.format("Unable to retry operation %s due to error \"%s\"", operation, originalError));
        }
    }

    static MongoException transformWriteException(MongoException exception) {
        if (exception.getCode() == 20 && exception.getMessage().contains("Transaction numbers")) {
            return new MongoClientException("This MongoDB deployment does not support retryable writes. Please add retryWrites=false to your connection string.", exception);
        }
        return exception;
    }

    private CommandOperationHelper() {
    }

    private static class CommandProtocolExecutingCallback<D, R>
    implements SingleResultCallback<AsyncConnectionSource> {
        private final String database;
        private final BsonDocument command;
        private final Decoder<D> decoder;
        private final ReadPreference readPreference;
        private final FieldNameValidator fieldNameValidator;
        private final CommandWriteTransformerAsync<D, R> transformer;
        private final SingleResultCallback<R> callback;
        private final SessionContext sessionContext;

        CommandProtocolExecutingCallback(String database, BsonDocument command, FieldNameValidator fieldNameValidator, Decoder<D> decoder, ReadPreference readPreference, CommandWriteTransformerAsync<D, R> transformer, SessionContext sessionContext, SingleResultCallback<R> callback) {
            this.database = database;
            this.command = command;
            this.fieldNameValidator = fieldNameValidator;
            this.decoder = decoder;
            this.readPreference = readPreference;
            this.transformer = transformer;
            this.sessionContext = sessionContext;
            this.callback = callback;
        }

        @Override
        public void onResult(final AsyncConnectionSource source, Throwable t2) {
            if (t2 != null) {
                this.callback.onResult(null, t2);
            } else {
                source.getConnection(new SingleResultCallback<AsyncConnection>(){

                    @Override
                    public void onResult(final AsyncConnection connection, Throwable t2) {
                        if (t2 != null) {
                            CommandProtocolExecutingCallback.this.callback.onResult(null, t2);
                        } else {
                            final SingleResultCallback wrappedCallback = OperationHelper.releasingCallback(CommandProtocolExecutingCallback.this.callback, source, connection);
                            connection.commandAsync(CommandProtocolExecutingCallback.this.database, CommandProtocolExecutingCallback.this.command, CommandProtocolExecutingCallback.this.fieldNameValidator, CommandProtocolExecutingCallback.this.readPreference, CommandProtocolExecutingCallback.this.decoder, CommandProtocolExecutingCallback.this.sessionContext, new SingleResultCallback<D>(){

                                @Override
                                public void onResult(D response, Throwable t2) {
                                    if (t2 != null) {
                                        wrappedCallback.onResult(null, t2);
                                    } else {
                                        wrappedCallback.onResult(CommandProtocolExecutingCallback.this.transformer.apply(response, connection), null);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }
    }

    static class TransformingWriteResultCallback<T, R>
    implements SingleResultCallback<T> {
        private final CommandWriteTransformerAsync<T, R> transformer;
        private final AsyncConnection connection;
        private final SingleResultCallback<R> callback;

        TransformingWriteResultCallback(CommandWriteTransformerAsync<T, R> transformer, AsyncConnection connection, SingleResultCallback<R> callback) {
            this.transformer = transformer;
            this.connection = connection;
            this.callback = callback;
        }

        @Override
        public void onResult(T result, Throwable t2) {
            if (t2 != null) {
                this.callback.onResult(null, t2);
            } else {
                try {
                    R transformedResult = this.transformer.apply(result, this.connection);
                    this.callback.onResult(transformedResult, null);
                } catch (Throwable transformError) {
                    this.callback.onResult(null, transformError);
                }
            }
        }
    }

    static class TransformingReadResultCallback<T, R>
    implements SingleResultCallback<T> {
        private final CommandReadTransformerAsync<T, R> transformer;
        private final AsyncConnectionSource source;
        private final AsyncConnection connection;
        private final SingleResultCallback<R> callback;

        TransformingReadResultCallback(CommandReadTransformerAsync<T, R> transformer, AsyncConnectionSource source, AsyncConnection connection, SingleResultCallback<R> callback) {
            this.transformer = transformer;
            this.source = source;
            this.connection = connection;
            this.callback = callback;
        }

        @Override
        public void onResult(T result, Throwable t2) {
            if (t2 != null) {
                this.callback.onResult(null, t2);
            } else {
                try {
                    R transformedResult = this.transformer.apply(result, this.source, this.connection);
                    this.callback.onResult(transformedResult, null);
                } catch (Throwable transformError) {
                    this.callback.onResult(null, transformError);
                }
            }
        }
    }

    static interface CommandCreator {
        public BsonDocument create(ServerDescription var1, ConnectionDescription var2);
    }

    static class IdentityTransformerAsync<T>
    implements CommandReadTransformerAsync<T, T> {
        IdentityTransformerAsync() {
        }

        @Override
        public T apply(T t2, AsyncConnectionSource source, AsyncConnection connection) {
            return t2;
        }
    }

    static class IdentityWriteTransformerAsync<T>
    implements CommandWriteTransformerAsync<T, T> {
        IdentityWriteTransformerAsync() {
        }

        @Override
        public T apply(T t2, AsyncConnection connection) {
            return t2;
        }
    }

    static class IdentityWriteTransformer<T>
    implements CommandWriteTransformer<T, T> {
        IdentityWriteTransformer() {
        }

        @Override
        public T apply(T t2, Connection connection) {
            return t2;
        }
    }

    static class IdentityReadTransformer<T>
    implements CommandReadTransformer<T, T> {
        IdentityReadTransformer() {
        }

        @Override
        public T apply(T t2, ConnectionSource source, Connection connection) {
            return t2;
        }
    }

    static interface CommandReadTransformerAsync<T, R> {
        public R apply(T var1, AsyncConnectionSource var2, AsyncConnection var3);
    }

    static interface CommandWriteTransformerAsync<T, R> {
        public R apply(T var1, AsyncConnection var2);
    }

    static interface CommandWriteTransformer<T, R> {
        public R apply(T var1, Connection var2);
    }

    static interface CommandReadTransformer<T, R> {
        public R apply(T var1, ConnectionSource var2, Connection var3);
    }
}

