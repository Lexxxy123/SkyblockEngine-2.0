/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.operation;

import com.mongodb.MongoException;
import com.mongodb.MongoWriteConcernException;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.WriteConcernResult;
import com.mongodb.bulk.WriteConcernError;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.internal.connection.ProtocolHelper;
import com.mongodb.internal.operation.ServerVersionHelper;
import org.bson.BsonDocument;
import org.bson.BsonString;

public final class WriteConcernHelper {
    public static void appendWriteConcernToCommand(WriteConcern writeConcern, BsonDocument commandDocument, ConnectionDescription description) {
        if (writeConcern != null && !writeConcern.isServerDefault() && ServerVersionHelper.serverIsAtLeastVersionThreeDotFour(description)) {
            commandDocument.put("writeConcern", writeConcern.asDocument());
        }
    }

    public static void throwOnWriteConcernError(BsonDocument result, ServerAddress serverAddress) {
        if (WriteConcernHelper.hasWriteConcernError(result)) {
            WriteConcernHelper.throwOnSpecialException(result, serverAddress);
            throw WriteConcernHelper.createWriteConcernException(result, serverAddress);
        }
    }

    public static void throwOnSpecialException(BsonDocument result, ServerAddress serverAddress) {
        MongoException specialException = ProtocolHelper.createSpecialException(result, serverAddress, "errmsg");
        if (specialException != null) {
            throw specialException;
        }
    }

    public static boolean hasWriteConcernError(BsonDocument result) {
        return result.containsKey("writeConcernError");
    }

    public static MongoWriteConcernException createWriteConcernException(BsonDocument result, ServerAddress serverAddress) {
        return new MongoWriteConcernException(WriteConcernHelper.createWriteConcernError(result.getDocument("writeConcernError")), WriteConcernResult.acknowledged(0, false, null), serverAddress);
    }

    public static WriteConcernError createWriteConcernError(BsonDocument writeConcernErrorDocument) {
        return new WriteConcernError(writeConcernErrorDocument.getNumber("code").intValue(), writeConcernErrorDocument.getString("codeName", new BsonString("")).getValue(), writeConcernErrorDocument.getString("errmsg").getValue(), writeConcernErrorDocument.getDocument("errInfo", new BsonDocument()));
    }

    private WriteConcernHelper() {
    }
}

