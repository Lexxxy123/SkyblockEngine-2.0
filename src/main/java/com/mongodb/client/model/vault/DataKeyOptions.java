/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model.vault;

import java.util.List;
import org.bson.BsonDocument;

public class DataKeyOptions {
    private List<String> keyAltNames;
    private BsonDocument masterKey;

    public DataKeyOptions keyAltNames(List<String> keyAltNames) {
        this.keyAltNames = keyAltNames;
        return this;
    }

    public DataKeyOptions masterKey(BsonDocument masterKey) {
        this.masterKey = masterKey;
        return this;
    }

    public List<String> getKeyAltNames() {
        return this.keyAltNames;
    }

    public BsonDocument getMasterKey() {
        return this.masterKey;
    }

    public String toString() {
        return "DataKeyOptions{keyAltNames=" + this.keyAltNames + ", masterKey=" + this.masterKey + '}';
    }
}

