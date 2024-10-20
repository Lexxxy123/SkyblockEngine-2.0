/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.internal.connection.BsonWriterHelper;
import com.mongodb.internal.connection.LevelCountingBsonWriter;
import java.util.List;
import org.bson.BsonBinaryWriter;
import org.bson.BsonElement;
import org.bson.BsonReader;

public class ElementExtendingBsonWriter
extends LevelCountingBsonWriter {
    private final List<BsonElement> extraElements;

    public ElementExtendingBsonWriter(BsonBinaryWriter writer, List<BsonElement> extraElements) {
        super(writer);
        this.extraElements = extraElements;
    }

    @Override
    public void writeEndDocument() {
        if (this.getCurrentLevel() == 0) {
            BsonWriterHelper.writeElements(this.getBsonBinaryWriter(), this.extraElements);
        }
        super.writeEndDocument();
    }

    @Override
    public void pipe(BsonReader reader) {
        if (this.getCurrentLevel() == -1) {
            this.getBsonBinaryWriter().pipe(reader, this.extraElements);
        } else {
            this.getBsonBinaryWriter().pipe(reader);
        }
    }
}

