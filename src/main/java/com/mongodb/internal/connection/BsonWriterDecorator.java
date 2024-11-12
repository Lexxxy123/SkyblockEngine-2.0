/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import org.bson.BsonBinary;
import org.bson.BsonDbPointer;
import org.bson.BsonReader;
import org.bson.BsonRegularExpression;
import org.bson.BsonTimestamp;
import org.bson.BsonWriter;
import org.bson.assertions.Assertions;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;

public class BsonWriterDecorator
implements BsonWriter {
    private final BsonWriter bsonWriter;

    BsonWriterDecorator(BsonWriter bsonWriter) {
        this.bsonWriter = Assertions.notNull("bsonWriter", bsonWriter);
    }

    BsonWriter getBsonWriter() {
        return this.bsonWriter;
    }

    @Override
    public void writeStartDocument(String name) {
        this.bsonWriter.writeStartDocument(name);
    }

    @Override
    public void writeStartDocument() {
        this.bsonWriter.writeStartDocument();
    }

    @Override
    public void writeEndDocument() {
        this.bsonWriter.writeEndDocument();
    }

    @Override
    public void writeStartArray(String name) {
        this.bsonWriter.writeStartArray(name);
    }

    @Override
    public void writeStartArray() {
        this.bsonWriter.writeStartArray();
    }

    @Override
    public void writeEndArray() {
        this.bsonWriter.writeEndArray();
    }

    @Override
    public void writeBinaryData(String name, BsonBinary binary) {
        this.bsonWriter.writeBinaryData(name, binary);
    }

    @Override
    public void writeBinaryData(BsonBinary binary) {
        this.bsonWriter.writeBinaryData(binary);
    }

    @Override
    public void writeBoolean(String name, boolean value) {
        this.bsonWriter.writeBoolean(name, value);
    }

    @Override
    public void writeBoolean(boolean value) {
        this.bsonWriter.writeBoolean(value);
    }

    @Override
    public void writeDateTime(String name, long value) {
        this.bsonWriter.writeDateTime(name, value);
    }

    @Override
    public void writeDateTime(long value) {
        this.bsonWriter.writeDateTime(value);
    }

    @Override
    public void writeDBPointer(String name, BsonDbPointer value) {
        this.bsonWriter.writeDBPointer(name, value);
    }

    @Override
    public void writeDBPointer(BsonDbPointer value) {
        this.bsonWriter.writeDBPointer(value);
    }

    @Override
    public void writeDouble(String name, double value) {
        this.bsonWriter.writeDouble(name, value);
    }

    @Override
    public void writeDouble(double value) {
        this.bsonWriter.writeDouble(value);
    }

    @Override
    public void writeInt32(String name, int value) {
        this.bsonWriter.writeInt32(name, value);
    }

    @Override
    public void writeInt32(int value) {
        this.bsonWriter.writeInt32(value);
    }

    @Override
    public void writeInt64(String name, long value) {
        this.bsonWriter.writeInt64(name, value);
    }

    @Override
    public void writeInt64(long value) {
        this.bsonWriter.writeInt64(value);
    }

    @Override
    public void writeDecimal128(Decimal128 value) {
        this.bsonWriter.writeDecimal128(value);
    }

    @Override
    public void writeDecimal128(String name, Decimal128 value) {
        this.bsonWriter.writeDecimal128(name, value);
    }

    @Override
    public void writeJavaScript(String name, String code) {
        this.bsonWriter.writeJavaScript(name, code);
    }

    @Override
    public void writeJavaScript(String code) {
        this.bsonWriter.writeJavaScript(code);
    }

    @Override
    public void writeJavaScriptWithScope(String name, String code) {
        this.bsonWriter.writeJavaScriptWithScope(name, code);
    }

    @Override
    public void writeJavaScriptWithScope(String code) {
        this.bsonWriter.writeJavaScriptWithScope(code);
    }

    @Override
    public void writeMaxKey(String name) {
        this.bsonWriter.writeMaxKey(name);
    }

    @Override
    public void writeMaxKey() {
        this.bsonWriter.writeMaxKey();
    }

    @Override
    public void writeMinKey(String name) {
        this.bsonWriter.writeMinKey(name);
    }

    @Override
    public void writeMinKey() {
        this.bsonWriter.writeMinKey();
    }

    @Override
    public void writeName(String name) {
        this.bsonWriter.writeName(name);
    }

    @Override
    public void writeNull(String name) {
        this.bsonWriter.writeNull(name);
    }

    @Override
    public void writeNull() {
        this.bsonWriter.writeNull();
    }

    @Override
    public void writeObjectId(String name, ObjectId objectId) {
        this.bsonWriter.writeObjectId(name, objectId);
    }

    @Override
    public void writeObjectId(ObjectId objectId) {
        this.bsonWriter.writeObjectId(objectId);
    }

    @Override
    public void writeRegularExpression(String name, BsonRegularExpression regularExpression) {
        this.bsonWriter.writeRegularExpression(name, regularExpression);
    }

    @Override
    public void writeRegularExpression(BsonRegularExpression regularExpression) {
        this.bsonWriter.writeRegularExpression(regularExpression);
    }

    @Override
    public void writeString(String name, String value) {
        this.bsonWriter.writeString(name, value);
    }

    @Override
    public void writeString(String value) {
        this.bsonWriter.writeString(value);
    }

    @Override
    public void writeSymbol(String name, String value) {
        this.bsonWriter.writeSymbol(name, value);
    }

    @Override
    public void writeSymbol(String value) {
        this.bsonWriter.writeSymbol(value);
    }

    @Override
    public void writeTimestamp(String name, BsonTimestamp value) {
        this.bsonWriter.writeTimestamp(name, value);
    }

    @Override
    public void writeTimestamp(BsonTimestamp value) {
        this.bsonWriter.writeTimestamp(value);
    }

    @Override
    public void writeUndefined(String name) {
        this.bsonWriter.writeUndefined(name);
    }

    @Override
    public void writeUndefined() {
        this.bsonWriter.writeUndefined();
    }

    @Override
    public void pipe(BsonReader reader) {
        this.bsonWriter.pipe(reader);
    }

    @Override
    public void flush() {
        this.bsonWriter.flush();
    }
}

