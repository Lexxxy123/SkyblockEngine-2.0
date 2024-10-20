/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.mongodb.crypt.capi.MongoAwsKmsProviderOptions
 *  com.mongodb.crypt.capi.MongoCryptOptions
 *  com.mongodb.crypt.capi.MongoCryptOptions$Builder
 *  com.mongodb.crypt.capi.MongoLocalKmsProviderOptions
 */
package com.mongodb.internal.capi;

import com.mongodb.Block;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientException;
import com.mongodb.MongoClientSettings;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.SocketSettings;
import com.mongodb.crypt.capi.MongoAwsKmsProviderOptions;
import com.mongodb.crypt.capi.MongoCryptOptions;
import com.mongodb.crypt.capi.MongoLocalKmsProviderOptions;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.bson.BsonDocument;

public final class MongoCryptHelper {
    public static MongoCryptOptions createMongoCryptOptions(Map<String, Map<String, Object>> kmsProviders, Map<String, BsonDocument> namespaceToLocalSchemaDocumentMap) {
        MongoCryptOptions.Builder mongoCryptOptionsBuilder = MongoCryptOptions.builder();
        for (Map.Entry<String, Map<String, Object>> entry : kmsProviders.entrySet()) {
            if (entry.getKey().equals("aws")) {
                mongoCryptOptionsBuilder.awsKmsProviderOptions(MongoAwsKmsProviderOptions.builder().accessKeyId((String)entry.getValue().get("accessKeyId")).secretAccessKey((String)entry.getValue().get("secretAccessKey")).build());
                continue;
            }
            if (entry.getKey().equals("local")) {
                mongoCryptOptionsBuilder.localKmsProviderOptions(MongoLocalKmsProviderOptions.builder().localMasterKey(ByteBuffer.wrap((byte[])entry.getValue().get("key"))).build());
                continue;
            }
            throw new MongoClientException("Unrecognized KMS provider key: " + entry.getKey());
        }
        mongoCryptOptionsBuilder.localSchemaMap(namespaceToLocalSchemaDocumentMap);
        return mongoCryptOptionsBuilder.build();
    }

    public static List<String> createMongocryptdSpawnArgs(Map<String, Object> options) {
        ArrayList<String> spawnArgs = new ArrayList<String>();
        String path = options.containsKey("mongocryptdSpawnPath") ? (String)options.get("mongocryptdSpawnPath") : "mongocryptd";
        spawnArgs.add(path);
        if (options.containsKey("mongocryptdSpawnArgs")) {
            spawnArgs.addAll((List)options.get("mongocryptdSpawnArgs"));
        }
        if (!spawnArgs.contains("--idleShutdownTimeoutSecs")) {
            spawnArgs.add("--idleShutdownTimeoutSecs");
            spawnArgs.add("60");
        }
        return spawnArgs;
    }

    public static MongoClientSettings createMongocryptdClientSettings(String connectionString) {
        return MongoClientSettings.builder().applyToClusterSettings(new Block<ClusterSettings.Builder>(){

            @Override
            public void apply(ClusterSettings.Builder builder) {
                builder.serverSelectionTimeout(10L, TimeUnit.SECONDS);
            }
        }).applyToSocketSettings(new Block<SocketSettings.Builder>(){

            @Override
            public void apply(SocketSettings.Builder builder) {
                builder.readTimeout(10, TimeUnit.SECONDS);
                builder.connectTimeout(10, TimeUnit.SECONDS);
            }
        }).applyConnectionString(new ConnectionString(connectionString != null ? connectionString : "mongodb://localhost:27020")).build();
    }

    public static ProcessBuilder createProcessBuilder(Map<String, Object> options) {
        return new ProcessBuilder(MongoCryptHelper.createMongocryptdSpawnArgs(options));
    }

    public static void startProcess(ProcessBuilder processBuilder) {
        try {
            processBuilder.redirectErrorStream(true);
            processBuilder.redirectOutput(new File(System.getProperty("os.name").startsWith("Windows") ? "NUL" : "/dev/null"));
            processBuilder.start();
        } catch (Throwable t2) {
            throw new MongoClientException("Exception starting mongocryptd process. Is `mongocryptd` on the system path?", t2);
        }
    }

    private MongoCryptHelper() {
    }
}

