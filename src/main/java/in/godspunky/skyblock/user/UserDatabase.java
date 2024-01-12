package in.godspunky.skyblock.user;

import com.mongodb.client.MongoCollection;
import org.bson.Document;


public class UserDatabase {
    public static final MongoCollection<Document> collection = DatabaseManager.getCollection("users");
    public final String id;


    public UserDatabase(String uuid) {
        this.id = uuid;
    }

    public UserDatabase(String uuid, boolean idk) {
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

    public void setUserProperty(String key, Object value) {
        MongoCollection<Document> userCollection = DatabaseManager.getCollection("users");
        Document updateDoc = new Document("$set", new Document(key, value));
        userCollection.updateOne(new Document("uuid", id), updateDoc);
    }
}