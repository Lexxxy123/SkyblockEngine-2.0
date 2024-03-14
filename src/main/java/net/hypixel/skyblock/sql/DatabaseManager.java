package net.hypixel.skyblock.sql;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class DatabaseManager {
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    public static void connectToDatabase(String connectionString, String databaseName) {
        mongoClient = MongoClients.create(connectionString);
        database = mongoClient.getDatabase(databaseName);
    }

    public static MongoCollection<Document> getCollection(String collectionName) {
        return database.getCollection(collectionName);
    }

    public static MongoDatabase getDatabase() {
        return database;
    }

    public static void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}