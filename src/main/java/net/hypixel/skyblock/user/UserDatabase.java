package net.hypixel.skyblock.user;

import com.mongodb.client.MongoCollection;
import net.hypixel.skyblock.sql.DatabaseManager;
import org.bson.Document;


public class UserDatabase {
    public static final MongoCollection<Document> collection = DatabaseManager.getCollection("users");
    public final String id;


    public UserDatabase(String uuid) {
        this.id = uuid;
    }


    public boolean exists() {
        MongoCollection<Document> userCollection = DatabaseManager.getCollection("users");
        return userCollection.find(new Document("uuid", id)).first() != null;
    }


}