/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.MongoClientException;
import com.mongodb.MongoException;
import com.mongodb.MongoNamespace;
import com.mongodb.ReadConcern;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.assertions.Assertions;
import com.mongodb.async.AsyncBatchCursor;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.binding.AsyncConnectionSource;
import com.mongodb.binding.AsyncReadBinding;
import com.mongodb.binding.AsyncWriteBinding;
import com.mongodb.binding.ConnectionSource;
import com.mongodb.binding.ReadBinding;
import com.mongodb.binding.ReferenceCounted;
import com.mongodb.binding.WriteBinding;
import com.mongodb.bulk.DeleteRequest;
import com.mongodb.bulk.IndexRequest;
import com.mongodb.bulk.UpdateRequest;
import com.mongodb.bulk.WriteRequest;
import com.mongodb.client.model.Collation;
import com.mongodb.connection.AsyncConnection;
import com.mongodb.connection.Connection;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.connection.QueryResult;
import com.mongodb.connection.ServerDescription;
import com.mongodb.connection.ServerType;
import com.mongodb.diagnostics.logging.Logger;
import com.mongodb.diagnostics.logging.Loggers;
import com.mongodb.internal.async.ErrorHandlingResultCallback;
import com.mongodb.internal.operation.ServerVersionHelper;
import com.mongodb.operation.AsyncQueryBatchCursor;
import com.mongodb.operation.AsyncSingleBatchQueryCursor;
import com.mongodb.operation.BatchCursor;
import com.mongodb.operation.BsonDocumentWrapperHelper;
import com.mongodb.operation.QueryBatchCursor;
import com.mongodb.session.SessionContext;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.codecs.Decoder;

final class OperationHelper {
    public static final Logger LOGGER = Loggers.getLogger("operation");

    static void validateReadConcern(Connection connection, ReadConcern readConcern) {
        OperationHelper.validateReadConcern(connection.getDescription(), readConcern);
    }

    static void validateReadConcern(ConnectionDescription description, ReadConcern readConcern) {
        if (!ServerVersionHelper.serverIsAtLeastVersionThreeDotTwo(description) && !readConcern.isServerDefault()) {
            throw new IllegalArgumentException(String.format("ReadConcern not supported by server version: %s", description.getServerVersion()));
        }
    }

    static void validateReadConcern(AsyncConnection connection, ReadConcern readConcern, AsyncCallableWithConnection callable) {
        IllegalArgumentException throwable = null;
        if (!ServerVersionHelper.serverIsAtLeastVersionThreeDotTwo(connection.getDescription()) && !readConcern.isServerDefault()) {
            throwable = new IllegalArgumentException(String.format("ReadConcern not supported by server version: %s", connection.getDescription().getServerVersion()));
        }
        callable.call(connection, throwable);
    }

    static void validateReadConcern(final AsyncConnectionSource source, AsyncConnection connection, ReadConcern readConcern, final AsyncCallableWithConnectionAndSource callable) {
        OperationHelper.validateReadConcern(connection, readConcern, new AsyncCallableWithConnection(){

            @Override
            public void call(AsyncConnection connection, Throwable t2) {
                callable.call(source, connection, t2);
            }
        });
    }

    static void validateCollation(Connection connection, Collation collation) {
        OperationHelper.validateCollation(connection.getDescription(), collation);
    }

    static void validateCollation(ConnectionDescription connectionDescription, Collation collation) {
        if (collation != null && !ServerVersionHelper.serverIsAtLeastVersionThreeDotFour(connectionDescription)) {
            throw new IllegalArgumentException(String.format("Collation not supported by server version: %s", connectionDescription.getServerVersion()));
        }
    }

    static void validateCollationAndWriteConcern(ConnectionDescription connectionDescription, Collation collation, WriteConcern writeConcern) {
        if (collation != null && !ServerVersionHelper.serverIsAtLeastVersionThreeDotFour(connectionDescription)) {
            throw new IllegalArgumentException(String.format("Collation not supported by server version: %s", connectionDescription.getServerVersion()));
        }
        if (collation != null && !writeConcern.isAcknowledged()) {
            throw new MongoClientException("Specifying collation with an unacknowledged WriteConcern is not supported");
        }
    }

    static void validateCollation(AsyncConnection connection, Collation collation, AsyncCallableWithConnection callable) {
        IllegalArgumentException throwable = null;
        if (!ServerVersionHelper.serverIsAtLeastVersionThreeDotFour(connection.getDescription()) && collation != null) {
            throwable = new IllegalArgumentException(String.format("Collation not supported by server version: %s", connection.getDescription().getServerVersion()));
        }
        callable.call(connection, throwable);
    }

    static void validateCollation(final AsyncConnectionSource source, AsyncConnection connection, Collation collation, final AsyncCallableWithConnectionAndSource callable) {
        OperationHelper.validateCollation(connection, collation, new AsyncCallableWithConnection(){

            @Override
            public void call(AsyncConnection connection, Throwable t2) {
                callable.call(source, connection, t2);
            }
        });
    }

    static void validateWriteRequestCollations(ConnectionDescription connectionDescription, List<? extends WriteRequest> requests, WriteConcern writeConcern) {
        Collation collation = null;
        for (WriteRequest writeRequest : requests) {
            if (writeRequest instanceof UpdateRequest) {
                collation = ((UpdateRequest)writeRequest).getCollation();
            } else if (writeRequest instanceof DeleteRequest) {
                collation = ((DeleteRequest)writeRequest).getCollation();
            }
            if (collation == null) continue;
            break;
        }
        OperationHelper.validateCollationAndWriteConcern(connectionDescription, collation, writeConcern);
    }

    static void validateWriteRequests(ConnectionDescription connectionDescription, Boolean bypassDocumentValidation, List<? extends WriteRequest> requests, WriteConcern writeConcern) {
        OperationHelper.checkBypassDocumentValidationIsSupported(connectionDescription, bypassDocumentValidation, writeConcern);
        OperationHelper.validateWriteRequestCollations(connectionDescription, requests, writeConcern);
    }

    static void validateWriteRequests(AsyncConnection connection, Boolean bypassDocumentValidation, List<? extends WriteRequest> requests, WriteConcern writeConcern, AsyncCallableWithConnection callable) {
        try {
            OperationHelper.validateWriteRequests(connection.getDescription(), bypassDocumentValidation, requests, writeConcern);
            callable.call(connection, null);
        } catch (Throwable t2) {
            callable.call(connection, t2);
        }
    }

    static void validateIndexRequestCollations(Connection connection, List<IndexRequest> requests) {
        for (IndexRequest request : requests) {
            if (request.getCollation() == null) continue;
            OperationHelper.validateCollation(connection, request.getCollation());
            break;
        }
    }

    static void validateIndexRequestCollations(AsyncConnection connection, List<IndexRequest> requests, final AsyncCallableWithConnection callable) {
        boolean calledTheCallable = false;
        for (IndexRequest request : requests) {
            if (request.getCollation() == null) continue;
            calledTheCallable = true;
            OperationHelper.validateCollation(connection, request.getCollation(), new AsyncCallableWithConnection(){

                @Override
                public void call(AsyncConnection connection, Throwable t2) {
                    callable.call(connection, t2);
                }
            });
            break;
        }
        if (!calledTheCallable) {
            callable.call(connection, null);
        }
    }

    static void validateReadConcernAndCollation(Connection connection, ReadConcern readConcern, Collation collation) {
        OperationHelper.validateReadConcern(connection, readConcern);
        OperationHelper.validateCollation(connection, collation);
    }

    static void validateReadConcernAndCollation(ConnectionDescription description, ReadConcern readConcern, Collation collation) {
        OperationHelper.validateReadConcern(description, readConcern);
        OperationHelper.validateCollation(description, collation);
    }

    static void validateReadConcernAndCollation(AsyncConnection connection, ReadConcern readConcern, final Collation collation, final AsyncCallableWithConnection callable) {
        OperationHelper.validateReadConcern(connection, readConcern, new AsyncCallableWithConnection(){

            @Override
            public void call(AsyncConnection connection, Throwable t2) {
                if (t2 != null) {
                    callable.call(connection, t2);
                } else {
                    OperationHelper.validateCollation(connection, collation, callable);
                }
            }
        });
    }

    static void validateReadConcernAndCollation(final AsyncConnectionSource source, AsyncConnection connection, ReadConcern readConcern, Collation collation, final AsyncCallableWithConnectionAndSource callable) {
        OperationHelper.validateReadConcernAndCollation(connection, readConcern, collation, new AsyncCallableWithConnection(){

            @Override
            public void call(AsyncConnection connection, Throwable t2) {
                callable.call(source, connection, t2);
            }
        });
    }

    static void checkBypassDocumentValidationIsSupported(ConnectionDescription connectionDescription, Boolean bypassDocumentValidation, WriteConcern writeConcern) {
        if (bypassDocumentValidation != null && ServerVersionHelper.serverIsAtLeastVersionThreeDotTwo(connectionDescription) && !writeConcern.isAcknowledged()) {
            throw new MongoClientException("Specifying bypassDocumentValidation with an unacknowledged WriteConcern is not supported");
        }
    }

    static boolean isRetryableWrite(boolean retryWrites, WriteConcern writeConcern, ServerDescription serverDescription, ConnectionDescription connectionDescription, SessionContext sessionContext) {
        if (!retryWrites) {
            return false;
        }
        if (!writeConcern.isAcknowledged()) {
            LOGGER.debug("retryWrites set to true but the writeConcern is unacknowledged.");
            return false;
        }
        if (sessionContext.hasActiveTransaction()) {
            LOGGER.debug("retryWrites set to true but in an active transaction.");
            return false;
        }
        return OperationHelper.canRetryWrite(serverDescription, connectionDescription, sessionContext);
    }

    static boolean canRetryWrite(ServerDescription serverDescription, ConnectionDescription connectionDescription, SessionContext sessionContext) {
        if (ServerVersionHelper.serverIsLessThanVersionThreeDotSix(connectionDescription)) {
            LOGGER.debug("retryWrites set to true but the server does not support retryable writes.");
            return false;
        }
        if (serverDescription.getLogicalSessionTimeoutMinutes() == null) {
            LOGGER.debug("retryWrites set to true but the server does not have 3.6 feature compatibility enabled.");
            return false;
        }
        if (connectionDescription.getServerType().equals((Object)ServerType.STANDALONE)) {
            LOGGER.debug("retryWrites set to true but the server is a standalone server.");
            return false;
        }
        if (!sessionContext.hasSession()) {
            LOGGER.debug("retryWrites set to true but there is no implicit session, likely because the MongoClient was created with multiple MongoCredential instances and sessions can only be used with a single MongoCredential");
            return false;
        }
        return true;
    }

    static boolean isRetryableRead(boolean retryReads, ServerDescription serverDescription, ConnectionDescription connectionDescription, SessionContext sessionContext) {
        if (!retryReads) {
            return false;
        }
        if (sessionContext.hasActiveTransaction()) {
            LOGGER.debug("retryReads set to true but in an active transaction.");
            return false;
        }
        return OperationHelper.canRetryRead(serverDescription, connectionDescription, sessionContext);
    }

    static boolean canRetryRead(ServerDescription serverDescription, ConnectionDescription connectionDescription, SessionContext sessionContext) {
        if (ServerVersionHelper.serverIsLessThanVersionThreeDotSix(connectionDescription)) {
            LOGGER.debug("retryReads set to true but the server does not support retryable reads.");
            return false;
        }
        if (serverDescription.getLogicalSessionTimeoutMinutes() == null) {
            LOGGER.debug("retryReads set to true but the server does not have 3.6 feature compatibility enabled.");
            return false;
        }
        if (serverDescription.getType() != ServerType.STANDALONE && !sessionContext.hasSession()) {
            LOGGER.debug("retryReads set to true but there is no implicit session, likely because the MongoClient was created with multiple MongoCredential instances and sessions can only be used with a single MongoCredential");
            return false;
        }
        return true;
    }

    static <T> QueryBatchCursor<T> createEmptyBatchCursor(MongoNamespace namespace, Decoder<T> decoder, ServerAddress serverAddress, int batchSize) {
        return new QueryBatchCursor(new QueryResult(namespace, Collections.emptyList(), 0L, serverAddress), 0, batchSize, decoder);
    }

    static <T> AsyncBatchCursor<T> createEmptyAsyncBatchCursor(MongoNamespace namespace, ServerAddress serverAddress) {
        return new AsyncSingleBatchQueryCursor(new QueryResult(namespace, Collections.emptyList(), 0L, serverAddress));
    }

    static <T> BatchCursor<T> cursorDocumentToBatchCursor(BsonDocument cursorDocument, Decoder<T> decoder, ConnectionSource source, int batchSize) {
        return new QueryBatchCursor<T>(OperationHelper.cursorDocumentToQueryResult(cursorDocument, source.getServerDescription().getAddress()), 0, batchSize, decoder, source);
    }

    static <T> AsyncBatchCursor<T> cursorDocumentToAsyncBatchCursor(BsonDocument cursorDocument, Decoder<T> decoder, AsyncConnectionSource source, AsyncConnection connection, int batchSize) {
        return new AsyncQueryBatchCursor<T>(OperationHelper.cursorDocumentToQueryResult(cursorDocument, source.getServerDescription().getAddress()), 0, batchSize, 0L, decoder, source, connection, cursorDocument);
    }

    static <T> QueryResult<T> cursorDocumentToQueryResult(BsonDocument cursorDocument, ServerAddress serverAddress) {
        return OperationHelper.cursorDocumentToQueryResult(cursorDocument, serverAddress, "firstBatch");
    }

    static <T> QueryResult<T> getMoreCursorDocumentToQueryResult(BsonDocument cursorDocument, ServerAddress serverAddress) {
        return OperationHelper.cursorDocumentToQueryResult(cursorDocument, serverAddress, "nextBatch");
    }

    private static <T> QueryResult<T> cursorDocumentToQueryResult(BsonDocument cursorDocument, ServerAddress serverAddress, String fieldNameContainingBatch) {
        long cursorId = ((BsonInt64)cursorDocument.get("id")).getValue();
        MongoNamespace queryResultNamespace = new MongoNamespace(cursorDocument.getString("ns").getValue());
        return new QueryResult(queryResultNamespace, BsonDocumentWrapperHelper.toList(cursorDocument, fieldNameContainingBatch), cursorId, serverAddress);
    }

    static <T> SingleResultCallback<T> releasingCallback(SingleResultCallback<T> wrapped, AsyncConnectionSource source) {
        return new ReferenceCountedReleasingWrappedCallback<T>(wrapped, Collections.singletonList(source));
    }

    static <T> SingleResultCallback<T> releasingCallback(SingleResultCallback<T> wrapped, AsyncConnection connection) {
        return new ReferenceCountedReleasingWrappedCallback<T>(wrapped, Collections.singletonList(connection));
    }

    static <T> SingleResultCallback<T> releasingCallback(SingleResultCallback<T> wrapped, AsyncConnectionSource source, AsyncConnection connection) {
        return new ReferenceCountedReleasingWrappedCallback<T>(wrapped, Arrays.asList(connection, source));
    }

    static <T> SingleResultCallback<T> releasingCallback(SingleResultCallback<T> wrapped, AsyncReadBinding readBinding, AsyncConnectionSource source, AsyncConnection connection) {
        return new ReferenceCountedReleasingWrappedCallback<T>(wrapped, Arrays.asList(readBinding, connection, source));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static <T> T withConnection(ReadBinding binding, CallableWithConnection<T> callable) {
        ConnectionSource source = binding.getReadConnectionSource();
        try {
            T t2 = OperationHelper.withConnectionSource(source, callable);
            return t2;
        } finally {
            source.release();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static <T> T withConnection(ReadBinding binding, CallableWithConnectionAndSource<T> callable) {
        ConnectionSource source = binding.getReadConnectionSource();
        try {
            T t2 = OperationHelper.withConnectionSource(source, callable);
            return t2;
        } finally {
            source.release();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static <T> T withReadConnectionSource(ReadBinding binding, CallableWithSource<T> callable) {
        ConnectionSource source = binding.getReadConnectionSource();
        try {
            T t2 = callable.call(source);
            return t2;
        } finally {
            source.release();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static <T> T withReleasableConnection(ReadBinding binding, MongoException connectionException, CallableWithConnectionAndSource<T> callable) {
        Connection connection;
        ConnectionSource source = null;
        try {
            source = binding.getReadConnectionSource();
            connection = source.getConnection();
        } catch (Throwable t2) {
            if (source != null) {
                source.release();
            }
            throw connectionException;
        }
        try {
            T t3 = callable.call(source, connection);
            return t3;
        } finally {
            source.release();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static <T> T withConnection(WriteBinding binding, CallableWithConnection<T> callable) {
        ConnectionSource source = binding.getWriteConnectionSource();
        try {
            T t2 = OperationHelper.withConnectionSource(source, callable);
            return t2;
        } finally {
            source.release();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static <T> T withReleasableConnection(WriteBinding binding, CallableWithConnectionAndSource<T> callable) {
        ConnectionSource source = binding.getWriteConnectionSource();
        try {
            T t2 = callable.call(source, source.getConnection());
            return t2;
        } finally {
            source.release();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static <T> T withReleasableConnection(WriteBinding binding, MongoException connectionException, CallableWithConnectionAndSource<T> callable) {
        Connection connection;
        ConnectionSource source = null;
        try {
            source = binding.getWriteConnectionSource();
            connection = source.getConnection();
        } catch (Throwable t2) {
            if (source != null) {
                source.release();
            }
            throw connectionException;
        }
        try {
            T t3 = callable.call(source, connection);
            return t3;
        } finally {
            source.release();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static <T> T withConnectionSource(ConnectionSource source, CallableWithConnection<T> callable) {
        Connection connection = source.getConnection();
        try {
            T t2 = callable.call(connection);
            return t2;
        } finally {
            connection.release();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static <T> T withConnectionSource(ConnectionSource source, CallableWithConnectionAndSource<T> callable) {
        Connection connection = source.getConnection();
        try {
            T t2 = callable.call(source, connection);
            return t2;
        } finally {
            connection.release();
        }
    }

    static void withAsyncConnection(AsyncWriteBinding binding, AsyncCallableWithConnection callable) {
        binding.getWriteConnectionSource(ErrorHandlingResultCallback.errorHandlingCallback(new AsyncCallableWithConnectionCallback(callable), LOGGER));
    }

    static void withAsyncConnection(AsyncWriteBinding binding, AsyncCallableWithConnectionAndSource callable) {
        binding.getWriteConnectionSource(ErrorHandlingResultCallback.errorHandlingCallback(new AsyncCallableWithConnectionAndSourceCallback(callable), LOGGER));
    }

    static void withAsyncReadConnection(AsyncReadBinding binding, AsyncCallableWithSource callable) {
        binding.getReadConnectionSource(ErrorHandlingResultCallback.errorHandlingCallback(new AsyncCallableWithSourceCallback(callable), LOGGER));
    }

    static void withAsyncReadConnection(AsyncReadBinding binding, AsyncCallableWithConnectionAndSource callable) {
        binding.getReadConnectionSource(ErrorHandlingResultCallback.errorHandlingCallback(new AsyncCallableWithConnectionAndSourceCallback(callable), LOGGER));
    }

    private static void withAsyncConnectionSourceCallableConnection(final AsyncConnectionSource source, final AsyncCallableWithConnection callable) {
        source.getConnection(new SingleResultCallback<AsyncConnection>(){

            @Override
            public void onResult(AsyncConnection connection, Throwable t2) {
                source.release();
                if (t2 != null) {
                    callable.call(null, t2);
                } else {
                    callable.call(connection, null);
                }
            }
        });
    }

    private static void withAsyncConnectionSource(AsyncConnectionSource source, AsyncCallableWithSource callable) {
        callable.call(source, null);
    }

    private static void withAsyncConnectionSource(final AsyncConnectionSource source, final AsyncCallableWithConnectionAndSource callable) {
        source.getConnection(new SingleResultCallback<AsyncConnection>(){

            @Override
            public void onResult(AsyncConnection result, Throwable t2) {
                callable.call(source, result, t2);
            }
        });
    }

    private OperationHelper() {
    }

    private static class AsyncCallableWithConnectionAndSourceCallback
    implements SingleResultCallback<AsyncConnectionSource> {
        private final AsyncCallableWithConnectionAndSource callable;

        AsyncCallableWithConnectionAndSourceCallback(AsyncCallableWithConnectionAndSource callable) {
            this.callable = callable;
        }

        @Override
        public void onResult(AsyncConnectionSource source, Throwable t2) {
            if (t2 != null) {
                this.callable.call(null, null, t2);
            } else {
                OperationHelper.withAsyncConnectionSource(source, this.callable);
            }
        }
    }

    private static class AsyncCallableWithSourceCallback
    implements SingleResultCallback<AsyncConnectionSource> {
        private final AsyncCallableWithSource callable;

        AsyncCallableWithSourceCallback(AsyncCallableWithSource callable) {
            this.callable = callable;
        }

        @Override
        public void onResult(AsyncConnectionSource source, Throwable t2) {
            if (t2 != null) {
                this.callable.call(null, t2);
            } else {
                OperationHelper.withAsyncConnectionSource(source, this.callable);
            }
        }
    }

    private static class AsyncCallableWithConnectionCallback
    implements SingleResultCallback<AsyncConnectionSource> {
        private final AsyncCallableWithConnection callable;

        AsyncCallableWithConnectionCallback(AsyncCallableWithConnection callable) {
            this.callable = callable;
        }

        @Override
        public void onResult(AsyncConnectionSource source, Throwable t2) {
            if (t2 != null) {
                this.callable.call(null, t2);
            } else {
                OperationHelper.withAsyncConnectionSourceCallableConnection(source, this.callable);
            }
        }
    }

    static class ConnectionReleasingWrappedCallback<T>
    implements SingleResultCallback<T> {
        private final SingleResultCallback<T> wrapped;
        private final AsyncConnectionSource source;
        private final AsyncConnection connection;

        ConnectionReleasingWrappedCallback(SingleResultCallback<T> wrapped, AsyncConnectionSource source, AsyncConnection connection) {
            this.wrapped = wrapped;
            this.source = Assertions.notNull("source", source);
            this.connection = Assertions.notNull("connection", connection);
        }

        @Override
        public void onResult(T result, Throwable t2) {
            this.connection.release();
            this.source.release();
            this.wrapped.onResult(result, t2);
        }

        public SingleResultCallback<T> releaseConnectionAndGetWrapped() {
            this.connection.release();
            this.source.release();
            return this.wrapped;
        }
    }

    private static class ReferenceCountedReleasingWrappedCallback<T>
    implements SingleResultCallback<T> {
        private final SingleResultCallback<T> wrapped;
        private final List<? extends ReferenceCounted> referenceCounted;

        ReferenceCountedReleasingWrappedCallback(SingleResultCallback<T> wrapped, List<? extends ReferenceCounted> referenceCounted) {
            this.wrapped = wrapped;
            this.referenceCounted = Assertions.notNull("referenceCounted", referenceCounted);
        }

        @Override
        public void onResult(T result, Throwable t2) {
            for (ReferenceCounted referenceCounted : this.referenceCounted) {
                if (referenceCounted == null) continue;
                referenceCounted.release();
            }
            this.wrapped.onResult(result, t2);
        }
    }

    static interface AsyncCallableWithConnectionAndSource {
        public void call(AsyncConnectionSource var1, AsyncConnection var2, Throwable var3);
    }

    static interface AsyncCallableWithSource {
        public void call(AsyncConnectionSource var1, Throwable var2);
    }

    static interface AsyncCallableWithConnection {
        public void call(AsyncConnection var1, Throwable var2);
    }

    static interface CallableWithConnectionAndSource<T> {
        public T call(ConnectionSource var1, Connection var2);
    }

    static interface CallableWithSource<T> {
        public T call(ConnectionSource var1);
    }

    static interface CallableWithConnection<T> {
        public T call(Connection var1);
    }
}

