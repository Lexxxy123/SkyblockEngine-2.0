/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

import com.mongodb.MongoWriteConcernException;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcernResult;
import com.mongodb.connection.AsyncConnection;
import com.mongodb.connection.Connection;
import com.mongodb.internal.operation.WriteConcernHelper;
import com.mongodb.operation.BsonDocumentWrapperHelper;
import com.mongodb.operation.CommandOperationHelper;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonInt32;

final class FindAndModifyHelper {
    static <T> CommandOperationHelper.CommandWriteTransformer<BsonDocument, T> transformer() {
        return new CommandOperationHelper.CommandWriteTransformer<BsonDocument, T>(){

            @Override
            public T apply(BsonDocument result, Connection connection) {
                return FindAndModifyHelper.transformDocument(result, connection.getDescription().getServerAddress());
            }
        };
    }

    static <T> CommandOperationHelper.CommandWriteTransformerAsync<BsonDocument, T> asyncTransformer() {
        return new CommandOperationHelper.CommandWriteTransformerAsync<BsonDocument, T>(){

            @Override
            public T apply(BsonDocument result, AsyncConnection connection) {
                return FindAndModifyHelper.transformDocument(result, connection.getDescription().getServerAddress());
            }
        };
    }

    private static <T> T transformDocument(BsonDocument result, ServerAddress serverAddress) {
        if (WriteConcernHelper.hasWriteConcernError(result)) {
            throw new MongoWriteConcernException(WriteConcernHelper.createWriteConcernError(result.getDocument("writeConcernError")), FindAndModifyHelper.createWriteConcernResult(result.getDocument("lastErrorObject", new BsonDocument())), serverAddress);
        }
        if (!result.isDocument("value")) {
            return null;
        }
        return BsonDocumentWrapperHelper.toDocument(result.getDocument("value", null));
    }

    private static WriteConcernResult createWriteConcernResult(BsonDocument result) {
        BsonBoolean updatedExisting = result.getBoolean("updatedExisting", BsonBoolean.FALSE);
        return WriteConcernResult.acknowledged(result.getNumber("n", new BsonInt32(0)).intValue(), updatedExisting.getValue(), result.get("upserted"));
    }

    private FindAndModifyHelper() {
    }
}

