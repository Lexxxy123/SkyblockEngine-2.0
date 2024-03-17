package net.hypixel.skyblock.sql;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.concurrent.CompletableFuture;

public class DatabaseManager {
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    public static CompletableFuture<Void> connectToDatabase(String connectionString, String databaseName) {
        CompletableFuture<MongoClient> clientFuture = CompletableFuture.supplyAsync(() -> MongoClients.create(connectionString));
        return clientFuture.thenAccept(client -> {
            mongoClient = client;
            database = client.getDatabase(databaseName);
        });
    }

    public static CompletableFuture<MongoCollection<Document>> getCollection(String collectionName) {
        if (database == null) {
            return CompletableFuture.completedFuture(null);
        }
        return CompletableFuture.completedFuture(database.getCollection(collectionName));
    }

    public static CompletableFuture<Void> closeConnection() {
        if (mongoClient != null) {
            return CompletableFuture.runAsync(mongoClient::close);
        }
        return CompletableFuture.completedFuture(null);
    }
}