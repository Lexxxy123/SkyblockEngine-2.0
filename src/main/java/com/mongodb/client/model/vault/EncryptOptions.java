/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model.vault;

import org.bson.BsonBinary;

public class EncryptOptions {
    private BsonBinary keyId;
    private String keyAltName;
    private final String algorithm;

    public EncryptOptions(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getAlgorithm() {
        return this.algorithm;
    }

    public BsonBinary getKeyId() {
        return this.keyId;
    }

    public String getKeyAltName() {
        return this.keyAltName;
    }

    public EncryptOptions keyId(BsonBinary keyId) {
        this.keyId = keyId;
        return this;
    }

    public EncryptOptions keyAltName(String keyAltName) {
        this.keyAltName = keyAltName;
        return this;
    }

    public String toString() {
        return "EncryptOptions{keyId=" + this.keyId + ", keyAltName=" + this.keyAltName + ", algorithm='" + this.algorithm + "'}";
    }
}

