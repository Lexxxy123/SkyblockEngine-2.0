/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.AcknowledgedBulkWriteResult;
import com.mongodb.BulkWriteException;
import com.mongodb.BulkWriteUpsert;
import com.mongodb.DBObject;
import com.mongodb.DBObjects;
import com.mongodb.MongoBulkWriteException;
import com.mongodb.UnacknowledgedBulkWriteResult;
import com.mongodb.bulk.BulkWriteError;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.bulk.WriteConcernError;
import java.util.ArrayList;
import java.util.List;
import org.bson.BsonDocument;
import org.bson.BsonDocumentReader;
import org.bson.codecs.Decoder;
import org.bson.codecs.DecoderContext;

final class BulkWriteHelper {
    static com.mongodb.BulkWriteResult translateBulkWriteResult(BulkWriteResult bulkWriteResult, Decoder<DBObject> decoder) {
        if (bulkWriteResult.wasAcknowledged()) {
            return new AcknowledgedBulkWriteResult(bulkWriteResult.getInsertedCount(), bulkWriteResult.getMatchedCount(), bulkWriteResult.getDeletedCount(), bulkWriteResult.getModifiedCount(), BulkWriteHelper.translateBulkWriteUpserts(bulkWriteResult.getUpserts(), decoder));
        }
        return new UnacknowledgedBulkWriteResult();
    }

    static List<BulkWriteUpsert> translateBulkWriteUpserts(List<com.mongodb.bulk.BulkWriteUpsert> upserts, Decoder<DBObject> decoder) {
        ArrayList<BulkWriteUpsert> retVal = new ArrayList<BulkWriteUpsert>(upserts.size());
        for (com.mongodb.bulk.BulkWriteUpsert cur : upserts) {
            retVal.add(new BulkWriteUpsert(cur.getIndex(), BulkWriteHelper.getUpsertedId(cur, decoder)));
        }
        return retVal;
    }

    private static Object getUpsertedId(com.mongodb.bulk.BulkWriteUpsert cur, Decoder<DBObject> decoder) {
        return decoder.decode(new BsonDocumentReader(new BsonDocument("_id", cur.getId())), DecoderContext.builder().build()).get("_id");
    }

    static BulkWriteException translateBulkWriteException(MongoBulkWriteException e2, Decoder<DBObject> decoder) {
        return new BulkWriteException(BulkWriteHelper.translateBulkWriteResult(e2.getWriteResult(), decoder), BulkWriteHelper.translateWriteErrors(e2.getWriteErrors()), BulkWriteHelper.translateWriteConcernError(e2.getWriteConcernError()), e2.getServerAddress());
    }

    static com.mongodb.WriteConcernError translateWriteConcernError(WriteConcernError writeConcernError) {
        return writeConcernError == null ? null : new com.mongodb.WriteConcernError(writeConcernError.getCode(), writeConcernError.getMessage(), DBObjects.toDBObject(writeConcernError.getDetails()));
    }

    static List<com.mongodb.BulkWriteError> translateWriteErrors(List<BulkWriteError> errors) {
        ArrayList<com.mongodb.BulkWriteError> retVal = new ArrayList<com.mongodb.BulkWriteError>(errors.size());
        for (BulkWriteError cur : errors) {
            retVal.add(new com.mongodb.BulkWriteError(cur.getCode(), cur.getMessage(), DBObjects.toDBObject(cur.getDetails()), cur.getIndex()));
        }
        return retVal;
    }

    private BulkWriteHelper() {
    }
}

