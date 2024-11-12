/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import java.util.HashMap;
import java.util.Map;

public enum AuthenticationMechanism {
    GSSAPI("GSSAPI"),
    PLAIN("PLAIN"),
    MONGODB_X509("MONGODB-X509"),
    MONGODB_CR("MONGODB-CR"),
    SCRAM_SHA_1("SCRAM-SHA-1"),
    SCRAM_SHA_256("SCRAM-SHA-256");

    private static final Map<String, AuthenticationMechanism> AUTH_MAP;
    private final String mechanismName;

    private AuthenticationMechanism(String mechanismName) {
        this.mechanismName = mechanismName;
    }

    public String getMechanismName() {
        return this.mechanismName;
    }

    public String toString() {
        return this.mechanismName;
    }

    public static AuthenticationMechanism fromMechanismName(String mechanismName) {
        AuthenticationMechanism mechanism = AUTH_MAP.get(mechanismName);
        if (mechanism == null) {
            throw new IllegalArgumentException("Unsupported authMechanism: " + mechanismName);
        }
        return mechanism;
    }

    static {
        AUTH_MAP = new HashMap<String, AuthenticationMechanism>();
        for (AuthenticationMechanism value : AuthenticationMechanism.values()) {
            AUTH_MAP.put(value.getMechanismName(), value);
        }
    }
}

