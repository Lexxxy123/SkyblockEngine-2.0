/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.user;

import com.mongodb.client.MongoCollection;
import java.util.concurrent.CompletableFuture;
import net.hypixel.skyblock.database.DatabaseManager;
import org.bson.Document;

public class UserDatabase {
    static final CompletableFuture<MongoCollection<Document>> collectionFuture = DatabaseManager.getCollection("users");
    public final String id;

    public UserDatabase(String uuid) {
        this.id = uuid;
    }

    public CompletableFuture<Boolean> existsAsync() {
        return collectionFuture.thenApply(userCollection -> {
            Document user = (Document)userCollection.find(new Document("uuid", this.id)).first();
            return user != null;
        });
    }
}

