package net.hypixel.skyblock.user;

import com.mongodb.client.MongoCollection;
import net.hypixel.skyblock.sql.DatabaseManager;
import org.bson.Document;

import java.util.concurrent.CompletableFuture;

public class UserDatabase {
    static final CompletableFuture<MongoCollection<Document>> collectionFuture = DatabaseManager.getCollection("users");
    public final String id;

    public UserDatabase(String uuid) {
        this.id = uuid;
    }

    public CompletableFuture<Boolean> existsAsync() {
        return collectionFuture.thenApply(userCollection -> {
            // Perform database operations within the asynchronous context
            Document user = userCollection.find(new Document("uuid", id)).first();
            return user != null;
        });
    }
}
