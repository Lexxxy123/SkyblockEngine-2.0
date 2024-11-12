/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoDriverInformation;
import com.mongodb.assertions.Assertions;
import java.nio.charset.Charset;
import java.util.List;
import org.bson.BsonBinaryWriter;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.BsonWriter;
import org.bson.codecs.BsonDocumentCodec;
import org.bson.codecs.EncoderContext;
import org.bson.io.BasicOutputBuffer;

public final class ClientMetadataHelper {
    public static final BsonDocument CLIENT_METADATA_DOCUMENT = new BsonDocument();
    private static final String SEPARATOR = "|";
    private static final String APPLICATION_FIELD = "application";
    private static final String APPLICATION_NAME_FIELD = "name";
    private static final String DRIVER_FIELD = "driver";
    private static final String DRIVER_NAME_FIELD = "name";
    private static final String DRIVER_VERSION_FIELD = "version";
    private static final String PLATFORM_FIELD = "platform";
    private static final String OS_FIELD = "os";
    private static final String OS_TYPE_FIELD = "type";
    private static final String OS_NAME_FIELD = "name";
    private static final String OS_ARCHITECTURE_FIELD = "architecture";
    private static final String OS_VERSION_FIELD = "version";
    private static final int MAXIMUM_CLIENT_METADATA_ENCODED_SIZE = 512;

    private static String getOperatingSystemType(String operatingSystemName) {
        if (ClientMetadataHelper.nameMatches(operatingSystemName, "linux")) {
            return "Linux";
        }
        if (ClientMetadataHelper.nameMatches(operatingSystemName, "mac")) {
            return "Darwin";
        }
        if (ClientMetadataHelper.nameMatches(operatingSystemName, "windows")) {
            return "Windows";
        }
        if (ClientMetadataHelper.nameMatches(operatingSystemName, "hp-ux", "aix", "irix", "solaris", "sunos")) {
            return "Unix";
        }
        return "unknown";
    }

    private static boolean nameMatches(String name, String ... prefixes) {
        for (String prefix : prefixes) {
            if (!name.toLowerCase().startsWith(prefix.toLowerCase())) continue;
            return true;
        }
        return false;
    }

    static BsonDocument createClientMetadataDocument(String applicationName) {
        return ClientMetadataHelper.createClientMetadataDocument(applicationName, null);
    }

    public static BsonDocument createClientMetadataDocument(String applicationName, MongoDriverInformation mongoDriverInformation) {
        return ClientMetadataHelper.createClientMetadataDocument(applicationName, mongoDriverInformation, CLIENT_METADATA_DOCUMENT);
    }

    static BsonDocument createClientMetadataDocument(String applicationName, MongoDriverInformation mongoDriverInformation, BsonDocument templateDocument) {
        if (applicationName != null) {
            Assertions.isTrueArgument("applicationName UTF-8 encoding length <= 128", applicationName.getBytes(Charset.forName("UTF-8")).length <= 128);
        }
        BsonDocument document = templateDocument.clone();
        if (applicationName != null) {
            document.append(APPLICATION_FIELD, new BsonDocument("name", new BsonString(applicationName)));
        }
        if (mongoDriverInformation != null) {
            ClientMetadataHelper.addDriverInformation(mongoDriverInformation, document);
        }
        if (ClientMetadataHelper.clientMetadataDocumentTooLarge(document)) {
            BsonDocument operatingSystemDocument = document.getDocument(OS_FIELD, null);
            if (operatingSystemDocument != null) {
                operatingSystemDocument.remove("version");
                operatingSystemDocument.remove(OS_ARCHITECTURE_FIELD);
                operatingSystemDocument.remove("name");
            }
            if (operatingSystemDocument == null || ClientMetadataHelper.clientMetadataDocumentTooLarge(document)) {
                document.remove(PLATFORM_FIELD);
                if (ClientMetadataHelper.clientMetadataDocumentTooLarge(document)) {
                    document = new BsonDocument(DRIVER_FIELD, templateDocument.getDocument(DRIVER_FIELD));
                    document.append(OS_FIELD, new BsonDocument(OS_TYPE_FIELD, new BsonString("unknown")));
                    if (ClientMetadataHelper.clientMetadataDocumentTooLarge(document)) {
                        document = null;
                    }
                }
            }
        }
        return document;
    }

    private static BsonDocument addDriverInformation(MongoDriverInformation mongoDriverInformation, BsonDocument document) {
        MongoDriverInformation driverInformation = ClientMetadataHelper.getDriverInformation(mongoDriverInformation);
        BsonDocument driverMetadataDocument = new BsonDocument("name", ClientMetadataHelper.listToBsonString(driverInformation.getDriverNames())).append("version", ClientMetadataHelper.listToBsonString(driverInformation.getDriverVersions()));
        document.append(DRIVER_FIELD, driverMetadataDocument);
        document.append(PLATFORM_FIELD, ClientMetadataHelper.listToBsonString(driverInformation.getDriverPlatforms()));
        return document;
    }

    static boolean clientMetadataDocumentTooLarge(BsonDocument document) {
        BasicOutputBuffer buffer = new BasicOutputBuffer(512);
        new BsonDocumentCodec().encode((BsonWriter)new BsonBinaryWriter(buffer), document, EncoderContext.builder().build());
        return buffer.getPosition() > 512;
    }

    static MongoDriverInformation getDriverInformation(MongoDriverInformation mongoDriverInformation) {
        MongoDriverInformation.Builder builder = mongoDriverInformation != null ? MongoDriverInformation.builder(mongoDriverInformation) : MongoDriverInformation.builder();
        return builder.driverName("mongo-java-driver").driverVersion("3.12.14").driverPlatform(String.format("Java/%s/%s", System.getProperty("java.vendor", "unknown-vendor"), System.getProperty("java.runtime.version", "unknown-version"))).build();
    }

    static BsonString listToBsonString(List<String> listOfStrings) {
        StringBuilder stringBuilder = new StringBuilder();
        int i2 = 0;
        for (String val : listOfStrings) {
            if (i2 > 0) {
                stringBuilder.append(SEPARATOR);
            }
            stringBuilder.append(val);
            ++i2;
        }
        return new BsonString(stringBuilder.toString());
    }

    private ClientMetadataHelper() {
    }

    static {
        BsonDocument driverMetadataDocument = ClientMetadataHelper.addDriverInformation(null, new BsonDocument());
        CLIENT_METADATA_DOCUMENT.append(DRIVER_FIELD, driverMetadataDocument.get(DRIVER_FIELD));
        try {
            String operatingSystemName = System.getProperty("os.name", "unknown");
            CLIENT_METADATA_DOCUMENT.append(OS_FIELD, new BsonDocument().append(OS_TYPE_FIELD, new BsonString(ClientMetadataHelper.getOperatingSystemType(operatingSystemName))).append("name", new BsonString(operatingSystemName)).append(OS_ARCHITECTURE_FIELD, new BsonString(System.getProperty("os.arch", "unknown"))).append("version", new BsonString(System.getProperty("os.version", "unknown")))).append(PLATFORM_FIELD, driverMetadataDocument.get(PLATFORM_FIELD, new BsonString("")));
        } catch (SecurityException securityException) {
            // empty catch block
        }
    }
}

