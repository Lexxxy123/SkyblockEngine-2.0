package in.godspunky.skyblock.user;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class ProfileDatabase {
    public static final MongoCollection<Document> collection = DatabaseManager.getCollection("profiles");
    public final String id;


    public ProfileDatabase(String uuid, boolean idk) {
        this.id = uuid;
    }

    public boolean exists() {
        MongoCollection<Document> userCollection = DatabaseManager.getCollection("users");
        return userCollection.find(new Document("uuid", id)).first() != null;
    }

    public Document getDocument() {
        MongoCollection<Document> userCollection = DatabaseManager.getCollection("users");
        return userCollection.find(new Document("uuid", id)).first();
    }
}
