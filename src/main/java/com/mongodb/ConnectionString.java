/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.AuthenticationMechanism;
import com.mongodb.MongoCompressor;
import com.mongodb.MongoConfigurationException;
import com.mongodb.MongoCredential;
import com.mongodb.MongoNamespace;
import com.mongodb.ReadConcern;
import com.mongodb.ReadConcernLevel;
import com.mongodb.ReadPreference;
import com.mongodb.Tag;
import com.mongodb.TagSet;
import com.mongodb.WriteConcern;
import com.mongodb.diagnostics.logging.Logger;
import com.mongodb.diagnostics.logging.Loggers;
import com.mongodb.internal.dns.DefaultDnsResolver;
import com.mongodb.lang.Nullable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.bson.UuidRepresentation;

public class ConnectionString {
    private static final String MONGODB_PREFIX = "mongodb://";
    private static final String MONGODB_SRV_PREFIX = "mongodb+srv://";
    private static final Set<String> ALLOWED_OPTIONS_IN_TXT_RECORD = new HashSet<String>(Arrays.asList("authsource", "replicaset"));
    private static final String UTF_8 = "UTF-8";
    private static final Logger LOGGER = Loggers.getLogger("uri");
    private final MongoCredential credential;
    private final boolean isSrvProtocol;
    private final List<String> hosts;
    private final String database;
    private final String collection;
    private final String connectionString;
    private ReadPreference readPreference;
    private WriteConcern writeConcern;
    private Boolean retryWrites;
    private Boolean retryReads;
    private ReadConcern readConcern;
    private Integer minConnectionPoolSize;
    private Integer maxConnectionPoolSize;
    private Integer threadsAllowedToBlockForConnectionMultiplier;
    private Integer maxWaitTime;
    private Integer maxConnectionIdleTime;
    private Integer maxConnectionLifeTime;
    private Integer connectTimeout;
    private Integer socketTimeout;
    private Boolean sslEnabled;
    private Boolean sslInvalidHostnameAllowed;
    private String streamType;
    private String requiredReplicaSetName;
    private Integer serverSelectionTimeout;
    private Integer localThreshold;
    private Integer heartbeatFrequency;
    private String applicationName;
    private List<MongoCompressor> compressorList;
    private UuidRepresentation uuidRepresentation;
    private static final Set<String> GENERAL_OPTIONS_KEYS = new LinkedHashSet<String>();
    private static final Set<String> AUTH_KEYS = new HashSet<String>();
    private static final Set<String> READ_PREFERENCE_KEYS = new HashSet<String>();
    private static final Set<String> WRITE_CONCERN_KEYS = new HashSet<String>();
    private static final Set<String> COMPRESSOR_KEYS = new HashSet<String>();
    private static final Set<String> ALL_KEYS = new HashSet<String>();
    private static final Set<String> TRUE_VALUES;
    private static final Set<String> FALSE_VALUES;

    public ConnectionString(String connectionString) {
        String nsPart;
        String hostIdentifier;
        String userAndHostInformation;
        this.connectionString = connectionString;
        boolean isMongoDBProtocol = connectionString.startsWith(MONGODB_PREFIX);
        this.isSrvProtocol = connectionString.startsWith(MONGODB_SRV_PREFIX);
        if (!isMongoDBProtocol && !this.isSrvProtocol) {
            throw new IllegalArgumentException(String.format("The connection string is invalid. Connection strings must start with either '%s' or '%s", MONGODB_PREFIX, MONGODB_SRV_PREFIX));
        }
        String unprocessedConnectionString = isMongoDBProtocol ? connectionString.substring(MONGODB_PREFIX.length()) : connectionString.substring(MONGODB_SRV_PREFIX.length());
        int idx = unprocessedConnectionString.indexOf("/");
        if (idx == -1) {
            if (unprocessedConnectionString.contains("?")) {
                throw new IllegalArgumentException("The connection string contains options without trailing slash");
            }
            userAndHostInformation = unprocessedConnectionString;
            unprocessedConnectionString = "";
        } else {
            userAndHostInformation = unprocessedConnectionString.substring(0, idx);
            unprocessedConnectionString = unprocessedConnectionString.substring(idx + 1);
        }
        String userName = null;
        char[] password = null;
        idx = userAndHostInformation.lastIndexOf("@");
        if (idx > 0) {
            String userInfo = userAndHostInformation.substring(0, idx).replace("+", "%2B");
            hostIdentifier = userAndHostInformation.substring(idx + 1);
            int colonCount = this.countOccurrences(userInfo, ":");
            if (userInfo.contains("@") || colonCount > 1) {
                throw new IllegalArgumentException("The connection string contains invalid user information. If the username or password contains a colon (:) or an at-sign (@) then it must be urlencoded");
            }
            if (colonCount == 0) {
                userName = this.urldecode(userInfo);
            } else {
                idx = userInfo.indexOf(":");
                userName = this.urldecode(userInfo.substring(0, idx));
                password = this.urldecode(userInfo.substring(idx + 1), true).toCharArray();
            }
        } else {
            hostIdentifier = userAndHostInformation;
        }
        List<String> unresolvedHosts = Collections.unmodifiableList(this.parseHosts(Arrays.asList(hostIdentifier.split(","))));
        if (this.isSrvProtocol) {
            if (unresolvedHosts.size() > 1) {
                throw new IllegalArgumentException("Only one host allowed when using mongodb+srv protocol");
            }
            if (unresolvedHosts.get(0).contains(":")) {
                throw new IllegalArgumentException("Host for when using mongodb+srv protocol can not contain a port");
            }
        }
        this.hosts = unresolvedHosts;
        idx = unprocessedConnectionString.indexOf("?");
        if (idx == -1) {
            nsPart = unprocessedConnectionString;
            unprocessedConnectionString = "";
        } else {
            nsPart = unprocessedConnectionString.substring(0, idx);
            unprocessedConnectionString = unprocessedConnectionString.substring(idx + 1);
        }
        if (nsPart.length() > 0) {
            idx = (nsPart = this.urldecode(nsPart)).indexOf(".");
            if (idx < 0) {
                this.database = nsPart;
                this.collection = null;
            } else {
                this.database = nsPart.substring(0, idx);
                this.collection = nsPart.substring(idx + 1);
            }
            MongoNamespace.checkDatabaseNameValidity(this.database);
        } else {
            this.database = null;
            this.collection = null;
        }
        String txtRecordsQueryParameters = this.isSrvProtocol ? new DefaultDnsResolver().resolveAdditionalQueryParametersFromTxtRecords(unresolvedHosts.get(0)) : "";
        String connectionStringQueryParamenters = unprocessedConnectionString;
        Map<String, List<String>> connectionStringOptionsMap = this.parseOptions(connectionStringQueryParamenters);
        Map<String, List<String>> txtRecordsOptionsMap = this.parseOptions(txtRecordsQueryParameters);
        if (!ALLOWED_OPTIONS_IN_TXT_RECORD.containsAll(txtRecordsOptionsMap.keySet())) {
            throw new MongoConfigurationException(String.format("A TXT record is only permitted to contain the keys %s, but the TXT record for '%s' contains the keys %s", ALLOWED_OPTIONS_IN_TXT_RECORD, unresolvedHosts.get(0), txtRecordsOptionsMap.keySet()));
        }
        Map<String, List<String>> combinedOptionsMaps = this.combineOptionsMaps(txtRecordsOptionsMap, connectionStringOptionsMap);
        if (this.isSrvProtocol && !combinedOptionsMaps.containsKey("ssl")) {
            combinedOptionsMaps.put("ssl", Collections.singletonList("true"));
        }
        this.translateOptions(combinedOptionsMaps);
        this.credential = this.createCredentials(combinedOptionsMaps, userName, password);
        this.warnOnUnsupportedOptions(combinedOptionsMaps);
    }

    private Map<String, List<String>> combineOptionsMaps(Map<String, List<String>> txtRecordsOptionsMap, Map<String, List<String>> connectionStringOptionsMap) {
        HashMap<String, List<String>> combinedOptionsMaps = new HashMap<String, List<String>>(txtRecordsOptionsMap);
        for (Map.Entry<String, List<String>> entry : connectionStringOptionsMap.entrySet()) {
            combinedOptionsMaps.put(entry.getKey(), entry.getValue());
        }
        return combinedOptionsMaps;
    }

    private void warnOnUnsupportedOptions(Map<String, List<String>> optionsMap) {
        for (String key : optionsMap.keySet()) {
            if (ALL_KEYS.contains(key) || !LOGGER.isWarnEnabled()) continue;
            LOGGER.warn(String.format("Connection string contains unsupported option '%s'.", key));
        }
    }

    private void translateOptions(Map<String, List<String>> optionsMap) {
        boolean tlsInsecureSet = false;
        boolean tlsAllowInvalidHostnamesSet = false;
        for (String key : GENERAL_OPTIONS_KEYS) {
            String value = this.getLastValue(optionsMap, key);
            if (value == null) continue;
            if (key.equals("maxpoolsize")) {
                this.maxConnectionPoolSize = this.parseInteger(value, "maxpoolsize");
                continue;
            }
            if (key.equals("minpoolsize")) {
                this.minConnectionPoolSize = this.parseInteger(value, "minpoolsize");
                continue;
            }
            if (key.equals("maxidletimems")) {
                this.maxConnectionIdleTime = this.parseInteger(value, "maxidletimems");
                continue;
            }
            if (key.equals("maxlifetimems")) {
                this.maxConnectionLifeTime = this.parseInteger(value, "maxlifetimems");
                continue;
            }
            if (key.equals("waitqueuemultiple")) {
                this.threadsAllowedToBlockForConnectionMultiplier = this.parseInteger(value, "waitqueuemultiple");
                continue;
            }
            if (key.equals("waitqueuetimeoutms")) {
                this.maxWaitTime = this.parseInteger(value, "waitqueuetimeoutms");
                continue;
            }
            if (key.equals("connecttimeoutms")) {
                this.connectTimeout = this.parseInteger(value, "connecttimeoutms");
                continue;
            }
            if (key.equals("sockettimeoutms")) {
                this.socketTimeout = this.parseInteger(value, "sockettimeoutms");
                continue;
            }
            if (key.equals("tlsallowinvalidhostnames")) {
                this.sslInvalidHostnameAllowed = this.parseBoolean(value, "tlsAllowInvalidHostnames");
                tlsAllowInvalidHostnamesSet = true;
                continue;
            }
            if (key.equals("sslinvalidhostnameallowed")) {
                this.sslInvalidHostnameAllowed = this.parseBoolean(value, "sslinvalidhostnameallowed");
                tlsAllowInvalidHostnamesSet = true;
                continue;
            }
            if (key.equals("tlsinsecure")) {
                this.sslInvalidHostnameAllowed = this.parseBoolean(value, "tlsinsecure");
                tlsInsecureSet = true;
                continue;
            }
            if (key.equals("ssl")) {
                this.initializeSslEnabled("ssl", value);
                continue;
            }
            if (key.equals("tls")) {
                this.initializeSslEnabled("tls", value);
                continue;
            }
            if (key.equals("streamtype")) {
                this.streamType = value;
                LOGGER.warn("The streamType query parameter is deprecated and support for it will be removed in the next major release.");
                continue;
            }
            if (key.equals("replicaset")) {
                this.requiredReplicaSetName = value;
                continue;
            }
            if (key.equals("readconcernlevel")) {
                this.readConcern = new ReadConcern(ReadConcernLevel.fromString(value));
                continue;
            }
            if (key.equals("serverselectiontimeoutms")) {
                this.serverSelectionTimeout = this.parseInteger(value, "serverselectiontimeoutms");
                continue;
            }
            if (key.equals("localthresholdms")) {
                this.localThreshold = this.parseInteger(value, "localthresholdms");
                continue;
            }
            if (key.equals("heartbeatfrequencyms")) {
                this.heartbeatFrequency = this.parseInteger(value, "heartbeatfrequencyms");
                continue;
            }
            if (key.equals("appname")) {
                this.applicationName = value;
                continue;
            }
            if (key.equals("retrywrites")) {
                this.retryWrites = this.parseBoolean(value, "retrywrites");
                continue;
            }
            if (key.equals("retryreads")) {
                this.retryReads = this.parseBoolean(value, "retryreads");
                continue;
            }
            if (!key.equals("uuidrepresentation")) continue;
            this.uuidRepresentation = this.createUuidRepresentation(value);
        }
        if (tlsInsecureSet && tlsAllowInvalidHostnamesSet) {
            throw new IllegalArgumentException("tlsAllowInvalidHostnames or sslInvalidHostnameAllowed set along with tlsInsecure is not allowed");
        }
        this.writeConcern = this.createWriteConcern(optionsMap);
        this.readPreference = this.createReadPreference(optionsMap);
        this.compressorList = this.createCompressors(optionsMap);
    }

    private void initializeSslEnabled(String key, String value) {
        Boolean booleanValue = this.parseBoolean(value, key);
        if (this.sslEnabled != null && !this.sslEnabled.equals(booleanValue)) {
            throw new IllegalArgumentException("Conflicting tls and ssl parameter values are not allowed");
        }
        this.sslEnabled = booleanValue;
    }

    private List<MongoCompressor> createCompressors(Map<String, List<String>> optionsMap) {
        String compressors = "";
        Integer zlibCompressionLevel = null;
        for (String key : COMPRESSOR_KEYS) {
            String value = this.getLastValue(optionsMap, key);
            if (value == null) continue;
            if (key.equals("compressors")) {
                compressors = value;
                continue;
            }
            if (!key.equals("zlibcompressionlevel")) continue;
            zlibCompressionLevel = Integer.parseInt(value);
        }
        return this.buildCompressors(compressors, zlibCompressionLevel);
    }

    private List<MongoCompressor> buildCompressors(String compressors, @Nullable Integer zlibCompressionLevel) {
        ArrayList<MongoCompressor> compressorsList = new ArrayList<MongoCompressor>();
        for (String cur : compressors.split(",")) {
            if (cur.equals("zlib")) {
                MongoCompressor zlibCompressor = MongoCompressor.createZlibCompressor();
                if (zlibCompressionLevel != null) {
                    zlibCompressor = zlibCompressor.withProperty("LEVEL", zlibCompressionLevel);
                }
                compressorsList.add(zlibCompressor);
                continue;
            }
            if (cur.equals("snappy")) {
                compressorsList.add(MongoCompressor.createSnappyCompressor());
                continue;
            }
            if (cur.equals("zstd")) {
                compressorsList.add(MongoCompressor.createZstdCompressor());
                continue;
            }
            if (cur.isEmpty()) continue;
            throw new IllegalArgumentException("Unsupported compressor '" + cur + "'");
        }
        return Collections.unmodifiableList(compressorsList);
    }

    @Nullable
    private WriteConcern createWriteConcern(Map<String, List<String>> optionsMap) {
        String w2 = null;
        Integer wTimeout = null;
        Boolean safe = null;
        Boolean fsync = null;
        Boolean journal = null;
        for (String key : WRITE_CONCERN_KEYS) {
            String value = this.getLastValue(optionsMap, key);
            if (value == null) continue;
            if (key.equals("safe")) {
                safe = this.parseBoolean(value, "safe");
                continue;
            }
            if (key.equals("w")) {
                w2 = value;
                continue;
            }
            if (key.equals("wtimeoutms")) {
                wTimeout = Integer.parseInt(value);
                continue;
            }
            if (key.equals("fsync")) {
                fsync = this.parseBoolean(value, "fsync");
                continue;
            }
            if (!key.equals("journal")) continue;
            journal = this.parseBoolean(value, "journal");
        }
        return this.buildWriteConcern(safe, w2, wTimeout, fsync, journal);
    }

    @Nullable
    private ReadPreference createReadPreference(Map<String, List<String>> optionsMap) {
        String readPreferenceType = null;
        ArrayList<TagSet> tagSetList = new ArrayList<TagSet>();
        long maxStalenessSeconds = -1L;
        for (String key : READ_PREFERENCE_KEYS) {
            String value = this.getLastValue(optionsMap, key);
            if (value == null) continue;
            if (key.equals("readpreference")) {
                readPreferenceType = value;
                continue;
            }
            if (key.equals("maxstalenessseconds")) {
                maxStalenessSeconds = this.parseInteger(value, "maxstalenessseconds");
                continue;
            }
            if (!key.equals("readpreferencetags")) continue;
            for (String cur : optionsMap.get(key)) {
                TagSet tagSet = this.getTags(cur.trim());
                tagSetList.add(tagSet);
            }
        }
        return this.buildReadPreference(readPreferenceType, tagSetList, maxStalenessSeconds);
    }

    private UuidRepresentation createUuidRepresentation(String value) {
        if (value.equalsIgnoreCase("unspecified")) {
            return UuidRepresentation.UNSPECIFIED;
        }
        if (value.equalsIgnoreCase("javaLegacy")) {
            return UuidRepresentation.JAVA_LEGACY;
        }
        if (value.equalsIgnoreCase("csharpLegacy")) {
            return UuidRepresentation.C_SHARP_LEGACY;
        }
        if (value.equalsIgnoreCase("pythonLegacy")) {
            return UuidRepresentation.PYTHON_LEGACY;
        }
        if (value.equalsIgnoreCase("standard")) {
            return UuidRepresentation.STANDARD;
        }
        throw new IllegalArgumentException("Unknown uuid representation: " + value);
    }

    @Nullable
    private MongoCredential createCredentials(Map<String, List<String>> optionsMap, @Nullable String userName, @Nullable char[] password) {
        AuthenticationMechanism mechanism = null;
        String authSource = null;
        String gssapiServiceName = null;
        String authMechanismProperties = null;
        for (String key : AUTH_KEYS) {
            String value = this.getLastValue(optionsMap, key);
            if (value == null) continue;
            if (key.equals("authmechanism")) {
                mechanism = AuthenticationMechanism.fromMechanismName(value);
                continue;
            }
            if (key.equals("authsource")) {
                authSource = value;
                continue;
            }
            if (key.equals("gssapiservicename")) {
                gssapiServiceName = value;
                continue;
            }
            if (!key.equals("authmechanismproperties")) continue;
            authMechanismProperties = value;
        }
        MongoCredential credential = null;
        if (mechanism != null) {
            credential = this.createMongoCredentialWithMechanism(mechanism, userName, password, authSource, gssapiServiceName);
        } else if (userName != null) {
            credential = MongoCredential.createCredential(userName, this.getAuthSourceOrDefault(authSource, this.database != null ? this.database : "admin"), password);
        }
        if (credential != null && authMechanismProperties != null) {
            for (String part : authMechanismProperties.split(",")) {
                String[] mechanismPropertyKeyValue = part.split(":");
                if (mechanismPropertyKeyValue.length != 2) {
                    throw new IllegalArgumentException(String.format("The connection string contains invalid authentication properties. '%s' is not a key value pair", part));
                }
                String key = mechanismPropertyKeyValue[0].trim().toLowerCase();
                String value = mechanismPropertyKeyValue[1].trim();
                credential = key.equals("canonicalize_host_name") ? credential.withMechanismProperty(key, Boolean.valueOf(value)) : credential.withMechanismProperty(key, value);
            }
        }
        return credential;
    }

    private MongoCredential createMongoCredentialWithMechanism(AuthenticationMechanism mechanism, String userName, @Nullable char[] password, @Nullable String authSource, @Nullable String gssapiServiceName) {
        MongoCredential credential;
        String mechanismAuthSource;
        switch (mechanism) {
            case PLAIN: {
                mechanismAuthSource = this.getAuthSourceOrDefault(authSource, this.database != null ? this.database : "$external");
                break;
            }
            case GSSAPI: 
            case MONGODB_X509: {
                mechanismAuthSource = this.getAuthSourceOrDefault(authSource, "$external");
                if (mechanismAuthSource.equals("$external")) break;
                throw new IllegalArgumentException(String.format("Invalid authSource for %s, it must be '$external'", new Object[]{mechanism}));
            }
            default: {
                mechanismAuthSource = this.getAuthSourceOrDefault(authSource, this.database != null ? this.database : "admin");
            }
        }
        switch (mechanism) {
            case GSSAPI: {
                credential = MongoCredential.createGSSAPICredential(userName);
                if (gssapiServiceName != null) {
                    credential = credential.withMechanismProperty("SERVICE_NAME", gssapiServiceName);
                }
                if (password == null || !LOGGER.isWarnEnabled()) break;
                LOGGER.warn("Password in connection string not used with MONGODB_X509 authentication mechanism.");
                break;
            }
            case PLAIN: {
                credential = MongoCredential.createPlainCredential(userName, mechanismAuthSource, password);
                break;
            }
            case MONGODB_CR: {
                credential = MongoCredential.createMongoCRCredential(userName, mechanismAuthSource, password);
                break;
            }
            case MONGODB_X509: {
                if (password != null) {
                    throw new IllegalArgumentException("Invalid mechanism, MONGODB_x509 does not support passwords");
                }
                credential = MongoCredential.createMongoX509Credential(userName);
                break;
            }
            case SCRAM_SHA_1: {
                credential = MongoCredential.createScramSha1Credential(userName, mechanismAuthSource, password);
                break;
            }
            case SCRAM_SHA_256: {
                credential = MongoCredential.createScramSha256Credential(userName, mechanismAuthSource, password);
                break;
            }
            default: {
                throw new UnsupportedOperationException(String.format("The connection string contains an invalid authentication mechanism'. '%s' is not a supported authentication mechanism", new Object[]{mechanism}));
            }
        }
        return credential;
    }

    private String getAuthSourceOrDefault(@Nullable String authSource, String defaultAuthSource) {
        if (authSource != null) {
            return authSource;
        }
        return defaultAuthSource;
    }

    @Nullable
    private String getLastValue(Map<String, List<String>> optionsMap, String key) {
        List<String> valueList = optionsMap.get(key);
        if (valueList == null) {
            return null;
        }
        return valueList.get(valueList.size() - 1);
    }

    private Map<String, List<String>> parseOptions(String optionsPart) {
        String slaveok;
        HashMap<String, List<String>> optionsMap = new HashMap<String, List<String>>();
        if (optionsPart.length() == 0) {
            return optionsMap;
        }
        for (String part : optionsPart.split("&|;")) {
            if (part.length() == 0) continue;
            int idx = part.indexOf("=");
            if (idx >= 0) {
                String key = part.substring(0, idx).toLowerCase();
                String value = part.substring(idx + 1);
                ArrayList<String> valueList = (ArrayList<String>)optionsMap.get(key);
                if (valueList == null) {
                    valueList = new ArrayList<String>(1);
                }
                valueList.add(this.urldecode(value));
                optionsMap.put(key, valueList);
                continue;
            }
            throw new IllegalArgumentException(String.format("The connection string contains an invalid option '%s'. '%s' is missing the value delimiter eg '%s=value'", optionsPart, part, part));
        }
        if (optionsMap.containsKey("wtimeout") && !optionsMap.containsKey("wtimeoutms")) {
            optionsMap.put("wtimeoutms", (List<String>)optionsMap.remove("wtimeout"));
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Uri option 'wtimeout' has been deprecated, use 'wtimeoutms' instead.");
            }
        }
        if ((slaveok = this.getLastValue(optionsMap, "slaveok")) != null && !optionsMap.containsKey("readpreference")) {
            String readPreference = Boolean.TRUE.equals(this.parseBoolean(slaveok, "slaveok")) ? "secondaryPreferred" : "primary";
            optionsMap.put("readpreference", Collections.singletonList(readPreference));
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Uri option 'slaveok' has been deprecated, use 'readpreference' instead.");
            }
        }
        if (optionsMap.containsKey("j") && !optionsMap.containsKey("journal")) {
            optionsMap.put("journal", (List<String>)optionsMap.remove("j"));
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Uri option 'j' has been deprecated, use 'journal' instead.");
            }
        }
        return optionsMap;
    }

    @Nullable
    private ReadPreference buildReadPreference(@Nullable String readPreferenceType, List<TagSet> tagSetList, long maxStalenessSeconds) {
        if (readPreferenceType != null) {
            if (tagSetList.isEmpty() && maxStalenessSeconds == -1L) {
                return ReadPreference.valueOf(readPreferenceType);
            }
            if (maxStalenessSeconds == -1L) {
                return ReadPreference.valueOf(readPreferenceType, tagSetList);
            }
            return ReadPreference.valueOf(readPreferenceType, tagSetList, maxStalenessSeconds, TimeUnit.SECONDS);
        }
        if (!tagSetList.isEmpty() || maxStalenessSeconds != -1L) {
            throw new IllegalArgumentException("Read preference mode must be specified if either read preference tags or max staleness is specified");
        }
        return null;
    }

    @Nullable
    private WriteConcern buildWriteConcern(@Nullable Boolean safe, @Nullable String w2, @Nullable Integer wTimeout, @Nullable Boolean fsync, @Nullable Boolean journal) {
        WriteConcern retVal = null;
        if (w2 != null || wTimeout != null || fsync != null || journal != null) {
            if (w2 == null) {
                retVal = WriteConcern.ACKNOWLEDGED;
            } else {
                try {
                    retVal = new WriteConcern(Integer.parseInt(w2));
                } catch (NumberFormatException e2) {
                    retVal = new WriteConcern(w2);
                }
            }
            if (wTimeout != null) {
                retVal = retVal.withWTimeout(wTimeout.intValue(), TimeUnit.MILLISECONDS);
            }
            if (journal != null) {
                retVal = retVal.withJournal(journal);
            }
            if (fsync != null) {
                retVal = retVal.withFsync(fsync);
            }
            return retVal;
        }
        if (safe != null) {
            retVal = safe != false ? WriteConcern.ACKNOWLEDGED : WriteConcern.UNACKNOWLEDGED;
        }
        return retVal;
    }

    private TagSet getTags(String tagSetString) {
        ArrayList<Tag> tagList = new ArrayList<Tag>();
        if (tagSetString.length() > 0) {
            for (String tag : tagSetString.split(",")) {
                String[] tagKeyValuePair = tag.split(":");
                if (tagKeyValuePair.length != 2) {
                    throw new IllegalArgumentException(String.format("The connection string contains an invalid read preference tag. '%s' is not a key value pair", tagSetString));
                }
                tagList.add(new Tag(tagKeyValuePair[0].trim(), tagKeyValuePair[1].trim()));
            }
        }
        return new TagSet(tagList);
    }

    @Nullable
    private Boolean parseBoolean(String input, String key) {
        String trimmedInput = input.trim().toLowerCase();
        if (TRUE_VALUES.contains(trimmedInput)) {
            if (!trimmedInput.equals("true")) {
                LOGGER.warn(String.format("Deprecated boolean value '%s' in the connection string for '%s'. Replace with 'true'", trimmedInput, key));
            }
            return true;
        }
        if (FALSE_VALUES.contains(trimmedInput)) {
            if (!trimmedInput.equals("false")) {
                LOGGER.warn(String.format("Deprecated boolean value '%s' in the connection string for '%s'. Replace with'false'", trimmedInput, key));
            }
            return false;
        }
        LOGGER.warn(String.format("Ignoring unrecognized boolean value '%s' in the connection string for '%s'. Replace with either 'true' or 'false'", trimmedInput, key));
        return null;
    }

    private int parseInteger(String input, String key) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e2) {
            throw new IllegalArgumentException(String.format("The connection string contains an invalid value for '%s'. '%s' is not a valid integer", key, input));
        }
    }

    private List<String> parseHosts(List<String> rawHosts) {
        if (rawHosts.size() == 0) {
            throw new IllegalArgumentException("The connection string must contain at least one host");
        }
        ArrayList<String> hosts = new ArrayList<String>();
        for (String host : rawHosts) {
            if (host.length() == 0) {
                throw new IllegalArgumentException(String.format("The connection string contains an empty host '%s'. ", rawHosts));
            }
            if (host.endsWith(".sock")) {
                host = this.urldecode(host);
            } else if (host.startsWith("[")) {
                if (!host.contains("]")) {
                    throw new IllegalArgumentException(String.format("The connection string contains an invalid host '%s'. IPv6 address literals must be enclosed in '[' and ']' according to RFC 2732", host));
                }
                int idx = host.indexOf("]:");
                if (idx != -1) {
                    this.validatePort(host, host.substring(idx + 2));
                }
            } else {
                int colonCount = this.countOccurrences(host, ":");
                if (colonCount > 1) {
                    throw new IllegalArgumentException(String.format("The connection string contains an invalid host '%s'. Reserved characters such as ':' must be escaped according RFC 2396. Any IPv6 address literal must be enclosed in '[' and ']' according to RFC 2732.", host));
                }
                if (colonCount == 1) {
                    this.validatePort(host, host.substring(host.indexOf(":") + 1));
                }
            }
            hosts.add(host);
        }
        Collections.sort(hosts);
        return hosts;
    }

    private void validatePort(String host, String port) {
        boolean invalidPort = false;
        try {
            int portInt = Integer.parseInt(port);
            if (portInt <= 0 || portInt > 65535) {
                invalidPort = true;
            }
        } catch (NumberFormatException e2) {
            invalidPort = true;
        }
        if (invalidPort) {
            throw new IllegalArgumentException(String.format("The connection string contains an invalid host '%s'. The port '%s' is not a valid, it must be an integer between 0 and 65535", host, port));
        }
    }

    private int countOccurrences(String haystack, String needle) {
        return haystack.length() - haystack.replace(needle, "").length();
    }

    private String urldecode(String input) {
        return this.urldecode(input, false);
    }

    private String urldecode(String input, boolean password) {
        try {
            return URLDecoder.decode(input, UTF_8);
        } catch (UnsupportedEncodingException e2) {
            if (password) {
                throw new IllegalArgumentException("The connection string contained unsupported characters in the password.");
            }
            throw new IllegalArgumentException(String.format("The connection string contained unsupported characters: '%s'.Decoding produced the following error: %s", input, e2.getMessage()));
        }
    }

    @Nullable
    public String getUsername() {
        return this.credential != null ? this.credential.getUserName() : null;
    }

    @Nullable
    public char[] getPassword() {
        return this.credential != null ? this.credential.getPassword() : null;
    }

    public boolean isSrvProtocol() {
        return this.isSrvProtocol;
    }

    public List<String> getHosts() {
        return this.hosts;
    }

    @Nullable
    public String getDatabase() {
        return this.database;
    }

    @Nullable
    public String getCollection() {
        return this.collection;
    }

    @Deprecated
    public String getURI() {
        return this.getConnectionString();
    }

    public String getConnectionString() {
        return this.connectionString;
    }

    @Deprecated
    public List<MongoCredential> getCredentialList() {
        return this.credential != null ? Collections.singletonList(this.credential) : Collections.emptyList();
    }

    @Nullable
    public MongoCredential getCredential() {
        return this.credential;
    }

    @Nullable
    public ReadPreference getReadPreference() {
        return this.readPreference;
    }

    @Nullable
    public ReadConcern getReadConcern() {
        return this.readConcern;
    }

    @Nullable
    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }

    @Deprecated
    public boolean getRetryWrites() {
        return this.retryWrites == null ? true : this.retryWrites;
    }

    public Boolean getRetryWritesValue() {
        return this.retryWrites;
    }

    public Boolean getRetryReads() {
        return this.retryReads;
    }

    @Nullable
    public Integer getMinConnectionPoolSize() {
        return this.minConnectionPoolSize;
    }

    @Nullable
    public Integer getMaxConnectionPoolSize() {
        return this.maxConnectionPoolSize;
    }

    @Deprecated
    @Nullable
    public Integer getThreadsAllowedToBlockForConnectionMultiplier() {
        return this.threadsAllowedToBlockForConnectionMultiplier;
    }

    @Nullable
    public Integer getMaxWaitTime() {
        return this.maxWaitTime;
    }

    @Nullable
    public Integer getMaxConnectionIdleTime() {
        return this.maxConnectionIdleTime;
    }

    @Nullable
    public Integer getMaxConnectionLifeTime() {
        return this.maxConnectionLifeTime;
    }

    @Nullable
    public Integer getConnectTimeout() {
        return this.connectTimeout;
    }

    @Nullable
    public Integer getSocketTimeout() {
        return this.socketTimeout;
    }

    @Nullable
    public Boolean getSslEnabled() {
        return this.sslEnabled;
    }

    @Deprecated
    @Nullable
    public String getStreamType() {
        return this.streamType;
    }

    @Nullable
    public Boolean getSslInvalidHostnameAllowed() {
        return this.sslInvalidHostnameAllowed;
    }

    @Nullable
    public String getRequiredReplicaSetName() {
        return this.requiredReplicaSetName;
    }

    @Nullable
    public Integer getServerSelectionTimeout() {
        return this.serverSelectionTimeout;
    }

    @Nullable
    public Integer getLocalThreshold() {
        return this.localThreshold;
    }

    @Nullable
    public Integer getHeartbeatFrequency() {
        return this.heartbeatFrequency;
    }

    @Nullable
    public String getApplicationName() {
        return this.applicationName;
    }

    public List<MongoCompressor> getCompressorList() {
        return this.compressorList;
    }

    @Nullable
    public UuidRepresentation getUuidRepresentation() {
        return this.uuidRepresentation;
    }

    public String toString() {
        return this.connectionString;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (!(o2 instanceof ConnectionString)) {
            return false;
        }
        ConnectionString that = (ConnectionString)o2;
        if (this.collection != null ? !this.collection.equals(that.collection) : that.collection != null) {
            return false;
        }
        if (this.connectTimeout != null ? !this.connectTimeout.equals(that.connectTimeout) : that.connectTimeout != null) {
            return false;
        }
        if (this.credential != null ? !this.credential.equals(that.credential) : that.credential != null) {
            return false;
        }
        if (this.database != null ? !this.database.equals(that.database) : that.database != null) {
            return false;
        }
        if (!this.hosts.equals(that.hosts)) {
            return false;
        }
        if (this.maxConnectionIdleTime != null ? !this.maxConnectionIdleTime.equals(that.maxConnectionIdleTime) : that.maxConnectionIdleTime != null) {
            return false;
        }
        if (this.maxConnectionLifeTime != null ? !this.maxConnectionLifeTime.equals(that.maxConnectionLifeTime) : that.maxConnectionLifeTime != null) {
            return false;
        }
        if (this.maxConnectionPoolSize != null ? !this.maxConnectionPoolSize.equals(that.maxConnectionPoolSize) : that.maxConnectionPoolSize != null) {
            return false;
        }
        if (this.maxWaitTime != null ? !this.maxWaitTime.equals(that.maxWaitTime) : that.maxWaitTime != null) {
            return false;
        }
        if (this.minConnectionPoolSize != null ? !this.minConnectionPoolSize.equals(that.minConnectionPoolSize) : that.minConnectionPoolSize != null) {
            return false;
        }
        if (this.readPreference != null ? !this.readPreference.equals(that.readPreference) : that.readPreference != null) {
            return false;
        }
        if (this.requiredReplicaSetName != null ? !this.requiredReplicaSetName.equals(that.requiredReplicaSetName) : that.requiredReplicaSetName != null) {
            return false;
        }
        if (this.socketTimeout != null ? !this.socketTimeout.equals(that.socketTimeout) : that.socketTimeout != null) {
            return false;
        }
        if (this.sslEnabled != null ? !this.sslEnabled.equals(that.sslEnabled) : that.sslEnabled != null) {
            return false;
        }
        if (this.threadsAllowedToBlockForConnectionMultiplier != null ? !this.threadsAllowedToBlockForConnectionMultiplier.equals(that.threadsAllowedToBlockForConnectionMultiplier) : that.threadsAllowedToBlockForConnectionMultiplier != null) {
            return false;
        }
        if (this.writeConcern != null ? !this.writeConcern.equals(that.writeConcern) : that.writeConcern != null) {
            return false;
        }
        if (this.applicationName != null ? !this.applicationName.equals(that.applicationName) : that.applicationName != null) {
            return false;
        }
        return this.compressorList.equals(that.compressorList);
    }

    public int hashCode() {
        int result = this.credential != null ? this.credential.hashCode() : 0;
        result = 31 * result + this.hosts.hashCode();
        result = 31 * result + (this.database != null ? this.database.hashCode() : 0);
        result = 31 * result + (this.collection != null ? this.collection.hashCode() : 0);
        result = 31 * result + (this.readPreference != null ? this.readPreference.hashCode() : 0);
        result = 31 * result + (this.writeConcern != null ? this.writeConcern.hashCode() : 0);
        result = 31 * result + (this.minConnectionPoolSize != null ? this.minConnectionPoolSize.hashCode() : 0);
        result = 31 * result + (this.maxConnectionPoolSize != null ? this.maxConnectionPoolSize.hashCode() : 0);
        result = 31 * result + (this.threadsAllowedToBlockForConnectionMultiplier != null ? this.threadsAllowedToBlockForConnectionMultiplier.hashCode() : 0);
        result = 31 * result + (this.maxWaitTime != null ? this.maxWaitTime.hashCode() : 0);
        result = 31 * result + (this.maxConnectionIdleTime != null ? this.maxConnectionIdleTime.hashCode() : 0);
        result = 31 * result + (this.maxConnectionLifeTime != null ? this.maxConnectionLifeTime.hashCode() : 0);
        result = 31 * result + (this.connectTimeout != null ? this.connectTimeout.hashCode() : 0);
        result = 31 * result + (this.socketTimeout != null ? this.socketTimeout.hashCode() : 0);
        result = 31 * result + (this.sslEnabled != null ? this.sslEnabled.hashCode() : 0);
        result = 31 * result + (this.requiredReplicaSetName != null ? this.requiredReplicaSetName.hashCode() : 0);
        result = 31 * result + (this.applicationName != null ? this.applicationName.hashCode() : 0);
        result = 31 * result + this.compressorList.hashCode();
        return result;
    }

    static {
        GENERAL_OPTIONS_KEYS.add("minpoolsize");
        GENERAL_OPTIONS_KEYS.add("maxpoolsize");
        GENERAL_OPTIONS_KEYS.add("waitqueuemultiple");
        GENERAL_OPTIONS_KEYS.add("waitqueuetimeoutms");
        GENERAL_OPTIONS_KEYS.add("connecttimeoutms");
        GENERAL_OPTIONS_KEYS.add("maxidletimems");
        GENERAL_OPTIONS_KEYS.add("maxlifetimems");
        GENERAL_OPTIONS_KEYS.add("sockettimeoutms");
        GENERAL_OPTIONS_KEYS.add("ssl");
        GENERAL_OPTIONS_KEYS.add("tls");
        GENERAL_OPTIONS_KEYS.add("tlsinsecure");
        GENERAL_OPTIONS_KEYS.add("sslinvalidhostnameallowed");
        GENERAL_OPTIONS_KEYS.add("tlsallowinvalidhostnames");
        GENERAL_OPTIONS_KEYS.add("replicaset");
        GENERAL_OPTIONS_KEYS.add("readconcernlevel");
        GENERAL_OPTIONS_KEYS.add("streamtype");
        GENERAL_OPTIONS_KEYS.add("serverselectiontimeoutms");
        GENERAL_OPTIONS_KEYS.add("localthresholdms");
        GENERAL_OPTIONS_KEYS.add("heartbeatfrequencyms");
        GENERAL_OPTIONS_KEYS.add("retrywrites");
        GENERAL_OPTIONS_KEYS.add("retryreads");
        GENERAL_OPTIONS_KEYS.add("appname");
        GENERAL_OPTIONS_KEYS.add("uuidrepresentation");
        COMPRESSOR_KEYS.add("compressors");
        COMPRESSOR_KEYS.add("zlibcompressionlevel");
        READ_PREFERENCE_KEYS.add("readpreference");
        READ_PREFERENCE_KEYS.add("readpreferencetags");
        READ_PREFERENCE_KEYS.add("maxstalenessseconds");
        WRITE_CONCERN_KEYS.add("safe");
        WRITE_CONCERN_KEYS.add("w");
        WRITE_CONCERN_KEYS.add("wtimeoutms");
        WRITE_CONCERN_KEYS.add("fsync");
        WRITE_CONCERN_KEYS.add("journal");
        AUTH_KEYS.add("authmechanism");
        AUTH_KEYS.add("authsource");
        AUTH_KEYS.add("gssapiservicename");
        AUTH_KEYS.add("authmechanismproperties");
        ALL_KEYS.addAll(GENERAL_OPTIONS_KEYS);
        ALL_KEYS.addAll(AUTH_KEYS);
        ALL_KEYS.addAll(READ_PREFERENCE_KEYS);
        ALL_KEYS.addAll(WRITE_CONCERN_KEYS);
        ALL_KEYS.addAll(COMPRESSOR_KEYS);
        TRUE_VALUES = new HashSet<String>(Arrays.asList("true", "yes", "1"));
        FALSE_VALUES = new HashSet<String>(Arrays.asList("false", "no", "0"));
    }
}

