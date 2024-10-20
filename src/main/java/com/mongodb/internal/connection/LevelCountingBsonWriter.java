/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.internal.connection.BsonWriterDecorator;
import org.bson.BsonBinaryWriter;

abstract class LevelCountingBsonWriter
extends BsonWriterDecorator {
    private int level = -1;

    LevelCountingBsonWriter(BsonBinaryWriter bsonBinaryWriter) {
        super(bsonBinaryWriter);
    }

    BsonBinaryWriter getBsonBinaryWriter() {
        return (BsonBinaryWriter)super.getBsonWriter();
    }

    int getCurrentLevel() {
        return this.level;
    }

    @Override
    public void writeStartDocument(String name) {
        ++this.level;
        super.writeStartDocument(name);
    }

    @Override
    public void writeStartDocument() {
        ++this.level;
        super.writeStartDocument();
    }

    @Override
    public void writeEndDocument() {
        --this.level;
        super.writeEndDocument();
    }
}

