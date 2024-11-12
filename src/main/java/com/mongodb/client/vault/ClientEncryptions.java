/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.vault;

import com.mongodb.ClientEncryptionSettings;
import com.mongodb.client.internal.ClientEncryptionImpl;
import com.mongodb.client.vault.ClientEncryption;

public final class ClientEncryptions {
    public static ClientEncryption create(ClientEncryptionSettings options) {
        return new ClientEncryptionImpl(options);
    }

    private ClientEncryptions() {
    }
}

