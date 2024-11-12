/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.internal.connection.BaseQueryMessage;
import com.mongodb.internal.connection.MessageSettings;
import com.mongodb.internal.connection.RequestMessage;
import com.mongodb.internal.validator.NoOpFieldNameValidator;
import org.bson.BsonDocument;
import org.bson.io.BsonOutput;

class QueryMessage
extends BaseQueryMessage {
    private final BsonDocument queryDocument;
    private final BsonDocument fields;

    QueryMessage(String collectionName, int skip, int numberToReturn, BsonDocument queryDocument, BsonDocument fields, MessageSettings settings) {
        super(collectionName, skip, numberToReturn, settings);
        this.queryDocument = queryDocument;
        this.fields = fields;
    }

    @Override
    protected RequestMessage.EncodingMetadata encodeMessageBodyWithMetadata(BsonOutput bsonOutput) {
        this.writeQueryPrologue(bsonOutput);
        int firstDocumentStartPosition = bsonOutput.getPosition();
        this.addDocument(this.queryDocument, bsonOutput, new NoOpFieldNameValidator());
        if (this.fields != null) {
            this.addDocument(this.fields, bsonOutput, new NoOpFieldNameValidator());
        }
        return new RequestMessage.EncodingMetadata(firstDocumentStartPosition);
    }
}

