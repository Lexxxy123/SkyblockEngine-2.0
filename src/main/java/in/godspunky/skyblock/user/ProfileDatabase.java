package in.godspunky.skyblock.user;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

public class ProfileDatabase {
    private final String uuid;
    private final boolean createIfNotExist;

    public ProfileDatabase(String uuid, boolean createIfNotExist) {
        this.uuid = uuid;
        this.createIfNotExist = createIfNotExist;
    }

    public boolean exists() {
        MongoCollection<Document> userCollection = DatabaseManager.getCollection("profiles");
        return userCollection.find(new Document("_id", uuid)).first() != null;
    }

    public Document getDocument() {
        MongoCollection<Document> userCollection = DatabaseManager.getCollection("profiles");
        return userCollection.find(new Document("_id", uuid)).first();
    }

    public void setProfileProperty(String key, Object value) {
        MongoCollection<Document> userCollection = DatabaseManager.getCollection("profiles");
        Document updateDoc = new Document("$set", new Document(key, value));
        userCollection.updateOne(new Document("_id", uuid), updateDoc);
    }

    public void set(String key, Object value) {
        insertOrUpdate(key, value);
    }


    public Object get(String key, Object def) {
        Document doc = getDocument();
        if (doc == null) {
            return def;
        }
        return doc.get(key);
    }

    public void insertOrUpdate(String key, Object value) {
        MongoCollection<Document> collection = DatabaseManager.getCollection("profiles");
        if (exists()) {
            Document query = new Document("_id", uuid);
            Document found = collection.find(query).first();

            assert found != null;
            collection.updateOne(found, Updates.set(key, value));
            return;
        }
        Document New = new Document("_id", uuid);
        New.append(key, value);
        collection.insertOne(New);
    }
}
