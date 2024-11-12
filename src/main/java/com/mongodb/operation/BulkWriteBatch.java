/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.MongoBulkWriteException;
import com.mongodb.MongoClientException;
import com.mongodb.MongoInternalException;
import com.mongodb.MongoNamespace;
import com.mongodb.WriteConcern;
import com.mongodb.bulk.BulkWriteError;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.bulk.BulkWriteUpsert;
import com.mongodb.bulk.DeleteRequest;
import com.mongodb.bulk.InsertRequest;
import com.mongodb.bulk.UpdateRequest;
import com.mongodb.bulk.WriteConcernError;
import com.mongodb.bulk.WriteRequest;
import com.mongodb.connection.BulkWriteBatchCombiner;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.connection.ServerDescription;
import com.mongodb.connection.SplittablePayload;
import com.mongodb.internal.connection.FieldTrackingBsonWriter;
import com.mongodb.internal.connection.IndexMap;
import com.mongodb.internal.operation.WriteConcernHelper;
import com.mongodb.internal.validator.CollectibleDocumentFieldNameValidator;
import com.mongodb.internal.validator.MappedFieldNameValidator;
import com.mongodb.internal.validator.NoOpFieldNameValidator;
import com.mongodb.internal.validator.UpdateFieldNameValidator;
import com.mongodb.operation.OperationHelper;
import com.mongodb.session.SessionContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bson.BsonArray;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonDocumentWrapper;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.BsonWriter;
import org.bson.FieldNameValidator;
import org.bson.codecs.BsonValueCodecProvider;
import org.bson.codecs.Codec;
import org.bson.codecs.Decoder;
import org.bson.codecs.Encoder;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

final class BulkWriteBatch {
    private static final CodecRegistry REGISTRY = CodecRegistries.fromProviders(new BsonValueCodecProvider());
    private static final Decoder<BsonDocument> DECODER = REGISTRY.get(BsonDocument.class);
    private static final FieldNameValidator NO_OP_FIELD_NAME_VALIDATOR = new NoOpFieldNameValidator();
    private static final WriteRequestEncoder WRITE_REQUEST_ENCODER = new WriteRequestEncoder();
    private final MongoNamespace namespace;
    private final ConnectionDescription connectionDescription;
    private final boolean ordered;
    private final WriteConcern writeConcern;
    private final Boolean bypassDocumentValidation;
    private final boolean retryWrites;
    private final BulkWriteBatchCombiner bulkWriteBatchCombiner;
    private final IndexMap indexMap;
    private final WriteRequest.Type batchType;
    private final BsonDocument command;
    private final SplittablePayload payload;
    private final List<WriteRequestWithIndex> unprocessed;
    private final SessionContext sessionContext;

    public static BulkWriteBatch createBulkWriteBatch(MongoNamespace namespace, ServerDescription serverDescription, ConnectionDescription connectionDescription, boolean ordered, WriteConcern writeConcern, Boolean bypassDocumentValidation, boolean retryWrites, List<? extends WriteRequest> writeRequests, SessionContext sessionContext) {
        if (sessionContext.hasSession() && !sessionContext.isImplicitSession() && !sessionContext.hasActiveTransaction() && !writeConcern.isAcknowledged()) {
            throw new MongoClientException("Unacknowledged writes are not supported when using an explicit session");
        }
        boolean canRetryWrites = OperationHelper.isRetryableWrite(retryWrites, writeConcern, serverDescription, connectionDescription, sessionContext);
        ArrayList<WriteRequestWithIndex> writeRequestsWithIndex = new ArrayList<WriteRequestWithIndex>();
        boolean writeRequestsAreRetryable = true;
        for (int i2 = 0; i2 < writeRequests.size(); ++i2) {
            WriteRequest writeRequest = writeRequests.get(i2);
            writeRequestsAreRetryable = writeRequestsAreRetryable && BulkWriteBatch.isRetryable(writeRequest);
            writeRequestsWithIndex.add(new WriteRequestWithIndex(writeRequest, i2));
        }
        if (canRetryWrites && !writeRequestsAreRetryable) {
            canRetryWrites = false;
            OperationHelper.LOGGER.debug("retryWrites set but one or more writeRequests do not support retryable writes");
        }
        return new BulkWriteBatch(namespace, connectionDescription, ordered, writeConcern, bypassDocumentValidation, canRetryWrites, new BulkWriteBatchCombiner(connectionDescription.getServerAddress(), ordered, writeConcern), writeRequestsWithIndex, sessionContext);
    }

    private BulkWriteBatch(MongoNamespace namespace, ConnectionDescription connectionDescription, boolean ordered, WriteConcern writeConcern, Boolean bypassDocumentValidation, boolean retryWrites, BulkWriteBatchCombiner bulkWriteBatchCombiner, List<WriteRequestWithIndex> writeRequestsWithIndices, SessionContext sessionContext) {
        this.namespace = namespace;
        this.connectionDescription = connectionDescription;
        this.ordered = ordered;
        this.writeConcern = writeConcern;
        this.bypassDocumentValidation = bypassDocumentValidation;
        this.bulkWriteBatchCombiner = bulkWriteBatchCombiner;
        this.batchType = writeRequestsWithIndices.isEmpty() ? WriteRequest.Type.INSERT : writeRequestsWithIndices.get(0).writeRequest.getType();
        this.retryWrites = retryWrites;
        ArrayList<BsonDocument> payloadItems = new ArrayList<BsonDocument>();
        ArrayList<WriteRequestWithIndex> unprocessedItems = new ArrayList<WriteRequestWithIndex>();
        IndexMap indexMap = IndexMap.create();
        for (int i2 = 0; i2 < writeRequestsWithIndices.size(); ++i2) {
            WriteRequestWithIndex writeRequestWithIndex = writeRequestsWithIndices.get(i2);
            if (writeRequestWithIndex.getType() != this.batchType) {
                if (ordered) {
                    unprocessedItems.addAll(writeRequestsWithIndices.subList(i2, writeRequestsWithIndices.size()));
                    break;
                }
                unprocessedItems.add(writeRequestWithIndex);
                continue;
            }
            indexMap = indexMap.add(payloadItems.size(), writeRequestWithIndex.index);
            payloadItems.add(new BsonDocumentWrapper<WriteRequest>(writeRequestWithIndex.writeRequest, WRITE_REQUEST_ENCODER));
        }
        this.indexMap = indexMap;
        this.unprocessed = unprocessedItems;
        this.payload = new SplittablePayload(this.getPayloadType(this.batchType), payloadItems);
        this.sessionContext = sessionContext;
        this.command = new BsonDocument();
        if (!payloadItems.isEmpty()) {
            this.command.put(this.getCommandName(this.batchType), new BsonString(namespace.getCollectionName()));
            this.command.put("ordered", new BsonBoolean(ordered));
            if (!writeConcern.isServerDefault() && !sessionContext.hasActiveTransaction()) {
                this.command.put("writeConcern", writeConcern.asDocument());
            }
            if (bypassDocumentValidation != null) {
                this.command.put("bypassDocumentValidation", new BsonBoolean(bypassDocumentValidation));
            }
            if (retryWrites) {
                this.command.put("txnNumber", new BsonInt64(sessionContext.advanceTransactionNumber()));
            }
        }
    }

    private BulkWriteBatch(MongoNamespace namespace, ConnectionDescription connectionDescription, boolean ordered, WriteConcern writeConcern, Boolean bypassDocumentValidation, boolean retryWrites, BulkWriteBatchCombiner bulkWriteBatchCombiner, IndexMap indexMap, WriteRequest.Type batchType, BsonDocument command, SplittablePayload payload, List<WriteRequestWithIndex> unprocessed, SessionContext sessionContext) {
        this.namespace = namespace;
        this.connectionDescription = connectionDescription;
        this.ordered = ordered;
        this.writeConcern = writeConcern;
        this.bypassDocumentValidation = bypassDocumentValidation;
        this.bulkWriteBatchCombiner = bulkWriteBatchCombiner;
        this.indexMap = indexMap;
        this.batchType = batchType;
        this.payload = payload;
        this.unprocessed = unprocessed;
        this.retryWrites = retryWrites;
        this.sessionContext = sessionContext;
        if (retryWrites) {
            command.put("txnNumber", new BsonInt64(sessionContext.advanceTransactionNumber()));
        }
        this.command = command;
    }

    public void addResult(BsonDocument result) {
        if (this.writeConcern.isAcknowledged()) {
            if (this.hasError(result)) {
                MongoBulkWriteException bulkWriteException = this.getBulkWriteException(result);
                this.bulkWriteBatchCombiner.addErrorResult(bulkWriteException, this.indexMap);
            } else {
                this.bulkWriteBatchCombiner.addResult(this.getBulkWriteResult(result), this.indexMap);
            }
        }
    }

    public boolean getRetryWrites() {
        return this.retryWrites;
    }

    public BsonDocument getCommand() {
        return this.command;
    }

    public SplittablePayload getPayload() {
        return this.payload;
    }

    public Decoder<BsonDocument> getDecoder() {
        return DECODER;
    }

    public BulkWriteResult getResult() {
        return this.bulkWriteBatchCombiner.getResult();
    }

    public boolean hasErrors() {
        return this.bulkWriteBatchCombiner.hasErrors();
    }

    public MongoBulkWriteException getError() {
        return this.bulkWriteBatchCombiner.getError();
    }

    public boolean shouldProcessBatch() {
        return !this.bulkWriteBatchCombiner.shouldStopSendingMoreBatches() && !this.payload.isEmpty();
    }

    public boolean hasAnotherBatch() {
        return !this.unprocessed.isEmpty();
    }

    public BulkWriteBatch getNextBatch() {
        if (this.payload.hasAnotherSplit()) {
            IndexMap nextIndexMap = IndexMap.create();
            int newIndex = 0;
            for (int i2 = this.payload.getPosition(); i2 < this.payload.getPayload().size(); ++i2) {
                nextIndexMap = nextIndexMap.add(newIndex, this.indexMap.map(i2));
                ++newIndex;
            }
            return new BulkWriteBatch(this.namespace, this.connectionDescription, this.ordered, this.writeConcern, this.bypassDocumentValidation, this.retryWrites, this.bulkWriteBatchCombiner, nextIndexMap, this.batchType, this.command, this.payload.getNextSplit(), this.unprocessed, this.sessionContext);
        }
        return new BulkWriteBatch(this.namespace, this.connectionDescription, this.ordered, this.writeConcern, this.bypassDocumentValidation, this.retryWrites, this.bulkWriteBatchCombiner, this.unprocessed, this.sessionContext);
    }

    public FieldNameValidator getFieldNameValidator() {
        if (this.batchType == WriteRequest.Type.INSERT) {
            return new CollectibleDocumentFieldNameValidator();
        }
        if (this.batchType == WriteRequest.Type.UPDATE || this.batchType == WriteRequest.Type.REPLACE) {
            HashMap<String, FieldNameValidator> rootMap = new HashMap<String, FieldNameValidator>();
            if (this.batchType == WriteRequest.Type.REPLACE) {
                rootMap.put("u", new CollectibleDocumentFieldNameValidator());
            } else {
                rootMap.put("u", new UpdateFieldNameValidator());
            }
            return new MappedFieldNameValidator(NO_OP_FIELD_NAME_VALIDATOR, rootMap);
        }
        return NO_OP_FIELD_NAME_VALIDATOR;
    }

    private BulkWriteResult getBulkWriteResult(BsonDocument result) {
        int count = result.getNumber("n").intValue();
        List<BulkWriteUpsert> upsertedItems = this.getUpsertedItems(result);
        return BulkWriteResult.acknowledged(this.batchType, count - upsertedItems.size(), this.getModifiedCount(result), upsertedItems);
    }

    private List<BulkWriteUpsert> getUpsertedItems(BsonDocument result) {
        BsonArray upsertedValue = result.getArray("upserted", new BsonArray());
        ArrayList<BulkWriteUpsert> bulkWriteUpsertList = new ArrayList<BulkWriteUpsert>();
        for (BsonValue upsertedItem : upsertedValue) {
            BsonDocument upsertedItemDocument = (BsonDocument)upsertedItem;
            bulkWriteUpsertList.add(new BulkWriteUpsert(upsertedItemDocument.getNumber("index").intValue(), upsertedItemDocument.get("_id")));
        }
        return bulkWriteUpsertList;
    }

    private Integer getModifiedCount(BsonDocument result) {
        return result.getNumber("nModified", new BsonInt32(0)).intValue();
    }

    private boolean hasError(BsonDocument result) {
        return result.get("writeErrors") != null || result.get("writeConcernError") != null;
    }

    private MongoBulkWriteException getBulkWriteException(BsonDocument result) {
        if (!this.hasError(result)) {
            throw new MongoInternalException("This method should not have been called");
        }
        return new MongoBulkWriteException(this.getBulkWriteResult(result), this.getWriteErrors(result), this.getWriteConcernError(result), this.connectionDescription.getServerAddress());
    }

    private List<BulkWriteError> getWriteErrors(BsonDocument result) {
        ArrayList<BulkWriteError> writeErrors = new ArrayList<BulkWriteError>();
        BsonArray writeErrorsDocuments = (BsonArray)result.get("writeErrors");
        if (writeErrorsDocuments != null) {
            for (BsonValue cur : writeErrorsDocuments) {
                BsonDocument curDocument = (BsonDocument)cur;
                writeErrors.add(new BulkWriteError(curDocument.getNumber("code").intValue(), curDocument.getString("errmsg").getValue(), curDocument.getDocument("errInfo", new BsonDocument()), curDocument.getNumber("index").intValue()));
            }
        }
        return writeErrors;
    }

    private WriteConcernError getWriteConcernError(BsonDocument result) {
        BsonDocument writeConcernErrorDocument = (BsonDocument)result.get("writeConcernError");
        if (writeConcernErrorDocument == null) {
            return null;
        }
        return WriteConcernHelper.createWriteConcernError(writeConcernErrorDocument);
    }

    private String getCommandName(WriteRequest.Type batchType) {
        if (batchType == WriteRequest.Type.INSERT) {
            return "insert";
        }
        if (batchType == WriteRequest.Type.UPDATE || batchType == WriteRequest.Type.REPLACE) {
            return "update";
        }
        return "delete";
    }

    private SplittablePayload.Type getPayloadType(WriteRequest.Type batchType) {
        if (batchType == WriteRequest.Type.INSERT) {
            return SplittablePayload.Type.INSERT;
        }
        if (batchType == WriteRequest.Type.UPDATE) {
            return SplittablePayload.Type.UPDATE;
        }
        if (batchType == WriteRequest.Type.REPLACE) {
            return SplittablePayload.Type.REPLACE;
        }
        return SplittablePayload.Type.DELETE;
    }

    private static Codec<BsonDocument> getCodec(BsonDocument document) {
        return REGISTRY.get(document.getClass());
    }

    private static boolean isRetryable(WriteRequest writeRequest) {
        if (writeRequest.getType() == WriteRequest.Type.UPDATE || writeRequest.getType() == WriteRequest.Type.REPLACE) {
            return !((UpdateRequest)writeRequest).isMulti();
        }
        if (writeRequest.getType() == WriteRequest.Type.DELETE) {
            return !((DeleteRequest)writeRequest).isMulti();
        }
        return true;
    }

    static class WriteRequestWithIndex {
        private final int index;
        private final WriteRequest writeRequest;

        WriteRequestWithIndex(WriteRequest writeRequest, int index) {
            this.writeRequest = writeRequest;
            this.index = index;
        }

        WriteRequest.Type getType() {
            return this.writeRequest.getType();
        }
    }

    static class WriteRequestEncoder
    implements Encoder<WriteRequest> {
        WriteRequestEncoder() {
        }

        @Override
        public void encode(BsonWriter writer, WriteRequest writeRequest, EncoderContext encoderContext) {
            if (writeRequest.getType() == WriteRequest.Type.INSERT) {
                BsonDocument document = ((InsertRequest)writeRequest).getDocument();
                BulkWriteBatch.getCodec(document).encode(writer, document, EncoderContext.builder().isEncodingCollectibleDocument(true).build());
            } else if (writeRequest.getType() == WriteRequest.Type.UPDATE || writeRequest.getType() == WriteRequest.Type.REPLACE) {
                UpdateRequest update = (UpdateRequest)writeRequest;
                writer.writeStartDocument();
                writer.writeName("q");
                BulkWriteBatch.getCodec(update.getFilter()).encode(writer, update.getFilter(), EncoderContext.builder().build());
                BsonValue updateValue = update.getUpdateValue();
                if (!updateValue.isDocument() && !updateValue.isArray()) {
                    throw new IllegalArgumentException("Invalid BSON value for an update.");
                }
                if (updateValue.isArray() && updateValue.asArray().isEmpty()) {
                    throw new IllegalArgumentException("Invalid pipeline for an update. The pipeline may not be empty.");
                }
                writer.writeName("u");
                if (updateValue.isDocument()) {
                    FieldTrackingBsonWriter fieldTrackingBsonWriter = new FieldTrackingBsonWriter(writer);
                    BulkWriteBatch.getCodec(updateValue.asDocument()).encode(fieldTrackingBsonWriter, updateValue.asDocument(), EncoderContext.builder().build());
                    if (writeRequest.getType() == WriteRequest.Type.UPDATE && !fieldTrackingBsonWriter.hasWrittenField()) {
                        throw new IllegalArgumentException("Invalid BSON document for an update. The document may not be empty.");
                    }
                } else if (update.getType() == WriteRequest.Type.UPDATE && updateValue.isArray()) {
                    writer.writeStartArray();
                    for (BsonValue bsonValue : updateValue.asArray()) {
                        BulkWriteBatch.getCodec(bsonValue.asDocument()).encode(writer, bsonValue.asDocument(), EncoderContext.builder().build());
                    }
                    writer.writeEndArray();
                }
                if (update.isMulti()) {
                    writer.writeBoolean("multi", update.isMulti());
                }
                if (update.isUpsert()) {
                    writer.writeBoolean("upsert", update.isUpsert());
                }
                if (update.getCollation() != null) {
                    writer.writeName("collation");
                    BsonDocument collation = update.getCollation().asDocument();
                    BulkWriteBatch.getCodec(collation).encode(writer, collation, EncoderContext.builder().build());
                }
                if (update.getArrayFilters() != null) {
                    writer.writeStartArray("arrayFilters");
                    for (BsonDocument bsonDocument : update.getArrayFilters()) {
                        BulkWriteBatch.getCodec(bsonDocument).encode(writer, bsonDocument, EncoderContext.builder().build());
                    }
                    writer.writeEndArray();
                }
                writer.writeEndDocument();
            } else {
                DeleteRequest deleteRequest = (DeleteRequest)writeRequest;
                writer.writeStartDocument();
                writer.writeName("q");
                BulkWriteBatch.getCodec(deleteRequest.getFilter()).encode(writer, deleteRequest.getFilter(), EncoderContext.builder().build());
                writer.writeInt32("limit", deleteRequest.isMulti() ? 0 : 1);
                if (deleteRequest.getCollation() != null) {
                    writer.writeName("collation");
                    BsonDocument collation = deleteRequest.getCollation().asDocument();
                    BulkWriteBatch.getCodec(collation).encode(writer, collation, EncoderContext.builder().build());
                }
                writer.writeEndDocument();
            }
        }

        @Override
        public Class<WriteRequest> getEncoderClass() {
            return WriteRequest.class;
        }
    }
}

