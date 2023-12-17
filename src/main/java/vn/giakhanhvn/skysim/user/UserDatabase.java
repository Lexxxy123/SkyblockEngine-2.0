package vn.giakhanhvn.skysim.user;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import vn.giakhanhvn.skysim.item.SItem;
import vn.giakhanhvn.skysim.user.DatabaseManager;

public class UserDatabase {
    private final String uuid;
    private final boolean createIfNotExist;

    public UserDatabase(String uuid, boolean createIfNotExist) {
        this.uuid = uuid;
        this.createIfNotExist = createIfNotExist;
    }

    public boolean exists() {
        MongoCollection<Document> userCollection = DatabaseManager.getCollection("users");
        return userCollection.find(new Document("uuid", uuid)).first() != null;
    }

    public Document getDocument() {
        MongoCollection<Document> userCollection = DatabaseManager.getCollection("users");
        return userCollection.find(new Document("uuid", uuid)).first();
    }

    public void setUserProperty(String key, Object value) {
        MongoCollection<Document> userCollection = DatabaseManager.getCollection("users");
        Document updateDoc = new Document("$set", new Document(key, value));
        userCollection.updateOne(new Document("uuid", uuid), updateDoc);
    }
}
