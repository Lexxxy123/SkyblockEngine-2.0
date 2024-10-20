/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client;

import com.mongodb.ClientSessionOptions;
import com.mongodb.annotations.Immutable;
import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.ClientSession;
import com.mongodb.client.ListDatabasesIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.connection.ClusterDescription;
import java.io.Closeable;
import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;

@Immutable
public interface MongoClient
extends Closeable {
    public MongoDatabase getDatabase(String var1);

    public ClientSession startSession();

    public ClientSession startSession(ClientSessionOptions var1);

    @Override
    public void close();

    public MongoIterable<String> listDatabaseNames();

    public MongoIterable<String> listDatabaseNames(ClientSession var1);

    public ListDatabasesIterable<Document> listDatabases();

    public ListDatabasesIterable<Document> listDatabases(ClientSession var1);

    public <TResult> ListDatabasesIterable<TResult> listDatabases(Class<TResult> var1);

    public <TResult> ListDatabasesIterable<TResult> listDatabases(ClientSession var1, Class<TResult> var2);

    public ChangeStreamIterable<Document> watch();

    public <TResult> ChangeStreamIterable<TResult> watch(Class<TResult> var1);

    public ChangeStreamIterable<Document> watch(List<? extends Bson> var1);

    public <TResult> ChangeStreamIterable<TResult> watch(List<? extends Bson> var1, Class<TResult> var2);

    public ChangeStreamIterable<Document> watch(ClientSession var1);

    public <TResult> ChangeStreamIterable<TResult> watch(ClientSession var1, Class<TResult> var2);

    public ChangeStreamIterable<Document> watch(ClientSession var1, List<? extends Bson> var2);

    public <TResult> ChangeStreamIterable<TResult> watch(ClientSession var1, List<? extends Bson> var2, Class<TResult> var3);

    public ClusterDescription getClusterDescription();
}

