/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.mongodb.crypt.capi.MongoCryptOptions
 *  com.mongodb.crypt.capi.MongoCrypts
 */
package com.mongodb.client.internal;

import com.mongodb.AutoEncryptionSettings;
import com.mongodb.ClientEncryptionSettings;
import com.mongodb.MongoClientException;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoNamespace;
import com.mongodb.client.MongoClients;
import com.mongodb.client.internal.CollectionInfoRetriever;
import com.mongodb.client.internal.CommandMarker;
import com.mongodb.client.internal.Crypt;
import com.mongodb.client.internal.KeyManagementService;
import com.mongodb.client.internal.KeyRetriever;
import com.mongodb.client.internal.SimpleMongoClient;
import com.mongodb.client.internal.SimpleMongoClients;
import com.mongodb.crypt.capi.MongoCryptOptions;
import com.mongodb.crypt.capi.MongoCrypts;
import com.mongodb.internal.capi.MongoCryptHelper;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;

public final class Crypts {
    public static Crypt createCrypt(SimpleMongoClient client, AutoEncryptionSettings options) {
        return new Crypt(MongoCrypts.create((MongoCryptOptions)MongoCryptHelper.createMongoCryptOptions(options.getKmsProviders(), options.getSchemaMap())), new CollectionInfoRetriever(client), new CommandMarker(options.isBypassAutoEncryption(), options.getExtraOptions()), Crypts.createKeyRetriever(client, options.getKeyVaultMongoClientSettings(), options.getKeyVaultNamespace()), Crypts.createKeyManagementService(), options.isBypassAutoEncryption());
    }

    static Crypt create(SimpleMongoClient keyVaultClient, ClientEncryptionSettings options) {
        return new Crypt(MongoCrypts.create((MongoCryptOptions)MongoCryptHelper.createMongoCryptOptions(options.getKmsProviders(), null)), Crypts.createKeyRetriever(keyVaultClient, false, options.getKeyVaultNamespace()), Crypts.createKeyManagementService());
    }

    private static KeyRetriever createKeyRetriever(SimpleMongoClient defaultKeyVaultClient, MongoClientSettings keyVaultMongoClientSettings, String keyVaultNamespaceString) {
        boolean keyRetrieverOwnsClient;
        SimpleMongoClient keyVaultClient;
        if (keyVaultMongoClientSettings != null) {
            keyVaultClient = SimpleMongoClients.create(MongoClients.create(keyVaultMongoClientSettings));
            keyRetrieverOwnsClient = true;
        } else {
            keyVaultClient = defaultKeyVaultClient;
            keyRetrieverOwnsClient = false;
        }
        return Crypts.createKeyRetriever(keyVaultClient, keyRetrieverOwnsClient, keyVaultNamespaceString);
    }

    private static KeyRetriever createKeyRetriever(SimpleMongoClient keyVaultClient, boolean keyRetrieverOwnsClient, String keyVaultNamespaceString) {
        return new KeyRetriever(keyVaultClient, keyRetrieverOwnsClient, new MongoNamespace(keyVaultNamespaceString));
    }

    private static KeyManagementService createKeyManagementService() {
        return new KeyManagementService(Crypts.getSslContext(), 443, 10000);
    }

    private static SSLContext getSslContext() {
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getDefault();
        } catch (NoSuchAlgorithmException e2) {
            throw new MongoClientException("Unable to create default SSLContext", e2);
        }
        return sslContext;
    }

    private Crypts() {
    }
}

