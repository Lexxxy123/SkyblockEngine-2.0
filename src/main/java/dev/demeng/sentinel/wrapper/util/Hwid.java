/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package dev.demeng.sentinel.wrapper.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hwid {
    private static final char[] HEX = "0123456789ABCDEF".toCharArray();

    public static String getHwid() {
        try {
            MessageDigest hash = MessageDigest.getInstance("MD5");
            String str = System.getProperty("os.name") + System.getProperty("os.arch") + System.getProperty("os.version") + Runtime.getRuntime().availableProcessors() + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_ARCHITECTURE") + System.getenv("PROCESSOR_ARCHITEW6432") + System.getenv("NUMBER_OF_PROCESSORS");
            return Hwid.bytesToHex(hash.digest(str.getBytes()));
        } catch (NoSuchAlgorithmException e2) {
            throw new IllegalArgumentException("Invalid algorithm: ", e2);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j2 = 0; j2 < bytes.length; ++j2) {
            int v2 = bytes[j2] & 0xFF;
            hexChars[j2 * 2] = HEX[v2 >>> 4];
            hexChars[j2 * 2 + 1] = HEX[v2 & 0xF];
        }
        return new String(hexChars);
    }
}

