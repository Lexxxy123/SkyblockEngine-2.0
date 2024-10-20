/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

@Deprecated
public class ParallelCollectionScanOptions {
    private int batchSize;

    public int getBatchSize() {
        return this.batchSize;
    }

    public ParallelCollectionScanOptions batchSize(int batchSize) {
        this.batchSize = batchSize;
        return this;
    }
}

