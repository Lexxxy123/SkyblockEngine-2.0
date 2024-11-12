/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection.tlschannel.util;

import javax.net.ssl.SSLEngineResult;

public class Util {
    public static void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError();
        }
    }

    public static String resultToString(SSLEngineResult result) {
        return String.format("status=%s,handshakeStatus=%s,bytesConsumed=%d,bytesConsumed=%d", new Object[]{result.getStatus(), result.getHandshakeStatus(), result.bytesProduced(), result.bytesConsumed()});
    }
}

