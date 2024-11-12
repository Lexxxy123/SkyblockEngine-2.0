/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.MongoServerException;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcernResult;
import com.mongodb.lang.Nullable;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonValue;

public class WriteConcernException
extends MongoServerException {
    private static final long serialVersionUID = -1100801000476719450L;
    private final WriteConcernResult writeConcernResult;
    private final BsonDocument response;

    public WriteConcernException(BsonDocument response, ServerAddress address, WriteConcernResult writeConcernResult) {
        super(WriteConcernException.extractErrorCode(response), String.format("Write failed with error code %d and error message '%s'", WriteConcernException.extractErrorCode(response), WriteConcernException.extractErrorMessage(response)), address);
        this.response = response;
        this.writeConcernResult = writeConcernResult;
    }

    public static int extractErrorCode(BsonDocument response) {
        String errorMessage = WriteConcernException.extractErrorMessage(response);
        if (errorMessage != null) {
            if (response.containsKey("err") && errorMessage.contains("E11000 duplicate key error")) {
                return 11000;
            }
            if (!response.containsKey("code") && response.containsKey("errObjects")) {
                for (BsonValue curErrorDocument : response.getArray("errObjects")) {
                    if (!errorMessage.equals(WriteConcernException.extractErrorMessage(curErrorDocument.asDocument()))) continue;
                    return curErrorDocument.asDocument().getNumber("code").intValue();
                }
            }
        }
        return response.getNumber("code", new BsonInt32(-1)).intValue();
    }

    @Nullable
    public static String extractErrorMessage(BsonDocument response) {
        if (response.isString("err")) {
            return response.getString("err").getValue();
        }
        if (response.isString("errmsg")) {
            return response.getString("errmsg").getValue();
        }
        return null;
    }

    public WriteConcernResult getWriteConcernResult() {
        return this.writeConcernResult;
    }

    public int getErrorCode() {
        return WriteConcernException.extractErrorCode(this.response);
    }

    @Nullable
    public String getErrorMessage() {
        return WriteConcernException.extractErrorMessage(this.response);
    }

    public BsonDocument getResponse() {
        return this.response;
    }
}

