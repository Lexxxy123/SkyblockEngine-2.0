/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model.changestream;

import com.mongodb.MongoNamespace;
import com.mongodb.assertions.Assertions;
import com.mongodb.client.model.changestream.ChangeStreamDocumentCodec;
import com.mongodb.client.model.changestream.OperationType;
import com.mongodb.client.model.changestream.UpdateDescription;
import com.mongodb.lang.Nullable;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.BsonString;
import org.bson.BsonTimestamp;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

public final class ChangeStreamDocument<TDocument> {
    @BsonId
    private final BsonDocument resumeToken;
    private final BsonDocument namespaceDocument;
    private final BsonDocument destinationNamespaceDocument;
    private final TDocument fullDocument;
    private final BsonDocument documentKey;
    private final BsonTimestamp clusterTime;
    private final OperationType operationType;
    private final UpdateDescription updateDescription;
    private final BsonInt64 txnNumber;
    private final BsonDocument lsid;

    @Deprecated
    public ChangeStreamDocument(BsonDocument resumeToken, MongoNamespace namespace, TDocument fullDocument, BsonDocument documentKey, OperationType operationType, UpdateDescription updateDescription) {
        this(resumeToken, namespace, fullDocument, documentKey, null, operationType, updateDescription);
    }

    @Deprecated
    public ChangeStreamDocument(BsonDocument resumeToken, MongoNamespace namespace, TDocument fullDocument, BsonDocument documentKey, @Nullable BsonTimestamp clusterTime, OperationType operationType, UpdateDescription updateDescription) {
        this(resumeToken, ChangeStreamDocument.namespaceToDocument(namespace), fullDocument, documentKey, clusterTime, operationType, updateDescription);
    }

    @Deprecated
    public ChangeStreamDocument(@BsonProperty(value="resumeToken") BsonDocument resumeToken, @BsonProperty(value="ns") BsonDocument namespaceDocument, @BsonProperty(value="fullDocument") TDocument fullDocument, @BsonProperty(value="documentKey") BsonDocument documentKey, @Nullable @BsonProperty(value="clusterTime") BsonTimestamp clusterTime, @BsonProperty(value="operationType") OperationType operationType, @BsonProperty(value="updateDescription") UpdateDescription updateDescription) {
        this(operationType, resumeToken, namespaceDocument, null, fullDocument, documentKey, clusterTime, updateDescription);
    }

    @Deprecated
    public ChangeStreamDocument(@BsonProperty(value="operationType") OperationType operationType, @BsonProperty(value="resumeToken") BsonDocument resumeToken, @Nullable @BsonProperty(value="ns") BsonDocument namespaceDocument, @Nullable @BsonProperty(value="to") BsonDocument destinationNamespaceDocument, @Nullable @BsonProperty(value="fullDocument") TDocument fullDocument, @Nullable @BsonProperty(value="documentKey") BsonDocument documentKey, @Nullable @BsonProperty(value="clusterTime") BsonTimestamp clusterTime, @Nullable @BsonProperty(value="updateDescription") UpdateDescription updateDescription) {
        this(operationType, resumeToken, namespaceDocument, destinationNamespaceDocument, fullDocument, documentKey, clusterTime, updateDescription, null, null);
    }

    @BsonCreator
    public ChangeStreamDocument(@BsonProperty(value="operationType") OperationType operationType, @BsonProperty(value="resumeToken") BsonDocument resumeToken, @Nullable @BsonProperty(value="ns") BsonDocument namespaceDocument, @Nullable @BsonProperty(value="to") BsonDocument destinationNamespaceDocument, @Nullable @BsonProperty(value="fullDocument") TDocument fullDocument, @Nullable @BsonProperty(value="documentKey") BsonDocument documentKey, @Nullable @BsonProperty(value="clusterTime") BsonTimestamp clusterTime, @Nullable @BsonProperty(value="updateDescription") UpdateDescription updateDescription, @Nullable @BsonProperty(value="txnNumber") BsonInt64 txnNumber, @Nullable @BsonProperty(value="lsid") BsonDocument lsid) {
        this.resumeToken = resumeToken;
        this.namespaceDocument = namespaceDocument;
        this.destinationNamespaceDocument = destinationNamespaceDocument;
        this.documentKey = documentKey;
        this.fullDocument = fullDocument;
        this.clusterTime = clusterTime;
        this.operationType = operationType;
        this.updateDescription = updateDescription;
        this.txnNumber = txnNumber;
        this.lsid = lsid;
    }

    private static BsonDocument namespaceToDocument(MongoNamespace namespace) {
        Assertions.notNull("namespace", namespace);
        return new BsonDocument("db", new BsonString(namespace.getDatabaseName())).append("coll", new BsonString(namespace.getCollectionName()));
    }

    public BsonDocument getResumeToken() {
        return this.resumeToken;
    }

    @BsonIgnore
    @Nullable
    public MongoNamespace getNamespace() {
        if (this.namespaceDocument == null) {
            return null;
        }
        if (!this.namespaceDocument.containsKey("db") || !this.namespaceDocument.containsKey("coll")) {
            return null;
        }
        return new MongoNamespace(this.namespaceDocument.getString("db").getValue(), this.namespaceDocument.getString("coll").getValue());
    }

    @BsonProperty(value="ns")
    @Nullable
    public BsonDocument getNamespaceDocument() {
        return this.namespaceDocument;
    }

    @BsonIgnore
    @Nullable
    public MongoNamespace getDestinationNamespace() {
        if (this.destinationNamespaceDocument == null) {
            return null;
        }
        return new MongoNamespace(this.destinationNamespaceDocument.getString("db").getValue(), this.destinationNamespaceDocument.getString("coll").getValue());
    }

    @BsonProperty(value="to")
    @Nullable
    public BsonDocument getDestinationNamespaceDocument() {
        return this.destinationNamespaceDocument;
    }

    @BsonIgnore
    @Nullable
    public String getDatabaseName() {
        if (this.namespaceDocument == null) {
            return null;
        }
        if (!this.namespaceDocument.containsKey("db")) {
            return null;
        }
        return this.namespaceDocument.getString("db").getValue();
    }

    @Nullable
    public TDocument getFullDocument() {
        return this.fullDocument;
    }

    @Nullable
    public BsonDocument getDocumentKey() {
        return this.documentKey;
    }

    @Nullable
    public BsonTimestamp getClusterTime() {
        return this.clusterTime;
    }

    public OperationType getOperationType() {
        return this.operationType;
    }

    @Nullable
    public UpdateDescription getUpdateDescription() {
        return this.updateDescription;
    }

    @Nullable
    public BsonInt64 getTxnNumber() {
        return this.txnNumber;
    }

    @Nullable
    public BsonDocument getLsid() {
        return this.lsid;
    }

    public static <TFullDocument> Codec<ChangeStreamDocument<TFullDocument>> createCodec(Class<TFullDocument> fullDocumentClass, CodecRegistry codecRegistry) {
        return new ChangeStreamDocumentCodec<TFullDocument>(fullDocumentClass, codecRegistry);
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        ChangeStreamDocument that = (ChangeStreamDocument)o2;
        if (this.resumeToken != null ? !this.resumeToken.equals(that.resumeToken) : that.resumeToken != null) {
            return false;
        }
        if (this.namespaceDocument != null ? !this.namespaceDocument.equals(that.namespaceDocument) : that.namespaceDocument != null) {
            return false;
        }
        if (this.destinationNamespaceDocument != null ? !this.destinationNamespaceDocument.equals(that.destinationNamespaceDocument) : that.destinationNamespaceDocument != null) {
            return false;
        }
        if (this.fullDocument != null ? !this.fullDocument.equals(that.fullDocument) : that.fullDocument != null) {
            return false;
        }
        if (this.documentKey != null ? !this.documentKey.equals(that.documentKey) : that.documentKey != null) {
            return false;
        }
        if (this.clusterTime != null ? !this.clusterTime.equals(that.clusterTime) : that.clusterTime != null) {
            return false;
        }
        if (this.operationType != that.operationType) {
            return false;
        }
        if (this.updateDescription != null ? !this.updateDescription.equals(that.updateDescription) : that.updateDescription != null) {
            return false;
        }
        if (this.txnNumber != null ? !this.txnNumber.equals(that.txnNumber) : that.txnNumber != null) {
            return false;
        }
        return !(this.lsid != null ? !this.lsid.equals(that.lsid) : that.lsid != null);
    }

    public int hashCode() {
        int result = this.resumeToken != null ? this.resumeToken.hashCode() : 0;
        result = 31 * result + (this.namespaceDocument != null ? this.namespaceDocument.hashCode() : 0);
        result = 31 * result + (this.destinationNamespaceDocument != null ? this.destinationNamespaceDocument.hashCode() : 0);
        result = 31 * result + (this.fullDocument != null ? this.fullDocument.hashCode() : 0);
        result = 31 * result + (this.documentKey != null ? this.documentKey.hashCode() : 0);
        result = 31 * result + (this.clusterTime != null ? this.clusterTime.hashCode() : 0);
        result = 31 * result + (this.operationType != null ? this.operationType.hashCode() : 0);
        result = 31 * result + (this.updateDescription != null ? this.updateDescription.hashCode() : 0);
        result = 31 * result + (this.txnNumber != null ? this.txnNumber.hashCode() : 0);
        result = 31 * result + (this.lsid != null ? this.lsid.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "ChangeStreamDocument{ operationType=" + (Object)((Object)this.operationType) + ", resumeToken=" + this.resumeToken + ", namespace=" + this.getNamespace() + ", destinationNamespace=" + this.getDestinationNamespace() + ", fullDocument=" + this.fullDocument + ", documentKey=" + this.documentKey + ", clusterTime=" + this.clusterTime + ", updateDescription=" + this.updateDescription + ", txnNumber=" + this.txnNumber + ", lsid=" + this.lsid + "}";
    }
}

