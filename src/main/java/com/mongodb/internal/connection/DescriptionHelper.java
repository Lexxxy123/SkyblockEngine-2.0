/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.ServerAddress;
import com.mongodb.Tag;
import com.mongodb.TagSet;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.connection.ConnectionId;
import com.mongodb.connection.ServerConnectionState;
import com.mongodb.connection.ServerDescription;
import com.mongodb.connection.ServerType;
import com.mongodb.connection.ServerVersion;
import com.mongodb.internal.connection.CommandHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.bson.BsonArray;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.types.ObjectId;

public final class DescriptionHelper {
    static ConnectionDescription createConnectionDescription(ConnectionId connectionId, BsonDocument isMasterResult, BsonDocument buildInfoResult) {
        ConnectionDescription connectionDescription = new ConnectionDescription(connectionId, DescriptionHelper.getVersion(buildInfoResult), DescriptionHelper.getMaxWireVersion(isMasterResult), DescriptionHelper.getServerType(isMasterResult), DescriptionHelper.getMaxWriteBatchSize(isMasterResult), DescriptionHelper.getMaxBsonObjectSize(isMasterResult), DescriptionHelper.getMaxMessageSizeBytes(isMasterResult), DescriptionHelper.getCompressors(isMasterResult));
        if (isMasterResult.containsKey("connectionId")) {
            ConnectionId newConnectionId = connectionDescription.getConnectionId().withServerValue(isMasterResult.getNumber("connectionId").intValue());
            connectionDescription = connectionDescription.withConnectionId(newConnectionId);
        }
        return connectionDescription;
    }

    public static ServerDescription createServerDescription(ServerAddress serverAddress, BsonDocument isMasterResult, ServerVersion serverVersion, long roundTripTime) {
        return ServerDescription.builder().state(ServerConnectionState.CONNECTED).version(serverVersion).address(serverAddress).type(DescriptionHelper.getServerType(isMasterResult)).canonicalAddress(isMasterResult.containsKey("me") ? isMasterResult.getString("me").getValue() : null).hosts(DescriptionHelper.listToSet(isMasterResult.getArray("hosts", new BsonArray()))).passives(DescriptionHelper.listToSet(isMasterResult.getArray("passives", new BsonArray()))).arbiters(DescriptionHelper.listToSet(isMasterResult.getArray("arbiters", new BsonArray()))).primary(DescriptionHelper.getString(isMasterResult, "primary")).maxDocumentSize(DescriptionHelper.getMaxBsonObjectSize(isMasterResult)).tagSet(DescriptionHelper.getTagSetFromDocument(isMasterResult.getDocument("tags", new BsonDocument()))).setName(DescriptionHelper.getString(isMasterResult, "setName")).minWireVersion(DescriptionHelper.getMinWireVersion(isMasterResult)).maxWireVersion(DescriptionHelper.getMaxWireVersion(isMasterResult)).electionId(DescriptionHelper.getElectionId(isMasterResult)).setVersion(DescriptionHelper.getSetVersion(isMasterResult)).lastWriteDate(DescriptionHelper.getLastWriteDate(isMasterResult)).roundTripTime(roundTripTime, TimeUnit.NANOSECONDS).logicalSessionTimeoutMinutes(DescriptionHelper.getLogicalSessionTimeoutMinutes(isMasterResult)).ok(CommandHelper.isCommandOk(isMasterResult)).build();
    }

    private static int getMinWireVersion(BsonDocument isMasterResult) {
        return isMasterResult.getInt32("minWireVersion", new BsonInt32(ServerDescription.getDefaultMinWireVersion())).getValue();
    }

    private static int getMaxWireVersion(BsonDocument isMasterResult) {
        return isMasterResult.getInt32("maxWireVersion", new BsonInt32(ServerDescription.getDefaultMaxWireVersion())).getValue();
    }

    private static Date getLastWriteDate(BsonDocument isMasterResult) {
        if (!isMasterResult.containsKey("lastWrite")) {
            return null;
        }
        return new Date(isMasterResult.getDocument("lastWrite").getDateTime("lastWriteDate").getValue());
    }

    private static ObjectId getElectionId(BsonDocument isMasterResult) {
        return isMasterResult.containsKey("electionId") ? isMasterResult.getObjectId("electionId").getValue() : null;
    }

    private static Integer getSetVersion(BsonDocument isMasterResult) {
        return isMasterResult.containsKey("setVersion") ? Integer.valueOf(isMasterResult.getNumber("setVersion").intValue()) : null;
    }

    private static int getMaxMessageSizeBytes(BsonDocument isMasterResult) {
        return isMasterResult.getInt32("maxMessageSizeBytes", new BsonInt32(ConnectionDescription.getDefaultMaxMessageSize())).getValue();
    }

    private static int getMaxBsonObjectSize(BsonDocument isMasterResult) {
        return isMasterResult.getInt32("maxBsonObjectSize", new BsonInt32(ServerDescription.getDefaultMaxDocumentSize())).getValue();
    }

    private static int getMaxWriteBatchSize(BsonDocument isMasterResult) {
        return isMasterResult.getInt32("maxWriteBatchSize", new BsonInt32(ConnectionDescription.getDefaultMaxWriteBatchSize())).getValue();
    }

    private static Integer getLogicalSessionTimeoutMinutes(BsonDocument isMasterResult) {
        return isMasterResult.isNumber("logicalSessionTimeoutMinutes") ? Integer.valueOf(isMasterResult.getNumber("logicalSessionTimeoutMinutes").intValue()) : null;
    }

    private static String getString(BsonDocument response, String key) {
        if (response.containsKey(key)) {
            return response.getString(key).getValue();
        }
        return null;
    }

    static ServerVersion getVersion(BsonDocument buildInfoResult) {
        List<BsonValue> versionArray = buildInfoResult.getArray("versionArray").subList(0, 3);
        return new ServerVersion(Arrays.asList(versionArray.get(0).asInt32().getValue(), versionArray.get(1).asInt32().getValue(), versionArray.get(2).asInt32().getValue()));
    }

    private static Set<String> listToSet(BsonArray array) {
        if (array == null || array.isEmpty()) {
            return Collections.emptySet();
        }
        HashSet<String> set = new HashSet<String>();
        for (BsonValue value : array) {
            set.add(value.asString().getValue());
        }
        return set;
    }

    private static ServerType getServerType(BsonDocument isMasterResult) {
        if (!CommandHelper.isCommandOk(isMasterResult)) {
            return ServerType.UNKNOWN;
        }
        if (DescriptionHelper.isReplicaSetMember(isMasterResult)) {
            if (isMasterResult.getBoolean("hidden", BsonBoolean.FALSE).getValue()) {
                return ServerType.REPLICA_SET_OTHER;
            }
            if (isMasterResult.getBoolean("ismaster", BsonBoolean.FALSE).getValue()) {
                return ServerType.REPLICA_SET_PRIMARY;
            }
            if (isMasterResult.getBoolean("secondary", BsonBoolean.FALSE).getValue()) {
                return ServerType.REPLICA_SET_SECONDARY;
            }
            if (isMasterResult.getBoolean("arbiterOnly", BsonBoolean.FALSE).getValue()) {
                return ServerType.REPLICA_SET_ARBITER;
            }
            if (isMasterResult.containsKey("setName") && isMasterResult.containsKey("hosts")) {
                return ServerType.REPLICA_SET_OTHER;
            }
            return ServerType.REPLICA_SET_GHOST;
        }
        if (isMasterResult.containsKey("msg") && isMasterResult.get("msg").equals(new BsonString("isdbgrid"))) {
            return ServerType.SHARD_ROUTER;
        }
        return ServerType.STANDALONE;
    }

    private static boolean isReplicaSetMember(BsonDocument isMasterResult) {
        return isMasterResult.containsKey("setName") || isMasterResult.getBoolean("isreplicaset", BsonBoolean.FALSE).getValue();
    }

    private static TagSet getTagSetFromDocument(BsonDocument tagsDocuments) {
        ArrayList<Tag> tagList = new ArrayList<Tag>();
        for (Map.Entry<String, BsonValue> curEntry : tagsDocuments.entrySet()) {
            tagList.add(new Tag(curEntry.getKey(), curEntry.getValue().asString().getValue()));
        }
        return new TagSet(tagList);
    }

    private static List<String> getCompressors(BsonDocument isMasterResult) {
        ArrayList<String> compressorList = new ArrayList<String>();
        for (BsonValue compressor : isMasterResult.getArray("compression", new BsonArray())) {
            compressorList.add(compressor.asString().getValue());
        }
        return compressorList;
    }

    private DescriptionHelper() {
    }
}

