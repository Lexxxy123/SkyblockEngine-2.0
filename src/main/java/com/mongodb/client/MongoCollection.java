/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client;

import com.mongodb.MongoNamespace;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.annotations.ThreadSafe;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.ClientSession;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.MapReduceIterable;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.CountOptions;
import com.mongodb.client.model.CreateIndexOptions;
import com.mongodb.client.model.DeleteOptions;
import com.mongodb.client.model.DropIndexOptions;
import com.mongodb.client.model.EstimatedDocumentCountOptions;
import com.mongodb.client.model.FindOneAndDeleteOptions;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.IndexModel;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.InsertManyOptions;
import com.mongodb.client.model.InsertOneOptions;
import com.mongodb.client.model.RenameCollectionOptions;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.WriteModel;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.lang.Nullable;
import java.util.List;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

@ThreadSafe
public interface MongoCollection<TDocument> {
    public MongoNamespace getNamespace();

    public Class<TDocument> getDocumentClass();

    public CodecRegistry getCodecRegistry();

    public ReadPreference getReadPreference();

    public WriteConcern getWriteConcern();

    public ReadConcern getReadConcern();

    public <NewTDocument> MongoCollection<NewTDocument> withDocumentClass(Class<NewTDocument> var1);

    public MongoCollection<TDocument> withCodecRegistry(CodecRegistry var1);

    public MongoCollection<TDocument> withReadPreference(ReadPreference var1);

    public MongoCollection<TDocument> withWriteConcern(WriteConcern var1);

    public MongoCollection<TDocument> withReadConcern(ReadConcern var1);

    @Deprecated
    public long count();

    @Deprecated
    public long count(Bson var1);

    @Deprecated
    public long count(Bson var1, CountOptions var2);

    @Deprecated
    public long count(ClientSession var1);

    @Deprecated
    public long count(ClientSession var1, Bson var2);

    @Deprecated
    public long count(ClientSession var1, Bson var2, CountOptions var3);

    public long countDocuments();

    public long countDocuments(Bson var1);

    public long countDocuments(Bson var1, CountOptions var2);

    public long countDocuments(ClientSession var1);

    public long countDocuments(ClientSession var1, Bson var2);

    public long countDocuments(ClientSession var1, Bson var2, CountOptions var3);

    public long estimatedDocumentCount();

    public long estimatedDocumentCount(EstimatedDocumentCountOptions var1);

    public <TResult> DistinctIterable<TResult> distinct(String var1, Class<TResult> var2);

    public <TResult> DistinctIterable<TResult> distinct(String var1, Bson var2, Class<TResult> var3);

    public <TResult> DistinctIterable<TResult> distinct(ClientSession var1, String var2, Class<TResult> var3);

    public <TResult> DistinctIterable<TResult> distinct(ClientSession var1, String var2, Bson var3, Class<TResult> var4);

    public FindIterable<TDocument> find();

    public <TResult> FindIterable<TResult> find(Class<TResult> var1);

    public FindIterable<TDocument> find(Bson var1);

    public <TResult> FindIterable<TResult> find(Bson var1, Class<TResult> var2);

    public FindIterable<TDocument> find(ClientSession var1);

    public <TResult> FindIterable<TResult> find(ClientSession var1, Class<TResult> var2);

    public FindIterable<TDocument> find(ClientSession var1, Bson var2);

    public <TResult> FindIterable<TResult> find(ClientSession var1, Bson var2, Class<TResult> var3);

    public AggregateIterable<TDocument> aggregate(List<? extends Bson> var1);

    public <TResult> AggregateIterable<TResult> aggregate(List<? extends Bson> var1, Class<TResult> var2);

    public AggregateIterable<TDocument> aggregate(ClientSession var1, List<? extends Bson> var2);

    public <TResult> AggregateIterable<TResult> aggregate(ClientSession var1, List<? extends Bson> var2, Class<TResult> var3);

    public ChangeStreamIterable<TDocument> watch();

    public <TResult> ChangeStreamIterable<TResult> watch(Class<TResult> var1);

    public ChangeStreamIterable<TDocument> watch(List<? extends Bson> var1);

    public <TResult> ChangeStreamIterable<TResult> watch(List<? extends Bson> var1, Class<TResult> var2);

    public ChangeStreamIterable<TDocument> watch(ClientSession var1);

    public <TResult> ChangeStreamIterable<TResult> watch(ClientSession var1, Class<TResult> var2);

    public ChangeStreamIterable<TDocument> watch(ClientSession var1, List<? extends Bson> var2);

    public <TResult> ChangeStreamIterable<TResult> watch(ClientSession var1, List<? extends Bson> var2, Class<TResult> var3);

    public MapReduceIterable<TDocument> mapReduce(String var1, String var2);

    public <TResult> MapReduceIterable<TResult> mapReduce(String var1, String var2, Class<TResult> var3);

    public MapReduceIterable<TDocument> mapReduce(ClientSession var1, String var2, String var3);

    public <TResult> MapReduceIterable<TResult> mapReduce(ClientSession var1, String var2, String var3, Class<TResult> var4);

    public BulkWriteResult bulkWrite(List<? extends WriteModel<? extends TDocument>> var1);

    public BulkWriteResult bulkWrite(List<? extends WriteModel<? extends TDocument>> var1, BulkWriteOptions var2);

    public BulkWriteResult bulkWrite(ClientSession var1, List<? extends WriteModel<? extends TDocument>> var2);

    public BulkWriteResult bulkWrite(ClientSession var1, List<? extends WriteModel<? extends TDocument>> var2, BulkWriteOptions var3);

    public void insertOne(TDocument var1);

    public void insertOne(TDocument var1, InsertOneOptions var2);

    public void insertOne(ClientSession var1, TDocument var2);

    public void insertOne(ClientSession var1, TDocument var2, InsertOneOptions var3);

    public void insertMany(List<? extends TDocument> var1);

    public void insertMany(List<? extends TDocument> var1, InsertManyOptions var2);

    public void insertMany(ClientSession var1, List<? extends TDocument> var2);

    public void insertMany(ClientSession var1, List<? extends TDocument> var2, InsertManyOptions var3);

    public DeleteResult deleteOne(Bson var1);

    public DeleteResult deleteOne(Bson var1, DeleteOptions var2);

    public DeleteResult deleteOne(ClientSession var1, Bson var2);

    public DeleteResult deleteOne(ClientSession var1, Bson var2, DeleteOptions var3);

    public DeleteResult deleteMany(Bson var1);

    public DeleteResult deleteMany(Bson var1, DeleteOptions var2);

    public DeleteResult deleteMany(ClientSession var1, Bson var2);

    public DeleteResult deleteMany(ClientSession var1, Bson var2, DeleteOptions var3);

    public UpdateResult replaceOne(Bson var1, TDocument var2);

    @Deprecated
    public UpdateResult replaceOne(Bson var1, TDocument var2, UpdateOptions var3);

    public UpdateResult replaceOne(Bson var1, TDocument var2, ReplaceOptions var3);

    public UpdateResult replaceOne(ClientSession var1, Bson var2, TDocument var3);

    @Deprecated
    public UpdateResult replaceOne(ClientSession var1, Bson var2, TDocument var3, UpdateOptions var4);

    public UpdateResult replaceOne(ClientSession var1, Bson var2, TDocument var3, ReplaceOptions var4);

    public UpdateResult updateOne(Bson var1, Bson var2);

    public UpdateResult updateOne(Bson var1, Bson var2, UpdateOptions var3);

    public UpdateResult updateOne(ClientSession var1, Bson var2, Bson var3);

    public UpdateResult updateOne(ClientSession var1, Bson var2, Bson var3, UpdateOptions var4);

    public UpdateResult updateOne(Bson var1, List<? extends Bson> var2);

    public UpdateResult updateOne(Bson var1, List<? extends Bson> var2, UpdateOptions var3);

    public UpdateResult updateOne(ClientSession var1, Bson var2, List<? extends Bson> var3);

    public UpdateResult updateOne(ClientSession var1, Bson var2, List<? extends Bson> var3, UpdateOptions var4);

    public UpdateResult updateMany(Bson var1, Bson var2);

    public UpdateResult updateMany(Bson var1, Bson var2, UpdateOptions var3);

    public UpdateResult updateMany(ClientSession var1, Bson var2, Bson var3);

    public UpdateResult updateMany(ClientSession var1, Bson var2, Bson var3, UpdateOptions var4);

    public UpdateResult updateMany(Bson var1, List<? extends Bson> var2);

    public UpdateResult updateMany(Bson var1, List<? extends Bson> var2, UpdateOptions var3);

    public UpdateResult updateMany(ClientSession var1, Bson var2, List<? extends Bson> var3);

    public UpdateResult updateMany(ClientSession var1, Bson var2, List<? extends Bson> var3, UpdateOptions var4);

    @Nullable
    public TDocument findOneAndDelete(Bson var1);

    @Nullable
    public TDocument findOneAndDelete(Bson var1, FindOneAndDeleteOptions var2);

    @Nullable
    public TDocument findOneAndDelete(ClientSession var1, Bson var2);

    @Nullable
    public TDocument findOneAndDelete(ClientSession var1, Bson var2, FindOneAndDeleteOptions var3);

    @Nullable
    public TDocument findOneAndReplace(Bson var1, TDocument var2);

    @Nullable
    public TDocument findOneAndReplace(Bson var1, TDocument var2, FindOneAndReplaceOptions var3);

    @Nullable
    public TDocument findOneAndReplace(ClientSession var1, Bson var2, TDocument var3);

    @Nullable
    public TDocument findOneAndReplace(ClientSession var1, Bson var2, TDocument var3, FindOneAndReplaceOptions var4);

    @Nullable
    public TDocument findOneAndUpdate(Bson var1, Bson var2);

    @Nullable
    public TDocument findOneAndUpdate(Bson var1, Bson var2, FindOneAndUpdateOptions var3);

    @Nullable
    public TDocument findOneAndUpdate(ClientSession var1, Bson var2, Bson var3);

    @Nullable
    public TDocument findOneAndUpdate(ClientSession var1, Bson var2, Bson var3, FindOneAndUpdateOptions var4);

    @Nullable
    public TDocument findOneAndUpdate(Bson var1, List<? extends Bson> var2);

    @Nullable
    public TDocument findOneAndUpdate(Bson var1, List<? extends Bson> var2, FindOneAndUpdateOptions var3);

    @Nullable
    public TDocument findOneAndUpdate(ClientSession var1, Bson var2, List<? extends Bson> var3);

    @Nullable
    public TDocument findOneAndUpdate(ClientSession var1, Bson var2, List<? extends Bson> var3, FindOneAndUpdateOptions var4);

    public void drop();

    public void drop(ClientSession var1);

    public String createIndex(Bson var1);

    public String createIndex(Bson var1, IndexOptions var2);

    public String createIndex(ClientSession var1, Bson var2);

    public String createIndex(ClientSession var1, Bson var2, IndexOptions var3);

    public List<String> createIndexes(List<IndexModel> var1);

    public List<String> createIndexes(List<IndexModel> var1, CreateIndexOptions var2);

    public List<String> createIndexes(ClientSession var1, List<IndexModel> var2);

    public List<String> createIndexes(ClientSession var1, List<IndexModel> var2, CreateIndexOptions var3);

    public ListIndexesIterable<Document> listIndexes();

    public <TResult> ListIndexesIterable<TResult> listIndexes(Class<TResult> var1);

    public ListIndexesIterable<Document> listIndexes(ClientSession var1);

    public <TResult> ListIndexesIterable<TResult> listIndexes(ClientSession var1, Class<TResult> var2);

    public void dropIndex(String var1);

    public void dropIndex(String var1, DropIndexOptions var2);

    public void dropIndex(Bson var1);

    public void dropIndex(Bson var1, DropIndexOptions var2);

    public void dropIndex(ClientSession var1, String var2);

    public void dropIndex(ClientSession var1, Bson var2);

    public void dropIndex(ClientSession var1, String var2, DropIndexOptions var3);

    public void dropIndex(ClientSession var1, Bson var2, DropIndexOptions var3);

    public void dropIndexes();

    public void dropIndexes(ClientSession var1);

    public void dropIndexes(DropIndexOptions var1);

    public void dropIndexes(ClientSession var1, DropIndexOptions var2);

    public void renameCollection(MongoNamespace var1);

    public void renameCollection(MongoNamespace var1, RenameCollectionOptions var2);

    public void renameCollection(ClientSession var1, MongoNamespace var2);

    public void renameCollection(ClientSession var1, MongoNamespace var2, RenameCollectionOptions var3);
}

