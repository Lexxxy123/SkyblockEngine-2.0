/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.assertions.Assertions;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.IndexOptionDefaults;
import com.mongodb.client.model.ValidationOptions;
import com.mongodb.lang.Nullable;
import org.bson.conversions.Bson;

public class CreateCollectionOptions {
    private boolean autoIndex = true;
    private long maxDocuments;
    private boolean capped;
    private long sizeInBytes;
    private Boolean usePowerOf2Sizes;
    private Bson storageEngineOptions;
    private IndexOptionDefaults indexOptionDefaults = new IndexOptionDefaults();
    private ValidationOptions validationOptions = new ValidationOptions();
    private Collation collation;

    @Deprecated
    public boolean isAutoIndex() {
        return this.autoIndex;
    }

    @Deprecated
    public CreateCollectionOptions autoIndex(boolean autoIndex) {
        this.autoIndex = autoIndex;
        return this;
    }

    public long getMaxDocuments() {
        return this.maxDocuments;
    }

    public CreateCollectionOptions maxDocuments(long maxDocuments) {
        this.maxDocuments = maxDocuments;
        return this;
    }

    public boolean isCapped() {
        return this.capped;
    }

    public CreateCollectionOptions capped(boolean capped) {
        this.capped = capped;
        return this;
    }

    public long getSizeInBytes() {
        return this.sizeInBytes;
    }

    public CreateCollectionOptions sizeInBytes(long sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
        return this;
    }

    @Deprecated
    @Nullable
    public Boolean isUsePowerOf2Sizes() {
        return this.usePowerOf2Sizes;
    }

    @Deprecated
    public CreateCollectionOptions usePowerOf2Sizes(@Nullable Boolean usePowerOf2Sizes) {
        this.usePowerOf2Sizes = usePowerOf2Sizes;
        return this;
    }

    @Nullable
    public Bson getStorageEngineOptions() {
        return this.storageEngineOptions;
    }

    public CreateCollectionOptions storageEngineOptions(@Nullable Bson storageEngineOptions) {
        this.storageEngineOptions = storageEngineOptions;
        return this;
    }

    public IndexOptionDefaults getIndexOptionDefaults() {
        return this.indexOptionDefaults;
    }

    public CreateCollectionOptions indexOptionDefaults(IndexOptionDefaults indexOptionDefaults) {
        this.indexOptionDefaults = Assertions.notNull("indexOptionDefaults", indexOptionDefaults);
        return this;
    }

    public ValidationOptions getValidationOptions() {
        return this.validationOptions;
    }

    public CreateCollectionOptions validationOptions(ValidationOptions validationOptions) {
        this.validationOptions = Assertions.notNull("validationOptions", validationOptions);
        return this;
    }

    @Nullable
    public Collation getCollation() {
        return this.collation;
    }

    public CreateCollectionOptions collation(@Nullable Collation collation) {
        this.collation = collation;
        return this;
    }

    public String toString() {
        return "CreateCollectionOptions{autoIndex=" + this.autoIndex + ", maxDocuments=" + this.maxDocuments + ", capped=" + this.capped + ", sizeInBytes=" + this.sizeInBytes + ", usePowerOf2Sizes=" + this.usePowerOf2Sizes + ", storageEngineOptions=" + this.storageEngineOptions + ", indexOptionDefaults=" + this.indexOptionDefaults + ", validationOptions=" + this.validationOptions + ", collation=" + this.collation + '}';
    }
}

