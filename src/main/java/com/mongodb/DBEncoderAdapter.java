/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.DBEncoder;
import com.mongodb.DBObject;
import com.mongodb.assertions.Assertions;
import java.nio.ByteBuffer;
import org.bson.BsonBinaryReader;
import org.bson.BsonWriter;
import org.bson.ByteBufNIO;
import org.bson.codecs.Encoder;
import org.bson.codecs.EncoderContext;
import org.bson.io.BasicOutputBuffer;
import org.bson.io.ByteBufferBsonInput;

class DBEncoderAdapter
implements Encoder<DBObject> {
    private final DBEncoder encoder;

    DBEncoderAdapter(DBEncoder encoder) {
        this.encoder = Assertions.notNull("encoder", encoder);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void encode(BsonWriter writer, DBObject document, EncoderContext encoderContext) {
        BasicOutputBuffer buffer = new BasicOutputBuffer();
        try {
            this.encoder.writeObject(buffer, document);
            BsonBinaryReader reader = new BsonBinaryReader(new ByteBufferBsonInput(new ByteBufNIO(ByteBuffer.wrap(buffer.toByteArray()))));
            try {
                writer.pipe(reader);
            } finally {
                reader.close();
            }
        } finally {
            buffer.close();
        }
    }

    @Override
    public Class<DBObject> getEncoderClass() {
        return DBObject.class;
    }
}

