/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoNamespace;
import com.mongodb.assertions.Assertions;
import com.mongodb.client.model.Collation;
import com.mongodb.lang.Nullable;
import com.mongodb.operation.GroupOperation;
import org.bson.BsonDocumentWrapper;
import org.bson.BsonJavaScript;
import org.bson.codecs.Codec;

@Deprecated
public class GroupCommand {
    private final String collectionName;
    private final DBObject keys;
    private final String keyf;
    private final DBObject condition;
    private final DBObject initial;
    private final String reduce;
    private final String finalize;
    private final Collation collation;

    public GroupCommand(DBCollection collection, DBObject keys, DBObject condition, DBObject initial, String reduce, @Nullable String finalize) {
        this(collection, keys, condition, initial, reduce, finalize, null);
    }

    public GroupCommand(DBCollection collection, DBObject keys, DBObject condition, DBObject initial, String reduce, @Nullable String finalize, @Nullable Collation collation) {
        Assertions.notNull("collection", collection);
        this.collectionName = collection.getName();
        this.keys = keys;
        this.condition = condition;
        this.initial = initial;
        this.reduce = reduce;
        this.finalize = finalize;
        this.keyf = null;
        this.collation = collation;
    }

    public GroupCommand(DBCollection collection, String keyf, DBObject condition, DBObject initial, String reduce, String finalize) {
        this(collection, keyf, condition, initial, reduce, finalize, null);
    }

    public GroupCommand(DBCollection collection, String keyf, DBObject condition, DBObject initial, String reduce, @Nullable String finalize, @Nullable Collation collation) {
        Assertions.notNull("collection", collection);
        this.collectionName = collection.getName();
        this.keyf = Assertions.notNull("keyf", keyf);
        this.condition = condition;
        this.initial = initial;
        this.reduce = reduce;
        this.finalize = finalize;
        this.keys = null;
        this.collation = collation;
    }

    public DBObject toDBObject() {
        BasicDBObject args = new BasicDBObject("ns", this.collectionName).append("cond", this.condition).append("$reduce", this.reduce).append("initial", this.initial);
        if (this.keys != null) {
            args.put("key", this.keys);
        }
        if (this.keyf != null) {
            args.put("$keyf", this.keyf);
        }
        if (this.finalize != null) {
            args.put("finalize", this.finalize);
        }
        return new BasicDBObject("group", args);
    }

    GroupOperation<DBObject> toOperation(MongoNamespace namespace, Codec<DBObject> codec, boolean retryReads) {
        if (this.initial == null) {
            throw new IllegalArgumentException("Group command requires an initial document for the aggregate result");
        }
        if (this.reduce == null) {
            throw new IllegalArgumentException("Group command requires a reduce function for the aggregate result");
        }
        GroupOperation<DBObject> operation = new GroupOperation<DBObject>(namespace, new BsonJavaScript(this.reduce), new BsonDocumentWrapper<DBObject>(this.initial, codec), codec).retryReads(retryReads);
        if (this.keys != null) {
            operation.key(new BsonDocumentWrapper<DBObject>(this.keys, codec));
        }
        if (this.keyf != null) {
            operation.keyFunction(new BsonJavaScript(this.keyf));
        }
        if (this.condition != null) {
            operation.filter(new BsonDocumentWrapper<DBObject>(this.condition, codec));
        }
        if (this.finalize != null) {
            operation.finalizeFunction(new BsonJavaScript(this.finalize));
        }
        operation.collation(this.collation);
        return operation;
    }
}

