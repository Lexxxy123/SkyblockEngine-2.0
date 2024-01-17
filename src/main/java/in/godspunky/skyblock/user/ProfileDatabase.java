package in.godspunky.skyblock.user;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
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

    public void set(String key, Object value) {
        MongoCollection<Document> userCollection = DatabaseManager.getCollection("profiles");
        Document updateDoc = new Document("$set", new Document(key, value));
        userCollection.updateOne(new Document("_id", id), updateDoc);
    }

    public Object get(String key, Object def) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        if (doc == null) {
            return def;
        }
        return doc.get(key);
    }

}
