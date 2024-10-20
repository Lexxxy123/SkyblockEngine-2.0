/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.internal.connection.BsonWriterDecorator;
import org.bson.BsonBinary;
import org.bson.BsonDbPointer;
import org.bson.BsonReader;
import org.bson.BsonRegularExpression;
import org.bson.BsonTimestamp;
import org.bson.BsonWriter;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;

public class FieldTrackingBsonWriter
extends BsonWriterDecorator {
    private boolean hasWrittenField;
    private boolean topLevelDocumentWritten;

    public FieldTrackingBsonWriter(BsonWriter bsonWriter) {
        super(bsonWriter);
    }

    public boolean hasWrittenField() {
        return this.hasWrittenField;
    }

    @Override
    public void writeStartDocument(String name) {
        if (this.topLevelDocumentWritten) {
            this.hasWrittenField = true;
        }
        super.writeStartDocument(name);
    }

    @Override
    public void writeStartDocument() {
        if (this.topLevelDocumentWritten) {
            this.hasWrittenField = true;
        }
        this.topLevelDocumentWritten = true;
        super.writeStartDocument();
    }

    @Override
    public void writeStartArray(String name) {
        this.hasWrittenField = true;
        super.writeStartArray(name);
    }

    @Override
    public void writeStartArray() {
        this.hasWrittenField = true;
        super.writeStartArray();
    }

    @Override
    public void writeBinaryData(String name, BsonBinary binary) {
        this.hasWrittenField = true;
        super.writeBinaryData(name, binary);
    }

    @Override
    public void writeBinaryData(BsonBinary binary) {
        this.hasWrittenField = true;
        super.writeBinaryData(binary);
    }

    @Override
    public void writeBoolean(String name, boolean value) {
        this.hasWrittenField = true;
        super.writeBoolean(name, value);
    }

    @Override
    public void writeBoolean(boolean value) {
        this.hasWrittenField = true;
        super.writeBoolean(value);
    }

    @Override
    public void writeDateTime(String name, long value) {
        this.hasWrittenField = true;
        super.writeDateTime(name, value);
    }

    @Override
    public void writeDateTime(long value) {
        this.hasWrittenField = true;
        super.writeDateTime(value);
    }

    @Override
    public void writeDBPointer(String name, BsonDbPointer value) {
        this.hasWrittenField = true;
        super.writeDBPointer(name, value);
    }

    @Override
    public void writeDBPointer(BsonDbPointer value) {
        this.hasWrittenField = true;
        super.writeDBPointer(value);
    }

    @Override
    public void writeDouble(String name, double value) {
        this.hasWrittenField = true;
        super.writeDouble(name, value);
    }

    @Override
    public void writeDouble(double value) {
        this.hasWrittenField = true;
        super.writeDouble(value);
    }

    @Override
    public void writeInt32(String name, int value) {
        this.hasWrittenField = true;
        super.writeInt32(name, value);
    }

    @Override
    public void writeInt32(int value) {
        this.hasWrittenField = true;
        super.writeInt32(value);
    }

    @Override
    public void writeInt64(String name, long value) {
        super.writeInt64(name, value);
        this.hasWrittenField = true;
    }

    @Override
    public void writeInt64(long value) {
        this.hasWrittenField = true;
        super.writeInt64(value);
    }

    @Override
    public void writeDecimal128(Decimal128 value) {
        this.hasWrittenField = true;
        super.writeDecimal128(value);
    }

    @Override
    public void writeDecimal128(String name, Decimal128 value) {
        this.hasWrittenField = true;
        super.writeDecimal128(name, value);
    }

    @Override
    public void writeJavaScript(String name, String code) {
        this.hasWrittenField = true;
        super.writeJavaScript(name, code);
    }

    @Override
    public void writeJavaScript(String code) {
        this.hasWrittenField = true;
        super.writeJavaScript(code);
    }

    @Override
    public void writeJavaScriptWithScope(String name, String code) {
        super.writeJavaScriptWithScope(name, code);
        this.hasWrittenField = true;
    }

    @Override
    public void writeJavaScriptWithScope(String code) {
        this.hasWrittenField = true;
        super.writeJavaScriptWithScope(code);
    }

    @Override
    public void writeMaxKey(String name) {
        this.hasWrittenField = true;
        super.writeMaxKey(name);
    }

    @Override
    public void writeMaxKey() {
        this.hasWrittenField = true;
        super.writeMaxKey();
    }

    @Override
    public void writeMinKey(String name) {
        this.hasWrittenField = true;
        super.writeMinKey(name);
    }

    @Override
    public void writeMinKey() {
        this.hasWrittenField = true;
        super.writeMinKey();
    }

    @Override
    public void writeNull(String name) {
        this.hasWrittenField = true;
        super.writeNull(name);
    }

    @Override
    public void writeNull() {
        this.hasWrittenField = true;
        super.writeNull();
    }

    @Override
    public void writeObjectId(String name, ObjectId objectId) {
        this.hasWrittenField = true;
        super.writeObjectId(name, objectId);
    }

    @Override
    public void writeObjectId(ObjectId objectId) {
        this.hasWrittenField = true;
        super.writeObjectId(objectId);
    }

    @Override
    public void writeRegularExpression(String name, BsonRegularExpression regularExpression) {
        this.hasWrittenField = true;
        super.writeRegularExpression(name, regularExpression);
    }

    @Override
    public void writeRegularExpression(BsonRegularExpression regularExpression) {
        this.hasWrittenField = true;
        super.writeRegularExpression(regularExpression);
    }

    @Override
    public void writeString(String name, String value) {
        this.hasWrittenField = true;
        super.writeString(name, value);
    }

    @Override
    public void writeString(String value) {
        this.hasWrittenField = true;
        super.writeString(value);
    }

    @Override
    public void writeSymbol(String name, String value) {
        this.hasWrittenField = true;
        super.writeSymbol(name, value);
    }

    @Override
    public void writeSymbol(String value) {
        this.hasWrittenField = true;
        super.writeSymbol(value);
    }

    @Override
    public void writeTimestamp(String name, BsonTimestamp value) {
        this.hasWrittenField = true;
        super.writeTimestamp(name, value);
    }

    @Override
    public void writeTimestamp(BsonTimestamp value) {
        this.hasWrittenField = true;
        super.writeTimestamp(value);
    }

    @Override
    public void writeUndefined(String name) {
        this.hasWrittenField = true;
        super.writeUndefined(name);
    }

    @Override
    public void writeUndefined() {
        this.hasWrittenField = true;
        super.writeUndefined();
    }

    @Override
    public void pipe(BsonReader reader) {
        this.hasWrittenField = true;
        super.pipe(reader);
    }
}

