/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.annotations.Immutable;
import com.mongodb.assertions.Assertions;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Immutable
public final class MongoNamespace {
    public static final String COMMAND_COLLECTION_NAME = "$cmd";
    private static final Set<Character> PROHIBITED_CHARACTERS_IN_DATABASE_NAME = new HashSet<Character>(Arrays.asList(Character.valueOf('\u0000'), Character.valueOf('/'), Character.valueOf('\\'), Character.valueOf(' '), Character.valueOf('\"'), Character.valueOf('.')));
    private final String databaseName;
    private final String collectionName;
    @BsonIgnore
    private final String fullName;

    public static void checkDatabaseNameValidity(String databaseName) {
        Assertions.notNull("databaseName", databaseName);
        Assertions.isTrueArgument("databaseName is not empty", !databaseName.isEmpty());
        for (int i2 = 0; i2 < databaseName.length(); ++i2) {
            if (!PROHIBITED_CHARACTERS_IN_DATABASE_NAME.contains(Character.valueOf(databaseName.charAt(i2)))) continue;
            throw new IllegalArgumentException("state should be: databaseName does not contain '" + databaseName.charAt(i2) + "'");
        }
    }

    public static void checkCollectionNameValidity(String collectionName) {
        Assertions.notNull("collectionName", collectionName);
        Assertions.isTrueArgument("collectionName is not empty", !collectionName.isEmpty());
    }

    public MongoNamespace(String fullName) {
        Assertions.notNull("fullName", fullName);
        this.fullName = fullName;
        this.databaseName = MongoNamespace.getDatatabaseNameFromFullName(fullName);
        this.collectionName = MongoNamespace.getCollectionNameFullName(fullName);
        MongoNamespace.checkDatabaseNameValidity(this.databaseName);
        MongoNamespace.checkCollectionNameValidity(this.collectionName);
    }

    @BsonCreator
    public MongoNamespace(@BsonProperty(value="db") String databaseName, @BsonProperty(value="coll") String collectionName) {
        MongoNamespace.checkDatabaseNameValidity(databaseName);
        MongoNamespace.checkCollectionNameValidity(collectionName);
        this.databaseName = databaseName;
        this.collectionName = collectionName;
        this.fullName = databaseName + '.' + collectionName;
    }

    @BsonProperty(value="db")
    public String getDatabaseName() {
        return this.databaseName;
    }

    @BsonProperty(value="coll")
    public String getCollectionName() {
        return this.collectionName;
    }

    public String getFullName() {
        return this.fullName;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        MongoNamespace that = (MongoNamespace)o2;
        if (!this.collectionName.equals(that.collectionName)) {
            return false;
        }
        return this.databaseName.equals(that.databaseName);
    }

    public String toString() {
        return this.fullName;
    }

    public int hashCode() {
        int result = this.databaseName.hashCode();
        result = 31 * result + this.collectionName.hashCode();
        return result;
    }

    private static String getCollectionNameFullName(String namespace) {
        int firstDot = namespace.indexOf(46);
        if (firstDot == -1) {
            return namespace;
        }
        return namespace.substring(firstDot + 1);
    }

    private static String getDatatabaseNameFromFullName(String namespace) {
        int firstDot = namespace.indexOf(46);
        if (firstDot == -1) {
            return "";
        }
        return namespace.substring(0, firstDot);
    }
}

