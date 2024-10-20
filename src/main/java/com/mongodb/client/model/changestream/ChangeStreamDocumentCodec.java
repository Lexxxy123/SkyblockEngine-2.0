/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model.changestream;

import com.mongodb.MongoNamespace;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.OperationTypeCodec;
import com.mongodb.client.model.changestream.UpdateDescription;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.BsonValueCodecProvider;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.ClassModel;
import org.bson.codecs.pojo.ClassModelBuilder;
import org.bson.codecs.pojo.PojoCodecProvider;

final class ChangeStreamDocumentCodec<TResult>
implements Codec<ChangeStreamDocument<TResult>> {
    private static final OperationTypeCodec OPERATION_TYPE_CODEC = new OperationTypeCodec();
    private final Codec<ChangeStreamDocument<TResult>> codec;

    ChangeStreamDocumentCodec(Class<TResult> fullDocumentClass, CodecRegistry codecRegistry) {
        ClassModelBuilder<ChangeStreamDocument> classModelBuilder = ClassModel.builder(ChangeStreamDocument.class);
        classModelBuilder.getProperty("fullDocument").codec(codecRegistry.get(fullDocumentClass));
        classModelBuilder.getProperty("operationType").codec(OPERATION_TYPE_CODEC);
        ClassModel<ChangeStreamDocument> changeStreamDocumentClassModel = classModelBuilder.build();
        PojoCodecProvider provider = PojoCodecProvider.builder().register(MongoNamespace.class).register(UpdateDescription.class).register(changeStreamDocumentClassModel).build();
        CodecRegistry registry = CodecRegistries.fromRegistries(CodecRegistries.fromProviders(provider, new BsonValueCodecProvider()), codecRegistry);
        this.codec = registry.get(ChangeStreamDocument.class);
    }

    @Override
    public ChangeStreamDocument<TResult> decode(BsonReader reader, DecoderContext decoderContext) {
        return (ChangeStreamDocument)this.codec.decode(reader, decoderContext);
    }

    @Override
    public void encode(BsonWriter writer, ChangeStreamDocument<TResult> value, EncoderContext encoderContext) {
        this.codec.encode(writer, value, encoderContext);
    }

    @Override
    public Class<ChangeStreamDocument<TResult>> getEncoderClass() {
        return ChangeStreamDocument.class;
    }
}

