/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.assertions.Assertions;
import com.mongodb.lang.Nullable;
import java.io.Serializable;

public class DBRef
implements Serializable {
    private static final long serialVersionUID = -849581217713362618L;
    private final Object id;
    private final String collectionName;
    private final String databaseName;

    public DBRef(String collectionName, Object id) {
        this(null, collectionName, id);
    }

    public DBRef(@Nullable String databaseName, String collectionName, Object id) {
        this.id = Assertions.notNull("id", id);
        this.collectionName = Assertions.notNull("collectionName", collectionName);
        this.databaseName = databaseName;
    }

    public Object getId() {
        return this.id;
    }

    public String getCollectionName() {
        return this.collectionName;
    }

    @Nullable
    public String getDatabaseName() {
        return this.databaseName;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        DBRef dbRef = (DBRef)o2;
        if (!this.id.equals(dbRef.id)) {
            return false;
        }
        if (!this.collectionName.equals(dbRef.collectionName)) {
            return false;
        }
        return !(this.databaseName != null ? !this.databaseName.equals(dbRef.databaseName) : dbRef.databaseName != null);
    }

    public int hashCode() {
        int result = this.id.hashCode();
        result = 31 * result + this.collectionName.hashCode();
        result = 31 * result + (this.databaseName != null ? this.databaseName.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "{ \"$ref\" : \"" + this.collectionName + "\", \"$id\" : \"" + this.id + "\"" + (this.databaseName == null ? "" : ", \"$db\" : \"" + this.databaseName + "\"") + " }";
    }
}

